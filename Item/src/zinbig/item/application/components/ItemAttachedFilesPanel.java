/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AttachFileForm;
import zinbig.item.application.forms.DetachFileForm;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de este panel se utilizan para poder cargar los archivos
 * adjuntos del ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemAttachedFilesPanel extends Panel {

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
	public ItemAttachedFilesPanel(String anId, ItemDTO anItemDTO) {
		super(anId);

		this.setOutputMarkupId(true);

		AttachFileForm attachForm = new AttachFileForm("attachFileForm",
				anItemDTO);
		attachForm.setOutputMarkupId(true);
		this.add(attachForm);

		DetachFileForm detachForm = new DetachFileForm("detachForm", anItemDTO);
		detachForm.setOutputMarkupId(true);
		this.add(detachForm);
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

	/**
	 * Reemplaza el formulario que se utiliza para mostrar la lista de archivos
	 * adjuntos de nodos ya existentes.
	 * 
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está editando.
	 */
	public void updateListOfAttachedFiles(ItemDTO anItemDTO) {
		// agrega el formulario para dar de baja los archivos adjuntos.
		DetachFileForm oldForm = (DetachFileForm) this.get("detachForm");

		DetachFileForm newForm = new DetachFileForm("detachForm", anItemDTO);
		oldForm.setOutputMarkupId(true);

		oldForm.replaceWith(newForm);

	}

}
