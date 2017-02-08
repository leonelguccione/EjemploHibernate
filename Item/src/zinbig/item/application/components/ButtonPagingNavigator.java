/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.io.Serializable;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 * Las instancias de esta clase se utilizan para mostrar un componente que
 * permite navegar entre las p�ginas de un listado.<br>
 * Este componente extiende el componente PagingNavigator para mostrar im�genes
 * en vez de texto y para no mostrarse en caso de que no existan m�s p�ginas que
 * la actual.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ButtonPagingNavigator extends PagingNavigator implements
		Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -937829640476466539L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el id de este componente.
	 * @param pageable
	 *            es el componente que debe paginarse.
	 */
	public ButtonPagingNavigator(String id, IPageable pageable) {
		super(id, pageable);

		if (pageable.getPageCount() == 1) {
			this.setVisible(false);
		}
	}

}
