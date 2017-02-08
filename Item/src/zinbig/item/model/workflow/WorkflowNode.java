/**
 * Este paquete contiene las clases e interfaces requeridas para modelar la 
 * funcionalidad asociada a los workflows, sus nodos y links entre estos �ltimos.
 */
package zinbig.item.model.workflow;

import java.util.Date;

import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para representar el paso de un �tem
 * por un nodo del workflow.<br>
 * Cada instancia de esta clase responde a la estructura definida por la
 * correspondiente instancia de la clase WorkflowNodeDescription.<br>
 * Esta clase implementa la interface Cloneable para permitir copias de sus
 * instancias.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowNode implements Cloneable {

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Es el t�tulo de este nodo.
	 */
	public String title;

	/**
	 * Es la fecha de creaci�n de este nodo.
	 */
	public Date creationDate;

	/**
	 * Es la fecha de finalizaci�n de este nodo.
	 */
	public Date finishDate;

	/**
	 * Es el usuario ( o grupo de usuarios) actualmente responsable del �tem en
	 * este estado.
	 */
	public AbstractUser currentResponsible;

	/**
	 * Es el template a partir del cual se cre� este nodo.
	 */
	public WorkflowNodeDescription nodeDescription;

	/**
	 * Constructor.
	 * 
	 */
	public WorkflowNode() {
		this.setTitle("");
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el t�tulo de este nodo.
	 * @param anAbstractUser
	 *            es el usuario o grupo de usuarios responsable del �tem en este
	 *            estado.
	 * @param aWorkflowNodeDescription
	 *            es el template a partir del cual se cre� este nodo.
	 */
	public WorkflowNode(String aTitle, AbstractUser anAbstractUser,
			WorkflowNodeDescription aWorkflowNodeDescription) {
		this.setTitle(aTitle);
		this.setCurrentResponsible(anAbstractUser);
		this.setNodeDescription(aWorkflowNodeDescription);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return la identificaci�n de este objeto.
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
	 * @return el t�tulo de este nodo.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo de este nodo.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Notifica al receptor para que se desvincule de todos los objetos
	 * colaboradores que no deben ser eliminados en cascada.<br>
	 * Esta notificaci�n se recibe cuando se est� eliminando un �tem y se debe
	 * eliminar en cascada todo objeto colaborador de �ste.
	 * 
	 */
	public void prepareForDeletion() {
		this.setCurrentResponsible(null);
		this.setNodeDescription(null);

	}

	/**
	 * Getter.
	 * 
	 * @return el usuario o grupo de usuarios actualmente responsable del �tem
	 *         en este estado.
	 */
	public AbstractUser getCurrentResponsible() {
		return this.currentResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param anAbstractUser
	 *            es un usuario o grupo de usuarios responsables del �tem en
	 *            este estado.
	 */
	public void setCurrentResponsible(AbstractUser anAbstractUser) {
		this.currentResponsible = anAbstractUser;
	}

	/**
	 * Getter.
	 * 
	 * @return el template a partir del cual se cre� este nodo.
	 */
	public WorkflowNodeDescription getNodeDescription() {
		return this.nodeDescription;
	}

	/**
	 * Setter.
	 * 
	 * @param aWorkflowNodeDescription
	 *            es el template a partir del cual se cre� este nodo.
	 */
	public void setNodeDescription(
			WorkflowNodeDescription aWorkflowNodeDescription) {
		this.nodeDescription = aWorkflowNodeDescription;
	}

	/**
	 * Decrementa la cantidad de referencias que tiene la descripci�n de nodo de
	 * este nodo.
	 */
	public void decreaseReferencesCount() {
		this.getNodeDescription().decreaseReferencesCount();

	}

	/**
	 * Incrementa la cantidad de referencias que tiene la descripci�n de nodo de
	 * este nodo.
	 */
	public void increaseReferencesCount() {
		this.getNodeDescription().increaseReferencesCount();

	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creaci�n de este nodo.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creaci�n de este nodo.
	 */
	public void setCreationDate(Date aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de finalizaci�n de este nodo.
	 */
	public Date getFinishDate() {
		return this.finishDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de finalizaci�n de este nodo.
	 */
	public void setFinishDate(Date aDate) {
		this.finishDate = aDate;
	}

	/**
	 * Notifica al nodo de workflow para que elimine la referencia al usuario
	 * recibido.
	 * 
	 * @param anUser
	 *            es el usuario que se est� eliminando.
	 * @param projectLeader
	 *            es el l�der del proyecto en donde existe este nodo.
	 */
	public void deleteUser(User anUser, User projectLeader) {
		this.setCurrentResponsible(projectLeader);

	}

}
