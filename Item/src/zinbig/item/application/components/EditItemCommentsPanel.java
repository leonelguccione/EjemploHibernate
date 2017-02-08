/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.EditItemCommentsForm;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de este panel se utilizan para poder editar los comentarios de
 * un ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemCommentsPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -6917350882194739421L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 * @param anItemDTO
	 *            es el dto que representa al item que se está editando.
	 */
	public EditItemCommentsPanel(String anId, ItemDTO anItemDTO) {
		super(anId);

		EditItemCommentsForm aForm = new EditItemCommentsForm(
				"editItemCommentsForm", anItemDTO);

		aForm.setOutputMarkupId(true);
		this.add(aForm);
		this.setOutputMarkupId(true);
	}

	/**
	 * Actualiza la versión del ítem que se está editando.<BR>
	 * Esta actualziación se debe a que el formulario que contiene este panel ha
	 * actualizado la información del ítem y por lo tanto se cuenta con una
	 * nueva versión del ítem.
	 * 
	 * @param newItemDTO
	 *            es el nuevo dto que representa al ítem.
	 */
	public void updateItemVersion(ItemDTO newItemDTO) {
		((ViewItemDetailPage) this.getPage()).setItemDTO(newItemDTO);

	}
}
