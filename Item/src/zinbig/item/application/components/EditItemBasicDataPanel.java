/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.EditItemForm;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de este panel se utilizan para poder editar los detalles
 * básicos de un ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemBasicDataPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8961536659461520363L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 * @param anItemDTO
	 *            es el dto que representa al item que se está editando.
	 */
	public EditItemBasicDataPanel(String anId, ItemDTO anItemDTO) {
		super(anId);

		EditItemForm aForm = new EditItemForm("editItemForm", anItemDTO);
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
