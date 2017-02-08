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

import zinbig.item.application.components.ManagePrioritiesPanel;
import zinbig.item.application.pages.BasePage;
import zinbig.item.services.bi.PrioritiesServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.PriorityDTO;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de baja una
 * prioridad dentro de un conjunto de prioridades. <BR>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DeletePriorityForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8286849616877371594L;

	/**
	 * Es el id del conjunto de prioridades del cual se debe quitar la
	 * prioridad.
	 */
	public String prioritySetId;

	/**
	 * Mantiene la colección de dtos de prioridades seleccionados para ser
	 * eliminados.
	 */
	public Collection<String> selectedPriorities;

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
	public DeletePriorityForm(String anId, String aPrioritySetId) {
		super(anId);

		this.setPrioritySetId(aPrioritySetId);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this.getString("listPrioritiesPanel.noPrioritySelected"));
		messages.add(this
				.getString("listPrioritiesPanel.errorDeletingPriorities"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedPriorities(new ArrayList<String>());

		// crea el componente para el listado de las prioridades existentes
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedPriorities());
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = null;
		String columnOrder = null;

		if (this.getUserDTO() != null) {
			columnName = this.getUserDTO().getUserPreference(
					Constants.MANAGE_PRIORITIES_COLUMN_NAME);
			columnOrder = this.getUserDTO().getUserPreference(
					Constants.MANAGE_PRIORITIES_COLUMN_ORDER);
		}
		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.MANAGE_PRIORITIES_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.MANAGE_PRIORITIES_COLUMN_ORDER);
		}

		final SortableDataProvider<PriorityDTO> provider = new SortableDataProvider<PriorityDTO>() {

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
			public Iterator<PriorityDTO> iterator(int index, int count) {

				return getPriorities(this.getSort().getProperty(),
						this.getSort().isAscending()).iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aPriorityDTO
			 *            es el dto que representa a una de las prioridades que
			 *            se está iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<PriorityDTO> model(PriorityDTO aPriorityDTO) {
				return new Model<PriorityDTO>(aPriorityDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return getPriorities(this.getSort().getProperty(),
						this.getSort().isAscending()).size();
			}
		};

		provider.setSort(columnName, columnOrder.equals("ASC"));

		// crea el componente para listar las prioridades.
		final DataView priorities = new DataView("pageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el nombre de la prioridad, el valor y la
			 * imagen.
			 */
			@Override
			protected void populateItem(Item item) {
				PriorityDTO dto = (PriorityDTO) item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);
				check.setEnabled(dto.getReferencesCount() == 0);

				Label aLabel = new Label("name", dto.getName());
				item.add(aLabel);

				Label valueLabel = new Label("value", dto.getValue());
				item.add(valueLabel);

				Label referencesLabel = new Label("referencesCount", new Long(
						dto.getReferencesCount()).toString());
				item.add(referencesLabel);

			}

		};

		// agrega el link para ordenar por el nombre de los proyectos.
		OrderByLink sortByNameLink = new OrderByLink("sortByNameLink",
				"p.title", provider) {

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
				priorities.setCurrentPage(0);
				SortParam sp = provider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.MANAGE_PRIORITIES_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.MANAGE_PRIORITIES_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre.
		group.add(sortByNameLink);

		group.add(priorities);
		this.add(group);

		// construye el link de borrado de prioridades.
		SubmitLink deleteLink = new SubmitLink("deletePrioritiesLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Borra las prioridades seleccionadas.
			 */
			@Override
			public void onSubmit() {

				if (!DeletePriorityForm.this.getSelectedPriorities().isEmpty()) {
					try {
						DeletePriorityForm.this.getPrioritiesService()
								.deletePrioritiesOfPrioritySet(
										DeletePriorityForm.this
												.getPrioritySetId(),
										DeletePriorityForm.this
												.getSelectedPriorities());

						((ManagePrioritiesPanel) DeletePriorityForm.this
								.getParent()).updateListOfPriorities();
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("listPrioritiesPanel.errorDeletingPriorities"));
					}

				} else {
					this
							.error(this
									.getString("listPrioritiesPanel.noPrioritySelected"));
				}
			}

		};
		this.add(deleteLink);

	}

	/**
	 * Getter.
	 * 
	 * @return el id del conjunto de prioridades al cual se está agregando la
	 *         nueva prioridad.
	 */
	public String getPrioritySetId() {
		return this.prioritySetId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id del conjunto de prioridades al cual se está agregando
	 *            la nueva prioridad.
	 */
	public void setPrioritySetId(String anId) {
		this.prioritySetId = anId;
	}

	/**
	 * Recupera la colección de prioridades correspondiente al conjunto de
	 * prioridades.
	 * 
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultad.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * 
	 * @return una colección que contiene DTOs para cada una de las prioridades.
	 */
	private List<PriorityDTO> getPriorities(String aPropertyName,
			boolean isAscending) {
		ArrayList<PriorityDTO> result = new ArrayList<PriorityDTO>();

		try {
			PrioritiesServiceBI aService = this.getPrioritiesService();
			result.addAll(aService.getPriorities(this.getPrioritySetId(),
					aPropertyName, isAscending));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return la colección de dtos de las prioridades seleccionadas para ser
	 *         eliminadas.
	 */
	public Collection<String> getSelectedPriorities() {
		return this.selectedPriorities;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de dtos de las prioridades seleccionadas para
	 *            ser eliminadas.
	 */
	public void setSelectedPriorities(Collection<String> aCollection) {
		this.selectedPriorities = aCollection;
	}

}
