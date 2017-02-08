/**
 * Este paquete contiene las clases e interfaces requeridas para modelar la 
 * funcionalidad asociada a los workflows, sus nodos y links entre estos �ltimos.
 */
package zinbig.item.model.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.ItemType;
import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase se utilizan para representar los enlaces que
 * componen una descripci�n de workflow. Cada enlace tiene un t�tulo �nico
 * dentro de su workflow y conecta dos nodos,el inicial y el final. En algunos
 * casos ambos nodos podrian ser en realidad el mismo nodo (esto se utiliza para
 * representar un bucle o enlace a s� mismo). Adem�s de los dos nodos, un enlace
 * tambi�n contiene una colecci�n de tipos de �tems para los cuales el enlace es
 * v�lido. <br>
 * Esta clase implementa la interface Cloneable a fin de que se puedan crear
 * enlaces a partir de esta definici�n.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowLinkDescription implements Cloneable, Versionable {

	/**
	 * Es la descripci�n del nodo inicial de esta descripci�n de nodo.
	 */
	protected WorkflowNodeDescription initialNodeDescription;

	/**
	 * Es la descripci�n del nodo final de esta descripci�n de nodo.
	 */
	protected WorkflowNodeDescription finalNodeDescription;

	/**
	 * Es una colecci�n que contiene los tipos de �tems para los cuales esta
	 * descripci�n de enlace es v�lida.
	 */
	public Collection<ItemType> itemTypes;

	/**
	 * Es el t�tulo de esta descripci�n de enlace.
	 */
	public String title;

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Mantiene la versi�n de este objeto.
	 */
	public int version;

	/**
	 * Constructor.
	 */
	public WorkflowLinkDescription() {
		this.setOid(IDGenerator.getId());
		this.setItemTypes(new ArrayList<ItemType>());
	}

	/**
	 * Getter.
	 * 
	 * @return el nodo inicial de este enlace.
	 */
	public WorkflowNodeDescription getInitialNodeDescription() {
		return this.initialNodeDescription;
	}

	/**
	 * Setter.
	 * 
	 * @param aNodeDescription
	 *            es el nodo inicial de este enlace.
	 */
	public void setInitialNodeDescription(
			WorkflowNodeDescription aNodeDescription) {
		this.initialNodeDescription = aNodeDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return el nodo final de este enlace.
	 */
	public WorkflowNodeDescription getFinalNodeDescription() {
		return this.finalNodeDescription;
	}

	/**
	 * Setter.
	 * 
	 * @param aNodeDescription
	 *            es el nodo final de este enlace.
	 */
	public void setFinalNodeDescription(WorkflowNodeDescription aNodeDescription) {
		this.finalNodeDescription = aNodeDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los tipos de �tems de este enlace.
	 */
	public Collection<ItemType> getItemTypes() {
		return this.itemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param someItemTypes
	 *            es una colecci�n que contiene los tipos de �tems de este
	 *            enlace.
	 */
	public void setItemTypes(Collection<ItemType> someItemTypes) {
		this.itemTypes = someItemTypes;
	}

	/**
	 * Getter.
	 * 
	 * @return el t�tulo de este enlace.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param es
	 *            el t�tulo de este enlace.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
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
	 * Obtiene una copia de este objeto.
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		WorkflowLinkDescription clone = (WorkflowLinkDescription) super.clone();

		clone.setOid(IDGenerator.getId());

		clone.setInitialNodeDescription((WorkflowNodeDescription) this
				.getInitialNodeDescription().clone());
		clone.setFinalNodeDescription((WorkflowNodeDescription) this
				.getFinalNodeDescription().clone());
		clone.setItemTypes(new ArrayList<ItemType>());

		Iterator<ItemType> iterator = this.getItemTypes().iterator();
		while (iterator.hasNext()) {
			clone.getItemTypes().add((ItemType) iterator.next().clone());
		}

		return clone;
	}

	/**
	 * Notifica al receptor para que se prepare para ser eliminado, rompiendo
	 * todos los nexos con los objetos de modelo que no deben ser eliminados en
	 * cascada.
	 * 
	 */
	public void prepareForDeletion() {
		this.setInitialNodeDescription(null);
		this.setFinalNodeDescription(null);

		// los tipos de �tems no se borran as� se eliminan en cascada con cada
		// link.

	}

	/**
	 * Getter.
	 * 
	 * @return la versi�n de este objeto.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es el n�mero de versi�n de esta instancia.
	 */
	public void setVersion(int aNumber) {
		this.version = aNumber;
	}

	/**
	 * Verifica si esta descripci�n de enlace est� definida para el tipo de �tem
	 * recibido.
	 * 
	 * @param anItemType
	 *            es el tipo de �tem para el cual se debe verificar.
	 * @return true en caso de que esta descripci�n de enlace tenga este tipo de
	 *         �tem; false en caso contrario.
	 */
	public boolean containsItemType(ItemType anItemType) {
		return this.getItemTypes().contains(anItemType);
	}

}
