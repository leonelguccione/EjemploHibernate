/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deberán respetar las implementaciones del patrón de diseño Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.Item;
import zinbig.item.model.ItemFile;
import zinbig.item.model.ItemType;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.FilterUnknownException;
import zinbig.item.model.exceptions.ItemTypeUnknownException;
import zinbig.item.model.exceptions.ItemUnknownException;
import zinbig.item.model.filters.Filter;
import zinbig.item.model.projects.Project;
import zinbig.item.model.users.User;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Esta interface establece el protocolo estándar que deberá ser respetado por
 * todas las clases que implementen el patrón de diseño Repository para acceder
 * eficientemente a las instancias de la clase Ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface ItemsRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Recupera una colección de ítems que esté contenida entre los índices
	 * recibidos y que pertenecen a los proyectos asociados con el usuario
	 * además de los ítems públicos. <br>
	 * 
	 * @param anUser
	 *            es el usuario para el cual se deben listar los ítems.
	 * @param aProject
	 *            es el proyecto actual. Este parámetro puede ser nulo.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar a los ítems.
	 * @param beginIndex
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return una colección de ítems del sistema.
	 */
	public Collection<Item> findItems(User anUser, Project aProject,
			String aFilterString, int beginIndex, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Retorna la cantidad de ítems para el listado pedido por el usuario cuyo
	 * dto se ha recibido. <br>
	 * Si este dto es nulo entonces se calcula la cantidad de ítems públicos:
	 * caso contrario además se cuentan los ítems pertenecientes a los proyectos
	 * asociados con el usuario recibido.
	 * 
	 * @param anUser
	 *            es el usuario para el cual se deben listar los ítems.
	 * @param aProject
	 *            es el proyecto actual. Este parámetro puede ser nulo.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar.
	 * @return la cantidad de ítems para el listado.
	 */
	public int getItemsCount(User anUser, Project aProject, String aFilterString);

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del filtro que se debe buscar.
	 * @return el filtro identificado por el id recibido.
	 * @throws FilterUnknownException
	 *             esta excepción se dispara en caso de no encontrar el filtro.
	 */
	public Filter findFilterById(String anOid) throws FilterUnknownException;

	/**
	 * Obtiene una colección que contiene los filtros favoritos de un usuario.
	 * 
	 * @param anUser
	 *            es el usuario dueño de los filtros.
	 * @return una colección que contiene los filtros favoritos del usuario.
	 */
	public Collection<Filter> getFavoriteItemsFiltersOfUser(User anUser);

	/**
	 * Verifica si existe un filtro con un nombre dado para el usuario recibido.
	 * 
	 * @param aName
	 *            es el nombre del filtro.
	 * @param anUser
	 *            es el usuario en el que se debe buscar el filtro.
	 * @return true en caso de que el usuario tenga un filtro de ítems con el
	 *         nombre dado;false en caso contrario.
	 * @throws FilterUnknownException
	 *             esta excepción se dispara en caso de no encontrar el filtro.
	 */
	public Filter findFilterByNameForUser(String aName, User anUser)
			throws FilterUnknownException;

	/**
	 * Obtiene una colección que contiene los filtros de un usuario.
	 * 
	 * @param anUser
	 *            es el usuario dueño de los filtros.
	 * @return una colección que contiene los filtros del usuario.
	 */
	public Collection<Filter> getItemsFiltersOfUser(User anUser);

	/**
	 * Verifica si el usuario cuyo oid se recibió es observador del ítem.
	 * 
	 * @param anUserOID
	 *            es el oid del usuario.
	 * @param anItemOID
	 *            es el oid del ítem.
	 * @return true en caso de que el usuario sea observador del item; false en
	 *         caso contrario.
	 */
	public boolean isUserObserverOfItem(String anUserOID, String anItemOID);

	/**
	 * Recupera un ítem con el oid dado.
	 * 
	 * @param anOid
	 *            es el oid del ítem que se está buscando.
	 * @return el ítem con el id dado.
	 * @throws ItemUnknownException
	 *             esta excepción puede levantarse en caso de tratar de
	 *             recuperar un ítem que no existe.
	 */
	public Item findById(String anOid) throws ItemUnknownException;

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del tipo de ítem que se debe recuperar.
	 * @return el tipo de ítem buscado, o se levanta una excepción en caso de no
	 *         encontrarlo.
	 * @throws ItemTypeUnknownException
	 *             esta excepción se levanta en caso de no encontrar el tipo de
	 *             ítme buscado.
	 */
	public ItemType findItemTypeById(String anOid)
			throws ItemTypeUnknownException;

	/**
	 * Finder.
	 * 
	 * @param aProjectDTO
	 *            el dto que representa la proyecto para el cual se deben
	 *            recuperar los tipos de ítems.
	 * @param aPropertyName
	 *            establece por cual propiedad se debería devolver ordenado el
	 *            resultado.
	 * @param anOrdering
	 *            establece si el orden es ascendente o no.
	 * 
	 * @return una colección que contiene todos los tipos de ítem del proyecto
	 *         seleccionado.
	 */
	public Collection<ItemType> findItemTypesOfProject(ProjectDTO aProjectDTO,
			String aPropertyName, String anOrdering) throws Exception;

	/**
	 * Recupera una colección de tipos de ítems que esté contenida entre los
	 * índices recibidos. <br>
	 * 
	 * @param index
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden con el que se debe devolver el resultado.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            el resultado.
	 * @return una colección de tipos de ítems del sistema.
	 */
	public Collection<ItemType> findItemTypes(int index, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Recupera los tipos de ítems cuyos identificadores se han recibido.
	 * 
	 * @param someItemTypesDTO
	 *            es una colección que contiene los dtos que representan a los
	 *            tipos de ítems que se deben recuperar.
	 * 
	 * @return una colección de tipos de ítems.
	 */
	public Collection<ItemType> findItemTypeByIds(
			Iterator<ItemTypeDTO> someItemTypesDTO);

	/**
	 * Verifica si existe un tipo de ítem con el título dado.
	 * 
	 * @param tracker
	 *            es el objeto que representa al sistema.
	 * @param aTitle
	 *            es el título que se está buscando.
	 * @return true en caso de que el tracker contenga un tipo de ítem con el
	 *         título dado; false en caso contrario.
	 */
	public boolean containsItemTypeWithTitle(Tracker tracker, String aTitle);

	/**
	 * Verifica si existe un tipo de ítem con el título dado en el proyecto.
	 * 
	 * @param aProject
	 *            es el proyecto donde se debe buscar.
	 * @param aTitle
	 *            es el título que se está buscando.
	 * @return true en caso de que el proyecto contenga un tipo de ítem con el
	 *         título dado; false en caso contrario.
	 */
	public boolean containsItemTypeWithTitleInProject(Project aProject,
			String aTitle);

	/**
	 * Recupera los tipos de ítems cuyos identificadores se recibieron.
	 * 
	 * @param someItemTypesOids
	 *            es una colección que contiene los identificadores de los tipos
	 *            de ítems.
	 * @return una colección con todos los tipos de ítems.
	 */
	public Collection<ItemType> findItemTypeByIds(
			Collection<String> someItemTypesOids);

	/**
	 * Recupera ítems por sus identificadores.
	 * 
	 * @param selectedItems
	 *            es una colección que contiene los ids de los ítems que se
	 *            deben recuperar.
	 * @return una colección que contiene los ítems recuperados.
	 */
	public Collection<Item> findItemsById(Collection<String> selectedItems);

	/**
	 * Recupera los archivos adjuntos de un ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que tiene los adjuntos.
	 * @return la colección de archivos adjuntos del ítem.
	 */
	public Collection<ItemFile> findAttachedFilesOfItem(ItemDTO anItemDTO);

	/**
	 * Recupera archivos adjuntos por sus identificadores.
	 * 
	 * @param someFilesOids
	 *            es una colección que contiene los identificadores de los
	 *            archivos que se deben recuperar.
	 * 
	 * @return la colección de archivos recuperados.
	 */
	public Collection<ItemFile> findAttachedFilesById(
			Collection<String> someFilesOids);

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todos los tipos de ítems utilizados en
	 *         los proyectos.
	 */
	public Collection<ItemType> findAllItemTypesOfProjects();

	/**
	 * Recupera todos los usuarios observadores de un ítem.
	 * 
	 * @param anOid
	 *            es el identificador del ítem para el cual se debe recuperar la
	 *            colección de observadores.
	 * @return una colección de usuarios registrados como observadores.
	 */
	public Collection<User> getObserversOfItem(String anOid);

	/**
	 * Recupera ítems por sus identificadores, siempre y cuando pertenezcan al
	 * proyecto referenciado.
	 * 
	 * @param selectedItems
	 *            es una colección que contiene los ids de los ítems que se
	 *            deben recuperar.
	 * @param aProjectOid
	 *            es el identificador del proyecto para el cual se deben
	 *            recuperar los ítems.
	 * @return una colección que contiene los ítems recuperados.
	 */
	public Collection<Item> findItemsById(Collection<String> selectedItems,
			String aProjectOid);
}
