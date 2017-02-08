/**
 * Este paquete contiene las clases requeridas para modelar las propiedades
 * adicionales que se pueden definir en cada proyecto para ser completadas luego
 * en cada ítem.
 */
package zinbig.item.model.properties;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para definir las propiedades
 * adicionales. Es una implementación del patrón de diseño TypeObject, en el
 * cual las instancias de esta clase representan el "tipo".
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class PropertyDescription {

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Define si una propiedad es requerida o no.
	 */
	public boolean required;

	/**
	 * Mantiene el nombre de la propiedad descripta por este objeto.
	 */
	public String name;

	/**
	 * Especifica que tipo de propiedad adicional es este objeto.
	 */
	public char type;

	/**
	 * Constructor por defecto. Este constructor no debería utilizarse.
	 */
	public PropertyDescription() {
		this("", false, 'S');
	}

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre de la nueva descripción de propiedad.
	 * @param isRequired
	 *            establece si la nueva propiedad es requerida o no.
	 * @param aType
	 *            especifica el tipo de este objeto.
	 */
	public PropertyDescription(String aName, boolean isRequired, char aType) {
		this.setName(aName);
		this.setRequired(isRequired);
		this.setType(aType);
		this.setOid(IDGenerator.getId());
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
	 * @return true en caso de que la propiedad sea requerida; false en caso
	 *         contrario.
	 */
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            define si la propiedad es requerida.
	 */
	public void setRequired(boolean aBoolean) {
		this.required = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la propiedad.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el tipo de esta descripción de propiedad.
	 */
	public char getType() {
		return this.type;
	}

	/**
	 * Setter.
	 * 
	 * @param aType
	 *            es el tipo de esta descripción de propiedad.
	 */
	public void setType(char aType) {
		this.type = aType;
	}

	/**
	 * Configura al receptor con valores adicionales. Cada subclase debe
	 * reimplementar este método.
	 * 
	 * @param aValue
	 *            es un string que podría contener valores adicionales
	 *            requeridos.
	 */
	public abstract void configureWithValueAdditionalValue(String aValue);

}
