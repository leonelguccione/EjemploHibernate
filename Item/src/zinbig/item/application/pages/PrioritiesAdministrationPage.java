/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.PrioritySetsAdministrationForm;

/**
 * Las instancias de esta clase se utilizan para administrar las prioridades. <br>
 * Todas las páginas de administración siempre tienen la misma estructura: se
 * muestra una lista de los elementos con links que permiten borrar, o editar
 * elementos individuales. <br>
 * También se muestra un link para dar de alta un nuevo elemento.<br>
 * Esta clase implementa la interface Pageable ya que se le puede especificar la
 * cantidad de ítems que se deben mostrar en el listado. Si el usuario nunca
 * seleccionó un valor particular para la cantidad de ítems en el listado,
 * entonces se tomará el valor de la propiedad de sistema
 * PrioritiesAdministrationPage.itemsPerPage para el usuario en la sesión. En
 * caso de que alguna vez haya cambiado este valor por defecto, entonces se
 * recuperará dicho valor de las preferencias del usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrioritiesAdministrationPage extends SecuredPage implements
		Pageable {

	/**
	 * Mantiene la cantidad de ítems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Constructor.
	 */
	public PrioritiesAdministrationPage() {
		super();

		try {
			// recupera la cantidad de ítems que se deben mostrar en el listado.
			this
					.setItemsPerPage(new Integer(
							this
									.getPropertyValue("PrioritiesAdministrationPage.itemsPerPage"))
							.intValue());

			// crea el panel para la selección de la cantidad de ítems a mostrar
			// por página
			ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
					"itemsPerPageSelectionComponent", this);
			this.add(itemsPerPageComponent);

			// agrega el formulario de administración de proyectos
			this.add(new PrioritySetsAdministrationForm("listPrioritiesForm",
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
	 * @param aNumber
	 *            es la cantidad de elementos a mostrar por página.
	 */
	@Override
	public void updateItemsPerPage() {

		int itemsPerPage = this.getItemsPerPage();

		this.updateUserPreferences("PrioritiesAdministrationPage.itemsPerPage",
				new Integer(itemsPerPage).toString());

		PrioritySetsAdministrationForm aForm = (PrioritySetsAdministrationForm) this
				.get("listPrioritiesForm");
		aForm.updateItemsPerPage(itemsPerPage);

	}
}
