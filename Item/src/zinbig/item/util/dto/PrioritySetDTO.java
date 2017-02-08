/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar la información de
 * los conjuntos de prioridades.<br>
 * La representación de un conjunto consiste de su nombre,su oid y una
 * indicación de si puede ser eliminado o no.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrioritySetDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 3819875242449056710L;

	/**
	 * Es el nombre del conjunto representado por este dto.
	 */
	public String name;

	/**
	 * Determina si el conjunto de prioridades representado por este dto se
	 * puede eliminar.
	 */
	public boolean isDeletable;

	/**
	 * Indica si el conjunto de prioridades representado por este dto es el
	 * conjunto por defecto.
	 */
	public boolean defaultPrioritySet;

	/**
	 * Mantiene la cantidad de prioridades que contiene el conjunto de
	 * prioridades representado por este dto.
	 */
	public int prioritiesCount;

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre del conjunto de prioridades.
	 * @param anOid
	 *            es la identificación del conjunto.
	 * @param aBoolean
	 *            indica si se puede eliminar o no el conjunto.
	 * @param isDefaultPrioritySet
	 *            indica si el conjunto de prioridades representado por este dto
	 *            es el conjunto por defecto.
	 * @param aNumber
	 *            indica el número de la versión del objeto representado por
	 *            este dto.
	 * @param aCount
	 *            es la cantidad de prioridades que contiene el conjunto de
	 *            prioridades representado por este dto.
	 */
	public PrioritySetDTO(String aName, String anOid, boolean aBoolean,
			int aNumber, boolean isDefaultPrioritySet, int aCount) {
		this.setName(aName);
		this.setOid(anOid);
		this.setDeletable(aBoolean);
		this.setVersion(aNumber);
		this.setDefaultPrioritySet(isDefaultPrioritySet);
		this.setPrioritiesCount(aCount);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del objeto representado por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del objeto representado por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return indica si el objeto representado por este dto puede ser
	 *         eliminado.
	 */
	public boolean isDeletable() {
		return this.isDeletable;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            true en caso de que el objeto representado por este dto puede
	 *            ser eliminado; false en caso contrario.
	 */
	public void setDeletable(boolean aBoolean) {
		this.isDeletable = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el conjunto de prioridades representado por
	 *         este dto sea el conjunto por defecto; false en caso contrario.
	 */
	public boolean isDefaultPrioritySet() {
		return this.defaultPrioritySet;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si este dto representa a un conjunto de prioridades por
	 *            defecto.
	 */
	public void setDefaultPrioritySet(boolean aBoolean) {
		this.defaultPrioritySet = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de prioridades que contiene el conjunto de
	 *         prioridades representado por este dto.
	 */
	public int getPrioritiesCount() {
		return this.prioritiesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de prioridades que contiene el conjunto de
	 *            prioridades representado por este dto.
	 */
	public void setPrioritiesCount(int aNumber) {
		this.prioritiesCount = aNumber;
	}

}
