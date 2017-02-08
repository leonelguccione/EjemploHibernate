/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

/**
 * Las instancias de esta clase se utilizan para mostrar condiciones de error en
 * la aplicación.
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
	 * @return true ya que esta clase representa la página de error.
	 */
	@Override
	public boolean isErrorPage() {
		return true;
	}

}
