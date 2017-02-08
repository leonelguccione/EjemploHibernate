/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase se utilizan para agrupar prioridades. A cada
 * proyecto se le asigna siempre un conjunto de prioridades. Si un proyecto
 * requiere cambiar su conjunto de prioridades lo puede hacer cambiando el que
 * ya tiene (con lo que el cambio se dará en todos los demás proyectos que
 * compartan el mismo conjunto de prioridades) o crear un nuevo conjunto de
 * prioridades para este proyecto.<br>
 * El Administrador del sistema es el único que puede crear y asignar conjuntos
 * de prioridades para los proyectos (esto de todos modos se puede cambiar
 * asignando la operación correspondiente a otro grupo de usuarios). Esta clase
 * implementa la interfaz Versionable a fin de poder controlar la edición
 * concurrente por parte de dos usuarios.<br>
 * Esta clase implementa la interface Cloneable ya que al agregar un nuevo
 * proyecto que referencia a un conjunto de prioridades, en vez de asignar este
 * conjunto de prioridades siempre se asigna una copia del mismo para permitir
 * su edición en forma independiente.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrioritySet implements Versionable, Cloneable {

	/**
	 * Mantiene la versión de este objeto.
	 */
	public int version;

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Indica si este conjunto de prioridades es el conjunto por defecto.
	 */
	protected boolean defaultPrioritySet;

	/**
	 * Es el conjunto de prioridades asignadas a este objeto.
	 */
	protected Collection<Priority> priorities;

	/**
	 * Es el nombre del conjunto de prioridades.
	 */
	protected String name;

	/**
	 * Es la cantidad de proyectos que referencian a este conjunto de
	 * prioridades.
	 */
	protected int referencesCount;

	/**
	 * Constructor. <br>
	 * Este constructor no debería utilizarse directamente. Existe para poder
	 * realizar pruebas de unidad.
	 */
	public PrioritySet() {
		this.setPriorities(new ArrayList<Priority>());
		this.setDefaultPrioritySet(false);
		this.setReferencesCount(0);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Constructor. <br>
	 * 
	 * @param aName
	 *            es el nombre del conjunto de prioridades.
	 * @param aBoolean
	 *            indica si este conjunto es el default.
	 */
	public PrioritySet(String aName, boolean aBoolean) {
		this();
		this.setName(aName);
		this.setDefaultPrioritySet(aBoolean);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de esta instancia.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de esta instancia.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de prioridades.
	 */
	public Collection<Priority> getPriorities() {
		return this.priorities;
	}

	/**
	 * Setter.
	 * 
	 * @param somePriorities
	 *            es el conjunto de prioridades.
	 */
	public void setPriorities(Collection<Priority> somePriorities) {
		this.priorities = somePriorities;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que este objeto sea el conjunto de prioridades
	 *         por defecto: false en caso contrario.
	 */
	public boolean isDefaultPrioritySet() {
		return this.defaultPrioritySet;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si este objeto es el conjunto default de
	 *            prioridades.
	 */
	public void setDefaultPrioritySet(boolean aBoolean) {
		this.defaultPrioritySet = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de este conjunto de prioridades.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de este conjunto de prioridades.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos que referencian a este conjunto de
	 *         prioridades.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de referencias a este conjunto de prioridades.
	 */
	public void setReferencesCount(int aNumber) {
		this.referencesCount = aNumber;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que este conjunto de prioridades se pueda borrar;
	 *         false en caso contrario.<br>
	 *         Si este objeto es el conjunto de prioridades por defecto entonces
	 *         no se puede eliminar; en cualquier otro caso se debe verificar si
	 *         tiene referencias de algún proyecto. Solamente se pueden eliminar
	 *         los conjuntos de prioridades sin referencias.
	 */
	public boolean isDeletable() {
		boolean result;
		if (this.isDefaultPrioritySet()) {
			result = false;
		} else {
			result = (this.getReferencesCount() == 0 & this.getPriorities()
					.isEmpty()) ? true : false;
		}

		return result;
	}

	/**
	 * Incrementa la cuenta de proyectos que referencian a este conjunto de
	 * prioridades.
	 * 
	 */
	public void increaseReferencesCount() {
		this.setReferencesCount(this.getReferencesCount() + 1);
	}

	/**
	 * Decrementa la cuenta de proyectos que referencian a este conjunto de
	 * prioridades.
	 * 
	 */
	public void decreaseReferencesCount() {
		this.setReferencesCount(this.getReferencesCount() - 1);
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
	 * Agrega una nueva prioridad al conjunto de prioridades.
	 * 
	 * @param aPriority
	 *            es la prioridad que se debe agregar.
	 */
	public void addPriority(Priority aPriority) {
		this.getPriorities().add(aPriority);

	}

	/**
	 * Verifica si este conjunto de prioridades contiene una prioridad con el
	 * nombre dado.
	 * 
	 * @param aName
	 *            es el nombre de la prioridad que se debe buscar.
	 * @return true en caso de que este conjunto de prioridaes ya tenga el
	 *         nombre dado; false en caso contrario.
	 */
	public boolean containsPriorityWithName(String aName) {
		boolean result = false;
		Iterator<Priority> iterator = this.getPriorities().iterator();

		Priority aPriority = null;
		while (iterator.hasNext()) {
			aPriority = iterator.next();
			if (aPriority.getTitle().equals(aName)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Borra las prioridades contenidas en la colección.
	 * 
	 * @param somePriorities
	 *            es la colección que contiene las prioridades que deben ser
	 *            eliminadas del receptor.
	 */
	public void deletePriorities(Collection<Priority> somePriorities) {
		this.getPriorities().removeAll(somePriorities);

	}

	/**
	 * Realiza una copia de este objeto.
	 * 
	 * @return una copia entera de este objeto, incluyendo las prioridades que
	 *         lo componen.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {

		PrioritySet clone = (PrioritySet) super.clone();
		clone.setPriorities(new ArrayList<Priority>());
		clone.setReferencesCount(0);
		clone.setDefaultPrioritySet(false);
		clone.setVersion(1);
		clone.setOid(IDGenerator.getId());

		Iterator<Priority> priorityIterator = this.getPriorities().iterator();
		Priority aPriority = null;

		while (priorityIterator.hasNext()) {
			aPriority = priorityIterator.next();
			clone.getPriorities().add((Priority) aPriority.clone());
		}

		return clone;
	}

}
