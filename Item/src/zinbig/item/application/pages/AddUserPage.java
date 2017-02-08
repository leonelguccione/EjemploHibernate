/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.AddUserForm;

/**
 * Las instancias de esta página se utilizan para registrar a un nuevo usuario
 * de la aplicación. <br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddUserPage extends SecuredPage {

	/**
	 * Constructor.
	 */
	public AddUserPage() {
		super();

		try {
			// agrega el link que permite volver a ver todos los usuarios ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", UsersAdministrationPage.class);
			this.add(aLink);

			// agrega el formulario de alta de nuevo usuario.
			this.add(new AddUserForm("addUserForm"));
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}
	}

}
