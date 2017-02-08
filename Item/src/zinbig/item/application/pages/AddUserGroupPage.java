/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.AddUserGroupForm;

/**
 * Las instancias de esta clase se utilizan para dar de alta un nuevo grupo de
 * usuarios.<br>
 * Esta página permite seleccionar el tipo del nuevo grupo (de sistema o de
 * proyectos), un nombre para el grupo (que no puede repetirse, por lo que se
 * verifica esta condición mediante una llamada AJAX) y un email opcional.<br>
 * Al final se puede dar de alta y volver a la misma página o dar de alta e
 * inmediatamente editar el nuevo grupo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddUserGroupPage extends SecuredPage {

	/**
	 * Constructor.
	 */
	public AddUserGroupPage() {
		super();

		try {
			// agrega el link que permite volver a ver todos los grupos ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", UserGroupsAdministrationPage.class);
			this.add(aLink);

			// agrega el formulario para el alta de grupos de usuarios.
			AddUserGroupForm aForm = new AddUserGroupForm("addUserGroupForm");
			this.add(aForm);
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}
	}

}
