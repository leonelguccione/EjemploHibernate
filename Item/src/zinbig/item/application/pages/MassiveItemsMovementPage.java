/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.MassiveItemsMovementForm;

/**
 * Las instancias de esta clase se utilizan para permitir a los usuarios mover
 * �tems a otro estado en forma masiva.<br>
 * Esta p�gina puede ser invocada desde dos lugares distintos, desde el link de
 * acciones adicionales en el listado de los �tems o desde el men�.<br>
 * Si se invoca desde el listado de �tems se pueden elegir algunos para realizar
 * el pasaje, los cuales ya figurar�n en el campo con los identificadores
 * cargados, permitiendo la incorporaci�n de m�s �tems.<Br>
 * En el caso de la ejecuci�n desde el men�, la lista de �tems aparece vac�a.
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
	 *            es un diccionario que contiene los par�metros requeridos para
	 *            la ejecuci�n.
	 */
	public MassiveItemsMovementPage(PageParameters parameters) {
		super();
		try {
			String filterOid = "";

			if (parameters.containsKey("FILTER_OID")) {
				filterOid = parameters.getString("FILTER_OID");
			}

			// agrega el link que permite volver a ver todos los �tems filtrados
			// en caso de venir de un listado.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ItemTypesAdministrationPage.class);
			this.add(aLink);
			aLink.setVisible(!filterOid.equals(""));

			// agrega el formulario para el pasaje masivo de �tems.
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
