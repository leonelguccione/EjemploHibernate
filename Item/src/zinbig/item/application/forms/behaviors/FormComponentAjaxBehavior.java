/**
 * Este paquete contiene clases útiles para la creación de páginas que se basan
 * en ajax para brindar su funcionalidad.
 * 
 */
package zinbig.item.application.forms.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

/**
 * Las instancias de esta clase se utilizan para notificar mediante ajax de
 * eventos de actualización del contenido de algún campo del formulario
 * recibido.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FormComponentAjaxBehavior extends
		AjaxFormComponentUpdatingBehavior implements IAjaxIndicatorAware {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -4951244737526837130L;

	/**
	 * Es el formulario al que pertenece el campo que se actualiza mediante este
	 * objeto.
	 */
	protected FormWithAjaxUpdatableComponent form;

	/**
	 * Es el nombre del componente que se debe actualizar.
	 */
	protected String feedbackComponentName;

	/**
	 * Constructor.
	 * 
	 * @param aForm
	 *            es el formulario al cual se ha agregado este objeto.
	 * @param aName
	 *            es el nombre del componente que se debe actualizar.
	 */
	public FormComponentAjaxBehavior(FormWithAjaxUpdatableComponent aForm,
			String aName) {
		super("onchange");
		this.setForm(aForm);
		this.setFeedbackComponentName(aName);

	}

	/**
	 * Notifica a este objeto que se ha actualizado el componente al cual se lo
	 * ha agregado.
	 * 
	 * @param aTarget
	 *            es el objeto que contiene todos los componentes que se deben
	 *            actualizar.
	 */
	@Override
	protected void onUpdate(AjaxRequestTarget aTarget) {

		aTarget.addComponent(this.getForm()
				.get(this.getFeedbackComponentName()));
		this.getForm().ajaxComponentUpdated();
	}

	/**
	 * Getter.
	 * 
	 * @return un string que contiene el markup que se debe incluir para este
	 *         objeto.
	 */
	@Override
	public String getAjaxIndicatorMarkupId() {
		return this.getForm().getMarkupIdForImageContainer();
	}

	/**
	 * Getter.
	 * 
	 * @return el formulario al cual pertenece este objeto.
	 */
	public FormWithAjaxUpdatableComponent getForm() {
		return this.form;
	}

	/**
	 * Setter.
	 * 
	 * @param aForm
	 *            es el formulario al cual se agrega este objeto.
	 */
	public void setForm(FormWithAjaxUpdatableComponent aForm) {
		this.form = aForm;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del componente que se debe actualizar.
	 */
	public String getFeedbackComponentName() {
		return this.feedbackComponentName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del componente que se debe actualizar.
	 */
	public void setFeedbackComponentName(String aName) {
		this.feedbackComponentName = aName;
	}

}
