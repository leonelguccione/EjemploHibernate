/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.EditProjectBasicDataPanel;
import zinbig.item.application.components.ManageAdditionalPropertiesPanel;
import zinbig.item.application.components.ManageItemTypesPanel;
import zinbig.item.application.components.ManagePrioritiesPanel;
import zinbig.item.application.components.ManageWorkflowLinkDescriptionsPanel;
import zinbig.item.application.components.ManageWorkflowNodeDescriptionsPanel;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de esta clase se utilizan para editar la información de los proyectos. <br>
 * A esta página se accede mediante el listado de todos los proyectos existentes al seleccionar uno en
 * particular para su edición.<br>
 * Al editar un proyecto se permite cambiar el nombre, siempre y cuando no se repita; el resto de los datos se
 * puede cambiar siempre que no se violen las reglas del dominio.<br>
 * Esta página requiere la presencia de un oid (projectOID) de un proyecto en el diccionario que se recibe
 * como parámetro en el constructor. <br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class EditProjectPage extends SecuredPage {

  /**
   * Constructor.
   * 
   * @param parameters
   *          es un diccionario que contiene todos los parámetros requeridos para la creación de esta página.
   */
  public EditProjectPage(PageParameters parameters) {
    super();
    try {
      // agrega el link que permite volver a ver todos los proyectos ya
      // existentes.
      Link<BasePage> aLink = new BookmarkablePageLink<BasePage>("viewAllLink",
          ProjectsAdministrationPage.class);
      this.add(aLink);

      final String anOid = parameters.getString("projectOID");

      final ProjectDTO aProjectDTO = this.getProjectsService().findProjectById(anOid, true);
      ((ItemSession) this.getSession()).setProjectDTO(aProjectDTO);

      // crea las solapas para editar los datos básicos, editar las
      // prioridades y editar el workflow.
      List<ITab> tabs = new ArrayList<ITab>();
      tabs.add(new AbstractTab(new Model<String>(this.getString("editProjectPage.basicData"))) {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Getter.
         * 
         * @param anId
         *          es el identificador del panel.
         * @return el panel que se utiliza para editar los detalles básicos del proyecto.
         */
        @Override
        public Panel getPanel(String anId) {
          return new EditProjectBasicDataPanel(anId, anOid);
        }

      });

      tabs.add(new AbstractTab(new Model<String>(this.getString("editProjectPage.priorities"))) {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Getter.
         * 
         * @param anId
         *          es el identificador del panel.
         * @return el panel que se utiliza para editar las prioridades del proyecto.
         */
        @Override
        public Panel getPanel(String anId) {

          return new ManagePrioritiesPanel(anId, aProjectDTO.getPrioritySetDTO().getOid());
        }

      });

      tabs.add(new AbstractTab(new Model<String>(this.getString("editProjectPage.itemsTypes"))) {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Getter.
         * 
         * @param anId
         *          es el identificador del panel.
         * @return el panel que se utiliza para editar los tipos de ítems del proyecto.
         */
        @Override
        public Panel getPanel(String anId) {

          return new ManageItemTypesPanel(anId, aProjectDTO);
        }

      });
      tabs.add(new AbstractTab(new Model<String>(this.getString("editProjectPage.workflowLinkDescriptions"))) {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Getter.
         * 
         * @param anId
         *          es el identificador del panel.
         * @return el panel que se utiliza para editar los enlaces del workflow del proyecto.
         */
        @Override
        public Panel getPanel(String anId) {

          return new ManageWorkflowLinkDescriptionsPanel(anId, aProjectDTO.getWorkflowDescriptionDTO()
              .getOid(), aProjectDTO.getOid());
        }

      });

      tabs.add(new AbstractTab(new Model<String>(this.getString("editProjectPage.workflowNodeDescriptions"))) {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = -4952268724528231622L;

        /**
         * Getter.
         * 
         * @param anId
         *          es el identificador del panel.
         * @return el panel que se utiliza para editar el workflow del proyecto.
         */
        @Override
        public Panel getPanel(String anId) {

          return new ManageWorkflowNodeDescriptionsPanel(anId, aProjectDTO.getWorkflowDescriptionDTO()
              .getOid(), aProjectDTO.getOid());
        }

      });

      tabs.add(new AbstractTab(new Model<String>(this.getString("editProjectPage.additionalProperties"))) {

        /**
         * UID por defecto.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Getter.
         * 
         * @param anId
         *          es el identificador del panel.
         * @return el panel que se utiliza para editar las propiedades adicionales del proyecto.
         */
        @Override
        public Panel getPanel(String anId) {

          return new ManageAdditionalPropertiesPanel(anId, aProjectDTO);
        }

      });

      TabbedPanel tabPanel = new TabbedPanel("tabs", tabs);

      this.add(tabPanel);

      if (parameters.containsKey("TAB")) {
        tabPanel.setSelectedTab(parameters.getInt("TAB"));
      }
    } catch (Exception e) {

      e.printStackTrace();
      this.setResponsePage(DashboardPage.class);
    }

  }

}
