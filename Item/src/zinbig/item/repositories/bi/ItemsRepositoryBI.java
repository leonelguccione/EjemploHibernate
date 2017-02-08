/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deber�n respetar las implementaciones del patr�n de dise�o Repository.
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
 * Esta interface establece el protocolo est�ndar que deber� ser respetado por
 * todas las clases que implementen el patr�n de dise�o Repository para acceder
 * eficientemente a las instancias de la clase �tem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface ItemsRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Recupera una colecci�n de �tems que est� contenida entre los �ndices
	 * recibidos y que pertenecen a los proyectos asociados con el usuario
	 * adem�s de los �tems p�blicos. <br>
	 * 
	 * @param anUser
	 *            es el usuario para el cual se deben listar los �tems.
	 * @param aProject
	 *            es el proyecto actual. Este par�metro puede ser nulo.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar a los �tems.
	 * @param beginIndex
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return una colecci�n de �tems del sistema.
	 */
	public Collection<Item> findItems(User anUser, Project aProject,
			String aFilterString, int beginIndex, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Retorna la cantidad de �tems para el listado pedido por el usuario cuyo
	 * dto se ha recibido. <br>
	 * Si este dto es nulo entonces se calcula la cantidad de �tems p�blicos:
	 * caso contrario adem�s se cuentan los �tems pertenecientes a los proyectos
	 * asociados con el usuario recibido.
	 * 
	 * @param anUser
	 *            es el usuario para el cual se deben listar los �tems.
	 * @param aProject
	 *            es el proyecto actual. Este par�metro puede ser nulo.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar.
	 * @return la cantidad de �tems para el listado.
	 */
	public int getItemsCount(User anUser, Project aProject, String aFilterString);

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del filtro que se debe buscar.
	 * @return el filtro identificado por el id recibido.
	 * @throws FilterUnknownException
	 *             esta excepci�n se dispara en caso de no encontrar el filtro.
	 */
	public Filter findFilterById(String anOid) throws FilterUnknownException;

	/**
	 * Obtiene una colecci�n que contiene los filtros favoritos de un usuario.
	 * 
	 * @param anUser
	 *            es el usuario due�o de los filtros.
	 * @return una colecci�n que contiene los filtros favoritos del usuario.
	 */
	public Collection<Filter> getFavoriteItemsFiltersOfUser(User anUser);

	/**
	 * Verifica si existe un filtro con un nombre dado para el usuario recibido.
	 * 
	 * @param aName
	 *            es el nombre del filtro.
	 * @param anUser
	 *            es el usuario en el que se debe buscar el filtro.
	 * @return true en caso de que el usuario tenga un filtro de �tems con el
	 *         nombre dado;false en caso contrario.
	 * @throws FilterUnknownException
	 *             esta excepci�n se dispara en caso de no encontrar el filtro.
	 */
	public Filter findFilterByNameForUser(String aName, User anUser)
			throws FilterUnknownException;

	/**
	 * Obtiene una colecci�n que contiene los filtros de un usuario.
	 * 
	 * @param anUser
	 *            es el usuario due�o de los filtros.
	 * @return una colecci�n que contiene los filtros del usuario.
	 */
	public Collection<Filter> getItemsFiltersOfUser(User anUser);

	/**
	 * Verifica si el usuario cuyo oid se recibi� es observador del �tem.
	 * 
	 * @param anUserOID
	 *            es el oid del usuario.
	 * @param anItemOID
	 *            es el oid del �tem.
	 * @return true en caso de que el usuario sea observador del item; false en
	 *         caso contrario.
	 */
	public boolean isUserObserverOfItem(String anUserOID, String anItemOID);

	/**
	 * Recupera un �tem con el oid dado.
	 * 
	 * @param anOid
	 *            es el oid del �tem que se est� buscando.
	 * @return el �tem con el id dado.
	 * @throws ItemUnknownException
	 *             esta excepci�n puede levantarse en caso de tratar de
	 *             recuperar un �tem que no existe.
	 */
	public Item findById(String anOid) throws ItemUnknownException;

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del tipo de �tem que se debe recuperar.
	 * @return el tipo de �tem buscado, o se levanta una excepci�n en caso de no
	 *         encontrarlo.
	 * @throws ItemTypeUnknownException
	 *             esta excepci�n se levanta en caso de no encontrar el tipo de
	 *             �tme buscado.
	 */
	public ItemType findItemTypeById(String anOid)
			throws ItemTypeUnknownException;

	/**
	 * Finder.
	 * 
	 * @param aProjectDTO
	 *            el dto que representa la proyecto para el cual se deben
	 *            recuperar los tipos de �tems.
	 * @param aPropertyName
	 *            establece por cual propiedad se deber�a devolver ordenado el
	 *            resultado.
	 * @param anOrdering
	 *            establece si el orden es ascendente o no.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tem del proyecto
	 *         seleccionado.
	 */
	public Collection<ItemType> findItemTypesOfProject(ProjectDTO aProjectDTO,
			String aPropertyName, String anOrdering) throws Exception;

	/**
	 * Recupera una colecci�n de tipos de �tems que est� contenida entre los
	 * �ndices recibidos. <br>
	 * 
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden con el que se debe devolver el resultado.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            el resultado.
	 * @return una colecci�n de tipos de �tems del sistema.
	 */
	public Collection<ItemType> findItemTypes(int index, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Recupera los tipos de �tems cuyos identificadores se han recibido.
	 * 
	 * @param someItemTypesDTO
	 *            es una colecci�n que contiene los dtos que representan a los
	 *            tipos de �tems que se deben recuperar.
	 * 
	 * @return una colecci�n de tipos de �tems.
	 */
	public Collection<ItemType> findItemTypeByIds(
			Iterator<ItemTypeDTO> someItemTypesDTO);

	/**
	 * Verifica si existe un tipo de �tem con el t�tulo dado.
	 * 
	 * @param tracker
	 *            es el objeto que representa al sistema.
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que el tracker contenga un tipo de �tem con el
	 *         t�tulo dado; false en caso contrario.
	 */
	public boolean containsItemTypeWithTitle(Tracker tracker, String aTitle);

	/**
	 * Verifica si existe un tipo de �tem con el t�tulo dado en el proyecto.
	 * 
	 * @param aProject
	 *            es el proyecto donde se debe buscar.
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que el proyecto contenga un tipo de �tem con el
	 *         t�tulo dado; false en caso contrario.
	 */
	public boolean containsItemTypeWithTitleInProject(Project aProject,
			String aTitle);

	/**
	 * Recupera los tipos de �tems cuyos identificadores se recibieron.
	 * 
	 * @param someItemTypesOids
	 *            es una colecci�n que contiene los identificadores de los tipos
	 *            de �tems.
	 * @return una colecci�n con todos los tipos de �tems.
	 */
	public Collection<ItemType> findItemTypeByIds(
			Collection<String> someItemTypesOids);

	/**
	 * Recupera �tems por sus identificadores.
	 * 
	 * @param selectedItems
	 *            es una colecci�n que contiene los ids de los �tems que se
	 *            deben recuperar.
	 * @return una colecci�n que contiene los �tems recuperados.
	 */
	public Collection<Item> findItemsById(Collection<String> selectedItems);

	/**
	 * Recupera los archivos adjuntos de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que tiene los adjuntos.
	 * @return la colecci�n de archivos adjuntos del �tem.
	 */
	public Collection<ItemFile> findAttachedFilesOfItem(ItemDTO anItemDTO);

	/**
	 * Recupera archivos adjuntos por sus identificadores.
	 * 
	 * @param someFilesOids
	 *            es una colecci�n que contiene los identificadores de los
	 *            archivos que se deben recuperar.
	 * 
	 * @return la colecci�n de archivos recuperados.
	 */
	public Collection<ItemFile> findAttachedFilesById(
			Collection<String> someFilesOids);

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems utilizados en
	 *         los proyectos.
	 */
	public Collection<ItemType> findAllItemTypesOfProjects();

	/**
	 * Recupera todos los usuarios observadores de un �tem.
	 * 
	 * @param anOid
	 *            es el identificador del �tem para el cual se debe recuperar la
	 *            colecci�n de observadores.
	 * @return una colecci�n de usuarios registrados como observadores.
	 */
	public Collection<User> getObserversOfItem(String anOid);

	/**
	 * Recupera �tems por sus identificadores, siempre y cuando pertenezcan al
	 * proyecto referenciado.
	 * 
	 * @param selectedItems
	 *            es una colecci�n que contiene los ids de los �tems que se
	 *            deben recuperar.
	 * @param aProjectOid
	 *            es el identificador del proyecto para el cual se deben
	 *            recuperar los �tems.
	 * @return una colecci�n que contiene los �tems recuperados.
	 */
	public Collection<Item> findItemsById(Collection<String> selectedItems,
			String aProjectOid);
}
