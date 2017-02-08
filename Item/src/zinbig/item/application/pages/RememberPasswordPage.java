/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import zinbig.item.application.forms.RememberPasswordForm;

/**
 * Las instancias de esta clase se utilizan para enviar la clave a los usuarios
 * que lo soliciten a trav�s del link "Recordar clave".
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class RememberPasswordPage extends BasePage {

	/**
	 * Constructor.
	 */
	public RememberPasswordPage() {
		super();
		// agrega el formulario para enviar la clave al usuario
		RememberPasswordForm aForm = new RememberPasswordForm("rememberForm");
		this.add(aForm);
	}

}
