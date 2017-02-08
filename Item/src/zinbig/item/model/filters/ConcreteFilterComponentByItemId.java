/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
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
	 *            es el string por el que se debe filtrar a los �tems.
	 */
	public ConcreteFilterComponentByItemId(String aSearchString) {
		this.setSelectedItemId(aSearchString);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los �tems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creaci�n del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se est� creando a partir de este componente.
	 * @return un string que representa el filtro por id que se debe aplicar.
	 */
	@Override
	public String getFilterString(FilterStringCreationStrategy aStrategy,
			Filter aFilter) {

		return aStrategy.getFilterStringForConcreteFilterComponentByItemId(this
				.getSelectedItemId());
	}

}
