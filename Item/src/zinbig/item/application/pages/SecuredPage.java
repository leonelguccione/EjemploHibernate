/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import zinbig.item.util.dto.UserDTO;

/**
 * Esta clase act�a como ra�z de la jerarqu�a de todas aquellas p�ginas que
 * requieren de un usuario en la sesi�n. <Br>
 * Esta p�gina realiza controla si un usuario est� logueado; en caso contrario
 * reenv�a a la p�gina de inicio.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class SecuredPage extends BasePage {

	/**
	 * Constructor.
	 */
	public SecuredPage() {
		super();

		final UserDTO dto = this.getUserDTO();
		if (dto == null) {
			this.redirectToInterceptPage(new DashboardPage());
		}

	}

}
