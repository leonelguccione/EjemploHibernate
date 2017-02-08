/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.ManageItemTypesPanel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.AddItemTypePage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.ItemTypesAdministrationPage;
import zinbig.item.model.exceptions.ItemTypeTitleNotUniqueException;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase se utilizan para registrar un nuevo tipo de ítem
 * en la aplicación. <br>
 * Si el dto del proyecto que se pasa en el constructor es nulo entonces los
 * tipos de ítems se agregan al sistema y no a un proyecto determinado.
 * 
 * La única restricción que se aplica en el alta de un nuevo tipo de ítem es que
 * no se esté utilizando ya el mismo título en otro tipo existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddItemTypeForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 7953278024769115081L;

	/**
	 * Es un campo que almacena el título del nuevo tipo de ítem.
	 */
	public String titleAITF;

	/**
	 * Es el dto del proyecto a donde se agregarán los tipos de ítems.
	 */
	public ProjectDTO projectDTO;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este formulario dentro del panel.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto en donde se podrían estar
	 *            agregando nuevos tipos. Si este dto es nulo entonces se
	 *            agregan los tipos de ítem al sistema.
	 */
	@SuppressWarnings("unchecked")
	public AddItemTypeForm(String id, ProjectDTO aProjectDTO) {
		super(id);

		this.setProjectDTO(aProjectDTO);

		// construye el input field para el título
		final TextField<String> titleField = new TextField<String>("titleAITF",
				new PropertyModel<String>(this, "titleAITF"));
		titleField.setRequired(true);
		titleField.setOutputMarkupId(true);
		titleField.add(new ItemSimpleStringValidator(titleField));
		titleField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddItemTypeForm.name.length"))));
		titleField
				.add(new FormComponentAjaxBehavior(this, "titleAITFFeedback"));
		this.add(titleField);

		// construye el label para mostrar los mensajes de error relacionados
		// con el componente del título.
		final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
				"titleAITFFeedback", titleField);
		titleFeedbackLabel.setOutputMarkupId(true);
		this.add(titleFeedbackLabel);

		// construye la imagen indicadora para Ajax
		final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
		imageContainer.setOutputMarkupId(true);
		this.add(imageContainer);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía los datos del formulario para intentar dar de alta el nuevo
			 * tipo de ítem.
			 */
			@Override
			public void onSubmit() {
				try {
					ItemsServiceBI service = AddItemTypeForm.this
							.getItemsService();
					if (AddItemTypeForm.this.getProjectDTO() == null) {
						service.addItemType(getTitleAITF());

						setResponsePage(AddItemTypePage.class);
					} else {
						service.addItemTypeToProject(getTitleAITF(),
								AddItemTypeForm.this.getProjectDTO());
						AddItemTypeForm.this.setTitleAITF(null);
						((ManageItemTypesPanel) AddItemTypeForm.this
								.getParent()).updateListOfItemTypes();
					}

				} catch (ItemTypeTitleNotUniqueException nuue) {
					nuue.printStackTrace();
					error(getString("ItemTypeTitleNotUniqueException"));
				} catch (Exception e) {
					e.printStackTrace();
					setResponsePage(ErrorPage.class);
				}
			}

		};
		this.add(submitLink);

		// construye el link de cancelación del formulario
		Link cancelLink = new BookmarkablePageLink("cancelLink",
				ItemTypesAdministrationPage.class);
		this.add(cancelLink);

	}

	/**
	 * Este método se dispara a raíz de la actualización del campo title por
	 * parte de un evento AJAX.<br>
	 * Este método invoca al servicio de ítems para verificar que no exista un
	 * tipo de ítem con el título ingresado. En caso de que exista ya se muestra
	 * un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {

			if (this.getItemsService().existsItemTypeWithTitleInProject(
					this.getProjectDTO(), this.getTitleAITF())) {

				this.get("titleAITF").error(
						getString("ItemTypeTitleNotUniqueException"));

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
	 * @return el título del nuevo tipo de ítem.
	 */
	public String getTitleAITF() {
		return this.titleAITF;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del nuevo tipo de ítem.
	 */
	public void setTitleAITF(String aTitle) {
		this.titleAITF = aTitle;
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

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto a donde se agregarán los tipos de ítems.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el dto del proyecto a donde se agregarán los tipos de
	 *            ítems.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

}
