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

import zinbig.item.model.Operation;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.OperationUnknownException;
import zinbig.item.repositories.bi.OperationsRepositoryBI;
import zinbig.item.util.dto.OperationDTO;

/**
 * Esta clase implementa un repositorio de operaciones que utiliza Hibernate
 * para acceder en forma eficiente a las instancias de la clase Operation.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernateOperationsRepository extends HibernateBaseRepository implements OperationsRepositoryBI {

	/**
	 * Finder.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema y que contiene la
	 *            colección de operaciones disponibles.
	 * @param aName
	 *            es el nombre de la operación que se está buscando.
	 * @return la operación con el nombre dado.
	 * @throws OperationUnknownException
	 *             esta excepción puede levantarse en caso de que el sistema no
	 *             contenga una operación con el nombre dado.
	 */
	@Override
	public Operation findOperationWithName(Tracker aTracker, String aName) throws OperationUnknownException {

		Query aQuery = this.getNamedQuery("operationsForTrackerQuery");

		aQuery.setParameter("aName", aName);
		aQuery.setParameter("anOid", aTracker.getOid());
		aQuery.setMaxResults(1);

		Operation result = (Operation) aQuery.uniqueResult();

		if (result == null) {
			throw new OperationUnknownException();
		} else {
			return result;
		}
	}

	/**
	 * Verifica si el objeto que representa al sistema contiene una operación
	 * con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el rol en el que se debe buscar la operación.
	 * @param aName
	 *            es el nombre de la operación que se está buscando.
	 * @return true en caso de que el sistema contenga una operación con el
	 *         nombre dado; false en caso contrario.
	 */
	@Override
	public boolean containsOperationWithName(Tracker aTracker, String aName) {
		boolean result = false;
		try {
			Operation anOperation = this.findOperationWithName(aTracker, aName);
			result = (anOperation != null);

		} catch (OperationUnknownException e) {
			// no se debe hacer nada
		}

		return result;
	}

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todas las operaciones que pueden ser
	 *         ejecutadas sin que el usuario se registre en la aplicación.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Operation> findAnonymousOperations() {

		Query aQuery = this.getNamedQuery("anonymousOperationsQuery");

		Collection<Operation> result = aQuery.list();

		return result;

	}

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todas las operaciones asignadas a un
	 *         usuario con el oid recibido.<br>
	 *         Las oepraciones anónimas no se recuperan en forma directa, sino
	 *         que se asume que fueron asignadas a los usuarios cuando se
	 *         crearon los grupos.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Operation> findAllOperationsOfUser(String anUserOid) {

		Query aQuery = this.getNamedQuery("allOperationsOfUserQuery");
		aQuery.setParameter("anOid", anUserOid);
		aQuery.setCacheable(true);
		Collection<Operation> result = aQuery.list();

		return result;

	}

	/**
	 * Finder.
	 * 
	 * @param mustListAdministrativeOperations
	 *            establece si se deben listar las operaciones administrativas.
	 * @return una colección que contiene todas las operaciones que se pueden
	 *         ejecutar a nivel de sistema.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Operation> findAllOperations(boolean mustListAdministrativeOperations) {
		Query aQuery = this.getNamedQuery("allOperationsQuery");
		aQuery.setParameter("aBoolean", mustListAdministrativeOperations);

		Collection<Operation> result = aQuery.list();

		return result;
	}

	/**
	 * Recupera una colección de operaciones cuyos identificadores se han
	 * recibido.
	 * 
	 * @param operationDTOs
	 *            es una colección que contiene los dtos que representan a las
	 *            operaciones.
	 * @return una colección que contiene las operaciones.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Operation> findOperations(Collection<OperationDTO> operationDTOs) {
		Collection<String> ids = new ArrayList<String>();

		Iterator<OperationDTO> iterator = operationDTOs.iterator();
		while (iterator.hasNext()) {
			ids.add(iterator.next().getOid());
		}

		Query aQuery = this.getNamedQuery("operationsByIdsQuery");
		aQuery.setParameterList("aList", ids);
		Collection<Operation> result = aQuery.list();
		return result;
	}

	/**
	 * Recupera una operación con el oid dado.
	 * 
	 * @param anOid
	 *            es el oid de la operación que se está buscando.
	 * @return la operación con el id dado.
	 * @throws OperationUnknownException
	 *             esta excepción puede levantarse en caso de tratar de
	 *             recuperar una operación que no existe.
	 */
	@Override
	public Operation findById(String anOid) throws OperationUnknownException {
		Operation result = (Operation) this.findById(Operation.class, anOid);

		if (result == null) {

			throw new OperationUnknownException();

		}
		return result;
	}

}
