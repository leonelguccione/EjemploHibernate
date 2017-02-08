/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase se utilizan pare representar la informaci�n
 * relevante de las descripciones de los nodos de workflows.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * Esta clase implementa la interface Versionable para poder controlar la
 * edici�n concurrente del nodo de workflow representado por este dto por parte
 * de dos usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowNodeDescriptionDTO extends ItemAbstractDTO implements
		Serializable, Versionable {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -6293928775129565447L;

	/**
	 * Es el t�tulo de la descripci�n de nodo representada por este dto.
	 */
	public String title;

	/**
	 * Es el oid de la descripci�n de nodo representada por este dto.
	 */
	public String oid;

	/**
	 * Es el la cantidad de referencias de la descripci�n de nodo representada
	 * por este dto.
	 */
	public int referencesCount;

	/**
	 * Define si la descripci�n de nodo representada por este dto es final o no.
	 */
	public boolean isFinalNode;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la descripci�n de nodo.
	 * @param referencesCount
	 *            es la cantidad de referencias de la descripci�n de nodo.
	 * @param isFinalNode
	 *            establece si la descripci�n de nodo es final o no.
	 * @param anOid
	 *            es el oid de la descripci�n de nodo.
	 */
	public WorkflowNodeDescriptionDTO(String aTitle, int referencesCount,
			boolean isFinalNode, String anOid) {
		this.setTitle(aTitle);
		this.setReferencesCount(referencesCount);
		this.setFinalNode(isFinalNode);
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
	 * @return la cantidad de referencias de la descripci�n de nodo.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de referencias de la descripci�n de nodo.
	 */
	public void setReferencesCount(int aCount) {
		this.referencesCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que sea una descripci�n de nodo final; false en
	 *         caso contrario.
	 */
	public boolean isFinalNode() {
		return this.isFinalNode;
	}

	/**
	 * Setter.
	 * 
	 * @param isFinalNode
	 *            define si la descripci�n de nodo es final.
	 */
	public void setFinalNode(boolean isFinalNode) {
		this.isFinalNode = isFinalNode;
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

}
