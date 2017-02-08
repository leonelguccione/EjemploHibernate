/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.AddItemTypeForm;

/**
 * Las instancias de esta clase se utilizan para permitir a los usuarios dar de
 * alta nuevos tipos de ítems. <br>
 * Esta clase extiende la clase SecuredPage ya que es necesario ingresar al
 * sistema y ser identificado para poder dar de alta ítems.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddItemTypePage extends SecuredPage {

	/**
	 * Constructor.
	 */
	public AddItemTypePage() {
		super();
		try {
			// agrega el link que permite volver a ver todos los tipos de ítems
			// ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ItemTypesAdministrationPage.class);
			this.add(aLink);

			// agrega el formulario de alta de nuevo usuario.
			this.add(new AddItemTypeForm("addItemTypeForm", null));
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(DashboardPage.class);
		}
	}

}
