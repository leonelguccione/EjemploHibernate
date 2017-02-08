/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan pare representar la informaci�n
 * relevante de las descripciones de los enlaces del workflows.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowLinkDescriptionDTO extends ItemAbstractDTO implements
		Serializable {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 5899214378296150368L;

	/**
	 * Es el t�tulo de la descripci�n de nodo representada por este dto.
	 */
	public String title;

	/**
	 * Es el oid de la descripci�n de nodo representada por este dto.
	 */
	public String oid;

	/**
	 * Es una referencia al dto que representa al nodo inicial del enlace.
	 */
	public WorkflowNodeDescriptionDTO initialNode;

	/**
	 * Es una referencia al dto que representa al nodo final del enlace.
	 */
	public WorkflowNodeDescriptionDTO finalNode;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la descripci�n de enlace.
	 * 
	 * @param anOid
	 *            es el oid de la descripci�n de enlace.
	 */
	public WorkflowLinkDescriptionDTO(String aTitle, String anOid,
			WorkflowNodeDescriptionDTO initialNode,
			WorkflowNodeDescriptionDTO finalNode) {
		this.setTitle(aTitle);
		this.setFinalNode(finalNode);
		this.setInitialNode(initialNode);
		this.setOid(anOid);
	}

	/**
	 * Getter.
	 * 
	 * @return el t�tulo de la descripci�n de nodo.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la descripci�n de nodo.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return el oid de la descripci�n de nodo.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de la descripci�n de nodo.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al nodo inicial.
	 */
	public WorkflowNodeDescriptionDTO getInitialNode() {
		return this.initialNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aNodeDTO
	 *            es un dto que representa al nodo inicial.
	 */
	public void setInitialNode(WorkflowNodeDescriptionDTO aNodeDTO) {
		this.initialNode = aNodeDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al nodo final.
	 */
	public WorkflowNodeDescriptionDTO getFinalNode() {
		return this.finalNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aNodeDTO
	 *            es el dto que representa al nodo final.
	 */
	public void setFinalNode(WorkflowNodeDescriptionDTO aNodeDTO) {
		this.finalNode = aNodeDTO;
	}

}
