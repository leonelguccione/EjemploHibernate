/**
 * Este paquete contiene clases e interfaces �tiles para la creaci�n de filtros
 * para los listados de �tems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Esta clase representa el tope de la jerarqu�a de los componentes de filtros
 * de �tems para definir la forma en la que se filtra de acuerdo al proyecto.
 * Cada una de las subclases debe definir como se considera la informaci�n del
 * proyecto a la hora de filtrar.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterComponentByProject {

	/**
	 * Son los oids de los proyectos seleccionados para este filtro.
	 */
	protected Collection<String> selectedProjectOids;

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
	 * @return son los oids de los proyectos seleccionados para este componente
	 *         de filtro.
	 */
	public Collection<String> getSelectedProjectOids() {
		return this.selectedProjectOids;
	}

	/**
	 * Setter.
	 * 
	 * @param someOids
	 *            son los oids de los proyectos seleccionados para este
	 *            componente de filtro.
	 */
	public void setSelectedProjectOids(Collection<String> someOids) {
		this.selectedProjectOids = someOids;
	}

}
