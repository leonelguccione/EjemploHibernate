/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar los tipos de ítems
 * administrados por la herramienta.<br>
 * La representación de un ítem consta de su oid, versión y título.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTypeDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -334164382239152364L;

	/**
	 * Es el título del tipo de ítem representado por este dto.
	 */
	public String title;

	/**
	 * Es la cantidad de referencias hacia el tipo de ítem representado por este
	 * dto.
	 */
	public int referencesCount;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el título del tipo de ítem.
	 * @param aVersion
	 *            es la versión del tipo de ítem.
	 * @param anOid
	 *            es el oid de este tipo de item.
	 * @param aNumber
	 *            es la cantidad de referencias que tiene el tipo de ítem
	 *            representado por este dto.
	 */
	public ItemTypeDTO(String aTitle, String anOid, int aVersion, int aNumber) {
		this.setTitle(aTitle);
		this.setVersion(aVersion);
		this.setOid(anOid);
		this.setReferencesCount(aNumber);
	}

	/**
	 * Getter.
	 * 
	 * @return el título del ítem representado por este dto.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del ítem representado por este dto.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Retorna una representación como string del receptor.
	 * 
	 * @return el título del tipo de ítem representado por este dto.
	 */
	public String toString() {
		return this.title;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de referencias que tiene el tipo de ítem representado
	 *         por este dto.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de referencias que tiene el tipo de ítem
	 *            representado por este dto.
	 */
	public void setReferencesCount(int aNumber) {
		this.referencesCount = aNumber;
	}

}
