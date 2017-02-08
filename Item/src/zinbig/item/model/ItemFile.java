/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

import java.util.Date;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para representar los archivos
 * adjuntos que los usuarios suben.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemFile {

	/**
	 * Es el identificador de este objeto.
	 */
	protected String oid;

	/**
	 * Es la fecha de creación de este archivo.
	 */
	protected Date creationDate;

	/**
	 * Es el nombre del archivo. Este nombre no contiene la ruta, sino que
	 * solamente tiene el nombre del archivo.
	 */
	protected String filename;

	/**
	 * Constructor por defecto.
	 */
	public ItemFile() {
		this("", new Date());
	}

	/**
	 * Cosntructor.
	 * 
	 * @param aFilename
	 *            es el nombre del archivo.
	 * @param aCreationDate
	 *            es la fecha de creación del archivo.
	 */
	public ItemFile(String aFilename, Date aCreationDate) {
		this.setOid(IDGenerator.getId());
		this.setCreationDate(aCreationDate);
		this.setFilename(aFilename);
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
	 * @return la fecha de creación de este archivo.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creación de este archivo.
	 */
	public void setCreationDate(Date aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del archivo.
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilename
	 *            es el nombre del archivo.
	 */
	public void setFilename(String aFilename) {
		this.filename = aFilename;
	}

}
