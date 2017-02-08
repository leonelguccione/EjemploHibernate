/**
 * Este paquete contiene clases �tiles para la creaci�n de p�ginas que se basan
 * en ajax para brindar su funcionalidad.
 * 
 */
package zinbig.item.application.forms.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.Pageable;

/**
 * Las instancias de esta clase se utilizan para actualizar la cantidad de �tems
 * que se deben mostrar en los listados. Cada p�gina que implemente la interface
 * Pageable y que extienda de la clase BasePage puede hacer uso de este behavior
 * para definir la cantidad de �tems en su listado. <br>
 * Este objeto act�a cuando se dispara el evento "onchange" del componente que
 * muestra la cantidad de �tems.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemsPerPageBehavior extends AjaxFormComponentUpdatingBehavior {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -2761167703704023204L;

	/**
	 * Es la referencia a la p�gina que est� utilizando este componente.
	 */
	private BasePage page;

	/**
	 * Constructor.
	 * 
	 * @param aPage
	 *            es la p�gina para la cual se est� utilizando este componente.
	 */
	public ItemsPerPageBehavior(BasePage aPage) {

		// registra este componente para el evento "onchange".
		super("onchange");
		this.setPage(aPage);
	}

	/**
	 * Este m�todo se invoca autom�ticamente cuando se hace alguna actualizaci�n
	 * en el componente que muestra la cantidad de �tems que se est�n mostrando
	 * en el listado de la p�gina.
	 * 
	 * @param aTarget
	 *            es el objetivo de esta llamada AJAX.
	 */
	@Override
	protected void onUpdate(AjaxRequestTarget aTarget) {
		Pageable aPageable = (Pageable) this.getPage();

		// notifica a la p�gina que tiene el listado que actualice la cantidad
		// de items que muestra en el listado.
		aPageable.updateItemsPerPage();

		// refresca la p�gina para que aparezca el n�mero correcto de �tems en
		// el listado
		this.getPage().setResponsePage(this.getPage());
	}

	/**
	 * Getter.
	 * 
	 * @return la p�gina para la cual se ha registrado este objeto.
	 */
	public BasePage getPage() {
		return this.page;
	}

	/**
	 * Setter.
	 * 
	 * @param aPage
	 *            es la p�gina para cual se ha registrado este componente.
	 */
	public void setPage(BasePage aPage) {
		this.page = aPage;
	}

}
