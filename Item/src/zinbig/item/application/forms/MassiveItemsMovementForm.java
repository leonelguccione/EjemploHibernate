/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.application.pages.MassiveItemsMovementPage;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para realizar un pasaje masivo de
 * ítems de un estado a otro.<br>
 * Este formulario presenta un campo para ingresar los números de los ítems y el
 * proyecto al cual pertenecen.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MassiveItemsMovementForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -8606080988435506370L;

	/**
	 * Contiene los ids de los ítems que deben ser pasados de estado.
	 */
	protected String items;

	/**
	 * Mantiene el dto del proyecto seleccionado por el usuario.
	 */
	protected ProjectDTO selectedProject;

	/**
	 * Mantiene el DTO del nodo seleccionado como próximo por el usuario.
	 */
	protected WorkflowNodeDescriptionDTO selectedNode;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param someItemsIds
	 *            es un string compuesto por los ids de los ítems. Este
	 *            parámetro se utiliza al recargar el formulario con algunos ids
	 *            de ítems que no se pudieron mover.
	 */
	public MassiveItemsMovementForm(String anId, String someItemsIds) {
		super(anId);
		Collection<String> someSelectedItems = ((ItemSession) this.getSession())
				.getSelectedItems();
		if (!someItemsIds.equals("")) {
			this.setItems(someItemsIds);
		} else {
			if (!someSelectedItems.isEmpty()) {
				Collection<ItemDTO> itemDTOs = new ArrayList<ItemDTO>();
				try {
					itemDTOs.addAll(this.getItemsService().findItemsByOid(
							someSelectedItems));
				} catch (Exception e) {

					e.printStackTrace();
				}
				StringBuffer ids = new StringBuffer();
				for (ItemDTO dto : itemDTOs) {
					ids.append(new Integer(dto.getId()).toString());
					ids.append(",");
				}
				this.setItems(ids.toString().substring(0, ids.length() - 1));

			}
		}
		// crea el componente para listar los proyectos
		DropDownChoice<ProjectDTO> projectsList = this
				.createProjectsListComponent();
		this.add(projectsList);

		// agrega el componente para mostrar el feedback del proyecto al
		// usuario.
		final FeedbackLabel projectFeedbackLabel = new FeedbackLabel(
				"projectMIMFeedback", projectsList);
		projectFeedbackLabel.setOutputMarkupId(true);
		this.add(projectFeedbackLabel);

		// crea el componente para listar los próximos nodos
		DropDownChoice<WorkflowNodeDescriptionDTO> nodesList = this
				.createNodesListComponent();
		this.add(nodesList);

		// agrega el componente para mostrar el feedback relacionado con errores
		// relativos al próximo nodo.
		final FeedbackLabel nextNodeFeedbackLabel = new FeedbackLabel(
				"nodeMIMFeedback", nodesList);
		nextNodeFeedbackLabel.setOutputMarkupId(true);
		this.add(nextNodeFeedbackLabel);

		// construye el input field para los ítems seleccionados.
		final TextArea<String> itemsField = new TextArea<String>("itemsField",
				new PropertyModel<String>(this, "items"));
		itemsField.setRequired(true);
		itemsField.setEnabled(false);
		itemsField.setOutputMarkupId(true);
		this.add(itemsField);

		// construye el label para mostrar los mensajes de error relacionados
		// con los ítems.
		final FeedbackLabel itemsFieldFeedback = new FeedbackLabel(
				"itesmFieldFeedback", itemsField);
		itemsFieldFeedback.setOutputMarkupId(true);
		this.add(itemsFieldFeedback);

		// construye el link de cancelación del formulario
		Link<String> cancelLink = new BookmarkablePageLink<String>(
				"cancelLink", DashboardPage.class);
		this.add(cancelLink);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para mover masivamente los ítems al nuevo
			 * estado seleccionado.
			 */
			@Override
			public void onSubmit() {
				String result = "";
				ProjectsServiceBI service = MassiveItemsMovementForm.this
						.getProjectsService();
				try {
					result = service.massiveItemsMovement(
							MassiveItemsMovementForm.this.getSelectedProject(),
							MassiveItemsMovementForm.this.getSelectedNode(),
							MassiveItemsMovementForm.this.getItems());

					PageParameters params = new PageParameters();
					params.put("ITEMS_NOT_MOVED", result);
					setResponsePage(MassiveItemsMovementPage.class, params);
					MassiveItemsMovementForm.this.setItems(result);
				} catch (Exception e) {

					e.printStackTrace();
					MassiveItemsMovementForm.this.setItems(result);
				}

			}

		};
		this.add(submitLink);

	}

	/**
	 * Este método es ejecutado al finalizar la creación del html
	 * correspondiente a este componente.
	 */
	@Override
	protected void onAfterRender() {

		super.onAfterRender();
		final Response response = this.getResponse();
		response
				.write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[1].focus()</script>");
	}

	/**
	 * Getter.
	 * 
	 * @return un string que contiene los identificadores de los ítems
	 *         seleccionados.
	 */
	public String getItems() {
		return this.items;
	}

	/**
	 * Setter.
	 * 
	 * @param someItems
	 *            es un string que contiene los identificadores de los ítems
	 *            seleccionados.
	 */
	public void setItems(String someItems) {
		this.items = someItems;
	}

	/**
	 * Crea el componente para listar todos los proyectos en los cuales el
	 * usuario puede dar de alta un nuevo ítem.<br>
	 * Además de crear este componente se crean los behaviors de Ajax para
	 * actualizar las demás listas en base a la selección realizada por el
	 * usuario.
	 * 
	 * @return el componente creado para mostrar los proyectos.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<ProjectDTO> createProjectsListComponent() {

		// crea un modelo para acceder a la lista de proyectos que debe
		// mostrarse a través del dropdownchoice de proyectos.
		IModel<List<ProjectDTO>> projectChoices = new AbstractReadOnlyModel<List<ProjectDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de proyectos que contiene este modelo.
			 * 
			 * @return una lista de dtos de los proyectos almacenados como
			 *         claves. <br>
			 *         Como todos los mapas tienen las mismas claves se puede
			 *         elegir el conjunto de claves de cualquiera.
			 */
			@Override
			public List<ProjectDTO> getObject() {

				List<ProjectDTO> list = new ArrayList();
				try {
					list.addAll(MassiveItemsMovementForm.this
							.getProjectsService().findPrivateProjectsOfUser(
									getUserDTO()));
				} catch (Exception e) {

					e.printStackTrace();
				}

				return list;
			}

		};

		// crea un renderer específico para los proyectos.
		IChoiceRenderer<String> projectRenderer = new ChoiceRenderer<String>(
				"toString", "oid");
		DropDownChoice projectsList = new DropDownChoice("project",
				new PropertyModel(this, "selectedProject"), projectChoices,
				projectRenderer);

		projectsList.setOutputMarkupId(true);

		// agrega un behavior para validar que se seleccione siempre un proyecto
		// antes de enviar el formulario
		projectsList.add(new AbstractValidator() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Este método es invocado cuando se debe validar el componente. <br>
			 * Si el componente no tiene una selección entonces se registra un
			 * error.
			 * 
			 * @param validatable
			 *            es el componente que se debe validar
			 */
			@Override
			protected void onValidate(IValidatable validatable) {
				// no hace nada
			}

			/**
			 * Getter.
			 * 
			 * @return por defecto siempre se devuelve false para que no se
			 *         valide en caso de tener un valor nulo el componente.
			 */
			@Override
			public boolean validateOnNullValue() {
				return false;
			}

		});

		// agrega un behavior para refrescar los ítems dependiendo
		// del proyecto seleccionado.
		projectsList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Manejador del evento de actualización de la lista de proyectos.
			 * Se actualiza la lista de prioridades con las prioridades
			 * correspondientes al proyecto seleccionado.
			 * 
			 * @param target
			 *            es el objetivo del pedido AJAX.
			 */
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				try {

					TextArea<String> itemsField = (TextArea<String>) MassiveItemsMovementForm.this
							.get("itemsField");
					boolean enabled = MassiveItemsMovementForm.this
							.getSelectedProject() != null;
					itemsField.setEnabled(enabled);

					DropDownChoice<WorkflowNodeDescriptionDTO> nodesList = (DropDownChoice<WorkflowNodeDescriptionDTO>) MassiveItemsMovementForm.this
							.get("nextNode");
					target.addComponent(itemsField);

					target.addComponent(nodesList);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		return projectsList;

	}

	/**
	 * Crea el componente para listar todos los nodos del workflow del proyecto
	 * seleccionado. Por defecto este componente aparece deshabilitado. Cuando
	 * el usuario selecciona un proyecto entonces se recuperan todos los nodos y
	 * se carga adecuadamente este componente.
	 * 
	 * @return el componente creado para mostrar los nodos.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<WorkflowNodeDescriptionDTO> createNodesListComponent() {

		// crea un modelo para acceder a la lista de nodos del workflow.
		IModel<List<WorkflowNodeDescriptionDTO>> nodesChoices = new AbstractReadOnlyModel<List<WorkflowNodeDescriptionDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de nodos del workflow del proyecto
			 * seleccionado.
			 * 
			 * @return una lista de dtos de los nodos del workflow del proyecto
			 *         seleccionado. En caso de que no se haya seleccionado un
			 *         proyecto la lista vuelve vacía.
			 */
			@Override
			public List<WorkflowNodeDescriptionDTO> getObject() {

				List<WorkflowNodeDescriptionDTO> list = new ArrayList();
				try {
					if (MassiveItemsMovementForm.this.getSelectedProject() != null) {
						list
								.addAll(MassiveItemsMovementForm.this
										.getWorkflowsService()
										.getAllWorkflowNodeDescriptionOfWorkflowDescription(
												getSelectedProject()
														.getWorkflowDescriptionDTO()
														.getOid()));
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

				return list;
			}

		};

		// crea un renderer específico para los nodos.
		IChoiceRenderer<String> nodeRenderer = new ChoiceRenderer<String>(
				"title", "oid");
		DropDownChoice nodesList = new DropDownChoice("nextNode",
				new PropertyModel(this, "selectedNode"), nodesChoices,
				nodeRenderer);

		nodesList.setOutputMarkupId(true);
		nodesList.setRequired(true);

		return nodesList;

	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto seleccionado por el usuario.
	 */
	public ProjectDTO getSelectedProject() {
		return this.selectedProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto del proyecto seleccionado por el usuario.
	 */
	public void setSelectedProject(ProjectDTO aDTO) {
		this.selectedProject = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el DTO de la descripción de nodo seleccionada por el usuario.
	 */
	public WorkflowNodeDescriptionDTO getSelectedNode() {
		return this.selectedNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el DTO de la descripción de nodo seleccionada por el
	 *            usuario.
	 */
	public void setSelectedNode(WorkflowNodeDescriptionDTO aDTO) {
		this.selectedNode = aDTO;
	}

}
