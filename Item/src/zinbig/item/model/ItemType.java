/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase se utilizan para diferenciar a los ítems de
 * acuerdo a su tipo. Un tipo puede ser Defecto, Mejora, etc.<br>
 * El sistema toma en cuenta el tipo del ítem para definir el workflow de
 * posibles estados disponibles. Cada transición entre estados se define de
 * acuerdo al tipo del ítem.<br>
 * Los tipos de ítems con alguna referencia hacia ellos no se pueden eliminar.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ItemType implements Versionable, Cloneable {

	/**
	 * Mantiene la versión de este objeto.
	 */
	public int version;

	/**
	 * Es el título de este tipo de ítem.
	 */
	protected String title;

	/**
	 * Mantiene el oid que permite identificar a esta instancia.
	 */
	protected String oid;

	/**
	 * Es la cantidad de referencias hacia este tipo de ítem.
	 */
	protected int referencesCount;

	/**
	 * Constructor por defecto.<No se debe utilizar.
	 */
	public ItemType() {
		this("");
	}

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el título del nuevo tipo de ítem.
	 * 
	 */
	public ItemType(String aTitle) {
		this.setTitle(aTitle);
		this.setOid(IDGenerator.getId());
		this.setReferencesCount(0);
	}

	/**
	 * Getter.
	 * 
	 * @return el título del nuevo tipo de ítem.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del nuevo tipo de ítem.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return el oid del item.
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
	 * Getter.
	 * 
	 * @return la versión de este objeto.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es el número de versión de esta instancia.
	 */
	public void setVersion(int aNumber) {
		this.version = aNumber;
	}

	/**
	 * Retorna una copia de este objeto.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ItemType clone = (ItemType) super.clone();
		clone.setVersion(1);
		clone.setOid(IDGenerator.getId());
		clone.setTitle(this.getTitle());

		return clone;
	}

	/**
	 * Notifica al receptor que se prepare para ser eliminado del sistema.<br>
	 * 
	 */
	public void prepareForDeletion() {

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de referencias hacia este tipo de ítem.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de referencias hacia este tipo de ítem.
	 */
	public void setReferencesCount(int aNumber) {
		this.referencesCount = aNumber;
	}

	/**
	 * Incrementa la cantidad de referencias a este objeto. <br>
	 * Esta información es utilizada para saber si este objeto puede ser
	 * eliminado o no del sistema.
	 * 
	 */
	public void increaseReferencesCount() {
		this.setReferencesCount(this.getReferencesCount() + 1);

	}

	/**
	 * Decrementa la cantidad de referencias a este objeto. <br>
	 * Esta información es utilizada para saber si este objeto puede ser
	 * eliminado o no del sistema.
	 * 
	 */
	public void decreaseReferencesCount() {
		this.setReferencesCount(this.getReferencesCount() - 1);

	}

}
