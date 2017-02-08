/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

import zinbig.item.util.persistence.Versionable;

/**
 * Esta clase actúa como tope de la jerarquía de DTOS.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class ItemAbstractDTO implements Versionable, Serializable {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 6609355052736566624L;

	/**
	 * Mantiene la versión del proyecto representado por este dto.
	 */
	public int version;

	/**
	 * Es el OID del usuario representado por este DTO.
	 */
	protected String oid;

	/**
	 * Getter.
	 * 
	 * @return la versión del objeto representado por este dto.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es el número de versión del objeto representado por este dto.
	 */
	public void setVersion(int aNumber) {
		this.version = aNumber;
	}

	/**
	 * Getter.
	 * 
	 * @return el OID del objeto representado por este DTO.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOID
	 *            es el OID del objeto representado por este DTO.
	 */
	public void setOid(String anOID) {
		this.oid = anOID;
	}
}
