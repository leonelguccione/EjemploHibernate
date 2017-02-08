/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
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
import zinbig.item.application.dataproviders.ProjectDTODataProvider;
import zinbig.item.application.pages.AddProjectPage;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.EditProjectPage;
import zinbig.item.application.pages.ProjectsAdministrationPage;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de esta clase se utilizan para presentar al usuario una lista
 * de proyectos. Estos proyectos pueden ser seleccionados para realizar
 * operaciones sobre ellos como eliminarlos o editarlos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ProjectsAdministrationForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2133281244659004147L;

	/**
	 * Es una colección de Oids de proyectos que han sido seleccionados.
	 */
	private Collection<String> selectedProjects;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es la identificación de este formulario.
	 * @param aPage
	 *            es la página en la cual se está agregando este componente.
	 */
	public ProjectsAdministrationForm(String anId,
			ProjectsAdministrationPage aPage) {
		super(anId);

		// agrega el link para poder dar de alta un nuevo proyecto
		Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
				"newProjectLink", AddProjectPage.class);
		this.add(aLink);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this
				.getString("projectsAdministrationForm.noProjectSelected"));
		messages.add(this
				.getString("projectsAdministrationForm.errorDeletingProjects"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedProjects(new HashSet<String>());

		CheckGroup<ProjectDTO> group = new CheckGroup<ProjectDTO>("group",
				new PropertyModel<Collection<ProjectDTO>>(this,
						"selectedProjects"));
		this.add(group);
		group.add(new CheckGroupSelector("groupselector"));

		SubmitLink deleteLink = new SubmitLink("deleteProjectsLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el requerimiento para borrar los proyectos seleccionados.
			 * En caso de que no se hayan seleccionado proyectos se muestra un
			 * cartel de error al usuario.
			 */
			@Override
			public void onSubmit() {
				if (ProjectsAdministrationForm.this.getSelectedProjectsCount() == 0) {
					this
							.error(this
									.getString("projectsAdministrationForm.noProjectSelected"));
				} else {
					try {
						ProjectsAdministrationForm.this.getProjectsService()
								.deleteProjects(
										ProjectsAdministrationForm.this
												.getSelectedProjects());
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("projectsAdministrationForm.errorDeletingProjects"));
					}
				}
			}

		};

		this.add(deleteLink);

		// recupera las preferencias de ordenamiento del usuario
		String columnName = this.getUserDTO().getUserPreference(
				Constants.PROJECTS_ADMINISTRATION_COLUMN_NAME);
		String columnOrder = this.getUserDTO().getUserPreference(
				Constants.PROJECTS_ADMINISTRATION_COLUMN_ORDER);

		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.PROJECTS_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.PROJECTS_ADMINISTRATION_COLUMN_ORDER);
		}

		final ProjectDTODataProvider dataProvider = new ProjectDTODataProvider(
				columnName, columnOrder);

		final DataView<ProjectDTO> dataView = new DataView<ProjectDTO>(
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
			protected void populateItem(final Item<ProjectDTO> item) {
				ProjectDTO dto = item.getModelObject();

				PageParameters parameters = new PageParameters();
				parameters.put("projectOID", dto.getOid());

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);
				Label aLabel = new Label("name", dto.getName());

				Link aLink = new BookmarkablePageLink("editLink",
						EditProjectPage.class, parameters);
				aLink.add(aLabel);
				item.add(aLink);

				item.add(new Label("shortname", dto.getShortName()));
				item.add(new Label("link", dto.getLink()));
			}
		};

		dataView.setItemsPerPage(aPage.getItemsPerPage());

		// agrega el link para ordenar por el nombre de los proyectos.
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
						Constants.PROJECTS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.PROJECTS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el link para ordenar por el nombre de los proyectos.
		OrderByLink sortByLeaderLink = new OrderByLink("sortByLinkLink",
				"projectLink", dataProvider) {

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
						Constants.PROJECTS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.PROJECTS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};

		// agrega el checkbox group.
		group.add(dataView);

		// agrega el link para ordenar por el nombre.
		group.add(sortByNameLink);

		// agrega el link para ordenar por el leader.
		group.add(sortByLeaderLink);

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
	 * Getter.
	 * 
	 * @return una colección que contiene los oids de los proyectos
	 *         seleccionados.
	 */
	protected Collection<String> getSelectedProjects() {
		return this.selectedProjects;
	}

	/**
	 * Setter.
	 * 
	 * @param someProjectsOids
	 *            es la nueva colección de oids de proyectos seleccionados.
	 */
	private void setSelectedProjects(Collection<String> someProjectsOids) {
		this.selectedProjects = someProjectsOids;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos seleccionados.
	 */
	protected int getSelectedProjectsCount() {
		return this.getSelectedProjects().size();
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para armar el listado.
	 */
	@SuppressWarnings("unchecked")
	public DataView<ProjectDTO> getDataView() {
		return (DataView<ProjectDTO>) ((CheckGroup<ProjectDTO>) this
				.get("group")).get("pageable");
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

		DataView<ProjectDTO> dataView = this.getDataView();
		ButtonPagingNavigator navigator = (ButtonPagingNavigator) this
				.get("navigator");

		dataView.setItemsPerPage(aNumber);

		if (dataView.getDataProvider().size() > aNumber) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

	}

}
