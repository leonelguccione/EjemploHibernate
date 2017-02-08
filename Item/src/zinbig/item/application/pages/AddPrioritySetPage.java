/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.AddPrioritySetForm;

/**
 * Las instancias de esta página se utilizan para registrar a un nuevo grupo de
 * prioridades. <br>
 * Est página presenta un link en la parte superior que permite volver al
 * listado de grupos de prioridades y luego presenta el formulario de alta de
 * nuevo grupo.<br>
 * Mediante una llamada AJAX se controla que el nombre del grupo no se esté
 * utilizando ya.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddPrioritySetPage extends SecuredPage {

	/**
	 * Constructor.
	 */
	public AddPrioritySetPage() {
		super();
		try {
			// agrega el link que permite volver a ver todos los proyectos ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", PrioritiesAdministrationPage.class);
			this.add(aLink);

			// agrega el formulario de alta de conjuntos de prioridades.
			this.add(new AddPrioritySetForm("addPrioritySetForm"));
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}
	}

}
