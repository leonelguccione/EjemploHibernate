/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.ManagePrioritiesPanel;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de alta una
 * nueva prioridad dentro de un conjunto de prioridades. <BR>
 * Las reglas del dominio establecen que no pueden existir dos prioridades con
 * el mismo nombre dentro del mismo conjunto, por lo que es se debe verificar
 * esta situación.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddPriorityForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8954503801922309543L;

	/**
	 * Es un campo que almacena el nombre provisto para la nueva prioridad.
	 */
	public String nameAPF;

	/**
	 * Es un campo que almacena el valor de la nueva prioridad.
	 */
	public String valueAPF;

	/**
	 * Es el id del conjunto de prioridades al cual se debe agregar la nueva
	 * prioridad.
	 */
	public String prioritySetId;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param aPrioritySetId
	 *            es el nombre del conjunto de prioridades al cual se debe
	 *            agregar la nueva prioridad.
	 */
	public AddPriorityForm(String anId, String aPrioritySetId) {
		super(anId);

		this.setPrioritySetId(aPrioritySetId);

		// construye el input field para el nombre de la prioridad
		final TextField<String> nameField = new TextField<String>("nameAPF",
				new PropertyModel<String>(this, "nameAPF"));
		nameField.setRequired(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		nameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddPriorityForm.name.length"))));

		this.add(nameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre de la prioridad.
		final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
				"nameAPFFeedback", nameField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(nameFeedbackLabel);

		// construye el input field para el valor de la prioridad
		final TextField<Integer> valueField = new TextField<Integer>(
				"valueAPF", new PropertyModel<Integer>(this, "valueAPF"),
				Integer.class);
		valueField.setRequired(true);
		this.add(valueField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre de la prioridad.
		final FeedbackLabel valueFeedbackLabel = new FeedbackLabel(
				"valueAPFFeedback", valueField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(valueFeedbackLabel);

		// construye el link de envío del formulario. Este link retorna a la
		// misma página
		SubmitLink saveLink = new SubmitLink("saveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta una nueva prioridad.
			 */
			@Override
			public void onSubmit() {

				try {

					AddPriorityForm.this.getPrioritiesService()
							.addPriorityToPrioritySet(
									AddPriorityForm.this.getPrioritySetId(),
									AddPriorityForm.this.getNameAPF(),
									AddPriorityForm.this.getValueAPF());

					AddPriorityForm.this.setNameAPF(null);
					AddPriorityForm.this.setValueAPF(null);

					((ManagePrioritiesPanel) AddPriorityForm.this.getParent())
							.updateListOfPriorities();

				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(saveLink);

	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la prioridad.
	 */
	public String getNameAPF() {
		return this.nameAPF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la prioridad.
	 */
	public void setNameAPF(String aName) {
		this.nameAPF = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el valor cargado para la nueva prioridad.
	 */
	public String getValueAPF() {
		return this.valueAPF;
	}

	/**
	 * Setter.
	 * 
	 * @param aValue
	 *            es el valor cargado para la nueva prioridad.
	 */
	public void setValueAPF(String aValue) {
		this.valueAPF = aValue;
	}

	/**
	 * Getter.
	 * 
	 * @return el id del conjunto de prioridades al cual se está agregando la
	 *         nueva prioridad.
	 */
	public String getPrioritySetId() {
		return this.prioritySetId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id del conjunto de prioridades al cual se está agregando
	 *            la nueva prioridad.
	 */
	public void setPrioritySetId(String anId) {
		this.prioritySetId = anId;
	}

	/**
	 * Este método es ejecutado al finalizar la creación del html
	 * correspondiente a este componente.
	 */
	@Override
	protected void onAfterRender() {

		super.onAfterRender();
		final Response response = this.getResponse();
		response
				.write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[1].focus()</script>");
	}

}
