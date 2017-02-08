/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.ItemSession;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.menu.Menu;

/**
 * Esta clase representa el tope de la jerarqu�a de los componentes que se
 * utilizan para representar el men� de operaciones de un usuario, sea an�nimo o
 * registrado.<br>
 * Esta clase provee comportamiento e implementaciones en com�n a sus subclases.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class AbstractMenu extends Panel implements Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 2625868038651629531L;

	/**
	 * Es una colecci�n que contiene los DTOs de las operaciones que deben ser
	 * representadas como �tems de men�.
	 */
	protected Collection<OperationDTO> operations;

	/**
	 * Constructor por defecto.
	 * 
	 * @param id
	 *            es el identificador de este componente.
	 * @param someOperations
	 *            es una colecci�n que contiene las operaciones asignadas al
	 *            usuario y que deben ser representadas como �tems de men� por
	 *            este componente.
	 */
	public AbstractMenu(String id, Collection<OperationDTO> someOperations) {
		super(id);
		this.setOperations(someOperations);

		List<Menu> list = this.convertToMenuList(someOperations);

		// agrega el componente de men�
		this.add(this.createListView(list));
		this.getApplication().getMarkupSettings().setStripWicketTags(true);
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n de DTOs de operaciones que deben ser representadas
	 *         como �tems de men�.
	 */
	public Collection<OperationDTO> getOperations() {
		return this.operations;
	}

	/**
	 * Setter.
	 * 
	 * @param someOperations
	 *            es una colecci�n de DTOs de operaciones que deben ser
	 *            representadas como �tems de men�.
	 */
	public void setOperations(Collection<OperationDTO> someOperations) {
		this.operations = someOperations;
	}

	/**
	 * Convierte los dtos de las operaciones en �tems de men�.
	 * 
	 * 
	 * @param operationsList
	 *            es la lista de dtos de operaciones.
	 * @return una lista de �tems de men� para cada una de las operaciones.
	 */
	protected abstract List<Menu> convertToMenuList(
			Collection<OperationDTO> operationsList);

	/**
	 * Crea el componente que itera sobre los �tems de men� y arma el men�.
	 * 
	 * @param aList
	 *            es la lista de elementos que componen el men�.
	 * @return el componente creado.
	 */
	@SuppressWarnings("unchecked")
	protected abstract ListView createListView(List<Menu> aList);

	/**
	 * Actualiza el componente de men� con el identificador recibido. <br>
	 * Esto generalmente se debe a cambios en el idioma de la interfaz, por lo
	 * que se deben volver a crear todos los �tems del men�.
	 * 
	 * @param anId
	 *            es el identificador del componente de men� que debe
	 *            refrescarse.
	 * @param aBoolean
	 *            indica si el componente debe inicializarse en null o no.
	 */
	protected void updateMenu(String anId, boolean aBoolean) {

		// obtiene la sesi�n del usuario para invalidar el men� ya creado
		ItemSession session = (ItemSession) this.getSession();
		if (aBoolean) {
			session.setMenu(null);
		}

		// dispara la creaci�n del nuevo men�.
		List<Menu> list = this.convertToMenuList(this.getOperations());
		session.setMenu(list);
		this.get(anId).replaceWith(this.createListView(list));

	}

	/**
	 * Actualiza el men�. <br>
	 * Esto generalmente se debe a cambios en el idioma de la interfaz, por lo
	 * que se deben volver a crear todos los �tems del men�.
	 */
	public abstract void updateMenu();

}
