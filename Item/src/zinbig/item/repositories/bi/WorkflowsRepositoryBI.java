/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deberán respetar las implementaciones del patrón de diseño Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Collection;

import zinbig.item.model.exceptions.WorkflowDescriptionUnknownException;
import zinbig.item.model.exceptions.WorkflowLinkDescriptionUnknownException;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionUnknownException;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.model.workflow.WorkflowLinkDescription;
import zinbig.item.model.workflow.WorkflowNode;
import zinbig.item.model.workflow.WorkflowNodeDescription;

/**
 * Esta interface establece el protocolo estándar que deberá ser respetado por
 * todas las clases que implementen el patrón de diseño Repository para acceder
 * eficientemente a las instancias de la clase Workflow y sus clases asociadas.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface WorkflowsRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el identificador de la instancia de la clase Workflow que
	 *            se debe recuperar.
	 * @return la instancia con el identificador dado.
	 * @throws WorkflowDescriptionUnknownException
	 *             esta excepción puede ser levantada en caso de tratar de
	 *             recuperar una descripción de workflow con un identificador
	 *             inexistente.
	 */
	public WorkflowDescription findById(String anOid)
			throws WorkflowDescriptionUnknownException;

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el título de la descripción de nodo que se está buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow en donde se
	 *            debe buscar.
	 * @return la descripción de nodo correspondiente a la descripción de
	 *         workflow cuyo id se ha recibido.
	 * 
	 * @throws WorkflowNodeDescriptionUnknownException
	 *             esta excepción puede levantarse si no se encuentra la
	 *             descripción de nodo que se busca.
	 */
	public WorkflowNodeDescription findWorkflowNodeDescriptionByTitleInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid)
			throws WorkflowNodeDescriptionUnknownException;

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el título de la descripción del enlace que se está
	 *            buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow en donde se
	 *            debe buscar.
	 * @return la descripción de nodo correspondiente a la descripción de
	 *         workflow cuyo id se ha recibido.
	 * 
	 * @throws WorkflowNodeDescriptionUnknownException
	 *             esta excepción puede levantarse si no se encuentra la
	 *             descripción de nodo que se busca.
	 */
	public WorkflowLinkDescription findWorkflowLinkDescriptionByTitleInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid)
			throws WorkflowLinkDescriptionUnknownException;

	/**
	 * Finder.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripción de workflow.
	 * @return una colección que contiene las descripciones de nodo de una
	 *         descripción de workflow.
	 * @throws WorkflowDescriptionUnknownException
	 *             esta excepción puede ser levantada en caso de tratar de
	 *             recuperar una descripción de workflow con un identificador
	 *             inexistente.
	 */
	public Collection<WorkflowNodeDescription> findWorkflowNodeDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid)
			throws WorkflowDescriptionUnknownException;

	/**
	 * Recupera las descripciones de nodos cuyos oids se recibieron como
	 * parámetro.
	 * 
	 * @param selectedWorkflowNodeDescriptions
	 *            es la colección que contiene los oids de las descripciones de
	 *            nodos a recuperar.
	 * @return una colección de descripciones de nodos. Eventualmente esta
	 *         colección podría estar vacía.
	 */
	public Collection<WorkflowNodeDescription> findWorkflowNodeDescriptionsById(
			Collection<String> selectedWorkflowNodeDescriptions);

	/**
	 * Recupera los nodos de workflow cuya descripción de nodo corresponde a
	 * algún oid recibido.
	 * 
	 * @param selectedWorkflowNodeDescriptions
	 *            es una colección que contiene los oids de las descripciones de
	 *            nodos.
	 * @return una colección que contiene los nodos del workflow que referencian
	 *         a las descripciones de nodos.
	 */
	public Collection<WorkflowNode> findReferencingWorkflowNodes(
			Collection<String> selectedWorkflowNodeDescriptions);

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid de la descripción de nodo que se está buscando.
	 * @return una instancia de la clase WorkflowNodeDescription con el oid
	 *         dado.
	 * 
	 * @throws WorkflowNodeDescriptionUnknownException
	 *             esta excepción puede levantarse en caso de que no exista la
	 *             descripción de nodo que se está buscando.
	 */
	public WorkflowNodeDescription findWorkflowNodeDescriptionById(String anOid)
			throws WorkflowNodeDescriptionUnknownException;

	/**
	 * Verifica si existe una descripción de enlace con un título dado en una
	 * descripción de workflow.
	 * 
	 * 
	 * @param aTitle
	 *            es el título del nuevo enlace que se está buscando.
	 * @param aWorkflowDescription
	 *            es la descripción de workflow en donde se debe buscar.
	 * @return true en caso de que ya exista un enlace con el título dado, false
	 *         en caso contrario.
	 */
	public boolean existsWorkflowLinkDescriptionWithTitleInWorkflowDescription(
			String aTitle, WorkflowDescription aWorkflowDescription);

	/**
	 * Recupera todas las descripciones de enlace de una descripción de
	 * workflow.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripción de workflow para la cual se deben
	 *            recuperar todos los enlaces.
	 * @param aPropertyName
	 *            es la propiedad por la cual se debe ordenar el resultado.
	 * @param isAscending
	 *            establece el orden del listado.
	 * @return una colección de descripciones de enlaces.
	 */
	public Collection<WorkflowLinkDescription> findWorkflowLinkDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid, String aPropertyName,
			boolean isAscending);

	/**
	 * Recupera las descripciones de enlaces cuyos oids se recibieron como
	 * parámetro.
	 * 
	 * @param selectedWorkflowLinkDescriptions
	 *            es la colección que contiene los oids de las descripciones de
	 *            enlaces a recuperar.
	 * @return una colección de descripciones de enlaces. Eventualmente esta
	 *         colección podría estar vacía.
	 */
	public Collection<WorkflowLinkDescription> findWorkflowLinkDescriptionsById(
			Collection<String> selectedWorkflowLinkDescriptions);

	/**
	 * Finder.
	 * 
	 * @param ssomeWorkflowNodeDescriptions
	 *            es una colección que contiene las descripciones de nodos
	 *            referenciadas.
	 * @return una colección de descripciones de links que tienen como origen o
	 *         fin a alguno de los nodos recibidos.
	 */
	public Collection<WorkflowLinkDescription> findReferencingWorkflowLinkDescriptions(
			Collection<String> someWorkflowNodeDescriptions);

	/**
	 * Recupera todos los nodos adyacentes a una descripción dada.
	 * 
	 * @param currentWorkflowNode
	 *            es la descripción para la cual se deben recuperar sus vecinos.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden del resultado.
	 * @return una colección que contiene todas las descripciones vecinas.
	 */
	public Collection<WorkflowNodeDescription> findAdjacentWorkflowNodeDescriptionsOfWorkflowNodeDescription(
			WorkflowNodeDescription currentWorkflowNode, String aPropertyName,
			String anOrdering);

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todas las descripciones de nodos
	 *         utilizadas en los proyectos.
	 */
	public Collection<WorkflowNodeDescription> findAllWorkflowNodeDescriptionsOfProjects();

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del ítem para el cual se deben recuperar los estados
	 *            anteriores.
	 * @return una colección que contiene los estados anteriores por los cuales
	 *         pasó el ítem.
	 */
	public Collection<WorkflowNode> findOldWorkflowNodesOfItem(String anOid);

}
