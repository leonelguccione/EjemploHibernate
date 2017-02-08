/**
 * Este paquete contiene las clases e interfaces requeridas para modelar la 
 * funcionalidad asociada a los workflows, sus nodos y links entre estos �ltimos.
 */
package zinbig.item.model.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.ItemType;
import zinbig.item.model.exceptions.WorkflowLinkDescriptionTitleNotUniqueException;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionTitleNotUniqueException;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionUnknownException;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.repositories.RepositoryLocator;
import zinbig.item.repositories.bi.WorkflowsRepositoryBI;
import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase se utilizan para modelar cada uno de los
 * circuitos que se crean en la herramienta y luego se van asignanco a los
 * diferentes proyectos.<br>
 * Una descripci�n de un workflow est� compuesta de descripciones de sus nodos y
 * enlaces entre �stos.<br>
 * <br>
 * Esta clase tiene las referencias tanto a las descripciones de los nodos
 * inicial y nulo, as� como a las instancias de estas descripciones, es decir el
 * nodo inicial y el nodo nulo, de modo de que todos los �tems apunten a un solo
 * nodo inicial y a un solo nodo nulo.<br>
 * Esta clase implementa la interfaz Versionable a fin de poder controlar la
 * edici�n concurrente por parte de dos usuarios.<br>
 * Esta clase implementa la interface Cloneable para permitir que los proyectos
 * que se crean y que seleccionan esta definici�n de workflow, luego la puedan
 * editar libremente sin afectar a otros proeyctos que seleccionaron la misma
 * definici�n inicial.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowDescription implements Versionable, Cloneable {

	/**
	 * Esta colecci�n contiene las descripciones de los nodos que componen esta
	 * instancia.
	 */
	protected Collection<WorkflowNodeDescription> workflowNodeDescriptions;

	/**
	 * Es una descripci�n de un nodo especial, reconocido como el �nico nodo por
	 * donde se puede comenzar el workflow.
	 */
	protected WorkflowNodeDescription initialNodeDescription;

	/**
	 * Es una colecci�n que contiene las descripciones de enlaces de este
	 * objeto.
	 */
	public Collection<WorkflowLinkDescription> workflowLinkDescriptions;
	/**
	 * Es el t�tulo de esta descripci�n de workflow.
	 */
	public String title;

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Mantiene la versi�n de este objeto.
	 */
	public int version;

	/**
	 * Constructor por defecto.
	 */
	protected WorkflowDescription() {
		this("", null);
	}

	/**
	 * Constructor.
	 * 
	 *@param aTitle
	 *            es el t�tulo de este workflow.
	 * @param anInitialNodeDescription
	 *            es la descripci�n del nodo inicial de este workflow.
	 * 
	 * 
	 */
	public WorkflowDescription(String aTitle,
			WorkflowNodeDescription anInitialNodeDescription) {
		this
				.setWorkflowNodeDescriptions(new ArrayList<WorkflowNodeDescription>());
		this.setInitialNodeDescription(anInitialNodeDescription);
		this
				.setWorkflowLinkDescriptions(new ArrayList<WorkflowLinkDescription>());
		this.setTitle(aTitle);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return la colecci�n de descripciones de nodos que componen a este
	 *         objeto.
	 */
	public Collection<WorkflowNodeDescription> getWorkflowNodeDescriptions() {
		return this.workflowNodeDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colecci�n que contiene las descripciones de los nodos
	 *            que componen a este objeto.
	 */
	public void setWorkflowNodeDescriptions(
			Collection<WorkflowNodeDescription> aCollection) {
		this.workflowNodeDescriptions = aCollection;
	}

	/**
	 * Getter.
	 * 
	 * @return la descripci�n del nodo inicial del workflow.
	 */
	public WorkflowNodeDescription getInitialNodeDescription() {
		return this.initialNodeDescription;
	}

	/**
	 * Setter.
	 * 
	 * @param initialNodeDescription
	 *            es la descripci�n del nodo inicial del workflow.
	 */
	private void setInitialNodeDescription(
			WorkflowNodeDescription initialNodeDescription) {
		this.initialNodeDescription = initialNodeDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de este objeto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @param anAbstractUser
	 *            es el usuario responsable del �tem en el nuevo estado que se
	 *            est� creando.
	 * 
	 * @return un nodo de workflow instanciado seg�n la configuraci�n
	 *         establecida en la descripci�n del nodo inicial.
	 */
	public WorkflowNode getInitialNode(AbstractUser anAbstractUser) {

		return this.getInitialNodeDescription().createNodeForUser(
				anAbstractUser);
	}

	/**
	 * Getter.
	 * 
	 * @return el t�tulo de esta descripci�n de workflow.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo de esta descripci�n de workflow.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Devuelve un string que representa al receptor.
	 * 
	 * @return "WorkflowDescription" + el t�tulo de esta descripci�n de
	 *         workflow.
	 */
	public String toString() {
		return "WorkflowDescription " + this.getTitle();
	}

	/**
	 * Getter.
	 * 
	 * @return la versi�n de este objeto.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es el n�mero de versi�n de esta instancia.
	 */
	public void setVersion(int aNumber) {
		this.version = aNumber;
	}

	/**
	 * Retorna una copia de este objeto.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		WorkflowDescription clone = (WorkflowDescription) super.clone();
		clone.setVersion(1);
		clone.setOid(IDGenerator.getId());
		clone.setInitialNodeDescription((WorkflowNodeDescription) this
				.getInitialNodeDescription().clone());

		clone
				.setWorkflowNodeDescriptions(new ArrayList<WorkflowNodeDescription>());

		Iterator<WorkflowNodeDescription> iterator = this
				.getWorkflowNodeDescriptions().iterator();

		while (iterator.hasNext()) {
			clone.getWorkflowNodeDescriptions().add(
					(WorkflowNodeDescription) iterator.next().clone());
		}

		clone
				.setWorkflowLinkDescriptions(new ArrayList<WorkflowLinkDescription>());

		Iterator<WorkflowLinkDescription> linkIterator = this
				.getWorkflowLinkDescriptions().iterator();

		while (linkIterator.hasNext()) {
			clone.getWorkflowLinkDescriptions().add(
					(WorkflowLinkDescription) linkIterator.next().clone());
		}

		return clone;
	}

	/**
	 * Agrega una nueva descripci�n de nodo a esta descripci�n de workflow.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la nueva descripci�n de nodo.
	 * @param isFinalNode
	 *            establece si la nueva descripci�n de nodo es final o nol.
	 * @param someUsers
	 *            es una colecci�n que contiene usuarios y grupos de usuarios
	 *            autorizados para el nuevo nodo.
	 */
	public void addWorkflowNodeDescription(String aTitle, boolean isFinalNode,
			Collection<AbstractUser> someUsers) {
		WorkflowNodeDescription newNode = new WorkflowNodeDescription(aTitle,
				isFinalNode, 0);
		newNode.setAuthorizedUsers(someUsers);

		this.getWorkflowNodeDescriptions().add(newNode);

	}

	/**
	 * Elimina del modelo las descripciones de nodos recibidas.
	 * 
	 * @param someNodeDescriptions
	 *            es la colecci�n que contiene las descripciones de nodos que se
	 *            deben eliminar.
	 * @param someNodes
	 *            es una coelcci�n que contiene los nodos que referencian a las
	 *            descripciones de nodos recibidas.
	 * @param someLinkDescriptions
	 *            es una colecci�n que contiene las descripciones de los enlaces
	 *            que referencian a las descripciones de nodo que se est�n
	 *            eliminando.
	 */
	public void deleteWorkflowNodeDescriptions(
			Collection<WorkflowNodeDescription> someNodeDescriptions,
			Collection<WorkflowNode> someNodes,
			Collection<WorkflowLinkDescription> someLinkDescriptions) {

		// para cada uno de los nodos se asigna nulo a su definici�n de nodo.
		// estos nodos deber�an ser en teor�a solamente nodos viejos.
		Iterator<WorkflowNode> nodeIterator = someNodes.iterator();
		while (nodeIterator.hasNext()) {
			nodeIterator.next().setNodeDescription(null);
		}

		Iterator<WorkflowLinkDescription> linkIterator = someLinkDescriptions
				.iterator();
		WorkflowLinkDescription aLinkDescription = null;
		while (linkIterator.hasNext()) {
			aLinkDescription = linkIterator.next();
			aLinkDescription.prepareForDeletion();
			this.getWorkflowLinkDescriptions().remove(aLinkDescription);
		}

		// notifica a cada descripci�n de nodo que se la va a eliminar
		Iterator<WorkflowNodeDescription> iterator = someNodeDescriptions
				.iterator();
		WorkflowNodeDescription aNodeDescription = null;
		while (iterator.hasNext()) {
			aNodeDescription = iterator.next();
			aNodeDescription.prepareForDeletion();
			this.getWorkflowNodeDescriptions().remove(aNodeDescription);
		}

	}

	/**
	 * Agrega a esta descrici�n de workflow una nueva descripci�n de enlace
	 * entre dos nodos.
	 * 
	 * @param aTitle
	 *            es el t�tulo de este nuevo enlace.
	 * @param initialNode
	 *            es el nodo inicial del enlace.
	 * @param finalNode
	 *            es el nodo final del enlace.
	 * @param itemTypes
	 *            es una colecci�n que contiene los tipos de �tems para los
	 *            cuales el enlace es v�lido.
	 * 
	 * @throws WorkflowLinkDescriptionTitleNotUniqueException
	 *             esta excepci�n puede levantarse en caso de que se est�
	 *             agregando a esta descripci�n de workflow un enlace con un
	 *             t�tulo ya utilizado.
	 */
	public void addWorkflowLinkDescription(String aTitle,
			WorkflowNodeDescription initialNode,
			WorkflowNodeDescription finalNode, Collection<ItemType> itemTypes)
			throws WorkflowLinkDescriptionTitleNotUniqueException {

		if (this.getWorkflowsRepository()
				.existsWorkflowLinkDescriptionWithTitleInWorkflowDescription(
						aTitle, this)) {
			throw new WorkflowLinkDescriptionTitleNotUniqueException();
		} else {
			WorkflowLinkDescription link = new WorkflowLinkDescription();
			link.setTitle(aTitle);
			link.setFinalNodeDescription(finalNode);
			link.setInitialNodeDescription(initialNode);
			initialNode.addWorkflowLinkDescription(link);
			link.setItemTypes(itemTypes);
			this.getWorkflowLinkDescriptions().add(link);

		}

	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene las descripciones de los enlaces.
	 */
	public Collection<WorkflowLinkDescription> getWorkflowLinkDescriptions() {
		return this.workflowLinkDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param someWorkflowLinkDescriptions
	 *            es una colecci�n que contiene las descripciones de los
	 *            enlaces.
	 */
	public void setWorkflowLinkDescriptions(
			Collection<WorkflowLinkDescription> someWorkflowLinkDescriptions) {
		this.workflowLinkDescriptions = someWorkflowLinkDescriptions;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n de dise�o Repository que este
	 *         objeto utiliza para acceder en forma eficiente a su colecci�n de
	 *         usuarios.
	 */
	private WorkflowsRepositoryBI getWorkflowsRepository() {
		return RepositoryLocator.getInstance().getWorkflowsRepository();
	}

	/**
	 * Elimina del modelo las descripciones de enlaces recibidas.
	 * 
	 * @param someLinkDescriptions
	 *            es la colecci�n que contiene las descripciones de enlaces que
	 *            se deben eliminar.
	 */
	public void deleteWorkflowLinkDescriptions(
			Collection<WorkflowLinkDescription> someLinkDescriptions) {

		// para cada uno de los enlace se asigna nulo a sus nodos iniciales y
		// finales
		Iterator<WorkflowLinkDescription> linkIterator = someLinkDescriptions
				.iterator();
		WorkflowLinkDescription aLink = null;
		while (linkIterator.hasNext()) {
			aLink = linkIterator.next();
			aLink.prepareForDeletion();
			aLink.getInitialNodeDescription().removeWorkflowLinkDescription(
					aLink);
		}
		this.getWorkflowLinkDescriptions().removeAll(someLinkDescriptions);

	}

	/**
	 * Notifica al receptor que se prepare para ser eliminado.
	 */
	public void prepareForDeletion() {

		Iterator<WorkflowLinkDescription> linksIterator = this
				.getWorkflowLinkDescriptions().iterator();
		while (linksIterator.hasNext()) {
			linksIterator.next().prepareForDeletion();
		}

		Iterator<WorkflowNodeDescription> nodesIterator = this
				.getWorkflowNodeDescriptions().iterator();
		while (nodesIterator.hasNext()) {
			nodesIterator.next().prepareForDeletion();
		}

		// el nodo inicial no se toca as� se elimina en cascada con este
		// workflow.

	}

	/**
	 * Borra el tipo de �tem de todos las descripciones de enlace.
	 * 
	 * @param anItemType
	 *            es el tipo de �tem que se debe remover.
	 */
	public void deleteItemType(ItemType anItemType) {
		Iterator<WorkflowLinkDescription> iterator = this
				.getWorkflowLinkDescriptions().iterator();
		WorkflowLinkDescription aLink = null;

		while (iterator.hasNext()) {
			aLink = iterator.next();
			aLink.getItemTypes().remove(anItemType);
		}

	}

	/**
	 * Borra al usuario recibido de las definiciones de nodo de este workflow.
	 * 
	 * @param anUser
	 *            es el usuario que debe ser eliminado.
	 */
	public void deleteUser(User anUser) {
		Iterator<WorkflowNodeDescription> iterator = this
				.getWorkflowNodeDescriptions().iterator();
		WorkflowNodeDescription aNode = null;

		while (iterator.hasNext()) {
			aNode = iterator.next();
			aNode.getAuthorizedUsers().remove(anUser);
		}

	}

	/**
	 * Agrega como usuarios autorizados del nodo inicial al grupo recibido.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que debe ser agregado como usuario
	 *            autorizado en el nodo inicial.
	 */
	public void addInitialNodeAuthorizedUsers(UserGroup anUserGroup) {
		this.getInitialNodeDescription().addAuthorizedUser(anUserGroup);

	}

	/**
	 * Remueve un grupo de usuarios de la lista de usuarios autorizados de los
	 * nodos.<BR>
	 * Si este grupo de usuarios adem�s agreg� usuarios individuales, �stos no
	 * se retiran.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuario a remover.
	 */
	public void removeUserGroup(UserGroup anUserGroup) {
		Iterator<WorkflowNodeDescription> iterator = this
				.getWorkflowNodeDescriptions().iterator();
		WorkflowNodeDescription aNodeDescription = null;

		while (iterator.hasNext()) {
			aNodeDescription = iterator.next();
			aNodeDescription.removeAuthorizedUser(anUserGroup);
		}

		this.getInitialNodeDescription().removeAuthorizedUser(anUserGroup);

	}

	/**
	 * Edita la informaci�n de una descripci�n de nodo de este workflow.
	 * 
	 * @param aWorkflowNodeDescription
	 *            es la descripci�n de nodo de este workflow que se est�
	 *            editando.
	 * @param aTitle
	 *            es el nuevo t�tulo que se desea asignar.
	 * @param isFinalNode
	 *            establece si el nodo es final o no.
	 * @param someUsers
	 *            es una colecci�n que contiene los usuarios y grupos de
	 *            usuarios asignados a este nodo.
	 * 
	 * @throws WorkflowNodeDescriptionTitleNotUniqueException
	 *             esta excepci�n puede levantarse en caso de querer editar el
	 *             nodo con un t�tulo ya existente.
	 */
	public void editWorkflowNodeDescription(
			WorkflowNodeDescription aWorkflowNodeDescription, String aTitle,
			boolean isFinalNode, Collection<AbstractUser> someUsers)
			throws WorkflowNodeDescriptionTitleNotUniqueException {

		if (!aWorkflowNodeDescription.getNodeTitle().equals(aTitle)) {
			// no es el mismo t�tulo que ya tiene asi que se debe verificar su
			// validez.
			WorkflowNodeDescription anotherNode = null;

			try {
				this
						.getWorkflowsRepository()
						.findWorkflowNodeDescriptionByTitleInWorkflowDescription(
								aTitle, this.getOid());
			} catch (WorkflowNodeDescriptionUnknownException e) {

			}

			if (anotherNode != null) {
				// ya existe este t�tulo
				throw new WorkflowNodeDescriptionTitleNotUniqueException();
			}
		}
		aWorkflowNodeDescription.setNodeTitle(aTitle);
		aWorkflowNodeDescription.setFinalNode(isFinalNode);
		aWorkflowNodeDescription.getAuthorizedUsers().clear();
		aWorkflowNodeDescription.getAuthorizedUsers().addAll(someUsers);

	}

	/**
	 * Verifica si existe un camino (aunque sea indirecto) entre dos
	 * descripciones de nodos dados.
	 * 
	 * @param originNode
	 *            es el nodo origen.
	 * @param destinationNode
	 *            es el nodo destino.
	 * @param anItemType
	 *            es un tipo de �tem para el cual se debe verificar el camino.
	 * @return true en caso de que exista una manera de llegar desde le origen
	 *         al destino; false en caso contrario.
	 */
	public boolean verifyPathBetweenNodes(WorkflowNodeDescription originNode,
			WorkflowNodeDescription destinationNode, ItemType anItemType) {
		Collection<WorkflowNodeDescription> visited = new ArrayList<WorkflowNodeDescription>();

		return originNode.hasALinkToWorkflowNodeDescription(destinationNode,
				anItemType, visited);
	}

}
