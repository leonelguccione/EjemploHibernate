/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.AddUserGroupPage;
import zinbig.item.application.pages.EditUserGroupPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.model.exceptions.UserGroupNotUniqueException;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de alta en
 * forma básica un nuevo grupo de usuarios. <BR>
 * Básica significa cargar solamente el tipo del grupo de usuarios (para sistema
 * o para proyectos), un nombre (que no se puede repetir entre todos los demás
 * grupos, es decir que no puede haber otro grupo de usuarios de sistema o de
 * proyectos con el mismo nombre) y en forma opcional un email. <br>
 * Este formulario permite salvar el nuevo grupo y continuar cargando nuevos
 * grupos, o salvar el nuevo grupo e inmediatamente editarlo para poder
 * asociarle el resto de los datos que requiere.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddUserGroupForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -6042131074920742305L;

	/**
	 * Es un campo que almacena el nombre provisto por el nuevo grupo de
	 * usuarios.
	 */
	public String nameAUGF;

	/**
	 * Es el email provisto por el nuevo grupo de usuarios.
	 */
	public String emailAUGF;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 */
	public AddUserGroupForm(String anId) {
		super(anId);

		// construye la imagen indicadora para Ajax
		final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
		imageContainer.setOutputMarkupId(true);
		this.add(imageContainer);

		// construye el input field para el nombre del grupo de usuarios
		final TextField<String> nameField = new TextField<String>("nameAUGF",
				new PropertyModel<String>(this, "nameAUGF"));
		nameField.setRequired(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		nameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserGroupForm.name.length"))));
		nameField.add(new FormComponentAjaxBehavior(this, "nameAUGFFeedback"));

		this.add(nameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre del grupo.
		final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
				"nameAUGFFeedback", nameField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(nameFeedbackLabel);

		// construye el input field para el email
		final TextField<String> anEmailField = new TextField<String>(
				"emailAUGF", new PropertyModel<String>(this, "emailAUGF"));
		anEmailField.add(EmailAddressValidator.getInstance());
		anEmailField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddUserGroupForm.email.length"))));
		this.add(anEmailField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del email del grupo.
		final FeedbackLabel emailFeedbackLabel = new FeedbackLabel(
				"emailAUGFFeedback", anEmailField);
		emailFeedbackLabel.setOutputMarkupId(true);
		this.add(emailFeedbackLabel);

		// construye el link de envío del formulario. Este link retorna a la
		// misma página
		SubmitLink saveLink = new SubmitLink("saveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta un nuevo grupo de usuarios.
			 */
			@Override
			public void onSubmit() {
				try {

					AddUserGroupForm.this.getUsersService().addUserGroup(
							getNameAUGF(), getEmailAUGF(),
							getLocale().getLanguage());

					setResponsePage(AddUserGroupPage.class);

				} catch (UserGroupNotUniqueException ugnue) {
					AddUserGroupForm.this.get("nameAUGF").error(
							getString("UserGroupNotUniqueException"));
				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(saveLink);

		// construye el link de envío del formulario. Este link permite
		// editar el grupo recién creado.
		SubmitLink saveAndEditLink = new SubmitLink("saveAndEditLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta un nuevo grupo de usuarios.
			 */
			@Override
			public void onSubmit() {
				try {
					UserGroupDTO aDto;

					aDto = AddUserGroupForm.this.getUsersService()
							.addUserGroup(getNameAUGF(), getEmailAUGF(),
									getLocale().getLanguage());

					PageParameters parameters = new PageParameters();
					parameters.put("userGroupName", Utils.encodeString(aDto
							.getName()));
					setResponsePage(EditUserGroupPage.class, parameters);

				} catch (UserGroupNotUniqueException ugnue) {
					AddUserGroupForm.this.get("nameAUGF").error(
							getString("UserGroupNotUniqueException"));
				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(saveAndEditLink);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del grupo.
	 */
	public String getNameAUGF() {
		return this.nameAUGF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del grupo.
	 */
	public void setNameAUGF(String aName) {
		this.nameAUGF = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el email del grupo.
	 */
	public String getEmailAUGF() {
		return this.emailAUGF;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el email del grupo.
	 */
	public void setEmailAUGF(String anEmail) {
		this.emailAUGF = anEmail;
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo name por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de usuarios para verificar que no exista
	 * un usuario con el username ingresado. En caso de que exista ya un usuario
	 * se muestra un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (this.getUsersService().containsUserGroupWithName(nameAUGF)) {
				this.get("nameAUGF").error(
						getString("UserGroupNameNotUniqueException"));
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
