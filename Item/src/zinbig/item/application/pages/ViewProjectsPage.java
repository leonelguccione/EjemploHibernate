/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.ListProjectsForm;

/**
 * Las instancias de esta clase se utilizan para visualizar los proyectos. <br>
 * Esta clase implementa la interface Pageable ya que se le puede especificar la
 * cantidad de elementos que se deben mostrar en el listado. Si el usuario nunca
 * seleccionó un valor particular para la cantidad de elementos en el listado,
 * entonces se tomará el valor de la propiedad de sistema
 * ViewProjectsPage.itemsPerPage para el usuario en la sesión. En caso de que
 * alguna vez haya cambiado este valor por defecto, entonces se recuperará dicho
 * valor de las preferencias del usuario.<br>
 * 
 * Si al acceder a esta página no se cuenta con el dto de un usuario en la
 * sesión, entonces se muestran solamente los proyectos públicos. En cambio si
 * existe un usuario, se mostrarán tanto los proyectos públicos como los
 * proyectos privados a los que está asignado el usuario.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ViewProjectsPage extends BasePage implements Pageable {

	/**
	 * Mantiene la cantidad de ítems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Constructor.
	 */
	public ViewProjectsPage() {
		super();
		try {
			// recupera la cantidad de ítems que se deben mostrar en el listado.
			this.setItemsPerPage(new Integer(this
					.getPropertyValue("ViewProjectsPage.itemsPerPage"))
					.intValue());

			// crea el panel para la selección de la cantidad de ítems a mostrar
			// por
			// página
			ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
					"itemsPerPageSelectionComponent", this);
			this.add(itemsPerPageComponent);

			// agrega el formulario.
			ListProjectsForm form = new ListProjectsForm("listProjectsForm",
					this);
			this.add(form);

		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de ítems que se deben mostrar en la página.
	 */
	public int getItemsPerPage() {
		return this.itemsPerPage;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de ítems que se deben mostrar por cada página.
	 */
	public void setItemsPerPage(int aNumber) {
		this.itemsPerPage = aNumber;
	}

	/**
	 * Actualiza la cantidad de elementos que se muestran en el listado. Además
	 * si la cantidad de elementos que se muestran es menor que la cantidad
	 * total de elementos se pone invisible el componente de navegación de
	 * páginas.
	 * 
	 * 
	 */
	@Override
	public void updateItemsPerPage() {
		int itemsPerPage = this.getItemsPerPage();

		this.updateUserPreferences("ViewProjectsPage.itemsPerPage",
				new Integer(itemsPerPage).toString());

		ListProjectsForm aForm = (ListProjectsForm) this
				.get("listProjectsForm");
		aForm.updateItemsPerPage(itemsPerPage);

	}

}
