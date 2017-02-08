/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.EditProjectForm;

/**
 * Las instancias de este panel se utilizan para poder editar los detalles
 * básicos de un proyecto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditProjectBasicDataPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -7129494520591911245L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param anOid
	 *            es el oid del proyecto que se está editando.
	 */
	public EditProjectBasicDataPanel(String id, String anOid) {
		super(id);

		// agrega el formulario de edición de proyectos
		EditProjectForm aForm = new EditProjectForm("editProjectForm", anOid);
		this.add(aForm);
	}

}
