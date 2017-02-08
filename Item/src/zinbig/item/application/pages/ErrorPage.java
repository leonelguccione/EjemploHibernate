/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

/**
 * Las instancias de esta clase se utilizan para mostrar condiciones de error en
 * la aplicaci�n.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ErrorPage extends BasePage {

	/**
	 * Constructor.
	 */
	public ErrorPage() {
		super();
	}

	/**
	 * Getter.
	 * 
	 * @return true ya que esta clase representa la p�gina de error.
	 */
	@Override
	public boolean isErrorPage() {
		return true;
	}

}
