/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicaci�n.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.Collection;

import zinbig.item.util.dto.FilterComponentByProjectDTO;

/**
 * Las instancias de esta clase representan el estado del formulario de creaci�n
 * de filtros en el cual se cuenta con un usuario en la sesi�n (por lo que se
 * muestran los proyectos p�blicos y los proyectos privados del usuario).<br>
 * El componente de selecci�n de filtro del componente "proyecto" tambi�n se
 * define mediante este estado.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class AddItemFilterFormWithUserState extends AddFilterFormState
		implements Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -535789138055672119L;

	/**
	 * Constructor.
	 * 
	 * @param aForm
	 *            es el formulario para el cual este objeto act�a como estado.
	 */
	public AddItemFilterFormWithUserState(AddItemFilterForm aForm) {
		super(aForm);

	}

	/**
	 * Recupera el listado de proyectos que se debe mostrar en el componente que
	 * permite seleccionar el proyecto sobre el cual se ejecutar� el filtro.<br>
	 * Este m�todo recupera solamente los proyectos p�blicos.
	 */
	@Override
	public Collection<FilterComponentByProjectDTO> getProjectsList() {

		return this.getForm().getAllProjects();
	}

}
