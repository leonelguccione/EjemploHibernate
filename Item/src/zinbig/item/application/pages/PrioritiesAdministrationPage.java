/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.forms.PrioritySetsAdministrationForm;

/**
 * Las instancias de esta clase se utilizan para administrar las prioridades. <br>
 * Todas las p�ginas de administraci�n siempre tienen la misma estructura: se
 * muestra una lista de los elementos con links que permiten borrar, o editar
 * elementos individuales. <br>
 * Tambi�n se muestra un link para dar de alta un nuevo elemento.<br>
 * Esta clase implementa la interface Pageable ya que se le puede especificar la
 * cantidad de �tems que se deben mostrar en el listado. Si el usuario nunca
 * seleccion� un valor particular para la cantidad de �tems en el listado,
 * entonces se tomar� el valor de la propiedad de sistema
 * PrioritiesAdministrationPage.itemsPerPage para el usuario en la sesi�n. En
 * caso de que alguna vez haya cambiado este valor por defecto, entonces se
 * recuperar� dicho valor de las preferencias del usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrioritiesAdministrationPage extends SecuredPage implements
		Pageable {

	/**
	 * Mantiene la cantidad de �tems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Constructor.
	 */
	public PrioritiesAdministrationPage() {
		super();

		try {
			// recupera la cantidad de �tems que se deben mostrar en el listado.
			this
					.setItemsPerPage(new Integer(
							this
									.getPropertyValue("PrioritiesAdministrationPage.itemsPerPage"))
							.intValue());

			// crea el panel para la selecci�n de la cantidad de �tems a mostrar
			// por p�gina
			ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
					"itemsPerPageSelectionComponent", this);
			this.add(itemsPerPageComponent);

			// agrega el formulario de administraci�n de proyectos
			this.add(new PrioritySetsAdministrationForm("listPrioritiesForm",
					this));
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
	 * @param aNumber
	 *            es la cantidad de elementos a mostrar por p�gina.
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
