/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.ItemSession;
import zinbig.item.application.pages.BasePage;
import zinbig.item.services.ServiceLocator;
import zinbig.item.util.Constants;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para presentar al usuario un combo que permite seleccionar el
 * proyecto por defecto que se utilizará para todas las actividades.<BR>
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 */
public class DefaultProjectPanel extends Panel {

  /**
   * UID por defecto para la serialización.
   */
  private static final long serialVersionUID = 5915817304778272218L;

  /**
   * Es el DTO que representa al proyecto seleccionado por el usuario.
   */
  public ProjectDTO selectedProject;

  /**
   * Es el DTO que representa al usuario en la sesión.
   */
  public UserDTO userDTO;

  /**
   * Constructor.
   * 
   * @param id
   *          es el identificador de este panel.
   * @param userDTO
   *          es el dto que representa al usuario en la sesión.
   * @param aProjectDTO
   *          es el dto que representa al proyecto en la sesión. Este parámetro puede ser nulo.
   */
  public DefaultProjectPanel(String id, final UserDTO userDTO, ProjectDTO aProjectDTO) {
    super(id);
    this.setUserDTO(userDTO);

    // agrega la etiqueta del proyecto por defecto.
    Label defaultProjectLabel = new Label("defaultProjectLabel", this.getString("defaultProjectLabel"));
    this.add(defaultProjectLabel);

    // verifica si el usuario ya no tiene un proyecto por defecto definido
    // en sus preferencias.
    if (userDTO != null) {
      String projectOID = userDTO.getUserPreference(Constants.DEFAULT_PROJECT);
      if (projectOID != null) {
        try {
          ProjectDTO defaultProjectDTO = ServiceLocator.getInstance().getProjectsService()
              .findProjectById(projectOID, false);
          this.setSelectedProject(defaultProjectDTO);
        } catch (NumberFormatException e) {

        } catch (Exception e) {

        }
      }
    }

    // arma un modelo para la lista de proyectos del usuario.
    IModel<List<ProjectDTO>> projectChoices = new AbstractReadOnlyModel<List<ProjectDTO>>() {

      /**
       * UID por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Recupera la lista de dtos de proyectos que contiene este modelo.
       * 
       * @return una lista de dtos de los proyectos almacenados como claves. <br>
       *         Como todos los mapas tienen las mismas claves se puede elegir el conjunto de claves de
       *         cualquiera.
       */
      @Override
      public List<ProjectDTO> getObject() {

        List<ProjectDTO> result = new ArrayList<ProjectDTO>();
        try {
          result.addAll(ServiceLocator.getInstance().getProjectsService().findProjectsOfUser(userDTO));
        } catch (Exception e) {
          e.printStackTrace();
        }
        return result;

      }

    };

    // crea el combo.
    DropDownChoice<ProjectDTO> select = new DropDownChoice<ProjectDTO>("defaultProjectCombo",
        new PropertyModel<ProjectDTO>(this, "selectedProject"), projectChoices);
    select.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      /**
       * UID por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Notifica al receptor que se ha actualizado la selección en el combo. En este caso se actualiza el dto
       * del proyecto en la sesión y se registra la nueva preferencia del usuario.
       * 
       * @param aTarget
       *          es el objetivo de este request ajax.
       */
      @Override
      protected void onUpdate(AjaxRequestTarget aTarget) {
        ((ItemSession) DefaultProjectPanel.this.getSession()).setProjectDTO(DefaultProjectPanel.this
            .getSelectedProject());

        ((BasePage) DefaultProjectPanel.this.getPage()).updateUserPreferences(Constants.DEFAULT_PROJECT,
            DefaultProjectPanel.this.getSelectedProject().getOid().toString());

      }
    });
    this.add(select);
  }

  /**
   * Getter.
   * 
   * @return el dto que representa al proyecto seleccionado.
   */
  public ProjectDTO getSelectedProject() {
    return this.selectedProject;
  }

  /**
   * Setter.
   * 
   * @param aDTO
   *          es el dto que representa al proyecto seleccionado.
   */
  public void setSelectedProject(ProjectDTO aDTO) {
    this.selectedProject = aDTO;
  }

  /**
   * Getter.
   * 
   * @return el dto que representa al usuario en la sesión.
   */
  public UserDTO getUserDTO() {
    return this.userDTO;
  }

  /**
   * Setter.
   * 
   * @param aDTO
   *          es el dto que representa al usuario en la sesión.
   */
  public void setUserDTO(UserDTO aDTO) {
    this.userDTO = aDTO;
  }

}
