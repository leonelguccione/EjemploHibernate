/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro de ítems que filtra de acuerdo a su responsable.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByResponsible extends
		FilterComponentByResponsible {

	/**
	 * Define si se debe negar o no la condición establecida por este componente
	 * de filtro.
	 */
	public boolean negated;

	/**
	 * Es una colección que contiene los identificadores de todos los
	 * responsables de los ítems.
	 */
	public Collection<String> responsibleOids;

	/**
	 * Constructor.
	 * 
	 * @param aCollection
	 *            es una colección que contiene los identificadores de los
	 *            responsables de los ítems.
	 * @param mustNegate
	 *            establece si se debe negar o no la condición de este filtro.
	 */
	public ConcreteFilterComponentByResponsible(Collection<String> aCollection,
			boolean mustNegate) {
		this.setResponsibleOids(aCollection);
		this.setNegated(mustNegate);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los ítems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creación del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se está creando a partir de este componente.
	 * @return un string que representa el filtro por responsable que se debe
	 *         aplicar.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy
				.getFilterStringForConcreteFilterComponentByResponsible(this
						.getResponsibleOids(), this.isNegated());
	}

	/**
	 * Getter.
	 * 
	 * @return los identificadores de los responsables de los ítems.
	 */
	public Collection<String> getResponsibleOids() {
		return this.responsibleOids;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colección que contiene los identificadores de los
	 *            responsables de los ítems.
	 */
	public void setResponsibleOids(Collection<String> aCollection) {
		this.responsibleOids = aCollection;
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
