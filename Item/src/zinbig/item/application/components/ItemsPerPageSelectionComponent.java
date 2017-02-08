/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

import zinbig.item.application.forms.behaviors.ItemsPerPageBehavior;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.Pageable;

/**
 * Este componente permite definir la cantidad de elementos que se deben mostrar
 * en alg�n listado de la p�gina que lo contiene.<br>
 * Cada vez que se actualiza el valor y se dispara el evento "onchange" se
 * actualiza el valor de la propiedad relacionada con la cantidad de elementos
 * del listado, tanto en la base de datos para el usuario registrado como en el
 * dto que representa al usuario en la sesi�n. <br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemsPerPageSelectionComponent extends Panel {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -724325420465131091L;

	/**
	 * COnstructor.
	 * 
	 * @param id
	 *            es la identificaci�n de este componente.
	 * @param aPage
	 *            es la p�gina a la que se est� agregando este componente.
	 */
	public ItemsPerPageSelectionComponent(String id, BasePage aPage) {
		super(id);

		// construye el input field para la cantidad de items a mostrar
		final TextField<Integer> itemsPerPageField = new TextField<Integer>(
				"itemsPerPage",
				new PropertyModel<Integer>(this, "itemsPerPage"));
		itemsPerPageField.add(new RangeValidator<Integer>(1, 100));
		this.add(itemsPerPageField);
		itemsPerPageField.add(new ItemsPerPageBehavior(aPage));
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de elementos que muestra el listado de la p�gina.
	 */
	public int getItemsPerPage() {
		return ((Pageable) this.getPage()).getItemsPerPage();
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de elementos que muestra el listado de la
	 *            p�gina.
	 */
	public void setItemsPerPage(int aNumber) {
		((Pageable) this.getPage()).setItemsPerPage(aNumber);
	}

}
