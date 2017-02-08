/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

/**
 * Las instancias de esta clase se utilizan para filtrar los ítems considerando
 * tanto los proyectos públicos como privados del usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterComponentByProjectForRegisteredUser extends
		FilterComponentByProjectForUnregisteredUser {

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

		return aStrategy.getFilterStringForFilterComponentByProjectWithUser();
	}

}
