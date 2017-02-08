/**
 * Este paquete contiene clases útiles del sistema.
 * 
 */
package zinbig.item.util;

/**
 * Las instancias de esta clase se utilizan para representar propiedades del
 * sistema, tales como el puerto en donde se ejecuta la aplicación, la cuenta de
 * email del servidor, etc. <br>
 * Cada propiedad del sistema es almacenada en la base de datos y luego todas
 * las propiedades son levantadas como un diccionario (clave/valor) a nivel
 * aplicación. <br>
 * La idea es que el administrador pueda cambiar cada uno de los valores a
 * través de la misma aplicación.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SystemProperty {

	/**
	 * Es el identificador de esta instancia.
	 */
	public String oid;

	/**
	 * Es el nombre de la propiedad.
	 */
	public String propertyName;

	/**
	 * Es el valor de la propiedad.
	 */
	public String propertyValue;

	/**
	 * Constructor por defecto. No debería utilizarse directamente, existe para
	 * poder realizar tests de unidad sobre esta clase.
	 */
	public SystemProperty() {
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad.
	 * @param aValue
	 *            es el valor de la propiedad.
	 */
	public SystemProperty(String aName, String aValue) {
		this.setPropertyName(aName);
		this.setPropertyValue(aValue);
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
	 * @return el nombre de la propiedad.
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad.
	 */
	public void setPropertyName(String aName) {
		this.propertyName = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el valor de la propiedad.
	 */
	public String getPropertyValue() {
		return this.propertyValue;
	}

	/**
	 * Setter.
	 * 
	 * @param aValue
	 *            es el valor de la propiedad.
	 */
	public void setPropertyValue(String aValue) {
		this.propertyValue = aValue;
	}

}
