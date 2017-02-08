/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.ItemSession;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.menu.Menu;
import zinbig.item.util.menu.MenuItem;
import zinbig.item.util.menu.MenuItemComparator;

/**
 * Las instancias de esta clase se utilizan para "dibujar" un submen� con las
 * operaciones asignadas al usuario. <br>
 * En el caso de que no exista un usuario en la sesi�n se muestran operaciones
 * que no requieren de un usuario para su ejecuci�n; en su mayor�a son
 * operaciones de s�lo consulta.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SubmenuPanel extends AbstractMenu {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -9190977487295352048L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 */
	public SubmenuPanel(String anId, Collection<OperationDTO> someOperations) {
		super(anId, someOperations);

	}

	/**
	 * Convierte los dtos de las operaciones en �tems de men�.
	 * 
	 * 
	 * @param operationsList
	 *            es la lista de dtos de operaciones.
	 * @return una lista de �tems de men� para cada una de las operaciones.
	 */
	protected List<Menu> convertToMenuList(
			Collection<OperationDTO> operationsList) {

		ItemSession session = (ItemSession) this.getSession();
		List<Menu> menues = null;
		if (session.getMenu().iterator().hasNext()
				&& session.getMenu().iterator().next().getMenuItems().isEmpty()) {
			// crea el diccionario por categor�as
			Map<String, List<OperationDTO>> menuItems = this
					.prepareMenuItems(operationsList);
			// crea los items de men�.
			menues = this.createMenuItems(menuItems);
			session.setMenu(menues);
		} else {
			menues = session.getMenu();
		}

		return menues;
	}

	/**
	 * Recorre la lista de operaci�n y arma un diccionario cuyas claves son los
	 * t�tulos de los men�es y los valores son todas las operaciones que
	 * pertenecen a dicha categor�a de men�.
	 * 
	 * @param operationsList
	 *            es la lista de dtos que representan a las operaciones.
	 * @return un diccionario con t�tulos de categor�as como claves y una lista
	 *         de dtos de operaciones como valor de cada entrada.
	 */
	private Map<String, List<OperationDTO>> prepareMenuItems(
			Collection<OperationDTO> operationsList) {
		Map<String, List<OperationDTO>> menuItems = new HashMap<String, List<OperationDTO>>();
		Iterator<OperationDTO> iterator = operationsList.iterator();
		OperationDTO dto = null;
		while (iterator.hasNext()) {

			dto = iterator.next();
			if (dto.isVisibleInMenu()) {
				if (!menuItems.containsKey(dto.getCategoryName())) {
					menuItems.put(dto.getCategoryName(),
							new ArrayList<OperationDTO>());
				}
				((List<OperationDTO>) menuItems.get(dto.getCategoryName()))
						.add(dto);
			}
		}

		return menuItems;
	}

	/**
	 * Recorre el diccionario recibido creando una lista de �tems de men� que
	 * tienen como t�tulo el nombre de la categor�a.
	 * 
	 * @param menuItems
	 *            es el diccionario que contiene los t�tulos de las categor�as y
	 *            los dtos de las operaciones.
	 * @return una lista de men�es.
	 */
	private List<Menu> createMenuItems(Map<String, List<OperationDTO>> menuItems) {
		OperationDTO dto = null;

		List<Menu> menues = new ArrayList<Menu>();

		Iterator<Map.Entry<String, List<OperationDTO>>> entries = menuItems
				.entrySet().iterator();

		Map.Entry<String, List<OperationDTO>> entry = null;
		Menu aMenu = null;
		Iterator<OperationDTO> menuItemsIterator = null;
		List<MenuItem> aux = null;
		int currentCountOfProject = 0;
		int projectsCount = new Integer(((ItemApplication) this
				.getApplication()).getSystemProperty("MAX_PROJECTS_IN_MENU"))
				.intValue();
		int currentCountOfFilters = 0;
		int filtersCount = new Integer(
				((ItemApplication) this.getApplication())
						.getSystemProperty("MAX_FILTERS_IN_MENU")).intValue();

		MenuItem item = null;
		boolean shouldAddItem = false;

		while (entries.hasNext()) {
			entry = entries.next();

			aMenu = new Menu(this.getString(entry.getKey().toString()), this
					.getLocale());

			menuItemsIterator = ((List<OperationDTO>) entry.getValue())
					.iterator();
			aux = new ArrayList<MenuItem>();

			while (menuItemsIterator.hasNext()) {
				// recorro la lista de dtos de operaciones
				dto = (OperationDTO) menuItemsIterator.next();

				item = new MenuItem(this.getLocale());
				item.setTargetPageClassName(dto.getTargetPageClassName());
				item.setMenuSection(dto.getMenuSection());

				if (dto.getParameters().containsKey("PROJECT_MENU_TITLE")) {
					// se trata de un �tem de men� de un proyecto. Hay que
					// verificar que solamente se agreguen tantos �tems como lo
					// establezca la configuraci�n del sistema.

					item
							.setTitle(dto.getParameters().get(
									"PROJECT_MENU_TITLE"));
					item.addParameter("PROJECT_OID", dto.getParameters().get(
							"PROJECT_OID"));
					if (currentCountOfProject < projectsCount) {
						// agrega el �tem de men� de un proyecto si todav�a no
						// se super� la cantidad definida en la propiedad de
						// sistema.
						shouldAddItem = true;
						currentCountOfProject++;
					}
				} else {
					if (dto.getParameters().containsKey("FILTER_MENU_TITLE")) {
						// se trata de un �tem de men� correspondiente a un
						// filtro.
						item.setTitle(dto.getParameters().get(
								"FILTER_MENU_TITLE"));
						item.addParameter("FILTER_OID", dto.getParameters()
								.get("FILTER_OID"));
						if (currentCountOfFilters < filtersCount) {
							// agrega el �tem de men� del filtro en caso de que
							// no se haya superado la cantidad m�xima definida
							// por la propiedad de sistema.
							shouldAddItem = true;
							currentCountOfFilters++;
						}
					} else {
						// se trata de un �tem de men� de una operaci�n normal.
						// Se agrega directamente.
						item.setTitle(this.getString(dto.getName()));
						shouldAddItem = true;
					}
				}
				if (shouldAddItem) {// agrega el �tem de men� en caso de que
					// est� permitido.
					aux.add(item);
					shouldAddItem = false;
				}

			}

			Collections.sort(aux, new MenuItemComparator());
			aMenu.setMenuItems(aux);
			menues.add(aMenu);
		}

		Collections.sort(menues);

		return menues;
	}

	/**
	 * Actualiza el men�. <br>
	 * Esto generalmente se debe a cambios en el idioma de la interfaz, por lo
	 * que se deben volver a crear todos los �tems del men�.
	 */
	public void updateMenu() {

		if (this.getSession() != null
				&& ((ItemSession) this.getSession()).getUserDTO() != null) {
			this.setOperations(((ItemSession) this.getSession()).getUserDTO()
					.getOperations());
			super.updateMenu("menuitems", false);
		} else {
			this.setResponsePage(DashboardPage.class);
		}

	}

	/**
	 * Crea el componente que itera sobre los �tems de men� y arma el men�.
	 * 
	 * @param aList
	 *            es la lista de elementos que componen el men�.
	 * @return el componente creado.
	 */
	@SuppressWarnings("unchecked")
	protected ListView createListView(List<Menu> aList) {
		return new ListView("menuitems", aList) {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Itera sobre la colecci�n de elementos de la lista dibujando las
			 * categor�as de los �tems de men�.
			 */
			@Override
			public void populateItem(final ListItem listItem) {
				final Menu aMenu = (Menu) listItem.getModelObject();

				List menuItems = (List) aMenu.getMenuItems();

				listItem.add(new AttributeModifier("id", true, new Model(aMenu
						.getTitle())));
				ListView operations = new ListView("operations", menuItems) {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem arg0) {

						MenuItem menuItem = (MenuItem) arg0.getModelObject();

						PageParameters pageParameters = new PageParameters();
						String title = menuItem.getTitle();
						if (menuItem.hasParameters()) {

							pageParameters.put("PROJECT_OID", menuItem
									.getParameters().get("PROJECT_OID"));
							pageParameters.put("FILTER_OID", menuItem
									.getParameters().get("FILTER_OID"));
						}
						Link aLink = new BookmarkablePageLink("value", menuItem
								.getTargetPageClass(), pageParameters);
						aLink.setOutputMarkupId(true);
						aLink.add(new Label("aText", title));

						arg0.add(aLink);

					}

				};
				operations.setRenderBodyOnly(true);
				listItem.add(operations);

			}

		};

	}

}
