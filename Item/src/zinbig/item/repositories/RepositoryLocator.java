/**
 * Este paquete contiene las definiciones de las interfaces de negocio y sus 
 * implementaciones que deberán respetar el patrón de diseño Repository.
 */
package zinbig.item.repositories;

import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.repositories.bi.OperationsRepositoryBI;
import zinbig.item.repositories.bi.PrioritiesRepositoryBI;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.repositories.bi.TrackerRepositoryBI;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.repositories.bi.WorkflowsRepositoryBI;

/**
 * La única instancia de esta clase (patrón de diseño Singleton) se utiliza para
 * acceder a las implementaciones de los Repository que se deben usar para
 * acceder en forma eficiente a las diferentes colecciones.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class RepositoryLocator {

	/**
	 * Es la única instancia de esta clase. Es una implementación de patrón de
	 * diseño Singleton.
	 */
	private static RepositoryLocator instance;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente a los usuarios.
	 */
	private UsersRepositoryBI usersRepository;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente a los ítems.
	 */
	private ItemsRepositoryBI itemsRepository;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente a las operaciones.
	 */
	private OperationsRepositoryBI operationsRepository;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente a los proyectos.
	 */
	private ProjectsRepositoryBI projectsRepository;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente a las prioridades.
	 */
	private PrioritiesRepositoryBI prioritiesRepository;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente a las descripciones de los workflows y sus
	 * nodos.
	 */
	private WorkflowsRepositoryBI workflowsRepository;

	/**
	 * Es una implementación del patrón de diseño Repository utilizada para
	 * acceder en forma eficiente al objeto raíz de esta aplicación.
	 */
	private TrackerRepositoryBI trackerRepository;

	/**
	 * Constructor.<br>
	 * 
	 * Este constructor es privado para asegurar que no se pueda utilizar
	 * directamente. Para obtener la única instancia de esta clase se debe
	 * utilizar el método getInstance().
	 */
	private RepositoryLocator() {
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del patrón Repository para acceder en forma
	 *         eficiente a los usuarios.
	 */
	public UsersRepositoryBI getUsersRepository() {
		return this.usersRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param usersRepository
	 *            asigna la implementación del patrón Repository para acceder en
	 *            forma eficiente a los usuarios.
	 */
	public void setUsersRepository(UsersRepositoryBI usersRepository) {
		this.usersRepository = usersRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación del patrón Repository para acceder en forma
	 *         eficiente a las operaciones.
	 */
	public OperationsRepositoryBI getOperationsRepository() {
		return this.operationsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param operationsRepository
	 *            asigna la implementación del patrón Repository para acceder en
	 *            forma eficiente a las operaciones.
	 */
	public void setOperationsRepository(UsersRepositoryBI operationsRepository) {
		this.usersRepository = operationsRepository;
	}

	/**
	 * Método estático que permite acceder a la única instancia de esta clase.
	 * 
	 * @return la única instancia de esta clase.
	 */
	public static RepositoryLocator getInstance() {
		if (instance == null) {
			instance = new RepositoryLocator();
		}
		return instance;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón Repository utilizado para acceder
	 *         en forma eficiente a los proyectos.
	 */
	public ProjectsRepositoryBI getProjectsRepository() {
		return this.projectsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementación del patrón Repository utilizada para
	 *            acceder en forma eficiente a los proyectos.
	 */
	public void setProjectsRepository(ProjectsRepositoryBI aRepository) {
		this.projectsRepository = aRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementación del patrón Repository utilizada para
	 *            acceder en forma eficiente a las operaciones.
	 */
	public void setOperationsRepository(OperationsRepositoryBI aRepository) {
		this.operationsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón Repository utilizado para acceder
	 *         en forma eficiente a las prioridades.
	 */
	public PrioritiesRepositoryBI getPrioritiesRepository() {
		return this.prioritiesRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementación del patrón Repository utilizada para
	 *            acceder en forma eficiente a las prioridades.
	 */
	public void setPrioritiesRepository(PrioritiesRepositoryBI aRepository) {
		this.prioritiesRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón Repository utilizado para acceder
	 *         en forma eficiente a las descripciones de los workflows y sus
	 *         nodos.
	 */
	public WorkflowsRepositoryBI getWorkflowsRepository() {
		return this.workflowsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementación del patrón Repository utilizada para
	 *            acceder en forma eficiente a las descripciones de los
	 *            workflows y sus nodos.
	 */
	public void setWorkflowsRepository(WorkflowsRepositoryBI aRepository) {
		this.workflowsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón Repository utilizado para acceder
	 *         en forma eficiente a los ítems.
	 */
	public ItemsRepositoryBI getItemsRepository() {
		return this.itemsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementación del patrón Repository utilizada para
	 *            acceder en forma eficiente a los ítems.
	 */
	public void setItemsRepository(ItemsRepositoryBI aRepository) {
		this.itemsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón Repository utilizado para acceder
	 *         en forma eficiente al objeto raíz.
	 */
	public TrackerRepositoryBI getTrackerRepository() {
		return this.trackerRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementación del patrón Repository utilizada para
	 *            acceder en forma eficiente al objeto raíz.
	 */
	public void setTrackerRepository(TrackerRepositoryBI aRepository) {
		this.trackerRepository = aRepository;
	}

}
