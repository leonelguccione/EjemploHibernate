/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

import zinbig.item.model.Item;
import zinbig.item.model.ItemType;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.AdditionalPropertyNameNotUniqueException;
import zinbig.item.model.exceptions.InvalidDynamicQueryException;
import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.ItemEditionException;
import zinbig.item.model.exceptions.ProjectNameNotUniqueException;
import zinbig.item.model.exceptions.ProjectUnknownException;
import zinbig.item.model.exceptions.TrackerUnknownException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.projects.PrioritySet;
import zinbig.item.model.projects.Project;
import zinbig.item.model.properties.PropertyDescription;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.model.workflow.WorkflowNodeDescription;
import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.repositories.bi.WorkflowsRepositoryBI;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.dto.DTOFactory;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.ProjectDTOComparator;
import zinbig.item.util.dto.PropertyDescriptionDTO;
import zinbig.item.util.dto.PublicProjectDTOComparator;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para acceder a la l�gica de negocios relacionada con los proyectos
 * del sistema.<br>
 * La l�gica de negocios propiamente dicha no se encuentra en los servicios sino que est� definida en los
 * objetos de modelo.<br>
 * Los colaboradores de esta clase se inyectan mediante IoC.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class ProjectsServiceImpl extends BaseServiceImpl implements ProjectsServiceBI {

  /**
   * Es un diccionario que contiene informaci�n para poder crear las propiedades adicionales de los proyectos.
   */
  protected Map<String, String> additionalPropertyTypes;

  /**
   * Constructor.
   */
  public ProjectsServiceImpl() {
    super();
    this.setAdditionalPropertyTypes(new HashMap<String, String>());
  }

  /**
   * Getter.
   * 
   * @return un diccionario que contiene los nombres de las clases correspondientes a los diferentes tipos de
   *         propiedades adicionales.
   */
  public Map<String, String> getAdditionalPropertyTypes() {
    return this.additionalPropertyTypes;
  }

  /**
   * Setter.
   * 
   * @param aMap
   *          es un diccionario que contiene los nombres de las clases correspondientes a los diferentes tipos
   *          de propiedades adicionales.
   */
  public void setAdditionalPropertyTypes(Map<String, String> aMap) {
    this.additionalPropertyTypes = aMap;
  }

  /**
   * Recupera los proyectos p�blicos.
   * 
   * @param projectsCount
   *          indica la cantidad de proyectos p�blicos que se deben recuperar como m�ximo.
   * @return una colecci�n que contiene todos los proyectos p�blicos, hasta la cantidad m�xima solicitada.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<ProjectDTO> findPublicProjects(int projectsCount) throws Exception {

    Collection<ProjectDTO> result = new TreeSet<ProjectDTO>(new PublicProjectDTOComparator());

    Iterator<Project> iterator = this.getProjectsRepository().findPublicProjects(projectsCount).iterator();
    DTOFactory aFactory = this.getDtoFactory();
    Project aProject = null;

    while (iterator.hasNext()) {

      aProject = iterator.next();
      result.add(aFactory.createCompleteDTOForProject(aProject, false, false, false));
    }

    return result;
  }

  /**
   * Recupera todos los proyectos p�blicos.
   * 
   * @return una colecci�n que contiene todos los proyectos p�blicos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<ProjectDTO> findAllPublicProjects() throws Exception {
    Collection<Project> projects = this.getProjectsRepository().findAllPublicProjects();

    return this.getDtoFactory().createDTOForProjects(projects, true);
  }

  /**
   * Recupera una colecci�n que contiene todos los proyectos en los que un usuario participa y los proyectos
   * p�blicos.
   * 
   * @param anUserDTO
   *          es el usuario para el cual hay que buscar sus proyectos.
   * @return una colecci�n (sin repetidos) que contiene los proyectos en los que participa un usuario. La
   *         colecci�n est� ordenada por nombre del proyecto.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findProjectsOfUser(UserDTO anUserDTO) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();

    Collection<ProjectDTO> result = new TreeSet<ProjectDTO>(new ProjectDTOComparator());
    try {
      User anUser = aTracker.findUserWithUsername(anUserDTO.getUsername(), "C");
      Collection<Project> projects = this.getProjectsRepository().findAllProjectsOfUser(anUser);

      projects.addAll(this.getProjectsRepository().findAllPublicProjects());

      Project aProject = null;
      Iterator<Project> iterator = projects.iterator();
      DTOFactory aFactory = this.getDtoFactory();
      while (iterator.hasNext()) {
        aProject = iterator.next();
        result.add(aFactory.createCompleteDTOForProject(aProject, false, false, false));
      }
    } catch (UserUnknownException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Recupera una colecci�n que contiene todos los proyectos en los que un usuario participa.
   * 
   * @param anUserDTO
   *          es el usuario para el cual hay que buscar sus proyectos.
   * @return una colecci�n (sin repetidos) que contiene los proyectos en los que participa un usuario. La
   *         colecci�n est� ordenada por nombre del proyecto.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findPrivateProjectsOfUser(UserDTO anUserDTO) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();

    Collection<ProjectDTO> result = new TreeSet<ProjectDTO>(new ProjectDTOComparator());
    try {
      User anUser = aTracker.findUserWithUsername(anUserDTO.getUsername(), "C");
      Collection<Project> projects = this.getProjectsRepository().findAllProjectsOfUser(anUser);

      Project aProject = null;
      Iterator<Project> iterator = projects.iterator();
      DTOFactory aFactory = this.getDtoFactory();
      while (iterator.hasNext()) {
        aProject = iterator.next();
        result.add(aFactory.createDTOForProject(aProject));
      }
    } catch (UserUnknownException e) {

      e.printStackTrace();
    }

    return result;
  }

  /**
   * Recupera todos los proyectos.
   * 
   * @return una colecci�n que contiene todos los proyectos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findAllProjects() throws Exception {
    Collection<Project> projects = this.getProjectsRepository().findAllProjects();
    Collection<ProjectDTO> result = new TreeSet<ProjectDTO>(new ProjectDTOComparator());

    Project aProject = null;
    Iterator<Project> iterator = projects.iterator();
    DTOFactory aFactory = this.getDtoFactory();
    while (iterator.hasNext()) {
      aProject = iterator.next();
      result.add(aFactory.createDTOForProject(aProject));
    }

    return result;

  }

  /**
   * Getter.
   * 
   * @return la cantidad de proyectos que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public int getProjectsCount() throws Exception {
    Tracker aTracker = this.getTrackerRepository().findTracker();

    return aTracker.getProjectsCount();

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
   * @return la colecci�n de dtos de los proyectos del sistema empezando por el �ndice recibido y devolviendo
   *         �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<ProjectDTO> getProjects(int index, int count, String aPropertyName, String anOrdering)
      throws Exception {

    Collection<Project> projects = this.getProjectsRepository().findProjects(index, count, aPropertyName,
        anOrdering);

    return this.getDtoFactory().createDTOForProjects(projects, true);
  }

  /**
   * Agrega un nuevo proyecto al sistema.
   * 
   * @param aName
   *          es el nombre del proyecto.
   * @param aShortName
   *          es el nombre corto del proyecto.
   * @param aLink
   *          es el link relacionado con este proyecto.
   * @param isPublicProject
   *          indica si el proyecto es p� blico o no.
   * @param aPrioritySetDTO
   *          es el dto que representa al conjunto de prioridades seleccionado para el nuevo proyecto.
   * @param aClassName
   *          es el nombre de la clase de la estrategia de asignaci�n de responsables de los �tems.
   * @param aProjectLeaderUsername
   *          es el nombre de usuario del l�der seleccionado.
   * @param itemTypesDTOsIterator
   *          es un iterador de una colecci�n que contiene los tipos de �tems seleccionados para el nuevo
   *          proyecto.
   * @param aPathForAttachedFiles
   *          es un directorio a partir del cual se debe crear el directorio para los archivos adjuntos.
   * @throws ProjectNameNotUniqueException
   *           esta excepci�n se levanta en caso de tratar de dar de alta un proyecto con un nombre que ya
   *           existe.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public ProjectDTO addProject(String aName, String aShortName, String aLink, boolean isPublicProject,
      PrioritySetDTO aPrioritySetDTO, String aClassName, String aProjectLeaderUsername,
      Iterator<ItemTypeDTO> itemTypesDTOsIterator, String aPathForAttachedFiles) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();

    User anUser = this.getUsersRepository().findUserWithUsername(aTracker, aProjectLeaderUsername, "C");

    PrioritySet aPrioritySet = this.getPrioritiesRepository().findById(aPrioritySetDTO.getOid());

    WorkflowDescription aWorkflowDescription = aTracker.getWorkflowDescriptions().iterator().next();

    Collection<ItemType> itemTypes = new ArrayList<ItemType>();
    Iterator<ItemType> iterator = this.getItemsRepository().findItemTypeByIds(itemTypesDTOsIterator)
        .iterator();
    while (iterator.hasNext()) {
      itemTypes.add((ItemType) iterator.next().clone());
    }

    Project aProject = aTracker.addProject(aName, aShortName, aLink, isPublicProject,
        (PrioritySet) aPrioritySet.clone(), (WorkflowDescription) aWorkflowDescription.clone(), aClassName,
        anUser, itemTypes);

    // crea el directorio para guardar los archivos adjuntos de este nuevo
    // proyecto.
    (new File(aPathForAttachedFiles)).mkdirs();

    return this.getDtoFactory().createDTOForProject(aProject);
  }

  /**
   * Getter.
   * 
   * @param aName
   *          es el nombre del proyecto que se est� buscando.
   * @return true en caso de que exista un proyecto con el nombre dado; false en caso contrario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public boolean existsProjectWithName(String aName) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();
    boolean result = this.getProjectsRepository().containsProjectWithName(aTracker, aName);

    return result;

  }

  /**
   * Recupera un proyecto a partir de su oid.
   * 
   * @param anOid
   *          es el oid del proyecto que se debe recuperar.
   * @param mustLoadCompleteProject
   *          establece si se debe cargar todo el proyecto completo.
   * @return el proyecto identificado por el oid recibido.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public ProjectDTO findProjectById(String anOid, boolean mustLoadCompleteProject) throws Exception {
    Project aProject = this.getProjectsRepository().findById(anOid);
    ProjectDTO result = null;
    if (mustLoadCompleteProject) {
      result = this.getDtoFactory().createDTOForProject(aProject);
    } else {
      result = this.getDtoFactory().createCompleteDTOForProject(aProject, false, false, false);
    }
    return result;
  }

  /**
   * Edita la informaci�n almacenada de un proyecto.
   * 
   * @param projectDTO
   *          es el dto que contiene la informaci�n que se debe editar de un proyecto. El oid es el �nico
   *          campo que no se puede editar, y por lo tanto se utiliza para encontrar al proyecto que debe ser
   *          editado.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public void editProject(ProjectDTO projectDTO) throws Exception {

    try {

      Project aProject = this.getProjectsRepository().findById(projectDTO.getOid());

      // controla que se est� editando la �ltima versi�n del objeto
      if (!this.verifyLatestVersion(aProject, projectDTO)) {

        throw new ItemConcurrentModificationException();

      } else {
        Tracker aTracker = this.getTrackerRepository().findTracker();
        // recupera el nuevo l�der de proyecto.
        User anUser = this.getUsersRepository().findUserWithUsername(aTracker,
            projectDTO.getProjectLeaderDTO().getUsername(), "C");

        // actualiza la informaci�n del proyecto.
        aTracker.updateProject(aProject, projectDTO.getName(), projectDTO.getShortName(),
            projectDTO.getLink(), projectDTO.isPublicProject(), anUser,
            projectDTO.getItemAssignmentStrategy());
      }

    } catch (ProjectUnknownException pue) {
      pue.printStackTrace();
      throw new ItemEditionException();

    }
  }

  /**
   * Getter.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se debe listar todos sus miembros.
   * @return una colecci�n que contiene todos los usuarios del proyecto.
   * @throws Exception
   *           exta excepci�n puede levantarse en caso de que ocurra un error en la ejecuci�n del servicio.
   */
  public Collection<UserDTO> getUsersOfProject(ProjectDTO aProjectDTO) throws Exception {

    Collection<User> users = this.getProjectsRepository().getUsersOfProject(aProjectDTO.getOid().toString());

    return this.getDtoFactory().createDTOForUsers(users);
  }

  /**
   * Getter.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se debe listar todos sus grupos de usuarios.
   * @return una colecci�n que contiene todos los grupos de usuarios del proyecto.
   * @throws Exception
   *           exta excepci�n puede levantarse en caso de que ocurra un error en la ejecuci�n del servicio.
   */
  public Collection<UserGroupDTO> getUserGroupsOfProject(ProjectDTO aProjectDTO) {

    Collection<UserGroup> userGroups = this.getProjectsRepository().getUserGroupsOfProject(
        aProjectDTO.getOid().toString());

    return this.getDtoFactory().createDTOForUserGroups(userGroups, false);
  }

  /**
   * Borra los proyectos representados por los DTOs recibidos.
   * 
   * @param selectedProjectsOids
   *          es una colecci�n que contiene los oids de los proyectos que deben ser eliminados.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la eliminaci�n de los proyectos.
   */
  @Override
  public void deleteProjects(Collection<String> selectedProjectsOids) throws Exception {

    ProjectsRepositoryBI repository = this.getProjectsRepository();

    // recupera todos los proyectos a partir de sus correspondientes DTOs.
    Collection<Project> projects = repository.findProjectsByIds(selectedProjectsOids);
    Tracker aTracker = this.getTrackerRepository().findTracker();

    Project aProject = null;
    for (Iterator<Project> i = projects.iterator(); i.hasNext();) {
      aProject = i.next();

      aTracker.deleteProject(aProject);
      repository.delete(aProject);

    }

  }

  /**
   * Recupera la colecci�n de proyectos que el usuario representado por el dto recibido ha seleccionado como
   * favoritos.
   * 
   * @param anUserDTO
   *          es el dto del usuario.
   * @return una colecci�n de dtos que representan a los proyectos favoritos del usuario.
   */
  public Collection<ProjectDTO> findFavoriteProjectsOfUser(UserDTO anUserDTO) {

    Collection<ProjectDTO> result = new TreeSet<ProjectDTO>(new ProjectDTOComparator());
    try {
      Tracker aTracker = this.getTrackerRepository().findTracker();
      User anUser = aTracker.findUserWithUsername(anUserDTO.getUsername(), "C");
      Collection<Project> projects = this.getProjectsRepository().findFavoriteProjectsOfUser(anUser);

      Project aProject = null;
      Iterator<Project> iterator = projects.iterator();
      DTOFactory aFactory = this.getDtoFactory();
      while (iterator.hasNext()) {
        aProject = iterator.next();
        result.add(aFactory.createDTOForProject(aProject));
      }
    } catch (UserUnknownException e) {

      e.printStackTrace();
    } catch (TrackerUnknownException te) {
      te.printStackTrace();
    }

    return result;
  }

  /**
   * Marca a un proyecto determinado como favorito del usuario recibido.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que debe ser marcado como favorito.
   * @param anUserDTO
   *          es le dto que reprsenta al usuario que marc� al proyecto como favorito.
   * @return un dto que representa al usuario actualizado.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la eliminaci�n de los proyectos.
   */
  public UserDTO addFavoriteProjectToUser(ProjectDTO aProjectDTO, UserDTO anUserDTO) throws Exception {

    Tracker aTracker = this.getTrackerRepository().findTracker();
    User anUser = aTracker.findUserWithUsername(anUserDTO.getUsername(), "C");
    Project aProject = this.getProjectsRepository().findById(aProjectDTO.getOid());
    anUser.addFavoriteProject(aProject);

    anUserDTO.addFavoriteProject(aProjectDTO);
    anUserDTO.setUserPreferences(anUser.getUserPreferences());

    return anUserDTO;

  }

  /**
   * Getter.
   * 
   * @param anUserDTO
   *          es el dto que representa al usuario.
   * @return la cantidad de proyectos que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public int getProjectsCount(UserDTO anUserDTO) throws Exception {
    int result = 0;
    if (anUserDTO == null) {
      result = this.getProjectsRepository().findAllPublicProjects().size();
    } else {
      Tracker aTracker = this.getTrackerRepository().findTracker();
      User anUser = this.getUsersRepository().findUserWithUsername(aTracker, anUserDTO.getUsername(), "C");
      Collection<Project> projects = new HashSet<Project>();
      projects.addAll(this.getProjectsRepository().findAllProjectsOfUser(anUser));
      projects.addAll(this.getProjectsRepository().findAllPublicProjects());
      result = projects.size();
    }
    return result;
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
   * @param anUserDTO
   *          es el dto que representa al usuario.
   * @return la colecci�n de dtos de los proyectos del sistema empezando por el �ndice recibido y devolviendo
   *         �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  @Override
  public Collection<ProjectDTO> getProjects(int index, int count, String aPropertyName, String anOrdering,
      UserDTO anUserDTO) throws Exception {

    Collection<Project> projects = this.getProjectsRepository().findProjects(index, count, aPropertyName,
        anOrdering, anUserDTO);

    return this.getDtoFactory().createDTOForProjects(projects, true);
  }

  /**
   * Agrega como favoritos a los proyectos cuyos identificadores se han recibido.
   * 
   * @param selectedProjects
   *          es una colecci�n que contiene los identificadores.
   * @param anUserDTO
   *          es el usuario para el cual hay que agregar los proyectos favoritos.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la eliminaci�n de los proyectos.
   */
  public Collection<ProjectDTO> addFavoriteProjectsToUser(Collection<String> selectedProjects,
      UserDTO anUserDTO) throws Exception {
    Iterator<String> iterator = selectedProjects.iterator();
    ProjectDTO projectDTO = null;
    Collection<ProjectDTO> result = new ArrayList<ProjectDTO>();
    while (iterator.hasNext()) {
      projectDTO = this.findProjectById(iterator.next(), false);
      this.addFavoriteProjectToUser(projectDTO, anUserDTO);
      result.add(projectDTO);
    }

    return result;
  }

  /**
   * Quita la marca a un proyecto determinado como favorito del usuario recibido.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que debe ser desmarcado como favorito.
   * @param userDTO
   *          es le dto que reprsenta al usuario que desmarc� al proyecto como favorito.
   * @return un dto que representa al dto del usuario actualizado.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public UserDTO removeFavoriteProjectFromUser(ProjectDTO aProjectDTO, UserDTO anUserDTO) throws Exception {
    Tracker aTracker = this.getTrackerRepository().findTracker();
    User anUser = aTracker.findUserWithUsername(anUserDTO.getUsername(), "C");
    Project aProject = this.getProjectsRepository().findById(aProjectDTO.getOid());
    anUser.removeFavoriteProject(aProject);

    anUserDTO.removeFavoriteProject(aProjectDTO);
    anUserDTO.setUserPreferences(anUser.getUserPreferences());

    return anUserDTO;
  }

  /**
   * Recupera todas las definiciones de propiedades adicionales del proyecto.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se deben recuperar las definiciones de propiedades adicionales.
   * @return una colecci�n que contiene los DTOs de las propiedades adicionales que se definieron para el
   *         proyecto.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public Collection<PropertyDescriptionDTO> getPropertyDescriptionsOfProject(ProjectDTO aProjectDTO)
      throws Exception {

    ProjectsRepositoryBI repository = this.getProjectsRepository();

    Collection<PropertyDescriptionDTO> result = new ArrayList<PropertyDescriptionDTO>();
    result.addAll(this.getDtoFactory().createDTOsForPropertyDescriptions(
        repository.findPropertyDescriptionsOfProject(aProjectDTO.getOid()), this));

    return result;
  }

  /**
   * Obtiene la lista de valores asociada a una descripci�n de propiedad adicional din�mica.
   * 
   * @param aQuery
   *          es la consulta que se debe ejecutar.
   * @return una lista de string que representa los valores posibles para la propiedad adicional.
   */
  public Collection<String> getValuesForDynamicProperty(String aQuery) {
    Collection<String> result = new ArrayList<String>();
    ProjectsRepositoryBI repository = this.getProjectsRepository();
    result.addAll(repository.getValuesForDynamicProperty(aQuery));

    return result;
  }

  /**
   * Obtiene la cantidad de propiedades adicionales que tiene el proyecto representado por el DTO.
   * 
   * @param aProjectDTO
   *          es el DTO que representa al proyecto que se debe consultar.
   * @return la cantidad de propiedades adicionales; 0 en caso que de que no existan.
   */
  public int getAdditionalPropertiesCountOfProject(ProjectDTO aProjectDTO) {
    ProjectsRepositoryBI repository = this.getProjectsRepository();

    return repository.getAdditionalPropertiesCountOfProject(aProjectDTO.getOid());
  }

  /**
   * Obtiene una colecci�n de propiedades adicionales del proyecto recibido.
   * 
   * @param projectDTO
   *          es el DTO que representa al proyecto.
   * @param aPropertyName
   *          es el nombre de la propiedad por la que se debe ordenar el resultado.
   * @param isAscending
   *          establece si el orden es ascendente o no.
   * @return una colecci�n de propiedades adicionales.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<PropertyDescriptionDTO> getPropertyDescriptionsOfProject(ProjectDTO projectDTO,
      String aPropertyName, boolean isAscending) throws Exception {

    Collection<PropertyDescription> someProperties = this.getProjectsRepository()
        .findPropertyDescriptionsOfProject(projectDTO.getOid(), aPropertyName, isAscending);

    Collection<PropertyDescriptionDTO> dtos = this.getDtoFactory().createDTOsForPropertyDescriptions(
        someProperties, this);

    return dtos;
  }

  /**
   * Elimina de un proyecto las propiedades adicionales seleccionadas.
   * 
   * @param projectDTO
   *          es el DTO que representa al proyecto en el cual se deben eliminar las propiedades.
   * @param selectedProperties
   *          es una colecci�n que contiene los identificadores de las propiedades adicionales que se deben
   *          eliminar.
   * @throws Exception
   *           esta excepci�n se puede levantar a ra�z de la ejecuci�n de este servicio.
   */
  public void deletePropertyDescriptionsOfProject(ProjectDTO projectDTO, Collection<String> selectedProperties)
      throws Exception {
    ProjectsRepositoryBI repository = this.getProjectsRepository();

    Project aProject = repository.findById(projectDTO.getOid());

    // recupera todas las propiedades y las borra del proyecto.
    Collection<PropertyDescription> properties = repository
        .findPropertyDescriptionsOfProject(selectedProperties);

    PropertyDescription aProperty = null;
    for (Iterator<PropertyDescription> i = properties.iterator(); i.hasNext();) {
      aProperty = i.next();

      aProject.deleteItemProperty(aProperty);
      repository.delete(aProperty);

    }
  }

  /**
   * Agrega una nueva descripci�n de propiedad al proyecto.
   * 
   * @param aProjectDTO
   *          es el DTO que representa al proyecto que se est� editando.
   * @param aName
   *          es el nombre de la nueva propiedad.
   * @param isRequired
   *          establece si la nueva propiedad es requerida o no.
   * @param aType
   *          define el tipo de la propiedad adicional.
   * @param aValue
   *          es un valor adicional requerido por algunos tipos de propiedades adicionales.
   * @throws Exception
   *           esta excepci�n se puede levantar a ra�z de la ejecuci�n de este servicio.
   */
  public void addAdditionalProperty(ProjectDTO aProjectDTO, String aName, boolean isRequired, String aType,
      String aValue) throws Exception {

    ProjectsRepositoryBI repository = this.getProjectsRepository();

    Project aProject = repository.findById(aProjectDTO.getOid());
    if (repository.containsPropertyDescriptionWithName(aProject, aName)) {
      throw new AdditionalPropertyNameNotUniqueException();
    } else {

      if (aType.equals("DYNAMIC")) {

        try {
          this.getValuesForDynamicProperty(aValue);
        } catch (Exception e) {
          throw new InvalidDynamicQueryException();
        }
      }

      Class aClass = Class.forName(this.getAdditionalPropertyTypes().get(aType));
      PropertyDescription aPropertyDescription = (PropertyDescription) aClass.newInstance();
      aPropertyDescription.setName(aName);
      aPropertyDescription.setRequired(isRequired);
      aPropertyDescription.configureWithValueAdditionalValue(aValue);

      aProject.addPropertyDescription(aPropertyDescription);
    }

  }

  /**
   * Recibe una lista de identificadores de �tems que se deben mover en forma masiva a un nuevo estado.<br>
   * Solamente los �tems que no se encuentren en estado Cerrado o Bloqueado ser�n movidos. Adem�s, existe la
   * restricci�n de que debe existir un camino en el workflow entre el nodo actual de cada �tem y el nodo
   * propuesto como destino.
   * 
   * @param aProjectDTO
   *          es el DTO que representa al proyecto al cual pertenecen los �tems.
   * @param aWorkflowNodeDescriptionDTO
   *          es el DTO que representa al nodo del workflow al que se debe mover cada �tem.
   * @param someItems
   *          es una lista separada por comas con los identificadores de los �tems.
   * @return un string que contiene los identificadores de los �tems que no se pudieron mover al nuevo nodo.
   * @throws una
   *           excepci�n que puede levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public String massiveItemsMovement(ProjectDTO aProjectDTO,
      WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO, String someItems) throws Exception {

    ItemsRepositoryBI itemsRepository = this.getItemsRepository();

    // recupera el proyecto.
    ProjectsRepositoryBI projectsRepository = this.getProjectsRepository();
    Project aProject = projectsRepository.findById(aProjectDTO.getOid());

    // transforma los identificadores para poder recuperar todos los �tems
    // concretamente.
    Collection<String> itemsIds = new ArrayList<String>();
    StringTokenizer tokenizer = new StringTokenizer(someItems, ",");
    while (tokenizer.hasMoreTokens()) {
      itemsIds.add(tokenizer.nextToken());
    }
    // recupera todos los �tems a partir de sus ids.
    Collection<Item> items = itemsRepository.findItemsById(itemsIds, aProjectDTO.getOid());

    // recupera la descripci�n de nodo a la que se intenta pasar los �tems.
    WorkflowsRepositoryBI workflowsRepository = this.getWorkflowsRepository();
    WorkflowNodeDescription aNode = workflowsRepository
        .findWorkflowNodeDescriptionById(aWorkflowNodeDescriptionDTO.getOid());

    Collection<String> movedItems = aProject.massiveItemsMovement(items, aNode);

    // remueve de las colecci�n de ids recibidos todos aquellos que fueron
    // movidos al siguiente nodo.
    for (String s : movedItems) {
      itemsIds.remove(s);
    }

    // arma el string de resultado, dejando solamente los identificadores de
    // los items no movidos.
    StringBuffer result = new StringBuffer("");
    for (String s : itemsIds) {
      result.append(s + ",");
    }
    String finalResult = "";
    if (result.length() > 0) {
      finalResult = result.substring(0, result.length() - 1);
    }
    return finalResult;
  }
}
