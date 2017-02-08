/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro de �tems que filtra de acuerdo a su estado.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByState extends FilterComponentByState {

	/**
	 * Define si se debe negar o no la condici�n establecida por este componente
	 * de filtro.
	 */
	public boolean negated;

	/**
	 * Representa el estado (valor del enumerativo) por el que hay que filtrar.
	 */
	public Collection<String> values;

	/**
	 * Constructor.
	 * 
	 * @param someValues
	 *            es una colecci�n que contiene los estados por los que hay que
	 *            filtrar los �tems.
	 * @param mustNegate
	 *            establece si se debe negar o no la condici�n definida por este
	 *            componente de filtro.
	 */
	public ConcreteFilterComponentByState(Collection<String> someValues,
			boolean mustNegate) {
		this.setNegated(mustNegate);
		this.setValues(someValues);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los �tems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creaci�n del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se est� creando a partir de este componente.
	 * @return un string que representa el filtro por estados que se debe
	 *         aplicar.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy.getFilterStringForConcreteFilterComponentByState(this
				.getValues(), this.isNegated());
	}

	/**
	 * Getter.
	 * 
	 * @return los valores que representan los estados (enumerativo) por los que
	 *         hay que filtrar.
	 */
	public Collection<String> getValues() {
		return this.values;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colecci�n que representa los estados (enumerativo) por
	 *            los que hay que filtrar.
	 */
	public void setValues(Collection<String> aCollection) {
		this.values = aCollection;
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
