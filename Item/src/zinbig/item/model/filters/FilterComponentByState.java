/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

/**
 * Esta clase es el tope de la jerarquía de los componentes de filtros
 * responsables de filtrar los items por su estado. Establece el comportamiento
 * en comun para todas las subclases.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterComponentByState {

	/**
	 * Retorna el string que representa a este componente de filtro de ítems.
	 * 
	 * @param aStrategy
	 *            es la estrategia que se debe utilizar para crear el string.
	 * @return un string convertido de acuerdo a la estrategia recibida.
	 */
	public abstract String getFilterString(
			FilterStringCreationStrategy aStrategy, Filter aFilter);

}
