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
import zinbig.item.application.pages.EditPrioritySetPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.PrioritiesAdministrationPage;
import zinbig.item.model.exceptions.PrioritySetNameNotUniqueException;
import zinbig.item.model.exceptions.PrioritySetUnknownException;
import zinbig.item.services.ServiceLocator;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase de formulario se utilizan para editar en forma
 * básica un nuevo grupo de prioridades y definir si será el conjunto de
 * prioridades por defecto. <BR>
 * Básica significa editar solamente el nombre del grupo de prioridades. Dicho
 * nombre no se puede repetir entre todos los demás grupos. <br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditPrioritySetForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2207220086018271600L;

	/**
	 * Es un campo que almacena el nuevo nombre.
	 */
	public String nameEPSF;

	/**
	 * Indica si el grupo de prioridades por defecto o no.
	 */
	public boolean defaultPrioritySetEPSF;

	/**
	 * Mantiene la referencia al dto que representa al conjunto de prioridades.
	 */
	public PrioritySetDTO prioritySetDTO;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades.
	 */
	@SuppressWarnings("unchecked")
	public EditPrioritySetForm(String anId, String aPrioritySetId) {
		super(anId);

		try {
			// recupera el conjunto de prioridades a partir del nombre.
			PrioritySetDTO aPrioritySetDTO = ServiceLocator.getInstance()
					.getPrioritiesService().findPrioritySetById(aPrioritySetId);
			this.setPrioritySetDTO(aPrioritySetDTO);

			// construye la imagen indicadora para Ajax
			final AjaxIndicator imageContainer = new AjaxIndicator(
					"indicatorImg");
			imageContainer.setOutputMarkupId(true);
			this.add(imageContainer);

			// construye el input field para el nombre del grupo de usuarios
			final TextField<String> nameField = new TextField<String>(
					"nameEPSF", new PropertyModel<String>(this, "nameEPSF"));
			nameField.setRequired(true);
			nameField.add(new ItemSimpleStringValidator(nameField));
			nameField.add(StringValidator.maximumLength(new Integer(this
					.getSystemProperty("AddPrioritySetForm.name.length"))));
			nameField.add(new FormComponentAjaxBehavior(this,
					"nameEPSFFeedback"));
			this.setNameEPSF(aPrioritySetDTO.getName());

			this.add(nameField);

			// construye el label para mostrar los mensajes de error
			// relacionados
			// con el componente del nombre del grupo.
			final FeedbackLabel nameFeedbackLabel = new FeedbackLabel(
					"nameEPSFFeedback", nameField);
			nameFeedbackLabel.setOutputMarkupId(true);
			this.add(nameFeedbackLabel);

			// construye el chekbox para definir si es el grupo de prioridades
			// por
			// defecto o no.
			final CheckBox checkBox = new CheckBox("defaultPrioritySet",
					new PropertyModel(this, "defaultPrioritySetEPSF"));
			this.add(checkBox);
			this.setDefaultPrioritySetEPSF(aPrioritySetDTO
					.isDefaultPrioritySet());
			if (aPrioritySetDTO.isDefaultPrioritySet()) {
				checkBox.setEnabled(false);
			}

			// construye el link de cancelación del formulario.
			SubmitLink cancelLink = new SubmitLink("cancelLink") {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Cancela la edición de un conjunto de prioridades y muestra la
				 * página de administración de prioridades.
				 */
				@Override
				public void onSubmit() {

					setResponsePage(PrioritiesAdministrationPage.class);

				}

			};
			this.add(cancelLink);

			// construye el link de envío del formulario. Este link permite
			// editar
			// el grupo
			SubmitLink saveLink = new SubmitLink("saveLink") {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Envía el formulario para editar el grupo.
				 */
				@Override
				public void onSubmit() {
					try {
						getPrioritySetDTO().setName(getNameEPSF());
						getPrioritySetDTO().setDefaultPrioritySet(
								isDefaultPrioritySetEPSF());

						EditPrioritySetForm.this.getPrioritiesService()
								.editPrioritySet(getPrioritySetDTO());

						PageParameters parameters = new PageParameters();
						parameters.put("prioritySetName", Utils
								.encodeString(getPrioritySetDTO().getName()));
						setResponsePage(EditPrioritySetPage.class, parameters);

					} catch (PrioritySetNameNotUniqueException psnue) {

						EditPrioritySetForm.this.get("nameEPSF").error(
								getString("PrioritySetNameNotUniqueException"));
					} catch (Exception e) {
						e.printStackTrace();
						setResponsePage(ErrorPage.class);
					}
				}

			};
			this.add(saveLink);
		} catch (PrioritySetUnknownException e) {
			error(this.getString("PrioritySetUnknownException"));
			e.printStackTrace();
			this.setResponsePage(ErrorPage.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del grupo.
	 */
	public String getNameEPSF() {
		return this.nameEPSF;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del grupo.
	 */
	public void setNameEPSF(String aName) {
		this.nameEPSF = aName;
	}

	/**
	 * Este método se dispara a raíz de la actualización del campo name por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de usuarios para verificar que no exista
	 * un usuario con el username ingresado. En caso de que exista ya un usuario
	 * se muestra un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {

			if (!this.getPrioritySetDTO().getName().equals(this.getNameEPSF())) {

				if (EditPrioritySetForm.this.getPrioritiesService()
						.containsPrioritySetWithName(this.getNameEPSF())) {
					this.get("nameEPSF").error(
							getString("PrioritySetNameNotUniqueException"));
				}
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
	 * @return una indicación de si el grupo de prioridades es el grupo por
	 *         default.
	 */
	public boolean isDefaultPrioritySetEPSF() {
		return this.defaultPrioritySetEPSF;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            true si el grupo debe ser el grupo de prioridades por defecto;
	 *            false en caso contrario.
	 */
	public void setDefaultPrioritySetEPSF(boolean aBoolean) {
		this.defaultPrioritySetEPSF = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al conjunto de prioridades que se está
	 *         editando.
	 */
	public PrioritySetDTO getPrioritySetDTO() {
		return this.prioritySetDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aPrioritySetDTO
	 *            es el dto que representa al conjunto de prioridaes que se está
	 *            editando.
	 */
	public void setPrioritySetDTO(PrioritySetDTO aPrioritySetDTO) {
		this.prioritySetDTO = aPrioritySetDTO;
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
