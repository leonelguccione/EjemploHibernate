/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.SetAdditionalPropertiesForm;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de este panel se utilizan para poder ingresar los valores de
 * las propiedades adicionales de los �tems para un proyecto determinado. Cada
 * proyecto determina el conjunto de propiedades adicionales que deben ser
 * completados.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemAdditionalPropertiesPanel extends Panel {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 2571955665162871827L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 * @param anItemDTO
	 *            es el dto que representa al item que se est� editando.
	 */
	public ItemAdditionalPropertiesPanel(String anId, ItemDTO anItemDTO) {
		super(anId);

		// agrega el formulario que permite cargar las propiedades adicionales.
		SetAdditionalPropertiesForm aForm = new SetAdditionalPropertiesForm(
				"additionalPropertiesForm", anItemDTO);
		aForm.setOutputMarkupId(true);
		this.add(aForm);

	}

	/**
	 * Actualiza la versi�n del �tem que se est� editando.<BR>
	 * Esta actualziaci�n se debe a que el formulario que contiene este panel ha
	 * actualizado la informaci�n del �tem y por lo tanto se cuenta con una
	 * nueva versi�n del �tem.
	 * 
	 * @param newItemDTO
	 *            es el nuevo dto que representa al �tem.
	 */
	public void updateItemVersion(ItemDTO newItemDTO) {
		((ViewItemDetailPage) this.getPage()).setItemDTO(newItemDTO);

	}

}
