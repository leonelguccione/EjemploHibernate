/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para especificar un nivel de
 * prioridad para los items administrados por esta herramienta.<br>
 * Cada vez que se asigna una instancia de esta clase a un ítem se debe
 * actualizar el atributo referencesCount para conocer el número exacto de
 * referencias a esta instancia. Luego cuando se trate de borrar alguna
 * prioridad, solamente se podrán borrar aquellas que no estén referenciadas.<br>
 * Esta clase implementa la interface Cloneable para permitir que se copien
 * íntegramente sus instancias.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Priority implements Cloneable {

	/**
	 * Es el oid de esta instancia.
	 */
	public String oid;

	/**
	 * Es el título de esta prioridad.
	 */
	public String title;

	/**
	 * Es el valor de esta prioridad, que se utiliza para compararlas contra
	 * otras prioridades.
	 */
	public String value;

	/**
	 * Mantiene la cuenta de cuantos ítems tienen a este objeto como su
	 * prioridad.
	 */
	public int referencesCount;

	/**
	 * Constructor por defecto. <br>
	 * Este constructor no se debería utilizar. Existe para poder hacer pruebas
	 * de unidad sobre esta clase.
	 */
	public Priority() {
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el título de esta prioridad.
	 * @param aValue
	 *            es el valor de esta prioridad.
	 * 
	 */
	public Priority(String aTitle, String aValue) {
		this.setTitle(aTitle);
		this.setValue(aValue);
		this.setReferencesCount(0);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el oid de esta instancia.
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
	 * @return el título de esta prioridad.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título de esta prioridad.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Retorna una representación como string del receptor.
	 * 
	 * @return "Prioridad " y el título de la prioridad.
	 */
	public String toString() {
		return this.getTitle() + "{" + this.getValue() + "}";
	}

	/**
	 * Getter.
	 * 
	 * @return el valor asociado a esta prioridad.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter.
	 * 
	 * @param aValue
	 *            es el valor de esta prioridad.
	 */
	public void setValue(String aValue) {
		this.value = aValue;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de ítems que referencian a esta instancia.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de ítems que referencian a esta página.
	 */
	public void setReferencesCount(int aCount) {
		this.referencesCount = aCount;
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

	/**
	 * Retorna una copia de este objeto.
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Priority clone = (Priority) super.clone();
		clone.setOid(IDGenerator.getId());
		clone.setReferencesCount(0);

		return clone;
	}

}
