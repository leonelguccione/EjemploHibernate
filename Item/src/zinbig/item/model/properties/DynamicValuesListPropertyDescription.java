/**
 * Este paquete contiene las clases requeridas para modelar las propiedades
 * adicionales que se pueden definir en cada proyecto para ser completadas luego
 * en cada ítem.
 */
package zinbig.item.model.properties;

/**
 * Las instancias de esta clase se utilizan para describir propiedades
 * adicionales que toman sus posibles valores de una manera dinámica, es decir a
 * través de una consulta SQL.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DynamicValuesListPropertyDescription extends
		ListPropertyDescription {

	/**
	 * Mantiene la consulta SQL que será ejecutada para obtener los valores
	 * posibles para esta descripción de propiedad.
	 */
	public String query;

	/**
	 * Constructor.
	 */
	public DynamicValuesListPropertyDescription() {
		super();
		this.setQuery("");
		this.setType('D');
	}

	/**
	 * Constructor.
	 * 
	 * @param aQuery
	 *            es la consulta SQL que debe ser ejecutada para obtener los
	 *            valores de este tipo de propiedad adicional.
	 */
	public DynamicValuesListPropertyDescription(String aQuery) {
		this.setQuery(aQuery);
	}

	/**
	 * Getter.
	 * 
	 * @return la consulta SQL que debe ser ejecutada para obtener los valores
	 *         de este tipo de propiedad adicional.
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * Setter.
	 * 
	 * @param aSQLString
	 *            es la consulta SQL que debe ser ejecutada para obtener los
	 *            valores de este tipo de propiedad adicional.
	 */
	public void setQuery(String aSQLString) {
		this.query = aSQLString;
	}

	/**
	 * Configura al receptor con valores adicionales. Cada subclase debe
	 * reimplementar este método.
	 * 
	 * @param aValue
	 *            es un string que podría contener valores adicionales
	 *            requeridos.
	 */
	public void configureWithValueAdditionalValue(String aValue) {
		this.setQuery(aValue);
	}

}
