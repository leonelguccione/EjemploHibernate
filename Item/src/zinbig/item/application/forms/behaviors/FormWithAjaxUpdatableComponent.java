/**
 * Este paquete contiene clases �tiles para la creaci�n de p�ginas que se basan
 * en ajax para brindar su funcionalidad.
 * 
 */
package zinbig.item.application.forms.behaviors;

import org.apache.wicket.Component;

/**
 * Esta interface se utiliza para definir el comportamiento que deber�a
 * implementar cualquier formulario que tiene alg�n campo que se verifica con
 * AJAX y luego se actualiza un componente de feedback.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface FormWithAjaxUpdatableComponent {

	/**
	 * Notifica al formulario que se actualiz� el componente mediante AJAX.
	 */
	public void ajaxComponentUpdated();

	/**
	 * Getter.
	 * 
	 * @return el id del componente que se utiliza para mostrar la imagen de
	 *         AJAX.
	 */
	public String getMarkupIdForImageContainer();

	/**
	 * Getter.
	 * 
	 * @param feedbackComponentName
	 *            es el nombre del componente de feedback que se debe
	 *            actualizar.
	 * @return el componente cuyo nombre se ha registrado.
	 */
	public Component get(String feedbackComponentName);

}
