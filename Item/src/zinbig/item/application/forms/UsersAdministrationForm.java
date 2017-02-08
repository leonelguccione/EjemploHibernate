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
import zinbig.item.application.dataproviders.UserDTODataProvider;
import zinbig.item.application.pages.AddUserPage;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.EditUserPage;
import zinbig.item.application.pages.UsersAdministrationPage;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para administrar los usuarios. <br>
 * Administrar usuarios significa poder listarlos y seleccionarlos para su
 * edición.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UsersAdministrationForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -4996279859208068812L;

	/**
	 * Es una colección de Oids de usuarios que han sido seleccionados.
	 */
	private Collection<String> selectedUsers;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el id de este formulario.
	 * @param aPage
	 *            es la página en la que se ha agregado este formulario.
	 */
	public UsersAdministrationForm(String anId, UsersAdministrationPage aPage) {
		super(anId);

		// agrega el link para poder dar de alta un nuevo usuario
		Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
				"newUserLink", AddUserPage.class);
		this.add(aLink);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this.getString("usersAdministrationForm.noUserSelected"));
		messages.add(this
				.getString("usersAdministrationForm.errorDeletingUsers"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedUsers(new HashSet<String>());

		SubmitLink deleteLink = new SubmitLink("deleteUsersLink") {

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
				if (UsersAdministrationForm.this.getSelectedUsersCount() == 0) {
					this
							.error(this
									.getString("usersAdministrationForm.noUserSelected"));
				} else {
					try {
						UsersAdministrationForm.this.getUsersService()
								.deleteUsers(
										UsersAdministrationForm.this
												.getSelectedUsers());
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("usersAdministrationForm.errorDeletingUsers"));
					}
				}
			}

		};

		this.add(deleteLink);

		CheckGroup<UserDTO> group = new CheckGroup<UserDTO>("group",
				new PropertyModel<Collection<UserDTO>>(this, "selectedUsers"));
		this.add(group);
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = this.getUserDTO().getUserPreference(
				Constants.USERS_ADMINISTRATION_COLUMN_NAME);
		String columnOrder = this.getUserDTO().getUserPreference(
				Constants.USERS_ADMINISTRATION_COLUMN_ORDER);

		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.USERS_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.USERS_ADMINISTRATION_COLUMN_ORDER);
		}

		final UserDTODataProvider dataProvider = new UserDTODataProvider(
				columnName, columnOrder);

		// crea el componente para el listado de los usuarios existentes
		final DataView<UserDTO> dataView = new DataView<UserDTO>("pageable",
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
			protected void populateItem(final Item<UserDTO> item) {
				UserDTO dto = item.getModelObject();

				PageParameters parameters = new PageParameters();
				parameters.put("username", Utils
						.encodeString(dto.getUsername()));

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);
				check.setEnabled(!dto.isProjectLeader());

				Link aLink = new BookmarkablePageLink("editLink",
						EditUserPage.class, parameters);
				aLink.add(new Label("surname", dto.getSurname()));
				item.add(aLink);

				item.add(new Label("name", dto.getName()));
				item.add(new Label("username", dto.getUsername()));

			}

		};

		// asigna la cantidad de items que se deben mostrar por página
		dataView.setItemsPerPage(aPage.getItemsPerPage());

		// agrega el link para ordenar por el apellido de los usuarios.
		OrderByLink sortBySurnameLink = new OrderByLink("sortBySurnameLink",
				"surname", dataProvider) {

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
						Constants.USERS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.USERS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre de los proyectos.
		OrderByLink sortByUsernameLink = new OrderByLink("sortByUsernameLink",
				"username", dataProvider) {

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
						Constants.USERS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.USERS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el checkbox group.
		group.add(dataView);

		// agrega el link para ordenar por el nombre.
		group.add(sortBySurnameLink);

		// agrega el link para ordenar por el leader.
		group.add(sortByUsernameLink);

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
		// En este formulario se resta 1 ya que el admin no se debe contar.
		Label countLabel = new Label("count", new Long(dataProvider.size() - 1)
				.toString());
		this.add(countLabel);
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para armar el listado.
	 */
	@SuppressWarnings("unchecked")
	public DataView<UserDTO> getDataView() {
		return (DataView<UserDTO>) ((CheckGroup<UserDTO>) this.get("group"))
				.get("pageable");
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

		DataView<UserDTO> dataView = this.getDataView();
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
	 * @return una colección de oids de los usuarios seleccionados.
	 */
	public Collection<String> getSelectedUsers() {
		return this.selectedUsers;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colección de oids de los usuarios seleccionados.
	 */
	public void setSelectedUsers(Collection<String> aCollection) {
		this.selectedUsers = aCollection;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de usuarios seleccionados.
	 */
	protected int getSelectedUsersCount() {
		return this.getSelectedUsers().size();
	}

}
