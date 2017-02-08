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
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.ManageWorkflowNodeDescriptionsPanel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionTitleNotUniqueException;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instanacias de esta clase se utilizan para agregar nuevas descripciones
 * de nodos a las descripciones de workflow.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddWorkflowNodeDescriptionForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 1062599554686663886L;

	/**
	 * Es el título del nuevo nodo.
	 */
	protected String title;

	/**
	 * La descripción de nodo es final?.
	 */
	protected boolean finalNode;

	/**
	 * Es el oid de la descripción de workflow que se está editando.
	 */
	protected String workflowDescriptionOid;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 */
	@SuppressWarnings("unchecked")
	public AddWorkflowNodeDescriptionForm(String anId,
			final String aWorkflowDescriptionOid, final String aProjectOid) {
		super(anId);

		this.setWorkflowDescriptionOid(aWorkflowDescriptionOid);

		// construye el input field para el título
		final TextField<String> titleField = new TextField<String>(
				"titleAWNDF", new PropertyModel<String>(this, "title"));
		titleField.setRequired(true);
		titleField.setOutputMarkupId(true);
		titleField.add(new ItemSimpleStringValidator(titleField));
		titleField
				.add(StringValidator
						.maximumLength(new Integer(
								this
										.getSystemProperty("AddWorkflowNodeDescriptionForm.name.length"))));
		titleField
				.add(new FormComponentAjaxBehavior(this, "titleAWNDFFeedback"));
		this.add(titleField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del título.
		final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
				"titleAWNDFFeedback", titleField);
		titleFeedbackLabel.setOutputMarkupId(true);
		this.add(titleFeedbackLabel);

		// construye la imagen indicadora para Ajax
		final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
		imageContainer.setOutputMarkupId(true);
		this.add(imageContainer);

		// construye el chekbox para definir si es una descripción final o no.
		final CheckBox checkBox = new CheckBox("finalNode", new PropertyModel(
				this, "finalNode"));
		this.add(checkBox);

		// agrega las listas para seleccionar los usuarios y grupos de usuarios
		final Palette usersPalette = this.createUsersPalette();
		this.add(usersPalette);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("saveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario, dando de alta la nueva descripción de nodo.
			 */
			@Override
			public void onSubmit() {

				try {
					AddWorkflowNodeDescriptionForm.this.getWorkflowsService()
							.addWorkflowNodeDescription(
									AddWorkflowNodeDescriptionForm.this
											.getTitle(),
									AddWorkflowNodeDescriptionForm.this
											.isFinalNode(),
									AddWorkflowNodeDescriptionForm.this
											.getWorkflowDescriptionOid(),
									usersPalette.getSelectedChoices());
					AddWorkflowNodeDescriptionForm.this.setTitle(null);
					AddWorkflowNodeDescriptionForm.this.get("usersPalette")
							.replaceWith(
									AddWorkflowNodeDescriptionForm.this
											.createUsersPalette());

					// actualiza el panel que contiene este formulario
					((ManageWorkflowNodeDescriptionsPanel) AddWorkflowNodeDescriptionForm.this
							.getParent()).updateListOfWorkflowLinkDescriptions(
							aWorkflowDescriptionOid, aProjectOid);

				} catch (WorkflowNodeDescriptionTitleNotUniqueException wndnue) {
					AddWorkflowNodeDescriptionForm.this
							.get("titleAWNDF")
							.error(
									getString("WorkflowNodeTitleNotUniqueException"));

				} catch (Exception e) {
					this.setResponsePage(ErrorPage.class);
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
	 * @return el id del contenedor de la imagen utilizada por el componente
	 *         AJAX.
	 */
	public String getMarkupIdForImageContainer() {
		return this.get("indicatorImg").getMarkupId();
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo "title" por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de workflows para verificar que no exista
	 * una descripción de nodo ya con el título ingresado. En caso de que exista
	 * ya se muestra un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (this.getWorkflowsService()
					.existsWorkflowNodeDescriptionInWorkflowDescription(
							this.getTitle(), this.getWorkflowDescriptionOid())) {

				this.get("titleAWNDF").error(
						getString("WorkflowNodeTitleNotUniqueException"));

			}

		} catch (Exception e) {

		}

	}

	/**
	 * Getter.
	 * 
	 * @return el oid de la descripción del workflow que se está editando.
	 */
	public String getWorkflowDescriptionOid() {
		return this.workflowDescriptionOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de la descripción del workflow que se está editando.
	 */
	public void setWorkflowDescriptionOid(String anOid) {
		this.workflowDescriptionOid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el título ingresado para la nueva descripción de nodo.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título ingresado para la nueva descripción de nodo.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que la nueva descripción que se está agregando es
	 *         final o no; false en caso contrario.
	 */
	public boolean isFinalNode() {
		return this.finalNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            define si la nueva descripción de nodo es final o no.
	 */
	public void setFinalNode(boolean aBoolean) {
		this.finalNode = aBoolean;
	}

	/**
	 * Recupera todos los grupos de usuarios y usuarios disponibles a partir del
	 * servicio de usuarios.
	 * 
	 * @return una colección que contiene los dtos de los grupos de usuarios y
	 *         usuarios disponibles para el nuevo nodo.
	 */
	private List<AbstractUserDTO> constructUsersPaletteAvailableElements() {

		ProjectsServiceBI projectsService = this.getProjectsService();
		List<AbstractUserDTO> allUsers = new ArrayList<AbstractUserDTO>();

		try {
			ProjectDTO aProjectDTO = this.getProjectDTO();

			allUsers
					.addAll(projectsService.getUserGroupsOfProject(aProjectDTO));
			allUsers.addAll(projectsService.getUsersOfProject(aProjectDTO));

		} catch (Exception e) {

		}

		return allUsers;
	}

	/**
	 * Crea el componente para seleccionar los usuarios y grupos de usuarios.
	 * 
	 * @return un componente "Palette".
	 */
	@SuppressWarnings("unchecked")
	public Palette createUsersPalette() {
		List<AbstractUserDTO> users = this
				.constructUsersPaletteAvailableElements();
		IChoiceRenderer renderer = new ChoiceRenderer("toString", "oid");

		final Palette usersPalette = new Palette("usersPalette", new Model(
				(Serializable) new ArrayList<AbstractUserDTO>()), new Model(
				(Serializable) users), renderer, 6, true);

		return usersPalette;
	}

}
