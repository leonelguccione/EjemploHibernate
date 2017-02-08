/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import zinbig.item.model.exceptions.ItemTypeInUserException;
import zinbig.item.model.exceptions.ItemTypeTitleNotUniqueException;
import zinbig.item.model.exceptions.OperationNotUniqueException;
import zinbig.item.model.exceptions.PasswordMismatchException;
import zinbig.item.model.exceptions.PriorityNameNotUniqueException;
import zinbig.item.model.exceptions.PrioritySetNameNotUniqueException;
import zinbig.item.model.exceptions.PrioritySetUnknownException;
import zinbig.item.model.exceptions.ProjectNameNotUniqueException;
import zinbig.item.model.exceptions.UserGroupNotUniqueException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.exceptions.UsernameNotUniqueException;
import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.PrioritySet;
import zinbig.item.model.projects.Project;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.repositories.RepositoryLocator;
import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.repositories.bi.OperationsRepositoryBI;
import zinbig.item.repositories.bi.PrioritiesRepositoryBI;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.util.IDGenerator;
import zinbig.item.util.security.EncryptionStrategy;

/**
 * Esta clase act�a como raiz de todo el modelo. Es responsable de mantener las
 * colecciones de usuarios del sistema.<br>
 * Esta clase implementa el patr�n de dise�o RootObject.<br>
 * Para acceder en forma eficiente a sus colecciones, esta clase utiliza
 * diferentes implementaciones del patr�n Repository.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Tracker {

	/**
	 * Es una colecci�n que contiene todos los usuarios del sistema.
	 */
	protected Collection<User> users;

	/**
	 * Es una colecci�n que contiene todas las operaciones del sistema.
	 */
	protected Collection<Operation> operations;

	/**
	 * Es la identificaci�n de este objeto.
	 */
	protected String oid;

	/**
	 * Es el administrador del sistema.
	 */
	protected User administrator;

	/**
	 * Es la �nica instancia de esta clase. Es una implementaci�n de patr�n de
	 * dise�o Singleton.
	 */
	private static Tracker instance;

	/**
	 * Es una colecci�n que contiene los proyectos administrados por la
	 * herramienta.
	 */
	protected Collection<Project> projects;

	/**
	 * Es una colecci�n que contiene todos los grupos de usuarios.<br>
	 * Estos grupos de usuarios no est�n asignados a ning�n proyecto, sino que
	 * son las definiciones de los grupos b�sicos.
	 */
	protected Collection<UserGroup> userGroups;

	/**
	 * Es una colecci�n que contiene todos los conjuntos de prioridades creados
	 * en el sistema.
	 */
	protected Collection<PrioritySet> prioritySets;

	/**
	 * Es una colecci�n que contiene todos los tipos de �tems creados en el
	 * sistema.
	 */
	protected Collection<ItemType> itemTypes;

	/**
	 * Es una colecci�n que contiene todas las definiciones de workflows creadas
	 * por la herramienta que se encuentran disponibles para los proyectos.
	 */
	protected Collection<WorkflowDescription> workflowDescriptions;

	/**
	 * Constructor.<br>
	 * Es Privado ya que solamente puede existir una �nica instancia de esta
	 * clase.
	 */
	public Tracker() {
		this.setOid(IDGenerator.getId());
		this.setUsers(new ArrayList<User>());
		this.setItemTypes(new ArrayList<ItemType>());
		this.setProjects(new ArrayList<Project>());
		this.setUserGroups(new ArrayList<UserGroup>());
		this.setOperations(new ArrayList<Operation>());
		this.setPrioritySets(new ArrayList<PrioritySet>());
		this.setWorkflowDescriptions(new ArrayList<WorkflowDescription>());
	}

	/**
	 * M�todo est�tico que permite acceder a la �nica instancia de esta clase.
	 * 
	 * @return la �nica instancia de esta clase.
	 */
	public static Tracker getInstance() {
		if (instance == null) {
			instance = new Tracker();
		}
		return instance;
	}

	/**
	 * Getter.
	 * 
	 * @return la identificaci�n de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es la identificaci�n de este objeto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return la colecci�n de usuarios de este administrador.
	 */
	public Collection<User> getUsers() {
		return this.users;
	}

	/**
	 * Setter.
	 * 
	 * @param someUsers
	 *            es una colecci�n que contiene los usuarios del sistema. Es
	 *            privado ya que no se pueden asignar los usuarios de esta
	 *            manera. Solamente mediante el m�todo "add".
	 */
	private void setUsers(Collection<User> someUsers) {
		this.users = someUsers;
	}

	/**
	 * Agrega un nuevo usuario a la colecci�n de usuarios del sistema.<br>
	 * No est� permitido tener dos usuarios con el mismo nombre de usuario
	 * (aunque este atributo no sea utilizado como clave).<br>
	 * Todo usuario agregado mediante este m�todo es eliminable.
	 * 
	 * 
	 * @param aName
	 *            es el nombre del nuevo usuario.
	 * @param username
	 *            es el nombre de usuario del nuevo usuario.
	 * 
	 * @param password
	 *            es la clave del nuevo usuario.
	 * 
	 * @param encryptionStrategy
	 *            es un algoritmo que deber� utilizarse para encriptar la clase
	 *            del nuevo usuario.
	 * @param aStatus
	 *            indica si el usuario est� confirmado o no. El usuario tiene un
	 *            determinado tiempo para confirmar su ingreso al sistema.
	 * @param anEmail
	 *            es el mail del usuario.
	 * @param aDate
	 *            es la fecha de creaci�n del usuario.
	 * @param aLanguage
	 *            es el nombre del idioma del usuario.
	 * @param aSurname
	 *            es el apellido de este usuario.
	 * @param userGroups
	 *            es una colecci�n que contiene los grupos de usuarios a los que
	 *            se debe agregar al usuario.
	 * 
	 * @return una instancia que representa al nuevo usuario.
	 * 
	 * @throws UsernameNotUniqueException
	 *             esta excepci�n puede ser lanzada en caso de que se intente
	 *             agregar un nuevo usuario con un nombre de usuario ya
	 *             existente.
	 */
	public User addUser(String aName, String username, String password,
			EncryptionStrategy encryptionStrategy, String aStatus,
			String anEmail, Date aDate, String aLanguage, String aSurname,
			Collection<UserGroup> userGroups) throws UsernameNotUniqueException {

		if (!this.getUsersRepository().containsUserWithUsername(this, username)) {
			String encryptedPassword = encryptionStrategy.encrypt(password);
			User newUser = new User(aName, username, encryptedPassword,
					aStatus, anEmail, aDate, aLanguage, aSurname, true);

			// agrega el usuario
			this.getUsers().add(newUser);

			// asocia al usuario con cada uno de los grupos de usuarios.
			Iterator<UserGroup> iterator = userGroups.iterator();
			UserGroup anUserGroup;
			while (iterator.hasNext()) {
				anUserGroup = iterator.next();
				anUserGroup.addUser(newUser);
				newUser.addUserGroup(anUserGroup);
			}

			return newUser;

		} else {

			throw new UsernameNotUniqueException();
		}

	}

	/**
	 * Intenta registrar un usuario en el sistema.
	 * 
	 * 
	 * @param anUsername
	 *            es el nombre de usuario del usuario que est� intentando
	 *            ingresar al sistema.
	 * @param aPassword
	 *            es la clave sin encriptar del usuario.
	 * @param encryptionStrategy
	 *            es el algoritmo de encriptaci�n que deber� utilizarse para
	 *            encriptar la clave recibida y as� compararla con la que se
	 *            encuentra almacenada.
	 * 
	 * @return una instancia de clase User que representa al usuario.
	 * @throws PasswordMismatchException
	 *             esta excepci�n puede ser lanzada en caso de que las claves no
	 *             concuerden.
	 * @throws UserUnknownException
	 *             esta excepci�n puede ser lanzada en caso de que no exista un
	 *             usuario con el nombre de usuario dado.
	 */
	public User loginUser(String anUsername, String aPassword,
			EncryptionStrategy encryptionStrategy)
			throws PasswordMismatchException, UserUnknownException {

		User user = this.getUsersRepository().findUserWithUsername(this,
				anUsername, "C");

		if (user != null) {

			String encryptedPassword = encryptionStrategy.encrypt(aPassword);

			if (!encryptedPassword.equals(user.getPassword())) {

				throw new PasswordMismatchException();
			}
		} else {

			throw new UserUnknownException();
		}
		return user;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colecci�n de
	 *         usuarios.
	 */
	private UsersRepositoryBI getUsersRepository() {
		return RepositoryLocator.getInstance().getUsersRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colecci�n de
	 *         �tems y tipos de �tems.
	 */
	private ItemsRepositoryBI getItemsRepository() {
		return RepositoryLocator.getInstance().getItemsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colecci�n de
	 *         proyectos.
	 */
	private ProjectsRepositoryBI getProjectsRepository() {
		return RepositoryLocator.getInstance().getProjectsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colecci�n de
	 *         conjuntos de prioridades.
	 */
	private PrioritiesRepositoryBI getPrioritiesRepository() {
		return RepositoryLocator.getInstance().getPrioritiesRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colecci�n de
	 *         operaciones.
	 */
	private OperationsRepositoryBI getOperationsRepository() {
		return RepositoryLocator.getInstance().getOperationsRepository();
	}

	/**
	 * Recupera un usuario con un nombre de usuario dado.
	 * 
	 * @param anUsername
	 *            es el nombre de usuario del usuario a buscar.
	 * @param status
	 *            indica el status que debe tener el usuario que se busca.
	 * @return un usuario con el nombre de usuario dado.
	 * @throws UserUnknownException
	 *             esta excepci�n puede ser lanzada cuando se busca un usuario
	 *             inexistente.
	 */
	public User findUserWithUsername(String anUsername, String status)
			throws UserUnknownException {

		User existingUser = this.getUsersRepository().findUserWithUsername(
				this, anUsername, status);
		return existingUser;

	}

	/**
	 * Getter.
	 * 
	 * @return el administrador del sistema.
	 */
	public User getAdministrator() {
		return this.administrator;
	}

	/**
	 * Setter.
	 * 
	 * @param anUser
	 *            es el administrador del sistema.
	 */
	public void setAdministrator(User anUser) {
		this.administrator = anUser;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todas las operaciones que pueden seer
	 *         asignadas a los roles.
	 */
	public Collection<Operation> getOperations() {
		return this.operations;
	}

	/**
	 * Setter.
	 * 
	 * @param someOperations
	 *            es una colecci�n que contiene todos las operaciones que se
	 *            podr�n asignar a los roles.
	 */
	public void setOperations(Collection<Operation> someOperations) {
		this.operations = someOperations;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todos los proyectos.
	 */
	public Collection<Project> getProjects() {
		return this.projects;
	}

	/**
	 * Setter.
	 * 
	 * @param someProjects
	 *            es una colecci�n que contiene todos los proyectos.
	 */
	public void setProjects(Collection<Project> someProjects) {
		this.projects = someProjects;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todos los grupos de usuarios.
	 */
	public Collection<UserGroup> getUserGroups() {
		return this.userGroups;
	}

	/**
	 * Setter.
	 * 
	 * @param someGroups
	 *            es una colecci�n que contiene todos los grupos de usuarios.
	 */
	public void setUserGroups(Collection<UserGroup> someGroups) {
		this.userGroups = someGroups;
	}

	/**
	 * Agrega una nueva operaci�n al sistema.<br>
	 * Existe una restricci�n del dominio que no permite que existan dos
	 * operaciones con el mismo nombre.
	 * 
	 * @param anOperation
	 *            es la operaci�n que se quiere agregar.
	 * @throws OperationNotUniqueException
	 *             esta excepci�n se levanta cuando se trata de agregar una
	 *             operaci�n con un nombre ya existente.
	 */
	public void addOperation(Operation anOperation)
			throws OperationNotUniqueException {
		OperationsRepositoryBI aRepository = this.getOperationsRepository();

		if (!aRepository.containsOperationWithName(this, anOperation.getName())) {

			this.getOperations().add(anOperation);

		} else {
			throw new OperationNotUniqueException();
		}
	}

	/**
	 * Agrega un nuevo grupo de usuarios a la colecci�n del sistema.<br>
	 * No est� permitido tener dos grupos de usuarios con el mismo nombre
	 * (aunque este atributo no sea utilizado como clave).<br>
	 * Todo grupo de usuarios agregado mediante este m�todo puede ser eliminado
	 * luego (deletable=true).
	 * 
	 * 
	 * @param aName
	 *            es el nombre del nuevo grupo de usuarios.
	 * @param anEmail
	 *            es el mail del grupo de usuarios.
	 * @param aLanguage
	 *            indica el lenguaje del nuevo grupo de usuarios.
	 * @return una instancia que representa al nuevo grupo de usuarios.
	 * 
	 * @throws UserGroupNotUniqueException
	 *             esta excepci�n puede ser lanzada en caso de que se intente
	 *             agregar un nuevo grupo de usuarios con un nombre ya
	 *             existente.
	 */
	public UserGroup addUserGroup(String aName, String anEmail, String aLanguage)
			throws UserGroupNotUniqueException {

		if (!this.getUsersRepository().containsUserGroupWithName(this, aName)) {

			UserGroup newUserGroup = new UserGroup(aName, anEmail, true,
					aLanguage);

			this.getUserGroups().add(newUserGroup);
			return newUserGroup;

		} else {

			throw new UserGroupNotUniqueException();
		}

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de grupos de usuarios de sistema y de proyectos que
	 *         existen.
	 */
	public int getUserGroupsCount() {

		return this.getUsersRepository().getUserGroupsCount();

	}

	/**
	 * Actualiza la informaci�n almacenada de un grupo de usuarios. <br>
	 * La colecci�n de operaciones recibida no se agrega al grupo de usuarios,
	 * sino que se asigna en forma completa.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que se debe actualizar.
	 * @param aName
	 *            es el nuevo nombre del grupo.
	 * @param anEmail
	 *            es el nuevo email del grupo.
	 * @param someOperations
	 *            es una colecci�n de operaciones que se deben asignar al grupo
	 *            de usuarios.
	 * @param someProjects
	 *            es una coleccion que contiene los proyectos del grupo de
	 *            usuarios.
	 * @throws UserGroupNotUniqueException
	 *             esta excepci�n puede levantarse si se est� editando un grupo
	 *             de usuarios de sistema y se le est� asignando un nombre de
	 *             otro grupo ya existente.
	 */
	public void updateUserGroup(UserGroup anUserGroup, String aName,
			String anEmail, Collection<Operation> someOperations,
			Collection<Project> someProjects)
			throws UserGroupNotUniqueException {

		// verifica si el nombre actual es el mismo que el nuevo. En caso de que
		// no sean iguales verifica que no exista otro grupo con el nuevo
		// nombre.
		if (!anUserGroup.getName().equals(aName)) {

			if (this.getUsersRepository()
					.containsUserGroupWithName(this, aName)) {
				throw new UserGroupNotUniqueException();
			}

		}

		// actualizo las operaciones
		anUserGroup.getOperations().clear();
		if (!someOperations.isEmpty()) {
			anUserGroup.getOperations().addAll(someOperations);
		}

		// actualizo los proyectos
		Iterator<Project> projects = anUserGroup.getProjects().iterator();
		Collection<Project> aux = new ArrayList<Project>();
		Project aProject = null;
		while (projects.hasNext()) {
			aProject = projects.next();
			if (!someProjects.contains(aProject)) {
				// el proyecto fue seleccionado para desvinculaci�n
				aProject.removeUserGroup(anUserGroup);
				aux.add(aProject);
			}

		}
		for (Project p : aux) {
			anUserGroup.removeProject(p);
		}

		// saco los proyectos actuales
		someProjects.removeAll(anUserGroup.getProjects());

		if (!someProjects.isEmpty()) {
			Iterator<Project> iterator = someProjects.iterator();

			while (iterator.hasNext()) {
				aProject = iterator.next();

				anUserGroup.getProjects().add(aProject);
				aProject.addUserGroup(anUserGroup);
			}
		}

		anUserGroup.setName(aName);
		anUserGroup.setEmail(anEmail);

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de usuarios de sistema y de proyectos que existen.
	 */
	public int getUsersCount() {

		return this.getUsersRepository().getUsersCount();

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos que existen.
	 */
	public int getProjectsCount() {
		return this.getProjectsRepository().getProjectsCount();
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de conjuntos de prioridades que existen.
	 */
	public int getPrioritySetsCount() {
		return this.getPrioritySets().size();
	}

	/**
	 * Agrega un nuevo proyecto al sistema.
	 * 
	 * @param aName
	 *            es el nombre del nuevo proyecto.
	 * @param aShortName
	 *            es el nombe corto del proyecto.
	 * @param aLink
	 *            es el link relacionado con el proyecto.
	 * @param isPublicProject
	 *            indica si el proyecto es p�blico o no.
	 * @param aPrioritySet
	 *            es el conjunto de prioridades asignado a este proyecto.
	 * @param aWorkflowDescription
	 *            es la descripci�n del workflow asociada a este proyecto.
	 * @param aClassName
	 *            es el nombre de la clase de la estrategia de asignaci�n de
	 *            responsables de los �tems.
	 * @param aProjectLeader
	 *            es el usuario seleccionado como l�der del proyecto.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los tipos de �tems para el nuevo
	 *            proyecto.
	 * @return la instancia del proyecto recientemente creada.
	 * 
	 * @throws ProjectNameNotUniqueException
	 *             esta excepci�n puede ser lanzada en caso de que se est�
	 *             intentando dar de alta un proyecto con un nombre que ya
	 *             existe.
	 */
	public Project addProject(String aName, String aShortName, String aLink,
			boolean isPublicProject, PrioritySet aPrioritySet,
			WorkflowDescription aWorkflowDescription, String aClassName,
			User aProjectLeader, Collection<ItemType> someItemTypes)
			throws ProjectNameNotUniqueException {

		if (!this.getProjectsRepository().containsProjectWithName(this, aName)) {

			Project aProject = new Project(aName, aShortName, new Date(),
					isPublicProject, aLink, aPrioritySet, aWorkflowDescription,
					aClassName, aProjectLeader, someItemTypes);

			aPrioritySet.increaseReferencesCount();
			// agrega el proyecto
			this.getProjects().add(aProject);

			return aProject;

		} else {

			throw new ProjectNameNotUniqueException();
		}

	}

	/**
	 * Getter.
	 * 
	 * @return la colecci�n que contiene los conjuntos de prioridades del
	 *         sistema.
	 */
	public Collection<PrioritySet> getPrioritySets() {
		return this.prioritySets;
	}

	/**
	 * Setter.
	 * 
	 * @param somePrioritySets
	 *            es una colecci�n que contiene todos los conjuntos de
	 *            prioridades del sistema.
	 */
	public void setPrioritySets(Collection<PrioritySet> somePrioritySets) {
		this.prioritySets = somePrioritySets;
	}

	/**
	 * Agrega un nuevo grupo de prioridades al sistema.
	 * 
	 * @param aName
	 *            es el nombre del nuevo proyecto.
	 * @param isDefaultPrioritySet
	 *            indica si es el conjunto de prioridades por defecto.
	 * @return la instancia del conjunto de prioridades recientemente creado.
	 * 
	 * @throws PrioritySetNameNotUniqueException
	 *             esta excepci�n puede ser lanzada en caso de que se est�
	 *             intentando dar de alta un grupo de prioridades con un nombre
	 *             que ya existe.
	 */
	public PrioritySet addPrioritySet(String aName, boolean isDefaultPrioritySet)
			throws PrioritySetNameNotUniqueException {

		if (!this.getPrioritiesRepository().containsPrioritySetWithName(this,
				aName)) {

			// actualiza el anterior conjunto de prioridades por defecto en caso
			// de que el nuevo conjunto sea considerado por defecto
			if (isDefaultPrioritySet) {
				try {
					(this.getPrioritiesRepository().getDefaultPrioritySet(this))
							.setDefaultPrioritySet(false);
				} catch (PrioritySetUnknownException e) {
					// no deberia llegarse nunca a este punto ya que si no se
					// encuentra un conjunto de prioridades por defecto es una
					// situacion anormal de la aplicacion
					e.printStackTrace();
				}
			}
			PrioritySet aPrioritySet = new PrioritySet(aName,
					isDefaultPrioritySet);

			// agrega el conjunto de prioridades
			this.getPrioritySets().add(aPrioritySet);

			return aPrioritySet;

		} else {

			throw new PrioritySetNameNotUniqueException();
		}

	}

	/**
	 * Actualiza la informaci�n de un usuario.<br>
	 * Como desde la interfaz gr�fica no se permite editar el username, en este
	 * m�todo no es necesario controlar la unicidad del username.
	 * 
	 * @param anUser
	 *            es el usuario que se debe actualizar.
	 * @param name
	 *            es el nuevo nombre del usuario.
	 * @param surname
	 *            es el apellido del usuario.
	 * @param encrypt
	 *            es la clave del usuario.
	 * @param email
	 *            es el nuevo email del usuario.
	 * @param language
	 *            es el lenguaje seleccionado del usuario.
	 * @param userGroups
	 *            es una colecci�n que contiene los grupos de usuarios asignados
	 *            al usuario.
	 */
	public void updateUser(User anUser, String name, String surname,
			String encrypt, String email, String language,
			Collection<UserGroup> userGroups) {

		anUser.setName(name);
		anUser.setSurname(surname);
		anUser.setPassword(encrypt);
		anUser.setEmail(email);
		anUser.setLanguage(language);

		if (userGroups != null) {

			// borra todos los grupos de usuarios actualmente asignados al
			// usuario.
			Iterator<UserGroup> currentIterator = anUser.getUserGroups()
					.iterator();
			UserGroup anUserGroup;
			while (currentIterator.hasNext()) {
				anUserGroup = currentIterator.next();
				anUserGroup.removeUser(anUser);
			}

			// agrega los grupos de usuarios seleccionados.
			Iterator<UserGroup> iterator = userGroups.iterator();
			while (iterator.hasNext()) {

				anUserGroup = iterator.next();
				anUserGroup.addUser(anUser);
			}
		}

	}

	/**
	 * Actualiza la informaci�n del proyecto.<Br>
	 * Se controla que el nombre del proyecto sea �nico en todo el sistema.
	 * 
	 * @param aProject
	 *            es el proyecto que se debe editar.
	 * @param aName
	 *            es el nuevo nombre del proyecto.
	 * @param aShortName
	 *            es la abreviatura del proyecto.
	 * @param aLink
	 *            es el link a la p�gina web del proyecto.
	 * @param isPublicProject
	 *            indica si el proyecto es p�blico o privado.
	 * @param anUser
	 *            es el nuevo l�der del proyecto.
	 * @param aStrategyClassName
	 *            es el nombre de la clase de la estrategia de asignaci�n del
	 *            primer responsable de los �tems recientemente creados.
	 * @throws ProjectNameNotUniqueException
	 *             esta excepci�n puede levantarse en caso de que el nuevo
	 *             nombre para el proyecto ya se est� utilizando.
	 */
	public void updateProject(Project aProject, String aName,
			String aShortName, String aLink, boolean isPublicProject,
			User anUser, String aStrategyClassName)
			throws ProjectNameNotUniqueException {

		if (!aProject.getName().equals(aName)) {
			// los nombres no coinciden as� que se debe verificar que no exista
			// otro nombre con el nombre nuevo
			if (this.getProjectsRepository().containsProjectWithName(this,
					aName)) {
				throw new ProjectNameNotUniqueException();
			} else {
				aProject.setName(aName);
			}
		}
		aProject.setShortName(aShortName);
		aProject.setProjectLink(aLink);
		aProject.setPublicProject(isPublicProject);
		aProject.setProjectLeader(anUser);
		aProject
				.setItemResponsibleAssignmentStrategyClassName(aStrategyClassName);

	}

	/**
	 * Actualiza la infomraci�n b�sica de un conjunto de prioridades.<br>
	 * Se controla que el nombre del conjunto sera �nico.
	 * 
	 * @param aPrioritySet
	 *            es el conjunto de prioridades que se debe actualizar.
	 * @param aName
	 *            es el nuevo nombre para el conjunto de prioridades.
	 * @param isDefaultPrioritySet
	 *            indica si este conjunto debe ser el conjunto por defecto.
	 * @throws PrioritySetNameNotUniqueException
	 *             esta excepci�n puede levantarse en caso de que el nuevo
	 *             nombre del conjunto de prioridades ya se est� utilizando.
	 */
	public void updatePrioritySet(PrioritySet aPrioritySet, String aName,
			boolean isDefaultPrioritySet)
			throws PrioritySetNameNotUniqueException {

		if (!aPrioritySet.getName().equals(aName)) {
			// se le est� asignando un nuevo nombre. se debe verificar su
			// unicidad

			if (this.getPrioritiesRepository().containsPrioritySetWithName(
					this, aName)) {

				throw new PrioritySetNameNotUniqueException();

			} else {
				aPrioritySet.setName(aName);
			}
		}

		// en caso de que el conjunto de prioridades recibido sea el
		// nuevo conjunto por defecto, se actualiza el conjunto anterior
		if (isDefaultPrioritySet) {
			try {
				PrioritySet currentDefaultPrioritySet = this
						.getPrioritiesRepository().getDefaultPrioritySet(this);
				currentDefaultPrioritySet.setDefaultPrioritySet(false);
			} catch (PrioritySetUnknownException e) {
				// no deber�a llegarse aqu� ya que siempre debe haber un
				// conjunto de prioridades por defecto
				e.printStackTrace();
			}
		}

		aPrioritySet.setDefaultPrioritySet(isDefaultPrioritySet);

	}

	/**
	 * Agrega una nueva prioridad al conjunto de prioridades recibido. Se debe
	 * controlar que para el conjunto de prioridades no exista ya una prioridad
	 * con el mismo nombre.
	 * 
	 * @param aPrioritySet
	 *            es el conjunto de prioridades al cual se debe agregar la nueva
	 *            prioridad.
	 * @param aName
	 *            es el nombre de la nueva prioridad.
	 * @param aValue
	 *            es el valor asociado a la nueva prioridad.
	 * 
	 * @throws PriorityNameNotUniqueException
	 *             esta excepci�n puede levantarse en caso de que ya exista una
	 *             prioridad en el conjunto con el nombre dado.
	 */
	public void addPriorityToPrioritySet(PrioritySet aPrioritySet,
			String aName, String aValue) throws PriorityNameNotUniqueException {

		if (aPrioritySet.containsPriorityWithName(aName)) {

			throw new PriorityNameNotUniqueException();

		} else {

			Priority aPriority = new Priority(aName, aValue);
			aPrioritySet.addPriority(aPriority);

		}

	}

	/**
	 * Crea un nuevo �tem dentro del proyecto seleccionado.<br>
	 * Debido a que es un nuevo elemento, no es necesario controlar ning�n
	 * aspecto.
	 * 
	 * @param anUser
	 *            es el usuario que ha creado este �tem.
	 * @param aProject
	 *            es el proyecto en el cual se debe agregar el nuevo �tem.
	 * @param aTitle
	 *            es el t�tulo del �tem.
	 * @param aDescription
	 *            es la descripci�n del �tem.
	 * @param aPriority
	 *            es la prioridad del nuevo �tem. Se debe aumentar la cantidad
	 *            de referencias a la prioridad de modo que no se la pueda
	 *            eliminar si alg�n �tem la referencia.
	 * @param aState
	 *            es el estado del �tem. Si el estado es CREADO entonces el nodo
	 *            del workflow que le corresponde es el nulo (NullObject); en
	 *            cambio si es ABIERTO le corresponde el nodo inicial del
	 *            workflow.
	 * @param aDate
	 *            es la fecha de creaci�n del �tem.
	 * @param responsibleUser
	 *            es el usuario o grupo de usuarios al cual el creador est�
	 *            solicitando la asignaci�n del nuevo �tem.
	 * @param anItemType
	 *            es el tipo del nuevo �tem.
	 * @param somePropertyDescriptions
	 *            es un diccionario que contiene los valores ingresados para
	 *            cada una de las propiedades adicionales del nuevo �tem.
	 * 
	 * @return el �tem recientemente creado.
	 */
	public Item addItem(User anUser, Project aProject, String aTitle,
			String aDescription, Priority aPriority, ItemStateEnum aState,
			Date aDate, AbstractUser responsibleUser, ItemType anItemType,
			Map<String, String> somePropertyDescriptions) {

		Item newItem = aProject.createItem(anUser, aTitle, aDescription,
				aPriority, aState, aDate, responsibleUser, anItemType,
				somePropertyDescriptions);

		return newItem;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene las descripciones de los workflows
	 *         disponibles.
	 */
	public Collection<WorkflowDescription> getWorkflowDescriptions() {
		return this.workflowDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colecci�n que contiene las descripciones de los
	 *            workflows disponibles.
	 */
	public void setWorkflowDescriptions(
			Collection<WorkflowDescription> aCollection) {
		this.workflowDescriptions = aCollection;
	}

	/**
	 * Borra el proyecto recibido. Para la capa del modelo, borrar un objeto
	 * significa romper los v�nculos que pueda tener este objeto con el resto de
	 * los objetos; por ejemplo para el caso de un proyecto, se deben desasociar
	 * los �tems de sus responsables, pero no se eliminan los items del proyecto
	 * ya que esto se har� como parte de las actividades del servicio y los
	 * repositorios.
	 * 
	 * @param aProject
	 *            es el proyecto que debe ser eliminado.
	 */
	public void deleteProject(Project aProject) {

		aProject.prepareForDeletion();
		this.getProjects().remove(aProject);

	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems del sistema.
	 */
	public Collection<ItemType> getItemTypes() {
		return this.itemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param someItemTypes
	 *            es una colecci�n que contiene todos los tipos de �tems del
	 *            sistema.
	 */
	public void setItemTypes(Collection<ItemType> someItemTypes) {
		this.itemTypes = someItemTypes;
	}

	/**
	 * Agrega un nuevo tipo de �tem al sistema.
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo tipo de �tem.
	 * @return la nueva instancia de tipo de �tem.
	 * @throws ItemTypeTitleNotUniqueException
	 *             esta excepci�n puede levantarse en caso de que se est�
	 *             tratando de agregar un nuevo tipo de �tem con un t�tulo ya
	 *             existente.
	 */
	public ItemType addItemType(String aTitle)
			throws ItemTypeTitleNotUniqueException {

		if (!this.getItemsRepository().containsItemTypeWithTitle(this, aTitle)) {

			ItemType newItemType = new ItemType(aTitle);

			// agrega el nuevo tipo de �tem
			this.getItemTypes().add(newItemType);

			return newItemType;

		} else {

			throw new ItemTypeTitleNotUniqueException();
		}

	}

	/**
	 * Edita los datos de un tipo de �tem. No pueden haber dos tipos de �tems
	 * con el mismo t�tulo.
	 * 
	 * @param anItemType
	 *            es el tipo de �tem que se est� editando.
	 * @param aTitle
	 *            es el t�tulo nuevo para el tipo de �tem.
	 */
	public void editTypeItem(ItemType anItemType, String aTitle)
			throws ItemTypeTitleNotUniqueException {
		if (!this.getItemsRepository().containsItemTypeWithTitle(this, aTitle)) {

			anItemType.setTitle(aTitle);

		} else {

			throw new ItemTypeTitleNotUniqueException();
		}

	}

	/**
	 * Borra un grupos de usuarios del sistema.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que debe ser eliminado.
	 */
	public void deleteUserGroup(UserGroup anUserGroup) {
		anUserGroup.prepareForDeletion();
		this.getUserGroups().remove(anUserGroup);

	}

	/**
	 * Borra el tipo de �tem recibido.<Br>
	 * Borrar un tipo de �tem implica eliminarlo del sistema as� como actualizar
	 * todos los enlaces definidos en el workflow para que eliminen este tipo de
	 * �tem de su definici�n.<Br>
	 * En teor�a no deber�a llegar a este m�todo un tipo de �tem que a�n tenga
	 * referencias (es decir �tems que lo apuntan).
	 * 
	 * @param anItemType
	 *            es el tipo de �tem que debe ser eliminado.
	 */
	public void deleteItemType(ItemType anItemType)
			throws ItemTypeInUserException {

		if (anItemType.getReferencesCount() != 0) {
			throw new ItemTypeInUserException();
		} else {
			this.getItemTypes().remove(anItemType);

			anItemType.prepareForDeletion();

			WorkflowDescription workflowDescription = this
					.getWorkflowDescriptions().iterator().next();
			workflowDescription.deleteItemType(anItemType);
		}

	}

	/**
	 * Elimina el usuario del sistema.<BR>
	 * En caso de que el usuario tenga �tems asignados o haya trabajado como
	 * responsable de �stos en alg�n momento, los �tems ser�n asignados al l�der
	 * de cada proyecto.<BR>
	 * El usuario ser� removido de los grupos de usuarios y de todos los nodos
	 * de los workflows de los distintos proyectos. Al ejecutar esta operaci�n
	 * no queda ning�n rastro de la existencia del usuario.
	 * 
	 * @param anUser
	 *            es el usuario que debe ser eliminado.
	 */
	public void deleteUser(User anUser) {

		// actualiza el resto de los proyectos en los que pudiera haber
		// trabajado el usuario. Los proyectos actuales del usuario se
		// actualizan por medio del mismo usuario.
		Collection<Project> otherProjects = new ArrayList<Project>();
		otherProjects.addAll(this.getProjects());
		otherProjects.removeAll(anUser.getProjects());
		Iterator<Project> projectsIterator = otherProjects.iterator();
		Project aProject = null;
		while (projectsIterator.hasNext()) {
			aProject = projectsIterator.next();
			aProject.removeUser(anUser);
		}

		// notifica al usuario que se prepare para ser eliminado.
		anUser.prepareForDeletion();

		// actualiza el sistema
		this.getUsers().remove(anUser);

		// notifica al workflow que elimine al usuario de sus nodos.
		WorkflowDescription workflowDescription = this
				.getWorkflowDescriptions().iterator().next();
		workflowDescription.deleteUser(anUser);

	}

	/**
	 * Elimina un conjunto de prioridades del sistema.
	 * 
	 * @param aPrioritySet
	 *            es el conjunto de prioridades que debe ser eliminado.
	 */
	public void deletePrioritySet(PrioritySet aPrioritySet) {

		// no se notifica a las prioridades de este conjunto as� se eliminan en
		// cascada.
		this.getPrioritySets().remove(aPrioritySet);
	}

}
