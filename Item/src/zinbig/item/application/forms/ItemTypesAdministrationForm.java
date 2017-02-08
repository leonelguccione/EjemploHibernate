/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.ButtonPagingNavigator;
import zinbig.item.application.dataproviders.ItemTypeDTODataProvider;
import zinbig.item.application.pages.AddItemTypePage;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.EditItemTypePage;
import zinbig.item.application.pages.ItemTypesAdministrationPage;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.ItemTypeDTO;

/**
 * Las instancias de esta clase se utilizan para administrar los tipos de ítems.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ItemTypesAdministrationForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 7722736749032973483L;

	/**
	 * Es una colección de Oids de tipos de ítems que han sido seleccionados.
	 */
	private Collection<String> selectedItemTypes;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aPage
	 *            es la página a la que se ha agregado este formulario.
	 */
	public ItemTypesAdministrationForm(String anId,
			ItemTypesAdministrationPage aPage) {
		super(anId);

		// agrega el link para poder dar de alta un nuevo grupo de usuarios
		Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
				"newItemTypeLink", AddItemTypePage.class);
		this.add(aLink);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this
				.getString("itemTypesAdministrationForm.noItemTypeSelected"));
		messages
				.add(this
						.getString("itemTypesAdministrationForm.errorDeletingItemTypes"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedItemTypes(new HashSet<String>());

		CheckGroup<ItemTypeDTO> group = new CheckGroup<ItemTypeDTO>("group",
				new PropertyModel<Collection<ItemTypeDTO>>(this,
						"selectedItemTypes"));
		this.add(group);
		group.add(new CheckGroupSelector("groupselector"));

		SubmitLink deleteLink = new SubmitLink("deleteItemTypeLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el requerimiento para borrar los tipos de ítems
			 * seleccionados. En caso de que no se hayan seleccionado ítems se
			 * muestra un cartel de error al usuario.
			 */
			@Override
			public void onSubmit() {

				if (ItemTypesAdministrationForm.this
						.getSelectedItemTypesCount() == 0) {
					this
							.error(this
									.getString("itemTypesAdministrationForm.noItemTypeSelected"));
				} else {
					try {
						ItemTypesAdministrationForm.this.getItemsService()
								.deleteItemTypes(
										ItemTypesAdministrationForm.this
												.getSelectedItemTypes());
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("itemTypesAdministrationForm.errorDeletingItemTypes"));
					}
				}
			}

		};

		this.add(deleteLink);

		// recupera las preferencias de ordenamiento del usuario
		String columnName = this.getUserDTO().getUserPreference(
				Constants.ITEM_TYPES_ADMINISTRATION_COLUMN_NAME);
		String columnOrder = this.getUserDTO().getUserPreference(
				Constants.ITEM_TYPES_ADMINISTRATION_COLUMN_ORDER);

		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.ITEM_TYPES_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.ITEM_TYPES_ADMINISTRATION_COLUMN_ORDER);
		}

		final ItemTypeDTODataProvider dataProvider = new ItemTypeDTODataProvider(
				columnName, columnOrder);

		// crea el componente para el listado de los tipos de ítems
		// existentes
		final DataView<ItemTypeDTO> dataView = new DataView<ItemTypeDTO>(
				"pageable", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Define el contenido de cada una de las filas del listado.
			 * 
			 * @param item
			 *            es objeto que contiene la información que se utilizará
			 *            para armar la fila.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(final Item<ItemTypeDTO> item) {
				ItemTypeDTO dto = item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				check.setEnabled(dto.getReferencesCount() == 0);

				PageParameters parameters = new PageParameters();
				parameters.put("itemTypeOid", Utils.encodeString(dto.getOid()));
				Link aLink = new BookmarkablePageLink("editLink",
						EditItemTypePage.class, parameters);

				aLink.add(new Label("title", dto.getTitle()));
				item.add(aLink);

			}
		};

		dataView.setItemsPerPage(aPage.getItemsPerPage());

		// agrega el link para ordenar por el título de los tipos de ítems.
		OrderByLink sortByTitleLink = new OrderByLink("sortByTitleLink",
				"title", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEM_TYPES_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEM_TYPES_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el checkbox group.
		group.add(dataView);

		// agrega el link para ordenar por el nombre.
		group.add(sortByTitleLink);

		// agrega el componente de navegación
		ButtonPagingNavigator navigator = new ButtonPagingNavigator(
				"navigator", dataView);
		this.add(navigator);

		if (dataProvider.size() > aPage.getItemsPerPage()) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

		// agrega el componente que muestra la cantidad de elementos.
		Label countLabel = new Label("count", new Long(dataProvider.size())
				.toString());
		this.add(countLabel);
	}

	/**
	 * Actualiza el componente de navegación para verificar que hay más páginas
	 * para mostrar tomando como entrada la cantidad de elementos que se
	 * muestran en la página. Si la cantidad de elementos que se deben mostrar
	 * es mayor a la cantidad total de elementos, el navegador no aparece.
	 * 
	 * @param aNumber
	 *            es el número de elementos a mostrar por página.
	 */
	public void updateItemsPerPage(int aNumber) {

		DataView<ItemTypeDTO> dataView = this.getDataView();
		ButtonPagingNavigator navigator = (ButtonPagingNavigator) this
				.get("navigator");

		dataView.setItemsPerPage(aNumber);

		if (dataView.getDataProvider().size() > aNumber) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los oids de los tipos de ítems
	 *         seleccionados.
	 */
	public Collection<String> getSelectedItemTypes() {
		return this.selectedItemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param someItemTypes
	 *            es una colección que contiene los oids de los tipos de ítems
	 *            seleccionados.
	 */
	public void setSelectedItemTypes(Collection<String> someItemTypes) {
		this.selectedItemTypes = someItemTypes;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de tipos de ítems seleccionados.
	 */
	protected int getSelectedItemTypesCount() {
		return this.getSelectedItemTypes().size();
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para armar el listado.
	 */
	@SuppressWarnings("unchecked")
	public DataView<ItemTypeDTO> getDataView() {
		return (DataView<ItemTypeDTO>) ((CheckGroup<ItemTypeDTO>) this
				.get("group")).get("pageable");
	}

}
