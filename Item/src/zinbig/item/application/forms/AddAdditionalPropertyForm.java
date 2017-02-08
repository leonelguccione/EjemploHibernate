/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.ManageAdditionalPropertiesPanel;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.model.exceptions.InvalidDynamicQueryException;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de alta una
 * nueva propiedad adicional para los ítems.<br>
 * Para dar de alta una nueva propiedad se deben ingresar el nombre y si es
 * requerida o no. Además, se debe elegir un tipo para la propiedad adicional:
 * Simple (para un input field tradicional); TextArea (para un text area); Lista
 * fija (para un listado de valores fijos) o Lista dinámica (para un listado
 * cuyos valores se obtienen mediante una consulta sql).
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddAdditionalPropertyForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 4506320894068976304L;

	/**
	 * Mantiene la referencia al proyecto que se está editando.
	 */
	public ProjectDTO projectDTO;

	/**
	 * Es un campo que almacena el nombre provisto para la nueva prioridad.
	 */
	public String nameAAPF;

	/**
	 * Es un campo que almacena valor requerido por la propiedad en caso de que
	 * sea una lista fija de valores o una consulta SQL..
	 */
	public String valueAAPF;

	/**
	 * Indica si la propiedad que se está agregando es requerida o no.
	 */
	public boolean requiredProperty;

	/**
	 * Mantiene la selección de qué tipo de propiedad se está agregando.
	 */
	public String selectedPropertyType;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto que se está editando.
	 */
	@SuppressWarnings("unchecked")
	public AddAdditionalPropertyForm(String anId, ProjectDTO aProjectDTO) {
		super(anId);
		this.setProjectDTO(aProjectDTO);

		// construye el input field para el nombre de la nueva propiedad
		final TextField<String> nameField = new TextField<String>("nameAAPF",
				new PropertyModel<String>(this, "nameAAPF"));
		nameField.setRequired(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		nameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddAdditionalPropertyForm.name.length"))));

		this.add(nameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre de la propiedad.
		final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
				"nameAAPFFeedback", nameField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(nameFeedbackLabel);

		// construye el chekbox para definir si es una propiedad requerida o no.
		final CheckBox checkBox = new CheckBox("requiredProperty",
				new PropertyModel(this, "requiredProperty"));
		this.add(checkBox);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente checkbox.
		final FeedbackLabel requiredFeedbackLabel = new FeedbackLabel(
				"requiredPropertyAAPFFeedback", checkBox);
		requiredFeedbackLabel.setOutputMarkupId(true);
		this.add(requiredFeedbackLabel);

		// crea un modelo para acceder a la lista de tipos de propiedades
		// adicionales
		IModel<List<String>> typeChoices = new AbstractReadOnlyModel<List<String>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de tipos de propiedades.
			 * 
			 * @return una lista de strings que representan los diferentes tipos
			 *         de propiedades adicionales.
			 * 
			 */
			@Override
			public List<String> getObject() {
				List<String> types = new ArrayList<String>();
				types.add("SIMPLE");
				types.add("TEXTAREA");
				types.add("FIXED");
				types.add("DYNAMIC");
				return types;
			}

		};

		// construye la lista para mostrar todas los tipos propiedades
		// adicionales.
		IChoiceRenderer itemTypeRenderer = new ChoiceRenderer() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 446625613367370117L;

			/**
			 * Getter.
			 * 
			 * @return el valor que se debe mostrar para la opción recibida.
			 * 
			 */
			@Override
			public Object getDisplayValue(Object object) {

				return getString((String) object);
			}

			/**
			 * Getter.
			 * 
			 * @return el valor que se debe asociar con la opción recibida.
			 */
			@Override
			public String getIdValue(Object object, int index) {

				return (String) object;
			}
		};
		DropDownChoice typesList = new DropDownChoice("type",
				new PropertyModel(this, "selectedPropertyType"), typeChoices,
				itemTypeRenderer);
		typesList.setOutputMarkupId(true);
		typesList.setRequired(true);
		this.add(typesList);

		typesList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Manejador del evento de actualización de la lista de proyectos.
			 * Se actualiza la lista de prioridades con las prioridades
			 * correspondientes al proyecto seleccionado.
			 * 
			 * @param target
			 *            es el objetivo del pedido AJAX.
			 */
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				try {
					String selectedType = AddAdditionalPropertyForm.this
							.getSelectedPropertyType();
					boolean enabled = false;
					if (selectedType.equals("FIXED")
							| selectedType.equals("DYNAMIC")) {
						enabled = true;
					} else {
						enabled = false;
					}
					AddAdditionalPropertyForm.this.get("valueAAPF").setEnabled(
							enabled);
					target.addComponent(AddAdditionalPropertyForm.this
							.get("valueAAPF"));
					target.addComponent(AddAdditionalPropertyForm.this
							.get("valueAAPFFeedback"));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del tipo de la propiedad.
		final FeedbackLabel typeFeedbackLabel = new FeedbackLabel(
				"typeAAPFFeedback", typesList);
		typeFeedbackLabel.setOutputMarkupId(true);
		this.add(typeFeedbackLabel);

		// construye el input field para el valor adicional de la propiedad
		final TextArea<String> valueField = new TextArea<String>("valueAAPF",
				new PropertyModel<String>(this, "valueAAPF"));
		valueField.setRequired(true);
		valueField.setOutputMarkupId(true);
		valueField.setEnabled(false);
		this.add(valueField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del valor adicional de la propiedad.
		final FeedbackLabel valueFeedbackLabel = new FeedbackLabel(
				"valueAAPFFeedback", valueField);
		valueFeedbackLabel.setOutputMarkupId(true);
		this.add(valueFeedbackLabel);

		// construye el link de envío del formulario. Este link retorna a la
		// misma página
		SubmitLink saveLink = new SubmitLink("saveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta una nueva propiedad
			 * adicional.
			 */
			@Override
			public void onSubmit() {

				try {

					if (AddAdditionalPropertyForm.this
							.getSelectedPropertyType().equals("FIXED")
							| AddAdditionalPropertyForm.this
									.getSelectedPropertyType()
									.equals("DYNAMIC")) {
						if (AddAdditionalPropertyForm.this.getValueAAPF() == null) {
							AddAdditionalPropertyForm.this
									.get("valueAAPF")
									.error(
											this
													.getString("requiredAdditionalField"));
						}
					}

					AddAdditionalPropertyForm.this.getProjectsService()
							.addAdditionalProperty(getProjectDTO(),
									getNameAAPF(), isRequiredProperty(),
									getSelectedPropertyType(), getValueAAPF());

					AddAdditionalPropertyForm.this.setNameAAPF(null);
					AddAdditionalPropertyForm.this.setValueAAPF(null);
					AddAdditionalPropertyForm.this.setRequiredProperty(false);
					AddAdditionalPropertyForm.this.setValueAAPF(null);

					((ManageAdditionalPropertiesPanel) AddAdditionalPropertyForm.this
							.getParent()).updateListOfProperties();

				} catch (InvalidDynamicQueryException idqe) {
					AddAdditionalPropertyForm.this.get("valueAAPF").error(
							this.getString("invalidDynamicQuery"));
				}

				catch (Exception e) {
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
	 * @return el DTO que representa al proyecto que se está editando.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el DTO que representa al proyecto que se está editando.
	 */
	public void setProjectDTO(ProjectDTO aDTO) {
		this.projectDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la prioridad.
	 */
	public String getNameAAPF() {
		return this.nameAAPF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la prioridad.
	 */
	public void setNameAAPF(String aName) {
		this.nameAAPF = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de la que la propiedad adicional sea requerida;
	 *         false en caso contrario.
	 */
	public boolean isRequiredProperty() {
		return this.requiredProperty;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si la propiedad adicional es requerida o no.
	 */
	public void setRequiredProperty(boolean aBoolean) {
		this.requiredProperty = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el tipo de propiedad adicional seleccionado.
	 */
	public String getSelectedPropertyType() {
		return this.selectedPropertyType;
	}

	/**
	 * Setter.
	 * 
	 * @param aPropertyType
	 *            es el tipo de propiedad adicional seleccionado.
	 */
	public void setSelectedPropertyType(String aPropertyType) {
		this.selectedPropertyType = aPropertyType;
	}

	/**
	 * Getter.
	 * 
	 * @return el valor adicional de la propiedad seleccionada.
	 */
	public String getValueAAPF() {
		return this.valueAAPF;
	}

	/**
	 * Setter.
	 * 
	 * @param aValue
	 *            es el valor adicional de la propiedad seleccionada.
	 */
	public void setValueAAPF(String aValue) {
		this.valueAAPF = aValue;
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
