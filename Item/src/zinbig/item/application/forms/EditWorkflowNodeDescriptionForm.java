/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
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
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.EditProjectPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionTitleNotUniqueException;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instanacias de esta clase se utilizan para editar las descripciones de
 * nodos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditWorkflowNodeDescriptionForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 3554571400452985585L;

	/**
	 * Es el título del nuevo nodo.
	 */
	protected String title;

	/**
	 * Es el título actual de la descripción de nodo.
	 */
	protected String currentTitle;

	/**
	 * La descripción de nodo es final?.
	 */
	protected boolean finalNode;

	/**
	 * Es el identificador de la descripción de workflow a la cual pertenece la
	 * descripción de nodo que se está editando.
	 */
	protected String workflowDescriptionOid;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripción de nodo que se está
	 *            editando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow a la cual
	 *            pertenece la descripción de nodo que se está editando.
	 * @param aProjectOid
	 *            es el identificador del proyecto al cual pertenece la
	 *            descripción de nodo.
	 */
	@SuppressWarnings("unchecked")
	public EditWorkflowNodeDescriptionForm(String anId,
			final String aWorkflowNodeDescriptionOid,
			final String aWorkflowDescriptionOid, final String aProjectOid) {
		super(anId);

		try {
			this.setWorkflowDescriptionOid(aWorkflowDescriptionOid);
			WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO = this
					.getWorkflowsService().findWorkflowNodeDescription(
							aWorkflowNodeDescriptionOid);
			this.setTitle(aWorkflowNodeDescriptionDTO.getTitle());
			this.setCurrentTitle(aWorkflowNodeDescriptionDTO.getTitle());
			this.setFinalNode(aWorkflowNodeDescriptionDTO.isFinalNode());

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
			titleField.add(new FormComponentAjaxBehavior(this,
					"titleAWNDFFeedback"));
			this.add(titleField);

			// construye el label para mostrar los mensajes de error
			// relacionados
			// con el componente del título.
			final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
					"titleAWNDFFeedback", titleField);
			titleFeedbackLabel.setOutputMarkupId(true);
			this.add(titleFeedbackLabel);

			// construye la imagen indicadora para Ajax
			final AjaxIndicator imageContainer = new AjaxIndicator(
					"indicatorImg");
			imageContainer.setOutputMarkupId(true);
			this.add(imageContainer);

			// construye el chekbox para definir si es una descripción final o
			// no.
			final CheckBox checkBox = new CheckBox("finalNode",
					new PropertyModel(this, "finalNode"));
			this.add(checkBox);

			// agrega las listas para seleccionar los usuarios y grupos de
			// usuarios
			final Palette usersPalette = this
					.createUsersPalette(aWorkflowNodeDescriptionDTO);
			this.add(usersPalette);

			// construye el link de envío del formulario
			SubmitLink submitLink = new SubmitLink("saveLink") {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Envía el formulario, dando de alta la nueva descripción de
				 * nodo.
				 */
				@Override
				public void onSubmit() {

					try {
						EditWorkflowNodeDescriptionForm.this
								.getWorkflowsService()
								.editWorkflowNodeDescription(
										aWorkflowNodeDescriptionOid,
										aWorkflowDescriptionOid, getTitle(),
										isFinalNode(),
										usersPalette.getSelectedChoices());

						PageParameters parameters = new PageParameters();
						parameters.put("projectOID", aProjectOid);
						parameters.put("TAB", 4);
						this.setResponsePage(EditProjectPage.class, parameters);

					} catch (WorkflowNodeDescriptionTitleNotUniqueException wndnue) {
						EditWorkflowNodeDescriptionForm.this
								.get("titleAWNDF")
								.error(
										getString("WorkflowNodeTitleNotUniqueException"));

					} catch (Exception e) {
						this.setResponsePage(ErrorPage.class);
					}
				}

			};
			this.add(submitLink);
		} catch (Exception e) {
			this.setResponsePage(ErrorPage.class);
		}

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
			if ((!this.getTitle().equals(this.getCurrentTitle()))
					&& this
							.getWorkflowsService()
							.existsWorkflowNodeDescriptionInWorkflowDescription(
									this.getTitle(),
									this.getWorkflowDescriptionOid())) {

				this.get("titleAWNDF").error(
						getString("WorkflowNodeTitleNotUniqueException"));

			}

		} catch (Exception e) {

		}

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
	 * @param aWorkflowNodeDescriptionOid
	 *            es el dto que representa la descripción de nodo que se está
	 *            editando.
	 * 
	 * @return un componente "Palette".
	 */
	@SuppressWarnings("unchecked")
	public Palette createUsersPalette(
			WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO) {
		List<AbstractUserDTO> users = this
				.constructUsersPaletteAvailableElements();
		IChoiceRenderer renderer = new ChoiceRenderer("toString", "oid");

		WorkflowsServiceBI service = this.getWorkflowsService();
		List<AbstractUserDTO> assignedUsers = new ArrayList<AbstractUserDTO>();
		try {
			assignedUsers
					.addAll(service
							.getAllAuthorizedUsersOfWorkflowNodeDescription(aWorkflowNodeDescriptionDTO));
		} catch (Exception e) {

		}

		final Palette usersPalette = new Palette("usersPalette", new Model(
				(Serializable) assignedUsers), new Model((Serializable) users),
				renderer, 6, true);

		return usersPalette;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de la descripción de workflow a la que pertenece
	 *         la descripción de nodo que se está editando.
	 */
	public String getWorkflowDescriptionOid() {
		return this.workflowDescriptionOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de la descripción de workflow a la que
	 *            pertenece la descripción de nodo que se está editando.
	 */
	public void setWorkflowDescriptionOid(String anOid) {
		this.workflowDescriptionOid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el título actual de la descripción de nodo.
	 */
	public String getCurrentTitle() {
		return this.currentTitle;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título actual de la descripción de nodo.
	 */
	public void setCurrentTitle(String aTitle) {
		this.currentTitle = aTitle;
	}

}
