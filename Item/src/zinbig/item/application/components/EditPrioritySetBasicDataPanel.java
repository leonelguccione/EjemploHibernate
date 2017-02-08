/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.EditPrioritySetForm;

/**
 * Las instancias de este panel se utilizan para poder editar los detalles
 * básicos de un conjunto de prioridades.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditPrioritySetBasicDataPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -4370364607719981212L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades que se está editando.
	 */
	public EditPrioritySetBasicDataPanel(String id, String aPrioritySetId) {
		super(id);

		// agrega el formulario de edición de conjuntos de prioridades.
		this
				.add(new EditPrioritySetForm("editPrioritySetForm",
						aPrioritySetId));
	}

}
