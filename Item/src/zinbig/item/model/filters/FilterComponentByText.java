/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

/**
 * Esta clase es el tope de la jerarquía de los componentes de filtros
 * responsables de filtrar los items por un texto arbitario. Este texto será
 * verificado contra la descripción, el título y las observaciones de cada uno
 * de los ítems.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterComponentByText {

	/**
	 * Es el texto por el cual se debe filtrar los ítems.
	 */
	public String text;

	/**
	 * Retorna el string que representa a este componente de filtro de ítems.
	 * 
	 * @param aStrategy
	 *            es la estrategia que se debe utilizar para crear el string.
	 * @return un string convertido de acuerdo a la estrategia recibida.
	 */
	public abstract String getFilterString(
			FilterStringCreationStrategy aStrategy, Filter aFilter);

	/**
	 * Getter.
	 * 
	 * @return el texto por el cual se debe filtrar los ítems.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Setter.
	 * 
	 * @param aText
	 *            es el texto por el cual se debe filtrar los ítems.
	 */
	public void setText(String aText) {
		this.text = aText;
	}

}
