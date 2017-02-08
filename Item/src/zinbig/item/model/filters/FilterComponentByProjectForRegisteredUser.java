/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

/**
 * Las instancias de esta clase se utilizan para filtrar los �tems considerando
 * tanto los proyectos p�blicos como privados del usuario.
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
	 *            es la estrategia de creaci�n del string que se debe utilizar.
	 * @return un string creado a partir de la estrategia de conversi�n.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy.getFilterStringForFilterComponentByProjectWithUser();
	}

}
