/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.EditUserGroupForm;
import zinbig.item.util.Utils;

/**
 * Las instancias de esta clase se utilizan para editar la información de los
 * grupos de usuarios. <br>
 * A esta página se puede acceder por dos caminos: inmediatamente a partir del
 * alta de un nuevo grupo (botón Guardar y Editar) o mediante el listado de
 * todos los grupos existentes al seleccionar uno en particular para su edición.<br>
 * Al editar un grupo no se permite cambiar el tipo del grupo, el resto de los
 * datos se puede cambiar siempre que no se violen las reglas del dominio (como
 * que no se repitan dos grupos con el mismo nombre de grupo).<br>
 * Esta página requiere la presencia de un nombre de un grupo en el diccionario
 * que se recibe como parámetro en el constructor. <br>
 * Se debe guardar siempre el nombre actual del grupo ya que la búsqueda del
 * grupo para su edición se realizará por nombre.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditUserGroupPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            es un diccionario que contiene todos los parámetros requeridos
	 *            para la creación de esta página.
	 */
	public EditUserGroupPage(PageParameters parameters) {
		super();

		try {
			String aName = Utils.decodeString(parameters
					.getString("userGroupName"));

			// agrega el panel de edición de grupos de usuarios
			// agrega el formulario de edición de grupos de usuarios
			EditUserGroupForm aForm = new EditUserGroupForm(
					"editUserGroupForm", aName);
			this.add(aForm);

			// agrega el link que permite volver a ver todos los grupos de
			// usuarios
			// ya existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", UserGroupsAdministrationPage.class);
			this.add(aLink);
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}

	}

}
