/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.wicket.PageParameters;
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
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.ButtonPagingNavigator;
import zinbig.item.application.dataproviders.UserGroupDTODataProvider;
import zinbig.item.application.pages.AddUserGroupPage;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.EditUserGroupPage;
import zinbig.item.application.pages.UserGroupsAdministrationPage;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.UserGroupDTO;

/**
 * Las instancias de esta clase se utilizan para administrar los grupos de
 * usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class UserGroupsAdministrationForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -6126496221636009875L;

	/**
	 * Es una colección de Oids de grupos de usuarios que han sido
	 * seleccionados.
	 */
	private Collection<String> selectedUserGroups;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aPage
	 *            es la página a la que se ha agregado este formulario.
	 */
	public UserGroupsAdministrationForm(String anId,
			UserGroupsAdministrationPage aPage) {
		super(anId);

		// agrega el link para poder dar de alta un nuevo grupo de usuarios
		Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
				"newUserGroupLink", AddUserGroupPage.class);
		this.add(aLink);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this
				.getString("userGroupsAdministrationForm.noUserGroupSelected"));
		messages
				.add(this
						.getString("userGroupsAdministrationForm.errorDeletingUserGroups"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedUserGroups(new HashSet<String>());

		CheckGroup<UserGroupDTO> group = new CheckGroup<UserGroupDTO>("group",
				new PropertyModel<Collection<UserGroupDTO>>(this,
						"selectedUserGroups"));
		this.add(group);
		group.add(new CheckGroupSelector("groupselector"));

		SubmitLink deleteLink = new SubmitLink("deleteUserGroupLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el requerimiento para borrar los usuarios seleccionados. En
			 * caso de que no se hayan seleccionado usuarios se muestra un
			 * cartel de error al usuario.
			 */
			@Override
			public void onSubmit() {
				if (UserGroupsAdministrationForm.this
						.getSelectedUserGroupsCount() == 0) {
					this
							.error(this
									.getString("userGroupsAdministrationForm.noUserGroupSelected"));
				} else {
					UsersServiceBI service = UserGroupsAdministrationForm.this
							.getUsersService();

					try {
						service
								.deleteUserGroups(UserGroupsAdministrationForm.this
										.getSelectedUserGroups());
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("userGroupsAdministrationForm.errorDeletingUserGroups"));
					}

				}
			}

		};

		this.add(deleteLink);

		// recupera las preferencias de ordenamiento del usuario
		String columnName = this.getUserDTO().getUserPreference(
				Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_NAME);
		String columnOrder = this.getUserDTO().getUserPreference(
				Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_ORDER);

		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_ORDER);
		}

		final UserGroupDTODataProvider dataProvider = new UserGroupDTODataProvider(
				columnName, columnOrder);

		// crea el componente para el listado de los grupos de usuarios
		// existentes
		final DataView<UserGroupDTO> dataView = new DataView<UserGroupDTO>(
				"pageable", dataProvider) {

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
			protected void populateItem(final Item<UserGroupDTO> item) {
				UserGroupDTO dto = item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				PageParameters parameters = new PageParameters();
				parameters.put("userGroupName", Utils.encodeString(dto
						.getName()));
				Link aLink = new BookmarkablePageLink("editLink",
						EditUserGroupPage.class, parameters);

				aLink.add(new Label("name", dto.getName()));
				item.add(aLink);
				item.add(new Label("email", dto.getEmail()));

			}
		};

		dataView.setItemsPerPage(aPage.getItemsPerPage());

		// agrega el link para ordenar por el nombre de los grupos de usuarios.
		OrderByLink sortByNameLink = new OrderByLink("sortByNameLink", "name",
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
						Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el correo de los grupos de usuarios.
		OrderByLink sortByEmailLink = new OrderByLink("sortByEmailLink",
				"email", dataProvider) {

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
						Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.USERS_GROUPS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el checkbox group.
		group.add(dataView);

		// agrega el link para ordenar por el nombre.
		group.add(sortByNameLink);

		// agrega el link para ordenar por el leader.
		group.add(sortByEmailLink);

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
	}

	/**
	 * Actualiza el componente de navegación para verificar que hay más páginas
	 * para mostrar tomando como entrada la cantidad de elementos que se
	 * muestran en la página. Si la cantidad de elementos que se deben mostrar
	 * es mayor a la cantidad total de elementos, el navegador no aparece.
	 * 
	 * @param aNumber
	 *            es el número de elementos a mostrar por página.
	 */
	public void updateItemsPerPage(int aNumber) {

		DataView<UserGroupDTO> dataView = this.getDataView();
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
	 * @return una colección que contiene los oids de los grupos de usuarios
	 *         seleccionados.
	 */
	public Collection<String> getSelectedUserGroups() {
		return this.selectedUserGroups;
	}

	/**
	 * Setter.
	 * 
	 * @param someUserGroups
	 *            es una colección que contiene los oids de los grupos de
	 *            usuarios seleccionados.
	 */
	public void setSelectedUserGroups(Collection<String> someUserGroups) {
		this.selectedUserGroups = someUserGroups;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de grupos de usuarios seleccionados.
	 */
	protected int getSelectedUserGroupsCount() {
		return this.getSelectedUserGroups().size();
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para armar el listado.
	 */
	@SuppressWarnings("unchecked")
	public DataView<UserGroupDTO> getDataView() {
		return (DataView<UserGroupDTO>) ((CheckGroup<UserGroupDTO>) this
				.get("group")).get("pageable");
	}

}
