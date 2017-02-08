/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro por id concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByItemId extends FilterComponentByItemId {

	/**
	 * Constructor.
	 * 
	 * @param aSearchString
	 *            es el string por el que se debe filtrar a los ítems.
	 */
	public ConcreteFilterComponentByItemId(String aSearchString) {
		this.setSelectedItemId(aSearchString);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los ítems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creación del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se está creando a partir de este componente.
	 * @return un string que representa el filtro por id que se debe aplicar.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy.getFilterStringForConcreteFilterComponentByItemId(this
				.getSelectedItemId());
	}

}
