/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.ButtonPagingNavigator;
import zinbig.item.application.dataproviders.ProjectDTODataProviderForUser;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.Pageable;
import zinbig.item.application.pages.ProjectDashboardPage;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de esta clase se utilizan para listar los proyectos y poder operar con ellos. Si el usuario
 * que está listando los proyectos es anónimo no aparecen los links de operación. Se requiere un formulario
 * para poder operar con los proyectos ya que sino no se envían los datos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class ListProjectsForm extends ItemForm {

  /**
   * UID por defecto.
   */
  private static final long serialVersionUID = 871588569539200114L;

  /**
   * Es una colección que mantiene los oids de los proyectos seleccionados.
   */
  public Collection<String> selectedProjects;

  /**
   * Constructor.
   * 
   * @param anId
   *          es el identificador de este formulario.
   */
  public ListProjectsForm(String anId, Pageable aPageable) {
    super(anId);

    // crea el componente para mostrar los errores
    Collection<String> messages = new ArrayList<String>();
    messages.add(this.getString("NoProjectSelected"));
    messages.add(this.getString("FavoriteProjectsCountExceeded"));
    this.add(this.createFeedbackPanel(messages));

    this.setSelectedProjects(new ArrayList<String>());

    // agrega el link para poder marcar como favoritos a los proyectos
    SubmitLink aLink = new SubmitLink("favoriteLink") {

      /**
       * Uid por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Marca como favoritos a todos los proyectos seleccionados.
       */
      @Override
      public void onSubmit() {

        if (getSelectedProjects().isEmpty()) {
          this.error(this.getString("NoProjectSelected"));
        } else {
          if ((getSelectedProjects().size() + getUserDTO().getFavoriteProjectsCount()) > 3) {
            this.error(this.getString("FavoriteProjectsCountExceeded"));
          } else {
            try {

              ListProjectsForm.this.getProjectsService().addFavoriteProjectsToUser(
                  ListProjectsForm.this.getSelectedProjects(), ListProjectsForm.this.getUserDTO());

              ListProjectsForm.this.getSelectedProjects().clear();

              // actualiza el componente de menú
              ((BasePage) this.getPage()).updateMenu();
            } catch (Exception e) {

              e.printStackTrace();
            }
          }
        }

      }
    };
    this.add(aLink);
    aLink.setVisible(this.getUserDTO() != null && this.getUserDTO().getFavoriteProjectsCount() < 3);

    CheckGroup<ProjectDTO> group = new CheckGroup<ProjectDTO>("group",
        new PropertyModel<Collection<ProjectDTO>>(this, "selectedProjects"));
    this.add(group);
    group.add(new CheckGroupSelector("groupselector"));

    // recupera las preferencias de ordenamiento del usuario
    String columnName = null;
    String columnOrder = null;

    if (this.getUserDTO() != null) {
      columnName = this.getUserDTO().getUserPreference(Constants.VIEW_PROJECTS_COLUMN_NAME);
      columnOrder = this.getUserDTO().getUserPreference(Constants.VIEW_PROJECTS_COLUMN_ORDER);
    }
    if (columnName == null) {
      columnName = this.getSystemProperty(Constants.VIEW_PROJECTS_COLUMN_NAME);
      columnOrder = this.getSystemProperty(Constants.VIEW_PROJECTS_COLUMN_ORDER);
    }

    final ProjectDTODataProviderForUser dataProvider = new ProjectDTODataProviderForUser(this.getUserDTO(),
        columnName, columnOrder);

    // crea el componente para listar los proyectos.
    final DataView<ProjectDTO> projects = new DataView<ProjectDTO>("pageable", dataProvider) {

      /**
       * UID por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Arma cada línea del listado, conteniendo un check para seleccionar el elemento, el nombre de la
       * prioridad, el valor y la imagen.
       */
      @SuppressWarnings("unchecked")
      @Override
      protected void populateItem(Item<ProjectDTO> item) {
        ProjectDTO dto = (ProjectDTO) item.getModelObject();

        Check check = new Check("cb", new PropertyModel(dto, "oid"));
        item.add(check);

        Label nameLabel = new Label("name", dto.getName());

        PageParameters parameters = new PageParameters();
        parameters.put("PROJECT_OID", dto.getOid());
        Link<String> aLink = new BookmarkablePageLink<String>("viewLink", ProjectDashboardPage.class,
            parameters);
        aLink.add(nameLabel);
        item.add(aLink);

        Label shortnameLabel = new Label("shortname", dto.getShortName());
        item.add(shortnameLabel);

        Label itemsCountLabel = new Label("itemsCount", dto.getItemsCount().toString());
        item.add(itemsCountLabel);

        ExternalLink externalLink = new ExternalLink("linkLink", dto.getLink(), dto.getLink());
        item.add(externalLink);
      }

    };

    projects.setItemsPerPage(aPageable.getItemsPerPage());

    // agrega el link para ordenar por el nombre de los proyectos.
    OrderByLink sortByNameLink = new OrderByLink("sortByNameLink", "name", dataProvider) {

      /**
       * UID por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Notifica a este objeto que se cambió el orden. Reinicia el componente para dejarlo en la primer
       * página.
       */
      @Override
      protected void onSortChanged() {
        super.onSortChanged();
        projects.setCurrentPage(0);
        SortParam sp = dataProvider.getSort();

        BasePage page = (BasePage) this.getPage();
        page.updateUserPreferences(Constants.VIEW_PROJECTS_COLUMN_NAME, sp.getProperty());
        page.updateUserPreferences(Constants.VIEW_PROJECTS_COLUMN_ORDER, sp.isAscending() ? "ASC" : "DESC");
      }

    };

    group.add(projects);

    // agrega el link para ordenar por el nombre.
    group.add(sortByNameLink);

    // agrega el componente de navegación
    ButtonPagingNavigator navigator = new ButtonPagingNavigator("navigator", projects);
    this.add(navigator);
    int size = dataProvider.size();
    if (size > aPageable.getItemsPerPage()) {
      navigator.setVisible(true);
    } else {
      navigator.setVisible(false);
    }

    // agrega el componente que muestra la cantidad de elementos.
    Label countLabel = new Label("count", new Long(size).toString());
    this.add(countLabel);
  }

  /**
   * Getter.
   * 
   * @return una colección que contiene los oids de los proyectos seleccionados.
   */
  public Collection<String> getSelectedProjects() {
    return this.selectedProjects;
  }

  /**
   * Setter.
   * 
   * @param someProjects
   *          es una colección que contiene los oids de los proyectos seleccionados.
   */
  public void setSelectedProjects(Collection<String> someProjects) {
    this.selectedProjects = someProjects;
  }

  /**
   * Actualiza el componente de navegación para verificar que hay más páginas para mostrar tomando como
   * entrada la cantidad de elementos que se muestran en la página. Si la cantidad de elementos que se deben
   * mostrar es mayor a la cantidad total de elementos, el navegador no aparece.
   * 
   * @param aNumber
   *          es el número de elementos a mostrar por página.
   */
  public void updateItemsPerPage(int aNumber) {

    DataView<ProjectDTO> dataView = this.getDataView();
    ButtonPagingNavigator navigator = (ButtonPagingNavigator) this.get("navigator");

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
   * @return el componente que se utiliza para armar el listado.
   */
  @SuppressWarnings("unchecked")
  public DataView<ProjectDTO> getDataView() {
    return (DataView<ProjectDTO>) ((CheckGroup<ProjectDTO>) this.get("group")).get("pageable");
  }

}
