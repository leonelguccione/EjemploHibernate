/**
 * Este paquete contiene clases �tiles para la validaci�n de la informaci�n 
 * ingresada por el usuario.
 */
package zinbig.item.util.validators;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Las instancias de esta clase se utilizan para validar que el contenido del
 * componente recibido en el constructor sea un String que solamente contenga
 * letras min�sculas y may�sculas (con acentos), caracteres con acentos, signo
 * menos (-), espacio y signo underscore (_). <br>
 * Cualquier otro caract�r ser� rechazado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemSimpleStringValidator extends StringValidator implements
		Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 3807808627789625226L;

	/**
	 * Es el componente al cual se adjunt� este validador. Se utiliza para poder
	 * encontrar el mensaje I18N del error.
	 */
	public FormComponent<String> component;

	/**
	 * Constructor.
	 * 
	 * @param aComponent
	 *            es el componente al cual se agreg� este validador.
	 */
	public ItemSimpleStringValidator(FormComponent<String> aComponent) {
		this.setComponent(aComponent);
	}

	/**
	 * Este m�todo es invocado como parte de la cadena de validaci�n del
	 * contenido del componente al cual se adjunt�.
	 */
	@Override
	protected void onValidate(IValidatable<String> aValidateble) {

		if (!Pattern
				.matches(
						"[a-zA-Z0-9\\s\\.\\&\\%\\$\\#\\*\\+\\:\\;\\,\\=\\/\\[\\]\\'\\\"\\-\\(\\)\\?\\!\\�\\�\\_\\-������������]+",
						aValidateble.getValue())) {
			error(aValidateble, "formatValidationError");
		}
	}

	/**
	 * Getter.
	 * 
	 * @return el componente al cual se adjunt� este validador.
	 */
	public FormComponent<String> getComponent() {
		return this.component;
	}

	/**
	 * Setter.
	 * 
	 * @param aComponent
	 *            es el componente al cual se agreg� este validador.
	 */
	public void setComponent(FormComponent<String> aComponent) {
		this.component = aComponent;
	}

}
