/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.bi;

import java.util.Collection;

import zinbig.item.model.Operation;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.OperationUnknownException;
import zinbig.item.util.dto.OperationDTO;

/**
 * Esta interface define el comportamiento que deberían implementar todos los
 * repositorios (patrón Repository) utilizados para acceder en forma eficiente a
 * las instancias de la clase Operation.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface OperationsRepositoryBI extends ItemAbstractRepositoryBI {

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
	public boolean containsOperationWithName(Tracker aTracker, String aName);

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
	public Operation findOperationWithName(Tracker aTracker, String aName)
			throws OperationUnknownException;

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todas las operaciones que pueden ser
	 *         ejecutadas sin que el usuario se registre en la aplicación.
	 */
	public Collection<Operation> findAnonymousOperations();

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todas las operaciones asignadas a un
	 *         usuario con el oid recibido.
	 */
	public Collection<Operation> findAllOperationsOfUser(String anUserOid);

	/**
	 * Finder.
	 * 
	 * @param mustListAdministrativeOperations
	 *            establece si se deben recuperar o no las operaciones
	 *            administrativas.
	 * @return una colección que contiene todas las operaciones.
	 */
	public Collection<Operation> findAllOperations(
			boolean mustListAdministrativeOperations);

	/**
	 * Recupera una colección de operaciones cuyos identificadores se han
	 * recibido.
	 * 
	 * @param operationDTOs
	 *            es una colección que contiene los dtos que representan a las
	 *            operaciones.
	 * @return una colección que contiene las operaciones.
	 */
	public Collection<Operation> findOperations(
			Collection<OperationDTO> operationDTOs);

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
	public Operation findById(String anOid) throws OperationUnknownException;

}
