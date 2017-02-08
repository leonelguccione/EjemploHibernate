/**
 * Este paquete contiene clases e interfaces útiles para definir nuevos 
 * componentes para la aplicación.
 */
package zinbig.item.application.components;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import zinbig.item.application.pages.Pageable;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para representar las solapas que
 * muestran el resultado de la ejecución de un filtro sobre un conjunto de
 * ítems. Cada vez que se accede a esta solapa se ejecuta el servicio de ítems
 * buscando aquellos ítems que satisfacen el filtro recibido.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTab extends AbstractTab {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 5910399869105768175L;

	/**
	 * Es el dto que representa el filtro de esta solapa.
	 */
	protected FilterDTO filterDTO;

	/**
	 * Es el servicio que se debe utilizar para acceder a los ítems.
	 */
	protected ItemsServiceBI itemsService;

	/**
	 * Es el dto que representa al usuario en la sesión.
	 */
	protected UserDTO userDTO;

	/**
	 * Es el dto que representa al proyecto en el que se encuentra el usuario.
	 */
	protected ProjectDTO projectDTO;

	/**
	 * Es el panel que se debe utilizar para mostrar los resultados del filtro.
	 */
	protected Panel panel;

	/**
	 * Constructor.
	 * 
	 * 
	 * @param aTitle
	 *            es el título de esta solapa.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro de esta solapa.
	 * @param anUserDTO
	 *            es el dto del usuario en la sesión. Este parámetro puede ser
	 *            nulo en caso de un usuario anónimo.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto en el que se encuentra el
	 *            usuario. este parámetro puede ser nulo en caso de que no se
	 *            haya ingresado en ningún proyecto.
	 */
	public ItemTab(IModel<String> aTitle, FilterDTO aFilterDTO,
			UserDTO anUserDTO, ProjectDTO aProjectDTO, Pageable aPageable,
			AjaxTabbedPanel aPanel) {
		super(aTitle);
		this.setFilterDTO(aFilterDTO);
		this.setUserDTO(anUserDTO);
		this.setProjectDTO(aProjectDTO);

		this.setPanel(new ViewItemsPanel("panel", aFilterDTO, anUserDTO,
				aProjectDTO, aPageable, aPanel));
	}

	/**
	 * Getter.
	 * 
	 * @param panelId
	 *            es el identificador de este panel.
	 * @return un panel que contiene el resultado de la ejecución del filtro.
	 */
	@Override
	public Panel getPanel(String panelId) {

		return this.getPanel();

	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al filtro de esta solapa.
	 */
	public FilterDTO getFilterDTO() {
		return this.filterDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto que representa al filtro de esta solapa.
	 */
	public void setFilterDTO(FilterDTO aDTO) {
		this.filterDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el servicio que se debe utilizar para acceder a los ítems.
	 */
	public ItemsServiceBI getItemsService() {
		return this.itemsService;
	}

	/**
	 * Setter.
	 * 
	 * @param aService
	 *            es el servicio que se debe utilizar para acceder a los ítems.
	 */
	public void setItemsService(ItemsServiceBI aService) {
		this.itemsService = aService;
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
	 * @param aDTO
	 *            es el dto del usuario.
	 */
	public void setUserDTO(UserDTO aDTO) {
		this.userDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al proyecto.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto del proyecto.
	 */
	public void setProjectDTO(ProjectDTO aDTO) {
		this.projectDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el panel que utiliza esta solapa para mostrar los resultados del
	 *         filtro.
	 */
	public Panel getPanel() {
		return this.panel;
	}

	/**
	 * Setter.
	 * 
	 * @param aPanel
	 *            es el panel que se debe utilizar para mostrar los resultados
	 *            del filtro.
	 */
	public void setPanel(Panel aPanel) {
		this.panel = aPanel;
	}

	/**
	 * Notifica al receptor que se ha cambiado la cantidad de ítems a mostrar
	 * por página.
	 * 
	 * @param aNumber
	 *            es la nueva cantidad de elementos a mostrar.
	 */
	public void updateItemsPerPage(int aNumber) {
		((ViewItemsPanel) this.getPanel()).updateItemsPerPage(aNumber);

	}

	/**
	 * Actualiza el estado del filtro de esta solapa.
	 * 
	 * @param aFilterDTO
	 *            es el dto del filtro que se debe actualizar.
	 */
	public void updateFilter(FilterDTO aFilterDTO) {
		this.setFilterDTO(aFilterDTO);
		((ViewItemsPanel) this.getPanel()).updateFilter(aFilterDTO);

	}

}
