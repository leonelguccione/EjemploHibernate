/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import zinbig.item.model.Comment;
import zinbig.item.model.Item;
import zinbig.item.model.ItemFile;
import zinbig.item.model.ItemType;
import zinbig.item.model.Operation;
import zinbig.item.model.filters.Filter;
import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.PrioritySet;
import zinbig.item.model.projects.Project;
import zinbig.item.model.properties.DynamicValuesListPropertyDescription;
import zinbig.item.model.properties.FixedValuesListPropertyDescription;
import zinbig.item.model.properties.PropertyDescription;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.model.workflow.WorkflowLinkDescription;
import zinbig.item.model.workflow.WorkflowNode;
import zinbig.item.model.workflow.WorkflowNodeDescription;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.security.EncryptionStrategy;

/**
 * La única instancia de esta clase se utiliza para crear las instancias de los DTO para los objetos de
 * dominio.<br>
 * Es una implementación de los patrones de diseño Singleton y Builder.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class DTOFactory {

  /**
   * Mantiene una referencia a la única instancia de esta clase.
   */
  private static DTOFactory instance;

  /**
   * Es la estrategia de encriptación a utilizar.
   */
  protected EncryptionStrategy encryptionStrategy;

  /**
   * Constructor.<br>
   * Este constructor es privado para asegurar que no se pueda utilizar directamente. Para obtener la única
   * instancia de esta clase se debe utilizar el método getInstance().
   */
  private DTOFactory() {
    super();
  }

  /**
   * Método de clase para acceder a la única instancia de esta clase.
   * 
   * @return la única instancia de esta clase.
   */
  public static DTOFactory getInstance() {
    if (instance == null) {
      instance = new DTOFactory();
    }
    return instance;
  }

  /**
   * Crea un DTO que representa a un usuario.<br>
   * La representación de un usuario está compuesta por su nombre de usuario.
   * 
   * @param anUser
   *          es el usuario que se debe representar.
   * @param mustCreateCompleteDTO
   *          define si se debe crear un dto completo para el usuario o solamente uno básico.
   * @return un DTO que representa un usuario.
   */
  public UserDTO createDTOForUser(User anUser, boolean mustCreateCompleteDTO) {
    UserDTO dto;

    boolean isLeader = false;
    int favoriteProjects = 0;
    Map<String, String> preferences = new HashMap<String, String>();

    if (mustCreateCompleteDTO) {
      isLeader = anUser.isProjectLeader();

      // carga información de las preferencias del usuario.
      preferences.putAll(anUser.getUserPreferences());

      // carga la cantidad de proyectos favoritos
      favoriteProjects = anUser.getFavoriteProjectsCount();

    }
    // carga de los datos básicos
    dto = new UserDTO(anUser.getOid(), anUser.getUsername(), this.getEncryptionStrategy().decrypt(
        anUser.getPassword()), anUser.getLanguage(), anUser.getEmail(), anUser.getName(),
        anUser.getSurname(), anUser.isDeletable(), anUser.getVersion(), !anUser.isDeletable(),
        favoriteProjects, isLeader);
    dto.getUserPreferences().putAll(preferences);
    return dto;
  }

  /**
   * Crea un dto completamente cargado para un usuario.
   * 
   * @param anUser
   *          es el usuario que debe ser representado.
   * @param isAdminUser
   *          indica si el usuario es el administrador del sistema.
   * @param operations
   *          es el conjunto de operaciones asignadas al usuario.
   * @param mustVerifyLeadership
   *          indica si se debe verificar o no si el usuario es líder del proyecto.
   * @return un dto que contiene toda la información relacionada con el usuario.
   */
  public UserDTO createCompleteDTOForUser(User anUser, boolean isAdminUser, Collection<Operation> operations,
      boolean mustVerifyLeadership) {

    boolean isLeader = false;
    if (mustVerifyLeadership) {
      isLeader = anUser.isProjectLeader();
    }

    UserDTO dto = new UserDTO(anUser.getOid(), anUser.getUsername(), this.getEncryptionStrategy().decrypt(
        anUser.getPassword()), anUser.getLanguage(), anUser.getEmail(), anUser.getName(),
        anUser.getSurname(), anUser.isDeletable(), anUser.getVersion(), isAdminUser,
        anUser.getFavoriteProjectsCount(), isLeader);

    Collection<OperationDTO> operationsDTOs = this.createDTOForOperations(operations);

    dto.setOperations(operationsDTOs);

    dto.getUserPreferences().putAll(anUser.getUserPreferences());
    return dto;
  }

  /**
   * Crea un dto que representa a una operación.
   * 
   * @param anOperation
   *          es la operación que debe ser representada por el dto.
   * @return un DTO que contiene el nombre de la operación y su oid.
   */
  public OperationDTO createDTOForOperation(Operation anOperation) {
    OperationDTO aDTO = new OperationDTO(anOperation.getOid(), anOperation.getName(),
        anOperation.getCategoryName(), anOperation.getTargetPageClassName(), anOperation.getMenuSection(),
        anOperation.isVisibleInMenu());

    return aDTO;
  }

  /**
   * Crea dtos de las operaciones recibidas.
   * 
   * @param someOperations
   *          es la colección de operaciones para las cuales hay que crear los dtos.
   * @return una colección que contiene los dtos de cada una de las operaciones.
   */
  public Collection<OperationDTO> createDTOForOperations(Collection<Operation> someOperations) {
    Collection<OperationDTO> result = new ArrayList<OperationDTO>();
    Iterator<Operation> iterator = someOperations.iterator();
    Operation anOperation = null;

    while (iterator.hasNext()) {
      anOperation = iterator.next();
      result.add(this.createDTOForOperation(anOperation));
    }

    return result;
  }

  /**
   * Crea un dto que representa a un proyecto.
   * 
   * @param aProject
   *          es el proyecto que debe ser representado por el dto.
   * @return un DTO que contiene el nombre del proyecto.
   */
  public ProjectDTO createDTOForProject(Project aProject) {
    return this.createCompleteDTOForProject(aProject, true, true, true);
  }

  /**
   * Crea un dto que representa al grupo de usuarios recibido.
   * 
   * @param newUserGroup
   *          es el grupo que debe ser representado por el dto.
   * @param mustCreateCompleteDTO
   *          especifica si se debe crear un dto completo con todos los datos o uno simple.
   * @return un dto que representa al nuevo grupo.
   */
  public UserGroupDTO createDTOForUserGroup(UserGroup newUserGroup, boolean mustCreateCompleteDTO) {
    UserGroupDTO dto;

    // carga de los datos básicos
    dto = new UserGroupDTO(newUserGroup.getName(), newUserGroup.getEmail(), newUserGroup.isDeletable(),
        newUserGroup.getVersion(), newUserGroup.getOid(), newUserGroup.getLanguage());

    if (mustCreateCompleteDTO) {
      // carga las operaciones del grupo de usuarios
      Iterator<Operation> iterator = newUserGroup.getOperations().iterator();
      while (iterator.hasNext()) {
        dto.getOperationDTOs().add(this.createDTOForOperation(iterator.next()));
      }

      Iterator<Project> projectsIterator = ((UserGroup) newUserGroup).getProjects().iterator();
      while (projectsIterator.hasNext()) {
        dto.getProjectDTOs().add(this.createDTOForProject(projectsIterator.next()));
      }

    }

    return dto;
  }

  /**
   * Crea dtos para todos los grupos contenidos en la colección de grupos de usuarios.
   * 
   * @param someUserGroups
   *          es la colección que contiene todos los grupos de usuarios.
   * @param mustCreateCompleteDTO
   *          define si se debe crear un dto completo, o solo con sus datos básicos.
   * @return una colección de dtos que representan a los grupos de usuarios.
   */
  public Collection<UserGroupDTO> createDTOForUserGroups(Collection<UserGroup> someUserGroups,
      boolean mustCreateCompleteDTO) {

    Collection<UserGroupDTO> result = new ArrayList<UserGroupDTO>();
    Iterator<UserGroup> iterator = someUserGroups.iterator();
    while (iterator.hasNext()) {
      result.add(this.createDTOForUserGroup(iterator.next(), mustCreateCompleteDTO));
    }
    return result;
  }

  /**
   * Getter.
   * 
   * @return la estrategia de encriptación.
   */
  public EncryptionStrategy getEncryptionStrategy() {
    return this.encryptionStrategy;
  }

  /**
   * Setter.
   * 
   * @param aStrategy
   *          es la estrategia de encriptación.
   */
  public void setEncryptionStrategy(EncryptionStrategy aStrategy) {
    this.encryptionStrategy = aStrategy;
  }

  /**
   * Crea dtos para todos los usuarios contenidos en la colección.
   * 
   * @param someUsers
   *          es la colección que contiene todos los usuarios.
   * @return una colección de dtos que representan a los usuarios.
   */
  public Collection<UserDTO> createDTOForUsers(Collection<User> someUsers) {

    Collection<UserDTO> result = new TreeSet<UserDTO>(new UserDTOComparator());
    Iterator<User> iterator = someUsers.iterator();
    while (iterator.hasNext()) {
      result.add(this.createDTOForUser(iterator.next(), false));
    }

    return result;
  }

  /**
   * Crea dtos para todos los proyectos contenidos en la colección.
   * 
   * @param someProjects
   *          es la colección que contiene todos los proyectos.
   * @param aBoolean
   *          indica si cada proyecto tiene un oid o no.
   * @return una colección de dtos que representan a los proyectos.
   */
  public Collection<ProjectDTO> createDTOForProjects(Collection<Project> someProjects, boolean aBoolean) {

    Collection<ProjectDTO> result = new ArrayList<ProjectDTO>();
    Iterator<Project> iterator = someProjects.iterator();
    while (iterator.hasNext()) {
      result.add(this.createCompleteDTOForProject(iterator.next(), false, false, false));
    }
    return result;
  }

  /**
   * Crea dtos para todos los conjuntos de prioridades contenidos en la colección.
   * 
   * @param somePrioritySets
   *          es la colección que contiene todos los conjuntos de prioridades.
   * @param aBoolean
   *          indica si cada conjunto de prioridad tiene un oid o no.
   * @return una colección de dtos que representan a los conjuntos de prioridades.
   */
  public Collection<PrioritySetDTO> createDTOForPrioritySets(Collection<PrioritySet> somePrioritySets,
      boolean aBoolean) {

    Collection<PrioritySetDTO> result = new ArrayList<PrioritySetDTO>();
    Iterator<PrioritySet> iterator = somePrioritySets.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForPrioritySet(iterator.next()));
    }
    return result;
  }

  /**
   * Crea un dto que representa a un conjunto de prioridades.
   * 
   * @param aPrioritySet
   *          es el conjunto de prioridades que debe ser representado por el dto.
   * @return un DTO que contiene el nombre del conjunto de prioridades.
   */
  public PrioritySetDTO createDTOForPrioritySet(PrioritySet aPrioritySet) {
    PrioritySetDTO aDto = new PrioritySetDTO(aPrioritySet.getName(), aPrioritySet.getOid(),
        aPrioritySet.isDeletable(), aPrioritySet.getVersion(), aPrioritySet.isDefaultPrioritySet(),
        aPrioritySet.getPriorities().size());

    return aDto;
  }

  /**
   * Crea un dto para el proyecto recibido.
   * 
   * @param aProject
   *          es el proyecto que debe ser representado por el dto.
   * @param mustLoadPrioritySetDTO
   *          indica si se debe cargar el dto del conjunto de prioridades del proyecto.
   * @param mustLoadWorkflowDescriptionDTO
   *          indica si se debe cargar la información del workflow del proyecto.
   * @return un dto creado para el proyecto, cargado de acuerdo a los parámetros recibidos.
   */
  public ProjectDTO createCompleteDTOForProject(Project aProject, boolean mustLoadPrioritySetDTO,
      boolean mustLoadWorkflowDescriptionDTO, boolean mustLoadPropertyDescriptions) {
    UserDTO anUserDTO = this.createDTOForUser(aProject.getProjectLeader(), false);

    WorkflowDescriptionDTO aWorkflowDescriptionDTO = null;
    Long itemsCount = new Long(0);
    // verifica si se debe cargar la información del proyecto
    if (mustLoadWorkflowDescriptionDTO) {
      aWorkflowDescriptionDTO = this.createDTOForWorkflowDescription(aProject.getWorkflowDescription());
      itemsCount = new Long(aProject.getItems().size());
    }

    Collection<PropertyDescriptionDTO> propertyDescriptions = new ArrayList<PropertyDescriptionDTO>();
    if (mustLoadPropertyDescriptions) {
      if (!aProject.getItemProperties().isEmpty()) {
        propertyDescriptions.addAll(this.createDTOsForPropertyDescriptions(aProject.getItemProperties(),
            ServiceLocator.getInstance().getProjectsService()));
      }
    }

    ProjectDTO aProjectDTO = new ProjectDTO(aProject.getName(), aProject.getShortName(), aProject.getOid(),
        aProject.getProjectLink(), aProject.getVersion(), aProject.isPublicProject(),
        aProject.getItemResponsibleAssignmentStrategyClassName(), anUserDTO, itemsCount,
        aWorkflowDescriptionDTO, propertyDescriptions);

    if (mustLoadPrioritySetDTO) {
      // se debe cargar el dto del conjunto de prioridades
      aProjectDTO.setPrioritySetDTO(this.createDTOForPrioritySet(aProject.getPrioritySet()));
    }

    return aProjectDTO;
  }

  /**
   * Crea DTOs para las definiciones de las propiedades adicionales de los ítems.
   * 
   * @param aCollection
   *          es una colección que contiene las descripciones de propiedades que deben representarse.
   * @param aService
   *          es el servicio de proyectos que se utiliza para acceder a la lista dinámica de valores.
   * @return una colección con los DTOs recientemente creados.
   */
  public Collection<PropertyDescriptionDTO> createDTOsForPropertyDescriptions(
      Collection<PropertyDescription> aCollection, ProjectsServiceBI aService) {
    Iterator<PropertyDescription> iterator = aCollection.iterator();
    PropertyDescription aPropertyDescription = null;
    Collection<PropertyDescriptionDTO> result = new ArrayList<PropertyDescriptionDTO>();

    while (iterator.hasNext()) {
      aPropertyDescription = iterator.next();
      result.add(this.createDTOForPropertyDescription(aPropertyDescription, aService));
    }
    return result;
  }

  /**
   * Crea un DTO para representar a una descripción de propiedad adicional.
   * 
   * @param aPropertyDescription
   *          es la propiedad que debe representarse con el DTO.
   * @param aService
   *          es el servicio que se debe utilizar para acceder a la lista de valores de las descripciones de
   *          propiedades dinámicas.
   * @return el DTO que representa a la descripción de propiedad.
   */
  public PropertyDescriptionDTO createDTOForPropertyDescription(PropertyDescription aPropertyDescription,
      ProjectsServiceBI aService) {
    PropertyDescriptionDTO dto = new PropertyDescriptionDTO(aPropertyDescription.getOid(),
        aPropertyDescription.getName(), aPropertyDescription.isRequired(), aPropertyDescription.getType());
    if (aPropertyDescription.getType() == 'F') {
      dto.setValues(((FixedValuesListPropertyDescription) aPropertyDescription).getValuesAsCollection());
    } else {
      if (aPropertyDescription.getType() == 'D') {
        try {
          dto.setValues(aService
              .getValuesForDynamicProperty(((DynamicValuesListPropertyDescription) aPropertyDescription)
                  .getQuery()));
        } catch (Exception e) {

          e.printStackTrace();
        }
      }
    }

    return dto;
  }

  /**
   * Crea DTOs para representar a cada una de las prioridades de la colección.
   * 
   * @param somePriorities
   *          es el conjunto de prioridades que debe ser representado.
   * @return una colección que contiene DTOs para cada prioridad.
   */
  public Collection<PriorityDTO> createDTOForPriorities(Collection<Priority> somePriorities) {

    Collection<PriorityDTO> result = new ArrayList<PriorityDTO>();
    Iterator<Priority> iterator = somePriorities.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForPriority(iterator.next()));
    }
    return result;
  }

  /**
   * Crea un dto que representa a una prioridad.
   * 
   * @param aPriority
   *          la prioridad que debe ser representada a través del DTO.
   * @return un DTO que contiene el nombre de la prioridad, su valor y el path de la imagen asociada.
   */
  public PriorityDTO createDTOForPriority(Priority aPriority) {
    PriorityDTO aDto = new PriorityDTO(aPriority.getOid(), aPriority.getTitle(), aPriority.getValue(),
        aPriority.getReferencesCount());

    return aDto;
  }

  /**
   * Crea un dto que representa a un ítem.
   * 
   * @param anItem
   *          es el ítem que debe ser representado.
   * @return un dto cargado con la información mínima y básica requerida. <br>
   *         La historia del ítem no se carga a través de este método por cuestiones de performance.
   */
  public ItemDTO createDTOForItem(Item anItem) {

    AbstractUserDTO aResponsibleDTO = null;

    if (anItem.getResponsible().isUserGroup()) {
      aResponsibleDTO = this.createDTOForUserGroup((UserGroup) anItem.getResponsible(), false);
    } else {
      aResponsibleDTO = this.createDTOForUser((User) anItem.getResponsible(), false);
    }

    UserDTO aProjectLeaderDTO = this.createDTOForUser(anItem.getProject().getProjectLeader(), false);

    UserDTO aCreatorDTO = this.createDTOForUser(anItem.getCreator(), false);

    ProjectDTO aProjectDTO = this.createCompleteDTOForProject(anItem.getProject(), false, false, false);

    PriorityDTO aPriorityDTO = this.createDTOForPriority(anItem.getPriority());

    ItemTypeDTO anItemTypeDTO = this.createDTOForItemType(anItem.getItemType());

    WorkflowNodeDTO aWorkflowNodeDTO = this.createDTOForWorkflowNode(anItem.getCurrentWorkflowNode());

    Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    Map<String, String> additionalProperties = new HashMap<String, String>();
    additionalProperties.putAll(anItem.getAdditionalProperties());

    ItemDTO aDTO = new ItemDTO(anItem.getTitle(), anItem.getDescription(), anItem.getItemId(),
        aResponsibleDTO, aProjectLeaderDTO, anItem.getState().toString(), aProjectDTO, aPriorityDTO,
        anItem.getOid(), anItemTypeDTO, aWorkflowNodeDTO, anItem.getVersion(), formatter.format(anItem
            .getCreationDate()), aCreatorDTO, anItem.isFinished(), additionalProperties);

    return aDTO;
  }

  /**
   * Crea un DTO para representa un nodo del workflow.
   * 
   * @param aWorkflowNode
   *          es el nodo que se debe representar.
   * @return un dto que contiene el título y el oid del nodo.
   */
  public WorkflowNodeDTO createDTOForWorkflowNode(WorkflowNode aWorkflowNode) {
    String aTitle = "";
    String anOid = "0";
    String alias = "";
    String creationDate = "";
    if (aWorkflowNode != null) {
      aTitle = aWorkflowNode.getTitle();
      anOid = aWorkflowNode.getOid();
      alias = aWorkflowNode.getCurrentResponsible().getAlias();
      Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      creationDate = formatter.format(aWorkflowNode.getCreationDate());
    }
    WorkflowNodeDTO aDTO = new WorkflowNodeDTO(aTitle, anOid, creationDate, alias);

    return aDTO;
  }

  /**
   * Crea DTOs para representar a cada una de las descripciones de workflow de la colección.
   * 
   * @param someWorkflowDescriptions
   *          es el conjunto de descripciones de workflow que debe ser representado.
   * @return una colección que contiene DTOs para cada descripción de workflow.
   */
  public Collection<WorkflowDescriptionDTO> createDTOForWorkflowDescriptions(
      Collection<WorkflowDescription> someWorkflowDescriptions) {

    Collection<WorkflowDescriptionDTO> result = new ArrayList<WorkflowDescriptionDTO>();
    Iterator<WorkflowDescription> iterator = someWorkflowDescriptions.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForWorkflowDescription(iterator.next()));
    }
    return result;
  }

  /**
   * Crea un dto que representa a una descripción de workflow.
   * 
   * @param aWorkflowDescription
   *          es la descripción de workflow que debe ser representada a través del DTO.
   * @return un DTO que contiene el título y el oid de la descripción del workflow.
   */
  public WorkflowDescriptionDTO createDTOForWorkflowDescription(WorkflowDescription aWorkflowDescription) {
    WorkflowDescriptionDTO aDto = new WorkflowDescriptionDTO(aWorkflowDescription.getOid(),
        aWorkflowDescription.getTitle(), aWorkflowDescription.getVersion());

    return aDto;
  }

  /**
   * Crea DTOs para representar a cada uno de los ítems de la colección.
   * 
   * @param someItems
   *          es el conjunto de ítems que debe ser representado.
   * @return una colección que contiene DTOs para cada ítem.
   */
  public Collection<ItemDTO> createDTOForItems(Collection<Item> someItems) {

    Collection<ItemDTO> result = new ArrayList<ItemDTO>();
    Iterator<Item> iterator = someItems.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForItem(iterator.next()));
    }
    return result;
  }

  /**
   * Crea un DTO para representar la información de un filtro de ítems.
   * 
   * @param aFilter
   *          es el filtro que se debe representar.
   * @param hasOid
   *          indica si el filtro tiene oid o no.
   * @return un dto que representa al filtro.
   */
  public FilterDTO createDTOForItemFilter(Filter aFilter, boolean hasOid) {
    FilterDTO aDTO = new FilterDTO();
    aDTO.setName(aFilter.getName());
    aDTO.setFavorite(aFilter.isFavorite());
    aDTO.setFilterString(aFilter.getFilterString());

    aDTO.setFilterComponentByProject(aFilter.getFilterComponentByProject());
    aDTO.setNegateProject(aFilter.isNegateProject());
    aDTO.setFilterComponentByResponsible(aFilter.getFilterComponentByResponsible());
    aDTO.setNegateResponsible(aFilter.isNegateResponsible());
    aDTO.setFilterComponentByState(aFilter.getFilterComponentByState());
    aDTO.setNegateState(aFilter.isNegateState());
    aDTO.setFilterComponentByNode(aFilter.getFilterComponentByNode());
    aDTO.setNegateNode(aFilter.isNegateNode());
    aDTO.setFilterComponentByItemType(aFilter.getFilterComponentByItemType());
    aDTO.setNegateItemType(aFilter.isNegateItemType());

    if (hasOid) {
      aDTO.setOid(aFilter.getOid());
    }
    aDTO.setFilterComponentByText(aFilter.getFilterComponentByText());
    return aDTO;
  }

  /**
   * Crea dtos para la colección de filtros recibidos.
   * 
   * @param aCollection
   *          es la colección que contiene los filtros.
   * @param aBoolean
   *          indica si los filtros tienen un oid.
   * @return una colección de dtos de filtros.
   */
  public Collection<FilterDTO> createDTOForItemFilters(Collection<Filter> aCollection, boolean aBoolean) {
    Collection<FilterDTO> result = new ArrayList<FilterDTO>();
    Iterator<Filter> iterator = aCollection.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForItemFilter(iterator.next(), aBoolean));
    }
    return result;
  }

  /**
   * Crea un dto para representar el comentario.
   * 
   * @param aComment
   *          es el comentario para el cual se debe crear un dto.
   * @return un dto que representa al comentario recibido.
   */
  public CommentDTO createDTOForComment(Comment aComment) {

    Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    CommentDTO result = new CommentDTO(aComment.getComment(), formatter.format(aComment.getCreationDate()),
        aComment.getCreatorUsername());

    return result;
  }

  /**
   * Crea dtos para todos los comentarios recibidos.
   * 
   * @param someComments
   *          es una colección que contiene todos los comentarios.
   * @return una colección que contiene DTOs para cada uno de los comentarios.
   */
  public Collection<CommentDTO> createDTOsForComments(Collection<Comment> someComments) {
    Collection<CommentDTO> result = new ArrayList<CommentDTO>();
    Iterator<Comment> iterator = someComments.iterator();
    while (iterator.hasNext()) {
      result.add(this.createDTOForComment(iterator.next()));
    }

    return result;
  }

  /**
   * Crea dtos de los tipos de ítems recibidos.
   * 
   * @param someItemTypes
   *          es la colección de operaciones para las cuales hay que crear los dtos.
   * @return una colección que contiene los dtos de cada una de las operaciones.
   */
  public Collection<ItemTypeDTO> createDTOForItemTypes(Collection<ItemType> someItemTypes) {
    Collection<ItemTypeDTO> result = new ArrayList<ItemTypeDTO>();
    Iterator<ItemType> iterator = someItemTypes.iterator();
    ItemType anItemType = null;

    while (iterator.hasNext()) {
      anItemType = iterator.next();
      result.add(this.createDTOForItemType(anItemType));
    }

    return result;
  }

  /**
   * Crea un dto para representar el tipo de ítem
   * 
   * @param anItemType
   *          es el tipo de ítem para el cual se debe crear un dto.
   * @return un dto que representa al tipo de ítem recibido.
   */
  public ItemTypeDTO createDTOForItemType(ItemType anItemType) {

    ItemTypeDTO result = new ItemTypeDTO(anItemType.getTitle(), anItemType.getOid(), anItemType.getVersion(),
        anItemType.getReferencesCount());

    return result;
  }

  /**
   * Crea DTOs para representar a cada una de las descripciones de nodos de la colección.
   * 
   * @param someWorkflowNodeDescriptions
   *          es el conjunto de descripciones de nodo que debe ser representado.
   * @return una colección que contiene DTOs para cada descripción de nodo.
   */
  public Collection<WorkflowNodeDescriptionDTO> createDTOForWorkflowNodeDescriptions(
      Collection<WorkflowNodeDescription> someWorkflowNodeDescriptions) {

    Collection<WorkflowNodeDescriptionDTO> result = new ArrayList<WorkflowNodeDescriptionDTO>();
    Iterator<WorkflowNodeDescription> iterator = someWorkflowNodeDescriptions.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForWorkflowNodeDescription(iterator.next()));
    }
    return result;
  }

  /**
   * Crea un dto que representa a una descripción de nodo.
   * 
   * @param aWorkflowNodeDescription
   *          es la descripción de nodo que debe ser representada a través del DTO.
   * @return un DTO que representa la descripción de nodo.
   */
  public WorkflowNodeDescriptionDTO createDTOForWorkflowNodeDescription(
      WorkflowNodeDescription aWorkflowNodeDescription) {
    WorkflowNodeDescriptionDTO aDto = new WorkflowNodeDescriptionDTO(aWorkflowNodeDescription.getNodeTitle(),
        aWorkflowNodeDescription.getReferencesCount(), aWorkflowNodeDescription.isFinalNode(),
        aWorkflowNodeDescription.getOid());

    return aDto;
  }

  /**
   * Crea un dto que representa a una descripción de enlace.
   * 
   * @param aWorkflowLinkDescription
   *          es la descripción de enlace que debe ser representada a través del DTO.
   * @return un DTO que representa la descripción de enlace.
   */
  public WorkflowLinkDescriptionDTO createDTOForWorkflowLinkDescription(
      WorkflowLinkDescription aWorkflowLinkDescription) {
    WorkflowLinkDescriptionDTO aDto = new WorkflowLinkDescriptionDTO(aWorkflowLinkDescription.getTitle(),
        aWorkflowLinkDescription.getOid(), this.createDTOForWorkflowNodeDescription(aWorkflowLinkDescription
            .getInitialNodeDescription()), this.createDTOForWorkflowNodeDescription(aWorkflowLinkDescription
            .getFinalNodeDescription()));

    return aDto;
  }

  /**
   * Crea DTOs para representar a cada una de las descripciones de enlace de la colección.
   * 
   * @param someWorkflowLinkDescriptions
   *          es el conjunto de descripciones de enlaces que debe ser representado.
   * @return una colección que contiene DTOs para cada descripción de enlace.
   */
  public Collection<WorkflowLinkDescriptionDTO> createDTOForWorkflowLinkDescriptions(
      Collection<WorkflowLinkDescription> someWorkflowLinkDescriptions) {

    Collection<WorkflowLinkDescriptionDTO> result = new ArrayList<WorkflowLinkDescriptionDTO>();
    Iterator<WorkflowLinkDescription> iterator = someWorkflowLinkDescriptions.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForWorkflowLinkDescription(iterator.next()));
    }
    return result;
  }

  /**
   * Crea DTOs para todos los objetos recibidos.
   * 
   * @param aCollection
   *          es una colección que contiene usuarios y grupos de usuarios.
   * @return una colección que contiene DTOs que representan a los objetos recibidos.
   */
  public Collection<AbstractUserDTO> createDTOForAbstractUsers(Collection<AbstractUser> aCollection) {
    Iterator<AbstractUser> iterator = aCollection.iterator();
    Collection<AbstractUserDTO> result = new ArrayList<AbstractUserDTO>();

    AbstractUser anAbstractUser = null;
    while (iterator.hasNext()) {
      anAbstractUser = iterator.next();

      if (anAbstractUser.isUserGroup()) {
        result.add(this.createDTOForUserGroup((UserGroup) anAbstractUser, false));
      } else {
        result.add(this.createDTOForUser((User) anAbstractUser, false));
      }
    }

    return result;
  }

  /**
   * Crea DTOs para todos los archivos adjuntos recibidos.
   * 
   * @param attachedFiles
   *          es una colección que contiene los archivos adjuntos.
   * @return una colección con los dtos de los adjuntos.
   */
  public Collection<ItemFileDTO> createDTOForItemFilters(Collection<ItemFile> attachedFiles) {
    Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    Collection<ItemFileDTO> result = new ArrayList<ItemFileDTO>();
    Iterator<ItemFile> iterator = attachedFiles.iterator();
    ItemFile anItemFile = null;

    while (iterator.hasNext()) {
      anItemFile = iterator.next();
      result.add(new ItemFileDTO(anItemFile.getFilename(), formatter.format(anItemFile.getCreationDate()),
          anItemFile.getOid()));
    }
    return result;
  }

  /**
   * Crea DTOs para representar a cada uno de los nodos de la colección.
   * 
   * @param someWorkflowNodes
   *          es el conjunto de nodos que debe ser representado.
   * @return una colección que contiene DTOs para cada nodo.
   */
  public Collection<WorkflowNodeDTO> createDTOForWorkflowNodes(Collection<WorkflowNode> someWorkflowNodes) {

    Collection<WorkflowNodeDTO> result = new ArrayList<WorkflowNodeDTO>();
    Iterator<WorkflowNode> iterator = someWorkflowNodes.iterator();

    while (iterator.hasNext()) {
      result.add(this.createDTOForWorkflowNode(iterator.next()));
    }
    return result;
  }

}
