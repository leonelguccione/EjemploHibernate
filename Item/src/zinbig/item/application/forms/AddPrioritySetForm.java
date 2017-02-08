/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.AddPrioritySetPage;
import zinbig.item.application.pages.EditPrioritySetPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.model.exceptions.PrioritySetNameNotUniqueException;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase de formulario se utilizan para dar de alta en
 * forma básica un nuevo grupo de prioridades. <BR>
 * Básica significa cargar solamente el nombre del grupo de prioridades. Dicho
 * nombre no se puede repetir entre todos los demás grupos. Este formulario
 * permite salvar el nuevo grupo y continuar cargando nuevos grupos, o salvar el
 * nuevo grupo e inmediatamente editarlo para poder asociarle el resto de los
 * datos que requiere.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddPrioritySetForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -6914574965383610292L;

	/**
	 * Es un campo que almacena el nombre provisto por el nuevo grupo de
	 * prioridades.
	 */
	public String nameAPSF;

	/**
	 * Indica si el grupo de prioridades por defecto o no.
	 */
	public boolean defaultPrioritySetAPSF;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 */
	@SuppressWarnings("unchecked")
	public AddPrioritySetForm(String anId) {
		super(anId);

		// construye la imagen indicadora para Ajax
		final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
		imageContainer.setOutputMarkupId(true);
		this.add(imageContainer);

		// construye el input field para el nombre del grupo de usuarios
		final TextField<String> nameField = new TextField<String>("nameAPSF",
				new PropertyModel<String>(this, "nameAPSF"));
		nameField.setRequired(true);
		nameField.add(new ItemSimpleStringValidator(nameField));
		nameField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddPrioritySetForm.name.length"))));
		nameField.add(new FormComponentAjaxBehavior(this, "nameAPSFFeedback"));

		this.add(nameField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del nombre del grupo.
		final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
				"nameAPSFFeedback", nameField);
		nameFeedbackLabel.setOutputMarkupId(true);
		this.add(nameFeedbackLabel);

		// construye el chekbox para definir si es el grupo de prioridades por
		// defecto o no.
		final CheckBox checkBox = new CheckBox("defaultPrioritySet",
				new PropertyModel(this, "defaultPrioritySetAPSF"));
		this.add(checkBox);

		// construye el link de envío del formulario. Este link retorna a la
		// misma página
		SubmitLink saveLink = new SubmitLink("saveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta un nuevo grupo de
			 * prioridades.
			 */
			@Override
			public void onSubmit() {
				try {
					AddPrioritySetForm.this.getPrioritiesService()
							.addPrioritySet(getNameAPSF(),
									isDefaultPrioritySetAPSF());

					setResponsePage(AddPrioritySetPage.class);

				} catch (PrioritySetNameNotUniqueException psnue) {
					AddPrioritySetForm.this.get("nameAPSF").error(
							getString("PrioritySetNameNotUniqueException"));
					psnue.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(saveLink);

		// construye el link de envío del formulario. Este link permite editar
		// el grupo recién creado.
		SubmitLink saveAndEditLink = new SubmitLink("saveAndEditLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para dar de alta un nuevo grupo de
			 * prioridades.
			 */
			@Override
			public void onSubmit() {
				try {
					PrioritySetDTO aDto = AddPrioritySetForm.this
							.getPrioritiesService().addPrioritySet(
									getNameAPSF(), isDefaultPrioritySetAPSF());

					PageParameters parameters = new PageParameters();
					parameters.put("prioritySetName", Utils.encodeString(aDto
							.getName()));
					setResponsePage(EditPrioritySetPage.class, parameters);

				} catch (PrioritySetNameNotUniqueException psnue) {

					AddPrioritySetForm.this.get("nameAPSF").error(
							getString("PrioritySetNameNotUniqueException"));
				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(saveAndEditLink);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del grupo.
	 */
	public String getNameAPSF() {
		return this.nameAPSF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del grupo.
	 */
	public void setNameAPSF(String aName) {
		this.nameAPSF = aName;
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo name por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de prioridades para verificar que no
	 * exista un usuario con el username ingresado. En caso de que exista ya un
	 * usuario se muestra un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (this.getPrioritiesService().containsPrioritySetWithName(
					getNameAPSF())) {
				this.get("nameAPSF").error(
						getString("PrioritySetNameNotUniqueException"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter.
	 * 
	 * @return el id del contenedor de la imagen utilizada por el componente
	 *         AJAX.
	 */
	public String getMarkupIdForImageContainer() {
		return this.get("indicatorImg").getMarkupId();
	}

	/**
	 * Getter.
	 * 
	 * @return una indicación de si el nuevo grupo de prioridades es el grupo
	 *         por default.
	 */
	public boolean isDefaultPrioritySetAPSF() {
		return this.defaultPrioritySetAPSF;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            true si el nuevo grupo debe ser el grupo de prioridades por
	 *            defecto; false en caso contrario.
	 */
	public void setDefaultPrioritySetAPSF(boolean aBoolean) {
		this.defaultPrioritySetAPSF = aBoolean;
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
