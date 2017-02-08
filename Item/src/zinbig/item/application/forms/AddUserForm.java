/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.AddUserPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.UsersAdministrationPage;
import zinbig.item.model.exceptions.UsernameNotUniqueException;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase se utilizan para registrar un nuevo usuario en
 * la aplicación. <br>
 * El proceso de registro tiene básicamente dos pasos: primero un nuevo usuario
 * carga la información básica de su perfil mediante este formulario, luego de
 * lo cual recibe un email de activación de la cuenta. Si dentro de los 3 días
 * no activa la cuenta, ésta será dada de baja automáticamente. <br>
 * Las únicas restricciones que son impuestas en el registro son: utilizar un
 * username que no exista ya en la aplicación y proveer un email válido.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddUserForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -3229603764510902537L;

	/**
	 * Es un campo que almacena el username provisto por el nuevo usuario.
	 */
	public String usernameAUF;

	/**
	 * Es el email provisto por el nuevo usuario.
	 */
	public String emailAUF;

	/**
	 * Mantiene la password ingresada por el nuevo usuario.
	 */
	public String passwordAUF;

	/**
	 * Mantiene la confirmación de la password ingresada por el nuevo usuario.
	 */
	public String passwordConfirmationAUF;

	/**
	 * Es el nombre del usuario.
	 */
	public String nameAUF;

	/**
	 * Es el apellido del nuevo usuario.
	 */
	public String surnameAUF;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este formulario dentro del panel.
	 */
	@SuppressWarnings("unchecked")
	public AddUserForm(String id) {
		super(id);

		// construye el input field para el nombre
		final TextField<String> nameField = new TextField<String>("nameAUF",
				new PropertyModel<String>(this, "nameAUF"));
		nameField.setRequired(true);
		nameField.setOutputMarkupId(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		nameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.name.length"))));
		this.add(nameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre del usuario.
		final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
				"nameAUFFeedback", nameField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(nameFeedbackLabel);

		// construye el input field para el apellido
		final TextField<String> surnameField = new TextField<String>(
				"surnameAUF", new PropertyModel<String>(this, "surnameAUF"));
		surnameField.setRequired(true);
		surnameField.add(new ItemSimpleStringValidator(surnameField));
		surnameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.surname.length"))));
		this.add(surnameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del apellido del usuario.
		final FeedbackLabel surnameFeedbackLabel = new FeedbackLabel(
				"surnameAUFFeedback", surnameField);
		surnameFeedbackLabel.setOutputMarkupId(true);
		this.add(surnameFeedbackLabel);

		// construye la imagen indicadora para Ajax
		final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
		imageContainer.setOutputMarkupId(true);
		this.add(imageContainer);

		// construye el input field para el nombre de usuario
		final TextField<String> usernameField = new TextField<String>(
				"usernameAUF", new PropertyModel<String>(this, "usernameAUF"));
		usernameField.setRequired(true);
		usernameField.add(new ItemSimpleStringValidator(usernameField));
		usernameField.add(new FormComponentAjaxBehavior(this,
				"usernameAUFFeedback"));
		usernameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.username.length"))));
		this.add(usernameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del username del usuario.
		final FeedbackLabel usernameFeedbackLabel = new FeedbackLabel(
				"usernameAUFFeedback", usernameField);
		usernameFeedbackLabel.setOutputMarkupId(true);
		this.add(usernameFeedbackLabel);

		// construye el input field para la clave
		final PasswordTextField aPasswordField = new PasswordTextField(
				"passwordAUF", new PropertyModel<String>(this, "passwordAUF"));
		aPasswordField.setRequired(true);
		aPasswordField.setResetPassword(true);
		aPasswordField.add(new ItemSimpleStringValidator(aPasswordField));
		aPasswordField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.password.length"))));
		this.add(aPasswordField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente de la clave del usuario.
		final FeedbackLabel aPasswordFeedbackLabel = new FeedbackLabel(
				"passwordAUFFeedback", aPasswordField);
		aPasswordFeedbackLabel.setOutputMarkupId(true);
		this.add(aPasswordFeedbackLabel);

		// construye el input field para la confirmación de la clave
		final PasswordTextField aPasswordConfirmationField = new PasswordTextField(
				"passwordConfirmationAUF", new PropertyModel<String>(this,
						"passwordConfirmationAUF"));
		aPasswordConfirmationField.setRequired(true);
		aPasswordConfirmationField.add(new ItemSimpleStringValidator(
				aPasswordConfirmationField));
		aPasswordConfirmationField.setResetPassword(true);
		aPasswordConfirmationField.add(StringValidator
				.maximumLength(new Integer(this
						.getSystemProperty("AddUserForm.password.length"))));
		this.add(aPasswordConfirmationField);
		this.add(new EqualPasswordInputValidator(aPasswordField,
				aPasswordConfirmationField));

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente de la confirmación de la clave del usuario.
		final FeedbackLabel passwordConfirmationFeedbackLabel = new FeedbackLabel(
				"passwordConfirmationAUFFeedback", aPasswordConfirmationField);
		passwordConfirmationFeedbackLabel.setOutputMarkupId(true);
		this.add(passwordConfirmationFeedbackLabel);

		// construye el input field para el email
		final TextField<String> anEmailField = new TextField<String>(
				"emailAUF", new PropertyModel<String>(this, "emailAUF"));
		anEmailField.setRequired(true);
		anEmailField.add(EmailAddressValidator.getInstance());
		anEmailField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.email.length"))));
		this.add(anEmailField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del email del usuario.
		final FeedbackLabel emailFeedbackLabel = new FeedbackLabel(
				"emailAUFFeedback", anEmailField);
		emailFeedbackLabel.setOutputMarkupId(true);
		this.add(emailFeedbackLabel);

		// agrega las listas para seleccionar los grupos de usuarios a los
		// que pertenecerá el usuario
		List<UserGroupDTO> usergroups = this
				.constructUserGroupsPaletteAvailableElements();
		IChoiceRenderer renderer = new ChoiceRenderer("toString", "oid");

		final Palette usergroupsPalette = new Palette("usergroupsPalette",
				new Model((Serializable) new ArrayList<UserGroupDTO>()),
				new Model((Serializable) usergroups), renderer, 6, true);
		this.add(usergroupsPalette);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				try {

					AddUserForm.this.getUsersService().addUser(getNameAUF(),
							getUsernameAUF(), getPasswordAUF(), getEmailAUF(),
							new Date(), getLocale().getLanguage(),
							getSurnameAUF(),
							usergroupsPalette.getSelectedChoices());

					setResponsePage(AddUserPage.class);
				} catch (UsernameNotUniqueException nuue) {
					nuue.printStackTrace();
					error(getString("UsernameNotUniqueException"));
				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(submitLink);

		// construye el link de cancelación del formulario
		Link cancelLink = new BookmarkablePageLink("cancelLink",
				UsersAdministrationPage.class);
		this.add(cancelLink);

	}

	/**
	 * Getter.
	 * 
	 * @return el email ingresado por el usuario.
	 */
	public String getEmailAUF() {
		return this.emailAUF;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el email ingresado por el usuario.
	 */
	public void setEmailAUF(String anEmail) {
		this.emailAUF = anEmail;
	}

	/**
	 * Getter.
	 * 
	 * @return es el username ingresado por el usuario.
	 */
	public String getUsernameAUF() {
		return this.usernameAUF;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el username ingresado por el usuario.
	 */
	public void setUsernameAUF(String anUsername) {
		this.usernameAUF = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return la password ingresada por el usuario.
	 */
	public String getPasswordAUF() {
		return this.passwordAUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aPassword
	 *            es la clave ingresada por el usuario.
	 */
	public void setPasswordAUF(String aPassword) {
		this.passwordAUF = aPassword;
	}

	/**
	 * Getter.
	 * 
	 * @return la confirmación de la clave iongresada por el usuario.
	 */
	public String getPasswordConfirmationAUF() {
		return this.passwordConfirmationAUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aPasswordConfirmation
	 *            es la confirmación de la clave iongresada por el usuario.
	 */
	public void setPasswordConfirmationAUF(String aPasswordConfirmation) {
		this.passwordConfirmationAUF = aPasswordConfirmation;
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo username por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de usuarios para verificar que no exista
	 * un usuario con el username ingresado. En caso de que exista ya un usuario
	 * se muestra un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (this.getUsersService().existsUserWithUsername(
					this.getUsernameAUF())) {

				this.get("usernameAUF").error(
						getString("UsernameNotUniqueException"));

			}

		} catch (Exception e) {
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
	 * Getter.
	 * 
	 * @return el nombre del nuevo usuario.
	 */
	public String getNameAUF() {
		return this.nameAUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del nuevo usuario.
	 */
	public void setNameAUF(String aName) {
		this.nameAUF = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el apellido del usuario.
	 */
	public String getSurnameAUF() {
		return this.surnameAUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aSurname
	 *            es el apellido del usuario.
	 */
	public void setSurnameAUF(String aSurname) {
		this.surnameAUF = aSurname;
	}

	/**
	 * Recupera todos los grupos de usuarios disponibles a partir del servicio
	 * de usuarios.
	 * 
	 * @return una colección que contiene los dtos de los grupos de usuarios
	 *         disponibles para el nuevo usuario.
	 */
	private List<UserGroupDTO> constructUserGroupsPaletteAvailableElements() {

		UsersServiceBI usersService = this.getUsersService();
		List<UserGroupDTO> allUserGroups = new ArrayList<UserGroupDTO>();

		try {

			allUserGroups.addAll(usersService.getAllUsergroups());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allUserGroups;
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

}
