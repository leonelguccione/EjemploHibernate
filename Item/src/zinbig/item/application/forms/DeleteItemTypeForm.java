/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.ManageItemTypesPanel;
import zinbig.item.application.pages.BasePage;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de baja un
 * tipo de ítem de un proyecto. <BR>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DeleteItemTypeForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 8569024715647057471L;

	/**
	 * Es el dto del proyecto que se está editando..
	 */
	public ProjectDTO projectDTO;

	/**
	 * Mantiene la colección de dtos de los tipos de ítems seleccionados para
	 * ser eliminados.
	 */
	public Collection<String> selectedItemTypes;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param aProjectDTO
	 *            es el dto del proyecto que se está editando.
	 */
	@SuppressWarnings("unchecked")
	public DeleteItemTypeForm(String anId, ProjectDTO aProjectDTO) {
		super(anId);

		this.setProjectDTO(aProjectDTO);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this.getString("manageItemTypesPanel.noItemTypeSelected"));
		messages.add(this
				.getString("manageItemTypesPanel.errorDeletingItemTypes"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedItemTypes(new ArrayList<String>());

		// crea el componente para el listado de los tipos de ítems existentes
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedItemTypes());
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = null;
		String columnOrder = null;

		if (this.getUserDTO() != null) {
			columnName = this.getUserDTO().getUserPreference(
					Constants.MANAGE_ITEM_TYPES_COLUMN_NAME);
			columnOrder = this.getUserDTO().getUserPreference(
					Constants.MANAGE_ITEM_TYPES_COLUMN_ORDER);
		}
		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_ORDER);
		}

		final SortableDataProvider<ItemTypeDTO> provider = new SortableDataProvider<ItemTypeDTO>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Getter.
			 * 
			 * @param index
			 *            es un entero que indica a partir de que posición se
			 *            debe iterar.
			 * @param count
			 *            es un entero que indica la cantidad de posiciones que
			 *            se deben iterar.
			 */
			@Override
			public Iterator<ItemTypeDTO> iterator(int index, int count) {

				return getItemTypes(this.getSort().getProperty(),
						this.getSort().isAscending()).iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param anItemTypeDTO
			 *            es el dto que representa a una de los tipos de ítems
			 *            que se están iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<ItemTypeDTO> model(ItemTypeDTO anItemTypeDTO) {
				return new Model<ItemTypeDTO>(anItemTypeDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return getItemTypes(this.getSort().getProperty(),
						this.getSort().isAscending()).size();
			}
		};
		provider.setSort(columnName, columnOrder.equals("ASC"));

		// crea el componente para listar los tipos de ítems.
		final DataView itemTypes = new DataView("pageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el título del tipo de ítem y la cantidad
			 * de referencias que posee.
			 */
			@Override
			protected void populateItem(Item item) {
				ItemTypeDTO dto = (ItemTypeDTO) item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);
				check.setEnabled(dto.getReferencesCount() == 0);

				Label aLabel = new Label("title", dto.getTitle());
				item.add(aLabel);

				Label referencesLabel = new Label("referencesCount", new Long(
						dto.getReferencesCount()).toString());
				item.add(referencesLabel);

			}

		};

		// agrega el link para ordenar por el nombre de los tipos de ítems.
		OrderByLink sortByTitleLink = new OrderByLink("sortByTitleLink",
				columnName, provider) {

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
				itemTypes.setCurrentPage(0);
				SortParam sp = provider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.MANAGE_ITEM_TYPES_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.MANAGE_ITEM_TYPES_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el título.
		group.add(sortByTitleLink);

		group.add(itemTypes);
		this.add(group);

		// construye el link de borrado de tipos de ítems.
		SubmitLink deleteLink = new SubmitLink("deleteItemTypesLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Borra los tipos de ítems seleccionados.
			 */
			@Override
			public void onSubmit() {

				if (!DeleteItemTypeForm.this.getSelectedItemTypes().isEmpty()) {
					try {
						DeleteItemTypeForm.this
								.getItemsService()
								.deleteItemTypeOfProject(
										DeleteItemTypeForm.this.getProjectDTO(),
										DeleteItemTypeForm.this
												.getSelectedItemTypes());

						((ManageItemTypesPanel) DeleteItemTypeForm.this
								.getParent()).updateListOfItemTypes();
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("manageItemTypesPanel.errorDeletingItemTypes"));
					}

				} else {
					this
							.error(this
									.getString("manageItemTypesPanel.noItemTypeSelected"));
				}
			}

		};
		this.add(deleteLink);

	}

	/**
	 * Recupera la colección de tipos de ítems correspondiente al proyecto que
	 * se está editando.
	 * 
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * 
	 * @return una colección que contiene DTOs para cada uno de los tipos de
	 *         ítems.
	 */
	private List<ItemTypeDTO> getItemTypes(String aPropertyName,
			boolean isAscending) {
		ArrayList<ItemTypeDTO> result = new ArrayList<ItemTypeDTO>();

		try {
			ItemsServiceBI service = this.getItemsService();
			result.addAll(service.findItemTypesOfProject(this.getProjectDTO(),
					aPropertyName, isAscending ? "ASC" : "DESC"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return la colección de dtos de los tipos de ítems seleccionados.
	 */
	public Collection<String> getSelectedItemTypes() {
		return this.selectedItemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de dtos de los tipos de ítems seleccionados.
	 */
	public void setSelectedItemTypes(Collection<String> aCollection) {
		this.selectedItemTypes = aCollection;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto que se está editando.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el dto del proyecto que se está editando.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

}
