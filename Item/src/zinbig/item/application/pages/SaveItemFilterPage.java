/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.forms.ItemsAdministrationForm;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase se utilizan para poder cargar el nombre de un
 * neuvo filtro para guardarlo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SaveItemFilterPage extends WebPage {

	/**
	 * Es el nombre del nuevo filtro.
	 */
	public String filterName;

	/**
	 * Constructor.
	 * 
	 * @param aForm
	 *            es el formulario que lanzó esta ventana.
	 * @param window
	 *            es la ventana modal en la que se muestra esta página.
	 */
	@SuppressWarnings("unchecked")
	public SaveItemFilterPage(final ItemsAdministrationForm aForm,
			final ModalWindow window) {
		try {
			// agrega el componente para mostrar errores al usuario.
			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			TextField<String> nameField = new TextField<String>("filterName",
					new PropertyModel<String>(this, "filterName"));
			nameField.setOutputMarkupId(true);
			nameField.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Notifica al receptor que se está actualizando el componente.
				 */
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {

				}

			});
			nameField.setRequired(true);
			nameField.add(new ItemSimpleStringValidator(nameField));

			final Form form = new Form("form");
			form.add(feedback);
			form.add(nameField);
			form.add(new AjaxLink("closeOK") {
				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Notifica al receptor que se está haciendo click en el link.
				 */
				public void onClick(AjaxRequestTarget target) {
					if (aForm != null)
						if (getFilterName() != null) {
							aForm.saveFilterWithName(getFilterName(), window);

							window.close(target);
						} else {
							feedback.error(this
									.getString("SaveItemFilterPage.noName"));
							target.addComponent(feedback);
						}

				}
			});

			form.add(new AjaxLink("closeCancel") {
				/**
				 * UID por defecto.
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * Notifica al receptor que se ha hecho click en el link.
				 */
				@Override
				public void onClick(AjaxRequestTarget target) {
					if (aForm != null)

						window.close(target);
				}
			});
			this.add(form);

		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del filtro.
	 */
	public String getFilterName() {
		return this.filterName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del filtro.
	 */
	public void setFilterName(String aName) {
		this.filterName = aName;
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
				.write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[2].focus()</script>");
	}

}
