/**
 * Este paquete contiene clases útiles para la creación de páginas que se basan
 * en ajax para brindar su funcionalidad.
 * 
 */
package zinbig.item.application.forms.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.Pageable;

/**
 * Las instancias de esta clase se utilizan para actualizar la cantidad de ítems
 * que se deben mostrar en los listados. Cada página que implemente la interface
 * Pageable y que extienda de la clase BasePage puede hacer uso de este behavior
 * para definir la cantidad de ítems en su listado. <br>
 * Este objeto actúa cuando se dispara el evento "onchange" del componente que
 * muestra la cantidad de ítems.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemsPerPageBehavior extends AjaxFormComponentUpdatingBehavior {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -2761167703704023204L;

	/**
	 * Es la referencia a la página que está utilizando este componente.
	 */
	private BasePage page;

	/**
	 * Constructor.
	 * 
	 * @param aPage
	 *            es la página para la cual se está utilizando este componente.
	 */
	public ItemsPerPageBehavior(BasePage aPage) {

		// registra este componente para el evento "onchange".
		super("onchange");
		this.setPage(aPage);
	}

	/**
	 * Este método se invoca automáticamente cuando se hace alguna actualización
	 * en el componente que muestra la cantidad de ítems que se están mostrando
	 * en el listado de la página.
	 * 
	 * @param aTarget
	 *            es el objetivo de esta llamada AJAX.
	 */
	@Override
	protected void onUpdate(AjaxRequestTarget aTarget) {
		Pageable aPageable = (Pageable) this.getPage();

		// notifica a la página que tiene el listado que actualice la cantidad
		// de items que muestra en el listado.
		aPageable.updateItemsPerPage();

		// refresca la página para que aparezca el número correcto de ítems en
		// el listado
		this.getPage().setResponsePage(this.getPage());
	}

	/**
	 * Getter.
	 * 
	 * @return la página para la cual se ha registrado este objeto.
	 */
	public BasePage getPage() {
		return this.page;
	}

	/**
	 * Setter.
	 * 
	 * @param aPage
	 *            es la página para cual se ha registrado este componente.
	 */
	public void setPage(BasePage aPage) {
		this.page = aPage;
	}

}
