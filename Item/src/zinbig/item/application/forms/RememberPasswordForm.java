/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de este formulario se utilizan para enviar claves a los
 * usuarios que las hayan olvidado. <br>
 * Previamente al envío por email, la password es generada en forma aleatoria y
 * almacenada encriptada.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class RememberPasswordForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2636993026468915623L;

	/**
	 * Es el nombre de usuario ingresado.
	 */
	protected String nameRPF;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es la identificación de este formulario.
	 */
	public RememberPasswordForm(String id) {
		super(id);

		// construye el input field para el username
		final TextField<String> nameField = new TextField<String>("nameRPF",
				new PropertyModel<String>(this, "nameRPF"));
		nameField.setRequired(true);
		nameField.setOutputMarkupId(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		this.add(nameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del usuario.
		final FeedbackLabel usernameFeedbackLabel = new FeedbackLabel(
				"feedbackPanel", nameField);
		usernameFeedbackLabel.setOutputMarkupId(true);
		this.add(usernameFeedbackLabel);

		// construye el link de envío del formulario. Este link retorna al
		// dashboard en caso de éxito
		SubmitLink saveLink = new SubmitLink("sendLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta un nuevo grupo de usuarios.
			 */
			@Override
			public void onSubmit() {

				UsersServiceBI service = RememberPasswordForm.this
						.getUsersService();
				try {
					UserDTO aDTO = service.findUserWithUsername(getNameRPF(),
							"C");

					String newPassword = RandomStringUtils.random(10, true,
							true);

					// actualiza el modelo con la nueva clave recién creada
					aDTO.setPassword(newPassword);
					service.updateUserInformation(aDTO, null);

					// envía el email de notificación
					RememberPasswordForm.this.getEmailService()
							.sendEmailToRememberPassword(aDTO);

					this.setResponsePage(DashboardPage.class);
				} catch (UserUnknownException e) {
					// no existe el usuario
					nameField.error(this.getString("UserUnknownException"));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		};
		this.add(saveLink);

	}

	/**
	 * Getter.
	 * 
	 * @return el valor del campo Usuario ingresado por el usuario.
	 */
	public String getNameRPF() {
		return this.nameRPF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el valor del campo ingresado por el usuario.
	 */
	public void setNameRPF(String aName) {
		this.nameRPF = aName;
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
