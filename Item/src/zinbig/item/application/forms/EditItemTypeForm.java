/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicaci�n.
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
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.ItemTypesAdministrationPage;
import zinbig.item.model.exceptions.ItemTypeTitleNotUniqueException;
import zinbig.item.model.exceptions.ItemTypeUnknownException;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase se utilizan para editar un tipo de �tem en la
 * aplicaci�n. <br>
 * 
 * La �nica restricci�n que se aplica es que no se est� utilizando ya el mismo
 * t�tulo en otro tipo existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemTypeForm extends ItemForm implements
		FormWithAjaxUpdatableComponent {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -2905918208437894415L;

	/**
	 * Es un campo que almacena el t�tulo del tipo de �tem.
	 */
	public String titleEITF;

	/**
	 * Es el oide del tipo de �tem que se est� editando.
	 */
	public String itemTypeOid;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este formulario dentro del panel.
	 * @param anItemTypeOid
	 *            es el oid del tipo de �tem que se est� editando.
	 */
	@SuppressWarnings("unchecked")
	public EditItemTypeForm(String id, String anItemTypeOid) {
		super(id);

		this.setItemTypeOid(anItemTypeOid);

		try {
			final ItemTypeDTO anItemTypeDTO = this.getItemsService()
					.findItemTypeByOid(anItemTypeOid);
			this.setTitleEITF(anItemTypeDTO.getTitle());

			// construye el input field para el t�tulo
			final TextField<String> titleField = new TextField<String>(
					"titleEITF", new PropertyModel<String>(this, "titleEITF"));
			titleField.setRequired(true);
			titleField.setOutputMarkupId(true);
			titleField.add(new ItemSimpleStringValidator(titleField));
			titleField.add(StringValidator.maximumLength(new Integer(this
					.getSystemProperty("AddItemTypeForm.name.length"))));
			titleField.add(new FormComponentAjaxBehavior(this,
					"titleEITFFeedback"));
			this.add(titleField);

			// construye el label para mostrar los mensajes de error
			// relacionados
			// con el componente del t�tulo.
			final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
					"titleEITFFeedback", titleField);
			titleFeedbackLabel.setOutputMarkupId(true);
			this.add(titleFeedbackLabel);

			// construye la imagen indicadora para Ajax
			final AjaxIndicator imageContainer = new AjaxIndicator(
					"indicatorImg");
			imageContainer.setOutputMarkupId(true);
			this.add(imageContainer);

			// construye el link de env�o del formulario
			SubmitLink submitLink = new SubmitLink("submitLink") {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Env�a los datos del formulario para intentar editar el tipo
				 * de �tem.
				 */
				@Override
				public void onSubmit() {
					try {

						EditItemTypeForm.this.getItemsService().editItemType(
								anItemTypeDTO, getTitleEITF());

						setResponsePage(ItemTypesAdministrationPage.class);
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

			// construye el link de cancelaci�n del formulario
			Link cancelLink = new BookmarkablePageLink("cancelLink",
					ItemTypesAdministrationPage.class);
			this.add(cancelLink);
		} catch (ItemTypeUnknownException itue) {
			itue.printStackTrace();
			this.setResponsePage(ItemTypesAdministrationPage.class);
		} catch (Exception e) {

			e.printStackTrace();
			this.setResponsePage(ErrorPage.class);
		}

	}

	/**
	 * Este m�todo se dispara a ra�z de la actualizaci�n del campo title por
	 * parte de un evento AJAX.<br>
	 * Este m�todo invoca al servicio de �tems para verificar que no exista un
	 * tipo de �tem con el t�tulo ingresado. En caso de que exista ya se muestra
	 * un cartel de error.
	 */
	public void ajaxComponentUpdated() {

		try {
			if (this.getItemsService().existsItemTypeWithTitle(
					this.getTitleEITF())) {

				this.get("titleEITF").error(
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
	 * @return el t�tulo del tipo de �tem.
	 */
	public String getTitleEITF() {
		return this.titleEITF;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo del tipo de �tem.
	 */
	public void setTitleEITF(String aTitle) {
		this.titleEITF = aTitle;
	}

	/**
	 * Este m�todo es ejecutado al finalizar la creaci�n del html
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
	 * @return el oid del tipo de �tem que se est� editando.
	 */
	public String getItemTypeOid() {
		return this.itemTypeOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid del tipo del �tem que se est� editando.
	 */
	public void setItemTypeOid(String anOid) {
		this.itemTypeOid = anOid;
	}

}
