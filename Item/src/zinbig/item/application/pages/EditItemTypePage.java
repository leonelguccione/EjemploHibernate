/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.EditItemTypeForm;
import zinbig.item.util.Utils;

/**
 * Las instancias de esta clase se utilizan para editar la información de los
 * tipos de ítems. <br>
 * 
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemTypePage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            es un diccionario que contiene todos los parámetros requeridos
	 *            para la creación de esta página.
	 */
	public EditItemTypePage(PageParameters parameters) {
		super();
		try {
			// agrega el link que permite volver a ver todos los tipos de ítems
			// ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ItemTypesAdministrationPage.class);
			this.add(aLink);

			String itemTypeOid = Utils.decodeString(parameters
					.getString("itemTypeOid"));

			// agrega el formulario de edición de nuevo usuario.
			this.add(new EditItemTypeForm("editItemTypeForm", itemTypeOid));
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(DashboardPage.class);
		}

	}

}
