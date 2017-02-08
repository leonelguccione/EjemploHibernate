/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;


/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro de ítems que filtra de acuerdo a un texto ingresado. Este texto será
 * verificado contra el título, su descripción y las observaciones.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByText extends FilterComponentByText {

	/**
	 * Constructor.
	 * 
	 * @param aText
	 *            es el texto por el cual se debe filtrar los ítems.
	 */
	public ConcreteFilterComponentByText(String aText) {
		this.setText(aText);
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

		return aStrategy.getFilterStringForConcreteFilterComponentByText(this
				.getText());
	}

}
