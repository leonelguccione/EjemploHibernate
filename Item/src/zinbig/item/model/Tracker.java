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
 * Esta clase actúa como raiz de todo el modelo. Es responsable de mantener las
 * colecciones de usuarios del sistema.<br>
 * Esta clase implementa el patrón de diseño RootObject.<br>
 * Para acceder en forma eficiente a sus colecciones, esta clase utiliza
 * diferentes implementaciones del patrón Repository.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Tracker {

	/**
	 * Es una colección que contiene todos los usuarios del sistema.
	 */
	protected Collection<User> users;

	/**
	 * Es una colección que contiene todas las operaciones del sistema.
	 */
	protected Collection<Operation> operations;

	/**
	 * Es la identificación de este objeto.
	 */
	protected String oid;

	/**
	 * Es el administrador del sistema.
	 */
	protected User administrator;

	/**
	 * Es la única instancia de esta clase. Es una implementación de patrón de
	 * diseño Singleton.
	 */
	private static Tracker instance;

	/**
	 * Es una colección que contiene los proyectos administrados por la
	 * herramienta.
	 */
	protected Collection<Project> projects;

	/**
	 * Es una colección que contiene todos los grupos de usuarios.<br>
	 * Estos grupos de usuarios no están asignados a ningún proyecto, sino que
	 * son las definiciones de los grupos básicos.
	 */
	protected Collection<UserGroup> userGroups;

	/**
	 * Es una colección que contiene todos los conjuntos de prioridades creados
	 * en el sistema.
	 */
	protected Collection<PrioritySet> prioritySets;

	/**
	 * Es una colección que contiene todos los tipos de ítems creados en el
	 * sistema.
	 */
	protected Collection<ItemType> itemTypes;

	/**
	 * Es una colección que contiene todas las definiciones de workflows creadas
	 * por la herramienta que se encuentran disponibles para los proyectos.
	 */
	protected Collection<WorkflowDescription> workflowDescriptions;

	/**
	 * Constructor.<br>
	 * Es Privado ya que solamente puede existir una única instancia de esta
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
	 * Método estático que permite acceder a la única instancia de esta clase.
	 * 
	 * @return la única instancia de esta clase.
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
	 * @return la identificación de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es la identificación de este objeto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de usuarios de este administrador.
	 */
	public Collection<User> getUsers() {
		return this.users;
	}

	/**
	 * Setter.
	 * 
	 * @param someUsers
	 *            es una colección que contiene los usuarios del sistema. Es
	 *            privado ya que no se pueden asignar los usuarios de esta
	 *            manera. Solamente mediante el método "add".
	 */
	private void setUsers(Collection<User> someUsers) {
		this.users = someUsers;
	}

	/**
	 * Agrega un nuevo usuario a la colección de usuarios del sistema.<br>
	 * No está permitido tener dos usuarios con el mismo nombre de usuario
	 * (aunque este atributo no sea utilizado como clave).<br>
	 * Todo usuario agregado mediante este método es eliminable.
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
	 *            es un algoritmo que deberá utilizarse para encriptar la clase
	 *            del nuevo usuario.
	 * @param aStatus
	 *            indica si el usuario está confirmado o no. El usuario tiene un
	 *            determinado tiempo para confirmar su ingreso al sistema.
	 * @param anEmail
	 *            es el mail del usuario.
	 * @param aDate
	 *            es la fecha de creación del usuario.
	 * @param aLanguage
	 *            es el nombre del idioma del usuario.
	 * @param aSurname
	 *            es el apellido de este usuario.
	 * @param userGroups
	 *            es una colección que contiene los grupos de usuarios a los que
	 *            se debe agregar al usuario.
	 * 
	 * @return una instancia que representa al nuevo usuario.
	 * 
	 * @throws UsernameNotUniqueException
	 *             esta excepción puede ser lanzada en caso de que se intente
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
	 *            es el nombre de usuario del usuario que está intentando
	 *            ingresar al sistema.
	 * @param aPassword
	 *            es la clave sin encriptar del usuario.
	 * @param encryptionStrategy
	 *            es el algoritmo de encriptación que deberá utilizarse para
	 *            encriptar la clave recibida y así compararla con la que se
	 *            encuentra almacenada.
	 * 
	 * @return una instancia de clase User que representa al usuario.
	 * @throws PasswordMismatchException
	 *             esta excepción puede ser lanzada en caso de que las claves no
	 *             concuerden.
	 * @throws UserUnknownException
	 *             esta excepción puede ser lanzada en caso de que no exista un
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
	 * @return una implementación del patrón de diseño Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colección de
	 *         usuarios.
	 */
	private UsersRepositoryBI getUsersRepository() {
		return RepositoryLocator.getInstance().getUsersRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón de diseño Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colección de
	 *         ítems y tipos de ítems.
	 */
	private ItemsRepositoryBI getItemsRepository() {
		return RepositoryLocator.getInstance().getItemsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón de diseño Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colección de
	 *         proyectos.
	 */
	private ProjectsRepositoryBI getProjectsRepository() {
		return RepositoryLocator.getInstance().getProjectsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón de diseño Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colección de
	 *         conjuntos de prioridades.
	 */
	private PrioritiesRepositoryBI getPrioritiesRepository() {
		return RepositoryLocator.getInstance().getPrioritiesRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón de diseño Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colección de
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
	 *             esta excepción puede ser lanzada cuando se busca un usuario
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
	 * @return una colección que contiene todas las operaciones que pueden seer
	 *         asignadas a los roles.
	 */
	public Collection<Operation> getOperations() {
		return this.operations;
	}

	/**
	 * Setter.
	 * 
	 * @param someOperations
	 *            es una colección que contiene todos las operaciones que se
	 *            podrán asignar a los roles.
	 */
	public void setOperations(Collection<Operation> someOperations) {
		this.operations = someOperations;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene todos los proyectos.
	 */
	public Collection<Project> getProjects() {
		return this.projects;
	}

	/**
	 * Setter.
	 * 
	 * @param someProjects
	 *            es una colección que contiene todos los proyectos.
	 */
	public void setProjects(Collection<Project> someProjects) {
		this.projects = someProjects;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene todos los grupos de usuarios.
	 */
	public Collection<UserGroup> getUserGroups() {
		return this.userGroups;
	}

	/**
	 * Setter.
	 * 
	 * @param someGroups
	 *            es una colección que contiene todos los grupos de usuarios.
	 */
	public void setUserGroups(Collection<UserGroup> someGroups) {
		this.userGroups = someGroups;
	}

	/**
	 * Agrega una nueva operación al sistema.<br>
	 * Existe una restricción del dominio que no permite que existan dos
	 * operaciones con el mismo nombre.
	 * 
	 * @param anOperation
	 *            es la operación que se quiere agregar.
	 * @throws OperationNotUniqueException
	 *             esta excepción se levanta cuando se trata de agregar una
	 *             operación con un nombre ya existente.
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
	 * Agrega un nuevo grupo de usuarios a la colección del sistema.<br>
	 * No está permitido tener dos grupos de usuarios con el mismo nombre
	 * (aunque este atributo no sea utilizado como clave).<br>
	 * Todo grupo de usuarios agregado mediante este método puede ser eliminado
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
	 *             esta excepción puede ser lanzada en caso de que se intente
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
	 * Actualiza la información almacenada de un grupo de usuarios. <br>
	 * La colección de operaciones recibida no se agrega al grupo de usuarios,
	 * sino que se asigna en forma completa.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que se debe actualizar.
	 * @param aName
	 *            es el nuevo nombre del grupo.
	 * @param anEmail
	 *            es el nuevo email del grupo.
	 * @param someOperations
	 *            es una colección de operaciones que se deben asignar al grupo
	 *            de usuarios.
	 * @param someProjects
	 *            es una coleccion que contiene los proyectos del grupo de
	 *            usuarios.
	 * @throws UserGroupNotUniqueException
	 *             esta excepción puede levantarse si se está editando un grupo
	 *             de usuarios de sistema y se le está asignando un nombre de
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
				// el proyecto fue seleccionado para desvinculación
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
	 *            indica si el proyecto es público o no.
	 * @param aPrioritySet
	 *            es el conjunto de prioridades asignado a este proyecto.
	 * @param aWorkflowDescription
	 *            es la descripción del workflow asociada a este proyecto.
	 * @param aClassName
	 *            es el nombre de la clase de la estrategia de asignación de
	 *            responsables de los ítems.
	 * @param aProjectLeader
	 *            es el usuario seleccionado como líder del proyecto.
	 * @param someItemTypes
	 *            es una colección que contiene los tipos de ítems para el nuevo
	 *            proyecto.
	 * @return la instancia del proyecto recientemente creada.
	 * 
	 * @throws ProjectNameNotUniqueException
	 *             esta excepción puede ser lanzada en caso de que se esté
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
	 * @return la colección que contiene los conjuntos de prioridades del
	 *         sistema.
	 */
	public Collection<PrioritySet> getPrioritySets() {
		return this.prioritySets;
	}

	/**
	 * Setter.
	 * 
	 * @param somePrioritySets
	 *            es una colección que contiene todos los conjuntos de
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
	 *             esta excepción puede ser lanzada en caso de que se esté
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
	 * Actualiza la información de un usuario.<br>
	 * Como desde la interfaz gráfica no se permite editar el username, en este
	 * método no es necesario controlar la unicidad del username.
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
	 *            es una colección que contiene los grupos de usuarios asignados
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
	 * Actualiza la información del proyecto.<Br>
	 * Se controla que el nombre del proyecto sea único en todo el sistema.
	 * 
	 * @param aProject
	 *            es el proyecto que se debe editar.
	 * @param aName
	 *            es el nuevo nombre del proyecto.
	 * @param aShortName
	 *            es la abreviatura del proyecto.
	 * @param aLink
	 *            es el link a la página web del proyecto.
	 * @param isPublicProject
	 *            indica si el proyecto es público o privado.
	 * @param anUser
	 *            es el nuevo líder del proyecto.
	 * @param aStrategyClassName
	 *            es el nombre de la clase de la estrategia de asignación del
	 *            primer responsable de los ítems recientemente creados.
	 * @throws ProjectNameNotUniqueException
	 *             esta excepción puede levantarse en caso de que el nuevo
	 *             nombre para el proyecto ya se esté utilizando.
	 */
	public void updateProject(Project aProject, String aName,
			String aShortName, String aLink, boolean isPublicProject,
			User anUser, String aStrategyClassName)
			throws ProjectNameNotUniqueException {

		if (!aProject.getName().equals(aName)) {
			// los nombres no coinciden así que se debe verificar que no exista
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
	 * Actualiza la infomración básica de un conjunto de prioridades.<br>
	 * Se controla que el nombre del conjunto sera único.
	 * 
	 * @param aPrioritySet
	 *            es el conjunto de prioridades que se debe actualizar.
	 * @param aName
	 *            es el nuevo nombre para el conjunto de prioridades.
	 * @param isDefaultPrioritySet
	 *            indica si este conjunto debe ser el conjunto por defecto.
	 * @throws PrioritySetNameNotUniqueException
	 *             esta excepción puede levantarse en caso de que el nuevo
	 *             nombre del conjunto de prioridades ya se esté utilizando.
	 */
	public void updatePrioritySet(PrioritySet aPrioritySet, String aName,
			boolean isDefaultPrioritySet)
			throws PrioritySetNameNotUniqueException {

		if (!aPrioritySet.getName().equals(aName)) {
			// se le está asignando un nuevo nombre. se debe verificar su
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
				// no debería llegarse aquí ya que siempre debe haber un
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
	 *             esta excepción puede levantarse en caso de que ya exista una
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
	 * Crea un nuevo ítem dentro del proyecto seleccionado.<br>
	 * Debido a que es un nuevo elemento, no es necesario controlar ningún
	 * aspecto.
	 * 
	 * @param anUser
	 *            es el usuario que ha creado este ítem.
	 * @param aProject
	 *            es el proyecto en el cual se debe agregar el nuevo ítem.
	 * @param aTitle
	 *            es el título del ítem.
	 * @param aDescription
	 *            es la descripción del ítem.
	 * @param aPriority
	 *            es la prioridad del nuevo ítem. Se debe aumentar la cantidad
	 *            de referencias a la prioridad de modo que no se la pueda
	 *            eliminar si algún ítem la referencia.
	 * @param aState
	 *            es el estado del ítem. Si el estado es CREADO entonces el nodo
	 *            del workflow que le corresponde es el nulo (NullObject); en
	 *            cambio si es ABIERTO le corresponde el nodo inicial del
	 *            workflow.
	 * @param aDate
	 *            es la fecha de creación del ítem.
	 * @param responsibleUser
	 *            es el usuario o grupo de usuarios al cual el creador está
	 *            solicitando la asignación del nuevo ítem.
	 * @param anItemType
	 *            es el tipo del nuevo ítem.
	 * @param somePropertyDescriptions
	 *            es un diccionario que contiene los valores ingresados para
	 *            cada una de las propiedades adicionales del nuevo ítem.
	 * 
	 * @return el ítem recientemente creado.
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
	 * @return una colección que contiene las descripciones de los workflows
	 *         disponibles.
	 */
	public Collection<WorkflowDescription> getWorkflowDescriptions() {
		return this.workflowDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colección que contiene las descripciones de los
	 *            workflows disponibles.
	 */
	public void setWorkflowDescriptions(
			Collection<WorkflowDescription> aCollection) {
		this.workflowDescriptions = aCollection;
	}

	/**
	 * Borra el proyecto recibido. Para la capa del modelo, borrar un objeto
	 * significa romper los vínculos que pueda tener este objeto con el resto de
	 * los objetos; por ejemplo para el caso de un proyecto, se deben desasociar
	 * los ítems de sus responsables, pero no se eliminan los items del proyecto
	 * ya que esto se hará como parte de las actividades del servicio y los
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
	 * @return una colección que contiene todos los tipos de ítems del sistema.
	 */
	public Collection<ItemType> getItemTypes() {
		return this.itemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param someItemTypes
	 *            es una colección que contiene todos los tipos de ítems del
	 *            sistema.
	 */
	public void setItemTypes(Collection<ItemType> someItemTypes) {
		this.itemTypes = someItemTypes;
	}

	/**
	 * Agrega un nuevo tipo de ítem al sistema.
	 * 
	 * @param aTitle
	 *            es el título del nuevo tipo de ítem.
	 * @return la nueva instancia de tipo de ítem.
	 * @throws ItemTypeTitleNotUniqueException
	 *             esta excepción puede levantarse en caso de que se esté
	 *             tratando de agregar un nuevo tipo de ítem con un título ya
	 *             existente.
	 */
	public ItemType addItemType(String aTitle)
			throws ItemTypeTitleNotUniqueException {

		if (!this.getItemsRepository().containsItemTypeWithTitle(this, aTitle)) {

			ItemType newItemType = new ItemType(aTitle);

			// agrega el nuevo tipo de ítem
			this.getItemTypes().add(newItemType);

			return newItemType;

		} else {

			throw new ItemTypeTitleNotUniqueException();
		}

	}

	/**
	 * Edita los datos de un tipo de ítem. No pueden haber dos tipos de ítems
	 * con el mismo título.
	 * 
	 * @param anItemType
	 *            es el tipo de ítem que se está editando.
	 * @param aTitle
	 *            es el título nuevo para el tipo de ítem.
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
	 * Borra el tipo de ítem recibido.<Br>
	 * Borrar un tipo de ítem implica eliminarlo del sistema así como actualizar
	 * todos los enlaces definidos en el workflow para que eliminen este tipo de
	 * ítem de su definición.<Br>
	 * En teoría no debería llegar a este método un tipo de ítem que aún tenga
	 * referencias (es decir ítems que lo apuntan).
	 * 
	 * @param anItemType
	 *            es el tipo de ítem que debe ser eliminado.
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
	 * En caso de que el usuario tenga ítems asignados o haya trabajado como
	 * responsable de éstos en algún momento, los ítems serán asignados al líder
	 * de cada proyecto.<BR>
	 * El usuario será removido de los grupos de usuarios y de todos los nodos
	 * de los workflows de los distintos proyectos. Al ejecutar esta operación
	 * no queda ningún rastro de la existencia del usuario.
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

		// no se notifica a las prioridades de este conjunto así se eliminan en
		// cascada.
		this.getPrioritySets().remove(aPrioritySet);
	}

}
