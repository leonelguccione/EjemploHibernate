/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

/**
 * Esta clase representa el tope de la jerarqu�a de los componentes de filtros
 * de �tems para definir la forma en la que se filtra de acuerdo al id del �tem.
 * Cada una de las subclases debe definir como se considera la informaci�n del
 * proyecto a la hora de filtrar.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterComponentByItemId {

	/**
	 * Es el identificador de este componente de filtro de �tems.
	 */
	protected Long oid;

	/**
	 * Es el id del �tem seleccionado para este filtro.
	 */
	protected String selectedItemId;

	/**
	 * Retorna el string que representa a este componente de filtro de �tems.
	 * 
	 * @param aStrategy
	 *            es la estrategia que se debe utilizar para crear el string.
	 * @return un string convertido de acuerdo a la estrategia recibida.
	 */
	public abstract String getFilterString(
			FilterStringCreationStrategy aStrategy, Filter aFilter);

	/**
	 * Getter.
	 * 
	 * @return el oid de esta instancia.
	 */
	public Long getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de esta instancia.
	 */
	public void setOid(Long anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el id del �tem seleccionado para este componente de filtro.
	 */
	public String getSelectedItemId() {
		return this.selectedItemId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id del �tem seleccionado para este componente de filtro.
	 */
	public void setSelectedItemId(String anId) {
		this.selectedItemId = anId;
	}

}
