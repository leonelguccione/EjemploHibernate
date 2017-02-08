/**
 * Este paquete contiene clases e interfaces útiles para dar soporte a los
 * aspectos de persistencia de la aplicación.
 */
package zinbig.item.util.persistence;

/**
 * Las instancias de esta clase se utilizan para almacenar strings que
 * representan consultas a la bases de datos en HQL. Las instancias son
 * configuradas e inyectadas a la aplicación mediante Spring. La consulta
 * propiamente dicha se encuentra definida en el archivo applicationContext.xml.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemQuery {

	/**
	 * Es el string que representa la consulta en HQL.
	 */
	protected String queryString;

	/**
	 * Es el nombre de la propiedad por la que se deben ordenar los resultados.
	 */
	protected String propertyForOrdering;

	/**
	 * Es el orden a aplicar en el resultado.
	 */
	protected String ordering;

	/**
	 * Constructor.
	 * 
	 */
	public ItemQuery() {

	}

	/**
	 * Getter.
	 * 
	 * @return el string que contiene la consulta en HQL.
	 */
	public String getQueryString() {
		return this.queryString;
	}

	/**
	 * Setter.
	 * 
	 * @param aString
	 *            es el string que contiene la consulta en HQL.
	 */
	public void setQueryString(String aString) {
		this.queryString = aString;
	}

	/**
	 * Setter.
	 * 
	 * @param aPropertyName
	 *            es el nombre de la propiedad a utilizar para ordenar los
	 *            resultados.
	 */
	public void setPropertyForOrdering(String aPropertyName) {
		this.propertyForOrdering = aPropertyName;
	}

	/**
	 * Getter.
	 * 
	 * @return el orden a aplicar al resultado.
	 */
	public String getOrdering() {
		return this.ordering;
	}

	/**
	 * Setter.
	 * 
	 * @param anOrdening
	 *            es el orden a aplicar al resultado.
	 */
	public void setOrdering(String anOrdening) {
		this.ordering = anOrdening;
	}

	/**
	 * Getter.
	 * 
	 * @return la propiedad que se debe utilizar para ordenar el resultado.
	 */
	public String getPropertyForOrdering() {
		return this.propertyForOrdering;
	}

	/**
	 * Crea el string completo que se debe utilizar para realizar la consulta.
	 * 
	 * @return un string que contiene la propiedad y el orden a utilizar, en
	 *         caso de que estos se hayan asignado.
	 */
	public String createQueryString() {
		String result = this.getQueryString();
		if (this.getPropertyForOrdering() != null) {
			result = result + " ORDER BY " + this.getPropertyForOrdering()
					+ " " + this.getOrdering();
		}

		return result;
	}

	/**
	 * Crea el string completo que se debe utilizar para realizar la consulta.
	 * 
	 * @param aFilterString
	 *            es el filtro que se debe aplicar a la consulta.
	 * @return un string que contiene la propiedad y el orden a utilizar, en
	 *         caso de que estos se hayan asignado.
	 */
	public String createQueryString(String aFilterString) {

		String result = this.getQueryString() + aFilterString;
		if (this.getPropertyForOrdering() != null) {
			result = result + " ORDER BY " + this.getPropertyForOrdering()
					+ " " + this.getOrdering();
		}

		return result;
	}

}
