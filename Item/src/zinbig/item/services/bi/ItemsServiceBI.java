/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deberán implementar.<br>
 * 
 */
package zinbig.item.services.bi;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import zinbig.item.model.ItemStateEnum;
import zinbig.item.model.exceptions.ItemUnknownException;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.CommentDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTO;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemFileDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PriorityDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Esta interface define el protocolo de los servicios relacionados con los ítem
 * administrados por el sistema que deberán ser implementados por clases
 * concretas.<br>
 * Todo servicio en ningún momento debe devolver un objeto de dominio, sino que
 * siempre devolverá DTOs.<br>
 * Por último, los servicios no contienen lógica de negocios propiamente dicha
 * sino que son encargados de ejecutar dicha lógica presente en el modelo de
 * dominio.<br>
 * Todos los métodos de esta interface declaran el lanzamiento de excepciones.
 * Algunas serán excepciones de negocio y otras inesperadas (como la caída de la
 * conexión a la base de datos). La declaración del lanzamiento tiene como
 * objetivo lograr que el cliente tenga que manejar excepciones cada vez que
 * invoque a un servicio. Si bien aquí se declaran excepciones generales, en la
 * documentación de cada método también figura la excepción particular de
 * negocio que podría ser lanzada por la implementación en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y además en forma
 * particular alguna excepción de negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface ItemsServiceBI {

	/**
	 * Agrega un nuevo ítem al sistema.
	 * 
	 * @param aProjectDTO
	 *            es el proyecto en el cual se está agregando el ítem.
	 * @param aTitle
	 *            es el título del nuevo ítem.
	 * @param aDescription
	 *            es un texto descriptivo del ítem.
	 * @param aDate
	 *            es la fecha de creación del ítem.
	 * @param anUserDTO
	 *            es el usuario que ha creado este ítem.
	 * @param aPriorityDTO
	 *            es la prioridad del ítem.
	 * @param aState
	 *            es el estado del item. Puede ser CREADO o ABIERTO.
	 * @param responsible
	 *            es el dto que representa al usuario o grupo de usuarios que
	 *            será responsable del nuevo ítem.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo del nuevo ítem.
	 * @param someItemIds
	 *            es una colección que contiene los identificadores de los ítems
	 *            que se deben unificar al dar de alta este nuevo ítem.
	 * @param aComment
	 *            es el comentario que se debe agregar a cada ítem unificado
	 *            para dejar asentado que se lo ha unificado.
	 * @param somePropertyDescriptions
	 *            es un diccionario que contiene los valores asignados a cada
	 *            una de las propiedades adicionales del nuevo ítem.
	 * 
	 * @return un dto que representa al nuevo ítem recientemente creado.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de que ocurra un
	 *             error con el alta del ítem.
	 */
	public ItemDTO addItem(ProjectDTO aProjectDTO, String aTitle,
			String aDescription, Date aDate, UserDTO anUserDTO,
			PriorityDTO aPriorityDTO, ItemStateEnum aState,
			AbstractUserDTO responsible, ItemTypeDTO anItemTypeDTO,
			Collection<String> someItemIds, String aComment,
			Map<String, String> somePropertyDescriptions) throws Exception;

	/**
	 * Getter.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario que está solicitando el
	 *            listado de los ítems. En caso de que sea un usuario anónimo
	 *            este parámetro será nulo y por lo tanto se listarán solamente
	 *            los ítems pertenecientes a proyectos públicos. En caso de que
	 *            no sea nulo entonces se listarán además los ítems
	 *            pertencientes a los proyectos en los cuales el usuario está
	 *            asignado.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto actual. Este parámetro
	 *            podría ser nulo en caso de no haber ingresado previamente a un
	 *            proyecto en particular.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro de ítems que debe ser
	 *            aplicado.
	 * @param index
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return la colección de dtos de los ítems del sistema empezando por el
	 *         índice recibido y devolviendo únicamente la cantidad especificada
	 *         de elementos.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<ItemDTO> getItems(UserDTO anUserDTO,
			ProjectDTO aProjectDTO, FilterDTO aFilterDTO, int index, int count,
			String aPropertyName, String anOrdering) throws Exception;

	/**
	 * Obtiene la cantidad de ítems para el listado.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario. Este parámetro puede ser
	 *            nulo, en cuyo caso solamente se contarán los ítems
	 *            pertenecientes a los proyectos públicos. Si no es nulo
	 *            entonces también se considerarán los ítems pertenecientes a
	 *            los proyectos en los cuales está asociado el usuario.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto actual. Este parámetro
	 *            podría ser nulo en caso de no haber ingresado previamente a un
	 *            proyecto en particular.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que se debe aplicar a los
	 *            items.
	 * @return el número de ítems.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public int getItemsCount(UserDTO userDTO, ProjectDTO aProjectDTO,
			FilterDTO aFilterDTO) throws Exception;

	/**
	 * Crea un filtro a partir de la información seleccionada por el usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto del usuario para el cual se está creando este
	 *            filtro. Este parámetro puede ser nulo en caso de que no exista
	 *            usuario en la sesión.
	 * @param aFilterName
	 *            es el nombre para el nuevo filtro.
	 * @param someFilterComponentByProjects
	 *            es el componente que filtra los ítems a partir de sus
	 *            proyectos.
	 * @param itemStates
	 *            define los estados del ítems por los cuales se debe filtrar.
	 * @param responsibles
	 *            establece por cuales responsables se debe filtrar la colección
	 *            de ítems.
	 * @param aFilterComponentByItemId
	 *            es el componente que filtra a partir del id del ítem.
	 * @param someItemTypes
	 *            es una colección que contiene los títulos de los tipos por los
	 *            cuales hay que filtrar los ítems.
	 * @param someNodeDescriptions
	 *            es una colección que contiene los títulos de las descripciones
	 *            de nodo por los cuales hay que filtrar los ítems.
	 * @param negateResponsibles
	 *            establece si se debe negar la condición de filtro por
	 *            responsables.
	 * @param negateItemStates
	 *            establece si se debe negar la condición de filtro por estado
	 *            de los ítems.
	 * @param negateItemTypes
	 *            establece si se debe negar la condición de filtro por tipo de
	 *            los ítems.
	 * @param negateProjects
	 *            establece si se debe negar la condición de filtro por proyecto
	 *            de los ítems.
	 * @param negateNodes
	 *            establece si se debe negar la condición de filtro por nodo de
	 *            los ítems.
	 * @param aText
	 *            es el texto por el cual se debe filtrar los ítems.
	 * @return un dto que representa al nuevo filtro.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public FilterDTO createFilter(
			UserDTO anUserDTO,
			String aFilterName,
			Collection<FilterComponentByProjectDTO> someFilterComponentByProjects,
			String aFilterComponentByItemId,
			Collection<ItemStateEnum> itemStates,
			Collection<AbstractUserDTO> responsibles,
			Collection<ItemTypeDTO> someItemTypes,
			Collection<WorkflowNodeDescriptionDTO> someNodeDescriptions,
			boolean negateResponsibles, boolean negateItemStates,
			boolean negateItemTypes, boolean negateProjects,
			boolean negateNodes, String aText) throws Exception;

	/**
	 * Edita un filtro cuyo oid se recibe.
	 * 
	 * 
	 * @param aFilterOid
	 *            es el oid del filtro que se debe editar.
	 * @param someFilterComponentByProjects
	 *            es el componente que filtra los ítems a partir de sus
	 *            proyectos.
	 * @param itemStates
	 *            define los estados del ítems por los cuales se debe filtrar.
	 * @param responsibles
	 *            establece por cuales responsables se debe filtrar la colección
	 *            de ítems.
	 * @param someItemTypes
	 *            es una colección que contiene los títulos de los tipos por los
	 *            cuales hay que filtrar los ítems.
	 * @param someNodeDescriptions
	 *            es una colección que contiene los títulos de las descripciones
	 *            de nodo por los cuales hay que filtrar los ítems.
	 * @param negateResponsibles
	 *            establece si se debe negar la condición de filtro por
	 *            responsables.
	 * @param negateItemStates
	 *            establece si se debe negar la condición de filtro por estado
	 *            de los ítems.
	 * @param negateItemTypes
	 *            establece si se debe negar la condición de filtro por tipo de
	 *            los ítems.
	 * @param negateProjects
	 *            establece si se debe negar la condición de filtro por proyecto
	 *            de los ítems.
	 * @param negateNodes
	 *            establece si se debe negar la condición de filtro por nodo de
	 *            los ítems.
	 * @param aText
	 *            es el texto por el cual se debe filtrar los ítems.
	 * @return un dto que representa al nuevo filtro.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public FilterDTO updateFilter(
			String aFilterOid,
			Collection<FilterComponentByProjectDTO> someFilterComponentByProjects,
			Collection<ItemStateEnum> itemStates,
			Collection<AbstractUserDTO> responsibles,
			Collection<ItemTypeDTO> someItemTypes,
			Collection<WorkflowNodeDescriptionDTO> someNodeDescriptions,
			boolean negateResponsibles, boolean negateItemStates,
			boolean negateItemTypes, boolean negateProjects,
			boolean negateNodes, String aText) throws Exception;

	/**
	 * Obtiene una colección que contiene los filtros favoritos de un usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se deben
	 *            recuperar los filtros favoritos.
	 * @return una colección con DTOs que representan los filtros favoritos del
	 *         usuario.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public Collection<FilterDTO> findFavoritiesItemsFiltersOfUser(
			UserDTO anUserDTO) throws Exception;

	/**
	 * Almacena un nuevo filtro para el usuario recibido.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario para el cual se debe
	 *            almacenar el filtro.
	 * @param filterDTO
	 *            es el DTO que representa al nuevo filtro a guardar.
	 * 
	 * @return el DTO del nuevo filtro creado.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 * 
	 */
	public FilterDTO saveItemsFilterForUser(UserDTO userDTO, FilterDTO filterDTO)
			throws Exception;

	/**
	 * Finder.
	 * 
	 * @param filterId
	 *            es el id del filtro que se está buscando.
	 * @return un dto que representa al filtro que se estaba buscando.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public FilterDTO findItemFilterById(String filterId) throws Exception;

	/**
	 * Verifica si existe un filtro con el nombre dado para el usuario recibido.
	 * 
	 * @param aName
	 *            es el nombre del filtro que se debe buscar.
	 * @param anUserDTO
	 *            es un dto que representa al usuario en el que se debe buscar
	 *            el filtro.
	 * @return true en caso de que el usuario ya tenga un filtro con el nombre
	 *         dado, false en caso contrario.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public boolean containsFilterWithNameForUser(String aName, UserDTO anUserDTO)
			throws Exception;

	/**
	 * Obtiene una colección que contiene los filtros de un usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se deben
	 *            recuperar los filtros.
	 * @return una colección con DTOs que representan los filtros del usuario.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public Collection<FilterDTO> findItemsFiltersOfUser(UserDTO anUserDTO)
			throws Exception;

	/**
	 * Actualiza un filtro.
	 * 
	 * @param filterDTO
	 *            es el DTO que representa al nuevo filtro a guardar.
	 * @param anUserDTO
	 *            es el DTO que representa al usuario.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public void updateItemsFilter(FilterDTO filterDTO, UserDTO anUserDTO)
			throws Exception;

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el identificador del item que debe recuperarse.
	 * @return el ítem identificado por el oid recibido.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public ItemDTO findItemByOid(String anOid) throws Exception;

	/**
	 * Actualiza los datos básicos de un ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa la item que se está editando.
	 * @param aTitle
	 *            es el nuevo título de este ítem.
	 * @param aDescription
	 *            es la nueva descripción de este ítem.
	 * @param aPriorityDTO
	 *            es la nueva prioridad de este ítem.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de ítem seleccionado.
	 * 
	 * @return una nueva versión del dto que representa al ítem.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public ItemDTO editItem(ItemDTO anItemDTO, String aTitle,
			String aDescription, PriorityDTO aPriorityDTO,
			ItemTypeDTO anItemTypeDTO) throws Exception;

	/**
	 * Verifica si un usuario puede ser seguidor de un ítem.
	 * 
	 * @param anAbstractUserDTO
	 *            es el dto que representa al usuario o grupo de usuarios.
	 * @param anItemDTO
	 *            es el dto que representa al ítem.
	 * @return true en caso de que el usuario no sea ya un observador de este
	 *         ítem.
	 * 
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de algún error al
	 *             ejecutar este servicio.
	 */
	public boolean isUserObserverOfItem(AbstractUserDTO anAbstractUserDTO,
			ItemDTO anItemDTO) throws Exception;

	/**
	 * Agrega al usuario o grupo de usuarios como observador del ítem.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario o grupo de usuarios
	 *            observadores del ítem.
	 * @param anItemDTO
	 *            es el ítem observado.
	 * @throws Exception
	 */
	public void addObserverToItem(AbstractUserDTO userDTO, ItemDTO anItemDTO)
			throws Exception;

	/**
	 * El usuario ha decidido tomar este ítem bajo su responsabilidad. <br>
	 * Este mensaje se utiliza cuando un grupo de usuarios es responsable de un
	 * ítem y uno de los usuarios de dicho grupo ha tomado la responsabilidad
	 * del ítem.
	 * 
	 * @param userDTO
	 *            es el DTO que representa al usuario que será el nuevo
	 *            responsable.
	 * @param anItemDTO
	 *            es el DTO que representa al ítem en cuestión.
	 * 
	 * @return un dto que representa al ítem.
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse a raíz de la
	 *             ejecución de este método.
	 */
	public ItemDTO takeItem(UserDTO userDTO, ItemDTO anItemDTO)
			throws Exception;

	/**
	 * Retorna los comentarios de un ítem.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al ítem.
	 * @return una colección que contiene los dtos de los comentarios.
	 * 
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse a raíz de la
	 *             ejecución de este método.
	 */
	public Collection<CommentDTO> getCommentsOfItem(ItemDTO itemDTO)
			throws Exception;

	/**
	 * Agrega un nuevo comentario al ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem al que se debe agregar el
	 *            nuevo comentario.
	 * @param aCommentText
	 *            es el texto del nuevo comentario.
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * 
	 * @return el dto del ítem modificado.
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse por la ejecución
	 *             de este servicio.
	 */
	public ItemDTO addCommentToItem(ItemDTO anItemDTO, String aCommentText,
			UserDTO anUserDTO) throws Exception;

	/**
	 * Retorna DTOs de todos los tipos de ítems de un proyecto dado.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto.
	 * @param aPropertyName
	 *            establece por cual propiedad se debería devolver ordenado el
	 *            resultado.
	 * @param anOrdering
	 *            establece si el orden es ascendente o no.
	 * @return una colección que contiene los dtos de todos los tipos de ítems
	 *         del proyecto.
	 * 
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse por la ejecución
	 *             de este servicio.
	 */
	public Collection<ItemTypeDTO> findItemTypesOfProject(
			ProjectDTO aProjectDTO, String aPropertyName, String anOrdering)
			throws Exception;

	/**
	 * Getter.
	 * 
	 * @param index
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que debe aplicarse al resultado.
	 * @param aColumnName
	 *            es el nombre de la propiedad por la que debe ordenarse el
	 *            resultado.
	 * @return la colección de dtos de los tipos de ítems del sistema empezando
	 *         por el índice recibido y devolviendo únicamente la cantidad
	 *         especificada de elementos.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<ItemTypeDTO> getItemTypes(int index, int count,
			String aColumnName, String anOrdering) throws Exception;

	/**
	 * Getter.
	 * 
	 * @return la cantidad de ítems de sistema que existen.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public int getItemTypesCount() throws Exception;

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todos los tipos de ítems disponibles.
	 */
	public Collection<ItemTypeDTO> getAllItemTypes() throws Exception;

	/**
	 * Agrega un nuevo tipo de ítem al sistema.
	 * 
	 * @param aTitle
	 *            es el título del nuevo tipo de ítem.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void addItemType(String aTitle) throws Exception;

	/**
	 * Verifica si existe un tipo de ítem con un título dado.
	 * 
	 * @param aTitle
	 *            es el título que se está buscando.
	 * @return true en caso de que exista el tipo de ítem; false en caso
	 *         contrario.
	 */
	public boolean existsItemTypeWithTitle(String aTitle);

	/**
	 * Verifica si existe un tipo de ítem con un título dado, pero dentro de un
	 * proyecto determinado.
	 * 
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto en el cual se debe buscar
	 *            el tipo de ítem.
	 * @param aTitle
	 *            es el título que se está buscando.
	 * @return true en caso de que exista el tipo de ítem; false en caso
	 *         contrario.
	 */
	public boolean existsItemTypeWithTitleInProject(ProjectDTO aProjectDTO,
			String aTitle);

	/**
	 * Finder.
	 * 
	 * @param anItemTypeOid
	 *            es el oid del tipo de ítem que se está tratando de recuperar.
	 * @return un dto que representa al tipo de ítem recuperado.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public ItemTypeDTO findItemTypeByOid(String anItemTypeOid) throws Exception;

	/**
	 * Edita los datos de un tipo de ítem. Esta edición solamente afecta a la
	 * instancia que se está editando, no así a todas las copias de la misma.
	 * 
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de ítem que se debe editar.
	 * @param aTitle
	 *            es el nuevo título del tipo de ítem.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void editItemType(ItemTypeDTO anItemTypeDTO, String aTitle)
			throws Exception;

	/**
	 * Borra los tipos de ítems cuyos identificadores se han recibido.
	 * 
	 * @param selectedItemTypes
	 *            es una colección que contiene los identificadores de los tipos
	 *            de ítems que deben ser eliminados.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deleteItemTypes(Collection<String> selectedItemTypes)
			throws Exception;

	/**
	 * Elimina de un proyecto un conjunto de ítems.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario que está tratando de
	 *            eliminar los ítems.
	 * 
	 * @param selectedItems
	 *            es una colección que contiene los identificadores de los ítems
	 *            que se deben eliminar.
	 * 
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deleteItems(UserDTO userDTO, Collection<String> selectedItems)
			throws Exception;

	/**
	 * Elimina un filtro de ítems de un usuario particular.
	 * 
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que debe ser eliminado.
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * @return el dto del usuario actualizado.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public UserDTO deleteItemsFilterOfUser(FilterDTO aFilterDTO,
			UserDTO anUserDTO) throws Exception;

	/**
	 * Agrega un nuevo tipo de ítem a un proyecto dado.
	 * 
	 * @param aTitle
	 *            es el título del nuevo tipo de ítem.
	 * @param projectDTO
	 *            es el dto que representa al proyecto que se está editando.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void addItemTypeToProject(String aTitle, ProjectDTO projectDTO)
			throws Exception;

	/**
	 * Elimina un tipo de ítem de un proyecto dado.
	 * 
	 * @param projectDTO
	 *            es el dto que representa la proyecto que se está editando.
	 * @param someItemTypes
	 *            es una colección que contiene los tipos de ítems
	 *            seleccionados.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deleteItemTypeOfProject(ProjectDTO projectDTO,
			Collection<String> someItemTypes) throws Exception;

	/**
	 * Reasigna un ítem a un nuevo responsable.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al ítem.
	 * @param nextNodeDTO
	 *            es el dto que representa al nuevo estado al que se está
	 *            pasando el ítem.
	 * @param nextResponsibleDTO
	 *            es el dto que representa al nuevo responsable.
	 * @return el dto que representa al ítem.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public ItemDTO assignItemToUser(ItemDTO itemDTO,
			WorkflowNodeDescriptionDTO nextNodeDTO,
			AbstractUserDTO nextResponsibleDTO) throws Exception;

	/**
	 * Adjunta un nuevo archivo al ítem representado por el dto. <BR>
	 * La subida concreta del archivo se debe realizar por otro medio, este
	 * servicio no realiza esta función.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem al cual se debe adjuntar un
	 *            archivo.
	 * @param aFilename
	 *            es el nombre del archivo que se ha adjuntado.
	 * 
	 * @return el dto que representa al ítem.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public ItemDTO attachFileToItem(ItemDTO anItemDTO, String aFilename)
			throws Exception;

	/**
	 * Obtiene la colección de archivos adjuntos de un ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem para el cual se debe
	 *            recuperar la colección de archivos adjuntos.
	 * @return una colección que contiene los DTOs de los archivos adjuntos.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<ItemFileDTO> getAttachedFilesOfItem(ItemDTO anItemDTO)
			throws Exception;

	/**
	 * Elimina los archivos adjuntos del ítem recibido.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al ítem que actualmente tiene los
	 *            archivos.
	 * @param someFilesOids
	 *            es una colección que contiene los identificadores de los
	 *            archivos adjuntos que deben eliminarse.
	 * 
	 * @return el dto que representa al ítem.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public ItemDTO detachFilesFromItem(ItemDTO itemDTO,
			Collection<String> someFilesOids) throws Exception;

	/**
	 * Recupera la cantidad de tipos de ítems que tiene un proyecto dado.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto que se está consultando.
	 * @return un entero que representa la cantidad de tipos de ítems.
	 */
	public int getItemTypesCountOfProject(String anOid);

	/**
	 * Recupera todos los tipos de ítems utilizados por los proyectos.
	 * 
	 * @return una colección que contiene todos los tipos de ítems utilizados en
	 *         los proyectos.
	 */
	public Collection<ItemTypeDTO> getAllItemTypesOfProjects();

	/**
	 * Recupera los observadores de un ítem dado.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa el ítem para el cual se deben buscar
	 *            los observadores.
	 * @return una colección que contiene los observadores del ítem.
	 * @throws ItemUnknownException
	 *             esta excepción se puede lanzar en caso de que no exista el
	 *             ítem.
	 */
	public Collection<UserDTO> getObserversOfItem(ItemDTO anItemDTO)
			throws ItemUnknownException;

	/**
	 * Actualiza el valor de las propiedades adicionales del ítem representado
	 * por el DTO recibido.<br>
	 * Si el ítem ya cuenta con las propiedades, éstas serán sobreescritas.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa al ítem.
	 * @param someProperties
	 *            es un diccionario que contiene los valores de las propiedades
	 *            adicionales.
	 * @return un DTO que representa al ítem editando.
	 * @throws Exception
	 *             esta excepción se puede lanzar al ejecutar este servicio
	 *             debido a que no se encuentra el ítem o que no se esté
	 *             editando la última versión del mismo.
	 */
	public ItemDTO setAdditionalPropertiesToItem(ItemDTO anItemDTO,
			Map<String, String> someProperties) throws Exception;

	/**
	 * Finder.
	 * 
	 * @param someSelectedItems
	 *            es una colección que contiene los oids de los ítems que se
	 *            deben recuperar.
	 * @return una colección que contiene los DTOs de los ítems cuyos
	 *         identificadores se ha recibido.
	 * @throws Exception
	 *             es una excepción que se puede levantar en caso de no
	 *             encontrar algún ítem.
	 */
	public Collection<ItemDTO> findItemsByOid(
			Collection<String> someSelectedItems) throws Exception;

}
