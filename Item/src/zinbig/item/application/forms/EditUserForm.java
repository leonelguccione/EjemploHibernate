/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.basic.Label;
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
import zinbig.item.application.pages.EditUserPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.UsersAdministrationPage;
import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase se utilizan para editar la información de un
 * usuario en la aplicación. <br>
 * El único campo que no se puede editar es el username.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditUserForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 4401951082584922758L;

	/**
	 * Es un campo que almacena el username del usuario.
	 */
	public String usernameEUF;

	/**
	 * Es el email del usuario.
	 */
	public String emailEUF;

	/**
	 * Mantiene la password idel usuario.
	 */
	public String passwordEUF;

	/**
	 * Mantiene la confirmación de la password del usuario.
	 */
	public String passwordConfirmationEUF;

	/**
	 * Es el nombre del usuario.
	 */
	public String nameEUF;

	/**
	 * Es el apellido del usuario.
	 */
	public String surnameEUF;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este formulario dentro del panel.
	 * @param userDTO
	 *            es el dto que representa al usuario que se debe editar.
	 */
	@SuppressWarnings("unchecked")
	public EditUserForm(String id, final UserDTO userDTO) {
		super(id);

		// construye el input field para el nombre
		final TextField<String> nameField = new TextField<String>("nameEUF",
				new PropertyModel<String>(this, "nameEUF"));
		nameField.setRequired(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		nameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.name.length"))));
		this.add(nameField);
		this.setNameEUF(userDTO.getName());

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre del usuario.
		final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
				"nameEUFFeedback", nameField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(nameFeedbackLabel);

		// construye el input field para el apellido
		final TextField<String> surnameField = new TextField<String>(
				"surnameEUF", new PropertyModel<String>(this, "surnameEUF"));
		surnameField.setRequired(true);
		surnameField.add(new ItemSimpleStringValidator(surnameField));
		surnameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.surname.length"))));
		this.add(surnameField);
		this.setSurnameEUF(userDTO.getSurname());

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del apellido del usuario.
		final FeedbackLabel surnameFeedbackLabel = new FeedbackLabel(
				"surnameEUFFeedback", surnameField);
		surnameFeedbackLabel.setOutputMarkupId(true);
		this.add(surnameFeedbackLabel);

		// construye el label para el nombre de usuario
		final Label usernameLabel = new Label("username", userDTO.getUsername());
		this.add(usernameLabel);
		this.setUsernameEUF(userDTO.getUsername());

		// construye el input field para la clave
		final PasswordTextField aPasswordField = new PasswordTextField(
				"passwordEUF", new PropertyModel<String>(this, "passwordEUF"));
		aPasswordField.setRequired(true);
		aPasswordField.setResetPassword(true);
		aPasswordField.add(new ItemSimpleStringValidator(aPasswordField));
		aPasswordField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.password.length"))));
		this.add(aPasswordField);
		this.setPasswordEUF(userDTO.getPassword());

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente de la clave del usuario.
		final FeedbackLabel aPasswordFeedbackLabel = new FeedbackLabel(
				"passwordEUFFeedback", aPasswordField);
		aPasswordFeedbackLabel.setOutputMarkupId(true);
		this.add(aPasswordFeedbackLabel);

		// construye el input field para la confirmación de la clave
		final PasswordTextField aPasswordConfirmationField = new PasswordTextField(
				"passwordConfirmationEUF", new PropertyModel<String>(this,
						"passwordConfirmationEUF"));
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
		this.setPasswordConfirmationEUF(userDTO.getPassword());

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente de la confirmación de la clave del usuario.
		final FeedbackLabel passwordConfirmationFeedbackLabel = new FeedbackLabel(
				"passwordConfirmationEUFFeedback", aPasswordConfirmationField);
		passwordConfirmationFeedbackLabel.setOutputMarkupId(true);
		this.add(passwordConfirmationFeedbackLabel);

		// construye el input field para el email
		final TextField<String> anEmailField = new TextField<String>(
				"emailEUF", new PropertyModel<String>(this, "emailEUF"));
		anEmailField.setRequired(true);
		anEmailField.add(EmailAddressValidator.getInstance());
		anEmailField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserForm.email.length"))));
		this.add(anEmailField);
		this.setEmailEUF(userDTO.getEmail());

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del email del usuario.
		final FeedbackLabel emailFeedbackLabel = new FeedbackLabel(
				"emailEUFFeedback", anEmailField);
		emailFeedbackLabel.setOutputMarkupId(true);
		this.add(emailFeedbackLabel);

		// agrega las listas para seleccionar los grupos de usuarios a los
		// que pertenecerá el usuario
		final List<UserGroupDTO> assignedUsergroups = this
				.constructUserGroupsPaletteSelectedElements(userDTO);
		List<UserGroupDTO> usergroups = this
				.constructUserGroupsPaletteAvailableElements(assignedUsergroups);
		IChoiceRenderer renderer = new ChoiceRenderer("toString", "oid");

		final Palette usergroupsPalette = new Palette("usergroupsPalette",
				new Model((Serializable) assignedUsergroups), new Model(
						(Serializable) usergroups), renderer, 6, true);

		this.add(usergroupsPalette);

		// si el usuario no es administrador entonces no puede cambiar los
		// grupos a los que pertenece

		if (!this.getUserDTO().isAdminUser()) {
			usergroupsPalette.setVisible(false);
		} else {
			usergroupsPalette.setVisible(true);
		}

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				try {

					UserDTO dto = new UserDTO(userDTO.getOid(), userDTO
							.getUsername(), getPasswordEUF(), userDTO
							.getLanguage(), getEmailEUF(), getNameEUF(),
							getSurnameEUF(), userDTO.isDeletable(), userDTO
									.getVersion(), userDTO.isAdminUser(),
							userDTO.getFavoriteProjectsCount(), userDTO
									.isProjectLeader());

					Collection<UserGroupDTO> userGroupsDTOs = new ArrayList<UserGroupDTO>();
					if (usergroupsPalette.isVisible()) {
						Iterator<UserGroupDTO> iterator = usergroupsPalette
								.getSelectedChoices();

						while (iterator.hasNext()) {
							userGroupsDTOs.add(iterator.next());
						}
					}

					EditUserForm.this.getUsersService().updateUserInformation(
							dto, userGroupsDTOs);

					PageParameters pageParameters = new PageParameters();
					pageParameters.add("username", Utils.encodeString(dto
							.getUsername()));
					setResponsePage(EditUserPage.class, pageParameters);
				} catch (ItemConcurrentModificationException e) {
					e.printStackTrace();
					this.setResponsePage(ErrorPage.class);
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
	public String getEmailEUF() {
		return this.emailEUF;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el email ingresado por el usuario.
	 */
	public void setEmailEUF(String anEmail) {
		this.emailEUF = anEmail;
	}

	/**
	 * Getter.
	 * 
	 * @return es el username del usuario.
	 */
	public String getUsernameEUF() {
		return this.usernameEUF;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el username del usuario.
	 */
	public void setUsernameEUF(String anUsername) {
		this.usernameEUF = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return la password ingresada por el usuario.
	 */
	public String getPasswordEUF() {
		return this.passwordEUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aPassword
	 *            es la clave ingresada por el usuario.
	 */
	public void setPasswordEUF(String aPassword) {
		this.passwordEUF = aPassword;
	}

	/**
	 * Getter.
	 * 
	 * @return la confirmación de la clave ingresada por el usuario.
	 */
	public String getPasswordConfirmationEUF() {
		return this.passwordConfirmationEUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aPasswordConfirmation
	 *            es la confirmación de la clave iongresada por el usuario.
	 */
	public void setPasswordConfirmationEUF(String aPasswordConfirmation) {
		this.passwordConfirmationEUF = aPasswordConfirmation;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del nuevo usuario.
	 */
	public String getNameEUF() {
		return this.nameEUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del nuevo usuario.
	 */
	public void setNameEUF(String aName) {
		this.nameEUF = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el apellido del usuario.
	 */
	public String getSurnameEUF() {
		return this.surnameEUF;
	}

	/**
	 * Setter.
	 * 
	 * @param aSurname
	 *            es el apellido del usuario.
	 */
	public void setSurnameEUF(String aSurname) {
		this.surnameEUF = aSurname;
	}

	/**
	 * Recupera todos los grupos de usuarios disponibles a partir del servicio
	 * de usuarios.
	 * 
	 * @param alreadyAssignedUserGroups
	 *            es una colección que contiene los dtos de los grupos de
	 *            usuarios ya asignados al usuario.
	 * 
	 * @return una colección que contiene los dtos de los grupos de usuarios
	 *         disponibles para el nuevo usuario.
	 */
	private List<UserGroupDTO> constructUserGroupsPaletteAvailableElements(
			Collection<UserGroupDTO> alreadyAssignedUserGroups) {

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
	 * Recupera todos los grupos de usuarios ya asignados al usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * 
	 * @return una colección que contiene los dtos de los grupos de usuarios ya
	 *         asignados al usuario.
	 */
	private List<UserGroupDTO> constructUserGroupsPaletteSelectedElements(
			UserDTO anUserDTO) {

		UsersServiceBI usersService = this.getUsersService();
		List<UserGroupDTO> allUserGroups = new ArrayList<UserGroupDTO>();

		try {

			allUserGroups.addAll(usersService.getUserGroupsOfUser(anUserDTO));

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
