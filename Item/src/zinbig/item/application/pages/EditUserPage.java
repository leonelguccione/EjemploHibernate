/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.EditUserForm;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta página se utilizan para editar la información de un
 * usuario de la aplicación. <br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditUserPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param pageParameters
	 *            son los parámetros enviados a esta página.
	 */
	public EditUserPage(PageParameters pageParameters) {
		super();

		try {
			// recupera información del usuario a editar
			String anUsername = Utils.decodeString(pageParameters
					.getString("username"));

			UserDTO dto = this.getUsersService().findUserWithUsername(
					anUsername, "C");

			// agrega el link que permite volver a ver todos los usuarios ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", UsersAdministrationPage.class);
			this.add(aLink);

			// este control se realiza para que cuando el administrador esté
			// editando otros usuarios tenga un link para ver todos, pero en
			// cualquier otro caso no aparece este link.
			if (this.getUserDTO().isAdminUser() & !dto.isAdminUser()) {
				aLink.setVisible(true);
			} else {
				aLink.setVisible(false);
			}
			// agrega el formulario de edición de usuario.
			this.add(new EditUserForm("editUserForm", dto));

		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(DashboardPage.class);
		}
	}

}
