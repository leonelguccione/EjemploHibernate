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

import zinbig.item.application.components.ManageWorkflowLinkDescriptionsPanel;
import zinbig.item.application.pages.BasePage;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.WorkflowLinkDescriptionDTO;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de baja una
 * descripción de enlace de un workflow.<BR>
 * Toda descripción puede darse de baja, sin importar si existen ítems que no
 * puedan salir de un determinado nodo.<BR>
 * Al eliminar un enlace no se afecta al nodo inicial ni al final.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DeleteWorkflowLinkDescriptionForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8967332965722034997L;

	/**
	 * Es el id de la descripción de workflow que se está editando.
	 */
	public String workflowDescriptionId;

	/**
	 * Mantiene la colección de dtos de descripciones de enlaces para eliminar.
	 */
	public Collection<String> selectedWorkflowLinkDescriptions;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripción de workflow que se está editando.
	 * @param aProjectOid
	 *            es el oid del proyecto que se está editando.
	 */
	@SuppressWarnings("unchecked")
	public DeleteWorkflowLinkDescriptionForm(String anId,
			final String aWorkflowDescriptionOid, final String aProjectOid) {
		super(anId);

		this.setWorkflowDescriptionOid(aWorkflowDescriptionOid);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages
				.add(this
						.getString("DeleteWorkflowLinkDescriptionForm.noLinksSelected"));
		messages
				.add(this
						.getString("DeleteWorkflowLinkDescriptionForm.errorDeletingLinks"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedWorkflowLinkDescriptions(new ArrayList<String>());

		// crea el componente para el listado de las descripciones de enlaces
		// existentes
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedWorkflowLinkDescriptions());
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = null;
		String columnOrder = null;

		if (this.getUserDTO() != null) {
			columnName = this.getUserDTO().getUserPreference(
					Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_NAME);
			columnOrder = this.getUserDTO().getUserPreference(
					Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_ORDER);
		}
		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_ORDER);
		}

		final SortableDataProvider<WorkflowLinkDescriptionDTO> provider = new SortableDataProvider<WorkflowLinkDescriptionDTO>() {

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
			public Iterator<WorkflowLinkDescriptionDTO> iterator(int index,
					int count) {

				return getWorkflowLinkDescriptions(
						this.getSort().getProperty(),
						this.getSort().isAscending()).iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aWorkflowLinkDescriptionDTO
			 *            es el dto que representa a una de las descripciones de
			 *            enlace que se está iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<WorkflowLinkDescriptionDTO> model(
					WorkflowLinkDescriptionDTO aWorkflowLinkDescriptionDTO) {
				return new Model<WorkflowLinkDescriptionDTO>(
						aWorkflowLinkDescriptionDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return getWorkflowLinkDescriptions(
						this.getSort().getProperty(),
						this.getSort().isAscending()).size();
			}
		};
		provider.setSort(columnName, columnOrder.equals("ASC"));

		// crea el componente para listar las descripciones de enlaces.
		final DataView linkDescriptions = new DataView("pageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el nombre de la descripción.
			 */
			@Override
			protected void populateItem(Item item) {
				WorkflowLinkDescriptionDTO dto = (WorkflowLinkDescriptionDTO) item
						.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				Label aLabel = new Label("title", dto.getTitle());
				item.add(aLabel);

				Label initialNodeLabel = new Label("initialNode", dto
						.getInitialNode().getTitle());
				item.add(initialNodeLabel);

				Label finalNodeLabel = new Label("finalNode", dto
						.getFinalNode().getTitle());
				item.add(finalNodeLabel);

			}

		};

		// agrega el link para ordenar por el título de las descripciones de
		// enlaces.
		OrderByLink sortByTitleLink = new OrderByLink("sortByTitleLink",
				"upper(l.title)", provider) {

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
				linkDescriptions.setCurrentPage(0);
				SortParam sp = provider.getSort();

				BasePage page = (BasePage) this.getPage();
				page
						.updateUserPreferences(
								Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_NAME,
								sp.getProperty());
				page
						.updateUserPreferences(
								Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_ORDER,
								sp.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre.
		group.add(sortByTitleLink);

		group.add(linkDescriptions);
		this.add(group);

		// construye el link de borrado de prioridades.
		SubmitLink deleteLink = new SubmitLink(
				"deleteWorkflowLinkDescriptionsLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Borra las descripciones de enlaces seleccionadas.
			 */
			@Override
			public void onSubmit() {

				if (!DeleteWorkflowLinkDescriptionForm.this
						.getSelectedWorkflowLinkDescriptions().isEmpty()) {
					try {
						DeleteWorkflowLinkDescriptionForm.this
								.getWorkflowsService()
								.deleteWorflowLinkDescriptions(
										DeleteWorkflowLinkDescriptionForm.this
												.getWorkflowDescriptionOid(),
										DeleteWorkflowLinkDescriptionForm.this
												.getSelectedWorkflowLinkDescriptions());

						// actualiza el panel que contiene este formulario
						((ManageWorkflowLinkDescriptionsPanel) DeleteWorkflowLinkDescriptionForm.this
								.getParent())
								.updateListOfWorkflowLinkDescriptions(
										aWorkflowDescriptionOid, aProjectOid);
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("DeleteWorkflowLinkDescriptionForm.errorDeletingLinks"));
					}

				} else {
					this
							.error(this
									.getString("DeleteWorkflowLinkDescriptionForm.noLinksSelected"));
				}
			}

		};
		this.add(deleteLink);

		group.setVisible(linkDescriptions.getRowCount() != 0);

	}

	/**
	 * Getter.
	 * 
	 * @return el id de la descripción de workflow que se está editando.
	 */
	public String getWorkflowDescriptionOid() {
		return this.workflowDescriptionId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id de la descripción de workflow que se está editando.
	 */
	public void setWorkflowDescriptionOid(String anId) {
		this.workflowDescriptionId = anId;
	}

	/**
	 * Recupera la colección de descripciones de enlaces correspondiente a la
	 * descripción de nodo que se está editando.
	 * 
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * 
	 * @return una colección que contiene DTOs para cada una de las
	 *         descripciones de enlaces.
	 */
	private List<WorkflowLinkDescriptionDTO> getWorkflowLinkDescriptions(
			String aPropertyName, boolean isAscending) {
		ArrayList<WorkflowLinkDescriptionDTO> result = new ArrayList<WorkflowLinkDescriptionDTO>();

		try {
			WorkflowsServiceBI aService = this.getWorkflowsService();
			result.addAll(aService
					.getAllWorkflowLinkDescriptionsOfWorkflowDescription(this
							.getWorkflowDescriptionOid(), aPropertyName,
							isAscending));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return la colección de dtos de las descripciones de enlaces que deben
	 *         ser eliminadas.
	 */
	public Collection<String> getSelectedWorkflowLinkDescriptions() {
		return this.selectedWorkflowLinkDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de dtos de las descripciones de enlaces que
	 *            deben ser eliminadas.
	 */
	public void setSelectedWorkflowLinkDescriptions(
			Collection<String> aCollection) {
		this.selectedWorkflowLinkDescriptions = aCollection;
	}

}
