/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicaci�n.
 */
package zinbig.item.application.forms;

import java.util.Collection;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.ItemSession;
import zinbig.item.application.components.FeedbackPanelFilter;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.EmailServiceBI;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.PrioritiesServiceBI;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Esta clase abstracta es la base de la jerarqu�a de formularios de la
 * aplicaci�n. <br>
 * Establece el protocolo com�n a todas las subclases, as� como provee ciertos
 * m�todos �tiles.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemForm extends Form<Object> {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -691850914725110667L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es la identificaci�n de este formulario.
	 */
	public ItemForm(String anId) {
		super(anId);
	}

	/**
	 * Getter.
	 * 
	 * @param aPropertyKey
	 *            es el nombre de la propiedad que se debe recuperar.
	 * @return la propiedad de sistema almacenada bajo la clave recibida.
	 */
	public String getSystemProperty(String aPropertyKey) {

		return ((ItemApplication) this.getApplication())
				.getSystemProperty(aPropertyKey);
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del usuario que ingreso al sistema.
	 */
	public UserDTO getUserDTO() {
		return ((ItemSession) this.getSession()).getUserDTO();
	}

	/**
	 * Verifica si el usuario logueado en la sesi�n tiene asignada una operaci�n
	 * con el nombre recibido.
	 * 
	 * @param anOperationName
	 *            es el nombre de la operaci�n para verificar.
	 * @return true en caso de que el usuario tenga asignada la opeeraci�n;
	 *         false en caso contrario.
	 */
	protected boolean verifyPermissionAssigmentToUser(String anOperationName) {
		boolean result = false;
		UserDTO userDTO = this.getUserDTO();
		if (userDTO != null
				&& userDTO.containsOperationWithName(anOperationName)) {
			result = true;
		}
		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementaci�n del servicio de usuarios que se debe utilizar.
	 */
	protected UsersServiceBI getUsersService() {
		return ServiceLocator.getInstance().getUsersService();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n de los servicios relacionados con los �tems.
	 */
	public ItemsServiceBI getItemsService() {
		return ServiceLocator.getInstance().getItemsService();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n de los servicios relacionados con los
	 *         proyectos.
	 */
	public ProjectsServiceBI getProjectsService() {
		return ServiceLocator.getInstance().getProjectsService();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n de los servicios relacionados con las
	 *         prioridades.
	 */
	public PrioritiesServiceBI getPrioritiesService() {
		return ServiceLocator.getInstance().getPrioritiesService();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n de los servicios relacionados con los
	 *         workflows.
	 */
	public WorkflowsServiceBI getWorkflowsService() {
		return ServiceLocator.getInstance().getWorkflowsService();
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto seleccionado como default.
	 */
	public ProjectDTO getProjectDTO() {
		return ((ItemSession) this.getSession()).getProjectDTO();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n de los servicios relacionados con los emails.
	 */
	public EmailServiceBI getEmailService() {
		return ServiceLocator.getInstance().getEmailService();
	}

	/**
	 * Crea un componente para mostrar los mensajes de errores con un filtro que
	 * solamente permite mostrar los mensajes recibidos.
	 * 
	 * @param someMessages
	 *            es una colecci�n que contiene los mensajes permitidos por el
	 *            filtro.
	 * @return el componente para mostrar los errores.
	 */
	protected FeedbackPanel createFeedbackPanel(Collection<String> someMessages) {
		FeedbackPanel feedback = new FeedbackPanel("feedback");

		IFeedbackMessageFilter filter = new FeedbackPanelFilter(someMessages);
		feedback.setFilter(filter);

		return feedback;

	}

}
