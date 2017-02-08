/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.forms.EditWorkflowNodeDescriptionForm;

/**
 * Las instancias de esta clase se utilizan para editar la información de los
 * nodos del workflow. <br>
 * 
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditWorkflowNodeDescriptionPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            es un diccionario que contiene todos los parámetros requeridos
	 *            para la creación de esta página.
	 */
	public EditWorkflowNodeDescriptionPage(PageParameters parameters) {
		super();

		try {

			// carga los valores requeridos para poder mostrar la página de
			// edición de proyecto.
			parameters.put("TAB", 4);
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"returnToProjectLink", EditProjectPage.class, parameters);
			this.add(aLink);

			String nodeOid = parameters.getString("NODE_OID");
			String projectOid = parameters.getString("projectOID");
			String workflowDescriptionOid = parameters
					.getString("workflowDescriptionOid");
			EditWorkflowNodeDescriptionForm aForm = new EditWorkflowNodeDescriptionForm(
					"editWorkflowNodeDescriptionForm", nodeOid,
					workflowDescriptionOid, projectOid);
			aForm.setOutputMarkupId(true);
			this.add(aForm);

		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}

	}
}
