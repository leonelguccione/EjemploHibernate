/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemSession;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.menu.Menu;

/**
 * Las instancias de esta clase se utilizan para "dibujar" el men� con las
 * operaciones asignadas al usuario. <br>
 * En el caso de que no exista un usuario en la sesi�n se muestran operaciones
 * que no requieren de un usuario para su ejecuci�n; en su mayor�a son
 * operaciones de s�lo consulta.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MenuPanel extends AbstractMenu {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 3231232132132L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 * @param someOperations
	 *            es una colecci�n que contiene las operaciones que deben ser
	 *            consideradas para armar este men�.
	 */

	public MenuPanel(String anId, Collection<OperationDTO> someOperations) {
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

		if (session.getMenu() == null) {
			// crea el diccionario por categor�as
			Set<String> menuItems = this.prepareMenuItems(operationsList);

			// crea los items de men�.
			menues = this.createMenuItems(menuItems);
			session.setMenu(menues);
		} else {
			menues = session.getMenu();
		}

		return menues;
	}

	/**
	 * Recorre la lista de operaci�n y arma una colecci�n con los t�tulos de los
	 * men�es.
	 * 
	 * @param operationsList
	 *            es la lista de dtos que representan a las operaciones.
	 * @return un conjunto con t�tulos de categor�as.
	 */
	private Set<String> prepareMenuItems(Collection<OperationDTO> operationsList) {

		Set<String> menuItems = new TreeSet<String>();

		Iterator<OperationDTO> iterator = operationsList.iterator();
		OperationDTO dto = null;
		while (iterator.hasNext()) {

			dto = iterator.next();
			if (!menuItems.contains(dto.getCategoryName())) {
				menuItems.add(dto.getCategoryName());
			}

		}

		return menuItems;
	}

	/**
	 * Recorre el conjunto recibido creando una lista de �tems de men� que
	 * tienen como t�tulo el nombre de la categor�a.
	 * 
	 * @param menuItems
	 *            es el conjunto que contiene los t�tulos de las categor�as y
	 *            los dtos de las operaciones.
	 * @return una lista de men�es.
	 */
	private List<Menu> createMenuItems(Set<String> menuItems) {

		List<Menu> menues = new ArrayList<Menu>();

		Iterator<String> entries = menuItems.iterator();
		String entry = null;

		Menu aMenu = null;

		while (entries.hasNext()) {
			entry = entries.next();

			aMenu = new Menu(this.getString(entry), this.getLocale());

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

		super.updateMenu("menu", true);

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
		ListView aListView = new ListView("menu", aList) {
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
				listItem.setRenderBodyOnly(true);
				final Menu aMenu = (Menu) listItem.getModelObject();

				Label aLabel = new Label("text", " " + aMenu.getTitle());

				Link aLink = new BookmarkablePageLink("menuTitle",
						DashboardPage.class);
				aLink.setOutputMarkupId(true);

				aLink.add(new AttributeModifier("rel", true, new Model(aMenu
						.getTitle())));
				aLink.add(aLabel);
				listItem.add(aLink);

			}

		};
		return aListView;

	}

}
