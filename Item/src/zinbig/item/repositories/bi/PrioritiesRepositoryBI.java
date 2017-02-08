/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.bi;

import java.util.Collection;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.PrioritySetUnknownException;
import zinbig.item.model.exceptions.PriorityUnknownException;
import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.PrioritySet;
import zinbig.item.model.projects.Project;

/**
 * Esta interface define el comportamiento que deber�an implementar todos los
 * repositorios (patr�n Repository) utilizados para acceder en forma eficiente a
 * las prioridades.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface PrioritiesRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Obtiene la cantidad de items abiertos de un proyecto, agrupados por los
	 * niveles de prioridad.
	 * 
	 * @param project
	 *            es el proyecto que contiene todos los items y las prioridades.
	 * @return un diccionario con todos los niveles de prioridad y la cantidad
	 *         de items abiertos para cada nivel.
	 */
	public Object findUnfinishedItemsCountByPriorityForProject(Project project);

	/**
	 * Getter.
	 * 
	 * @return la cantidad de conjuntos de prioridades.
	 */
	public int getPrioritySetsCount();

	/**
	 * Recupera una colecci�n de conjuntos de prioridades que est� contenida
	 * entre los �ndices recibidos. <br>
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
	 * @return una colecci�n de conjuntos de prioridades del sistema.
	 */
	public Collection<PrioritySet> findPrioritySets(int index, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Verifica si existe un grupo de prioridades con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el objeto que representa la sistema
	 * @param aName
	 *            es el nombre del grupo de prioridades que se est� verificando.
	 * @return true en caso de que exista el grupo de prioridades con el nombre
	 *         dado; false en caso contrario.
	 */
	public boolean containsPrioritySetWithName(Tracker aTracker, String aName);

	/**
	 * Recupera un conjunto de prioridades por su nombre.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aName
	 *            es el nombre del conjunto de prioridades.
	 * @return el conjunto de prioridades con el nombre dado.
	 * @throws PrioritySetUnknownException
	 *             esta excepci�n puede levantarse si no existe un grupo de
	 *             prioridades con el nombre dado.
	 */
	public PrioritySet findPrioritySetByName(Tracker aTracker, String aName)
			throws PrioritySetUnknownException;

	/**
	 * Recupera un conjunto de prioridades por su id.
	 * 
	 * 
	 * @param anId
	 *            es el id del conjunto de prioridades.
	 * @return el conjunto de prioridades con el nombre dado.
	 * @throws PrioritySetUnknownException
	 *             esta excepci�n puede levantarse si no existe un grupo de
	 *             prioridades con el nombre dado.
	 */
	public PrioritySet findById(String anId) throws PrioritySetUnknownException;

	/**
	 * Recupera el conjunto de prioridades marcado como default.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @return el conjunto de prioridades por defecto.
	 * @throws PrioritySetUnknownException
	 *             esta excepci�n puede levantarse si no existe un grupo de
	 *             prioridades marcado como default.
	 */
	public PrioritySet getDefaultPrioritySet(Tracker aTracker)
			throws PrioritySetUnknownException;

	/**
	 * Recupera todos los conjuntos de prioridades del sistema.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @return una colecci�n que contiene todos los conjuntos de prioridades.
	 */
	public Collection<PrioritySet> getAllPrioritySets(Tracker aTracker);

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
	 * @return una colecci�n que contiene todas las prioridades del conjunto.
	 * @throws PrioritySetUnknownException
	 *             esta excepci�n puede levantarse si no existe un grupo de
	 *             prioridades con el nombre dado.
	 */
	public Collection<Priority> findPrioritiesOfPrioritySet(
			String aPrioritySetId, String aPropertyName, boolean isAscending)
			throws PrioritySetUnknownException;

	/**
	 * Recupera un conjunto de prioridades identificadas por los dtos contenidos
	 * en la colecci�n recibida.<br>
	 * La recuperaci�n se realiza por oids por lo que no es necesario pasar por
	 * par�metro el conjunto de prioridades que contiene a las prioridades.<br>
	 * En caso de que no existan las prioridades referenciadas por los DTOs se
	 * devolver� una colecci�n vac�a.
	 * 
	 * @param selectedPriorities
	 *            es la colecci�n que contiene los oids de las prioridades que
	 *            deben ser recuperadas.
	 * @return una colecci�n con las prioridades recuperadas.
	 */
	public Collection<Priority> findPriorities(
			Collection<String> selectedPriorities);

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid de la prioridad que se debe recuperar.
	 * @return la prioridad con el oid recibido.
	 * @throws PriorityUnknownException
	 *             esta excepci�n puede ser lanzada en caso de no encontrar una
	 *             prioridad con el oid dado.
	 */
	public Priority findPriorityById(String anOid)
			throws PriorityUnknownException;

	/**
	 * Finder.
	 * 
	 * @param somePrioritySetsIds
	 *            es una colecci�n que contiene los ids de los conjuntos de
	 *            prioridades que se deben recuperar.
	 * @return una colecci�n con los conjuntos de prioridades cuyos ids se
	 *         recibieron.
	 */
	public Collection<PrioritySet> findPrioritySetsByIds(
			Collection<String> somePrioritySetsIds);

}
