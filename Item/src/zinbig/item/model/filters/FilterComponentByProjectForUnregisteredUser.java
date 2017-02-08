/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

/**
 * Las instancias de esta clase se utilizan para filtrar los ítems considerando
 * solamente los proyectos públicos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterComponentByProjectForUnregisteredUser extends
		FilterComponentByProject {

	/**
	 * Retorna el string que corresponde a este componente de filtro.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creación del string que se debe utilizar.
	 * @return un string creado a partir de la estrategia de conversión.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy
				.getFilterStringForFilterComponentByProjectWithoutUser();
	}

}
