/**
 * Este paquete contiene las clases que componen la aplicaci�n Item.<br>
 * Este desarrollo se basa en el framework web Wicket y utiliza Spring para la 
 * mayor�a de las configuraciones.
 * 
 */
package zinbig.item.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebSession;

import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.menu.Menu;

/**
 * Las instancias de esta clase representan las sesiones de trabajo de los
 * usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemSession extends WebSession implements Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -3755977718450719575L;

	/**
	 * Es el dto que mantiene informaci�n del usuario.
	 */
	protected UserDTO userDTO;

	/**
	 * Es la lista de men�es creado para el usuario en la sesi�n.
	 */
	protected List<Menu> menu;

	/**
	 * Es el dto que representa al proyecto seleccionado por el usuario.
	 */
	protected ProjectDTO projectDTO;

	/**
	 * Es un diccionario que contiene los par�metros requeridos por las
	 * distintas p�ginas.
	 */
	protected Map<String, Object> parameters;

	/**
	 * Es una colecci�n que contiene los �tems seleccionados por un usuario.
	 */
	protected Collection<String> selectedItems;

	/**
	 * Constructor.
	 * 
	 * @param request
	 *            es el objeto que representa el request web.
	 * @param response
	 *            es el objeto que representa el response web.
	 */
	public ItemSession(Request request, Response response) {

		super(request);
		this.setParameters(new HashMap<String, Object>());
		this.setSelectedItems(new ArrayList<String>());

	}

	/**
	 * Verifica si hay alg�n usuario logueado.
	 * 
	 * @return true si hay un usuario logueado; false en caso contrario.
	 */
	public boolean isUserLoggedIn() {
		return (this.getUserDTO() != null);
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al usuario.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario.
	 */
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	/**
	 * Notifica al receptor que se lo est� invalidando. Borra el dto del usuario
	 * de la session.
	 * 
	 */
	@Override
	public void invalidate() {

		super.invalidate();
		this.setUserDTO(null);
	}

	/**
	 * Getter.
	 * 
	 * @return el men� creado para el usuario en la sesi�n.
	 */
	public List<Menu> getMenu() {
		return this.menu;
	}

	/**
	 * Setter.
	 * 
	 * @param aMenuList
	 *            es el men� del usuario en la sesi�n.
	 */
	public void setMenu(List<Menu> aMenuList) {
		this.menu = aMenuList;
	}

	/**
	 * Getter.
	 * 
	 * @return el diccionario que contiene los par�metros de esta sesi�n.
	 */
	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es el diccionario que contiene los par�metros de la sesi�n.
	 */
	public void setParameters(Map<String, Object> aMap) {
		this.parameters = aMap;
	}

	/**
	 * Agrega un nuevo par�metro a la lista de par�metros.
	 * 
	 * @param aKey
	 *            es la clave bajo la cual se almacena el par�metro.
	 * @param anObject
	 *            es el objeto que se debe almacenar.
	 */
	public void put(String aKey, Object anObject) {
		this.getParameters().put(aKey, anObject);
	}

	/**
	 * Getter.
	 * 
	 * @param aKey
	 *            es la clave bajo la cual se debe buscar el par�metro.
	 * @return el objeto bajo la clave recibida.
	 */
	public Object get(String aKey) {
		return this.getParameters().get(aKey);
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el dto del proyecto que se encuentra seleccionado.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto seleccionado por el usuario.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los ids de los �tems seleccionados por
	 *         el usuario.
	 */
	public Collection<String> getSelectedItems() {
		return this.selectedItems;
	}

	/**
	 * Setter.
	 * 
	 * @param someIds
	 *            es una colecci�n que contiene los ids de los �tems
	 *            seleccionados por el usuario.
	 */
	public void setSelectedItems(Collection<String> someIds) {
		this.selectedItems = someIds;
	}

}
