/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.ManageWorkflowLinkDescriptionsPanel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.model.exceptions.WorkflowLinkDescriptionTitleNotUniqueException;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase se utilizan para crear enlaces entre las
 * descripciones de nodos del workflow.<br>
 * Para crear un nuevo enlace se deben ingresar un nombre único y seleccionar
 * luego el nodo origin y destino así como todos los tipos de ítems para los
 * cuales es válido el enlace.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddWorkflowLinkDescriptionForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 4239241201228913973L;

	/**
	 * Es el título del nuevo enlace que se está creando.
	 */
	public String title;

	/**
	 * Es el oid de la descripción de workflow que se está editando.
	 */
	public String workflowDescriptionOid;

	/**
	 * Es el oid del proyecto que se está editando.
	 */
	public String projectOid;

	/**
	 * Mantiene una referencia al nodo inicial seleccionado.
	 */
	public WorkflowNodeDescriptionDTO selectedInitialNode;

	/**
	 * Mantiene una referencia al nodo final seleccionado.
	 */
	public WorkflowNodeDescriptionDTO selectedFinalNode;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripción de nodo que se está editando.
	 * @param aProjectOid
	 *            es el oid del proyecto que se está editando. Este parámetro
	 *            podría ser nulo en caso de que este formulario se esté
	 *            utilizando para editar la descripción de workflow que
	 *            pertenece al sistema y no a un proyecto determinado.
	 */
	@SuppressWarnings("unchecked")
	public AddWorkflowLinkDescriptionForm(String anId,
			final String aWorkflowDescriptionOid, final String aProjectOid) {
		super(anId);

		this.setWorkflowDescriptionOid(aWorkflowDescriptionOid);
		this.setProjectOid(aProjectOid);

		// construye la imagen indicadora para Ajax
		final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
		imageContainer.setOutputMarkupId(true);
		this.add(imageContainer);

		// construye el input field para el título del enlace.
		final TextField<String> titleField = new TextField<String>(
				"titleAWLDF", new PropertyModel<String>(this, "title"));
		titleField.setRequired(true);
		titleField.add(new ItemSimpleStringValidator(titleField));
		titleField
				.add(new FormComponentAjaxBehavior(this, "titleAWNLFFeedback"));
		titleField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.username.length"))));
		this.add(titleField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del título del enlace.
		final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
				"titleAWNLFFeedback", titleField);
		titleFeedbackLabel.setOutputMarkupId(true);
		this.add(titleFeedbackLabel);

		// crea el componente para listar los nodos iniciales
		DropDownChoice<WorkflowNodeDescriptionDTO> nodesList = this
				.createInitialNodeDescriptionListComponent();
		this.add(nodesList);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nodo inicial del enlace.
		final FeedbackLabel initialNodeFeedbackLabel = new FeedbackLabel(
				"initialNodeAWLDFFeedback", nodesList);
		initialNodeFeedbackLabel.setOutputMarkupId(true);
		this.add(initialNodeFeedbackLabel);

		// crea el componente para listar los nodos finales
		DropDownChoice<WorkflowNodeDescriptionDTO> finalNodesList = this
				.createFinalNodeDescriptionListComponent();
		this.add(finalNodesList);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nodo final del enlace.
		final FeedbackLabel finalNodeFeedbackLabel = new FeedbackLabel(
				"finalNodeAWLDFFeedback", finalNodesList);
		finalNodeFeedbackLabel.setOutputMarkupId(true);
		this.add(finalNodeFeedbackLabel);

		// agrega las listas para seleccionar los tipos de ítems que serán
		// válidos para el enlace que se está creando.

		final Palette itemTypesPalette = this
				.constructItemTypesPaletteAvailableElements();
		this.add(itemTypesPalette);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("saveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta la nueva transición.
			 */
			@Override
			public void onSubmit() {
				try {
					WorkflowsServiceBI service = AddWorkflowLinkDescriptionForm.this
							.getWorkflowsService();
					service.addWorkflowLinkDescriptionToWorkflowDescription(
							AddWorkflowLinkDescriptionForm.this.getTitle(),
							AddWorkflowLinkDescriptionForm.this.getProjectDTO()
									.getWorkflowDescriptionDTO(),
							AddWorkflowLinkDescriptionForm.this
									.getSelectedInitialNode(),
							AddWorkflowLinkDescriptionForm.this
									.getSelectedFinalNode(), itemTypesPalette
									.getSelectedChoices());
					AddWorkflowLinkDescriptionForm.this.setTitle(null);
					AddWorkflowLinkDescriptionForm.this
							.setSelectedInitialNode(null);
					AddWorkflowLinkDescriptionForm.this
							.setSelectedFinalNode(null);
					AddWorkflowLinkDescriptionForm.this
							.getItemTypesPalette()
							.replaceWith(
									AddWorkflowLinkDescriptionForm.this
											.constructItemTypesPaletteAvailableElements());

					// actualiza el panel que contiene este formulario
					((ManageWorkflowLinkDescriptionsPanel) AddWorkflowLinkDescriptionForm.this
							.getParent()).updateListOfWorkflowLinkDescriptions(
							aWorkflowDescriptionOid, aProjectOid);
				} catch (WorkflowLinkDescriptionTitleNotUniqueException e) {
					titleField
							.error(this
									.getString("WorkflowLinkDescriptionTitleNotUniqueException"));

				} catch (Exception e) {
					this.setResponsePage(ErrorPage.class);
					e.printStackTrace();
				}
			}

		};
		this.add(submitLink);

	}

	/**
	 * Crea el componente para listar todas las descripciones de nodos que serán
	 * tomadas como iniciales.<br>
	 * 
	 * 
	 * @return el componente creado para mostrar los nodos.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<WorkflowNodeDescriptionDTO> createInitialNodeDescriptionListComponent() {

		// crea un modelo para acceder a la lista de descripciones de nodos.
		IModel<List<WorkflowNodeDescriptionDTO>> nodesChoices = new AbstractReadOnlyModel<List<WorkflowNodeDescriptionDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de descripciones de nodos.
			 * 
			 * @return una lista de dtos de las descripciones de nodo
			 *         correspondiente a la descripción de nodo recibida.
			 */
			@Override
			public List<WorkflowNodeDescriptionDTO> getObject() {
				List<WorkflowNodeDescriptionDTO> result = new ArrayList<WorkflowNodeDescriptionDTO>();

				try {
					result
							.addAll(AddWorkflowLinkDescriptionForm.this
									.getWorkflowsService()
									.getAllWorkflowNodeDescriptionOfWorkflowDescription(
											AddWorkflowLinkDescriptionForm.this
													.getWorkflowDescriptionOid()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				return result;
			}

		};

		// crea un renderer específico para las descripciones de nodos.
		IChoiceRenderer<String> nodeRenderer = new ChoiceRenderer<String>(
				"title", "oid");
		DropDownChoice nodesList = new DropDownChoice("initialNodeDescription",
				new PropertyModel(this, "selectedInitialNode"), nodesChoices,
				nodeRenderer);
		nodesList.setRequired(true);
		nodesList.setOutputMarkupId(true);

		return nodesList;

	}

	/**
	 * Crea el componente para listar todas las descripciones de nodos que serán
	 * tomadas como finales.<br>
	 * 
	 * 
	 * @return el componente creado para mostrar los nodos.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<WorkflowNodeDescriptionDTO> createFinalNodeDescriptionListComponent() {

		// crea un modelo para acceder a la lista de descripciones de nodos.
		IModel<List<WorkflowNodeDescriptionDTO>> nodesChoices = new AbstractReadOnlyModel<List<WorkflowNodeDescriptionDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de descripciones de nodos.
			 * 
			 * @return una lista de dtos de las descripciones de nodo
			 *         correspondiente a la descripción de nodo recibida.
			 */
			@Override
			public List<WorkflowNodeDescriptionDTO> getObject() {
				List<WorkflowNodeDescriptionDTO> result = new ArrayList<WorkflowNodeDescriptionDTO>();

				try {
					result
							.addAll(AddWorkflowLinkDescriptionForm.this
									.getWorkflowsService()
									.getAllWorkflowNodeDescriptionOfWorkflowDescription(
											AddWorkflowLinkDescriptionForm.this
													.getWorkflowDescriptionOid()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				return result;
			}

		};

		// crea un renderer específico para las descripciones de nodos.
		IChoiceRenderer<String> nodeRenderer = new ChoiceRenderer<String>(
				"title", "oid");
		DropDownChoice nodesList = new DropDownChoice("finalNodeDescription",
				new PropertyModel(this, "selectedFinalNode"), nodesChoices,
				nodeRenderer);
		nodesList.setRequired(true);
		nodesList.setOutputMarkupId(true);

		return nodesList;

	}

	/**
	 * Getter.
	 * 
	 * @return el título del nuevo enlace.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del enlace.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return el id del contenedor de la imagen utilizada por el componente
	 *         AJAX.
	 */
	public String getMarkupIdForImageContainer() {
		return this.get("indicatorImg").getMarkupId();
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo title por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de workflows para verificar que no exista
	 * un enlace con el título ingresado.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (this.getWorkflowsService()
					.existsWorkflowLinkDescriptionInWorkflowDescription(
							this.getTitle(), this.getWorkflowDescriptionOid())) {

				this
						.get("titleAWLDF")
						.error(
								getString("WorkflowLinkDescriptionTitleNotUniqueException"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Getter.
	 * 
	 * @return el oid de la descripción de workflow que se está editando.
	 */
	public String getWorkflowDescriptionOid() {
		return this.workflowDescriptionOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de la descripción de workflow que se está editando.
	 */
	public void setWorkflowDescriptionOid(String anOid) {
		this.workflowDescriptionOid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el oid del proyecto que se está editando.
	 */
	public String getProjectOid() {
		return this.projectOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto que se está editando.
	 */
	public void setProjectOid(String anOid) {
		this.projectOid = anOid;
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
	 * @return la descripción de nodo seleccionada como inicial.
	 */
	public WorkflowNodeDescriptionDTO getSelectedInitialNode() {
		return this.selectedInitialNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es la descripción de nodo seleccionada como inicial.
	 */
	public void setSelectedInitialNode(WorkflowNodeDescriptionDTO aDTO) {
		this.selectedInitialNode = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return la descripción de nodo seleccionada como final.
	 */
	public WorkflowNodeDescriptionDTO getSelectedFinalNode() {
		return this.selectedFinalNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es la descripción de nodo seleccionada como final.
	 */
	public void setSelectedFinalNode(WorkflowNodeDescriptionDTO aDTO) {
		this.selectedFinalNode = aDTO;
	}

	/**
	 * Recupera todos los tipos de ítems para poder armar el componente que
	 * permite seleccionar los que serán válidos para el nuevo enlace.
	 * 
	 * 
	 * @return una colección que contiene los dtos de los tipos de ítems.
	 */
	@SuppressWarnings("unchecked")
	private Palette<ItemTypeDTO> constructItemTypesPaletteAvailableElements() {

		ItemsServiceBI itemsService = this.getItemsService();
		List<ItemTypeDTO> itemTypes = new ArrayList<ItemTypeDTO>();

		try {

			itemTypes
					.addAll(itemsService
							.findItemTypesOfProject(
									this.getProjectDTO(),
									this
											.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_NAME),
									this
											.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_ORDER)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>(
				"toString", "oid");

		Palette<ItemTypeDTO> newPalette = new Palette("itemTypesPalette",
				new Model((Serializable) new ArrayList<ItemTypeDTO>()),
				new Model((Serializable) itemTypes), renderer, 6, true);

		return newPalette;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que muestra los tipos de ítems disponibles.
	 */
	@SuppressWarnings("unchecked")
	public Palette<ItemTypeDTO> getItemTypesPalette() {
		return (Palette<ItemTypeDTO>) this.get("itemTypesPalette");
	}

}
