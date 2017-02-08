/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

/**
 * Esta clase es el tope de la jerarqu�a de los componentes de filtros
 * responsables de filtrar los items por su nodo de workflow. Establece el
 * comportamiento en com�n para todas las subclases.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterComponentByNode {

	/**
	 * Retorna el string que representa a este componente de filtro de �tems.
	 * 
	 * @param aStrategy
	 *            es la estrategia que se debe utilizar para crear el string.
	 * @param aFilter
	 *            es el filtro que se est� creando.
	 * @return un string convertido de acuerdo a la estrategia recibida.
	 */
	public abstract String getFilterString(
			FilterStringCreationStrategy aStrategy, Filter aFilter);

}
