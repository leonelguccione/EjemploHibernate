/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.UserGroupsAdministrationForm;

/**
 * Las instancias de esta página se utilizan para administrar los grupos de
 * usuarios de la aplicación.<br>
 * Toda página de administración por defecto presenta un listado de todos los
 * elementos, links que permiten editar o borrar alguno de los elementos y un
 * link para poder agregar un nuevo elemento.<br>
 * Esta clase implementa la interface Pageable ya que se le puede especificar la
 * cantidad de ítems que se deben mostrar en el listado. Si el usuario nunca
 * seleccionó un valor particular para la cantidad de ítems en el listado,
 * entonces se tomará el valor de la propiedad de sistema
 * UserGroupsAdministrationPage.itemsPerPage para el usuario en la sesión. En
 * caso de que alguna vez haya cambiado este valor por defecto, entonces se
 * recuperará dicho valor de las preferencias del usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserGroupsAdministrationPage extends SecuredPage implements
		Pageable {

	/**
	 * Mantiene la cantidad de ítems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Constructor.
	 */
	public UserGroupsAdministrationPage() {
		super();

		try {
			// recupera la cantidad de ítems que se deben mostrar en el listado.
			this
					.setItemsPerPage(new Integer(
							this
									.getPropertyValue("UserGroupsAdministrationPage.itemsPerPage"))
							.intValue());

			// crea el panel para la selección de la cantidad de ítems a mostrar
			// por
			// página
			ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
					"itemsPerPageSelectionComponent", this);
			this.add(itemsPerPageComponent);

			// agrega el formulario de administración de grupos de usuarios.
			this.add(new UserGroupsAdministrationForm("listUserGroupsForm",
					this));

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

		this.updateUserPreferences("UserGroupsAdministrationPage.itemsPerPage",
				new Integer(itemsPerPage).toString());

		UserGroupsAdministrationForm aForm = (UserGroupsAdministrationForm) this
				.get("listUserGroupsForm");
		aForm.updateItemsPerPage(itemsPerPage);

	}
}
