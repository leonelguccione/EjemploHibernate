/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.EditItemTypeForm;
import zinbig.item.util.Utils;

/**
 * Las instancias de esta clase se utilizan para editar la informaci�n de los
 * tipos de �tems. <br>
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
	 *            es un diccionario que contiene todos los par�metros requeridos
	 *            para la creaci�n de esta p�gina.
	 */
	public EditItemTypePage(PageParameters parameters) {
		super();
		try {
			// agrega el link que permite volver a ver todos los tipos de �tems
			// ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ItemTypesAdministrationPage.class);
			this.add(aLink);

			String itemTypeOid = Utils.decodeString(parameters
					.getString("itemTypeOid"));

			// agrega el formulario de edici�n de nuevo usuario.
			this.add(new EditItemTypeForm("editItemTypeForm", itemTypeOid));
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(DashboardPage.class);
		}

	}

}
