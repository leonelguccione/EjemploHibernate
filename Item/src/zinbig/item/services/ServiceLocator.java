/**
 * Este paquete contiene las definiciones de las interfaces de negocio y sus 
 * implementaciones que deberán implementar todos los servicios.
 */
package zinbig.item.services;

import zinbig.item.services.bi.EmailServiceBI;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.PrioritiesServiceBI;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.services.bi.WorkflowsServiceBI;

/**
 * La única instancia de esta clase (patrón de diseño Singleton) se utiliza para
 * acceder a las implementaciones de los servicios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ServiceLocator {

	/**
	 * Es la única instancia de esta clase. Es una implementación de patrón de
	 * diseño Singleton.
	 */
	private static ServiceLocator instance;

	/**
	 * Es una implementación del servicio vinculado con los usuarios.
	 */
	private UsersServiceBI usersService;

	/**
	 * Es una implementación del servicio vinculado con los proyectos.
	 */
	private ProjectsServiceBI projectsService;

	/**
	 * Es una implementación del servicio vinculado con los ítems.
	 */
	private ItemsServiceBI itemsService;

	/**
	 * Es una implementación del servicio vinculado con las prioridades.
	 */
	private PrioritiesServiceBI prioritiesService;

	/**
	 * Es una implementación del servicio vinculado con los workflows.
	 */
	private WorkflowsServiceBI workflowsService;

	/**
	 * Es una implementación del servicio vinculado con los emails.
	 */
	private EmailServiceBI emailService;

	/**
	 * Constructor.<br>
	 * 
	 * Este constructor es privado para asegurar que no se pueda utilizar
	 * directamente. Para obtener la única instancia de esta clase se debe
	 * utilizar el método getInstance().
	 */
	private ServiceLocator() {
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del servicio de usuarios.
	 */
	public UsersServiceBI getUsersService() {
		return this.usersService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es la implementación del servicio de usuarios.
	 */
	public void setUsersService(UsersServiceBI aService) {
		this.usersService = aService;
	}

	/**
	 * Método estático que permite acceder a la única instancia de esta clase.
	 * 
	 * @return la única instancia de esta clase.
	 */
	public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
		}
		return instance;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del servicio para proyectos.
	 */
	public ProjectsServiceBI getProjectsService() {
		return this.projectsService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es la implementación del servicio de proyectos.
	 */
	public void setProjectsService(ProjectsServiceBI aService) {
		this.projectsService = aService;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del servicio de ítems.
	 */
	public ItemsServiceBI getItemsService() {
		return this.itemsService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es la implementación del servicio de ítems.
	 */
	public void setItemsService(ItemsServiceBI aService) {
		this.itemsService = aService;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del servicio de prioridades.
	 */
	public PrioritiesServiceBI getPrioritiesService() {
		return this.prioritiesService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es la implementación del servicio de prioridades.
	 */
	public void setPrioritiesService(PrioritiesServiceBI aService) {
		this.prioritiesService = aService;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del servicio de workflows.
	 */
	public WorkflowsServiceBI getWorkflowsService() {
		return this.workflowsService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es la implementación del servicio de workflows.
	 */
	public void setWorkflowsService(WorkflowsServiceBI aService) {
		this.workflowsService = aService;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del servicio de emails.
	 */
	public EmailServiceBI getEmailService() {
		return this.emailService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es la implementación del servicio de emails.
	 */
	public void setEmailService(EmailServiceBI aService) {
		this.emailService = aService;
	}

}
