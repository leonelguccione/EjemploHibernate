/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.repositories.bi.OperationsRepositoryBI;
import zinbig.item.repositories.bi.PrioritiesRepositoryBI;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.repositories.bi.TrackerRepositoryBI;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.repositories.bi.WorkflowsRepositoryBI;
import zinbig.item.util.dto.DTOFactory;
import zinbig.item.util.persistence.Versionable;

/**
 * Esta clase act�a como el tope de la jerarqu�a de las clases que se utilizan
 * para acceder a la l�gica de negocios relacionada con el sistema.<br>
 * La l�gica de negocios propiamente dicha no se encuentra en los servicios sino
 * que est� definida en los objetos de modelo.<br>
 * Los colaboradores de esta clase se inyectan mediante IoC.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class BaseServiceImpl {
	/**
	 * Es una implementaci�n del patr�n de dise�o Repository que se utiliza para
	 * acceder en forma eficiente al modelo.<br>
	 * La mayor�a de las subclases de esta clase requieren este repositorio.
	 */
	protected TrackerRepositoryBI trackerRepository;

	/**
	 * Es el repositorio utilizado para acceder en forma eficiente a los
	 * proyectos.
	 */
	protected ProjectsRepositoryBI projectsRepository;

	/**
	 * Es el repositorio utilizado para acceder en forma eficiente a los
	 * proyectos.
	 */
	protected PrioritiesRepositoryBI prioritiesRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a los usuarios.
	 */
	protected UsersRepositoryBI usersRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a las operaciones.
	 */
	protected OperationsRepositoryBI operationsRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a los workflows y sus nodos.
	 */
	protected WorkflowsRepositoryBI workflowsRepository;

	/**
	 * Es una implementaci�n del patr�n de dise�o Repository utilizada para
	 * acceder en forma eficiente a los �tems.
	 */
	protected ItemsRepositoryBI itemsRepository;

	/**
	 * Es un objeto que se utiliza para crear DTOs para los objetos del dominio.
	 */
	protected DTOFactory dtoFactory;

	/**
	 * Getter.
	 * 
	 * @return la implementaci�n del patr�n de dise�o Repository que se utiliza
	 *         para acceder en forma eficiente a la ra�z del modelo.
	 */
	public TrackerRepositoryBI getTrackerRepository() {
		return this.trackerRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aTrackerRepository
	 *            es una implementaci�n del patr�n de dise�o Repository que se
	 *            debe utilizar para acceder en forma eficiente a la ra�z del
	 *            modelo.
	 */
	public void setTrackerRepository(TrackerRepositoryBI aTrackerRepository) {
		this.trackerRepository = aTrackerRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return un objeto que se utiliza para crear DTOs de los objetos de
	 *         modelo.
	 */
	public DTOFactory getDtoFactory() {
		return DTOFactory.getInstance();
	}

	/**
	 * Getter.
	 * 
	 * @param aDTOFactory
	 *            es un objeto que se utiliza para crear DTOs de los objetos de
	 *            modelo.
	 */
	public void setDtoFactory(DTOFactory aDTOFactory) {
		this.dtoFactory = aDTOFactory;
	}

	/**
	 * Verifica que el dto recibido representa la �ltima versi�n del objeto del
	 * modelo.
	 * 
	 * @param aModelObject
	 *            es el objeto del modelo.
	 * @param aDto
	 *            es el dto que representa al modelo.
	 * @return true en caso de que ambas versiones sean iguales; false en caso
	 *         contrario.
	 */
	public boolean verifyLatestVersion(Versionable aModelObject,
			Versionable aDto) {
		return aDto.getVersion() == aModelObject.getVersion();
	}

	/**
	 * Getter.
	 * 
	 * @return el repositorio utilizado para accederen forma eficiente a los
	 *         proyectos.
	 */
	public ProjectsRepositoryBI getProjectsRepository() {
		return this.projectsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es el repositorio que se utilizar� para acceder en forma
	 *            eficiente a los proyectos.
	 */
	public void setProjectsRepository(ProjectsRepositoryBI aRepository) {
		this.projectsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return el repositorio utilizado para accederen forma eficiente a los
	 *         conjuntos de prioridades.
	 */
	public PrioritiesRepositoryBI getPrioritiesRepository() {
		return this.prioritiesRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es el repositorio que se utilizar� para acceder en forma
	 *            eficiente a los conjuntos de prioridades.
	 */
	public void setPrioritiesRepository(PrioritiesRepositoryBI aRepository) {
		this.prioritiesRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementaci�n del patr�n de dise�o Repository utilizada para
	 *         acceder en forma eficiente a las operaciones.
	 * 
	 */
	public OperationsRepositoryBI getOperationsRepository() {
		return this.operationsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es el repositorio utilizado para acceder en forma eficiente a
	 *            las operaciones.
	 */
	public void setOperationsRepository(OperationsRepositoryBI aRepository) {
		this.operationsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementaci�n del patr�n de dise�o Repository utilizada para
	 *         acceder en forma eficiente a los usuarios.
	 */
	public UsersRepositoryBI getUsersRepository() {
		return this.usersRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es una implementaci�n del patr�n de dise�o Repository que se
	 *            debe utilizar para acceder en forma eficiente a los usuarios.
	 */
	public void setUsersRepository(UsersRepositoryBI aRepository) {
		this.usersRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository utilizada para
	 *         acceder en forma eficiente a los workflows.
	 */
	public WorkflowsRepositoryBI getWorkflowsRepository() {
		return this.workflowsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param aRepository
	 *            es una implementaci�n del patr�n de dise�o Repository
	 *            utilizada para acceder en forma eficiente a los workflows
	 */
	public void setWorkflowsRepository(WorkflowsRepositoryBI aRepository) {
		this.workflowsRepository = aRepository;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository utilizada para
	 *         acceder en forma eficiente a los �tems.
	 */
	public ItemsRepositoryBI getItemsRepository() {
		return this.itemsRepository;
	}

	/**
	 * Setter.
	 * 
	 * @param itemsRepository
	 *            es una implementaci�n del patr�n de dise�o Repository
	 *            utilizada para acceder en forma eficiente a los �tems.
	 */
	public void setItemsRepository(ItemsRepositoryBI itemsRepository) {
		this.itemsRepository = itemsRepository;
	}

}
