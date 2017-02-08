/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro por estado que no filtra, es decir que es nulo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class NullFilterComponentByState extends FilterComponentByState {

	/**
	 * Retorna el string que corresponde a este componente de filtro.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creaci�n del string que se debe utilizar.
	 * @return un string creado a partir de la estrategia de conversi�n.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {
		return "";
	}

}
