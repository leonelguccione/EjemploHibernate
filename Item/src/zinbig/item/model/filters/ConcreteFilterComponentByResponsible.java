/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro de �tems que filtra de acuerdo a su responsable.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByResponsible extends
		FilterComponentByResponsible {

	/**
	 * Define si se debe negar o no la condici�n establecida por este componente
	 * de filtro.
	 */
	public boolean negated;

	/**
	 * Es una colecci�n que contiene los identificadores de todos los
	 * responsables de los �tems.
	 */
	public Collection<String> responsibleOids;

	/**
	 * Constructor.
	 * 
	 * @param aCollection
	 *            es una colecci�n que contiene los identificadores de los
	 *            responsables de los �tems.
	 * @param mustNegate
	 *            establece si se debe negar o no la condici�n de este filtro.
	 */
	public ConcreteFilterComponentByResponsible(Collection<String> aCollection,
			boolean mustNegate) {
		this.setResponsibleOids(aCollection);
		this.setNegated(mustNegate);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los �tems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creaci�n del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se est� creando a partir de este componente.
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
	 * @return los identificadores de los responsables de los �tems.
	 */
	public Collection<String> getResponsibleOids() {
		return this.responsibleOids;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colecci�n que contiene los identificadores de los
	 *            responsables de los �tems.
	 */
	public void setResponsibleOids(Collection<String> aCollection) {
		this.responsibleOids = aCollection;
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
