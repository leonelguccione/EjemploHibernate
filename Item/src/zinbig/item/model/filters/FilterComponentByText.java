/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

/**
 * Esta clase es el tope de la jerarqu�a de los componentes de filtros
 * responsables de filtrar los items por un texto arbitario. Este texto ser�
 * verificado contra la descripci�n, el t�tulo y las observaciones de cada uno
 * de los �tems.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterComponentByText {

	/**
	 * Es el texto por el cual se debe filtrar los �tems.
	 */
	public String text;

	/**
	 * Retorna el string que representa a este componente de filtro de �tems.
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
	 * @return el texto por el cual se debe filtrar los �tems.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Setter.
	 * 
	 * @param aText
	 *            es el texto por el cual se debe filtrar los �tems.
	 */
	public void setText(String aText) {
		this.text = aText;
	}

}
