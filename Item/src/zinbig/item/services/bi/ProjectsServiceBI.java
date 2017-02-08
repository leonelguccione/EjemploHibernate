/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deber�n implementar.<br>
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
 * Esta interface define el protocolo de los servicios relacionados con los proyectos del sistema que deber�n
 * ser implementados por clases concretas.<br>
 * Todo servicio en ning�n momento debe devolver un objeto de dominio, sino que siempre devolver� DTOs.<br>
 * Por �ltimo, los servicios no contienen l�gica de negocios propiamente dicha sino que son encargados de
 * ejecutar dicha l�gica presente en el modelo de dominio.<br>
 * Todos los m�todos de esta interface declaran el lanzamiento de excepciones. Algunas ser�n excepciones de
 * negocio y otras inesperadas (como la ca�da de la conexi�n a la base de datos). La declaraci�n del
 * lanzamiento tiene como objetivo lograr que el cliente tenga que manejar excepciones cada vez que invoque a
 * un servicio. Si bien aqu� se declaran excepciones generales, en la documentaci�n de cada m�todo tambi�n
 * figura la excepci�n particular de negocio que podr�a ser lanzada por la implementaci�n en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y adem�s en forma particular alguna excepci�n de
 * negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public interface ProjectsServiceBI {

  /**
   * Recupera los proyectos p�blicos.
   * 
   * @param projectsCount
   *          indica la cantidad de proyectos p�blicos que se deben recuperar como m�ximo.
   * @return una colecci�n que contiene todos los proyectos p�blicos, hasta la cantidad m�xima solicitada.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findPublicProjects(int projectsCount) throws Exception;

  /**
   * Recupera todos los proyectos p�blicos.
   * 
   * @return una colecci�n que contiene todos los proyectos p�blicos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findAllPublicProjects() throws Exception;

  /**
   * Recupera una colecci�n que contiene todos los proyectos en los que un usuario participa y los proyectos
   * p�blicos.
   * 
   * @param anUserDTO
   *          es el usuario para el cual hay que buscar sus proyectos.
   * @return una colecci�n (sin repetidos) que contiene los proyectos en los que participa un usuario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findProjectsOfUser(UserDTO anUserDTO) throws Exception;

  /**
   * Recupera una colecci�n que contiene todos los proyectos en los que un usuario participa.
   * 
   * @param anUserDTO
   *          es el usuario para el cual hay que buscar sus proyectos.
   * @return una colecci�n (sin repetidos) que contiene los proyectos en los que participa un usuario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findPrivateProjectsOfUser(UserDTO anUserDTO) throws Exception;

  /**
   * Recupera todos los proyectos.
   * 
   * @return una colecci�n que contiene todos los proyectos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<ProjectDTO> findAllProjects() throws Exception;

  /**
   * Getter.
   * 
   * @return la cantidad de proyectos que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public int getProjectsCount() throws Exception;

  /**
   * Getter.
   * 
   * @param anUserDTO
   *          es el dto que representa al usuario.
   * @return la cantidad de proyectos que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public int getProjectsCount(UserDTO anUserDTO) throws Exception;

  /**
   * Getter.
   * 
   * @param firstIndex
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
  public Collection<ProjectDTO> getProjects(int firstIndex, int count, String aPropertyName, String anOrdering)
      throws Exception;

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
   *          es el dto del usuario.
   * @return la colecci�n de dtos de los proyectos del sistema empezando por el �ndice recibido y devolviendo
   *         �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
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
   *          indica si el proyecto es p�blico o no.
   * @param aPrioritySetDto
   *          es el dto que representa al conjunto de prioridades seleccionado para el nuevo proyecto.
   * @param aClassName
   *          es el nombre de la clase de la estrategia de asignaci�n de responsables de los �tems.
   * @param aProjectLeader
   *          es el usuario seleccionado como l�der del proyecto.
   * @param itemTypesDTOsIterator
   *          es un iterador de una colecci�n que contiene los tipos de �tems seleccionados para el nuevo
   *          proyecto.
   * @param aPathForAttachedFiles
   *          es un directorio a partir del cual se debe crear el directorio para los archivos adjuntos.
   * @throws ProjectNameNotUniqueException
   *           esta excepci�n se puede levantar en caso de que se intente dar de alta un proyecto con un
   *           nombre que ya existe.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public ProjectDTO addProject(String aName, String aShortName, String aLink, boolean isPublicProject,
      PrioritySetDTO aPrioritySetDto, String aClassName, String anUsername,
      Iterator<ItemTypeDTO> itemTypesDTOsIterator, String aPathForAttachedFiles) throws Exception;

  /**
   * Getter.
   * 
   * @param aName
   *          es el nombre del proyecto que se est� buscando.
   * @return true en caso de que exista un proyecto con el nombre dado; false en caso contrario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
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
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public ProjectDTO findProjectById(String anOid, boolean mustLoadCompleteProject) throws Exception;

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
  public void editProject(ProjectDTO projectDTO) throws Exception;

  /**
   * Getter.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se debe listar todos sus miembros.
   * @return una colecci�n que contiene todos los usuarios del proyecto.
   * @throws Exception
   *           exta excepci�n puede levantarse en caso de que ocurra un error en la ejecuci�n del servicio.
   */
  public Collection<UserDTO> getUsersOfProject(ProjectDTO aProjectDTO) throws Exception;

  /**
   * Getter.
   * 
   * @param aProjectDTO
   *          es el proyecto para el cual se debe listar todos sus grupos de usuarios.
   * @return una colecci�n que contiene todos los grupos de usuarios del proyecto.
   * @throws Exception
   *           exta excepci�n puede levantarse en caso de que ocurra un error en la ejecuci�n del servicio.
   */
  public Collection<UserGroupDTO> getUserGroupsOfProject(ProjectDTO aProjectDTO);

  /**
   * Borra los proyectos representados por los DTOs recibidos.
   * 
   * @param selectedProjectsOids
   *          es una colecci�n que contiene los oids de los proyectos que deben ser eliminados.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la eliminaci�n de los proyectos.
   */
  public void deleteProjects(Collection<String> selectedProjectsOids) throws Exception;

  /**
   * Recupera la colecci�n de proyectos que el usuario representado por el dto recibido ha seleccionado como
   * favoritos.
   * 
   * @param anUserDTO
   *          es el dto del usuario.
   * @return una colecci�n de dtos que representan a los proyectos favoritos del usuario.
   */
  public Collection<ProjectDTO> findFavoriteProjectsOfUser(UserDTO anUserDTO);

  /**
   * Marca a un proyecto determinado como favorito del usuario recibido.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que debe ser marcado como favorito.
   * @param userDTO
   *          es le dto que reprsenta al usuario que marc� al proyecto como favorito.
   * @return un dto que representa al usuario actualizado.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public UserDTO addFavoriteProjectToUser(ProjectDTO aProjectDTO, UserDTO userDTO) throws Exception;

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
  public UserDTO removeFavoriteProjectFromUser(ProjectDTO aProjectDTO, UserDTO userDTO) throws Exception;

  /**
   * Agrega como favoritos a los proyectos cuyos identificadores se han recibido.
   * 
   * @param selectedProjects
   *          es una colecci�n que contiene los identificadores.
   * @param anUserDTO
   *          es el usuario para el cual hay que agregar los proyectos favoritos.
   * @return una colecci�n con los dtos de los proyectos agregados como favoritos.
   * @throws Exception
   *           es una excepci�n que puede levantarse a ra�z de la eliminaci�n de los proyectos.
   */
  public Collection<ProjectDTO> addFavoriteProjectsToUser(Collection<String> selectedProjects,
      UserDTO anUserDTO) throws Exception;

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
      throws Exception;

  /**
   * Obtiene la lista de valores asociada a una descripci�n de propiedad adicional din�mica.
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
      String aPropertyName, boolean isAscending) throws Exception;

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
      throws Exception;

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
      String aValue) throws Exception;

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
      WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO, String someItems) throws Exception;

}
