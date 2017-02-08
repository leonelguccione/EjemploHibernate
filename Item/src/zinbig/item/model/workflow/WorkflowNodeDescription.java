/**
 * Este paquete contiene las clases e interfaces requeridas para modelar la 
 * funcionalidad asociada a los workflows, sus nodos y links entre estos �ltimos.
 */
package zinbig.item.model.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.ItemType;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.UserGroup;
import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase se utilizan para describir las caracter�sticas
 * que deber�n tener las instancias de los nodos del workflow. <br>
 * Es una implementaci�n del patr�n de dise�o TypeObject. <br>
 * Las descripciones de nodo que sean finales no pueden tener enlaces a otras
 * descripciones.
 * 
 * Esta clase implementa la interface Cloneable para permitir copias de sus
 * instancias.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowNodeDescription implements Cloneable, Versionable {

	/**
	 * Mantiene el t�tulo que deber� asignarse al nodo descripto por este
	 * objeto.
	 */
	public String nodeTitle;

	/**
	 * Define si esta descripci�n de nodo es final o no.
	 */
	public boolean finalNode;

	/**
	 * Es el identificador de esta instancia.
	 */
	public String oid;

	/**
	 * Mantiene la cantidad de �tems que est�n en un nodo cuya descripci�n es
	 * esta instancia.
	 */
	public int referencesCount;

	/**
	 * Mantiene la versi�n de este objeto.
	 */
	public int version;

	/**
	 * Es una colecci�n de usuarios (grupos y usuarios individuales) que est�n
	 * autorizados a ser responsables de un �tem en un nodo dado.
	 */
	public Collection<AbstractUser> authorizedUsers;

	/**
	 * Es una coelcci�n que contiene todos las descripciones de los enlaces que
	 * salen de esta descripci�n de nodo.
	 */
	public Collection<WorkflowLinkDescription> workflowLinks;

	/**
	 * Constructor.
	 * 
	 */
	public WorkflowNodeDescription() {
		this("", false, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param aNodeTitle
	 *            es el t�tulo del nodo descripto por este objeto.
	 * @param isFinalNode
	 *            establece si esta descripci�n de nodo es final o no.
	 * @param aCount
	 *            es la cantidad de referencias de esta descripci�n de estado.
	 */
	public WorkflowNodeDescription(String aNodeTitle, boolean isFinalNode,
			int aCount) {
		this.setNodeTitle(aNodeTitle);
		this.setFinalNode(isFinalNode);
		this.setOid(IDGenerator.getId());
		this.setReferencesCount(aCount);
		this.setAuthorizedUsers(new ArrayList<AbstractUser>());
		this.setWorkflowLinks(new ArrayList<WorkflowLinkDescription>());
	}

	/**
	 * Construye un nodo de workflow de acuerdo a las especificaciones
	 * establecidas en esta instancia.
	 * 
	 * @param anAbstractUser
	 *            es el usuario responsable del nodo en este nuevo estado.
	 * 
	 * @return el nuevo nodo del workflow.
	 */
	public WorkflowNode createNodeForUser(AbstractUser anAbstractUser) {
		return new WorkflowNode(this.getNodeTitle(), anAbstractUser, this);
	}

	/**
	 * Getter.
	 * 
	 * @return el t�tulo del nodo.
	 */
	public String getNodeTitle() {
		return this.nodeTitle;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            el t�tulo del nodo.
	 */
	public void setNodeTitle(String aTitle) {
		this.nodeTitle = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return el oid de esta instancia.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de esta instancia.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Obtiene una copia de este objeto.
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		WorkflowNodeDescription clone = (WorkflowNodeDescription) super.clone();

		clone.setOid(IDGenerator.getId());
		clone.setReferencesCount(0);

		// clona los usuarios autorizados
		clone.setAuthorizedUsers(new ArrayList<AbstractUser>());
		Iterator<AbstractUser> iterator = this.getAuthorizedUsers().iterator();
		while (iterator.hasNext()) {
			clone.getAuthorizedUsers().add(
					(AbstractUser) iterator.next().clone());
		}

		// clona las descripciones de los enlaces
		clone.setWorkflowLinks(new ArrayList<WorkflowLinkDescription>());
		Iterator<WorkflowLinkDescription> wIterator = this.getWorkflowLinks()
				.iterator();
		while (wIterator.hasNext()) {
			clone.getWorkflowLinks().add(
					(WorkflowLinkDescription) wIterator.next().clone());
		}

		return clone;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que esta descripci�n de nodo sea final; false en
	 *         caso contrario.
	 */
	public boolean isFinalNode() {
		return this.finalNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si esta descripci�n de nodo es final o no.
	 */
	public void setFinalNode(boolean aBoolean) {
		this.finalNode = aBoolean;
	}

	/**
	 * Representa a este objeto como un string.
	 * 
	 * @return el oid de esta instancia.
	 */
	@Override
	public String toString() {
		return "WND " + this.getOid();
	}

	/**
	 * Decrementa la cantidad de referencias de esta instancia.
	 */
	public void decreaseReferencesCount() {
		this.setReferencesCount(this.getReferencesCount() - 1);

	}

	/**
	 * Incrementa la cantidad de referencias de esta instancia.
	 */
	public void increaseReferencesCount() {
		this.setReferencesCount(this.getReferencesCount() + 1);

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de �tems que referencian a esta instancia.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de �tems que referencian a esta instancia.
	 */
	public void setReferencesCount(int aCount) {
		this.referencesCount = aCount;
	}

	/**
	 * Notifica al receptor que se prepare para ser eliminado. <br>
	 * Prepararse para ser eliminado significa romper todos los v�nculos
	 * existentes con los objetos que no ser�n eliminados.
	 */
	public void prepareForDeletion() {
		// rompe los v�nculos con los usuarios y los grupos de usuarios
		this.setAuthorizedUsers(null);
		this.setWorkflowLinks(null);
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
	 * Getter.
	 * 
	 * @return la colecci�n de usuarios autorizados a ser responsables de un
	 *         �tem en este nodo.
	 */
	public Collection<AbstractUser> getAuthorizedUsers() {
		return this.authorizedUsers;
	}

	/**
	 * Setter.
	 * 
	 * @param someUsers
	 *            es la colecci�n de usuarios autorizados a ser responsables de
	 *            un �tem en este nodo.
	 */
	public void setAuthorizedUsers(Collection<AbstractUser> someUsers) {
		this.authorizedUsers = someUsers;
	}

	/**
	 * Agrega al grupo de usuarios y a cada usuario individual como usuario
	 * autorizado de esta descripci�n de nodo.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que debe ser autorizado.
	 */
	public void addAuthorizedUser(UserGroup anUserGroup) {

		this.getAuthorizedUsers().add(anUserGroup);

		this.getAuthorizedUsers().addAll(anUserGroup.getUsers());
	}

	/**
	 * Elimina un grupo de usuarios de este nodo.
	 * 
	 * @param anUserGroup
	 *            es el grupo que se debe eliminar.
	 */
	public void removeAuthorizedUser(UserGroup anUserGroup) {
		this.getAuthorizedUsers().remove(anUserGroup);

	}

	/**
	 * Getter.
	 * 
	 * @return las descripciones de los enlaces que tienen a esta descripci�n de
	 *         nodo como inicio.
	 */
	public Collection<WorkflowLinkDescription> getWorkflowLinks() {
		return this.workflowLinks;
	}

	/**
	 * Setter.
	 * 
	 * @param someWorkflowLinkDescriptions
	 *            es las descripciones de los enlaces que tienen a esta
	 *            descripci�n de nodo como inicio.
	 */
	public void setWorkflowLinks(
			Collection<WorkflowLinkDescription> someWorkflowLinkDescriptions) {
		this.workflowLinks = someWorkflowLinkDescriptions;
	}

	/**
	 * Agrega una nueva descripci�n de enlace a esta descripci�n de nodo.
	 * 
	 * @param aLink
	 *            es la nueva descripci�n que se debe agregar.
	 */
	public void addWorkflowLinkDescription(WorkflowLinkDescription aLink) {
		this.getWorkflowLinks().add(aLink);

	}

	/**
	 * Remueve una descripci�n de enlace de esta descripci�n de nodo.
	 * 
	 * @param aLink
	 *            es la descripci�n de enlace que se debe eliminar.
	 */
	public void removeWorkflowLinkDescription(WorkflowLinkDescription aLink) {
		this.getWorkflowLinks().remove(aLink);

	}

	/**
	 * Verifica si esta descripci�n de nodo tiene un enlace que lo conecte (a�n
	 * en forma indirecta) con la descripci�n de nodo destino.
	 * 
	 * @param destinationNode
	 *            es la descripci�n de nodo a la cual se debe llevar.
	 * @param anItemType
	 *            es el tipo de �tem para el cual se debe verificar la
	 *            existencia del camino.
	 * @param visited
	 *            es una colecci�n que se utiliza para guardar los nodos vecinos
	 *            ya visitados.
	 * @return true en caso de que exista un enlace entre los nodos recibidos;
	 *         false en caso contrario.
	 */
	public boolean hasALinkToWorkflowNodeDescription(
			WorkflowNodeDescription destinationNode, ItemType anItemType,
			Collection<WorkflowNodeDescription> visited) {

		boolean hasLink = false;

		if (!visited.contains(this)) {
			visited.add(this);
			Iterator<WorkflowLinkDescription> linksIterator = this
					.getWorkflowLinks().iterator();
			WorkflowLinkDescription aLink = null;

			// en alguno de los links de este nodo ya encontr� el nodo destino
			while (linksIterator.hasNext() && !hasLink) {
				aLink = (WorkflowLinkDescription) linksIterator.next();

				if (aLink.containsItemType(anItemType)
						&& aLink.getFinalNodeDescription().equals(
								destinationNode)) {
					hasLink = true;
				}
			}

			// no encontr� el nodo destino en mis vecinos, mando a todos mis
			// vecionos a buscarlo entonces
			if (!hasLink) {
				linksIterator = this.getWorkflowLinks().iterator();

				while (linksIterator.hasNext() && !hasLink) {
					aLink = (WorkflowLinkDescription) linksIterator.next();
					hasLink = hasLink
							| aLink.getFinalNodeDescription()
									.hasALinkToWorkflowNodeDescription(
											destinationNode, anItemType,
											visited);
				}

			}
		}

		return hasLink;
	}
}
