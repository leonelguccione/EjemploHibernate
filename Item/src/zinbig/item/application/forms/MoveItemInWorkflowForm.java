/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.MoveItemInWorkflowPanel;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para mover un ítem de un estado a
 * otro.<br>
 * Siempre se debe elegir el siguiente nodo del workflow y un responsable. Este
 * responsable puede ser tanto un usuario individual como un grupo de usuarios.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MoveItemInWorkflowForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 6373964324028743652L;

	/**
	 * Es el dto que representa la ítem.
	 */
	public ItemDTO itemDTO;

	/**
	 * Es el dto que mantiene la selección del próximo estado para el ítem.
	 */
	public WorkflowNodeDescriptionDTO nextNode;

	/**
	 * Es el dto que mantiene la selección del nuevo responsable.
	 */
	public AbstractUserDTO nextResponsible;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está editando.
	 */
	public MoveItemInWorkflowForm(String anId, final ItemDTO anItemDTO,
			final String aFilterOID) {
		super(anId);

		this.setItemDTO(anItemDTO);

		// crea la etiqueta con el título del estado actual.
		String currentNodeLabel = anItemDTO.getCurrentNode() != null ? anItemDTO
				.getCurrentNode().getTitle()
				: "-";
		Label itemCurrentNodeLabel = new Label("currentNode", currentNodeLabel);
		this.add(itemCurrentNodeLabel);

		// construye el componente que permite mostrar una lista con todos los
		// posibles nodos siguientes del workflow.
		final DropDownChoice<WorkflowNodeDescriptionDTO> nodesList = this
				.createNodesListComponent(this.getItemDTO());
		this.add(nodesList);

		// construye el componente para dar feedback al usuario respecto de
		// la lista de próximos nodos.
		final FeedbackLabel nextNodesFeedbackLabel = new FeedbackLabel(
				"nextNodeMIIWFFeedback", nodesList);
		nextNodesFeedbackLabel.setOutputMarkupId(true);
		this.add(nextNodesFeedbackLabel);

		// agrega una etiqueta de título para el combo de nuevo estado
		Label nextNodeLabel = new Label("nextNodeLabel", this
				.getString("moveItemInWorkflowForm.nextNode"));
		this.add(nextNodeLabel);
		Label nextNodeLabelRequired = new Label("nextNodeLabelRequired", "*");
		this.add(nextNodeLabelRequired);

		// construye el componente que permite mostrar una lista con todos los
		// posibles responsables para el ítem en el próximo estado.
		final DropDownChoice<AbstractUserDTO> responsiblesList = this
				.createResponsiblesListComponent();
		this.add(responsiblesList);

		// construye el componente para dar feedback al usuario respecto de
		// la lista de próximos responsables.
		final FeedbackLabel nextResponsibleFeedbackLabel = new FeedbackLabel(
				"nextResponsibleMIIWFFeedback", responsiblesList);
		nextResponsibleFeedbackLabel.setOutputMarkupId(true);
		this.add(nextResponsibleFeedbackLabel);

		// agrega una etiqueta de título para el combo de nuevo responsable
		Label nextResponsibleLabel = new Label("nextResponsibleLabel", this
				.getString("moveItemInWorkflowForm.nextResponsible"));
		this.add(nextResponsibleLabel);
		Label nextResponsibleLabelRequired = new Label(
				"nextResponsibleLabelRequired", "*");
		this.add(nextResponsibleLabelRequired);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				ItemsServiceBI aService = MoveItemInWorkflowForm.this
						.getItemsService();
				if (MoveItemInWorkflowForm.this.getNextNode() == null) {
					nodesList
							.error(this
									.getString("moveItemInWorkflowForm.noNextStateSelected"));
				} else {
					if (MoveItemInWorkflowForm.this.getNextResponsible() == null) {
						responsiblesList
								.error(this
										.getString("moveItemInWorkflowForm.noNextResponsibleSelected"));
					} else {
						try {

							ItemDTO newItemDTO = aService.assignItemToUser(
									MoveItemInWorkflowForm.this.getItemDTO(),
									MoveItemInWorkflowForm.this.getNextNode(),
									MoveItemInWorkflowForm.this
											.getNextResponsible());

							PageParameters params = new PageParameters();

							if (aFilterOID == null) {
								params.put("FILTER_OID", "");

							} else {
								params.put("FILTER_OID", aFilterOID);
							}

							params.put("TAB", 2);
							params.put("ITEM_OID", anItemDTO.getOid());
							((MoveItemInWorkflowPanel) MoveItemInWorkflowForm.this
									.getParent()).updateItemVersion(newItemDTO);
							setResponsePage(ViewItemDetailPage.class, params);

						} catch (Exception e) {
							e.printStackTrace();

						}
					}
				}
			}
		};
		this.add(submitLink);
		nodesList.setVisible(!anItemDTO.isFinished());
		responsiblesList.setVisible(!anItemDTO.isFinished());
		submitLink.setVisible(!anItemDTO.isFinished());
		nextNodeLabel.setVisible(!anItemDTO.isFinished());
		nextNodeLabelRequired.setVisible(!anItemDTO.isFinished());
		nextResponsibleLabel.setVisible(!anItemDTO.isFinished());
		nextResponsibleLabelRequired.setVisible(!anItemDTO.isFinished());

	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa la ítem.
	 */
	public ItemDTO getItemDTO() {
		return this.itemDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem.
	 */
	public void setItemDTO(ItemDTO anItemDTO) {
		this.itemDTO = anItemDTO;
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
	 * Crea el componente que permite seleccionar un nuevo estado para el ítem.<br>
	 * 
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está moviendo.
	 * 
	 * @return el componente para listar los próximos estados.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<WorkflowNodeDescriptionDTO> createNodesListComponent(
			final ItemDTO anItemDTO) {

		// crea un modelo para acceder a la lista de próximos nodos.
		IModel<List<WorkflowNodeDescriptionDTO>> nodesChoices = new AbstractReadOnlyModel<List<WorkflowNodeDescriptionDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de los próximos nodos del workflow.
			 * 
			 * @return una lista de dtos de los nodos siguientes.
			 * 
			 */
			@Override
			public List<WorkflowNodeDescriptionDTO> getObject() {
				List<WorkflowNodeDescriptionDTO> nodes = new ArrayList<WorkflowNodeDescriptionDTO>();
				String aPropertyNameForOrdering = "node_title";
				String anOrdering = MoveItemInWorkflowForm.this
						.getSystemProperty(Constants.MANAGE_WORKFLOW_LINK_DESCRIPTIONS_COLUMN_ORDER);
				try {
					nodes.addAll(MoveItemInWorkflowForm.this
							.getWorkflowsService()
							.findNextWorkflowNodesForItem(anItemDTO,
									aPropertyNameForOrdering, anOrdering));
				} catch (Exception e) {
					e.printStackTrace();
				}

				return nodes;
			}

		};

		// construye la lista para mostrar todos los nodos siguientes.
		IChoiceRenderer nodeRenderer = new ChoiceRenderer("title", "oid");
		DropDownChoice nodesList = new DropDownChoice("nextNode",
				new PropertyModel(this, "nextNode"), nodesChoices, nodeRenderer);

		nodesList.setOutputMarkupId(true);

		// agrega un behavior para refrescar la lista de responsables
		// dependiendo
		// del nodo seleccionado.
		nodesList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Manejador del evento de actualización de la lista de nodos. Se
			 * actualiza la lista de responsables con los usuarios autorizados
			 * para el nodo seleccionado.
			 * 
			 * @param target
			 *            es el objetivo del pedido AJAX.
			 */
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				try {
					ProjectDTO projectDTO = getProjectDTO();
					boolean enabled = true;
					// si el estado actual es CREADO entonces dependiendo de la
					// estrategia de asignación se selecciona automáticamente o
					// no el nodo y el responsable.

					if (getItemDTO().getState().equals("CREATED")) {
						if (projectDTO
								.getItemAssignmentStrategy()
								.equals(
										"zinbig.item.model.projects.CreatorAssignmentStrategy")) {
							// la estrategia del proyecto es asignar el ítem a
							// su creador
							setNextResponsible(getItemDTO().getCreator());
							enabled = false;
						} else {
							if (projectDTO
									.getItemAssignmentStrategy()
									.equals(
											"zinbig.item.model.projects.ProjectLeaderAssignmentStrategy")) {
								setNextResponsible(getItemDTO()
										.getProjectLeader());

								enabled = false;
							}
						}
					}

					MoveItemInWorkflowForm.this
							.get("nextResponsible")
							.setEnabled(
									MoveItemInWorkflowForm.this.getNextNode() != null
											&& enabled);

					target.addComponent(MoveItemInWorkflowForm.this
							.get("nextResponsible"));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		nodesList.add(new AbstractValidator() {

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

		return nodesList;
	}

	/**
	 * @return the nextNode
	 */
	public WorkflowNodeDescriptionDTO getNextNode() {
		return this.nextNode;
	}

	/**
	 * @param nextNode
	 *            the nextNode to set
	 */
	public void setNextNode(WorkflowNodeDescriptionDTO nextNode) {
		this.nextNode = nextNode;
	}

	/**
	 * Crea el componente que permite seleccionar el próximo responsable para el
	 * ítem.<br>
	 * 
	 * 
	 * 
	 * 
	 * @return el componente para listar los posibles próximos responsables.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<AbstractUserDTO> createResponsiblesListComponent() {

		// crea un modelo para acceder a la lista de próximos responsables.
		IModel<List<AbstractUserDTO>> usersChoices = new AbstractReadOnlyModel<List<AbstractUserDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de los próximos responsables del ítem.
			 * 
			 * @return una lista de dtos de los usuarios.
			 * 
			 */
			@Override
			public List<AbstractUserDTO> getObject() {
				List<AbstractUserDTO> users = new ArrayList<AbstractUserDTO>();
				WorkflowsServiceBI service = MoveItemInWorkflowForm.this
						.getWorkflowsService();

				if (MoveItemInWorkflowForm.this.getNextNode() != null) {
					try {
						users
								.addAll(service
										.getAllAuthorizedUsersOfWorkflowNodeDescription(MoveItemInWorkflowForm.this
												.getNextNode()));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return users;
			}

		};

		// construye la lista para mostrar todos los usuarios.
		IChoiceRenderer userRenderer = new ChoiceRenderer("toString", "oid");
		DropDownChoice usersList = new DropDownChoice("nextResponsible",
				new PropertyModel(this, "nextResponsible"), usersChoices,
				userRenderer);

		usersList.setOutputMarkupId(true);
		usersList.setRequired(true);
		usersList.setEnabled(MoveItemInWorkflowForm.this.getNextNode() != null);
		return usersList;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al próximo responsable.
	 */
	public AbstractUserDTO getNextResponsible() {
		return this.nextResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param anAbstractUserDTO
	 *            es el dto que representa al próximo responsable.
	 */
	public void setNextResponsible(AbstractUserDTO anAbstractUserDTO) {
		this.nextResponsible = anAbstractUserDTO;
	}

}
