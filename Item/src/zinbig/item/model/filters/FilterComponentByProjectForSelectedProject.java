/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para poder armar los filtros de
 * �tems, filtr�ndolos por un proyecto en particular seleccionado por el
 * usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterComponentByProjectForSelectedProject extends
		FilterComponentByProject {

	/**
	 * Define si se debe negar o no la condici�n establecida por este componente
	 * de filtro.
	 */
	public boolean negated;

	/**
	 * Constructor.
	 * 
	 */
	public FilterComponentByProjectForSelectedProject() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param someOids
	 *            es el oid del proyecto seleccionado para este filtro.
	 * @param mustNegate
	 *            establece si se debe negar o no la condici�n de este filtro.
	 */
	public FilterComponentByProjectForSelectedProject(
			Collection<String> someOids, boolean mustNegate) {
		this.setSelectedProjectOids(someOids);
		this.setNegated(mustNegate);
	}

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

		return aStrategy
				.getFilterStringForFilterComponentByProjectWithSelectedProject(
						aFilter.getFilterComponentByProject(), this.isNegated());
	}

	/**
	 * Getter.
	 * 
	 * @return true si este filtro niega la condici�n seleccionada.
	 */
	public boolean isNegated() {
		return this.negated;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condici�n del filtro.
	 */
	public void setNegated(boolean aBoolean) {
		this.negated = aBoolean;
	}

}
