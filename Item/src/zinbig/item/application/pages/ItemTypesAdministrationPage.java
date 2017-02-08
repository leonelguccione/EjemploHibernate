/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.ItemTypesAdministrationForm;

/**
 * Las instancias de esta clase se utilizan para administrar los tipos de ítems
 * que luego se utilizan para crear enlaces en la descripción del workflow.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTypesAdministrationPage extends SecuredPage implements
		Pageable {

	/**
	 * Mantiene la cantidad de ítems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Constructor.
	 */
	public ItemTypesAdministrationPage() {
		super();

		try {
			this
					.setItemsPerPage(new Integer(
							this
									.getPropertyValue("ItemTypesAdministrationPage.itemsPerPage"))
							.intValue());

			// crea el panel para la selección de la cantidad de ítems a mostrar
			// por
			// página
			ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
					"itemsPerPageSelectionComponent", this);
			this.add(itemsPerPageComponent);

			// agrega el formulario de administración de grupos de usuarios.
			this
					.add(new ItemTypesAdministrationForm("listItemTypesForm",
							this));
		} catch (Exception e) {
			setResponsePage(DashboardPage.class);
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
	 */
	@Override
	public void updateItemsPerPage() {

		int itemsPerPage = this.getItemsPerPage();

		this.updateUserPreferences("ItemTypesAdministrationPage.itemsPerPage",
				new Integer(itemsPerPage).toString());

		ItemTypesAdministrationForm aForm = (ItemTypesAdministrationForm) this
				.get("listItemTypesForm");
		aForm.updateItemsPerPage(itemsPerPage);

	}

}
