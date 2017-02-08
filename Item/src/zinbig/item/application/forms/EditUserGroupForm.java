/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.UserGroupsAdministrationPage;
import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.UserGroupUnknownException;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de este formulario se utilizan para editar la información de
 * los grupos de usuarios.<br>
 * Se almacena en una variable el nombre actual del grupo ya que es posible
 * alterarlo mediante este formulario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditUserGroupForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2256766066081372066L;

	/**
	 * Mantiene el email del grupo de usuarios.
	 */
	public String emailEUGF;

	/**
	 * Mantiene el nombre del grupo de usuarios.
	 */
	public String nameEUGF;

	/**
	 * Mantiene el nombre actual del grupo de usuarios. Este campo se utiliza
	 * para realizar la búsqueda del grupo.
	 */
	public String currentUserGroupName;

	/**
	 * Es el dto que representa al grupo de usuarios.
	 */
	public UserGroupDTO userGroupDTO;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el id de este formulario.
	 * @param aName
	 *            es el nombre del grupo de usuarios que se va a editar.
	 */
	@SuppressWarnings("unchecked")
	public EditUserGroupForm(String anId, String aName) {

		super(anId);

		// almacena el nombre actual del grupo en caso de que se edite el nombre
		// del grupo
		try {
			UsersServiceBI service = ServiceLocator.getInstance()
					.getUsersService();

			final UserGroupDTO aDto = (UserGroupDTO) service
					.findUserGroup(aName);
			this.setUserGroupDTO(aDto);
			this.setCurrentUserGroupName(aDto.getName());
			this.setNameEUGF(aDto.getName());
			this.setEmailEUGF(aDto.getEmail());

			// construye la imagen indicadora para Ajax
			final AjaxIndicator imageContainer = new AjaxIndicator(
					"indicatorImg");
			imageContainer.setOutputMarkupId(true);
			this.add(imageContainer);

			// construye el input field para el nombre del grupo de usuarios
			final TextField<String> nameField = new TextField<String>(
					"nameEUGF", new PropertyModel<String>(this, "nameEUGF"));
			nameField.setRequired(true);
			nameField.add(new ItemSimpleStringValidator(nameField));
			nameField.add(StringValidator.maximumLength(new Integer(this
					.getSystemProperty("AddUserGroupForm.name.length"))));
			nameField.add(new FormComponentAjaxBehavior(this,
					"nameEUGFFeedback"));
			this.add(nameField);

			// construye el label para mostrar los mensajes de error
			// relacionados
			// con el componente del nombre del grupo de usuarios.
			final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
					"nameEUGFFeedback", nameField);
			nameFeedbackLabel.setOutputMarkupId(true);
			this.add(nameFeedbackLabel);

			// construye el input field para el email
			final TextField<String> anEmailField = new TextField<String>(
					"emailEUGF", new PropertyModel<String>(this, "emailEUGF"));
			anEmailField.add(EmailAddressValidator.getInstance());
			anEmailField.add(StringValidator.maximumLength(new Integer(this
					.getSystemProperty("AddUserGroupForm.email.length"))));
			this.add(anEmailField);

			// construye el label para mostrar los mensajes de error
			// relacionados
			// con el componente del email del grupo.
			final FeedbackLabel emailFeedbackLabel = new FeedbackLabel(
					"emailEUGFFeedback", anEmailField);
			emailFeedbackLabel.setOutputMarkupId(true);
			this.add(emailFeedbackLabel);

			// agrega el componente para seleccionar las operaciones
			Label operationsLabel = new Label("operationsLabel", this
					.getString("editUserGroupForm.operationsLabel"));
			this.add(operationsLabel);
			List<OperationDTO> operations = this
					.constructOperationsPaletteAvailableElements(aDto);
			IChoiceRenderer operationRenderer = new ChoiceRenderer(
					"completeName", "oid");

			final Palette operationsPalette = new Palette("operationsPalette",
					new Model((Serializable) aDto.getOperationDTOs()),
					new Model((Serializable) operations), operationRenderer, 6,
					true);

			this.add(operationsPalette);

			// agrega el componente para seleccionar los proyectos
			Label projectsLabel = new Label("projectsLabel", this
					.getString("editUserGroupForm.projectsLabel"));
			this.add(projectsLabel);
			List<ProjectDTO> projects = this
					.constructProjectPaletteAvailableElements(aDto);
			IChoiceRenderer projectRenderer = new ChoiceRenderer("name", "oid");
			final Palette projectsPalette = new Palette("projectsPalette",
					new Model((Serializable) aDto.getProjectDTOs()), new Model(
							(Serializable) projects), projectRenderer, 6, true);
			this.add(projectsPalette);

			// construye el link de envío del formulario. Este link retorna a la
			// página de administración de grupos de usuarios
			SubmitLink saveLink = new SubmitLink("saveLink") {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Envía el formulario para dar de alta un nuevo grupo de
				 * usuarios.
				 */
				@Override
				public void onSubmit() {
					getUserGroupDTO().setEmail(getEmailEUGF());
					getUserGroupDTO().setName(getNameEUGF());

					// recupero todos los proyectos seleccionados.
					Iterator selectedProjectsIterator = projectsPalette
							.getSelectedChoices();
					getUserGroupDTO().getProjectDTOs().clear();
					while (selectedProjectsIterator.hasNext()) {
						getUserGroupDTO().getProjectDTOs().add(
								(ProjectDTO) selectedProjectsIterator.next());

					}

					// recupero todas las operaciones seleccionadas.
					Iterator selectedOperationsIterator = operationsPalette
							.getSelectedChoices();
					getUserGroupDTO().getOperationDTOs().clear();
					while (selectedOperationsIterator.hasNext()) {
						getUserGroupDTO().getOperationDTOs().add(
								(OperationDTO) selectedOperationsIterator
										.next());
					}

					try {
						EditUserGroupForm.this.getUsersService().editUserGroup(
								getCurrentUserGroupName(), getUserGroupDTO());

						this
								.setResponsePage(UserGroupsAdministrationPage.class);

					} catch (ItemConcurrentModificationException e) {

						e.printStackTrace();
						this.setResponsePage(ErrorPage.class);

					} catch (Exception e) {
						e.printStackTrace();
						setResponsePage(ErrorPage.class);
					}

				}

			};
			this.add(saveLink);

			// agrega el link de cancelación
			Link<String> cancelLink = new BookmarkablePageLink<String>(
					"cancelLink", UserGroupsAdministrationPage.class);
			this.add(cancelLink);

			this.modelChanged();
		} catch (UserGroupUnknownException e) {
			error(this.getString("UserGroupUnknownException"));
			e.printStackTrace();
			this.setResponsePage(ErrorPage.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter.
	 * 
	 * @return el email ingresado.
	 */
	public String getEmailEUGF() {
		return this.emailEUGF;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el email del grupo de usuarios.
	 */
	public void setEmailEUGF(String anEmail) {
		this.emailEUGF = anEmail;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del grupo de usuarios.
	 */
	public String getNameEUGF() {
		return this.nameEUGF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del grupo de usuarios.
	 */
	public void setNameEUGF(String aName) {
		this.nameEUGF = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre actual del grupo de usuarios.
	 */
	public String getCurrentUserGroupName() {
		return this.currentUserGroupName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre actual del grupo de usuarios.
	 */
	public void setCurrentUserGroupName(String aName) {
		this.currentUserGroupName = aName;
	}

	/**
	 * Recupera todas las operaciones disponibles a partir del servicio de
	 * usuarios y elimina de dicha colección todas las operaciones ya agregadas
	 * al grupo.
	 * 
	 * @param anUserGroupDTO
	 *            es el dto que representa al grupo de usuarios y que contiene
	 *            los dtos de las operaciones ya asignadas.
	 * @return una colección que contiene los dtos de las operaciones
	 *         disponibles para el grupo de usuarios.
	 */
	private List<OperationDTO> constructOperationsPaletteAvailableElements(
			UserGroupDTO anUserGroupDTO) {

		UsersServiceBI usersService = this.getUsersService();
		List<OperationDTO> allOperations = new ArrayList<OperationDTO>();

		try {
			allOperations.addAll(usersService
					.getAllNonAdministrativeOperations());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allOperations;
	}

	/**
	 * Recupera todas los proyectos disponibles a partir del servicio de
	 * proyectos y elimina de dicha colección todos los proyectos ya agregados
	 * al grupo.
	 * 
	 * @param anUserGroupDTO
	 *            es el dto que representa al grupo de usuarios y que contiene
	 *            los dtos de los proyectos ya asignados.
	 * @return una colección que contiene los dtos de los proyectos disponibles
	 *         para el grupo de usuarios.
	 */
	private List<ProjectDTO> constructProjectPaletteAvailableElements(
			UserGroupDTO anUserGroupDTO) {

		ProjectsServiceBI projectsService = this.getProjectsService();
		List<ProjectDTO> allProjects = new ArrayList<ProjectDTO>();

		try {
			allProjects.addAll(projectsService.findAllProjects());

		} catch (Exception e) {
			// se loguea el error
			e.printStackTrace();
		}

		return allProjects;
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo name por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de usuarios para verificar que no exista
	 * un grupo de usuarios con el nombre ingresado. En caso de que exista se
	 * muestra un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (!this.getNameEUGF().equals(this.getCurrentUserGroupName())) {
				// si el nombre del grupo es distinto al nuevo entonces verifica
				// si
				// no existe conflicto de nombres
				if (this.getUsersService().containsUserGroupWithName(
						this.getNameEUGF())) {
					this.get("nameEUGF").error(
							getString("UserGroupNameNotUniqueException"));
				}
			}
		} catch (Exception e) {
			// no se debe hacer nada en este caso, a lo sumo si el nombre ya
			// existe el servicio verificará nuevamente esta condición y
			// levantará una excepción.
			e.printStackTrace();
		}

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
	 * @return el dto que representa al grupo de usuarios.
	 */
	public UserGroupDTO getUserGroupDTO() {
		return this.userGroupDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserGroupDTO
	 *            es el dto que representa al grupo de usuarios.
	 */
	public void setUserGroupDTO(UserGroupDTO anUserGroupDTO) {
		this.userGroupDTO = anUserGroupDTO;
	}

}
