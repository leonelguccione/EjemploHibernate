/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.ListProjectsForm;

/**
 * Las instancias de esta clase se utilizan para visualizar los proyectos. <br>
 * Esta clase implementa la interface Pageable ya que se le puede especificar la
 * cantidad de elementos que se deben mostrar en el listado. Si el usuario nunca
 * seleccion� un valor particular para la cantidad de elementos en el listado,
 * entonces se tomar� el valor de la propiedad de sistema
 * ViewProjectsPage.itemsPerPage para el usuario en la sesi�n. En caso de que
 * alguna vez haya cambiado este valor por defecto, entonces se recuperar� dicho
 * valor de las preferencias del usuario.<br>
 * 
 * Si al acceder a esta p�gina no se cuenta con el dto de un usuario en la
 * sesi�n, entonces se muestran solamente los proyectos p�blicos. En cambio si
 * existe un usuario, se mostrar�n tanto los proyectos p�blicos como los
 * proyectos privados a los que est� asignado el usuario.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ViewProjectsPage extends BasePage implements Pageable {

	/**
	 * Mantiene la cantidad de �tems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Constructor.
	 */
	public ViewProjectsPage() {
		super();
		try {
			// recupera la cantidad de �tems que se deben mostrar en el listado.
			this.setItemsPerPage(new Integer(this
					.getPropertyValue("ViewProjectsPage.itemsPerPage"))
					.intValue());

			// crea el panel para la selecci�n de la cantidad de �tems a mostrar
			// por
			// p�gina
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
