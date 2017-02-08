/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.MassiveItemsMovementForm;

/**
 * Las instancias de esta clase se utilizan para permitir a los usuarios mover
 * ítems a otro estado en forma masiva.<br>
 * Esta página puede ser invocada desde dos lugares distintos, desde el link de
 * acciones adicionales en el listado de los ítems o desde el menú.<br>
 * Si se invoca desde el listado de ítems se pueden elegir algunos para realizar
 * el pasaje, los cuales ya figurarán en el campo con los identificadores
 * cargados, permitiendo la incorporación de más ítems.<Br>
 * En el caso de la ejecución desde el menú, la lista de ítems aparece vacía.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MassiveItemsMovementPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            es un diccionario que contiene los parámetros requeridos para
	 *            la ejecución.
	 */
	public MassiveItemsMovementPage(PageParameters parameters) {
		super();
		try {
			String filterOid = "";

			if (parameters.containsKey("FILTER_OID")) {
				filterOid = parameters.getString("FILTER_OID");
			}

			// agrega el link que permite volver a ver todos los ítems filtrados
			// en caso de venir de un listado.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ItemTypesAdministrationPage.class);
			this.add(aLink);
			aLink.setVisible(!filterOid.equals(""));

			// agrega el formulario para el pasaje masivo de ítems.
			String somePreviousIds = "";
			if (parameters.containsKey("ITEMS_NOT_MOVED")) {
				somePreviousIds = parameters.getString("ITEMS_NOT_MOVED");
			}
			this.add(new MassiveItemsMovementForm("massiveForm",
					somePreviousIds));
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(DashboardPage.class);
		}
	}
}
