/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar a una prioridad. <br>
 * Dicha representación consiste del nombre, oid, valor e imagen.<br>
 * Esta clase implementa la interface Serializable de modo de poder almacenarla
 * en una sesión web de trabajo.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PriorityDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -4455555408730201066L;

	/**
	 * Mantiene el nombre de la prioridad representada por este dto.
	 */
	protected String name;

	/**
	 * Mantiene el valor asignado a la prioridad representada por este dto.
	 */
	protected String value;

	/**
	 * Mantiene la cantidad de referencias a la instancia de la clase Prioridad
	 * representada por este dto.
	 */
	public int referencesCount;

	/**
	 * Constructor.
	 * 
	 * @param anOid
	 *            es el oid de la prioridad representada por el dto.
	 * @param aName
	 *            es el nombre de la prioridad representada por el dto.
	 * @param aValue
	 *            es el valor de la prioridad representada por el dto.
	 * @param aCount
	 *            es la cantidad de referencias a la prioridad representada por
	 *            este dto.
	 */
	public PriorityDTO(String anOid, String aName, String aValue, int aCount) {
		this.setValue(aValue);
		this.setOid(anOid);
		this.setName(aName);
		this.setReferencesCount(aCount);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la prioridad representada por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la prioridad representada por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el valor de la prioridad representada por este dto.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter.
	 * 
	 * @param aValue
	 *            es el valor de la prioridad representada por este dto.
	 */
	public void setValue(String aValue) {
		this.value = aValue;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de referencias a la prioridad representada por este
	 *         dto.
	 */
	public int getReferencesCount() {
		return this.referencesCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de referencias a la prioridad representada por
	 *            este dto.
	 */
	public void setReferencesCount(int aCount) {
		this.referencesCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que presenta el hashcode de este objeto.
	 */
	@Override
	public int hashCode() {

		return (this.getName() + this.getName()).hashCode();
	}

	/**
	 * Verifica si el objeto recibido es igual al receptor.
	 * 
	 * @param anObject
	 *            es el objeto que hay que comparar.
	 * @return true en caso de que ambos objetos sean instancias de la clase
	 *         PriorityDTO y que representen a la misma prioridad (mismo oid);
	 *         false en caso contrario.
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;

		try {
			PriorityDTO aDTO = (PriorityDTO) anObject;
			if (aDTO.getOid().equals(this.getOid())) {
				result = true;
			}
		} catch (Exception e) {
			// no se debe hacer nada ya que probablemente haya fallado debido a
			// una excepción de casting.
		}

		return result;
	}

	/**
	 * Retorna un string que representa a la prioridad.
	 * 
	 * @return el nombre de la prioridad y su valor.
	 */
	public String getCompleteName() {
		return this.getName() + " [" + this.getValue() + "]";
	}

}
