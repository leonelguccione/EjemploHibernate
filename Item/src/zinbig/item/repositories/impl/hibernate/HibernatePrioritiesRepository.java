/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Query;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.PrioritySetUnknownException;
import zinbig.item.model.exceptions.PriorityUnknownException;
import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.PrioritySet;
import zinbig.item.model.projects.Project;
import zinbig.item.repositories.bi.PrioritiesRepositoryBI;

/**
 * Las instancias de este repositorio se utilizan para acceder en forma
 * eficiente a las instancias de la clase Priority.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernatePrioritiesRepository extends HibernateBaseRepository
		implements PrioritiesRepositoryBI {

	/**
	 * Retorna la cantidad de requerimientos para cada una de las prioridades
	 * del proyecto.
	 * 
	 * @param aProject
	 *            es le proyecto que contiene los requerimientos y prioridades.
	 * @return una lista de listas, que contiene el título del nivel de
	 *         prioridad y un número que representa la cantidad de
	 *         requerimientos no finalizados para ese nivel de prioridad.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object findUnfinishedItemsCountByPriorityForProject(Project aProject) {

		Query aQuery = this.getNamedQuery("itemsByPriorityQuery");

		aQuery.setParameter("aProject", aProject);
		aQuery.setParameter("aBoolean", false);

		Collection result = aQuery.list();

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de conjuntos de prioridades.
	 */
	@Override
	public int getPrioritySetsCount() {
		Query aQuery = this.getNamedQuery("prioritySetsCountQuery");

		aQuery.setMaxResults(1);

		return ((Long) aQuery.uniqueResult()).intValue();
	}

	/**
	 * Recupera una colección de conjuntos de prioridades que esté contenida
	 * entre los índices recibidos. <br>
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
	 * @return una colección de conjuntos de prioridades del sistema.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<PrioritySet> findPrioritySets(int index, int count,
			String aPropertyName, String anOrdering) {

		Query aQuery = this.getNamedQuery("allPrioritySetsQuery",
				aPropertyName, anOrdering);
		Collection<PrioritySet> result;
		aQuery.setMaxResults(count);
		aQuery.setFirstResult(index);

		result = aQuery.list();

		return result;
	}

	/**
	 * Verifica si existe un grupo de prioridades con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aName
	 *            es el nombre del grupo de prioridades que se está verificando.
	 * @return true en caso de que exista el grupo de prioridades con el nombre
	 *         dado; false en caso contrario.
	 */
	public boolean containsPrioritySetWithName(Tracker aTracker, String aName) {
		boolean result = false;

		try {
			PrioritySet aPrioritySet = this.findPrioritySetByName(aTracker,
					aName);
			result = (aPrioritySet != null);
		} catch (PrioritySetUnknownException e) {
			// no se debe hacer nada en este caso.
			result = false;
		}

		return result;
	}

	/**
	 * Recupera un conjunto de prioridades por su nombre.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aName
	 *            es el nombre del conjunto de prioridades.
	 * @return el conjunto de prioridades con el nombre dado.
	 * @throws PrioritySetUnknownException
	 *             esta excepción puede levantarse si no existe un grupo de
	 *             prioridades con el nombre dado.
	 */
	public PrioritySet findPrioritySetByName(Tracker aTracker, String aName)
			throws PrioritySetUnknownException {

		Query aQuery = this.getNamedQuery("prioritySetByNameQuery");

		aQuery.setParameter("aName", aName);
		aQuery.setMaxResults(1);

		PrioritySet result = (PrioritySet) aQuery.uniqueResult();

		if (result == null) {

			throw new PrioritySetUnknownException();

		}
		return result;

	}

	/**
	 * Recupera un conjunto de prioridades por su id.
	 * 
	 * 
	 * @param anId
	 *            es el id del conjunto de prioridades.
	 * @return el conjunto de prioridades con el nombre dado.
	 * @throws PrioritySetUnknownException
	 *             esta excepción puede levantarse si no existe un grupo de
	 *             prioridades con el nombre dado.
	 */
	public PrioritySet findById(String anOid)
			throws PrioritySetUnknownException {

		PrioritySet result = (PrioritySet) this.findById(PrioritySet.class,
				anOid);

		if (result == null) {

			throw new PrioritySetUnknownException();

		}
		return result;

	}

	/**
	 * Recupera todos los conjuntos de prioridades del sistema.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @return una colección que contiene todos los conjuntos de prioridades.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<PrioritySet> getAllPrioritySets(Tracker aTracker) {
		Query aQuery = this.getNamedQuery("allPrioritySetsQuery");
		Collection<PrioritySet> result;

		result = aQuery.list();

		return result;
	}

	/**
	 * Recupera el conjunto de prioridades marcado como default.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @return el conjunto de prioridades por defecto.
	 * @throws PrioritySetUnknownException
	 *             esta excepción puede levantarse si no existe un grupo de
	 *             prioridades marcado como default.
	 */
	@Override
	public PrioritySet getDefaultPrioritySet(Tracker aTracker)
			throws PrioritySetUnknownException {
		Query aQuery = this.getNamedQuery("defaultPrioritySetQuery");

		aQuery.setMaxResults(1);

		PrioritySet result = (PrioritySet) aQuery.uniqueResult();

		if (result == null) {

			throw new PrioritySetUnknownException();

		}
		return result;
	}

	/**
	 * Recupera todas las prioridades pertenecientes a un conjunto de
	 * prioridades cuyo id se ha recibido.
	 * 
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultad.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @return una colección que contiene todas las prioridades del conjunto.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Priority> findPrioritiesOfPrioritySet(
			String aPrioritySetId, String aPropertyName, boolean isAscending) {
		Query aQuery = this.getNamedQuery("prioritiesOfPrioritySetQuery",
				aPropertyName, isAscending ? "ASC" : "DESC");

		aQuery.setParameter("anId", aPrioritySetId);
		Collection<Priority> result;

		result = aQuery.list();

		return result;
	}

	/**
	 * Recupera un conjunto de prioridades identificadas por los dtos contenidos
	 * en la colección recibida.<br>
	 * La recuperación se realiza por oids por lo que no es necesario pasar por
	 * parámetro el conjunto de prioridades que contiene a las prioridades.<br>
	 * En caso de que no existan las prioridades referenciadas por los DTOs se
	 * devolverá una colección vacía.
	 * 
	 * @param selectedPriorities
	 *            es la colección que contiene los dtos de las prioridades que
	 *            deben ser recuperadas.
	 * @return una colección con las prioridades recuperadas.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Priority> findPriorities(
			Collection<String> selectedPriorities) {

		Query aQuery = this.getNamedQuery("prioritiesByIdsQuery");
		aQuery.setParameterList("aList", selectedPriorities);
		Collection<Priority> result = aQuery.list();
		return result;
	}

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid de la prioridad que se debe recuperar.
	 * @return la prioridad con el oid recibido.
	 * @throws PriorityUnknownException
	 *             esta excepción puede ser lanzada en caso de no encontrar una
	 *             prioridad con el oid dado.
	 */
	public Priority findPriorityById(String anOid)
			throws PriorityUnknownException {

		Query aQuery = this.getNamedQuery("priorityByIdQuery");

		aQuery.setParameter("anOid", anOid);
		aQuery.setMaxResults(1);

		Priority result = (Priority) aQuery.uniqueResult();

		if (result == null) {

			throw new PriorityUnknownException();

		}
		return result;

	}

	/**
	 * Finder.
	 * 
	 * @param somePrioritySetsIds
	 *            es una colección que contiene los ids de los conjuntos de
	 *            prioridades que se deben recuperar.
	 * @return una colección con los conjuntos de prioridades cuyos ids se
	 *         recibieron.
	 */
	@SuppressWarnings("unchecked")
	public Collection<PrioritySet> findPrioritySetsByIds(
			Collection<String> somePrioritySetsIds) {

		Collection<PrioritySet> prioritySets = new ArrayList<PrioritySet>();
		Query aQuery = this.getNamedQuery("prioritySetsByIdsQuery");
		aQuery.setParameterList("aList", somePrioritySetsIds);
		if (!somePrioritySetsIds.isEmpty()) {
			prioritySets.addAll(aQuery.list());
		}

		return prioritySets;

	}

}
