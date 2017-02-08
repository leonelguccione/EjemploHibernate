/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deberán implementar.<br>
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
 * workflows del sistema que deberán ser implementados por clases concretas.<br>
 * Todo servicio en ningún momento debe devolver un objeto de dominio, sino que
 * siempre devolverá DTOs.<br>
 * Por último, los servicios no contienen lógica de negocios propiamente dicha
 * sino que son encargados de ejecutar dicha lógica presente en el modelo de
 * dominio.<br>
 * Todos los métodos de esta interface declaran el lanzamiento de excepciones.
 * Algunas serán excepciones de negocio y otras inesperadas (como la caída de la
 * conexión a la base de datos). La declaración del lanzamiento tiene como
 * objetivo lograr que el cliente tenga que manejar excepciones cada vez que
 * invoque a un servicio. Si bien aquí se declaran excepciones generales, en la
 * documentación de cada método también figura la excepción particular de
 * negocio que podría ser lanzada por la implementación en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y además en forma
 * particular alguna excepción de negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface WorkflowsServiceBI {

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene dtos que representan a todos las
	 *         descripciones de workflows creadas por el sistema.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<WorkflowDescriptionDTO> getAllWorkflowDescriptions()
			throws Exception;

	/**
	 * Verifica si la descripción de workflow contiene una descripción de nodo
	 * con un título determinado.
	 * 
	 * @param aTitle
	 *            es el título que se está buscando.
	 * @param workflowDescriptionOid
	 *            es el identificador de la descripción de workflow.
	 * @return true en caso de que exista el título; false en caso contrario.
	 * 
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public boolean existsWorkflowNodeDescriptionInWorkflowDescription(
			String aTitle, String workflowDescriptionOid) throws Exception;

	/**
	 * Verifica si la descripción de workflow contiene una descripción de enlace
	 * con un título determinado.
	 * 
	 * @param aTitle
	 *            es el título que se está buscando.
	 * @param workflowDescriptionOid
	 *            es el identificador de la descripción de workflow.
	 * @return true en caso de que exista el título; false en caso contrario.
	 * 
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public boolean existsWorkflowLinkDescriptionInWorkflowDescription(
			String aTitle, String workflowDescriptionOid) throws Exception;

	/**
	 * Agrega una nueva descripión de nodo a la descripción de workflow.
	 * 
	 * @param aTitle
	 *            es el título de la nueva descripción de nodo.
	 * @param isFinalNode
	 *            establece si esta nueva descripción de nodo es final o no.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow.
	 * @param usersIterator
	 *            es un iterador de todos los usuarios y grupos de usuarios
	 *            seleccionados como autorizados para el nuevo nodo.
	 * @throws Exception
	 *             es cualquier excepción que podría levantarse a partir de la
	 *             ejecución de este servicio.
	 */
	public void addWorkflowNodeDescription(String aTitle, boolean isFinalNode,
			String aWorkflowDescriptionOid,
			Iterator<AbstractUserDTO> usersIterator) throws Exception;

	/**
	 * Recupera las descripciones de nodos de una descripción de workflow.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripción de workflow.
	 * @return una colección que contiene los dtos que representan a las
	 *         descripciones de nodos de la descripción de workflow.
	 * @throws Exception
	 *             es cualquier excepción que podría levantarse a partir de la
	 *             ejecución de este servicio.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getAllWorkflowNodeDescriptionOfWorkflowDescription(
			String workflowDescriptionOid) throws Exception;

	/**
	 * Elimina del sistema las descripciones de nodos cuyos oids se han
	 * recibido.
	 * 
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripción de nodo de la cual se deben
	 *            eliminar las descripciones de nodos.
	 * @param selectedWorkflowNodeDescriptions
	 *            es una colección que contiene los oids de todos los elementos
	 *            que deben ser eliminados.
	 * @throws Exception
	 *             es cualquier excepción que podría levantarse a partir de la
	 *             ejecución de este servicio.
	 */
	public void deleteWorkflowNodeDescriptions(String aWorkflowDescriptionOid,
			Collection<String> selectedWorkflowNodeDescriptions)
			throws Exception;

	/**
	 * Agrega un nuevo enlace entre dos descripciones de nodo.
	 * 
	 * @param aTitle
	 *            es el título del enlace.
	 * @param aWorkflowDescriptionDTO
	 *            es el dto que representa la descripción de workflow que se
	 *            está editando.
	 * @param initialWorkflowNodeDescriptionDTO
	 *            es el dto que representa al nodo inicial del enlace.
	 * @param finalWorkflowNodeDescriptionDTO
	 *            es el dto que representa al nodo final del enlace.
	 * @param someItemTypes
	 *            es un iterador de una colección que contiene los dtos de los
	 *            tipos de ítems seleccionados para el enlace.
	 * @throws Exception
	 *             es cualquier excepción que podría levantarse a raíz de la
	 *             ejecución de este servicio.
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
	 *            es el oid de la descripción de workflow para la cual hay que
	 *            recuperar todos los enlaces.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la que hay que ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @return una colección que contiene los dtos que representan a todos los
	 *         enlaces de la descripción de workflow.
	 * 
	 * @throws Exception
	 *             es cualquier excepción que podría levantarse a raíz de la
	 *             ejecución de este servicio.
	 */
	public Collection<WorkflowLinkDescriptionDTO> getAllWorkflowLinkDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid, String aPropertyName,
			boolean isAscending) throws Exception;

	/**
	 * Elimina del workflow los enlaces recibidos.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripción de workflow que se está editando.
	 * @param selectedWorkflowLinkDescriptions
	 *            es una colección que contiene los identificadores de los
	 *            enlaces que se deben eliminar.
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse a raíz de la
	 *             ejecución de este servicio.
	 */

	public void deleteWorflowLinkDescriptions(String workflowDescriptionOid,
			Collection<String> selectedWorkflowLinkDescriptions)
			throws Exception;

	/**
	 * Recupera todas las descripciones de nodo a la cual se puede llegar a
	 * partir de una descripción dada.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa el ítem que se está moviendo. El
	 *            estado actual puede ser nulo, lo que significa que el ítem
	 *            está en estado CREATED. En este caso se retorna solamente el
	 *            nodo inicial del workflow.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden del resultado.
	 * 
	 * @return una colección que contiene las descripciones de nodo halladas.
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse a raíz de la
	 *             ejecución de este servicio.
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
	 * @return una colección que contiene todos los usuarios autorizados.
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse a raíz de la
	 *             ejecución de este servicio.
	 */
	public Collection<AbstractUserDTO> getAllAuthorizedUsersOfWorkflowNodeDescription(
			WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO)
			throws Exception;

	/**
	 * Obtiene la cantidad de descripciones de links que tiene un workflow dado.
	 * 
	 * @param anOid
	 *            es el identificador del workflow que se está consultando.
	 * @return un entero que representa la cantidad de links que tiene el
	 *         workflow.
	 */
	public int getWorkflowLinkDescriptionCountOfWorkflowDescription(String anOid);

	/**
	 * Obtiene la cantidad de descripciones de nodos que tiene un workflow dado.
	 * 
	 * @param anOid
	 *            es el identificador del workflow que se está consultando.
	 * @return un entero que representa la cantidad de nodos que tiene el
	 *         workflow.
	 */
	public int getWorkflowNodeDescriptionCountOfWorkflowDescription(String anOid);

	/**
	 * Recupera todas descripciones de nodos utilizadas por los proyectos.
	 * 
	 * @return una colección que contiene todos las descripciones de los nodos
	 *         utilizadas en los proyectos.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getAllWorkflowNodeDescriptionOfProjects();

	/**
	 * Recupera una descripción de nodo por su identificador.
	 * 
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripción de nodo que se debe
	 *            recuperar.
	 * @return la descripción de nodo que se está buscando.
	 * @throws Exception
	 *             es cualquier excepción que se puede dar a raíz de la
	 *             ejecución de este servicio.
	 */
	public WorkflowNodeDescriptionDTO findWorkflowNodeDescription(
			String aWorkflowNodeDescriptionOid) throws Exception;

	/**
	 * Edita la información de una descripción de nodo.
	 * 
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripción de nodo que se está
	 *            editando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow a la que
	 *            pertenece al nodo que se está editando.
	 * @param aTitle
	 *            es el nuevo título que se desea asignar.
	 * @param isFinalNode
	 *            establece si el nodo es final o no.
	 * @param someUsers
	 *            es una colección que contiene los dtos de los usuarios y
	 *            grupos de usuarios seleccionados como posibles responsables de
	 *            los ítems en este nodo.
	 * 
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse a raíz de la
	 *             ejecución de este servicio.
	 */
	public void editWorkflowNodeDescription(String aWorkflowNodeDescriptionOid,
			String aWorkflowDescriptionOid, String aTitle, boolean isFinalNode,
			Iterator<AbstractUserDTO> someUsers) throws Exception;

	/**
	 * Recupera una lista de nodos que representan los estados anteriores por
	 * los que pasó el ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que pasó por los estados.
	 * @return una colección de DTOs que representan los estados anteriores del
	 *         ítem.
	 * 
	 * @throws Exception
	 *             es cualquier excepción que puede levantarse al ejecutar este
	 *             servicio.
	 */
	public Collection<WorkflowNodeDTO> getOldWorkflowNodesOfItem(
			ItemDTO anItemDTO) throws Exception;

}
