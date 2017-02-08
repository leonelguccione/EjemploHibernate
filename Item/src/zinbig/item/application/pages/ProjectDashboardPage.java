/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.ItemSession;
import zinbig.item.application.components.DefaultProjectPanel;
import zinbig.item.model.exceptions.ProjectUnknownException;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta página se utilizan para mostrar información resumida sobre el estado de los
 * proyectos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class ProjectDashboardPage extends BasePage {

  public String projectOid;

  /**
   * Constructor.
   * 
   * @param parameters
   *          es un diccionario que contiene los parámetros requeridos para esta página.
   */
  public ProjectDashboardPage(PageParameters parameters) {
    super();

    try {
      String aProjectOid = Utils.decodeString(parameters.getString("PROJECT_OID"));
      projectOid = aProjectOid;
      final ProjectsServiceBI aService = this.getProjectsService();

      final ProjectDTO aProjectDTO = aService.findProjectById(aProjectOid, false);
      // agrega el link para poder asignar al usuario un nuevo proyecto
      // favorito
      final UserDTO userDTO = this.getUserDTO();

      Link<BasePage> aLink = new Link<BasePage>("addFavoriteProjectLink") {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Marca al proyecto como favorito. Se asume que hay un usuario en la sesión, ya que si no existiera
         * este link estaría invisible.
         */
        @Override
        public void onClick() {
          try {
            UserDTO anUserDTO = aService.addFavoriteProjectToUser(aProjectDTO, userDTO);
            ((ItemSession) this.getSession()).setUserDTO(anUserDTO);
          } catch (Exception e) {

            e.printStackTrace();
          } finally {

            // actualiza el componente de menú
            ((BasePage) this.getPage()).updateMenu();
            PageParameters param = new PageParameters();
            param.put("PROJECT_OID", Utils.encodeString(aProjectDTO.getOid()));
            this.setResponsePage(ProjectDashboardPage.class, param);
          }
        }

      };

      aLink.setVisible(userDTO != null && !userDTO.isFavoriteProject(aProjectDTO));
      this.add(aLink);

      Link<BasePage> removeFavoriteLink = new Link<BasePage>("removeFavoriteProjectLink") {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Quita la marca al proyecto como favorito. Se asume que hay un usuario en la sesión, ya que si no
         * existiera este link estaría invisible.
         */
        @Override
        public void onClick() {
          try {
            UserDTO anUserDTO = aService.removeFavoriteProjectFromUser(aProjectDTO, userDTO);
            ((ItemSession) this.getSession()).setUserDTO(anUserDTO);
          } catch (Exception e) {
            e.printStackTrace();
          } finally {

            // actualiza el componente de menú
            ((BasePage) this.getPage()).updateMenu();
            PageParameters param = new PageParameters();
            param.put("PROJECT_OID", Utils.encodeString(aProjectDTO.getOid()));
            this.setResponsePage(ProjectDashboardPage.class, param);
          }
        }

      };
      removeFavoriteLink.setVisible(userDTO != null && userDTO.isFavoriteProject(aProjectDTO));
      this.add(removeFavoriteLink);

      Label projectName = new Label("projectName", aProjectDTO.getName());
      this.add(projectName);
      if (userDTO != null && aProjectDTO != null) {
        ((DefaultProjectPanel) this.get("defaultProjectPanel")).setSelectedProject(aProjectDTO);
      }

    } catch (ProjectUnknownException pue) {
      pue.printStackTrace();

      this.setResponsePage(ErrorPage.class);

    } catch (Exception e) {
      e.printStackTrace();
      this.setResponsePage(DashboardPage.class);
    }

  }

  /**
   * Este método es invocado al finalizar el renderizado de todos los componentes de la página. Se utiliza
   * para agregar el javascript requerido por los gráficos de Flash para invocar al servlet que ejecuta las
   * consultas.
   */
  @Override
  protected void onAfterRender() {

    super.onAfterRender();
    final Response response = this.getResponse();

    ItemApplication application = (ItemApplication) this.getApplication();
    String host = application.getSystemProperty("hostName");

    // agrega el componente para mostrar los ítems creados en el día de hoy
    // por tipo de ítem.
    response
        .write(" <script type=\"text/javascript\"> var myChart = new FusionCharts(\"/Item/FusionCharts/FCF_Column2D.swf\", \"myChartId\", \"400\", \"400\");        myChart.setDataURL(\"http://"
            + host
            + "/Item/FusionChartSerlvet?projectOid="
            + "A"
            + projectOid
            + "\");        myChart.render(\"chartdivA\");     </script>");
    // agrega el componente para mostrar los ítems abiertos por prioridad.
    response
        .write(" <script type=\"text/javascript\"> var myChart = new FusionCharts(\"/Item/FusionCharts/FCF_Pie3D.swf\", \"myChartId\", \"400\", \"400\");        myChart.setDataURL(\"http://"
            + host
            + "/Item/FusionChartSerlvet?projectOid="
            + "B"
            + projectOid
            + "\");   myChart.render(\"chartdivB\");     </script>");

    // agrega el componente para mostrar los ítems por su estado.
    response
        .write(" <script type=\"text/javascript\"> var myChart = new FusionCharts(\"/Item/FusionCharts/FCF_Column2D.swf\", \"myChartId\", \"400\", \"400\");        myChart.setDataURL(\"http://"
            + host
            + "/Item/FusionChartSerlvet?projectOid="
            + "C"
            + projectOid
            + "\");        myChart.render(\"chartdivC\");     </script>");

    // agrega el componente para mostrar el acumulado de los ítems abiertos
    // y cerrados por período
    response
        .write(" <script type=\"text/javascript\"> var myChart = new FusionCharts(\"/Item/FusionCharts/FCF_MSArea2D.swf\", \"myChartId\", \"400\", \"400\");        myChart.setDataURL(\"http://"
            + host
            + "/Item/FusionChartSerlvet?projectOid="
            + "D"
            + projectOid
            + "\");        myChart.render(\"chartdivD\");     </script>");
  }

}
