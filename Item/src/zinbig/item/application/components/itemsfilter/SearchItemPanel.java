/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components.itemsfilter;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.SearchItemForm;

/**
 * Las instancias de esta clase se utilizan para presentar al usuario un campo
 * que permite buscar ítems por su id.<br>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class SearchItemPanel extends Panel implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -8320390122440830736L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 */
	public SearchItemPanel(String anId) {
		super(anId);
		SearchItemForm form = new SearchItemForm("searchForm");
		this.add(form);
	}

}
