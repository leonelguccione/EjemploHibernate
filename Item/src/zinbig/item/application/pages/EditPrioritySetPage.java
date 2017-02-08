/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import zinbig.item.application.components.EditPrioritySetBasicDataPanel;
import zinbig.item.application.components.ManagePrioritiesPanel;
import zinbig.item.util.Utils;

/**
 * Las instancias de esta clase se utilizan para editar la información de los
 * grupos de prioridades. <br>
 * 
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditPrioritySetPage extends SecuredPage {

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            es un diccionario que contiene todos los parámetros requeridos
	 *            para la creación de esta página.
	 */
	public EditPrioritySetPage(PageParameters parameters) {
		super();

		try {

			final String prioritySetId = Utils.decodeString(parameters
					.getString("prioritySetId"));

			// agrega el link que permite volver a ver todos los usuarios ya
			// existentes.
			Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
					"viewAllLink", PrioritiesAdministrationPage.class);
			this.add(aLink);

			// cre las solapas para editar los datos básicos, agregar nuevas
			// prioridades y listarlas y posiblemente eliminarlas.
			List<ITab> tabs = new ArrayList<ITab>();
			tabs.add(new AbstractTab(new Model<String>(this
					.getString("editPrioritySetPage.basicData"))) {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Getter.
				 * 
				 * @return el panel que se utiliza para editar los detalles
				 *         básicos de un conjunto de prioridades.
				 */
				@Override
				public Panel getPanel(String arg0) {
					return new EditPrioritySetBasicDataPanel(arg0,
							prioritySetId);
				}

			});
			tabs.add(new AbstractTab(new Model<String>(this
					.getString("editPrioritySetPage.addNewPriority"))) {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Getter.
				 * 
				 * @return un panel que se utiliza para agregar nuevas
				 *         prioridades al conjunto de prioridades.
				 */
				@Override
				public Panel getPanel(String arg0) {
					return new ManagePrioritiesPanel(arg0, prioritySetId);
				}

			});

			this.add(new TabbedPanel("tabs", tabs));

		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}

	}

}
