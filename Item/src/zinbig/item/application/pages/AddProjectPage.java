/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.AddProjectForm;

/**
 * Las instancias de esta p�gina se utilizan para registrar a un nuevo proyecto
 * de la aplicaci�n. <br>
 * Est p�gina presenta un link en la parte superior que permite volver al
 * listado de proyectos y luego presenta el formulario de alta de nuevo
 * proyecto.<br>
 * Mediante una llamada AJAX se controla que el nombre del proyecto no se est�
 * utilizando ya.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddProjectPage extends SecuredPage {

	/**
	 * Constructor.
	 */
	public AddProjectPage() {
		super();
		try {
			// agrega el link que permite volver a ver todos los proyectos ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", ProjectsAdministrationPage.class);
			this.add(aLink);

			// agrega el formulario de alta de proyectos.
			this.add(new AddProjectForm("addProjectForm"));
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}
	}

}
