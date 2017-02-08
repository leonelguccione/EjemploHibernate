/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar el estado de un
 * formulario de alta de filtros de ítems en el cual no existe usuario en la
 * sesión.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddItemFilterFormWithoutUserState extends AddFilterFormState
		implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 6258321195282936412L;

	/**
	 * Constructor.
	 * 
	 * @param aForm
	 *            es el formulario para el cual este objeto actúa como estado.
	 */
	public AddItemFilterFormWithoutUserState(AddItemFilterForm aForm) {
		super(aForm);

	}

}
