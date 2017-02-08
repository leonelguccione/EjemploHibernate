/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.EditPrioritySetForm;

/**
 * Las instancias de este panel se utilizan para poder editar los detalles
 * b�sicos de un conjunto de prioridades.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditPrioritySetBasicDataPanel extends Panel {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -4370364607719981212L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades que se est� editando.
	 */
	public EditPrioritySetBasicDataPanel(String id, String aPrioritySetId) {
		super(id);

		// agrega el formulario de edici�n de conjuntos de prioridades.
		this
				.add(new EditPrioritySetForm("editPrioritySetForm",
						aPrioritySetId));
	}

}
