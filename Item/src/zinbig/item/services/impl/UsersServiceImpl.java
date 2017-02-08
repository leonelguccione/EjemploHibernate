/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import zinbig.item.model.Operation;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.ItemEditionException;
import zinbig.item.model.exceptions.PasswordMismatchException;
import zinbig.item.model.exceptions.UserGroupNotUniqueException;
import zinbig.item.model.exceptions.UserGroupUnknownException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.exceptions.UsernameNotUniqueException;
import zinbig.item.model.projects.Project;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.repositories.bi.OperationsRepositoryBI;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.FilterDTOComparator;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.ProjectDTOComparator;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.security.EncryptionStrategy;

/**
 * Las instancias de esta clase se utilizan para acceder a la l�gica de negocios relacionada con los usuarios
 * del sistema.<br>
 * La l�gica de negocios propiamente dicha no se encuentra en los servicios sino que est� definida en los
 * objetos de modelo. La �nica excepci�n a esta regla es la actualizaci�n de la informaci�n del usuario que se
 * realiza en este servicio.<br>
 * Al dar de alta un usuario, operaci�n que realiza el administrador del sistema, existe un tiempo previsto
 * para que el usuario ingrese al sistema y cambie su password. Si pasado este per�odo el usuario nuevo no ha
 * ingresado, entonces ser� eliminado del sistema. Los colaboradores de esta clase se inyectan mediante IoC.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class UsersServiceImpl extends BaseServiceImpl implements UsersServiceBI {

  /**
   * Es una implementaci�n de una estrategia de encriptaci�n para validar las claves de los usuarios.
   */
  protected EncryptionStrategy encryptionStrategy;

  /**
   * Constructor.
   */
  public UsersServiceImpl() {
    super();
  }

  /**
   * Getter.
   * 
   * @return la estrategia de encriptaci�n que se utiliza para validar las claves de los usuarios.
   */
  public EncryptionStrategy getEncryptionStrategy() {
    return this.encryptionStrategy;
  }

  /**
   * Setter.
   * 
   * @param anEncryptionStrategy
   *          es la estrategia de encriptaci�n que se debe utilizar para validar las claves de los usuarios.
   */
  public void setEncryptionStrategy(EncryptionStrategy anEncryptionStrategy) {
    this.encryptionStrategy = anEncryptionStrategy;
  }

  /**
   * Intenta ingresar al sistema con un nombre de usuario y clave.<br>
   * Este m�todo utiliza implementaciones de Repository para acceder eficientemente a los objetos del dominio.
   * 
   * @param username
   *          es el nombre de usuario a utilizar.
   * @param password
   *          es la clave del usuario que est� intentando ingresar.
   * @return un DTO que representa al usuario en caso de que la informaci�n provista sea correcta.
   * @throws UserUnknownException
   *           esta excepci�n puede ser lanzada en caso de que no exista el usuario.
   * @throws PasswordMismatchException
   *           esta excepci�n puede ser lanzada en caso de que la clave provista con coincida con la clave
   *           almacenada.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public UserDTO loginUser(String username, String password) throws Exception {
    long before = new Date().getTime();
    Tracker aTracker = this.getTrackerRepository().findTracker();
    
    User anUser = aTracker.loginUser(username, password, this.getEncryptionStrategy());

    boolean isAdmin = aTracker.getAdministrator().equals(anUser);

    // recupera todas las operaciones del usuario. esto lo hace a trav�s de
    // un servicio privado de modo de poder optimizar la performance.
    Collection<Operation> operations = this.getAllOperationsOfUser(anUser);

    UserDTO anUserDTO = this.getDtoFactory().createCompleteDTOForUser(anUser, isAdmin, operations, false);

    long after = new Date().getTime();
    System.out.println("tiempo del login:" + (after - before));
    return loadOperationsInformation(anUser, anUserDTO);

  }

  /**
   * Carga la informaci�n relacionada con las operaciones del usuario.
   * 
   * @param anUser
   *          es el usuario.
   * @param anUserDTO
   *          es el dto que representa al usuario.
   * @return el dto del usuario con la informaci�n actualizada de las operaciones.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public UserDTO loadOperationsInformation(User anUser, UserDTO anUserDTO) throws Exception {
    ProjectsServiceBI projectsService = ServiceLocator.getInstance().getProjectsService();

    if (anUser.getUserPreferences().containsKey("DEFAULT_PROJECT")) {
      String defaultProjectOID = anUser.getUserPreferences().get("DEFAULT_PROJECT");
      ProjectDTO aProjectDTO = projectsService.findProjectById(defaultProjectOID, false);
      anUserDTO.setDefaultProject(aProjectDTO);
    }

    Collection<OperationDTO> extraOperations = new ArrayList<OperationDTO>();

    if (anUserDTO.containsOperationWithName("VIEW_ALL_PROJECTS")) {
      TreeSet<ProjectDTO> projects = new TreeSet<ProjectDTO>(new ProjectDTOComparator());
      projects.addAll(projectsService.findFavoriteProjectsOfUser(anUserDTO));

      extraOperations.addAll(this.prepareOperationsDTOsForProjects(projects.iterator()));

    }

    if (anUserDTO.containsOperationWithName("CONFIGURE_ITEMS_FILTERS")) {

      TreeSet<FilterDTO> filters = new TreeSet<FilterDTO>(new FilterDTOComparator());
      filters.addAll(ServiceLocator.getInstance().getItemsService()
          .findFavoritiesItemsFiltersOfUser(anUserDTO));

      extraOperations.addAll(this.prepareOperationsDTOsForFilters(filters.iterator()));

    }
    anUserDTO.getOperations().addAll(extraOperations);

    return anUserDTO;
  }

  /**
   * Agrega un nuevo usuario al sistema.<br>
   * Este m�todo utiliza implementaciones de Repository para acceder eficientemente a los objetos del dominio.<br>
   * Por defecto el nuevo usuario es agregado con el status NC, que indica que no ha confirmado su ingreso. Si
   * al cabo de cierto per�odo de tiempo no lo confirma, ser� eliminado del sistema.
   * 
   * @param aName
   *          es el nombre del usuario.
   * @param username
   *          es el nombre de usuario.
   * @param password
   *          es la clase del nuevo usuario.
   * @param anEmail
   *          es el administratorEmail del usuario.
   * @param aDate
   *          es la fecha de creaci�n del usuario.
   * @param aLanguage
   *          es el nombre del idioma del usuario.
   * @param aSurname
   *          es el apellido de este usuario.
   * @param iterator
   *          es un iterador que permite recorrer la colecci�n de dtos que representan los grupos
   *          seleccionados para el nuevo usuario.
   * @return un DTO que representa al nuevo usuario creado.
   * @throws UsernameNotUniqueException
   *           esta excepci�n puede ser lanzada en caso de que ya exista un usuario con el nombre de usuario
   *           provisto.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public UserDTO addUser(String aName, String username, String password, String anEmail, Date aDate,
      String aLanguage, String aSurname, Iterator<UserGroupDTO> iterator) throws Exception {

    UserDTO result = null;

    Tracker aTracker = this.getTrackerRepository().findTracker();

    Collection<UserGroup> userGroups;
    // verifica que por lo menos exista un grupo de usuarios al cual asignar
    // a este usuario
    if (iterator.hasNext()) {
      userGroups = this.getUsersRepository().findUserGroups(iterator);
    } else {
      userGroups = new ArrayList<UserGroup>();
    }

    User newUser = aTracker.addUser(aName, username, password, this.getEncryptionStrategy(), "C", anEmail,
        aDate, aLanguage, aSurname, userGroups);

    result = this.getDtoFactory().createDTOForUser(newUser, false);

    return result;

  }

  /**
   * Busca un usuario con el nombre de usuario provisto.
   * 
   * @param anUsername
   *          es el nombre de usuario que se est� buscando.
   * @param status
   *          indica el status del usuario que se busca.
   * @return un DTO que representa al usuario hallado.
   * @throws UserUnknownException
   *           esta excepci�n puede ser levantada en caso de que no exista un usuario con el nombre de usuario
   *           dado.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public UserDTO findUserWithUsername(String anUsername, String status) throws Exception {

    UserDTO dto = null;
    Tracker aTracker = this.getTrackerRepository().findTracker();
    User anUser = aTracker.findUserWithUsername(anUsername, status);

    if (anUser != null) {

      dto = this.getDtoFactory().createDTOForUser(anUser, false);

    }
    return dto;

  }

  /**
   * Retorna verdadero si existe un usuario con el nombre de usuario dado.
   * 
   * @param anUsername
   *          es el nombre de usuario a buscar.
   * @return true en caso de que exista un usuario; false en caso contrario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public boolean existsUserWithUsername(String anUsername) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();
    boolean result = this.getUsersRepository().containsUserWithUsername(aTracker, anUsername);

    return result;
  }

  /**
   * Actualiza la informaci�n b�sica del usuario, es decir email, password, idioma preferido y algunos datos
   * b�sicos m�s.<br>
   * 
   * @param anUserDTO
   *          es el dto que mantiene la informaci�n actualizada que debe ser enviada a la capa del modelo.
   * @param userGroupDTOs
   *          es una colecci�n que contiene todos los dtos de los grupos de usuarios a los cuales se ha
   *          asignado al usuario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public void updateUserInformation(UserDTO anUserDTO, Collection<UserGroupDTO> userGroupDTOs)
      throws Exception {
    try {

      Tracker aTracker = this.getTrackerRepository().findTracker();
      User anUser = this.getUsersRepository().findUserWithUsername(aTracker, anUserDTO.getUsername(), "C");

      // controla que se est� editando la �ltima versi�n del objeto
      if (!this.verifyLatestVersion(anUser, anUserDTO)) {

        throw new ItemConcurrentModificationException();

      } else {

        Collection<UserGroup> userGroups = null;
        if (userGroupDTOs != null && !userGroupDTOs.isEmpty()) {
          userGroups = new ArrayList<UserGroup>();
          userGroups.addAll(this.getUsersRepository().findUserGroups(userGroupDTOs.iterator()));
        }

        // verifica si se cambi� la password.
        String aPassword = anUserDTO.getPassword().equals(anUser.getPassword()) ? anUser.getPassword() : this
            .getEncryptionStrategy().encrypt(anUserDTO.getPassword());

        aTracker.updateUser(anUser, anUserDTO.getName(), anUserDTO.getSurname(), aPassword,
            anUserDTO.getEmail(), anUserDTO.getLanguage(), userGroups);

      }

    } catch (UserUnknownException e) {
      e.printStackTrace();
      throw new ItemEditionException();

    }
  }

  /**
   * Recupera una colecci�n con todas las operaciones que se pueden ejecutar en forma an�nima.
   * 
   * @return una colecci�n sin orden predeterminado.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<OperationDTO> getListOfAnonymousOperations() throws Exception {

    OperationsRepositoryBI repository = this.getOperationsRepository();

    Collection<Operation> operations = repository.findAnonymousOperations();
    Collection<OperationDTO> result = this.getDtoFactory().createDTOForOperations(operations);

    TreeSet<ProjectDTO> projects = new TreeSet<ProjectDTO>(new ProjectDTOComparator());

    projects.addAll(ServiceLocator.getInstance().getProjectsService().findPublicProjects(3));

    result.addAll(this.prepareOperationsDTOsForProjects(projects.iterator()));

    return result;

  }

  /**
   * Recupera la lista de todas las operaciones que fueron asignadas a un usuario. <br>
   * Este m�todo no deber�a utilizarse desde la interfaz ya que recibe y devuelve objetos de modelo y no dtos.
   * Esta lista es el superconjunto de todas las operaciones.
   * 
   * @param anUser
   *          es el usuario para el cual se deben buscar las operaciones.
   * @return una lista que contiene todas las operaciones asignadas, tanto las de sistema como las asignadas a
   *         los diferentes grupos de proyectos de los cuales el usuario es miembro.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */

  private Collection<Operation> getAllOperationsOfUser(User anUser) throws Exception {

    OperationsRepositoryBI repository = this.getOperationsRepository();
    Collection<Operation> operations = repository.findAllOperationsOfUser(anUser.getOid());

    return operations;
  }

  /**
   * Retorna verdadero si existe un grupo de usuarios con el nombre dado. <br>
   * Este m�todo considera tanto los grupos de usuarios de sistema como los de proyectos.
   * 
   * @param aName
   *          es el nombre a buscar.
   * @return true en caso de que exista un grupo de usuarios; false en caso contrario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public boolean containsUserGroupWithName(String aName) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();
    boolean result = this.getUsersRepository().containsUserGroupWithName(aTracker, aName);

    return result;
  }

  /**
   * Agrega un nuevo grupo de usuarios al sistema.<br>
   * 
   * @param aName
   *          es el nombre del nuevo grupo de usuarios.
   * @param anEmail
   *          es el email del nuevo grupo.
   * @param aLanguage
   *          representa el lenguaje del nuevo grupo de usuarios.
   * @return un dto que representa al nuevo grupo de usuarios.
   * @throws UserGroupNotUniqueException
   *           esta excepci�n puede levantarse en caso de querer agregar un grupo de usuarios con un nombre
   *           que ya se est� utilizando.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public UserGroupDTO addUserGroup(String aName, String anEmail, String aLanguage) throws Exception {
    UserGroupDTO result = null;

    Tracker aTracker = this.getTrackerRepository().findTracker();

    UserGroup newUserGroup = aTracker.addUserGroup(aName, anEmail, aLanguage);
    result = this.getDtoFactory().createDTOForUserGroup(newUserGroup, false);

    return result;
  }

  /**
   * Getter.
   * 
   * @return la cantidad de grupos de usuarios de sistema que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public int getUserGroupsCount() throws Exception {
    Tracker aTracker = this.getTrackerRepository().findTracker();

    return aTracker.getUserGroupsCount();
  }

  /**
   * Getter.
   * 
   * @param index
   *          es el �ndice de inicio.
   * @param count
   *          es la cantidad de elementos a recuperar.
   * @param anOrdering
   *          es el orden que debe aplicarse al resultado.
   * @param aColumnName
   *          es el nombre de la propiedad por la que debe ordenarse el resultado.
   * @return la colecci�n de dtos de los grupos de usuarios del sistema empezando por el �ndice recibido y
   *         devolviendo �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<UserGroupDTO> getUserGroups(int index, int count, String aColumnName, String anOrdering)
      throws Exception {

    Collection<UserGroup> userGroups = this.getUsersRepository().findUserGroups(index, count, aColumnName,
        anOrdering);

    return this.getDtoFactory().createDTOForUserGroups(userGroups, false);
  }

  /**
   * Recupera todas las operaciones que se pueden asignar a los demas usuarios.
   * 
   * @return una colecci�n de dtos que representan las operaciones no administrativas.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<OperationDTO> getAllNonAdministrativeOperations() throws Exception {
    OperationsRepositoryBI repository = this.getOperationsRepository();

    Collection<Operation> operations = repository.findAllOperations(false);
    Collection<OperationDTO> result = this.getDtoFactory().createDTOForOperations(operations);

    return result;
  }

  /**
   * Edita la informaci�n almacenada del grupo de usuarios recibido.
   * 
   * @param aName
   *          es el nombre actual del grupo de usuarios. Es necesario ya que en la edici�n de un grupo se pudo
   *          haber cambiado incluso el nombre.
   * @param aDto
   *          es el dto que contiene la informaci�n del grupo de usuarios que se debe actualizar.
   * @throws ItemConcurrentModificationException
   *           esta excepci�n puede darse en caso de que dos usuarios traten de modificar el mismo grupo de
   *           usuarios al mismo tiempo.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public void editUserGroup(String aName, UserGroupDTO aDto) throws Exception {

    try {
      Tracker aTracker = this.getTrackerRepository().findTracker();
      UserGroup anUserGroup = this.getUsersRepository().findUserGroupByName(aTracker, aName);

      // controla que se est� editando la �ltima versi�n del objeto
      if (!this.verifyLatestVersion(anUserGroup, aDto)) {

        throw new ItemConcurrentModificationException();

      } else {

        Collection<Operation> operations;
        Collection<Project> projects;
        if (aDto.getOperationDTOs().isEmpty()) {
          operations = new ArrayList<Operation>();
        } else {
          operations = this.getOperationsRepository().findOperations(aDto.getOperationDTOs());
        }

        if (aDto.getProjectDTOs().isEmpty()) {
          projects = new ArrayList<Project>();
        } else {
          projects = this.getProjectsRepository().findProjects(aDto.getProjectDTOs());

        }

        aTracker.updateUserGroup(anUserGroup, aDto.getName(), aDto.getEmail(), operations, projects);

      }
    } catch (UserGroupUnknownException e) {

      e.printStackTrace();
      throw new ItemEditionException();

    }

  }

  /**
   * Recupera un grupo de usuarios por el nombre dado.
   * 
   * @param aName
   *          es el nombre del grupo de usuarios.
   * @return el grupo de usuarios con el nombre dado.
   * @throws UserGroupUnknownException
   *           esta excepci�n puede ser lanzada en caso de que el grupo de usuarios no exista.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public UserGroupDTO findUserGroup(String aName) throws Exception {
    Tracker aTracker = this.getTrackerRepository().findTracker();
    UserGroup anUserGroup = this.getUsersRepository().findUserGroupByName(aTracker, aName);
    UserGroupDTO result = null;

    if (anUserGroup == null) {
      throw new UserGroupUnknownException();
    } else {
      result = this.getDtoFactory().createDTOForUserGroup(anUserGroup, true);
    }
    return result;
  }

  /**
   * Getter.
   * 
   * @return la cantidad de usuarios de sistema que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public int getUsersCount() throws Exception {
    Tracker aTracker = this.getTrackerRepository().findTracker();

    return aTracker.getUsersCount();
  }

  /**
   * Getter.
   * 
   * @param index
   *          es el �ndice de inicio.
   * @param count
   *          es la cantidad de elementos a recuperar.
   * @param anOrdering
   *          es el orden que se debe aplicar a los resultados.
   * @param aPropertyName
   *          es el nombre de la propiedad que se debe utilizar para ordenar los resultados.
   * @return la colecci�n de dtos de los usuarios del sistema empezando por el �ndice recibido y devolviendo
   *         �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<UserDTO> getUsers(int index, int count, String aPropertyName, String anOrdering)
      throws Exception {

    Collection<User> users = this.getUsersRepository().findUsers(index, count, aPropertyName, anOrdering);

    Collection<UserDTO> dtos = this.getDtoFactory().createDTOForUsers(users);

    return dtos;
  }

  /**
   * Recupera todos los grupos de usuarios.
   * 
   * @return una colecci�n de dtos que representan a todos los grupos de usuarios.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<UserGroupDTO> getAllUsergroups() throws Exception {

    Collection<UserGroup> groups = this.getUsersRepository().findAllUserGroups();

    return this.getDtoFactory().createDTOForUserGroups(groups, false);
  }

  /**
   * Actualiza una preferencia del usuario.
   * 
   * @param aDto
   *          es el dto que representa la usuario en la sesi�n.
   * @param aKey
   *          es la clave bajo la cual se debe actualizar la preferencia.
   * @param aValue
   *          es el valor de la preferencia.
   * @throws Exception
   *           esta excepci�n se puede levantar por alg�n error al ejecutar este servicio.
   */
  @Override
  public void updateUserPreference(UserDTO aDTO, String aKey, String aValue) throws Exception {
    try {
      Tracker aTracker = this.getTrackerRepository().findTracker();
      User anUser = this.getUsersRepository().findUserWithUsername(aTracker, aDTO.getUsername(), "C");

      anUser.updatePreference(aKey, aValue);
    } catch (UserUnknownException uue) {
      throw new ItemEditionException();
    }
  }

  /**
   * Recupera todos los grupos de usuarios asignados al usuario recibido.
   * 
   * @param anUserDTO
   *          es el dto que representa al usuario para el cual se deben recuperar todos los grupos de usuarios
   *          asignados.
   * @return una colecci�n de dtos de grupos de usuarios.
   * @throws Exception
   *           esta excepci�n se puede levantar por alg�n error al ejecutar este servicio.
   */
  @Override
  public Collection<UserGroupDTO> getUserGroupsOfUser(UserDTO anUserDTO) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();
    User anUser = this.getUsersRepository().findUserWithUsername(aTracker, anUserDTO.getUsername(), "C");

    Collection<UserGroup> userGroups = this.getUsersRepository().findUserGroupsOfUser(anUser);
    return this.getDtoFactory().createDTOForUserGroups(userGroups, false);
  }

  /**
   * Recupera todos los usuarios del sistema, excepto el administrador.
   * 
   * @return una colecci�n que contiene todos los usuarios del sistema.
   * @throws Exception
   *           esta excepci�n se puede levantar por alg�n error al ejecutar este servicio.
   */
  @Override
  public Collection<UserDTO> getAllUsers() throws Exception {

    Collection<UserDTO> result = this.getDtoFactory().createDTOForUsers(
        this.getUsersRepository().findAllUsers());

    return result;
  }

  /**
   * Obtiene la lista de proyectos y para cada uno de ellos arma un dto de operaci�n que permite acceder al
   * dashboard del proyecto en concreto. <br>
   * Esta lista de proyectos contiene tanto los proyectos p�blicos como los que tienen al usuario como
   * miembro.
   * 
   * @param anUserDTO
   *          es el dto que representa al usuario en la sesi�n. Puede ser nulo en caso de que no exista un
   *          usuario en la sesi�n.
   * @return una colecci�n de dtos de operaciones para cada una de los proyectos p�blicos.
   */
  private Collection<OperationDTO> prepareOperationsDTOsForProjects(Iterator<ProjectDTO> projectsIterator) {

    ProjectDTO aProjectDTO = null;
    Collection<OperationDTO> result = new ArrayList<OperationDTO>();
    OperationDTO dto = null;
    while (projectsIterator.hasNext()) {
      aProjectDTO = projectsIterator.next();
      dto = new OperationDTO("", "VIEW_PROJECT_DASHBOARD", "PROJECTS_CATEGORY",
          "zinbig.item.application.pages.ProjectDashboardPage", new Integer(3), true);
      dto.getParameters().put("PROJECT_MENU_TITLE",
          "[" + aProjectDTO.getShortName() + "] " + aProjectDTO.getName());
      try {
        dto.getParameters().put("PROJECT_OID", URLEncoder.encode(aProjectDTO.getOid().toString(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {

        e.printStackTrace();
      }
      result.add(dto);
    }

    return result;
  }

  /**
   * Recorre la lista de filtros del usuario y para cada uno de ellos arma un dto de operaci�n que permite
   * ejecutar dicho filtro. <br>
   * 
   * @param filtersIterator
   *          es un iterador que contiene todos los filtros del usuario.
   * @return una colecci�n de dtos de operaciones para cada una de los filtros.
   */
  private Collection<OperationDTO> prepareOperationsDTOsForFilters(Iterator<FilterDTO> filtersIterator) {

    FilterDTO aFilterDTO = null;
    Collection<OperationDTO> result = new ArrayList<OperationDTO>();
    OperationDTO dto = null;
    while (filtersIterator.hasNext()) {
      aFilterDTO = filtersIterator.next();
      dto = new OperationDTO("", "FILTER_ITEMS", "ITEMS_CATEGORY",
          "zinbig.item.application.pages.ViewItemsPage", new Integer(2), true);
      dto.getParameters().put("FILTER_MENU_TITLE", aFilterDTO.getName());
      try {
        dto.getParameters().put("FILTER_OID", URLEncoder.encode(aFilterDTO.getOid().toString(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {

        e.printStackTrace();
      }
      result.add(dto);
    }

    return result;
  }

  /**
   * Elimina del sistema un grupo de usuarios seleccionados.
   * 
   * @param someUserGroups
   *          es una colecci�n que contiene los oids de los grupos de usuarios que hay que eliminar.
   * @throws Exception
   *           es cualquier excepci�n que puede levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public void deleteUserGroups(Collection<String> someUserGroups) throws Exception {

    UsersRepositoryBI repository = this.getUsersRepository();

    // recupera todos los grupos de usuarios a partir de sus
    // correspondientes ids.
    Collection<UserGroup> userGroups = repository.findUserGroupsById(someUserGroups);
    Tracker aTracker = this.getTrackerRepository().findTracker();

    UserGroup anUserGroup = null;
    for (Iterator<UserGroup> i = userGroups.iterator(); i.hasNext();) {
      anUserGroup = i.next();

      aTracker.deleteUserGroup(anUserGroup);
      repository.delete(anUserGroup);

    }

  }

  /**
   * Elimina del sistema una colecci�n de usuarios.
   * 
   * @param selectedUsers
   *          es la colecci�n que contiene los identificadores de los usuarios seleccionados para ser
   *          eliminados.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public void deleteUsers(Collection<String> selectedUsers) throws Exception {
    UsersRepositoryBI repository = this.getUsersRepository();

    // recupera todos los usuarios a partir de sus
    // correspondientes ids.
    Collection<User> users = repository.findUsersById(selectedUsers);
    Tracker aTracker = this.getTrackerRepository().findTracker();

    User anUser = null;
    for (Iterator<User> i = users.iterator(); i.hasNext();) {
      anUser = i.next();

      aTracker.deleteUser(anUser);
      repository.delete(anUser);

    }
  }

  /**
   * Obtiene una lista de DTOs que representan a los usuarios cuyos nombre de usuario comienzan con el string
   * recibido.
   * 
   * @param aString
   *          es el string que se debe utilizar para la b�squeda.
   * @param aLimit
   *          es el l�mite de elementos a devolver.
   * @return una colecci�n con los DTOs de los usuarios.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public Collection<UserDTO> findUsersWithUsernameLike(String aString, int aLimit) throws Exception {
    Tracker aTracker = this.getTrackerRepository().findTracker();
    Collection<User> users = this.getUsersRepository().findUsersWithUsernameLike(aTracker, aString, aLimit);

    return this.getDtoFactory().createDTOForUsers(users);
  }

}
