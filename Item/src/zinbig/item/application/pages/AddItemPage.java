/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.AddItemForm;

/**
 * Las instancias de esta clase se utilizan para permitir a los usuarios dar de
 * alta nuevos �tems. <br>
 * Esta clase extiende la clase SecuredPage ya que es necesario ingresar al
 * sistema y ser identificado para poder dar de alta �tems.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddItemPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param params
	 *            es un mapa que contiene los par�metros pasados a esta p�gina.
	 */
	public AddItemPage(PageParameters params) {
		super();
		try {
			String itemId = "";
			if (params.containsKey("NEW_ITEM")) {
				itemId = params.getString("NEW_ITEM");
			}

			// agrega una etiqueta para mostrar el n�mero del �ltimo �tem
			// agregado.
			Label lastItemItem;
			if (itemId.equals("")) {
				lastItemItem = new Label("lastItemId", "");
			} else {
				lastItemItem = new Label("lastItemId", this
						.getString("AddItemForm.lastItemId")
						+ itemId);
			}
			this.add(lastItemItem);

			this.add(new AddItemForm("addItemForm", this, itemId, params));

			// agrega el link que permite volver a ver todos los usuarios ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ViewItemsPage.class);
			this.add(aLink);

			// si el usuario tiene permiso de ver todos los �tems entonces
			// aparece
			// el link
			aLink.setVisible(this
					.verifyPermissionAssigmentToUser("VIEW_ALL_ITEMS"));
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}
	}

}
