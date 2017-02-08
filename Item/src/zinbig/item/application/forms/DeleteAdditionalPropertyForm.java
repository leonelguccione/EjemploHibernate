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

import zinbig.item.application.components.ManageAdditionalPropertiesPanel;
import zinbig.item.application.pages.BasePage;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de baja una
 * propiedad adicional de un proyecto. <BR>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DeleteAdditionalPropertyForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 6881699686218912520L;

	/**
	 * Es el DTO que representa al proyecto que se está editando.
	 */
	public ProjectDTO projectDTO;

	/**
	 * Mantiene la colección de las propiedades adicionales seleccionadas para
	 * ser eliminadas.
	 */
	public Collection<String> selectedProperties;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades del cual se deben quitar
	 *            laa prioridades.
	 */
	@SuppressWarnings("unchecked")
	public DeleteAdditionalPropertyForm(String anId, ProjectDTO aProjectDTO) {
		super(anId);

		this.setProjectDTO(aProjectDTO);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this
				.getString("listAdditionalPropertiesPanel.noPropertySelected"));
		messages
				.add(this
						.getString("listAdditionalPropertiesPanel.errorDeletingProperties"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedProperties(new ArrayList<String>());

		// crea el componente para el listado de las propiedades existentes
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedProperties());
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = null;
		String columnOrder = null;

		if (this.getUserDTO() != null) {
			columnName = this.getUserDTO().getUserPreference(
					Constants.MANAGE_PROPERTIES_COLUMN_NAME);
			columnOrder = this.getUserDTO().getUserPreference(
					Constants.MANAGE_PROPERTIES_COLUMN_ORDER);
		}
		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.MANAGE_PROPERTIES_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.MANAGE_PROPERTIES_COLUMN_ORDER);
		}

		final SortableDataProvider<PropertyDescriptionDTO> provider = new SortableDataProvider<PropertyDescriptionDTO>() {

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
			public Iterator<PropertyDescriptionDTO> iterator(int index,
					int count) {

				return getAdditionalProperties(this.getSort().getProperty(),
						this.getSort().isAscending()).iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aPriorityDTO
			 *            es el dto que representa a una de las propiedades
			 *            adicionales que se está iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<PropertyDescriptionDTO> model(
					PropertyDescriptionDTO aPropertyDescriptionDTO) {
				return new Model<PropertyDescriptionDTO>(
						aPropertyDescriptionDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return getAdditionalProperties(this.getSort().getProperty(),
						this.getSort().isAscending()).size();
			}
		};

		provider.setSort(columnName, columnOrder.equals("ASC"));

		// crea el componente para listar las propiedades.
		final DataView properties = new DataView("pageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el nombre de la propiedad y su tipo.
			 */
			@Override
			protected void populateItem(Item item) {
				PropertyDescriptionDTO dto = (PropertyDescriptionDTO) item
						.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				Label aLabel = new Label("name", dto.getName());
				item.add(aLabel);

				String type = "";

				switch (dto.getPropertyType()) {
				case 'S':
					type = this.getString("SIMPLE");
					break;
				case 'T':
					type = this.getString("TEXTAREA");
					break;
				case 'F':
					type = this.getString("FIXED");
					break;
				case 'D':
					type = this.getString("DYNAMIC");
					break;
				default:
					break;
				}

				Label valueLabel = new Label("type", type);
				item.add(valueLabel);

			}

		};

		// agrega el link para ordenar por el nombre de las propiedades.
		OrderByLink sortByNameLink = new OrderByLink("sortByNameLink", "title",
				provider) {

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
				properties.setCurrentPage(0);
				SortParam sp = provider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.MANAGE_PROPERTIES_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.MANAGE_PROPERTIES_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre.
		group.add(sortByNameLink);

		group.add(properties);
		this.add(group);

		// construye el link de borrado de propiedades.
		SubmitLink deleteLink = new SubmitLink("deletePropertiesLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Borra las propiedades seleccionadas.
			 */
			@Override
			public void onSubmit() {

				if (!DeleteAdditionalPropertyForm.this.getSelectedProperties()
						.isEmpty()) {
					try {
						DeleteAdditionalPropertyForm.this.getProjectsService()
								.deletePropertyDescriptionsOfProject(
										DeleteAdditionalPropertyForm.this
												.getProjectDTO(),
										DeleteAdditionalPropertyForm.this
												.getSelectedProperties());

						((ManageAdditionalPropertiesPanel) DeleteAdditionalPropertyForm.this
								.getParent()).updateListOfProperties();
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("listAdditionalPropertiesPanel.errorDeletingProperties"));
					}

				} else {
					this
							.error(this
									.getString("listAdditionalPropertiesPanel.noPropertySelected"));
				}
			}

		};
		this.add(deleteLink);

	}

	/**
	 * Getter.
	 * 
	 * @return el DTO que representa al proyecto que se está editando.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto que se está editando.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de identificadores de las propiedades adicionales
	 *         seleccionadas para ser eliminadas.
	 */
	public Collection<String> getSelectedProperties() {
		return this.selectedProperties;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de identificadores de las propiedades
	 *            adicionales seleccionadas para ser eliminadas.
	 */
	public void setSelectedProperties(Collection<String> aCollection) {
		this.selectedProperties = aCollection;
	}

	/**
	 * Recupera la colección de propiedades adicionales correspondientes al
	 * proyecto.
	 * 
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * 
	 * @return una colección que contiene DTOs para cada una de las propiedades.
	 */
	private List<PropertyDescriptionDTO> getAdditionalProperties(
			String aPropertyName, boolean isAscending) {
		ArrayList<PropertyDescriptionDTO> result = new ArrayList<PropertyDescriptionDTO>();

		try {
			ProjectsServiceBI aService = this.getProjectsService();
			result.addAll(aService.getPropertyDescriptionsOfProject(this
					.getProjectDTO(), aPropertyName, isAscending));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
