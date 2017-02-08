/**
 * Este paquete contiene las implementaciones de los repositorios para hacer 
 * pruebas con mock objects.
 */
package zinbig.item.repositories.impl.mock;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.Operation;
import zinbig.item.model.Tracker;
import zinbig.item.repositories.bi.OperationsRepositoryBI;
import zinbig.item.util.dto.OperationDTO;

/**
 * Las instancias de esta clase representan mock objects que se utilizan para
 * realizar pruebas de unidad que no requieran conexiones de bases de datos por
 * ejemplo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MockOperationsRepository implements OperationsRepositoryBI {

	/**
	 * Constructor.
	 */
	public MockOperationsRepository() {

	}

	/**
	 * Finder. <br>
	 * Esta implementaci�n recorre directamente la colecci�n de operaciones del
	 * Tracker.
	 * 
	 * @param aTracker
	 *            es el objeto que contiene las operaciones.
	 * @param aName
	 *            es el nombre de la operaci�n que se est� buscando.
	 * @return la operaci�n que se est� buscando.
	 */
	@Override
	public Operation findOperationWithName(Tracker aTracker, String aName) {
		Operation op;
		Iterator<Operation> iterator = aTracker.getOperations().iterator();
		Operation result = null;

		while (iterator.hasNext()) {
			op = iterator.next();
			if (op.getName().equals(aName)) {
				result = op;
			}
		}
		return result;
	}

	/**
	 * Verifica si el objeto que representa al sistema contiene una operaci�n
	 * con un nombre dado.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aName
	 *            es el nombre de la operaci�n.
	 * 
	 * @return true en caso de que el sistema contenga una operaci�n con el
	 *         nombre dado; false en caso contrario.
	 */
	@Override
	public boolean containsOperationWithName(Tracker aTracker, String aName) {
		return this.findOperationWithName(aTracker, aName) != null;
	}

	/**
	 * M�todo presente por compatibilidad con el repositorio de Hibernate.
	 */
	@SuppressWarnings("unchecked")
	public Object findById(Class aClass, String anOId) {

		return null;
	}

	/**
	 * M�todo presente por compatibilidad con el repositorio de Hibernate.
	 */

	public Operation findById(String anOId) {

		return null;
	}

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las operaciones que puede ser
	 *         ejecutadas sin un usuario registrado.
	 */
	@Override
	public Collection<Operation> findAnonymousOperations() {

		return null;
	}

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las operaciones asignadas a un
	 *         usuario con el oid recibido.
	 */
	@Override
	public Collection<Operation> findAllOperationsOfUser(String anUserOid) {

		return null;
	}

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las operaciones que se pueden
	 *         ejecutar a nivel de sistema.
	 */
	public Collection<Operation> findAllSystemOperations() {
		return null;
	}

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las operaciones que se pueden
	 *         ejecutar a nivel de proyectos.
	 */
	public Collection<Operation> findAllProjectOperations() {
		return null;

	}

	@Override
	public Collection<Operation> findOperations(
			Collection<OperationDTO> operationDTOs) {

		return null;
	}

	@Override
	public Collection<Operation> findAllOperations(
			boolean mustListAdministrativeOperations) {

		return null;
	}

	@Override
	public void delete(Object anObject) {

	}

	@Override
	public void deleteObjects(Collection<? extends Object> someObjects) {

	}

}
