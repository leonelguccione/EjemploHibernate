/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deber�n implementar.<br>
 * 
 */
package zinbig.item.services.bi;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.WorkflowDescriptionDTO;
import zinbig.item.util.dto.WorkflowLinkDescriptionDTO;
import zinbig.item.util.dto.WorkflowNodeDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Esta interface define el protocolo de los servicios relacionados con los
 * workflows del sistema que deber�n ser implementados por clases concretas.<br>
 * Todo servicio en ning�n momento debe devolver un objeto de dominio, sino que
 * siempre devolver� DTOs.<br>
 * Por �ltimo, los servicios no contienen l�gica de negocios propiamente dicha
 * sino que son encargados de ejecutar dicha l�gica presente en el modelo de
 * dominio.<br>
 * Todos los m�todos de esta interface declaran el lanzamiento de excepciones.
 * Algunas ser�n excepciones de negocio y otras inesperadas (como la ca�da de la
 * conexi�n a la base de datos). La declaraci�n del lanzamiento tiene como
 * objetivo lograr que el cliente tenga que manejar excepciones cada vez que
 * invoque a un servicio. Si bien aqu� se declaran excepciones generales, en la
 * documentaci�n de cada m�todo tambi�n figura la excepci�n particular de
 * negocio que podr�a ser lanzada por la implementaci�n en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y adem�s en forma
 * particular alguna excepci�n de negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface WorkflowsServiceBI {

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene dtos que representan a todos las
	 *         descripciones de workflows creadas por el sistema.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowDescriptionDTO> getAllWorkflowDescriptions()
			throws Exception;

	/**
	 * Verifica si la descripci�n de workflow contiene una descripci�n de nodo
	 * con un t�tulo determinado.
	 * 
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @param workflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow.
	 * @return true en caso de que exista el t�tulo; false en caso contrario.
	 * 
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public boolean existsWorkflowNodeDescriptionInWorkflowDescription(
			String aTitle, String workflowDescriptionOid) throws Exception;

	/**
	 * Verifica si la descripci�n de workflow contiene una descripci�n de enlace
	 * con un t�tulo determinado.
	 * 
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @param workflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow.
	 * @return true en caso de que exista el t�tulo; false en caso contrario.
	 * 
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public boolean existsWorkflowLinkDescriptionInWorkflowDescription(
			String aTitle, String workflowDescriptionOid) throws Exception;

	/**
	 * Agrega una nueva descripi�n de nodo a la descripci�n de workflow.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la nueva descripci�n de nodo.
	 * @param isFinalNode
	 *            establece si esta nueva descripci�n de nodo es final o no.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow.
	 * @param usersIterator
	 *            es un iterador de todos los usuarios y grupos de usuarios
	 *            seleccionados como autorizados para el nuevo nodo.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a partir de la
	 *             ejecuci�n de este servicio.
	 */
	public void addWorkflowNodeDescription(String aTitle, boolean isFinalNode,
			String aWorkflowDescriptionOid,
			Iterator<AbstractUserDTO> usersIterator) throws Exception;

	/**
	 * Recupera las descripciones de nodos de una descripci�n de workflow.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow.
	 * @return una colecci�n que contiene los dtos que representan a las
	 *         descripciones de nodos de la descripci�n de workflow.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a partir de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getAllWorkflowNodeDescriptionOfWorkflowDescription(
			String workflowDescriptionOid) throws Exception;

	/**
	 * Elimina del sistema las descripciones de nodos cuyos oids se han
	 * recibido.
	 * 
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripci�n de nodo de la cual se deben
	 *            eliminar las descripciones de nodos.
	 * @param selectedWorkflowNodeDescriptions
	 *            es una colecci�n que contiene los oids de todos los elementos
	 *            que deben ser eliminados.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a partir de la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteWorkflowNodeDescriptions(String aWorkflowDescriptionOid,
			Collection<String> selectedWorkflowNodeDescriptions)
			throws Exception;

	/**
	 * Agrega un nuevo enlace entre dos descripciones de nodo.
	 * 
	 * @param aTitle
	 *            es el t�tulo del enlace.
	 * @param aWorkflowDescriptionDTO
	 *            es el dto que representa la descripci�n de workflow que se
	 *            est� editando.
	 * @param initialWorkflowNodeDescriptionDTO
	 *            es el dto que representa al nodo inicial del enlace.
	 * @param finalWorkflowNodeDescriptionDTO
	 *            es el dto que representa al nodo final del enlace.
	 * @param someItemTypes
	 *            es un iterador de una colecci�n que contiene los dtos de los
	 *            tipos de �tems seleccionados para el enlace.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public void addWorkflowLinkDescriptionToWorkflowDescription(String aTitle,
			WorkflowDescriptionDTO aWorkflowDescriptionDTO,
			WorkflowNodeDescriptionDTO initialWorkflowNodeDescriptionDTO,
			WorkflowNodeDescriptionDTO finalWorkflowNodeDescriptionDTO,
			Iterator<ItemTypeDTO> someItemTypes) throws Exception;

	/**
	 * Getter.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow para la cual hay que
	 *            recuperar todos los enlaces.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la que hay que ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @return una colecci�n que contiene los dtos que representan a todos los
	 *         enlaces de la descripci�n de workflow.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowLinkDescriptionDTO> getAllWorkflowLinkDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid, String aPropertyName,
			boolean isAscending) throws Exception;

	/**
	 * Elimina del workflow los enlaces recibidos.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow que se est� editando.
	 * @param selectedWorkflowLinkDescriptions
	 *            es una colecci�n que contiene los identificadores de los
	 *            enlaces que se deben eliminar.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */

	public void deleteWorflowLinkDescriptions(String workflowDescriptionOid,
			Collection<String> selectedWorkflowLinkDescriptions)
			throws Exception;

	/**
	 * Recupera todas las descripciones de nodo a la cual se puede llegar a
	 * partir de una descripci�n dada.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa el �tem que se est� moviendo. El
	 *            estado actual puede ser nulo, lo que significa que el �tem
	 *            est� en estado CREATED. En este caso se retorna solamente el
	 *            nodo inicial del workflow.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden del resultado.
	 * 
	 * @return una colecci�n que contiene las descripciones de nodo halladas.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowNodeDescriptionDTO> findNextWorkflowNodesForItem(
			ItemDTO anItemDTO, String aPropertyName, String anOrdering)
			throws Exception;

	/**
	 * Recupera todos los usuarios y grupos de usuarios autorizados en un nodo
	 * del workflow.
	 * 
	 * @param aWorkflowNodeDescriptionDTO
	 *            es el nodo del cual se deben recuperar los usuarios y grupos
	 *            de usuarios.
	 * @return una colecci�n que contiene todos los usuarios autorizados.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<AbstractUserDTO> getAllAuthorizedUsersOfWorkflowNodeDescription(
			WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO)
			throws Exception;

	/**
	 * Obtiene la cantidad de descripciones de links que tiene un workflow dado.
	 * 
	 * @param anOid
	 *            es el identificador del workflow que se est� consultando.
	 * @return un entero que representa la cantidad de links que tiene el
	 *         workflow.
	 */
	public int getWorkflowLinkDescriptionCountOfWorkflowDescription(String anOid);

	/**
	 * Obtiene la cantidad de descripciones de nodos que tiene un workflow dado.
	 * 
	 * @param anOid
	 *            es el identificador del workflow que se est� consultando.
	 * @return un entero que representa la cantidad de nodos que tiene el
	 *         workflow.
	 */
	public int getWorkflowNodeDescriptionCountOfWorkflowDescription(String anOid);

	/**
	 * Recupera todas descripciones de nodos utilizadas por los proyectos.
	 * 
	 * @return una colecci�n que contiene todos las descripciones de los nodos
	 *         utilizadas en los proyectos.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getAllWorkflowNodeDescriptionOfProjects();

	/**
	 * Recupera una descripci�n de nodo por su identificador.
	 * 
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripci�n de nodo que se debe
	 *            recuperar.
	 * @return la descripci�n de nodo que se est� buscando.
	 * @throws Exception
	 *             es cualquier excepci�n que se puede dar a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public WorkflowNodeDescriptionDTO findWorkflowNodeDescription(
			String aWorkflowNodeDescriptionOid) throws Exception;

	/**
	 * Edita la informaci�n de una descripci�n de nodo.
	 * 
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripci�n de nodo que se est�
	 *            editando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow a la que
	 *            pertenece al nodo que se est� editando.
	 * @param aTitle
	 *            es el nuevo t�tulo que se desea asignar.
	 * @param isFinalNode
	 *            establece si el nodo es final o no.
	 * @param someUsers
	 *            es una colecci�n que contiene los dtos de los usuarios y
	 *            grupos de usuarios seleccionados como posibles responsables de
	 *            los �tems en este nodo.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public void editWorkflowNodeDescription(String aWorkflowNodeDescriptionOid,
			String aWorkflowDescriptionOid, String aTitle, boolean isFinalNode,
			Iterator<AbstractUserDTO> someUsers) throws Exception;

	/**
	 * Recupera una lista de nodos que representan los estados anteriores por
	 * los que pas� el �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que pas� por los estados.
	 * @return una colecci�n de DTOs que representan los estados anteriores del
	 *         �tem.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse al ejecutar este
	 *             servicio.
	 */
	public Collection<WorkflowNodeDTO> getOldWorkflowNodesOfItem(
			ItemDTO anItemDTO) throws Exception;

}
