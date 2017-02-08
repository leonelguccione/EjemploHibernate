/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;


/**
 * Las instancias de esta clase se utilizan para representar un componente de
 * filtro de �tems que filtra de acuerdo a un texto ingresado. Este texto ser�
 * verificado contra el t�tulo, su descripci�n y las observaciones.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ConcreteFilterComponentByText extends FilterComponentByText {

	/**
	 * Constructor.
	 * 
	 * @param aText
	 *            es el texto por el cual se debe filtrar los �tems.
	 */
	public ConcreteFilterComponentByText(String aText) {
		this.setText(aText);
	}

	/**
	 * Recupera el filtro que hay que aplicar a los �tems.
	 * 
	 * @param aStrategy
	 *            es la estrategia de creaci�n del filtro que se debe utilizar.
	 * @param aFilter
	 *            es el filtro que se est� creando a partir de este componente.
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
