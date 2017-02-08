/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.ProjectsAdministrationPage;
import zinbig.item.model.exceptions.ProjectNameNotUniqueException;
import zinbig.item.model.exceptions.ProjectUnknownException;
import zinbig.item.services.ServiceLocator;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase se utilizan para editar un proyecto en la aplicación. <br>
 * La edición de un proyecto se realiza editando el nombre (que no puede repetirse, por lo que se controla con
 * una llamada AJAX), el nombre corto, un link para la página del proyecto (opcional) y si es público o
 * privado este proyecto (si es público aparecerá en el menú de lo usuarios que no se hayan registrado).
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class EditProjectForm extends ItemForm implements FormWithAjaxUpdatableComponent {

  /**
   * UID por defecto para la serialización.
   */
  private static final long serialVersionUID = -198805381797719666L;

  /**
   * Es un campo que almacena el nombre provisto para el nuevo proyecto..
   */
  public String nameEPF;

  /**
   * Es el nombre corto para el nuevo proyecto.
   */
  public String shortnameEPF;

  /**
   * Mantiene el link asociado al proyecto.
   */
  public String linkEPF;

  /**
   * Indica si el proyecto que se está agregando es público o no.
   */
  public boolean publicProjectEPF;

  /**
   * Mantiene la referencia al dto del proyecto que se está editando.
   */
  public ProjectDTO projectDTO;

  /**
   * Es el dto que representa al usuario líder del proyecto que se está editando.
   */
  public UserDTO selectedProjectLeader;

  /**
   * Mantiene la selección de qué estrategia de asignación de responsable de los nuevos ítems se ha
   * seleccionado.
   */
  public String selectedAssignmentStrategyEPF;

  /**
   * Constructor.
   * 
   * @param id
   *          es el identificador de este formulario dentro del panel.
   * @param aProjectOid
   *          es el oid del proyecto que se está editando.
   */
  @SuppressWarnings("unchecked")
  public EditProjectForm(String id, String aProjectOid) {
    super(id);

    try {
      // recupera el dto del proyecto que se pretende editar.
      ProjectDTO aProjectDTO = this.getProjectsService().findProjectById(aProjectOid, true);
      this.setProjectDTO(aProjectDTO);

      // construye el input field para el nombre
      this.createComponentForName(aProjectDTO);

      // construye el input field para el nombre corto
      this.createComponentForShortname(aProjectDTO);

      // construye el componente para editar el líder del proyecto
      this.createComponentForLeader(aProjectDTO);

      // construye el input field para el link del proyecto
      this.createComponentForProjectLink(aProjectDTO);

      // construye el combo para mostrar las prioridades
      // this.createComponentForPriorities(aProjectDTO);

      // construye el componente para seleccionar la estrategia de
      // asignación del primer responsable
      this.createComponentForAssignmentStrategies(aProjectDTO);

      // construye el chekbox para definir si es un proyecto público o no
      final CheckBox checkBox = new CheckBox("publicProject", new PropertyModel(this, "publicProjectEPF"));
      this.add(checkBox);
      this.setPublicProjectEPF(aProjectDTO.isPublicProject());

      // crea el link para enviar el formulario
      this.createComponentForSubmitLink();

      // construye el link de cancelación del formulario
      Link cancelLink = new BookmarkablePageLink("cancelLink", ProjectsAdministrationPage.class);
      this.add(cancelLink);

    } catch (ProjectUnknownException e) {
      error(this.getString("ProjectUnknownException"));
      e.printStackTrace();
      this.setResponsePage(ErrorPage.class);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Crea el componente que se utilizará para enviar los datos del formulario.
   */
  private void createComponentForSubmitLink() {
    // construye el link de envío del formulario
    SubmitLink submitLink = new SubmitLink("submitLink") {

      /**
       * UID por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Realiza el envío de la información.
       */
      @Override
      public void onSubmit() {
        try {
          getProjectDTO().setLink(getLinkEPF());
          getProjectDTO().setName(getNameEPF());
          getProjectDTO().setShortName(getShortnameEPF());
          getProjectDTO().setPublicProject(isPublicProjectEPF());
          getProjectDTO().setProjectLeaderDTO(getSelectedProjectLeader());
          getProjectDTO().setItemAssignmentStrategy(getSelectedAssignmentStrategyEPF());

          EditProjectForm.this.getProjectsService().editProject(getProjectDTO());

          this.setResponsePage(ProjectsAdministrationPage.class);
        } catch (ProjectNameNotUniqueException nuue) {
          nuue.printStackTrace();
          EditProjectForm.this.get("nameEPF").error(getString("ProjectNameNotUniqueException"));
        } catch (Exception e) {

          setResponsePage(ErrorPage.class);
          e.printStackTrace();
        }
      }

    };
    this.add(submitLink);
  }

  /**
   * Crea el componente que se utiliza para editar el link del proyecto.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que se está editando.
   */
  private void createComponentForProjectLink(ProjectDTO aProjectDTO) {
    final TextField<String> linkField = new TextField<String>("linkEPF", new PropertyModel<String>(this,
        "linkEPF"));
    linkField.add(new UrlValidator());
    linkField.add(StringValidator.maximumLength(new Integer(this
        .getSystemProperty("AddProjectForm.link.length"))));
    this.add(linkField);
    this.setLinkEPF(aProjectDTO.getLink());

    // construye el label para mostrar los mensajes de error
    // relacionados
    // con el componente del link del proyecto.
    final FeedbackLabel linkFeedbackLabel = new FeedbackLabel("linkEPFFeedback", linkField);
    linkFeedbackLabel.setOutputMarkupId(true);
    this.add(linkFeedbackLabel);
  }

  /**
   * Crea el componente que se utiliza para editar el nombre corto del proyecto.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que se está editando.
   */
  private void createComponentForShortname(ProjectDTO aProjectDTO) {
    final TextField<String> shortnameField = new TextField<String>("shortnameEPF", new PropertyModel<String>(
        this, "shortnameEPF"));
    shortnameField.setRequired(true);
    shortnameField.add(new ItemSimpleStringValidator(shortnameField));
    shortnameField.add(StringValidator.exactLength(new Integer(this
        .getSystemProperty("AddProjectForm.shortname.length"))));
    this.add(shortnameField);
    this.setShortnameEPF(aProjectDTO.getShortName());

    // construye el label para mostrar los mensajes de error
    // relacionados
    // con el componente del nombre corto del proyecto.
    final FeedbackLabel shortnameFeedbackLabel = new FeedbackLabel("shortnameEPFFeedback", shortnameField);
    shortnameFeedbackLabel.setOutputMarkupId(true);
    this.add(shortnameFeedbackLabel);
  }

  /**
   * Crea el componente que se utiliza para editar el nombre del proyecto.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que se está editando.
   */
  private void createComponentForName(ProjectDTO aProjectDTO) {
    final TextField<String> nameField = new TextField<String>("nameEPF", new PropertyModel<String>(this,
        "nameEPF"));
    nameField.setRequired(true);
    nameField.add(new ItemSimpleStringValidator(nameField));
    nameField.add(new FormComponentAjaxBehavior(this, "nameEPFFeedback"));
    nameField.add(StringValidator.maximumLength(new Integer(this
        .getSystemProperty("AddProjectForm.name.length"))));
    this.add(nameField);
    this.setNameEPF(aProjectDTO.getName());

    // construye el label para mostrar los mensajes de error
    // relacionados
    // con el componente del nombre del proyecto.
    final FeedbackLabel nameFeedbackLabel = new FeedbackLabel("nameEPFFeedback", nameField);
    nameFeedbackLabel.setOutputMarkupId(true);
    this.add(nameFeedbackLabel);

    // construye la imagen indicadora para Ajax
    final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImg");
    imageContainer.setOutputMarkupId(true);
    this.add(imageContainer);

  }

  /**
   * Getter.
   * 
   * @return el nombre del proyecto.
   */
  public String getNameEPF() {
    return this.nameEPF;
  }

  /**
   * Setter.
   * 
   * @param aName
   *          es el nombre del proyecto.
   */
  public void setNameEPF(String aName) {
    this.nameEPF = aName;
  }

  /**
   * Getter.
   * 
   * @return es el nombre corto del nuevo proyecto.
   */
  public String getShortnameEPF() {
    return this.shortnameEPF;
  }

  /**
   * Setter.
   * 
   * @param aName
   *          es el nombre corto del proyecto.
   */
  public void setShortnameEPF(String aName) {
    this.shortnameEPF = aName;
  }

  /**
   * Getter.
   * 
   * @return el link asociado al proyecto.
   */
  public String getLinkEPF() {
    return this.linkEPF;
  }

  /**
   * Setter.
   * 
   * @param aLink
   *          es el link asociado al proyecto.
   */
  public void setLinkEPF(String aLink) {
    this.linkEPF = aLink;
  }

  /**
   * Este método se dispara a raíz de la actualización del campo name por parte de un evento AJAX.<br>
   * Este método invoca al servicio de proyectos para verificar que no exista un proyecto con el nombre
   * ingresado. En caso de que exista ya un proyecto se muestra un cartel de error.
   */
  public void ajaxComponentUpdated() {

    try {
      if (!this.getProjectDTO().getName().equals(this.getNameEPF())) {
        if (this.getProjectsService().existsProjectWithName(this.getNameEPF())) {

          this.get("nameEPF").error(getString("ProjectNameNotUniqueException"));

        }
      }
    } catch (Exception e) {
      // no se hace nada en caso de error. Si existe un proyecto con el
      // mismo nombre el servicio de edición de proyectos detectará la
      // excepción.
      e.printStackTrace();
    }

  }

  /**
   * Getter.
   * 
   * @return el id del contenedor de la imagen utilizada por el componente AJAX.
   */
  public String getMarkupIdForImageContainer() {
    return this.get("indicatorImg").getMarkupId();
  }

  /**
   * Getter.
   * 
   * @return true en caso de que el proyecto sea público; false en caso contrario.
   */
  public boolean isPublicProjectEPF() {
    return this.publicProjectEPF;
  }

  /**
   * Setter.
   * 
   * @param aBoolean
   *          es una indicación si el proyecto nuevo es público o no.
   */
  public void setPublicProjectEPF(boolean aBoolean) {
    this.publicProjectEPF = aBoolean;
  }

  /**
   * Getter.
   * 
   * @return el dto del proyecto que se está editando.
   */
  public ProjectDTO getProjectDTO() {
    return this.projectDTO;
  }

  /**
   * Setter.
   * 
   * @param aProjectDTO
   *          es el dto del proyecto que se está editando.
   */
  public void setProjectDTO(ProjectDTO aProjectDTO) {
    this.projectDTO = aProjectDTO;
  }

  /**
   * Este método es ejecutado al finalizar la creación del html correspondiente a este componente.
   */
  @Override
  protected void onAfterRender() {

    super.onAfterRender();
    final Response response = this.getResponse();
    response
        .write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[1].focus()</script>");
  }

  /**
   * Crea un componente para editar el líder del proyecto.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que se está editando.
   */
  @SuppressWarnings("unchecked")
  private void createComponentForLeader(ProjectDTO aProjectDTO) {

    // obtiene todos los usuarios
    Collection<UserDTO> users = new ArrayList<UserDTO>();
    try {
      users.addAll(this.getUsersService().getAllUsers());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // construye el componente para mostrar la lista
    DropDownChoice usersChoice = new DropDownChoice("leaderEPF", new PropertyModel<String>(this,
        "selectedProjectLeader"), (List<UserDTO>) users, new IChoiceRenderer() {

      /**
       * UID por defecto.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Getter.
       * 
       * @param anObject
       *          es el objeto que se está representando.
       * @return la representación como String que se debe mostrar para este objeto.
       */
      public String getDisplayValue(Object anObject) {

        UserDTO aDto = (UserDTO) anObject;

        return aDto.getSurname() + ", " + aDto.getName();
      }

      /**
       * Getter.
       * 
       * @param anObject
       *          es el objeto que se está representando.
       * @param index
       *          es la posición dentro de la lista del objeto.
       * @return el valor que se debe asignar cuando este objeto es seleccionado.
       */
      public String getIdValue(Object anObject, int index) {

        UserDTO aDto = (UserDTO) anObject;

        return aDto.getOid();
      }
    });
    usersChoice.setRequired(true);
    this.add(usersChoice);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del conjunto de usuarios.
    final FeedbackLabel usersChoiceFeedbackLabel = new FeedbackLabel("leaderEPFFeedback", usersChoice);
    usersChoiceFeedbackLabel.setOutputMarkupId(true);
    this.add(usersChoiceFeedbackLabel);

    // recupera el usuario asignado como líder para mostrarlo
    // como seleccionado
    UserDTO aDto;
    try {
      aDto = ServiceLocator.getInstance().getUsersService()
          .findUserWithUsername(aProjectDTO.getProjectLeaderDTO().getUsername(), "C");
      this.setSelectedProjectLeader(aDto);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Getter.
   * 
   * @return el dto que representa al líder del proyecto que se está editando.
   */
  public UserDTO getSelectedProjectLeader() {
    return this.selectedProjectLeader;
  }

  /**
   * Setter.
   * 
   * @param anUserDTO
   *          el dto que representa al líder del proyecto que se está editando.
   */
  public void setSelectedProjectLeader(UserDTO anUserDTO) {
    this.selectedProjectLeader = anUserDTO;
  }

  /**
   * Crea el componente para poder seleccionar la estrategia de asignación de responsables de los nuevos
   * ítems.
   * 
   * @param aProjectDTO
   *          es el dto que representa al proyecto que se está editando
   */
  @SuppressWarnings("unchecked")
  private void createComponentForAssignmentStrategies(ProjectDTO aProjectDTO) {
    Collection<String> strategies = new ArrayList<String>();
    try {
      strategies = ((ItemApplication) this.getApplication()).getItemAssignmentStrategies();
    } catch (Exception e) {
      e.printStackTrace();
    }

    DropDownChoice strategiesChoice = new DropDownChoice("itemAssignmentStrategyEPF",
        new PropertyModel<String>(this, "selectedAssignmentStrategyEPF"), (List<String>) strategies,
        new IChoiceRenderer() {

          /**
           * UID por defecto.
           */
          private static final long serialVersionUID = 1L;

          /**
           * Getter.
           * 
           * @param anObject
           *          es el objeto que se está representando.
           * @return la representación como String que se debe mostrar para este objeto.
           */
          public String getDisplayValue(Object anObject) {

            return EditProjectForm.this.getString((String) anObject);
          }

          /**
           * Getter.
           * 
           * @param anObject
           *          es el objeto que se está representando.
           * @param index
           *          es la posición dentro de la lista del objeto.
           * @return el valor que se debe asignar cuando este objeto es seleccionado.
           */
          public String getIdValue(Object object, int index) {

            return (String) object;
          }

        });
    strategiesChoice.setRequired(true);
    this.add(strategiesChoice);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del conjunto de estrategias de asignación.
    final FeedbackLabel strategiesChoiceFeedbackLabel = new FeedbackLabel(
        "itemAssignmentStrategyEPFFeedback", strategiesChoice);
    strategiesChoiceFeedbackLabel.setOutputMarkupId(true);
    this.add(strategiesChoiceFeedbackLabel);

    this.setSelectedAssignmentStrategyEPF(aProjectDTO.getItemAssignmentStrategy());
  }

  /**
   * Getter.
   * 
   * @return la estrategia de asignación de responsables de los ítems.
   */
  public String getSelectedAssignmentStrategyEPF() {
    return this.selectedAssignmentStrategyEPF;
  }

  /**
   * Setter.
   * 
   * @param anStrategyName
   *          es la estrategia de asignación de responsables de los ítems.
   */
  public void setSelectedAssignmentStrategyEPF(String anStrategyName) {
    this.selectedAssignmentStrategyEPF = anStrategyName;
  }

}
