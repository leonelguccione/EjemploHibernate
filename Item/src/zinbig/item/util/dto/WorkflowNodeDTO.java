/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan pare representar la información
 * relevante de los nodos de workflows.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowNodeDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -6293928775129565447L;

	/**
	 * Es el título del nodo representado por este dto.
	 */
	public String title;

	/**
	 * Es la fecha de creación del nodo representado por este dto.
	 */
	public String creationDate;

	/**
	 * Es el alias del responsable del nodo representado por este dto.
	 */
	public String responsibleAlias;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el título del nodo representado por este dto.
	 * 
	 * @param anOid
	 *            es el oid del nodo representado por este dto.
	 * @param aDate
	 *            es la fecha de creación del nodo representado por este dto.
	 * @param anAlias
	 *            es el alias del responsable del nodo representado por este
	 *            dto.
	 */
	public WorkflowNodeDTO(String aTitle, String anOid, String aDate,
			String anAlias) {
		this.setTitle(aTitle);
		this.setOid(anOid);
		this.setResponsibleAlias(anAlias);
		this.setCreationDate(aDate);
	}

	/**
	 * Getter.
	 * 
	 * @return el título de la descripción de nodo.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título de la descripción de nodo.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creación del nodo representado por este dto.
	 */
	public String getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creación del nodo representado por este dto.
	 */
	public void setCreationDate(String aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return el alias del responsable del nodo representado por este dto.
	 */
	public String getResponsibleAlias() {
		return this.responsibleAlias;
	}

	/**
	 * Setter.
	 * 
	 * @param anAlias
	 *            es el alias del responsable del nodo representado por este
	 *            dto.
	 */
	public void setResponsibleAlias(String anAlias) {
		this.responsibleAlias = anAlias;
	}

	/**
	 * Verifica la igualdad con otro objeto.<br>
	 * Este objeto solamente es igual a otro DTO de nodo de workflow que
	 * represente al mismo nodo (es decir con el mismo oid).
	 * 
	 * @param obj
	 *            es el otro objeto contra el que se debe comparar al receptor.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		try {
			WorkflowNodeDTO anotherDTO = (WorkflowNodeDTO) obj;
			result = anotherDTO.getOid().equals(this.getOid());
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return el hash code de este objeto, que se calcula en base al hash code
	 *         del oid del DTO.
	 */
	@Override
	public int hashCode() {

		return this.getOid().hashCode();
	}

	public String toString() {
		return this.getTitle();
	}

}
