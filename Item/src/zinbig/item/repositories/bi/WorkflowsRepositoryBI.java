/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deber�n respetar las implementaciones del patr�n de dise�o Repository.
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
 * Esta interface establece el protocolo est�ndar que deber� ser respetado por
 * todas las clases que implementen el patr�n de dise�o Repository para acceder
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
	 *             esta excepci�n puede ser levantada en caso de tratar de
	 *             recuperar una descripci�n de workflow con un identificador
	 *             inexistente.
	 */
	public WorkflowDescription findById(String anOid)
			throws WorkflowDescriptionUnknownException;

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la descripci�n de nodo que se est� buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow en donde se
	 *            debe buscar.
	 * @return la descripci�n de nodo correspondiente a la descripci�n de
	 *         workflow cuyo id se ha recibido.
	 * 
	 * @throws WorkflowNodeDescriptionUnknownException
	 *             esta excepci�n puede levantarse si no se encuentra la
	 *             descripci�n de nodo que se busca.
	 */
	public WorkflowNodeDescription findWorkflowNodeDescriptionByTitleInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid)
			throws WorkflowNodeDescriptionUnknownException;

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la descripci�n del enlace que se est�
	 *            buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow en donde se
	 *            debe buscar.
	 * @return la descripci�n de nodo correspondiente a la descripci�n de
	 *         workflow cuyo id se ha recibido.
	 * 
	 * @throws WorkflowNodeDescriptionUnknownException
	 *             esta excepci�n puede levantarse si no se encuentra la
	 *             descripci�n de nodo que se busca.
	 */
	public WorkflowLinkDescription findWorkflowLinkDescriptionByTitleInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid)
			throws WorkflowLinkDescriptionUnknownException;

	/**
	 * Finder.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow.
	 * @return una colecci�n que contiene las descripciones de nodo de una
	 *         descripci�n de workflow.
	 * @throws WorkflowDescriptionUnknownException
	 *             esta excepci�n puede ser levantada en caso de tratar de
	 *             recuperar una descripci�n de workflow con un identificador
	 *             inexistente.
	 */
	public Collection<WorkflowNodeDescription> findWorkflowNodeDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid)
			throws WorkflowDescriptionUnknownException;

	/**
	 * Recupera las descripciones de nodos cuyos oids se recibieron como
	 * par�metro.
	 * 
	 * @param selectedWorkflowNodeDescriptions
	 *            es la colecci�n que contiene los oids de las descripciones de
	 *            nodos a recuperar.
	 * @return una colecci�n de descripciones de nodos. Eventualmente esta
	 *         colecci�n podr�a estar vac�a.
	 */
	public Collection<WorkflowNodeDescription> findWorkflowNodeDescriptionsById(
			Collection<String> selectedWorkflowNodeDescriptions);

	/**
	 * Recupera los nodos de workflow cuya descripci�n de nodo corresponde a
	 * alg�n oid recibido.
	 * 
	 * @param selectedWorkflowNodeDescriptions
	 *            es una colecci�n que contiene los oids de las descripciones de
	 *            nodos.
	 * @return una colecci�n que contiene los nodos del workflow que referencian
	 *         a las descripciones de nodos.
	 */
	public Collection<WorkflowNode> findReferencingWorkflowNodes(
			Collection<String> selectedWorkflowNodeDescriptions);

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid de la descripci�n de nodo que se est� buscando.
	 * @return una instancia de la clase WorkflowNodeDescription con el oid
	 *         dado.
	 * 
	 * @throws WorkflowNodeDescriptionUnknownException
	 *             esta excepci�n puede levantarse en caso de que no exista la
	 *             descripci�n de nodo que se est� buscando.
	 */
	public WorkflowNodeDescription findWorkflowNodeDescriptionById(String anOid)
			throws WorkflowNodeDescriptionUnknownException;

	/**
	 * Verifica si existe una descripci�n de enlace con un t�tulo dado en una
	 * descripci�n de workflow.
	 * 
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo enlace que se est� buscando.
	 * @param aWorkflowDescription
	 *            es la descripci�n de workflow en donde se debe buscar.
	 * @return true en caso de que ya exista un enlace con el t�tulo dado, false
	 *         en caso contrario.
	 */
	public boolean existsWorkflowLinkDescriptionWithTitleInWorkflowDescription(
			String aTitle, WorkflowDescription aWorkflowDescription);

	/**
	 * Recupera todas las descripciones de enlace de una descripci�n de
	 * workflow.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow para la cual se deben
	 *            recuperar todos los enlaces.
	 * @param aPropertyName
	 *            es la propiedad por la cual se debe ordenar el resultado.
	 * @param isAscending
	 *            establece el orden del listado.
	 * @return una colecci�n de descripciones de enlaces.
	 */
	public Collection<WorkflowLinkDescription> findWorkflowLinkDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid, String aPropertyName,
			boolean isAscending);

	/**
	 * Recupera las descripciones de enlaces cuyos oids se recibieron como
	 * par�metro.
	 * 
	 * @param selectedWorkflowLinkDescriptions
	 *            es la colecci�n que contiene los oids de las descripciones de
	 *            enlaces a recuperar.
	 * @return una colecci�n de descripciones de enlaces. Eventualmente esta
	 *         colecci�n podr�a estar vac�a.
	 */
	public Collection<WorkflowLinkDescription> findWorkflowLinkDescriptionsById(
			Collection<String> selectedWorkflowLinkDescriptions);

	/**
	 * Finder.
	 * 
	 * @param ssomeWorkflowNodeDescriptions
	 *            es una colecci�n que contiene las descripciones de nodos
	 *            referenciadas.
	 * @return una colecci�n de descripciones de links que tienen como origen o
	 *         fin a alguno de los nodos recibidos.
	 */
	public Collection<WorkflowLinkDescription> findReferencingWorkflowLinkDescriptions(
			Collection<String> someWorkflowNodeDescriptions);

	/**
	 * Recupera todos los nodos adyacentes a una descripci�n dada.
	 * 
	 * @param currentWorkflowNode
	 *            es la descripci�n para la cual se deben recuperar sus vecinos.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden del resultado.
	 * @return una colecci�n que contiene todas las descripciones vecinas.
	 */
	public Collection<WorkflowNodeDescription> findAdjacentWorkflowNodeDescriptionsOfWorkflowNodeDescription(
			WorkflowNodeDescription currentWorkflowNode, String aPropertyName,
			String anOrdering);

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todas las descripciones de nodos
	 *         utilizadas en los proyectos.
	 */
	public Collection<WorkflowNodeDescription> findAllWorkflowNodeDescriptionsOfProjects();

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del �tem para el cual se deben recuperar los estados
	 *            anteriores.
	 * @return una colecci�n que contiene los estados anteriores por los cuales
	 *         pas� el �tem.
	 */
	public Collection<WorkflowNode> findOldWorkflowNodesOfItem(String anOid);

}
