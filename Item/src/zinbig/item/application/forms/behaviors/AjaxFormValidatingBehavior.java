/**
 * Este paquete contiene clases �tiles para la creaci�n de p�ginas que se basan
 * en ajax para brindar su funcionalidad.
 * 
 */
package zinbig.item.application.forms.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Las instancias de esta clase se utilizan para decorar los componentes de las
 * p�ginas con un comportamiento que valida el contenido del componente
 * utilizando Ajax.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AjaxFormValidatingBehavior extends
		AjaxFormComponentUpdatingBehavior {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -7191619170930771932L;

	/**
	 * Panel en el que se muestra el feedback al usuario.
	 */
	private final Component feedbackPanel;

	/**
	 * Constructor.
	 * 
	 * @param event
	 *            es el evento que notifica a este objeto.
	 * @param feedbackPanel
	 *            es el panel en el cual se deber� notificar las
	 *            actualizaciones.
	 */
	public AjaxFormValidatingBehavior(String event, Component feedbackPanel) {
		super(event);
		this.feedbackPanel = feedbackPanel;
	}

	/**
	 * Actualiza el panel de feedback debido a cambios en el componente que
	 * registr� este objeto.
	 * 
	 * @param target
	 *            es el elemento que contiene todos los componentes que deben
	 *            actualizarse debido a la actualazaci�n.
	 */
	protected void onUpdate(final AjaxRequestTarget target) {
		target.addComponent(this.getFeedbackPanel());
	}

	/**
	 * Agrega este objeto decorador a todos los componentes de la jerarqu�a.
	 * 
	 * @param event
	 *            es el evento que dispara cambios.
	 * @param form
	 *            es el formulario contenedor de los componentes a decorar.
	 * @param feedbackPanel
	 *            es el panel en el que se mostrar�n los cambios.
	 */
	@SuppressWarnings("unchecked")
	public static void addToAllFormComponents(final String event,
			final Form form, final Component feedbackPanel) {

		form.visitChildren(FormComponent.class, new IVisitor() {
			public Object component(Component component) {
				component.add(new AjaxFormValidatingBehavior(event,
						feedbackPanel));
				return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
			}
		});
	}

	/**
	 * Getter.
	 * 
	 * @return el panel en el que se muestran las actualizaciones.
	 */
	public Component getFeedbackPanel() {
		return this.feedbackPanel;
	}

}
