/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.Collection;

import zinbig.item.util.dto.FilterComponentByProjectDTO;

/**
 * Esta clase abstracta es el tope de la jerarquía de estados de los formularios
 * de alta de nuevo filtro de ítems. Es una implementación del patrón de diseño
 * State.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class AddFilterFormState implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2710479339303128463L;

	/**
	 * Mantiene una referencia al formulario del cual este objeto es estado.
	 */
	protected AddItemFilterForm form;

	/**
	 * Constructor.
	 * 
	 * @param aForm
	 *            es el formulario del cual este objeto trabaja como estado.
	 */
	public AddFilterFormState(AddItemFilterForm aForm) {
		this.setForm(aForm);
	}

	/**
	 * Getter.
	 * 
	 * @return el formulario para el cual trabaja como estado este objeto.
	 */
	public AddItemFilterForm getForm() {
		return this.form;
	}

	/**
	 * Setter.
	 * 
	 * @param aForm
	 *            es el formulario para el cual trabaja como estado este objeto.
	 */
	public void setForm(AddItemFilterForm aForm) {
		this.form = aForm;
	}

	/**
	 * Recupera el listado de proyectos que se debe mostrar en el componente que
	 * permite seleccionar el proyecto sobre el cual se ejecutará el filtro.<br>
	 * Este método recupera solamente los proyectos públicos.
	 */
	public Collection<FilterComponentByProjectDTO> getProjectsList() {

		return this.getForm().getPublicProjects();
	}

}
