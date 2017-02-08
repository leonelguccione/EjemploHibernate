/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicaci�n.
 */
package zinbig.item.application.forms;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.pages.ViewItemsPage;

/**
 * Las instancias de este formulario se utilizan para permitir al usuario buscar
 * �tems por su id.<BR>
 * Si no hay usuario en la sesi�n, se retornan todos los �tems cuyos ids
 * correspondan al id ingresado, pero solo contemplando los proyectos p�blicos.<Br>
 * Si hay usuario en la sesi�n, entonces se consideran todos los proyectos del
 * usuario si no tiene ning�n proyecto por defecto. En el caso de tener uno ya
 * seleccionado solamente se busca en dicho proyecto.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class SearchItemForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -1314522646644217130L;

	/**
	 * Mantiene el string por el que se debe buscar los �tems.
	 */
	public String searchString;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 */
	public SearchItemForm(String anId) {
		super(anId);

		TextField<String> searchInput = new TextField<String>("searchInput",
				new PropertyModel<String>(this, "searchString"));
		searchInput.setOutputMarkupId(true);
		this.add(searchInput);

		// agrega el link que permite enviar el formulario
		Button submitButton = new Button("submitButton") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Env�a el formulario.
			 */
			@Override
			public void onSubmit() {
				if (searchString != null) {
					String projectOid = null;
					String projectTitle = null;
					if (SearchItemForm.this.getProjectDTO() == null) {

						projectTitle = this.getString("ANY_PROJECT");
						projectOid = "";
					} else {

						projectTitle = SearchItemForm.this.getProjectDTO()
								.getName();
						projectOid = SearchItemForm.this.getProjectDTO()
								.getOid();
					}

					try {

						PageParameters parameters = new PageParameters();
						parameters.put("FILTER_DTO_TITLE", projectTitle);
						parameters.put("FILTER_DTO_OID", projectOid);
						parameters.put("FILTER_DTO_SEARCH_STRING",
								SearchItemForm.this.getSearchString());
						this.setResponsePage(ViewItemsPage.class, parameters);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		};
		this.add(submitButton);
	}

	/**
	 * Getter.
	 * 
	 * @return el string por el que hay que buscar los �tems.
	 */
	public String getSearchString() {
		return this.searchString;
	}

	/**
	 * Setter.
	 * 
	 * @param aSearchString
	 *            es el string por el que hay que buscar los �tems.
	 */
	public void setSearchString(String aSearchString) {
		this.searchString = aSearchString;
	}

}
