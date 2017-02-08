/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.ItemTypesAdministrationForm;

/**
 * Las instancias de esta clase se utilizan para administrar los tipos de �tems
 * que luego se utilizan para crear enlaces en la descripci�n del workflow.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTypesAdministrationPage extends SecuredPage implements
		Pageable {

	/**
	 * Mantiene la cantidad de �tems que se deben mostrar en el listado.
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

			// crea el panel para la selecci�n de la cantidad de �tems a mostrar
			// por
			// p�gina
			ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
					"itemsPerPageSelectionComponent", this);
			this.add(itemsPerPageComponent);

			// agrega el formulario de administraci�n de grupos de usuarios.
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
	 * @return la cantidad de �tems que se deben mostrar en la p�gina.
	 */
	public int getItemsPerPage() {
		return this.itemsPerPage;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de �tems que se deben mostrar por cada p�gina.
	 */
	public void setItemsPerPage(int aNumber) {
		this.itemsPerPage = aNumber;
	}

	/**
	 * Actualiza la cantidad de elementos que se muestran en el listado. Adem�s
	 * si la cantidad de elementos que se muestran es menor que la cantidad
	 * total de elementos se pone invisible el componente de navegaci�n de
	 * p�ginas.
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
