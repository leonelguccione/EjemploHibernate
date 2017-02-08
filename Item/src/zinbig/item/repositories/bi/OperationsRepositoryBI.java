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
 * Esta interface define el comportamiento que deber�an implementar todos los
 * repositorios (patr�n Repository) utilizados para acceder en forma eficiente a
 * las instancias de la clase Operation.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface OperationsRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Verifica si el objeto que representa al sistema contiene una operaci�n
	 * con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el rol en el que se debe buscar la operaci�n.
	 * @param aName
	 *            es el nombre de la operaci�n que se est� buscando.
	 * @return true en caso de que el sistema contenga una operaci�n con el
	 *         nombre dado; false en caso contrario.
	 */
	public boolean containsOperationWithName(Tracker aTracker, String aName);

	/**
	 * Finder.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema y que contiene la
	 *            colecci�n de operaciones disponibles.
	 * @param aName
	 *            es el nombre de la operaci�n que se est� buscando.
	 * @return la operaci�n con el nombre dado.
	 * @throws OperationUnknownException
	 *             esta excepci�n puede levantarse en caso de que el sistema no
	 *             contenga una operaci�n con el nombre dado.
	 */
	public Operation findOperationWithName(Tracker aTracker, String aName)
			throws OperationUnknownException;

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las operaciones que pueden ser
	 *         ejecutadas sin que el usuario se registre en la aplicaci�n.
	 */
	public Collection<Operation> findAnonymousOperations();

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las operaciones asignadas a un
	 *         usuario con el oid recibido.
	 */
	public Collection<Operation> findAllOperationsOfUser(String anUserOid);

	/**
	 * Finder.
	 * 
	 * @param mustListAdministrativeOperations
	 *            establece si se deben recuperar o no las operaciones
	 *            administrativas.
	 * @return una colecci�n que contiene todas las operaciones.
	 */
	public Collection<Operation> findAllOperations(
			boolean mustListAdministrativeOperations);

	/**
	 * Recupera una colecci�n de operaciones cuyos identificadores se han
	 * recibido.
	 * 
	 * @param operationDTOs
	 *            es una colecci�n que contiene los dtos que representan a las
	 *            operaciones.
	 * @return una colecci�n que contiene las operaciones.
	 */
	public Collection<Operation> findOperations(
			Collection<OperationDTO> operationDTOs);

	/**
	 * Recupera una operaci�n con el oid dado.
	 * 
	 * @param anOid
	 *            es el oid de la operaci�n que se est� buscando.
	 * @return la operaci�n con el id dado.
	 * @throws OperationUnknownException
	 *             esta excepci�n puede levantarse en caso de tratar de
	 *             recuperar una operaci�n que no existe.
	 */
	public Operation findById(String anOid) throws OperationUnknownException;

}
