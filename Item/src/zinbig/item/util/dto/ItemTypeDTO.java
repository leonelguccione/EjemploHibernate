/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar los tipos de �tems
 * administrados por la herramienta.<br>
 * La representaci�n de un �tem consta de su oid, versi�n y t�tulo.
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
	 * Es el t�tulo del tipo de �tem representado por este dto.
	 */
	public String title;

	/**
	 * Es la cantidad de referencias hacia el tipo de �tem representado por este
	 * dto.
	 */
	public int referencesCount;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el t�tulo del tipo de �tem.
	 * @param aVersion
	 *            es la versi�n del tipo de �tem.
	 * @param anOid
	 *            es el oid de este tipo de item.
	 * @param aNumber
	 *            es la cantidad de referencias que tiene el tipo de �tem
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
	 * @return el t�tulo del �tem representado por este dto.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo del �tem representado por este dto.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Retorna una representaci�n como string del receptor.
	 * 
	 * @return el t�tulo del tipo de �tem representado por este dto.
	 */
	public String toString() {
		return this.title;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de referencias que tiene el tipo de �tem representado
	 *         por este dto.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de referencias que tiene el tipo de �tem
	 *            representado por este dto.
	 */
	public void setReferencesCount(int aNumber) {
		this.referencesCount = aNumber;
	}

}
