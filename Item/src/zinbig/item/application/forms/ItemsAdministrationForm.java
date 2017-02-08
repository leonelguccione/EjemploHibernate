/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.ButtonPagingNavigator;
import zinbig.item.application.components.ItemAjaxTabbedPanel;
import zinbig.item.application.components.ItemTab;
import zinbig.item.application.components.ItemsPerPageSelectionComponent;
import zinbig.item.application.components.itemsfilter.ItemsFilterPanel;
import zinbig.item.application.dataproviders.ItemDTODataProvider;
import zinbig.item.application.pages.AddItemPage;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.EditItemFilterPage;
import zinbig.item.application.pages.MassiveItemsMovementPage;
import zinbig.item.application.pages.Pageable;
import zinbig.item.application.pages.SaveItemFilterPage;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para presentar al usuario un listado
 * con checkboxes de los ítems encontrados de acuerdo al filtro establecido.
 * 
 * @author Javier Bazzocco javier.bzzocco@zinbig.com
 * 
 */
public class ItemsAdministrationForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -1700727029740488249L;

	/**
	 * Mantiene una referencia al filtro que se ejecutó para este panel.
	 */
	protected FilterDTO filterDTO;

	/**
	 * Es una colección de Oids de items que han sido seleccionados.
	 */
	private Collection<String> selectedItems;

	/**
	 * Es la página en la que se están mostrando los ítems de este formulario.
	 */
	protected Pageable pageable;

	/**
	 * Es el panel que contiene las solapas que muestran los resultados de los
	 * filtros.
	 */
	protected AjaxTabbedPanel panel;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aFilterDTO
	 *            es el DTO que representa al filtro aplicado.
	 * @param anUserDTO
	 *            es el DTO que representa al usuario. Este parámetro puede ser
	 *            nulo.
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto. Este parámetro puede ser
	 *            nulo.
	 * @param aPage
	 *            es la página a la cual se ha agregado este formulario.
	 */
	public ItemsAdministrationForm(String anId, final FilterDTO aFilterDTO,
			UserDTO anUserDTO, ProjectDTO aProjectDTO, Pageable aPage,
			final AjaxTabbedPanel aPanel) {
		super(anId);

		// asigna las variables de instancias.
		this.setFilterDTO(aFilterDTO);
		this.setPageable(aPage);
		this.setPanel(aPanel);
		this.setSelectedItems(new HashSet<String>());

		// crea el panel para la selección de la cantidad de ítems a mostrar por
		// página
		ItemsPerPageSelectionComponent itemsPerPageComponent = new ItemsPerPageSelectionComponent(
				"itemsPerPageSelectionComponent", (BasePage) aPage);
		this.add(itemsPerPageComponent);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this.getString("itemsAdministrationForm.noItemsSelected"));
		messages.add(this
				.getString("ItemsAdministrationForm.errorDeletingItemsFilter"));
		messages.add(this
				.getString("ItemsAdministrationForm.errorDeletingItems"));
		messages
				.add(this
						.getString("itemsAdministrationForm.noItemsSelectedToAggregate"));
		this.add(this.createFeedbackPanel(messages));

		// crea el link que permite cerrar la solapa.
		Link<String> aLink = new Link<String>("closeLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica al receptor que se realizó un click en el link. En este
			 * caso intenta cerrar la solapa.
			 */
			@Override
			public void onClick() {

				((ItemAjaxTabbedPanel) aPanel).removeCurrentTab();

			}

		};
		this.add(aLink);

		// crea el link que permite unificar varios ítems en uno solo.
		SubmitLink aggregateLink = new SubmitLink("aggregateLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica al receptor que se realizó un click en el link. En este
			 * caso intenta unificar todos los ítems seleccionados en uno solo.
			 */
			@Override
			public void onSubmit() {

				if (ItemsAdministrationForm.this.getSelectedItems().size() < 2) {
					this
							.error(this
									.getString("itemsAdministrationForm.noItemsSelectedToAggregate"));
				} else {

					PageParameters parameters = new PageParameters();
					StringBuffer buffer = new StringBuffer();
					Iterator<String> iterator = ItemsAdministrationForm.this
							.getSelectedItems().iterator();
					while (iterator.hasNext()) {
						buffer.append(iterator.next() + ";");
					}
					parameters.put("AGGREGATE_ITEMS", buffer.toString());
					this.setResponsePage(AddItemPage.class, parameters);
				}

			}

		};
		this.add(aggregateLink);
		aggregateLink.setVisible(this.getUserDTO() != null
				&& this.verifyPermissionAssigmentToUser("AGGREGATE_ITEMS")
				&& this.verifyPermissionAssigmentToUser("ADD_ITEM"));

		// crea el componente para marcar a un filtro como favorito.
		final Label favoriteFilterLabel = new Label("favoriteFilterLabel");
		favoriteFilterLabel.setDefaultModel(new Model<String>(this
				.getString("ItemsAdministrationForm.addFavoriteFilter")));

		final Label limitFilterLabel = new Label(
				"limitLabel",
				new Model<String>(
						this
								.getString("ItemsAdministrationForm.favoriteFilterLimit")));
		this.add(limitFilterLabel);

		limitFilterLabel.setVisible(this.verifyLimitLabelVisibility());
		limitFilterLabel.setOutputMarkupId(true);

		// crea el link que permite cerrar la solapa.
		final AjaxLink<String> favoriteLink = new AjaxLink<String>(
				"favoriteFilterLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica al receptor que se seleccionó el link. Marca como
			 * favorito al filtro.
			 */
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {

					getFilterDTO().setFavorite(true);

					ItemsAdministrationForm.this.getItemsService()
							.updateItemsFilter(getFilterDTO(), getUserDTO());
					((ItemsFilterPanel) ((BasePage) getPageable())
							.get("filterPanel")).updateFilters();
					// actualiza el componente de menú
					getUserDTO().addFavoriteFilter(getFilterDTO());
					((ItemSession) this.getSession()).setUserDTO(getUserDTO());

					((BasePage) getPageable()).updateMenu();

					this.setVisible(false);

					limitFilterLabel.setVisible(ItemsAdministrationForm.this
							.verifyLimitLabelVisibility());

					target.addComponent(limitFilterLabel);
					target
							.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
									.get("filterPanel")));
					target.addComponent((((BasePage) getPageable())
							.get("submenuPanel")));
					target
							.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
									.get("filterPanel"))
									.get("favoriteFiltersPanel"));

					target.addComponent(this);
					target.addComponent(panel);

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		};
		this.add(favoriteLink);
		favoriteLink.setOutputMarkupId(true);

		favoriteLink.setVisible(this.getFavoriteFilterLinkVisibility());

		favoriteLink.add(favoriteFilterLabel);

		// crea el link que permite borrar los ítems.
		SubmitLink deleteLink = new SubmitLink("deleteItemsLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica que se ha seleccionado el link. En este caso intenta
			 * borrar los ítems seleccionados.<BR>
			 * Solamente se podrán eliminar aquellos ítems en los cuales el
			 * usuario actual es el responsable o líder del proyecto.
			 */
			@Override
			public void onSubmit() {

				if (ItemsAdministrationForm.this.getSelectedItems().size() == 0) {
					this
							.error(this
									.getString("itemsAdministrationForm.noItemsSelected"));
				} else {

					ItemsServiceBI service = ItemsAdministrationForm.this
							.getItemsService();

					try {
						service.deleteItems(ItemsAdministrationForm.this
								.getUserDTO(), ItemsAdministrationForm.this
								.getSelectedItems());
					} catch (Exception e) {
						this
								.error(this
										.getString("itemsAdministrationForm.errorDeletingItems"));
						e.printStackTrace();
					}
				}
			}

		};
		this.add(deleteLink);
		deleteLink.setVisible(this.getUserDTO() != null
				&& this.verifyPermissionAssigmentToUser("DELETE_ITEMS"));

		// crea la ventana modal para cargar el nombre del filtro.
		final ModalWindow modal1 = this.createModalWindow();

		// agrega el link para guardar el filtro.
		AjaxLink<String> saveLink = new AjaxLink<String>("saveLink") {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Se ha hecho click en el link. Se muestra la ventana para cargar
			 * el nombre del filtro.
			 */
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.show(target);
			}
		};
		this.add(saveLink);

		// crea el link que permite borrar el filtro.
		AjaxLink<String> deleteFilterLink = new AjaxLink<String>(
				"deleteFilterLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica que se ha seleccionado el link. En este caso intenta
			 * borrar el filtro actual.
			 */
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {

					UserDTO userDTO = ItemsAdministrationForm.this
							.getItemsService().deleteItemsFilterOfUser(
									aFilterDTO,
									ItemsAdministrationForm.this.getUserDTO());

					((ItemAjaxTabbedPanel) aPanel).removeCurrentTab();

					// actualiza el menú
					((ItemSession) this.getSession()).setUserDTO(userDTO);
					((BasePage) getPageable()).updateMenu();
					ItemsAdministrationForm.this.getItemsFilterPanel()
							.updateFilters();
					target
							.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
									.get("filterPanel")));
					target
							.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
									.get("filterPanel"))
									.get("favoriteFiltersPanel"));
					target.addComponent(this);
					target.addComponent((((BasePage) getPageable())
							.get("submenuPanel")));
					target.addComponent(panel);
				} catch (Exception e) {
					this
							.error(this
									.getString("ItemsAdministrationForm.errorDeletingItemsFilter"));
					e.printStackTrace();
				}
			}

		};
		this.add(deleteFilterLink);

		saveLink.setVisible(aFilterDTO.getOid().equals("")
				&& this.getUserDTO() != null);
		deleteFilterLink.setVisible(!aFilterDTO.getOid().equals("")
				&& this.getUserDTO() != null);

		// crea el link que permite editar el filtro.
		SubmitLink editFilterLink = new SubmitLink("editFilterLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica que se ha seleccionado el link. En este caso intenta
			 * borrar el filtro actual.
			 */
			@Override
			public void onSubmit() {

				PageParameters parameters = new PageParameters();
				parameters.put("FILTER_OID", aFilterDTO.getOid());
				parameters.put("FILTER_TITLE", aFilterDTO.getName());
				this.setResponsePage(EditItemFilterPage.class, parameters);

			}

		};
		this.add(editFilterLink);
		editFilterLink.setVisible(!aFilterDTO.getOid().equals("")
				&& this.getUserDTO() != null);
		this.add(editFilterLink);

		CheckGroup<ItemDTO> group = new CheckGroup<ItemDTO>("group",
				new PropertyModel<Collection<ItemDTO>>(this, "selectedItems"));
		this.add(group);
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = null;
		String columnOrder = null;
		if (this.getUserDTO() != null
				&& this.getUserDTO().getUserPreference(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME) != null) {
			columnName = this.getUserDTO().getUserPreference(
					Constants.ITEMS_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this.getUserDTO().getUserPreference(
					Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER);
		} else {
			columnName = this
					.getSystemProperty(Constants.ITEMS_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER);
		}

		final ItemDTODataProvider dataProvider = new ItemDTODataProvider(
				anUserDTO, aProjectDTO, aFilterDTO, columnName, columnOrder);

		// crea el componente para mostrar el listado.
		final DataView<ItemDTO> dataView = new DataView<ItemDTO>("pageable",
				dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Define el contenido de cada una de las filas del listado.
			 * 
			 * @param item
			 *            es objeto que contiene la información que se utilizará
			 *            para armar la fila.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(final Item<ItemDTO> item) {
				ItemDTO dto = item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				Label idLabel = new Label("id", new Long(dto.getId())
						.toString());

				PageParameters params = new PageParameters();
				params.put("ITEM_OID", dto.getOid());
				params.put("FILTER_OID", getFilterDTO().getOid());
				Link aLink = new BookmarkablePageLink("viewLink",
						ViewItemDetailPage.class, params);
				aLink.add(idLabel);
				item.add(aLink);

				Label responsibleLabel = new Label("responsible", dto
						.getResponsible().getAlias());
				item.add(responsibleLabel);

				Label titleLabel = new Label("title", Utils.decodeString(dto
						.getTitle()));
				item.add(titleLabel);
				titleLabel.add(new AttributeAppender("title", true, new Model(
						Utils.decodeString(dto.getDescription())), ""));
				Label stateLabel = new Label("state", this.getString(dto
						.getState()));
				item.add(stateLabel);
				Label typeLabel = new Label("type", dto.getItemType()
						.getTitle());
				item.add(typeLabel);
				Label projectNameLabel = new Label("projectName", dto
						.getProjectName());
				item.add(projectNameLabel);

				Label nodeLabel = new Label("node", dto.getCurrentNode()
						.getTitle());
				item.add(nodeLabel);

				Label creationDateLabel = new Label("creationDate", dto
						.getCreationDate());
				item.add(creationDateLabel);

			}

		};

		dataView.setItemsPerPage(aPage.getItemsPerPage());

		// agrega el link para ordenar por el id de los ítems.
		OrderByLink sortByIdLink = new OrderByLink("sortByIdLink", "itemId",
				dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por la fecha de creación de los ítems.
		OrderByLink sortByCreationDateLink = new OrderByLink(
				"sortByCreationDateLink", "i.creationDate", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el estado del workflow en donde se
		// encuentra el ítem.
		OrderByLink sortByNodeLink = new OrderByLink("sortByNodeLink",
				"currentWorkflowNode", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el responsable de los ítems.
		OrderByLink sortByResponsibleLink = new OrderByLink(
				"sortByResponsibleLink", "responsible.alias", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el tipo de los ítems.
		OrderByLink sortByItemTypeLink = new OrderByLink("sortByItemTypeLink",
				"itemType.title", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre de los proyectos.
		OrderByLink sortByStateLink = new OrderByLink("sortByStateLink",
				"state", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre de los proyectos.
		OrderByLink sortByProjectNameLink = new OrderByLink(
				"sortByProjectNameLink", "project.name", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el título de los ítems.
		OrderByLink sortByTitleLink = new OrderByLink("sortByTitleLink",
				"title", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.ITEMS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el checkbox group.
		group.add(dataView);

		// agrega el link para ordenar por el id.
		group.add(sortByIdLink);
		// agrega el link para ordenar por el responsable.
		group.add(sortByResponsibleLink);
		// agrega el link para ordenar por el tipo del ítem.
		group.add(sortByItemTypeLink);
		// agrega el link para ordenar por el estado.
		group.add(sortByStateLink);
		// agrega el link para ordenar por el nombre del proyecto.
		group.add(sortByProjectNameLink);
		// agrega el link para ordenar por el título del ítem.
		group.add(sortByTitleLink);
		// agrega el link para ordenar por el estado del workflow del ítem.
		group.add(sortByNodeLink);
		// agrega el link para ordenar por la fecha de creación del ítem.
		group.add(sortByCreationDateLink);

		// agrega el componente de navegación
		ButtonPagingNavigator navigator = new ButtonPagingNavigator(
				"navigator", dataView);
		this.add(navigator);

		if (dataProvider.size() > aPage.getItemsPerPage()) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

		// agrega el componente que muestra la cantidad de elementos.
		Label countLabel = new Label("count", new Long(dataProvider.size())
				.toString());
		this.add(countLabel);

		// crea el link que permite realizar pasajes masivos de ítems
		SubmitLink massiveLink = new SubmitLink("massiveLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica que se ha seleccionado el link. En este caso intenta
			 * borrar el filtro actual.
			 */
			@Override
			public void onSubmit() {

				PageParameters parameters = new PageParameters();
				parameters.put("FILTER_OID", aFilterDTO.getOid());
				((ItemSession) this.getSession())
						.setSelectedItems(ItemsAdministrationForm.this
								.getSelectedItems());
				this
						.setResponsePage(MassiveItemsMovementPage.class,
								parameters);

			}

		};
		this.add(massiveLink);
		massiveLink.setVisible(this
				.verifyPermissionAssigmentToUser("MASSIVE_ITEMS_MOVEMENT"));

	}

	/**
	 * Getter.
	 * 
	 * @return el dto del filtro ejecutado para este panel.
	 */
	public FilterDTO getFilterDTO() {
		return this.filterDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterDTO
	 *            es el dto del filtro ejecutado para este panel.
	 */
	public void setFilterDTO(FilterDTO aFilterDTO) {
		this.filterDTO = aFilterDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los identificadores de los ítems
	 *         seleccionados.
	 */
	public Collection<String> getSelectedItems() {
		return this.selectedItems;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colección que contiene los identificadores de los ítems
	 *            seleccionados.
	 */
	public void setSelectedItems(Collection<String> aCollection) {
		this.selectedItems = aCollection;
	}

	/**
	 * Actualiza este formulario para que refleje la nueva cantidad de ítems por
	 * página.
	 * 
	 * @param aNumber
	 *            es el nuevo número de items que se deben mostrar.
	 */
	public void updateItemsPerPage(int aNumber) {
		DataView<ItemDTO> dataView = this.getDataView();
		ButtonPagingNavigator navigator = (ButtonPagingNavigator) this
				.get("navigator");

		dataView.setItemsPerPage(aNumber);

		if (dataView.getDataProvider().size() > aNumber) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para mostrar la lista de items.
	 */
	@SuppressWarnings("unchecked")
	private DataView<ItemDTO> getDataView() {
		return (DataView<ItemDTO>) ((CheckGroup<ItemDTO>) this.get("group"))
				.get("pageable");
	}

	/**
	 * Guarda el filtro. Este método es invocado por la ventana.
	 * 
	 * @param filterName
	 *            es el nombre del filtro.
	 * @param modal1
	 *            es la ventana que se abrió para que se cargue el nombre.
	 */
	public void saveFilterWithName(String filterName, ModalWindow modal1) {

		try {
			this.getFilterDTO().setName(filterName);
			this.setFilterDTO(this.getItemsService().saveItemsFilterForUser(
					this.getUserDTO(), this.getFilterDTO()));

			this.getItemsFilterPanel().updateFilters();

			this.get("saveLink").setVisible(false);
			this.get("deleteFilterLink").setVisible(true);
			this.get("editFilterLink").setVisible(true);

			if (this.getItemsService().findFavoritiesItemsFiltersOfUser(
					this.getUserDTO()).size() < 3) {

				this.get("favoriteFilterLink").setEnabled(true);
				this.get("favoriteFilterLink").setVisible(true);
			} else {
				this.get("limitLabel").setVisible(true);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Getter.
	 * 
	 * @return la página en la que se agregó este formulario.
	 */
	public Pageable getPageable() {
		return this.pageable;
	}

	/**
	 * Setter.
	 * 
	 * @param aPageable
	 *            es la página en la que se agregó este formulario.
	 */
	public void setPageable(Pageable aPageable) {
		this.pageable = aPageable;
	}

	/**
	 * Getter.
	 * 
	 * @return el panel que contiene las solapas.
	 */
	public AjaxTabbedPanel getPanel() {
		return this.panel;
	}

	/**
	 * Setter.
	 * 
	 * @param aPanel
	 *            es el panel que contiene las solapas.
	 */
	public void setPanel(AjaxTabbedPanel aPanel) {
		this.panel = aPanel;
	}

	/**
	 * Crea una ventana modal para cargar el nombre del nuevo filtro.
	 * 
	 * @return la nueva ventana modal.
	 */
	private ModalWindow createModalWindow() {
		final ModalWindow modal1;
		add(modal1 = new ModalWindow("modal1"));

		modal1.setPageMapName("modal-1");
		modal1.setCookieName("modal-1");

		modal1.setResizable(false);
		modal1.setTitle(this.getString("ItemsAdministrationForm.saveFilter"));
		modal1.setInitialWidth(30);
		modal1.setInitialHeight(15);
		modal1.setWidthUnit("em");
		modal1.setHeightUnit("em");
		modal1.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		modal1.setPageCreator(new ModalWindow.PageCreator() {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Crea la página para la ventana modal.
			 */
			public Page createPage() {
				return new SaveItemFilterPage(ItemsAdministrationForm.this,
						modal1);
			}
		});
		modal1.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Método callback que señala el cierra de la ventana.
			 */
			public void onClose(AjaxRequestTarget target) {

				if (getFilterDTO().getName() != null) {

					// actualiza el nombre de la solapa seleccionada.
					AjaxTabbedPanel tabs = (AjaxTabbedPanel) getParent()
							.getParent();
					ItemTab tab = (ItemTab) tabs.getTabs().get(
							tabs.getSelectedTab());
					tab.getTitle().setObject(getFilterDTO().getName());
					target.addComponent(tabs);
					target
							.addComponent(((ItemsFilterPanel) ((BasePage) getPageable())
									.get("filterPanel"))
									.get("favoriteFiltersPanel"));
					target.addComponent(get("favoriteFilterLink"));

				}
			}
		});
		modal1.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica que se cerró la ventana mediante el botón de cierre.
			 */
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {

				return true;
			}
		});

		return modal1;
	}

	/**
	 * Getter.
	 * 
	 * @return el panel de los filtros.
	 */
	private ItemsFilterPanel getItemsFilterPanel() {
		return (ItemsFilterPanel) ((BasePage) this.getPageable())
				.get("filterPanel");
	}

	/**
	 * Actualiza el estado del filtro de esta solapa.
	 * 
	 * @param aFilterDTO
	 *            es el filtro que se debe actualizar.
	 */
	public void updateFilter(FilterDTO aFilterDTO) {
		if (this.getFilterDTO().getName().equals(aFilterDTO.getName())) {
			this.setFilterDTO(aFilterDTO);
		}

		this.get("favoriteFilterLink").setVisible(
				this.getFavoriteFilterLinkVisibility());

		this.get("limitLabel").setVisible(this.verifyLimitLabelVisibility());

	}

	/**
	 * Verifica si el link de agregar filtro favorito tiene que estar visible o
	 * no.
	 * 
	 * @return true en caso de que se tenga que ver el link; false en caso
	 *         contrario.
	 */
	private boolean getFavoriteFilterLinkVisibility() {
		boolean result = false;
		try {

			if (getUserDTO() != null
					&& getItemsService().findFavoritiesItemsFiltersOfUser(
							getUserDTO()).size() < 3) {
				if (getFilterDTO().isFavorite()) {
					result = false;

				} else {
					if (getFilterDTO().getOid().equals("")) {
						result = false;
					} else {
						result = true & this
								.verifyPermissionAssigmentToUser("CONFIGURE_ITEMS_FILTERS");

					}
				}

			} else {
				result = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Verifica el estado de visibilidad de la etiqueta de límte de filtros
	 * favoritos.
	 * 
	 * @return true en caso de que el usuario tenga menos de tres favoritos;
	 *         false en caso contrario.
	 */
	private boolean verifyLimitLabelVisibility() {
		boolean result = false;
		try {

			result = getUserDTO() != null
					&& getItemsService().findFavoritiesItemsFiltersOfUser(
							getUserDTO()).size() == 3;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
