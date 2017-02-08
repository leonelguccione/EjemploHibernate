/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deber�n implementar.<br>
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
 * Esta interface define el protocolo de los servicios relacionados con los �tem
 * administrados por el sistema que deber�n ser implementados por clases
 * concretas.<br>
 * Todo servicio en ning�n momento debe devolver un objeto de dominio, sino que
 * siempre devolver� DTOs.<br>
 * Por �ltimo, los servicios no contienen l�gica de negocios propiamente dicha
 * sino que son encargados de ejecutar dicha l�gica presente en el modelo de
 * dominio.<br>
 * Todos los m�todos de esta interface declaran el lanzamiento de excepciones.
 * Algunas ser�n excepciones de negocio y otras inesperadas (como la ca�da de la
 * conexi�n a la base de datos). La declaraci�n del lanzamiento tiene como
 * objetivo lograr que el cliente tenga que manejar excepciones cada vez que
 * invoque a un servicio. Si bien aqu� se declaran excepciones generales, en la
 * documentaci�n de cada m�todo tambi�n figura la excepci�n particular de
 * negocio que podr�a ser lanzada por la implementaci�n en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y adem�s en forma
 * particular alguna excepci�n de negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface ItemsServiceBI {

	/**
	 * Agrega un nuevo �tem al sistema.
	 * 
	 * @param aProjectDTO
	 *            es el proyecto en el cual se est� agregando el �tem.
	 * @param aTitle
	 *            es el t�tulo del nuevo �tem.
	 * @param aDescription
	 *            es un texto descriptivo del �tem.
	 * @param aDate
	 *            es la fecha de creaci�n del �tem.
	 * @param anUserDTO
	 *            es el usuario que ha creado este �tem.
	 * @param aPriorityDTO
	 *            es la prioridad del �tem.
	 * @param aState
	 *            es el estado del item. Puede ser CREADO o ABIERTO.
	 * @param responsible
	 *            es el dto que representa al usuario o grupo de usuarios que
	 *            ser� responsable del nuevo �tem.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo del nuevo �tem.
	 * @param someItemIds
	 *            es una colecci�n que contiene los identificadores de los �tems
	 *            que se deben unificar al dar de alta este nuevo �tem.
	 * @param aComment
	 *            es el comentario que se debe agregar a cada �tem unificado
	 *            para dejar asentado que se lo ha unificado.
	 * @param somePropertyDescriptions
	 *            es un diccionario que contiene los valores asignados a cada
	 *            una de las propiedades adicionales del nuevo �tem.
	 * 
	 * @return un dto que representa al nuevo �tem recientemente creado.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de que ocurra un
	 *             error con el alta del �tem.
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
	 *            es el dto que representa al usuario que est� solicitando el
	 *            listado de los �tems. En caso de que sea un usuario an�nimo
	 *            este par�metro ser� nulo y por lo tanto se listar�n solamente
	 *            los �tems pertenecientes a proyectos p�blicos. En caso de que
	 *            no sea nulo entonces se listar�n adem�s los �tems
	 *            pertencientes a los proyectos en los cuales el usuario est�
	 *            asignado.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto actual. Este par�metro
	 *            podr�a ser nulo en caso de no haber ingresado previamente a un
	 *            proyecto en particular.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro de �tems que debe ser
	 *            aplicado.
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return la colecci�n de dtos de los �tems del sistema empezando por el
	 *         �ndice recibido y devolviendo �nicamente la cantidad especificada
	 *         de elementos.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<ItemDTO> getItems(UserDTO anUserDTO,
			ProjectDTO aProjectDTO, FilterDTO aFilterDTO, int index, int count,
			String aPropertyName, String anOrdering) throws Exception;

	/**
	 * Obtiene la cantidad de �tems para el listado.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario. Este par�metro puede ser
	 *            nulo, en cuyo caso solamente se contar�n los �tems
	 *            pertenecientes a los proyectos p�blicos. Si no es nulo
	 *            entonces tambi�n se considerar�n los �tems pertenecientes a
	 *            los proyectos en los cuales est� asociado el usuario.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto actual. Este par�metro
	 *            podr�a ser nulo en caso de no haber ingresado previamente a un
	 *            proyecto en particular.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que se debe aplicar a los
	 *            items.
	 * @return el n�mero de �tems.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public int getItemsCount(UserDTO userDTO, ProjectDTO aProjectDTO,
			FilterDTO aFilterDTO) throws Exception;

	/**
	 * Crea un filtro a partir de la informaci�n seleccionada por el usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto del usuario para el cual se est� creando este
	 *            filtro. Este par�metro puede ser nulo en caso de que no exista
	 *            usuario en la sesi�n.
	 * @param aFilterName
	 *            es el nombre para el nuevo filtro.
	 * @param someFilterComponentByProjects
	 *            es el componente que filtra los �tems a partir de sus
	 *            proyectos.
	 * @param itemStates
	 *            define los estados del �tems por los cuales se debe filtrar.
	 * @param responsibles
	 *            establece por cuales responsables se debe filtrar la colecci�n
	 *            de �tems.
	 * @param aFilterComponentByItemId
	 *            es el componente que filtra a partir del id del �tem.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los t�tulos de los tipos por los
	 *            cuales hay que filtrar los �tems.
	 * @param someNodeDescriptions
	 *            es una colecci�n que contiene los t�tulos de las descripciones
	 *            de nodo por los cuales hay que filtrar los �tems.
	 * @param negateResponsibles
	 *            establece si se debe negar la condici�n de filtro por
	 *            responsables.
	 * @param negateItemStates
	 *            establece si se debe negar la condici�n de filtro por estado
	 *            de los �tems.
	 * @param negateItemTypes
	 *            establece si se debe negar la condici�n de filtro por tipo de
	 *            los �tems.
	 * @param negateProjects
	 *            establece si se debe negar la condici�n de filtro por proyecto
	 *            de los �tems.
	 * @param negateNodes
	 *            establece si se debe negar la condici�n de filtro por nodo de
	 *            los �tems.
	 * @param aText
	 *            es el texto por el cual se debe filtrar los �tems.
	 * @return un dto que representa al nuevo filtro.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
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
	 *            es el componente que filtra los �tems a partir de sus
	 *            proyectos.
	 * @param itemStates
	 *            define los estados del �tems por los cuales se debe filtrar.
	 * @param responsibles
	 *            establece por cuales responsables se debe filtrar la colecci�n
	 *            de �tems.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los t�tulos de los tipos por los
	 *            cuales hay que filtrar los �tems.
	 * @param someNodeDescriptions
	 *            es una colecci�n que contiene los t�tulos de las descripciones
	 *            de nodo por los cuales hay que filtrar los �tems.
	 * @param negateResponsibles
	 *            establece si se debe negar la condici�n de filtro por
	 *            responsables.
	 * @param negateItemStates
	 *            establece si se debe negar la condici�n de filtro por estado
	 *            de los �tems.
	 * @param negateItemTypes
	 *            establece si se debe negar la condici�n de filtro por tipo de
	 *            los �tems.
	 * @param negateProjects
	 *            establece si se debe negar la condici�n de filtro por proyecto
	 *            de los �tems.
	 * @param negateNodes
	 *            establece si se debe negar la condici�n de filtro por nodo de
	 *            los �tems.
	 * @param aText
	 *            es el texto por el cual se debe filtrar los �tems.
	 * @return un dto que representa al nuevo filtro.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
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
	 * Obtiene una colecci�n que contiene los filtros favoritos de un usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se deben
	 *            recuperar los filtros favoritos.
	 * @return una colecci�n con DTOs que representan los filtros favoritos del
	 *         usuario.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
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
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 * 
	 */
	public FilterDTO saveItemsFilterForUser(UserDTO userDTO, FilterDTO filterDTO)
			throws Exception;

	/**
	 * Finder.
	 * 
	 * @param filterId
	 *            es el id del filtro que se est� buscando.
	 * @return un dto que representa al filtro que se estaba buscando.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
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
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public boolean containsFilterWithNameForUser(String aName, UserDTO anUserDTO)
			throws Exception;

	/**
	 * Obtiene una colecci�n que contiene los filtros de un usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se deben
	 *            recuperar los filtros.
	 * @return una colecci�n con DTOs que representan los filtros del usuario.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
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
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public void updateItemsFilter(FilterDTO filterDTO, UserDTO anUserDTO)
			throws Exception;

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el identificador del item que debe recuperarse.
	 * @return el �tem identificado por el oid recibido.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public ItemDTO findItemByOid(String anOid) throws Exception;

	/**
	 * Actualiza los datos b�sicos de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa la item que se est� editando.
	 * @param aTitle
	 *            es el nuevo t�tulo de este �tem.
	 * @param aDescription
	 *            es la nueva descripci�n de este �tem.
	 * @param aPriorityDTO
	 *            es la nueva prioridad de este �tem.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de �tem seleccionado.
	 * 
	 * @return una nueva versi�n del dto que representa al �tem.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public ItemDTO editItem(ItemDTO anItemDTO, String aTitle,
			String aDescription, PriorityDTO aPriorityDTO,
			ItemTypeDTO anItemTypeDTO) throws Exception;

	/**
	 * Verifica si un usuario puede ser seguidor de un �tem.
	 * 
	 * @param anAbstractUserDTO
	 *            es el dto que representa al usuario o grupo de usuarios.
	 * @param anItemDTO
	 *            es el dto que representa al �tem.
	 * @return true en caso de que el usuario no sea ya un observador de este
	 *         �tem.
	 * 
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public boolean isUserObserverOfItem(AbstractUserDTO anAbstractUserDTO,
			ItemDTO anItemDTO) throws Exception;

	/**
	 * Agrega al usuario o grupo de usuarios como observador del �tem.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario o grupo de usuarios
	 *            observadores del �tem.
	 * @param anItemDTO
	 *            es el �tem observado.
	 * @throws Exception
	 */
	public void addObserverToItem(AbstractUserDTO userDTO, ItemDTO anItemDTO)
			throws Exception;

	/**
	 * El usuario ha decidido tomar este �tem bajo su responsabilidad. <br>
	 * Este mensaje se utiliza cuando un grupo de usuarios es responsable de un
	 * �tem y uno de los usuarios de dicho grupo ha tomado la responsabilidad
	 * del �tem.
	 * 
	 * @param userDTO
	 *            es el DTO que representa al usuario que ser� el nuevo
	 *            responsable.
	 * @param anItemDTO
	 *            es el DTO que representa al �tem en cuesti�n.
	 * 
	 * @return un dto que representa al �tem.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este m�todo.
	 */
	public ItemDTO takeItem(UserDTO userDTO, ItemDTO anItemDTO)
			throws Exception;

	/**
	 * Retorna los comentarios de un �tem.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al �tem.
	 * @return una colecci�n que contiene los dtos de los comentarios.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este m�todo.
	 */
	public Collection<CommentDTO> getCommentsOfItem(ItemDTO itemDTO)
			throws Exception;

	/**
	 * Agrega un nuevo comentario al �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem al que se debe agregar el
	 *            nuevo comentario.
	 * @param aCommentText
	 *            es el texto del nuevo comentario.
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * 
	 * @return el dto del �tem modificado.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse por la ejecuci�n
	 *             de este servicio.
	 */
	public ItemDTO addCommentToItem(ItemDTO anItemDTO, String aCommentText,
			UserDTO anUserDTO) throws Exception;

	/**
	 * Retorna DTOs de todos los tipos de �tems de un proyecto dado.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto.
	 * @param aPropertyName
	 *            establece por cual propiedad se deber�a devolver ordenado el
	 *            resultado.
	 * @param anOrdering
	 *            establece si el orden es ascendente o no.
	 * @return una colecci�n que contiene los dtos de todos los tipos de �tems
	 *         del proyecto.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse por la ejecuci�n
	 *             de este servicio.
	 */
	public Collection<ItemTypeDTO> findItemTypesOfProject(
			ProjectDTO aProjectDTO, String aPropertyName, String anOrdering)
			throws Exception;

	/**
	 * Getter.
	 * 
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que debe aplicarse al resultado.
	 * @param aColumnName
	 *            es el nombre de la propiedad por la que debe ordenarse el
	 *            resultado.
	 * @return la colecci�n de dtos de los tipos de �tems del sistema empezando
	 *         por el �ndice recibido y devolviendo �nicamente la cantidad
	 *         especificada de elementos.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<ItemTypeDTO> getItemTypes(int index, int count,
			String aColumnName, String anOrdering) throws Exception;

	/**
	 * Getter.
	 * 
	 * @return la cantidad de �tems de sistema que existen.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public int getItemTypesCount() throws Exception;

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems disponibles.
	 */
	public Collection<ItemTypeDTO> getAllItemTypes() throws Exception;

	/**
	 * Agrega un nuevo tipo de �tem al sistema.
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo tipo de �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void addItemType(String aTitle) throws Exception;

	/**
	 * Verifica si existe un tipo de �tem con un t�tulo dado.
	 * 
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que exista el tipo de �tem; false en caso
	 *         contrario.
	 */
	public boolean existsItemTypeWithTitle(String aTitle);

	/**
	 * Verifica si existe un tipo de �tem con un t�tulo dado, pero dentro de un
	 * proyecto determinado.
	 * 
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto en el cual se debe buscar
	 *            el tipo de �tem.
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que exista el tipo de �tem; false en caso
	 *         contrario.
	 */
	public boolean existsItemTypeWithTitleInProject(ProjectDTO aProjectDTO,
			String aTitle);

	/**
	 * Finder.
	 * 
	 * @param anItemTypeOid
	 *            es el oid del tipo de �tem que se est� tratando de recuperar.
	 * @return un dto que representa al tipo de �tem recuperado.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemTypeDTO findItemTypeByOid(String anItemTypeOid) throws Exception;

	/**
	 * Edita los datos de un tipo de �tem. Esta edici�n solamente afecta a la
	 * instancia que se est� editando, no as� a todas las copias de la misma.
	 * 
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de �tem que se debe editar.
	 * @param aTitle
	 *            es el nuevo t�tulo del tipo de �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void editItemType(ItemTypeDTO anItemTypeDTO, String aTitle)
			throws Exception;

	/**
	 * Borra los tipos de �tems cuyos identificadores se han recibido.
	 * 
	 * @param selectedItemTypes
	 *            es una colecci�n que contiene los identificadores de los tipos
	 *            de �tems que deben ser eliminados.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteItemTypes(Collection<String> selectedItemTypes)
			throws Exception;

	/**
	 * Elimina de un proyecto un conjunto de �tems.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario que est� tratando de
	 *            eliminar los �tems.
	 * 
	 * @param selectedItems
	 *            es una colecci�n que contiene los identificadores de los �tems
	 *            que se deben eliminar.
	 * 
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteItems(UserDTO userDTO, Collection<String> selectedItems)
			throws Exception;

	/**
	 * Elimina un filtro de �tems de un usuario particular.
	 * 
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que debe ser eliminado.
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * @return el dto del usuario actualizado.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public UserDTO deleteItemsFilterOfUser(FilterDTO aFilterDTO,
			UserDTO anUserDTO) throws Exception;

	/**
	 * Agrega un nuevo tipo de �tem a un proyecto dado.
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo tipo de �tem.
	 * @param projectDTO
	 *            es el dto que representa al proyecto que se est� editando.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void addItemTypeToProject(String aTitle, ProjectDTO projectDTO)
			throws Exception;

	/**
	 * Elimina un tipo de �tem de un proyecto dado.
	 * 
	 * @param projectDTO
	 *            es el dto que representa la proyecto que se est� editando.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los tipos de �tems
	 *            seleccionados.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteItemTypeOfProject(ProjectDTO projectDTO,
			Collection<String> someItemTypes) throws Exception;

	/**
	 * Reasigna un �tem a un nuevo responsable.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al �tem.
	 * @param nextNodeDTO
	 *            es el dto que representa al nuevo estado al que se est�
	 *            pasando el �tem.
	 * @param nextResponsibleDTO
	 *            es el dto que representa al nuevo responsable.
	 * @return el dto que representa al �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemDTO assignItemToUser(ItemDTO itemDTO,
			WorkflowNodeDescriptionDTO nextNodeDTO,
			AbstractUserDTO nextResponsibleDTO) throws Exception;

	/**
	 * Adjunta un nuevo archivo al �tem representado por el dto. <BR>
	 * La subida concreta del archivo se debe realizar por otro medio, este
	 * servicio no realiza esta funci�n.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem al cual se debe adjuntar un
	 *            archivo.
	 * @param aFilename
	 *            es el nombre del archivo que se ha adjuntado.
	 * 
	 * @return el dto que representa al �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemDTO attachFileToItem(ItemDTO anItemDTO, String aFilename)
			throws Exception;

	/**
	 * Obtiene la colecci�n de archivos adjuntos de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem para el cual se debe
	 *            recuperar la colecci�n de archivos adjuntos.
	 * @return una colecci�n que contiene los DTOs de los archivos adjuntos.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<ItemFileDTO> getAttachedFilesOfItem(ItemDTO anItemDTO)
			throws Exception;

	/**
	 * Elimina los archivos adjuntos del �tem recibido.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al �tem que actualmente tiene los
	 *            archivos.
	 * @param someFilesOids
	 *            es una colecci�n que contiene los identificadores de los
	 *            archivos adjuntos que deben eliminarse.
	 * 
	 * @return el dto que representa al �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemDTO detachFilesFromItem(ItemDTO itemDTO,
			Collection<String> someFilesOids) throws Exception;

	/**
	 * Recupera la cantidad de tipos de �tems que tiene un proyecto dado.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto que se est� consultando.
	 * @return un entero que representa la cantidad de tipos de �tems.
	 */
	public int getItemTypesCountOfProject(String anOid);

	/**
	 * Recupera todos los tipos de �tems utilizados por los proyectos.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems utilizados en
	 *         los proyectos.
	 */
	public Collection<ItemTypeDTO> getAllItemTypesOfProjects();

	/**
	 * Recupera los observadores de un �tem dado.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa el �tem para el cual se deben buscar
	 *            los observadores.
	 * @return una colecci�n que contiene los observadores del �tem.
	 * @throws ItemUnknownException
	 *             esta excepci�n se puede lanzar en caso de que no exista el
	 *             �tem.
	 */
	public Collection<UserDTO> getObserversOfItem(ItemDTO anItemDTO)
			throws ItemUnknownException;

	/**
	 * Actualiza el valor de las propiedades adicionales del �tem representado
	 * por el DTO recibido.<br>
	 * Si el �tem ya cuenta con las propiedades, �stas ser�n sobreescritas.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa al �tem.
	 * @param someProperties
	 *            es un diccionario que contiene los valores de las propiedades
	 *            adicionales.
	 * @return un DTO que representa al �tem editando.
	 * @throws Exception
	 *             esta excepci�n se puede lanzar al ejecutar este servicio
	 *             debido a que no se encuentra el �tem o que no se est�
	 *             editando la �ltima versi�n del mismo.
	 */
	public ItemDTO setAdditionalPropertiesToItem(ItemDTO anItemDTO,
			Map<String, String> someProperties) throws Exception;

	/**
	 * Finder.
	 * 
	 * @param someSelectedItems
	 *            es una colecci�n que contiene los oids de los �tems que se
	 *            deben recuperar.
	 * @return una colecci�n que contiene los DTOs de los �tems cuyos
	 *         identificadores se ha recibido.
	 * @throws Exception
	 *             es una excepci�n que se puede levantar en caso de no
	 *             encontrar alg�n �tem.
	 */
	public Collection<ItemDTO> findItemsByOid(
			Collection<String> someSelectedItems) throws Exception;

}
