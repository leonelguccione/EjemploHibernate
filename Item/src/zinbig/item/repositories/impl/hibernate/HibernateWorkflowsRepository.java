/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.hibernate.Query;

import zinbig.item.model.exceptions.WorkflowDescriptionUnknownException;
import zinbig.item.model.exceptions.WorkflowLinkDescriptionUnknownException;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionUnknownException;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.model.workflow.WorkflowLinkDescription;
import zinbig.item.model.workflow.WorkflowNode;
import zinbig.item.model.workflow.WorkflowNodeDescription;
import zinbig.item.repositories.bi.WorkflowsRepositoryBI;

/**
 * Esta clase implementa un repositorio de workflows y sus nodos asociados que
 * utiliza Hibernate para acceder en forma eficiente a las instancias de la
 * clases WorkflowDescription, WorkflowNodeDescription y WorkflowNode.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernateWorkflowsRepository extends HibernateBaseRepository
		implements WorkflowsRepositoryBI {

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
			throws WorkflowDescriptionUnknownException {

		WorkflowDescription result = (WorkflowDescription) this.findById(
				WorkflowDescription.class, anOid);

		if (result == null) {

			throw new WorkflowDescriptionUnknownException();

		}
		return result;

	}

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
			throws WorkflowNodeDescriptionUnknownException {
		Query aQuery = this
				.getNamedQuery("nodeDescriptionOfWorkflowNodeDescriptionQuery");

		aQuery.setParameter("anId", aWorkflowDescriptionOid);
		aQuery.setParameter("aTitle", aTitle);

		aQuery.setMaxResults(1);

		WorkflowNodeDescription node = null;

		node = (WorkflowNodeDescription) aQuery.uniqueResult();

		if (node == null) {
			throw new WorkflowNodeDescriptionUnknownException();
		} else {
			return node;
		}

	}

	/**
	 * Finder.
	 * 
	 * @param aTitle
	 *            es el título de la descripción de enlace que se está buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow en donde se
	 *            debe buscar.
	 * @return la descripción de enlace correspondiente a la descripción de
	 *         workflow cuyo id se ha recibido.
	 * 
	 * @throws WorkflowLinkDescriptionUnknownException
	 *             esta excepción puede levantarse si no se encuentra la
	 *             descripción de nodo que se busca.
	 */
	public WorkflowLinkDescription findWorkflowLinkDescriptionByTitleInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid)
			throws WorkflowLinkDescriptionUnknownException {
		Query aQuery = this
				.getNamedQuery("linkDescriptionOfWorkflowNodeDescriptionQuery");

		aQuery.setParameter("anId", aWorkflowDescriptionOid);
		aQuery.setParameter("aTitle", aTitle);

		aQuery.setMaxResults(1);

		WorkflowLinkDescription link = null;

		link = (WorkflowLinkDescription) aQuery.uniqueResult();

		if (link == null) {
			throw new WorkflowLinkDescriptionUnknownException();
		} else {
			return link;
		}

	}

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
			throws WorkflowDescriptionUnknownException {

		WorkflowDescription workflow = this.findById(workflowDescriptionOid);
		Collection<WorkflowNodeDescription> result = new TreeSet<WorkflowNodeDescription>(
				new Comparator<WorkflowNodeDescription>() {

					/**
					 * Ordena los resultados de acuerdo a su título.
					 * 
					 * @param aWorkflowNodeDescription
					 *            es el primero nodo de worfklow.
					 * @param anotherWorkflowNodeDescription
					 *            es el segundo nodo de workflow.
					 * @return un entero que representa el orden en el que se
					 *         tienen que poner los nodos.
					 */
					@Override
					public int compare(
							WorkflowNodeDescription aWorkflowNodeDescription,
							WorkflowNodeDescription anotherWorkflowNodeDescription) {
						return aWorkflowNodeDescription.getNodeTitle()
								.compareTo(
										anotherWorkflowNodeDescription
												.getNodeTitle());
					}
				});
		result.add(workflow.getInitialNodeDescription());
		result.addAll(workflow.getWorkflowNodeDescriptions());
		return result;
	}

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
	@SuppressWarnings("unchecked")
	public Collection<WorkflowNodeDescription> findWorkflowNodeDescriptionsById(
			Collection<String> selectedWorkflowNodeDescriptions) {
		Query aQuery = this.getNamedQuery("workflowNodeDescriptionsByIdsQuery");
		aQuery.setParameterList("aList", selectedWorkflowNodeDescriptions);

		Collection<WorkflowNodeDescription> result = aQuery.list();
		return result;

	}

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
	@SuppressWarnings("unchecked")
	public Collection<WorkflowNode> findReferencingWorkflowNodes(
			Collection<String> selectedWorkflowNodeDescriptions) {

		Query aQuery = this
				.getNamedQuery("workflowNodeReferencingDescriptionsByIdsQuery");
		aQuery.setParameterList("aList", selectedWorkflowNodeDescriptions);

		Collection<WorkflowNode> result = aQuery.list();
		return result;
	}

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
			throws WorkflowNodeDescriptionUnknownException {
		WorkflowNodeDescription result = (WorkflowNodeDescription) this
				.findById(WorkflowNodeDescription.class, anOid);

		if (result == null) {

			throw new WorkflowNodeDescriptionUnknownException();

		}
		return result;
	}

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
			String aTitle, WorkflowDescription aWorkflowDescription) {
		boolean result = false;

		try {
			result = this
					.findWorkflowLinkDescriptionByTitleInWorkflowDescription(
							aTitle, aWorkflowDescription.getOid()) != null;
		} catch (WorkflowLinkDescriptionUnknownException e) {

		}

		return result;
	}

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
	@SuppressWarnings("unchecked")
	public Collection<WorkflowLinkDescription> findWorkflowLinkDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid, String aPropertyName,
			boolean isAscending) {

		Query aQuery = this.getNamedQuery(
				"allWorkflowLinkDescriptionsOfWorkflowDescriptionQuery",
				aPropertyName, isAscending ? "ASC" : "DESC");

		aQuery.setParameter("anOid", workflowDescriptionOid);

		return aQuery.list();
	}

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
	@SuppressWarnings("unchecked")
	public Collection<WorkflowLinkDescription> findWorkflowLinkDescriptionsById(
			Collection<String> selectedWorkflowLinkDescriptions) {
		Query aQuery = this.getNamedQuery("workflowLinkDescriptionsByIdsQuery");
		aQuery.setParameterList("aList", selectedWorkflowLinkDescriptions);

		Collection<WorkflowLinkDescription> result = aQuery.list();
		return result;
	}

	/**
	 * Finder.
	 * 
	 * @param ssomeWorkflowNodeDescriptions
	 *            es una colección que contiene las descripciones de nodos
	 *            referenciadas.
	 * @return una colección de descripciones de links que tienen como origen o
	 *         fin a alguno de los nodos recibidos.
	 */
	@SuppressWarnings("unchecked")
	public Collection<WorkflowLinkDescription> findReferencingWorkflowLinkDescriptions(
			Collection<String> someWorkflowNodeDescriptions) {
		Query aQuery = this
				.getNamedQuery("workflowLinkDescriptionsReferencingWorkflowNodeDescriptions");
		aQuery.setParameterList("aList", someWorkflowNodeDescriptions);

		Collection<WorkflowLinkDescription> result = aQuery.list();
		return result;
	}

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
	@SuppressWarnings("unchecked")
	public Collection<WorkflowNodeDescription> findAdjacentWorkflowNodeDescriptionsOfWorkflowNodeDescription(
			WorkflowNodeDescription currentWorkflowNode, String aPropertyName,
			String anOrdering) {

		Collection<WorkflowNodeDescription> result = new ArrayList<WorkflowNodeDescription>();

		Query aQuery = this.getNamedQuery(
				"allAdjacentWorkflowNodeDescriptions", aPropertyName,
				anOrdering);

		aQuery.setParameter("anOid", currentWorkflowNode.getOid());

		result.addAll(aQuery.list());
		return result;
	}

	/**
	 * Finder.
	 * 
	 * @return una colección que contiene todas las descripciones de nodos
	 *         utilizadas en los proyectos.
	 */
	@SuppressWarnings("unchecked")
	public Collection<WorkflowNodeDescription> findAllWorkflowNodeDescriptionsOfProjects() {
		Collection<WorkflowNodeDescription> result = new ArrayList<WorkflowNodeDescription>();

		// agrega todos los nodos de los proyectos que no sean nodos iniciales
		Query aQuery = this
				.getNamedQuery("allWorkflowNodeDescriptionsOfProjects");

		result.addAll(aQuery.list());

		aQuery = this
				.getNamedQuery("allInitialWorkflowNodeDescriptionsOfProjects");

		result.addAll(aQuery.list());
		return result;
	}

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el oid del ítem para el cual se deben recuperar los estados
	 *            anteriores.
	 * @return una colección que contiene los estados anteriores por los cuales
	 *         pasó el ítem.
	 */
	@SuppressWarnings("unchecked")
	public Collection<WorkflowNode> findOldWorkflowNodesOfItem(String anOid) {
		Collection<WorkflowNode> result = new ArrayList<WorkflowNode>();

		Query aQuery = this.getNamedQuery("oldWorkflowNodesOfItem");

		aQuery.setParameter("anOid", anOid);

		result.addAll(aQuery.list());
		return result;
	}
}
