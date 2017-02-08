/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import zinbig.item.util.dto.UserDTO;

/**
 * Esta clase actúa como raíz de la jerarquía de todas aquellas páginas que
 * requieren de un usuario en la sesión. <Br>
 * Esta página realiza controla si un usuario está logueado; en caso contrario
 * reenvía a la página de inicio.
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
