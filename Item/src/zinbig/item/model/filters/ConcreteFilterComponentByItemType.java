/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro de ítems que filtra de acuerdo a su tipo.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByItemType extends
		FilterComponentByItemType {

	/**
	 * Define si se debe negar o no la condición establecida por este componente
	 * de filtro.
	 */
	public boolean negated;

	/**
	 * Representa la colección de títulos de tipos de ítems por los que hay que
	 * filtrar.
	 */
	public Collection<String> titles;

	/**
	 * Constructor.
	 * 
	 * @param someTitles
	 *            es la colección que contiene los títulos de los tipos de ítems
	 *            por los cuales se debe filtrar.
	 * @param mustNegate
	 *            establece si se debe negar o no la condición de este filtro.
	 */
	public ConcreteFilterComponentByItemType(Collection<String> someTitles,
			boolean mustNegate) {
		this.setTitles(someTitles);
		this.setNegated(mustNegate);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los ítems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creación del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se está creando a partir de este componente.
	 * @return un string que representa el filtro por el tipo que se debe
	 *         aplicar.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy.getFilterStringForConcreteFilterComponentByItemType(
				this.getTitles(), this.isNegated());
	}

	/**
	 * Getter.
	 * 
	 * @return los títulos de los tipos de ítems por los que se debe filtrar.
	 */
	public Collection<String> getTitles() {
		return this.titles;
	}

	/**
	 * Setter.
	 * 
	 * @param someTitles
	 *            son los títulos de los tipos de ítems por los que se debe
	 *            filtrar.
	 */
	public void setTitles(Collection<String> someTitles) {
		this.titles = someTitles;
	}

	/**
	 * Getter.
	 * 
	 * @return true si este filtro niega la condición seleccionada.
	 */
	public boolean isNegated() {
		return this.negated;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condición del filtro.
	 */
	public void setNegated(boolean aBoolean) {
		this.negated = aBoolean;
	}

}
