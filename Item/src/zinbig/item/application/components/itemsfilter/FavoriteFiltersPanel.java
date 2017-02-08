/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components.itemsfilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.ItemAjaxTabbedPanel;
import zinbig.item.application.components.ItemTab;
import zinbig.item.application.dataproviders.FavoriteFilterDataProvider;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.Pageable;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para presentar al usuario una lista
 * con los filtros seleccionados como favoritos.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class FavoriteFiltersPanel extends Panel implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2598011076553869633L;

	/**
	 * Es un DTO que representa al filtro seleccionado por el usuario.
	 */
	public FilterDTO selectedFilter;

	/**
	 * Es un DTO que representa al usuario.
	 */
	public UserDTO userDTO;

	/**
	 * Es una colección que contiene los filtros del usuario que no son
	 * favoritos.
	 */
	public Collection<FilterDTO> otherFilters;

	/**
	 * Es el panel en donde se muestran las solapas con los resultados de los
	 * filtros.
	 */
	public AjaxTabbedPanel panel;

	/**
	 * Es la página en la que se ha agregado este panel.
	 */
	public Pageable pageable;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param anUserDTO
	 *            es el dto que representa al usuario en la sesión.
	 * @param aPageable
	 *            es la página en la que se ha agregado este panel.
	 * @param aPanel
	 *            es el panel en donde aparecen las solapas con los resultados
	 *            de los filtros.
	 * 
	 */
	public FavoriteFiltersPanel(String anId, UserDTO anUserDTO,
			final Pageable aPageable, final AjaxTabbedPanel aPanel) {
		super(anId);

		Label aLabel = new Label("title", this
				.getString("itemsFilterPanel.favoriteFilters"));
		this.add(aLabel);
		aLabel.setOutputMarkupId(true);

		// asigna las variables de instancia.
		this.setUserDTO(anUserDTO);
		this.setPanel(aPanel);
		this.setPageable(aPageable);
		this.setOtherFilters(new ArrayList<FilterDTO>());
		ItemsServiceBI service = ServiceLocator.getInstance().getItemsService();

		// carga los filtros favoritos del usuario
		if (anUserDTO != null) {
			try {
				// obtiene todos los filtros del usuario
				this.getOtherFilters().addAll(
						service.findItemsFiltersOfUser(anUserDTO));

				// remueve de la colección de filtros los filtros favoritos.
				this.getOtherFilters().removeAll(this.getFavoriteFilters());
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		// arma el componente para listar los filtros del usuario marcados como
		// favoritos.
		final DataView<FilterDTO> dataView = this
				.createDataViewForFavoriteFilters();
		this.add(dataView);
		dataView.setOutputMarkupId(true);

		// arma el componente para seleccionar un filtro que no es favorito.
		DropDownChoice<FilterDTO> otherFiltersSelect = this
				.createOtherFiltersComponent(aPageable, aPanel);
		this.add(otherFiltersSelect);

		// si no hay otros filtros no se muestra el listado
		otherFiltersSelect.setVisible(!otherFilters.isEmpty());

		aLabel.setVisible((dataView.getItemCount() + this.getOtherFilters()
				.size()) != 0);

	}

	/**
	 * Getter.
	 * 
	 * @return el DTO que representa al filtro seleccionado.
	 */
	public FilterDTO getSelectedFilter() {
		return this.selectedFilter;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterDTO
	 *            es el DTO que representa al filtro seleccionado.
	 */
	public void setSelectedFilter(FilterDTO aFilterDTO) {
		this.selectedFilter = aFilterDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el DTO que representa al usuario.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserDTO
	 *            es el DTO que representa al usuario.
	 */
	public void setUserDTO(UserDTO anUserDTO) {
		this.userDTO = anUserDTO;
	}

	/**
	 * Actualiza este panel ya que se han producido cambios en los filtros del
	 * usuario.
	 * 
	 */
	public void updateFilters() {

		ArrayList<FilterDTO> others = new ArrayList<FilterDTO>();

		// carga los filtros favoritos del usuario
		if (this.getUserDTO() != null) {
			try {
				// obtiene todos los filtros del usuario
				others.addAll(this.getItemsService().findItemsFiltersOfUser(
						this.getUserDTO()));

				others.removeAll(this.getFavoriteFilters());
				this.setOtherFilters(others);

				// reemplaza el componente que muestra los filtros para que se
				// refresce al usuario.
				DataView<FilterDTO> dataView = this
						.createDataViewForFavoriteFilters();
				this.get("filters").replaceWith(dataView);

				// si no hay otros filtros no se muestra el listado
				this.get("otherFilters").setVisible(!otherFilters.isEmpty());
				this.get("otherFilterLabel")
						.setVisible(!otherFilters.isEmpty());
				this.get("title").setVisible(
						(dataView.getItemCount() + this.getOtherFilters()
								.size()) != 0);

			} catch (Exception e) {

			}
		}

	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los filtros del usuario.
	 */
	public Collection<FilterDTO> getOtherFilters() {
		return this.otherFilters;
	}

	/**
	 * Setter.
	 * 
	 * @param someFilterDTOs
	 *            es una colección que contiene los filtros del usuario.
	 */
	public void setOtherFilters(Collection<FilterDTO> someFilterDTOs) {
		this.otherFilters = someFilterDTOs;
	}

	/**
	 * Getter.
	 * 
	 * @return una lista que contiene todos los filtros favoritos del usuario.
	 *         Esta colección no se puede mantener como una variable de
	 *         instancia de este panel ya que no se pueden armar PropertyModels
	 *         para el dataview que los muestra.
	 */
	public List<FilterDTO> getFavoriteFilters() {

		ItemsServiceBI service = ServiceLocator.getInstance().getItemsService();
		ArrayList<FilterDTO> favorites = new ArrayList<FilterDTO>();

		if (this.getUserDTO() != null) {
			try {
				favorites.addAll(service.findFavoritiesItemsFiltersOfUser(this
						.getUserDTO()));

			} catch (Exception e) {
				// en caso de error se retorna una colección vacía.
			}
		}
		return favorites;
	}

	/**
	 * Arma el componente para listar los filtros de ítems.
	 * 
	 * @return un componente para armar el listado de los filtros favoritos.
	 */
	private DataView<FilterDTO> createDataViewForFavoriteFilters() {
		// arma el componente para listar los filtros del usuario marcados como
		// favoritos.
		final DataView<FilterDTO> dataView = new DataView<FilterDTO>("filters",
				new FavoriteFilterDataProvider(this.getFavoriteFilters(), this
						.getUserDTO())) {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Carga el contenido de la lista de filtros favoritos.
			 */
			@Override
			protected void populateItem(Item<FilterDTO> item) {
				final FilterDTO filter = (FilterDTO) item.getModelObject();

				PageParameters params = new PageParameters();
				params.add("FILTER_OID", filter.getOid().toString());

				Link<String> aLink = new Link<String>("filterLink") {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Se ha seleccionado uno de los filtros favoritos. Carga
					 * una nueva solapa en el panel de solapas para el filtro
					 * seleccionado.
					 */
					@Override
					public void onClick() {

						List<ITab> tabs = panel.getTabs();
						panel.setVisible(true);

						ItemTab newTab = new ItemTab(new Model<String>(filter
								.getName()), filter, getUserDTO(), null,
								pageable, panel);
						if (tabs.size() == 10) {
							tabs.remove(0);
						}
						tabs.add(newTab);
						panel.setSelectedTab(tabs.size() - 1);

					}

				};
				aLink.add(new Label("filter", filter.getName()));
				item.add(aLink);

				AjaxLink<String> deleteLink = new AjaxLink<String>(
						"removeFavoriteFilterLink") {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Se ha seleccionado uno de los filtros favoritos. Carga
					 * una nueva solapa en el panel de solapas para el filtro
					 * seleccionado.
					 */
					@Override
					public void onClick(AjaxRequestTarget aTarget) {

						ItemsServiceBI service = FavoriteFiltersPanel.this
								.getItemsService();
						try {
							filter.setFavorite(false);
							service.updateItemsFilter(filter, getUserDTO());

							getUserDTO().removeFilter(filter);
							((ItemSession) this.getSession())
									.setUserDTO(getUserDTO());

							((BasePage) getPageable()).updateMenu();
							((ItemsFilterPanel) ((BasePage) getPageable())
									.get("filterPanel")).updateFilters();

							aTarget
									.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
											.get("filterPanel")));
							aTarget.addComponent((((BasePage) getPageable())
									.get("submenuPanel")));
							aTarget
									.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
											.get("filterPanel"))
											.get("favoriteFiltersPanel"));
							((ItemAjaxTabbedPanel) panel).updateFilter(filter);
							aTarget.addComponent(panel);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				};

				item.add(deleteLink);

			}

		};

		dataView.setOutputMarkupId(true);
		return dataView;
	}

	/**
	 * Getter.
	 * 
	 * @return el panel con las solapas con los resultados de los filtros.
	 */
	public AjaxTabbedPanel getPanel() {
		return this.panel;
	}

	/**
	 * Setter.
	 * 
	 * @param aTabPanel
	 *            es el panel con las solapas con los resultados de los filtros.
	 */
	public void setPanel(AjaxTabbedPanel aTabPanel) {
		this.panel = aTabPanel;
	}

	/**
	 * Getter.
	 * 
	 * @return la página en la que se ha agregado este panel.
	 */
	public Pageable getPageable() {
		return this.pageable;
	}

	/**
	 * Setter.
	 * 
	 * @param aPageable
	 *            es la página en la que se ha agregado este panel.
	 */
	public void setPageable(Pageable aPageable) {
		this.pageable = aPageable;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del servicio relacionado con los ítems.
	 */
	public ItemsServiceBI getItemsService() {
		return ServiceLocator.getInstance().getItemsService();
	}

	/**
	 * Crea el componente que muestra el listado de los filtros que no son
	 * favoritos.
	 * 
	 * @param aPageable
	 *            es la página a la que se agregó este componente.
	 * @param aPanel
	 *            es el panel al que se agregó este componente.
	 * @return una lista que muestra los dtos de los filtros no favoritos.
	 */
	private DropDownChoice<FilterDTO> createOtherFiltersComponent(
			final Pageable aPageable, final AjaxTabbedPanel aPanel) {
		DropDownChoice<FilterDTO> otherFiltersSelect = new DropDownChoice<FilterDTO>(
				"otherFilters", new PropertyModel<FilterDTO>(this,
						"selectedFilter"), new PropertyModel<List<FilterDTO>>(
						this, "otherFilters"));

		otherFiltersSelect
				.add(new AjaxFormComponentUpdatingBehavior("onchange") {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Manejador del evento de actualización de la lista de
					 * filtros. Se reenvía a la misma página pero con el filtro
					 * seleccionado.
					 * 
					 * @param target
					 *            es el objetivo del pedido AJAX.
					 */
					@Override
					protected void onUpdate(AjaxRequestTarget target) {

						if (getSelectedFilter() != null) {
							FilterDTO filter = getSelectedFilter();

							List<ITab> tabs = aPanel.getTabs();
							aPanel.setVisible(true);

							ItemTab newTab = new ItemTab(new Model<String>(
									filter.getName()), filter, getUserDTO(),
									null, aPageable, aPanel);

							tabs.add(newTab);
							aPanel.setSelectedTab(tabs.size() - 1);
							aPanel.setOutputMarkupId(true);

							setSelectedFilter(null);
							target.addComponent(aPanel);
							target.addComponent(aPanel.getParent());

						}
					}

				});

		otherFiltersSelect.setOutputMarkupId(true);
		Label otherFilterLabel = new Label("otherFilterLabel", this
				.getString("itemsFilterPanel.otherFilters"));
		this.add(otherFilterLabel);
		// si no hay otros filtros no se muestra el listado
		otherFiltersSelect.setVisible(!otherFilters.isEmpty());
		otherFilterLabel.setVisible(!otherFilters.isEmpty());

		return otherFiltersSelect;
	}
}
