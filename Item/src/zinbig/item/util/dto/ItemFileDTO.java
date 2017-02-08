/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar los archivos
 * adjuntos de los ítems.<BR>
 * Para representar un archivo se utilizan su nombre y su fecha de creación.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemFileDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -3340562232850989558L;

	/**
	 * Es el nombre del archivo representado por este dto.
	 */
	protected String filename;

	/**
	 * Es la fecha de creación del archivo representado por este dto.
	 */
	protected String creationDate;

	/**
	 * Constructor.
	 * 
	 * @param aFilename
	 *            es el nombre del archivo representado por este dto.
	 * @param aDate
	 *            es la fecha de creación del archivo representado por este dto.
	 * @param anOid
	 *            es el oid del archivo representado por este dto.
	 */
	public ItemFileDTO(String aFilename, String aDate, String anOid) {
		this.setCreationDate(aDate);
		this.setFilename(aFilename);
		this.setOid(anOid);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del archivo representado por este dto.
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilename
	 *            es el nombre del archivo representado por este dto.
	 */
	public void setFilename(String aFilename) {
		this.filename = aFilename;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creación del archivo representado por este dto.
	 */
	public String getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creación del archivo representado por este dto.
	 */
	public void setCreationDate(String aDate) {
		this.creationDate = aDate;
	}

}
