/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.Collection;

import zinbig.item.util.dto.FilterComponentByProjectDTO;

/**
 * Las instancias de esta clase representan el estado del formulario de creación
 * de filtros en el cual se cuenta con un usuario en la sesión (por lo que se
 * muestran los proyectos públicos y los proyectos privados del usuario).<br>
 * El componente de selección de filtro del componente "proyecto" también se
 * define mediante este estado.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class AddItemFilterFormWithUserState extends AddFilterFormState
		implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -535789138055672119L;

	/**
	 * Constructor.
	 * 
	 * @param aForm
	 *            es el formulario para el cual este objeto actúa como estado.
	 */
	public AddItemFilterFormWithUserState(AddItemFilterForm aForm) {
		super(aForm);

	}

	/**
	 * Recupera el listado de proyectos que se debe mostrar en el componente que
	 * permite seleccionar el proyecto sobre el cual se ejecutará el filtro.<br>
	 * Este método recupera solamente los proyectos públicos.
	 */
	@Override
	public Collection<FilterComponentByProjectDTO> getProjectsList() {

		return this.getForm().getAllProjects();
	}

}
