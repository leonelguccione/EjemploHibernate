/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.Locale;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.RememberPasswordPage;
import zinbig.item.model.exceptions.PasswordMismatchException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para enviar la información que
 * permite ingresar a un usuario registrado en la aplicación.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class LoginForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -9067375250697726764L;

	/**
	 * Es el username que ingresa el usuario.
	 */

	public String usernameLF;

	/**
	 * Es la password que ingresa el usuario.
	 */
	public String passwordLF;

	/**
	 * Es un componente para el ingreso del usuario.
	 */
	public TextField<String> aTextField;

	/**
	 * Es un componente para el ingreso de la clave.
	 */
	public PasswordTextField aPasswordField;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el id de este componente.
	 */
	public LoginForm(String anId) {
		super(anId);

		// agrega el componente para el nombre del usuario
		aTextField = new TextField<String>("usernameLF",
				new PropertyModel<String>(this, "usernameLF"));
		aTextField.setRequired(true);
		aTextField.setOutputMarkupId(true);
		this.add(aTextField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre del usuario.
		final FeedbackLabel usernameFeedbackLabel = new FeedbackLabel(
				"usernameLFFeedback", aTextField);
		usernameFeedbackLabel.setOutputMarkupId(true);
		this.add(usernameFeedbackLabel);

		// agrega el componente para la clave del usuario
		aPasswordField = new PasswordTextField("passwordLF",
				new PropertyModel<String>(this, "passwordLF"));
		aPasswordField.setRequired(true);
		aPasswordField.setOutputMarkupId(true);
		this.add(aPasswordField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente de la clave del usuario.
		final FeedbackLabel passwordFeedbackLabel = new FeedbackLabel(
				"passwordLFFeedback", aPasswordField);
		passwordFeedbackLabel.setOutputMarkupId(true);
		this.add(passwordFeedbackLabel);

		// agrega el link para enviar el formulario
		Button submitButton = new Button("submitButton") {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía la información ingresada en este formulario.
			 * 
			 * 
			 */
			@Override
			public void onSubmit() {

				try {

					UserDTO dto = LoginForm.this.getUsersService().loginUser(
							LoginForm.this.getUsernameLF(),
							LoginForm.this.getPasswordLF());

					if (!this.continueToOriginalDestination()) {

						setResponsePage(DashboardPage.class);
					}

					// actualiza el idioma de la interfaz gráfica con el idioma
					// del usuario.
					this.getSession().setLocale(new Locale(dto.getLanguage()));

					// se registra de al usuario en la sesión
					((ItemSession) this.getSession()).setUserDTO(dto);
					if (dto.getDefaultProject() != null) {
						((ItemSession) this.getSession()).setProjectDTO(dto
								.getDefaultProject());
					}
					((ItemSession) this.getSession()).setMenu(null);

				} catch (UserUnknownException e) {// usuario no encontrado

					aTextField.error(this.getString("UserUnknownException"));
					e.printStackTrace();

				} catch (PasswordMismatchException e) {

					aPasswordField.error(this
							.getString("PasswordMismatchException"));
					e.printStackTrace();

				} catch (Exception e) {
					setResponsePage(ErrorPage.class);
					e.printStackTrace();
				}
			}
		};
		submitButton.setModel(new Model<String>(this
				.getString("loginForm.enter")));
		this.add(submitButton);

		// agrega el link para recordar la clave
		Link<String> passwordLink = new BookmarkablePageLink<String>(
				"rememberPasswordLink", RememberPasswordPage.class);
		this.add(passwordLink);

	}

	/**
	 * Getter.
	 * 
	 * @return el username ingresado.
	 */
	public String getUsernameLF() {
		return this.usernameLF;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el username ingresado.
	 */
	public void setUsernameLF(String anUsername) {
		this.usernameLF = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return la clave ingresada.
	 */
	public String getPasswordLF() {
		return this.passwordLF;
	}

	/**
	 * Setter.
	 * 
	 * @param aPassword
	 *            es la clave ingresada.
	 */
	public void setPasswordLF(String aPassword) {
		this.passwordLF = aPassword;
	}

}
