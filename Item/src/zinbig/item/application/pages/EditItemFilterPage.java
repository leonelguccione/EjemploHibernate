/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.EditItemFilterForm;

/**
 * Las instancias de esta clase se utilizan para editar los filtros de ítems ya
 * creados.<br>
 * Todos los campos del filtro pueden ser editados, salvo su nombre que lo
 * identifica unívocamente.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemFilterPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            es un diccionario que contiene todos los parámetros requeridos
	 *            para la creación de esta página.
	 */
	public EditItemFilterPage(PageParameters parameters) {
		super();
		try {

			String filterTitle = parameters.getString("FILTER_TITLE");
			Label filterTitleLabel = new Label("filterTitle", filterTitle);
			this.add(filterTitleLabel);

			// agrega el link que permite volver a ver los ítems filtrados
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ViewItemsPage.class, parameters);
			this.add(aLink);

			String aFilterOid = parameters.getString("FILTER_OID");

			// agrega el formulario de edición del filtro.
			this.add(new EditItemFilterForm("editItemFilterForm", aFilterOid));
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(DashboardPage.class);
		}

	}

}
