/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deberán implementar.<br>
 * 
 */
package zinbig.item.services.bi;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.exceptions.ProjectNameNotUniqueException;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Esta interface define el protocolo de los servicios relacionados con los proyectos del sistema que deberán
 * ser implementados por clases concretas.<br>
 * Todo servicio en ningún momento debe devolver un objeto de dominio, sino que siempre devolverá DTOs.<br>
 * Por último, los servicios no contienen lógica de negocios propiamente dicha sino que son encargados de
 * ejecutar dicha lógica presente en el modelo de dominio.<br>
 * Todos los métodos de esta interface declaran el lanzamiento de excepciones. Algunas serán excepciones de
 * negocio y otras inesperadas (como la caída de la conexión a la base de datos). La declaración del
 * lanzamiento tiene como objetivo lograr que el cliente tenga que manejar excepciones cada vez que invoque a
 * un servicio. Si bien aquí se declaran excepciones generales, en la documentación de cada método también
 * figura la excepción particular de negocio que podría ser lanzada por la implementación en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y además en forma particular alguna excepción de
 * negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public interface ProjectsServiceBI {

  /**
   * Recupera los proyectos públicos.
   * 
   * @param projectsCount
   *          indica la cantidad de proyectos públicos que se deben recuperar como máximo.
   * @return una colección que contiene todos los proyectos públicos, hasta la cantidad máxima solicitada.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> findPublicProjects(int projectsCount) throws Exception;

  /**
   * Recupera todos los proyectos públicos.
   * 
   * @return una colección que contiene todos los proyectos públicos.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> findAllPublicProjects() throws Exception;

  /**
   * Recupera una colección que contiene todos los proyectos en los que un usuario participa y los proyectos
   * públicos.
   * 
   * @param anUserDTO
   *          es el usuario para el cual hay que buscar sus proyectos.
   * @return una colección (sin repetidos) que contiene los proyectos en los que participa un usuario.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> findProjectsOfUser(UserDTO anUserDTO) throws Exception;

  /**
   * Recupera una colección que contiene todos los proyectos en los que un usuario participa.
   * 
   * @param anUserDTO
   *          es el usuario para el cual hay que buscar sus proyectos.
   * @return una colección (sin repetidos) que contiene los proyectos en los que participa un usuario.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> findPrivateProjectsOfUser(UserDTO anUserDTO) throws Exception;

  /**
   * Recupera todos los proyectos.
   * 
   * @return una colección que contiene todos los proyectos.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> findAllProjects() throws Exception;

  /**
   * Getter.
   * 
   * @return la cantidad de proyectos que existen.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public int getProjectsCount() throws Exception;

  /**
   * Getter.
   * 
   * @param anUserDTO
   *          es el dto que representa al usuario.
   * @return la cantidad de proyectos que existen.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public int getProjectsCount(UserDTO anUserDTO) throws Exception;

  /**
   * Getter.
   * 
   * @param firstIndex
   *          es el índice de inicio.
   * @param count
   *          es la cantidad de elementos a recuperar.
   * @param anOrdering
   *          es el orden que se debe aplicar a los resultados.
   * @param aPropertyName
   *          es el nombre de la propiedad que se debe utilizar para ordenar los resultados.
   * @return la colección de dtos de los proyectos del sistema empezando por el índice recibido y devolviendo
   *         únicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> getProjects(int firstIndex, int count, String aPropertyName, String anOrdering)
      throws Exception;

  /**
   * Getter.
   * 
   * @param index
   *          es el índice de inicio.
   * @param count
   *          es la cantidad de elementos a recuperar.
   * @param anOrdering
   *          es el orden que se debe aplicar a los resultados.
   * @param aPropertyName
   *          es el nombre de la propiedad que se debe utilizar para ordenar los resultados.
   * @param anUserDTO
   *          es el dto del usuario.
   * @return la colección de dtos de los proyectos del sistema empezando por el índice recibido y devolviendo
   *         únicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<ProjectDTO> getProjects(int firstIndex, int count, String aPropertyName,
      String anOrdering, UserDTO anUserDTO) throws Exception;

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
   *          indica si el proyecto es público o no.
   * @param aPrioritySetDto
   *          es el dto que representa al conjunto de prioridades seleccionado para el nuevo proyecto.
   * @param aClassName
   *          es el nombre de la clase de la estrategia de asignación de responsables de los ítems.
   * @param aProjectLeader
   *          es el usuario seleccionado como líder del proyecto.
   * @param itemTypesDTOsIterator
   *          es un iterador de una colección que contiene los tipos de ítems seleccionados para el nuevo
   *          proyecto.
   * @param aPathForAttachedFiles
   *          es un directorio a partir del cual se debe crear el directorio para los archivos adjuntos.
   * @throws ProjectNameNotUniqueException
   *           esta excepción se puede levantar en caso de que se intente dar de alta un proyecto con un
   *           nombre que ya existe.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public ProjectDTO addProject(String aName, String aShortName, String aLink, boolean isPublicProject,
      PrioritySetDTO aPrioritySetDto, String aClassName, String anUsername,
      Iterator<ItemTypeDTO> itemTypesDTOsIterator, String aPathForAttachedFiles) throws Exception;

  /**
   * Getter.
   * 
   * @param aName
   *          es el nombre del proyecto que se está buscando.
   * @return true en caso de que exista un proyecto con el nombre dado; false en caso contrario.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public boolean existsProjectWithName(String aName) throws Exception;

  /**
   * Recupera un proyecto a partir de su oid.
   * 
   * @param anOid
   *          es el oid del proyecto que se debe recuperar.
   * @param mustLoadCompleteProject
   *          establece si se debe cargar todo el proyecto completo.
   * @return el proyecto identificado por el oid recibido.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public ProjectDTO findProjectById(String anOid, boolean mustLoadCompleteProject) throws Exception;

  /**
   * Edita la información almacenada de un proyecto.
   * 
   * @param projectDTO
   *          es el dto que contiene la información que se debe editar de un proyecto. El oid es el único
   *          campo que no se puede editar, y por lo tanto se utiliza para encontrar al proyecto que debe ser
   *          editado.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public void editProject(ProjectDTO projectDTO) throws Exception;

  /**
   * Getter.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se debe listar todos sus miembros.
   * @return una colección que contiene todos los usuarios del proyecto.
   * @throws Exception
   *           exta excepción puede levantarse en caso de que ocurra un error en la ejecución del servicio.
   */
  public Collection<UserDTO> getUsersOfProject(ProjectDTO aProjectDTO) throws Exception;

  /**
   * Getter.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se debe listar todos sus grupos de usuarios.
   * @return una colección que contiene todos los grupos de usuarios del proyecto.
   * @throws Exception
   *           exta excepción puede levantarse en caso de que ocurra un error en la ejecución del servicio.
   */
  public Collection<UserGroupDTO> getUserGroupsOfProject(ProjectDTO aProjectDTO);

  /**
   * Borra los proyectos representados por los DTOs recibidos.
   * 
   * @param selectedProjectsOids
   *          es una colección que contiene los oids de los proyectos que deben ser eliminados.
   * @throws Exception
   *           es una excepción que puede levantarse a raíz de la eliminación de los proyectos.
   */
  public void deleteProjects(Collection<String> selectedProjectsOids) throws Exception;

  /**
   * Recupera la colección de proyectos que el usuario representado por el dto recibido ha seleccionado como
   * favoritos.
   * 
   * @param anUserDTO
   *          es el dto del usuario.
   * @return una colección de dtos que representan a los proyectos favoritos del usuario.
   */
  public Collection<ProjectDTO> findFavoriteProjectsOfUser(UserDTO anUserDTO);

  /**
   * Marca a un proyecto determinado como favorito del usuario recibido.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que debe ser marcado como favorito.
   * @param userDTO
   *          es le dto que reprsenta al usuario que marcó al proyecto como favorito.
   * @return un dto que representa al usuario actualizado.
   * @throws Exception
   *           es una excepción que puede levantarse a raíz de la ejecución de este servicio.
   */
  public UserDTO addFavoriteProjectToUser(ProjectDTO aProjectDTO, UserDTO userDTO) throws Exception;

  /**
   * Quita la marca a un proyecto determinado como favorito del usuario recibido.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que debe ser desmarcado como favorito.
   * @param userDTO
   *          es le dto que reprsenta al usuario que desmarcó al proyecto como favorito.
   * @return un dto que representa al dto del usuario actualizado.
   * @throws Exception
   *           es una excepción que puede levantarse a raíz de la ejecución de este servicio.
   */
  public UserDTO removeFavoriteProjectFromUser(ProjectDTO aProjectDTO, UserDTO userDTO) throws Exception;

  /**
   * Agrega como favoritos a los proyectos cuyos identificadores se han recibido.
   * 
   * @param selectedProjects
   *          es una colección que contiene los identificadores.
   * @param anUserDTO
   *          es el usuario para el cual hay que agregar los proyectos favoritos.
   * @return una colección con los dtos de los proyectos agregados como favoritos.
   * @throws Exception
   *           es una excepción que puede levantarse a raíz de la eliminación de los proyectos.
   */
  public Collection<ProjectDTO> addFavoriteProjectsToUser(Collection<String> selectedProjects,
      UserDTO anUserDTO) throws Exception;

  /**
   * Recupera todas las definiciones de propiedades adicionales del proyecto.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se deben recuperar las definiciones de propiedades adicionales.
   * @return una colección que contiene los DTOs de las propiedades adicionales que se definieron para el
   *         proyecto.
   * @throws Exception
   *           es cualquier excepción que podría levantarse a raíz de la ejecución de este servicio.
   */
  public Collection<PropertyDescriptionDTO> getPropertyDescriptionsOfProject(ProjectDTO aProjectDTO)
      throws Exception;

  /**
   * Obtiene la lista de valores asociada a una descripción de propiedad adicional dinámica.
   * 
   * @param aQuery
   *          es la consulta que se debe ejecutar.
   * @return una lista de string que representa los valores posibles para la propiedad adicional.
   */
  public Collection<String> getValuesForDynamicProperty(String aQuery);

  /**
   * Obtiene la cantidad de propiedades adicionales que tiene el proyecto representado por el DTO.
   * 
   * @param aProjectDTO
   *          es el DTO que representa al proyecto que se debe consultar.
   * @return la cantidad de propiedades adicionales; 0 en caso que de que no existan.
   */
  public int getAdditionalPropertiesCountOfProject(ProjectDTO aProjectDTO);

  /**
   * Obtiene una colección de propiedades adicionales del proyecto recibido.
   * 
   * @param projectDTO
   *          es el DTO que representa al proyecto.
   * @param aPropertyName
   *          es el nombre de la propiedad por la que se debe ordenar el resultado.
   * @param isAscending
   *          establece si el orden es ascendente o no.
   * @return una colección de propiedades adicionales.
   * @throws Exception
   *           esta excepción se levanta en caso de algún error en la ejecución de este servicio.
   */
  public Collection<PropertyDescriptionDTO> getPropertyDescriptionsOfProject(ProjectDTO projectDTO,
      String aPropertyName, boolean isAscending) throws Exception;

  /**
   * Elimina de un proyecto las propiedades adicionales seleccionadas.
   * 
   * @param projectDTO
   *          es el DTO que representa al proyecto en el cual se deben eliminar las propiedades.
   * @param selectedProperties
   *          es una colección que contiene los identificadores de las propiedades adicionales que se deben
   *          eliminar.
   * @throws Exception
   *           esta excepción se puede levantar a raíz de la ejecución de este servicio.
   */
  public void deletePropertyDescriptionsOfProject(ProjectDTO projectDTO, Collection<String> selectedProperties)
      throws Exception;

  /**
   * Agrega una nueva descripción de propiedad al proyecto.
   * 
   * @param aProjectDTO
   *          es el DTO que representa al proyecto que se está editando.
   * @param aName
   *          es el nombre de la nueva propiedad.
   * @param isRequired
   *          establece si la nueva propiedad es requerida o no.
   * @param aType
   *          define el tipo de la propiedad adicional.
   * @param aValue
   *          es un valor adicional requerido por algunos tipos de propiedades adicionales.
   * @throws Exception
   *           esta excepción se puede levantar a raíz de la ejecución de este servicio.
   */
  public void addAdditionalProperty(ProjectDTO aProjectDTO, String aName, boolean isRequired, String aType,
      String aValue) throws Exception;

  /**
   * Recibe una lista de identificadores de ítems que se deben mover en forma masiva a un nuevo estado.<br>
   * Solamente los ítems que no se encuentren en estado Cerrado o Bloqueado serán movidos. Además, existe la
   * restricción de que debe existir un camino en el workflow entre el nodo actual de cada ítem y el nodo
   * propuesto como destino.
   * 
   * @param aProjectDTO
   *          es el DTO que representa al proyecto al cual pertenecen los ítems.
   * @param aWorkflowNodeDescriptionDTO
   *          es el DTO que representa al nodo del workflow al que se debe mover cada ítem.
   * @param someItems
   *          es una lista separada por comas con los identificadores de los ítems.
   * @return un string que contiene los identificadores de los ítems que no se pudieron mover al nuevo nodo.
   * @throws una
   *           excepción que puede levantarse a raíz de la ejecución de este servicio.
   */
  public String massiveItemsMovement(ProjectDTO aProjectDTO,
      WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO, String someItems) throws Exception;

}
