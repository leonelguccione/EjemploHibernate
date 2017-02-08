/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.ItemEditionException;
import zinbig.item.model.exceptions.PrioritySetUnknownException;
import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.PrioritySet;
import zinbig.item.model.projects.Project;
import zinbig.item.repositories.bi.PrioritiesRepositoryBI;
import zinbig.item.services.bi.PrioritiesServiceBI;
import zinbig.item.util.dto.PriorityDTO;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de esta clase se utilizan para acceder a la lógica de negocios
 * relacionada con las prioridades del sistema.<br>
 * La lógica de negocios propiamente dicha no se encuentra en los servicios sino
 * que está definida en los objetos de modelo.<br>
 * 
 * Los colaboradores de esta clase se inyectan mediante IoC.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrioritiesServiceImpl extends BaseServiceImpl implements
		PrioritiesServiceBI {

	/**
	 * Constructor.
	 */
	public PrioritiesServiceImpl() {
		super();
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de conjuntos de prioridades que existen.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	@Override
	public int getPrioritySetCount() throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();

		return aTracker.getPrioritySetsCount();
	}

	/**
	 * Getter.
	 * 
	 * @param index
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return la colección de dtos de los conjuntos de prioridades del sistema
	 *         empezando por el índice recibido y devolviendo únicamente la
	 *         cantidad especificada de elementos.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	@Override
	public Collection<PrioritySetDTO> getPrioritySets(int index, int count,
			String aPropertyName, String anOrdering) throws Exception {

		Collection<PrioritySet> prioritySets = this.getPrioritiesRepository()
				.findPrioritySets(index, count, aPropertyName, anOrdering);

		return this.getDtoFactory()
				.createDTOForPrioritySets(prioritySets, true);
	}

	/**
	 * Verifica si existe un grupo de prioridades con el nombre dado.
	 * 
	 * @param aName
	 *            es el nombre del grupo de prioridades.
	 * @return true en caso de que exista un grupo de prioridades con el nombre
	 *         recibido; false en caso contrario.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public boolean containsPrioritySetWithName(String aName) throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();

		return this.getPrioritiesRepository().containsPrioritySetWithName(
				aTracker, aName);
	}

	/**
	 * Getter.
	 * 
	 * @return un DTO que representa al conjunto de prioridades por defecto.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO getDefaultPrioritySet() throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();

		return this.getDtoFactory().createDTOForPrioritySet(
				this.getPrioritiesRepository().getDefaultPrioritySet(aTracker));
	}

	/**
	 * Recupera una colección de DTOs que representan a todos los conjuntos de
	 * prioridades del sistema.
	 * 
	 * @return una colección de DTOs que representa a todos los conjuntos de
	 *         prioridades, incluso al conjunto por defecto.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<PrioritySetDTO> getAllPrioritySets() throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();

		return this.getDtoFactory().createDTOForPrioritySets(
				this.getPrioritiesRepository().getAllPrioritySets(aTracker),
				true);
	}

	/**
	 * Agrega un nuevo conjunto de prioridades al sistema.
	 * 
	 * @param aName
	 *            es el nombre del nuevo conjunto de prioridades.
	 * @param isDefaultPrioritySet
	 *            indica si el conjunto de prioridades será el conjunto por
	 *            defecto.
	 * @return un DTO que representa al conjunto de prioridades recién creado.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO addPrioritySet(String aName,
			boolean isDefaultPrioritySet) throws Exception {

		Tracker aTracker = this.getTrackerRepository().findTracker();

		PrioritySet aPrioritySet = aTracker.addPrioritySet(aName,
				isDefaultPrioritySet);

		return this.getDtoFactory().createDTOForPrioritySet(aPrioritySet);
	}

	/**
	 * Recupera un conjunto de prioridades por su oid.
	 * 
	 * @param anOid
	 *            es el identificador del conjunto de prioridades que se debe
	 *            recuperar.
	 * @return el conjunto de prioridades.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO findPrioritySetById(String anOid) throws Exception {
		PrioritySet aPrioritySet = this.getPrioritiesRepository().findById(
				anOid);

		return this.getDtoFactory().createDTOForPrioritySet(aPrioritySet);
	}

	/**
	 * Recupera un conjunto de prioridades por su nombre.
	 * 
	 * @param aName
	 *            es el nombre del conjunto de prioridades que se debe
	 *            recuperar.
	 * @return el conjunto de prioridades.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO findPrioritySetByName(String aName) throws Exception {
		PrioritySet aPrioritySet = this.getPrioritiesRepository()
				.findPrioritySetByName(null, aName);

		return this.getDtoFactory().createDTOForPrioritySet(aPrioritySet);
	}

	/**
	 * Edita la información básica de un conjunto de prioridades.
	 * 
	 * @param prioritySetDTO
	 *            es el dto que representa al conjunto de prioridades que se
	 *            está editando.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void editPrioritySet(PrioritySetDTO prioritySetDTO) throws Exception {

		try {

			PrioritySet aPrioritySet = this.getPrioritiesRepository().findById(
					prioritySetDTO.getOid());

			// controla que se esté editando la última versión del objeto
			if (!this.verifyLatestVersion(aPrioritySet, prioritySetDTO)) {

				throw new ItemConcurrentModificationException();

			} else {
				Tracker aTracker = this.getTrackerRepository().findTracker();
				// actualiza la información del conjunto de prioridades
				aTracker.updatePrioritySet(aPrioritySet, prioritySetDTO
						.getName(), prioritySetDTO.isDefaultPrioritySet());
			}

		} catch (PrioritySetUnknownException psue) {

			psue.printStackTrace();
			throw new ItemEditionException();

		}

	}

	/**
	 * Agrega una nueva prioridad individual al conjunto de prioridades cuyo
	 * nombre se ha recibido.
	 * 
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades al cual se debe agregar
	 *            la nueva prioridad.
	 * @param aName
	 *            es el nombre de la nueva prioridad.
	 * @param aValue
	 *            es el valor de la nueva prioridad.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void addPriorityToPrioritySet(String aPrioritySetId, String aName,
			String aValue) throws Exception {

		Tracker aTracker = this.getTrackerRepository().findTracker();
		PrioritySet aPrioritySet = (PrioritySet) this.getPrioritiesRepository()
				.findById(aPrioritySetId);

		aTracker.addPriorityToPrioritySet(aPrioritySet, aName, aValue);

	}

	/**
	 * Getter.
	 * 
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades al cual pertenecen las
	 *            prioridades que deben ser recuperadas.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultad.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @return la colección de dtos de las prioridades pertenecientes a un
	 *         conjunto de prioridades (PrioritySet).
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<PriorityDTO> getPriorities(String aPrioritySetId,
			String aPropertyName, boolean isAscending) throws Exception {

		Collection<Priority> somePriorities = this.getPrioritiesRepository()
				.findPrioritiesOfPrioritySet(aPrioritySetId, aPropertyName,
						isAscending);

		Collection<PriorityDTO> dtos = this.getDtoFactory()
				.createDTOForPriorities(somePriorities);

		return dtos;
	}

	/**
	 * Getter.
	 * 
	 * @param aPrioritySetName
	 *            es el nombre del conjunto de prioridades que se debe consultar
	 *            para ver la cantidad de elementos que contiene.
	 * @return la cantidad de prioridades que tiene el conjunto de prioridades
	 *         cuyo nombre se ha recibido.
	 * 
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public int getPrioritiesCount(String aPrioritySetName) throws Exception {
		PrioritySetDTO aPrioritySetDTO = this
				.findPrioritySetByName(aPrioritySetName);

		return aPrioritySetDTO.getPrioritiesCount();
	}

	/**
	 * Borra las prioridades representadas por los dtos recibidos del conjunto
	 * de prioridades cuyo id también se ha recibido.
	 * 
	 * @param prioritySetId
	 *            es el nombre del conjunto de prioridades del cual se deben
	 *            borrar las prioridades.
	 * @param selectedPriorities
	 *            es la colección de oid de prioridades que deben ser borradas.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deletePrioritiesOfPrioritySet(String prioritySetId,
			Collection<String> selectedPriorities) throws Exception {

		PrioritySet aPrioritySet = (PrioritySet) this.getPrioritiesRepository()
				.findById(prioritySetId);

		Collection<Priority> somePriorities = this.getPrioritiesRepository()
				.findPriorities(selectedPriorities);
		aPrioritySet.deletePriorities(somePriorities);
	}

	/**
	 * Recupera las prioridades del proyecto recibido.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto del cual se deben
	 *            recuperar las prioridades.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se desea ordernar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden ascendente o descendente.
	 * @return una colección que contiene los dtos de las prioridades asignadas
	 *         al proyecto.
	 */
	public Collection<PriorityDTO> findPrioritiesOfProject(
			ProjectDTO aProjectDTO, String aPropertyName, String anOrdering)
			throws Exception {

		Project aProject = this.getProjectsRepository().findById(
				aProjectDTO.getOid());

		Collection<Priority> priorities = this.getPrioritiesRepository()
				.findPrioritiesOfPrioritySet(
						aProject.getPrioritySet().getOid(), aPropertyName,
						anOrdering.equals("ASC"));

		Collection<PriorityDTO> dtos = this.getDtoFactory()
				.createDTOForPriorities(priorities);
		return dtos;
	}

	/**
	 * Elimina del sistema una colección de conjuntos de prioridades.
	 * 
	 * @param selectedPrioritySets
	 *            es una colección que contiene los identificadores de los
	 *            conjuntos de prioridades a ser eliminados.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deletePrioritySets(Collection<String> selectedPrioritySets)
			throws Exception {

		Tracker aTracker = this.getTrackerRepository().findTracker();
		PrioritiesRepositoryBI repository = this.getPrioritiesRepository();

		Collection<PrioritySet> prioritySets = repository
				.findPrioritySetsByIds(selectedPrioritySets);

		PrioritySet aPrioritySet = null;
		for (Iterator<PrioritySet> i = prioritySets.iterator(); i.hasNext();) {
			aPrioritySet = i.next();

			aTracker.deletePrioritySet(aPrioritySet);
			repository.delete(aPrioritySet);

		}

	}

	/**
	 * Recupera la cantidad de prioridades que tiene un conjunto de prioridades.
	 * 
	 * @param aPrioritySetId
	 *            es el identificador del conjunto de prioridades.
	 * @return un entero que representa la cantidad de prioridades que tiene el
	 *         conjunto.
	 */
	public int getPrioritiesCountOfPrioritySet(String aPrioritySetId) {
		int result = 0;
		try {
			PrioritySet aPrioritySet = this.getPrioritiesRepository().findById(
					aPrioritySetId);
			result = aPrioritySet.getPriorities().size();
		} catch (PrioritySetUnknownException e) {

		}
		return result;

	}

}
