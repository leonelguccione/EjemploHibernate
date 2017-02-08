/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Las instancias de esta clase se utilizan para mostrar al usuario un texto de
 * error asociado con un componente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FeedbackLabel extends Label {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -7281516861813397003L;

	/**
	 * Es el componente para el cual hay que mostrar el error.
	 */
	@SuppressWarnings("unchecked")
	private FormComponent component;

	/**
	 * Es el texto que debe mostrar.
	 * 
	 */
	private IModel<String> text = null;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este componente.
	 * @param component
	 *            es el componente para el cual se muestran los errores.
	 */
	@SuppressWarnings("unchecked")
	public FeedbackLabel(String id, FormComponent component) {
		super(id);
		this.component = component;
	}

	/**
	 * Asigna un texto a este label dependiendo de si el componente tiene un
	 * mensaje de feedback o no.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.setDefaultModel(null);
		if (component.getFeedbackMessage() != null) {
			if (this.text != null) {
				this.setDefaultModel(text);
			} else {
				this.setDefaultModel(new Model(component.getFeedbackMessage()
						.getMessage()));

			}

		}
	}

}
