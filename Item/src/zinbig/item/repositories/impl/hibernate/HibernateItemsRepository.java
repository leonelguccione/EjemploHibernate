/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Query;

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
import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Esta clase implementa un repositorio de operaciones que utiliza Hibernate
 * para acceder en forma eficiente a las instancias de la clase Item.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernateItemsRepository extends HibernateBaseRepository implements
		ItemsRepositoryBI {

	/**
	 * Recupera una colecci�n de �tems que est� contenida entre los �ndices
	 * recibidos y que pertenecen a los proyectos asociados con el usuario
	 * adem�s de los �tems p�blicos. <br>
	 * 
	 * @param anUser
	 *            es el usuario para el cual se deben recuperar los �tems.
	 * @param aProject
	 *            es el proyecto en el cual se deben listar los �tems. Podr�a
	 *            ser nulo.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar.
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
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Item> findItems(User anUser, Project aProject,
			String aFilterString, int beginIndex, int count,
			String aPropertyName, String anOrdering) {

		Collection<Item> result;

		String projectsString = "";
		String replaceString = "";
		if (anUser != null && anUser.getProjects().size() > 0) {
			projectsString = this.getProjectsString(anUser);
			replaceString = "$projects";
		} else {
			replaceString = "or i.project.oid in $projects";

		}
		aFilterString = aFilterString.replace(replaceString, projectsString);

		Query aQuery = this.getNamedQuery("allItemsQuery", aFilterString,
				aPropertyName, anOrdering);

		aQuery.setMaxResults(count);
		aQuery.setFirstResult(beginIndex);

		result = aQuery.list();

		return result;

	}

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
	 *            es el proyecto actual en el que se deben listar los �tems.
	 *            Este pa�metro puede ser nulo.
	 * @param aFilter
	 *            es el filtro que se debe aplicar a los �tems.
	 * @return la cantidad de �tems para el listado.
	 */
	@Override
	public int getItemsCount(User anUser, Project aProject, String aFilterString) {

		if (anUser != null) {

			String projectsString = "";
			String replaceString = "";
			if (anUser != null && anUser.getProjects().size() > 0) {
				projectsString = this.getProjectsString(anUser);
				replaceString = "$projects";
			} else {
				replaceString = "or i.project.oid in $projects";

			}
			aFilterString = aFilterString
					.replace(replaceString, projectsString);
		}

		Query aQuery = this.getNamedQuery("allItemsCountQuery", aFilterString);

		aQuery.setMaxResults(1);

		return ((Long) aQuery.uniqueResult()).intValue();

	}

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del filtro que se debe buscar.
	 * @return el filtro identificado por el id recibido.
	 * @throws FilterUnknownException
	 *             esta excepci�n se dispara en caso de no encontrar el filtro.
	 */
	public Filter findFilterById(String anOid) throws FilterUnknownException {
		Query aQuery = this.getNamedQuery("filterByIdQuery");

		aQuery.setParameter("anOid", anOid);

		aQuery.setMaxResults(1);

		Filter result = (Filter) aQuery.uniqueResult();

		if (result == null) {

			throw new FilterUnknownException();

		}
		return result;
	}

	/**
	 * Retorna un string que contiene todos los identificadores de los proyectos
	 * a los cuales pertenece el usuario recibido.
	 * 
	 * @param anUser
	 *            es el usuario para el cual se debe consultar la lista de
	 *            proyectos.
	 * @return un string con los ids de los proyectos de la forma (oids).
	 */
	private String getProjectsString(User anUser) {
		Collection<String> projects = new ArrayList<String>();

		Iterator<Project> anIterator = anUser.getProjects().iterator();

		while (anIterator.hasNext()) {
			projects.add("'" + anIterator.next().getOid() + "'");
		}

		String projectsString = projects.toString();
		projectsString = projectsString.replace("[", "(").replace("]", ")");

		return projectsString;
	}

	/**
	 * Obtiene una colecci�n que contiene los filtros favoritos de un usuario.
	 * 
	 * @param anUser
	 *            es el usuario due�o de los filtros.
	 * @return una colecci�n que contiene los filtros favoritos del usuario.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Filter> getFavoriteItemsFiltersOfUser(User anUser) {
		Query aQuery = this.getNamedQuery("favoritesFiltersOfUserQuery");

		aQuery.setParameter("anUsername", anUser.getUsername());

		return aQuery.list();
	}

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
			throws FilterUnknownException {

		Query aQuery = this.getNamedQuery("filterByNameQuery");

		aQuery.setParameter("aName", aName);
		aQuery.setParameter("anOid", anUser.getOid());

		aQuery.setMaxResults(1);

		Filter result = (Filter) aQuery.uniqueResult();

		if (result == null) {

			throw new FilterUnknownException();

		}
		return result;

	}

	/**
	 * Obtiene una colecci�n que contiene los filtros de un usuario.
	 * 
	 * @param anUser
	 *            es el usuario due�o de los filtros.
	 * @return una colecci�n que contiene los filtros del usuario.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Filter> getItemsFiltersOfUser(User anUser) {
		Query aQuery = this.getNamedQuery("filtersOfUserQuery");

		aQuery.setParameter("anUsername", anUser.getUsername());

		return aQuery.list();
	}

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
	public boolean isUserObserverOfItem(String anUserOID, String anItemOID) {
		Query aQuery = this.getNamedQuery("userObserverOfItem");

		aQuery.setParameter("anUserOID", anUserOID);
		aQuery.setParameter("anItemOID", anItemOID);
		aQuery.setMaxResults(1);

		return ((Long) aQuery.uniqueResult()).intValue() != 0;
	}

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
	public Item findById(String anOid) throws ItemUnknownException {

		Item result = (Item) this.findById(Item.class, anOid);

		if (result == null) {

			throw new ItemUnknownException();

		}
		return result;
	}

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
			throws ItemTypeUnknownException {
		Query aQuery = this.getNamedQuery("itemTypeByIdQuery");

		aQuery.setParameter("anId", anOid);
		aQuery.setMaxResults(1);

		ItemType result = (ItemType) aQuery.uniqueResult();
		if (result == null) {
			throw new ItemTypeUnknownException();
		} else {
			return result;
		}
	}

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el t�tulo del tipo de �tem que se est� buscando.
	 * @param aTracker
	 *            es el objeto que representa la sistema.
	 * @return el tipo de �tem buscado, o se levanta una excepci�n en caso de no
	 *         encontrarlo.
	 * @throws ItemTypeUnknownException
	 *             esta excepci�n se levanta en caso de no encontrar el tipo de
	 *             �tme buscado.
	 */
	public ItemType findItemTypeByTitle(Tracker aTracker, String aTitle)
			throws ItemTypeUnknownException {
		Query aQuery = this.getNamedQuery("itemTypeByTitleQuery");

		aQuery.setParameter("aTitle", aTitle);
		aQuery.setMaxResults(1);

		ItemType result = (ItemType) aQuery.uniqueResult();

		if (result == null) {

			throw new ItemTypeUnknownException();

		}
		return result;
	}

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el t�tulo del tipo de �tem que se est� buscando.
	 * @param aProject
	 *            es el proyecto donde se debe buscar el tipo de �tem.
	 * @return el tipo de �tem buscado, o se levanta una excepci�n en caso de no
	 *         encontrarlo.
	 * @throws ItemTypeUnknownException
	 *             esta excepci�n se levanta en caso de no encontrar el tipo de
	 *             �tme buscado.
	 */
	public ItemType findItemTypeByTitleInProject(Project aProject, String aTitle)
			throws ItemTypeUnknownException {
		Query aQuery = this.getNamedQuery("itemTypeByTitleInProjectQuery");

		aQuery.setParameter("aTitle", aTitle);
		aQuery.setParameter("anOid", aProject.getOid());
		aQuery.setMaxResults(1);

		ItemType result = (ItemType) aQuery.uniqueResult();

		if (result == null) {

			throw new ItemTypeUnknownException();

		}
		return result;
	}

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
	 * @return una colecci�n que contiene todos los dtos de los tipos de �tem
	 *         del proyecto seleccionado.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ItemType> findItemTypesOfProject(ProjectDTO aProjectDTO,
			String aPropertyName, String anOrdering) throws Exception {
		Collection<ItemType> result = new ArrayList<ItemType>();
		Query aQuery = this.getNamedQuery("itemTypesOfProjectQuery",
				aPropertyName, anOrdering);

		aQuery.setParameter("anId", aProjectDTO.getOid());
		result.addAll((Collection<ItemType>) aQuery.list());

		return result;
	}

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
	@SuppressWarnings("unchecked")
	@Override
	public Collection<ItemType> findItemTypes(int index, int count,
			String aPropertyName, String anOrdering) {

		Query aQuery = this.getNamedQuery("allItemTypesQuery", aPropertyName,
				anOrdering);
		Collection<ItemType> result;
		aQuery.setMaxResults(count);
		aQuery.setFirstResult(index);

		result = aQuery.list();

		return result;
	}

	/**
	 * Recupera los tipos de �tems cuyos identificadores se han recibido.
	 * 
	 * @param someItemTypesDTO
	 *            es un iterador de una colecci�n que contiene los dtos que
	 *            representan a los tipos de �tems que se deben recuperar.
	 * 
	 * @return una colecci�n de tipos de �tems.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ItemType> findItemTypeByIds(
			Iterator<ItemTypeDTO> someItemTypesDTO) {

		Collection<ItemType> itemTypes = new ArrayList<ItemType>();
		Collection<String> ids = new ArrayList<String>();

		while (someItemTypesDTO.hasNext()) {
			ids.add(someItemTypesDTO.next().getOid());
		}

		Query aQuery = this.getNamedQuery("itemTypesByIdQuery");
		aQuery.setParameterList("aList", ids);

		if (!ids.isEmpty()) {
			itemTypes.addAll(aQuery.list());
		}

		return itemTypes;
	}

	/**
	 * Verifica si existe un tipo de �tem con el t�tulo dado.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que el tracker contenga un tipo de �tem con el
	 *         t�tulo dado; false en caso contrario.
	 */
	public boolean containsItemTypeWithTitle(Tracker aTracker, String aTitle) {
		boolean result = false;

		try {
			ItemType anItemType = this.findItemTypeByTitle(aTracker, aTitle);
			result = (anItemType != null);
		} catch (ItemTypeUnknownException e) {
			// no se debe hacer nada en este caso.
			result = false;
		}

		return result;
	}

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
			String aTitle) {
		boolean result = false;

		try {
			ItemType anItemType = this.findItemTypeByTitleInProject(aProject,
					aTitle);
			result = (anItemType != null);
		} catch (ItemTypeUnknownException e) {
			// no se debe hacer nada en este caso.
			result = false;
		}

		return result;
	}

	/**
	 * Recupera los tipos de �tems cuyos identificadores se recibieron.
	 * 
	 * @param someItemTypesOids
	 *            es una colecci�n que contiene los identificadores de los tipos
	 *            de �tems.
	 * @return una colecci�n con todos los tipos de �tems.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ItemType> findItemTypeByIds(
			Collection<String> someItemTypesOids) {
		Collection<ItemType> result = new ArrayList<ItemType>();

		Query aQuery = this.getNamedQuery("itemTypesByIdQuery");
		aQuery.setParameterList("aList", someItemTypesOids);

		if (!someItemTypesOids.isEmpty()) {
			result.addAll(aQuery.list());
		}

		return result;
	}

	/**
	 * Recupera �tems por sus identificadores.
	 * 
	 * @param selectedItems
	 *            es una colecci�n que contiene los ids de los �tems que se
	 *            deben recuperar.
	 * @return una colecci�n que contiene los �tems recuperados.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Item> findItemsById(Collection<String> selectedItems) {

		Collection<Item> items = new ArrayList<Item>();

		Query aQuery = this.getNamedQuery("itemsByIdQuery");
		aQuery.setParameterList("aList", selectedItems);

		if (!selectedItems.isEmpty()) {
			items.addAll(aQuery.list());
		}

		return items;
	}

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
	@SuppressWarnings("unchecked")
	public Collection<Item> findItemsById(Collection<String> selectedItems,
			String aProjectOid) {

		Collection<Item> items = new ArrayList<Item>();

		Query aQuery = this.getNamedQuery("itemsByItemIdQuery");
		Collection<Integer> ids = new ArrayList<Integer>();
		for (String s : selectedItems) {
			ids.add(new Integer(s));
		}

		aQuery.setParameterList("aList", ids);
		aQuery.setParameter("anOid", aProjectOid);

		if (!selectedItems.isEmpty()) {
			items.addAll(aQuery.list());
		}

		return items;
	}

	/**
	 * Recupera los archivos adjuntos de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que tiene los adjuntos.
	 * @return la colecci�n de archivos adjuntos del �tem.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ItemFile> findAttachedFilesOfItem(ItemDTO anItemDTO) {
		Collection<ItemFile> result = new ArrayList<ItemFile>();
		Query aQuery = this.getNamedQuery("itemFilesOfItemQuery");

		aQuery.setParameter("anId", anItemDTO.getOid());
		result.addAll((Collection<ItemFile>) aQuery.list());

		return result;
	}

	/**
	 * Recupera archivos adjuntos por sus identificadores.
	 * 
	 * @param someFilesOids
	 *            es una colecci�n que contiene los identificadores de los
	 *            archivos que se deben recuperar.
	 * 
	 * @return la colecci�n de archivos recuperados.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ItemFile> findAttachedFilesById(
			Collection<String> someFilesOids) {
		Collection<ItemFile> itemFiles = new ArrayList<ItemFile>();

		Query aQuery = this.getNamedQuery("itemFilesByIdQuery");
		aQuery.setParameterList("aList", someFilesOids);

		if (!someFilesOids.isEmpty()) {
			itemFiles.addAll(aQuery.list());
		}

		return itemFiles;
	}

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems utilizados en
	 *         los proyectos.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ItemType> findAllItemTypesOfProjects() {
		Collection<ItemType> result = new ArrayList<ItemType>();

		Query aQuery = this.getNamedQuery("allItemTypesOfProjects");

		result.addAll(aQuery.list());

		return result;
	}

	/**
	 * Recupera todos los usuarios observadores de un �tem.
	 * 
	 * @param anOid
	 *            es el identificador del �tem para el cual se debe recuperar la
	 *            colecci�n de observadores.
	 * @return una colecci�n de usuarios registrados como observadores.
	 */
	@SuppressWarnings("unchecked")
	public Collection<User> getObserversOfItem(String anOid) {
		Collection<User> result = new ArrayList<User>();
		Query aQuery = this.getNamedQuery("observersOfItemQuery");

		aQuery.setParameter("anOid", anOid);
		result.addAll((Collection<User>) aQuery.list());

		return result;
	}
}
