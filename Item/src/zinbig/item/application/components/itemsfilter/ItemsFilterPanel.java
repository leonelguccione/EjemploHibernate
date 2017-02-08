/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components.itemsfilter;

import java.io.Serializable;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AddItemFilterForm;
import zinbig.item.application.pages.Pageable;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Este panel se utiliza para presentar al usuario un componente que permite
 * visualizar los filtros ya creados por el usuario y que además le permite
 * crear nuevos filtros y guardarlos en caso de que así lo desee.<br>
 * Dependiendo de si existe o no un usuario en la sesión, el componente se
 * adaptará para no presentar la posibilidad de guardar filtros a un usuario
 * anónimo.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ItemsFilterPanel extends Panel implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -5412111516983363893L;

	/**
	 * Mantiene la referencia al dto que representa al usuario en la sesión.
	 */
	protected UserDTO userDTO;

	/**
	 * Es el dto del proyecto en la sesión.
	 */
	protected ProjectDTO projectDTO;

	/**
	 * Es el panel en donde se muestran los filtros favoritos del usuario.
	 */
	public FavoriteFiltersPanel favoriteFiltersPanel;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param anUserDTO
	 *            es el dto que representa al usuario en la sesión. Este
	 *            parámetro podría ser nulo en caso de que esté listado ítems un
	 *            usuario anónimo.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto en la sesión. Este
	 *            parámetro podría ser nulo.
	 * @param aPanel
	 *            es el panel que contiene los tabs que se van generando a
	 *            medida que se ejecuta el filtro que este panel permite crear.
	 */
	public ItemsFilterPanel(String anId, UserDTO anUserDTO,
			ProjectDTO aProjectDTO, AjaxTabbedPanel aPanel, Pageable aPageable) {
		super(anId);

		this.setUserDTO(anUserDTO);
		this.setProjectDTO(aProjectDTO);

		// agrega el panel para los filtros favoritos
		FavoriteFiltersPanel favoriteFiltersPanel = new FavoriteFiltersPanel(
				"favoriteFiltersPanel", anUserDTO, aPageable, aPanel);
		this.add(favoriteFiltersPanel);
		this.setFavoriteFiltersPanel(favoriteFiltersPanel);
		favoriteFiltersPanel.setOutputMarkupId(true);

		// si no tenemos usuario en la sesión entonces no podemos mostrar el
		// panel de filtros favoritos.
		favoriteFiltersPanel.setVisible(anUserDTO != null
				&& anUserDTO
						.containsOperationWithName("CONFIGURE_ITEMS_FILTERS"));

		// agrega el fomrulario que permite crear nuevos filtros.
		AddItemFilterForm form = new AddItemFilterForm("filterForm", anUserDTO,
				aPanel, aProjectDTO, aPageable);
		this.add(form);
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del usuario en la sesión.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto que representa al usuario.
	 */
	private void setUserDTO(UserDTO aDTO) {
		this.userDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto en la sesión.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el dto del proyecto en la sesión.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

	/**
	 * Notifica al receptor para que actualice sus componentes relacionados con
	 * los filtros del usuario.
	 */
	public void updateFilters() {
		this.getFavoriteFiltersPanel().updateFilters();

	}

	/**
	 * Getter.
	 * 
	 * @return el panel en donde se muestran los filtros favoritos del usuario.
	 */
	public FavoriteFiltersPanel getFavoriteFiltersPanel() {
		return this.favoriteFiltersPanel;
	}

	/**
	 * Setter.
	 * 
	 * @param aPanel
	 *            el panel en donde se muestran los filtros favoritos del
	 *            usuario.
	 */
	public void setFavoriteFiltersPanel(FavoriteFiltersPanel aPanel) {
		this.favoriteFiltersPanel = aPanel;
	}

}
