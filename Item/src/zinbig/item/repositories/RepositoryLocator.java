/**
 * Este paquete contiene las definiciones de las interfaces de negocio y sus 
 * implementaciones que deber�n respetar el patr�n de dise�o Repository.
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
 * La �nica instancia de esta clase (patr�n de dise�o Singleton) se utiliza para
 * acceder a las implementaciones de los Repository que se deben usar para
 * acceder en forma eficiente a las diferentes colecciones.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class RepositoryLocator {

	/**
	 * Es la �nica instancia de esta clase. Es una implementaci�n de patr�n de
	 * dise�o Singleton.
	 */
	private static RepositoryLocator instance;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a los usuarios.
	 */
	private UsersRepositoryBI usersRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a los �tems.
	 */
	private ItemsRepositoryBI itemsRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a las operaciones.
	 */
	private OperationsRepositoryBI operationsRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a los proyectos.
	 */
	private ProjectsRepositoryBI projectsRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a las prioridades.
	 */
	private PrioritiesRepositoryBI prioritiesRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a las descripciones de los workflows y sus
	 * nodos.
	 */
	private WorkflowsRepositoryBI workflowsRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente al objeto ra�z de esta aplicaci�n.
	 */
	private TrackerRepositoryBI trackerRepository;

	/**
	 * Constructor.<br>
	 * 
	 * Este constructor es privado para asegurar que no se pueda utilizar
	 * directamente. Para obtener la �nica instancia de esta clase se debe
	 * utilizar el m�todo getInstance().
	 */
	private RepositoryLocator() {
	}

	/**
	 * Getter.
	 * 
	 * @return la implementaci�n del patr�n Repository para acceder en forma
	 *         eficiente a los usuarios.
	 */
	public UsersRepositoryBI getUsersRepository() {
		return this.usersRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param usersRepository
	 *            asigna la implementaci�n del patr�n Repository para acceder en
	 *            forma eficiente a los usuarios.
	 */
	public void setUsersRepository(UsersRepositoryBI usersRepository) {
		this.usersRepository = usersRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementaci�n del patr�n Repository para acceder en forma
	 *         eficiente a las operaciones.
	 */
	public OperationsRepositoryBI getOperationsRepository() {
		return this.operationsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param operationsRepository
	 *            asigna la implementaci�n del patr�n Repository para acceder en
	 *            forma eficiente a las operaciones.
	 */
	public void setOperationsRepository(UsersRepositoryBI operationsRepository) {
		this.usersRepository = operationsRepository;
	}

	/**
	 * M�todo est�tico que permite acceder a la �nica instancia de esta clase.
	 * 
	 * @return la �nica instancia de esta clase.
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
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente a los proyectos.
	 */
	public ProjectsRepositoryBI getProjectsRepository() {
		return this.projectsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementaci�n del patr�n Repository utilizada para
	 *            acceder en forma eficiente a los proyectos.
	 */
	public void setProjectsRepository(ProjectsRepositoryBI aRepository) {
		this.projectsRepository = aRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementaci�n del patr�n Repository utilizada para
	 *            acceder en forma eficiente a las operaciones.
	 */
	public void setOperationsRepository(OperationsRepositoryBI aRepository) {
		this.operationsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente a las prioridades.
	 */
	public PrioritiesRepositoryBI getPrioritiesRepository() {
		return this.prioritiesRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementaci�n del patr�n Repository utilizada para
	 *            acceder en forma eficiente a las prioridades.
	 */
	public void setPrioritiesRepository(PrioritiesRepositoryBI aRepository) {
		this.prioritiesRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
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
	 *            es la implementaci�n del patr�n Repository utilizada para
	 *            acceder en forma eficiente a las descripciones de los
	 *            workflows y sus nodos.
	 */
	public void setWorkflowsRepository(WorkflowsRepositoryBI aRepository) {
		this.workflowsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente a los �tems.
	 */
	public ItemsRepositoryBI getItemsRepository() {
		return this.itemsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementaci�n del patr�n Repository utilizada para
	 *            acceder en forma eficiente a los �tems.
	 */
	public void setItemsRepository(ItemsRepositoryBI aRepository) {
		this.itemsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente al objeto ra�z.
	 */
	public TrackerRepositoryBI getTrackerRepository() {
		return this.trackerRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es la implementaci�n del patr�n Repository utilizada para
	 *            acceder en forma eficiente al objeto ra�z.
	 */
	public void setTrackerRepository(TrackerRepositoryBI aRepository) {
		this.trackerRepository = aRepository;
	}

}
