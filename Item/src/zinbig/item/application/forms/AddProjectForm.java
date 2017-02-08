/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.forms.behaviors.FormComponentAjaxBehavior;
import zinbig.item.application.forms.behaviors.FormWithAjaxUpdatableComponent;
import zinbig.item.application.pages.AddProjectPage;
import zinbig.item.application.pages.ErrorPage;
import zinbig.item.application.pages.ProjectsAdministrationPage;
import zinbig.item.model.exceptions.ProjectNameNotUniqueException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

import com.apress.wicketbook.ajax.AjaxIndicator;

/**
 * Las instancias de esta clase se utilizan para registrar un nuevo proyecto en la aplicación. <br>
 * El alta de un proyecto se realiza ingresando un nombre (que no puede repetirse, por lo que se controla con
 * una llamada AJAX), un nombre corto, un link para la página del proyecto (opcional) y si es público o
 * privado este proyecto (si es público aparecerá en el menú de lo usuarios que no se hayan registrado).
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class AddProjectForm extends ItemForm implements FormWithAjaxUpdatableComponent {

  /**
   * UID por defecto para la serialización.
   */
  private static final long serialVersionUID = -2563887304770021398L;

  /**
   * Es un campo que almacena el nombre provisto para el nuevo proyecto.
   */
  public String nameAPF;

  /**
   * Es un campo que almacena el nombre del líder de proyecto.
   */
  public String leaderAPF;

  /**
   * Es el nombre corto para el nuevo proyecto.
   */
  public String shortnameAPF;

  /**
   * Mantiene el link asociado al proyecto.
   */
  public String linkAPF;

  /**
   * Mantiene el dto del conjunto de prioridades seleccionado.
   */
  public PrioritySetDTO prioritySetAPF;

  /**
   * Indica si el proyecto que se está agregando es público o no.
   */
  public boolean publicProjectAPF;

  /**
   * Mantiene la selección de qué estrategia de asignación de responsable de los nuevos ítems se ha
   * seleccionado.
   */
  public String selectedAssignmentStrategyAPF;

  /**
   * Es el dto que representa al usuario seleccionado como líder del nuevo proyecto.
   */
  public UserDTO selectedProjectLeader;

  /**
   * Constructor.
   * 
   * @param id
   *          es el identificador de este formulario dentro del panel.
   */
  @SuppressWarnings("unchecked")
  public AddProjectForm(String id) {
    super(id);

    // construye el input field para el nombre y su correspondiente error.
    this.createComponentForName();

    // construye el componente para el nombre corto del proyecto y su
    // correspondiente label de error.
    this.createComponentForShortName();

    // construye el input field para el link dle proyecto
    this.createComponentForLink();

    // construye el combo para mostrar los usuarios para que se puede
    // seleccionar el líder del proyecto
    this.createComponentForLeader();

    // construye el combo para mostrar los conjuntos de prioridades
    this.createComponentForPriorities();

    // construye el combo para mostrar las diferentes estrategias para
    // asignar un responsable a los ítems recién creados.
    this.createComponentForAssignmentStrategies();

    // construye el chekbox para definir si es un proyecto público o no
    final CheckBox checkBox = new CheckBox("publicProject", new PropertyModel(this, "publicProjectAPF"));
    this.add(checkBox);

    // construye el link de envío del formulario
    this.createComponentForSubmitLink();

    // construye el link de cancelación del formulario
    Link cancelLink = new BookmarkablePageLink("cancelLink", ProjectsAdministrationPage.class);
    this.add(cancelLink);

    // agrega las listas para seleccionar los tipos de ítems
    List<ItemTypeDTO> itemTypes = this.constructItemTypesPaletteAvailableElements();
    IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("toString", "oid");

    final Palette itemTypesPalette = new Palette("itemTypesPalette", new Model(
        (Serializable) new ArrayList<ItemTypeDTO>()), new Model((Serializable) itemTypes), renderer, 6, true);
    this.add(itemTypesPalette);

  }

  /**
   * Crea el componente para enviar el formulario con los datos cargados.
   */
  private void createComponentForSubmitLink() {
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

          AddProjectForm.this.getProjectsService().addProject(getNameAPF(), getShortnameAPF(), getLinkAPF(),
              isPublicProjectAPF(), getPrioritySetAPF(), getSelectedAssignmentStrategyAPF(), getLeaderAPF(),
              getItemTypesPalette().getSelectedChoices(),
              ((ItemApplication) AddProjectForm.this.getApplication()).getPathForProject(getShortnameAPF()));

          this.setResponsePage(AddProjectPage.class);
        } catch (ProjectNameNotUniqueException nuue) {

          nuue.printStackTrace();
          AddProjectForm.this.get("nameAPF").error(getString("ProjectNameNotUniqueException"));
        } catch (UserUnknownException uue) {

          uue.printStackTrace();
          AddProjectForm.this.get("leaderAPF").error(getString("UsernameUnknownException"));
        } catch (Exception e) {

          setResponsePage(ErrorPage.class);
          e.printStackTrace();
        }
      }

    };
    this.add(submitLink);
  }

  /**
   * Crea el componente para poder seleccionar la estrategia de asignación de responsables de los nuevos
   * ítems.
   */
  @SuppressWarnings("unchecked")
  private void createComponentForAssignmentStrategies() {
    Collection<String> strategies = new ArrayList<String>();
    try {
      strategies = ((ItemApplication) this.getApplication()).getItemAssignmentStrategies();
    } catch (Exception e) {
      e.printStackTrace();
    }

    DropDownChoice strategiesChoice = new DropDownChoice("itemAssignmentStrategyAPF",
        new PropertyModel<String>(this, "selectedAssignmentStrategyAPF"), (List<String>) strategies,
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
          public String getDisplayValue(Object object) {

            String aString = object.toString();

            return AddProjectForm.this.getString(aString);
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

            return object.toString();
          }

        });
    strategiesChoice.setRequired(true);
    this.add(strategiesChoice);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del conjunto de estrategias de asignación.
    final FeedbackLabel strategiesChoiceFeedbackLabel = new FeedbackLabel(
        "itemAssignmentStrategyAPFFeedback", strategiesChoice);
    strategiesChoiceFeedbackLabel.setOutputMarkupId(true);
    this.add(strategiesChoiceFeedbackLabel);
  }

  /**
   * Crea el componente para selecciona el conjunto de prioridades del nuevo proyecto.
   */
  @SuppressWarnings("unchecked")
  private void createComponentForPriorities() {
    Collection<PrioritySetDTO> prioritySets = new ArrayList<PrioritySetDTO>();
    try {
      prioritySets.addAll(this.getPrioritiesService().getAllPrioritySets());
    } catch (Exception e) {
      e.printStackTrace();
    }

    DropDownChoice prioritySetChoice = new DropDownChoice("prioritySetAPF", new PropertyModel<String>(this,
        "prioritySetAPF"), (List<PrioritySetDTO>) prioritySets, new IChoiceRenderer() {

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
      public String getDisplayValue(Object object) {

        PrioritySetDTO aDto = (PrioritySetDTO) object;

        return aDto.getName();
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

        PrioritySetDTO aDto = (PrioritySetDTO) object;

        return aDto.getOid();
      }
    });
    prioritySetChoice.setRequired(true);
    this.add(prioritySetChoice);

    // recupera el conjunto de prioridades por defecto para mostrarlo como
    // seleccionado
    PrioritySetDTO aDto;
    try {
      aDto = this.getPrioritiesService().getDefaultPrioritySet();
      this.setPrioritySetAPF(aDto);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del conjunto de prioridades del proyecto.
    final FeedbackLabel prioritiesChoiceFeedbackLabel = new FeedbackLabel("prioritySetAPFFeedback",
        prioritySetChoice);
    prioritiesChoiceFeedbackLabel.setOutputMarkupId(true);
    this.add(prioritiesChoiceFeedbackLabel);
  }

  /**
   * Crea el componente para seleccionar el usuario que será el líder de este proyecto.
   */
  @SuppressWarnings("unchecked")
  private void createComponentForLeader() {

    final AutoCompleteTextField<String> leaderField = new AutoCompleteTextField<String>("leaderAPF",
        new PropertyModel<String>(this, "leaderAPF")) {

      @Override
      protected Iterator<String> getChoices(String input) {
        List<String> choices = Collections.emptyList();
        if (!Strings.isEmpty(input)) {

          try {
            Collection<UserDTO> users = AddProjectForm.this.getUsersService().findUsersWithUsernameLike(
                input, 10);
            if (!users.isEmpty()) {
              choices = new ArrayList<String>(10);
              for (UserDTO anUser : users) {
                choices.add(anUser.getUsername());
              }
            }
          } catch (Exception e) {

          }
        }
        return choices.iterator();
      }

    };
    leaderField.setRequired(true);
    leaderField.add(new ItemSimpleStringValidator(leaderField));
    leaderField.add(new FormComponentAjaxBehavior(this, "leaderAPFFeedback"));
    this.add(leaderField);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del líder del proyecto.
    final FeedbackLabel leaderFeedbackLabel = new FeedbackLabel("leaderAPFFeedback", leaderField);
    leaderFeedbackLabel.setOutputMarkupId(true);
    this.add(leaderFeedbackLabel);

    // // construye la imagen indicadora para Ajax
    // final AjaxIndicator imageContainer = new AjaxIndicator("indicatorImgL");
    // imageContainer.setOutputMarkupId(true);
    // this.add(imageContainer);

    //
    // Collection<UserDTO> users = new ArrayList<UserDTO>();
    // try {
    // users.addAll(this.getUsersService().getAllUsers());
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // DropDownChoice usersChoice = new DropDownChoice("leaderAPF",
    // new PropertyModel<String>(this, "selectedProjectLeader"),
    // (List<UserDTO>) users, new IChoiceRenderer() {
    //
    // /**
    // * UID por defecto.
    // */
    // private static final long serialVersionUID = 1L;
    //
    // /**
    // * Getter.
    // *
    // * @param anObject
    // * es el objeto que se está representando.
    // * @return la representación como String que se debe mostrar
    // * para este objeto.
    // */
    // public String getDisplayValue(Object anObject) {
    //
    // UserDTO aDto = (UserDTO) anObject;
    //
    // return aDto.getSurname() + ", " + aDto.getName();
    // }
    //
    // /**
    // * Getter.
    // *
    // * @param anObject
    // * es el objeto que se está representando.
    // * @param index
    // * es la posición dentro de la lista del objeto.
    // * @return el valor que se debe asignar cuando este objeto
    // * es seleccionado.
    // */
    // public String getIdValue(Object anObject, int index) {
    //
    // UserDTO aDto = (UserDTO) anObject;
    //
    // return aDto.getOid();
    // }
    // });
    // usersChoice.setRequired(true);
    // this.add(usersChoice);
    //
    // // construye el label para mostrar los mensajes de error relacionados
    // // con el componente del conjunto de usuarios.
    // final FeedbackLabel usersChoiceFeedbackLabel = new FeedbackLabel(
    // "leaderAPFFeedback", usersChoice);
    // usersChoiceFeedbackLabel.setOutputMarkupId(true);
    // this.add(usersChoiceFeedbackLabel);
  }

  /**
   * Crea el componente para cargar el link del proyecto.
   */
  private void createComponentForLink() {
    final TextField<String> linkField = new TextField<String>("linkAPF", new PropertyModel<String>(this,
        "linkAPF"));
    linkField.add(new UrlValidator());
    linkField.add(StringValidator.maximumLength(new Integer(this
        .getSystemProperty("AddProjectForm.link.length"))));
    this.add(linkField);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del link del proyecto.
    final FeedbackLabel linkFeedbackLabel = new FeedbackLabel("linkAPFFeedback", linkField);
    linkFeedbackLabel.setOutputMarkupId(true);
    this.add(linkFeedbackLabel);
  }

  /**
   * Crea el componente para dar de alta el nombre corto del proyecto y su correspondiente label para mostrar
   * errores.
   */
  private void createComponentForShortName() {
    // construye el input field para el nombre corto
    final TextField<String> shortnameField = new TextField<String>("shortnameAPF", new PropertyModel<String>(
        this, "shortnameAPF"));
    shortnameField.setRequired(true);
    shortnameField.add(new ItemSimpleStringValidator(shortnameField));
    shortnameField.add(StringValidator.exactLength(new Integer(this
        .getSystemProperty("AddProjectForm.shortname.length"))));
    this.add(shortnameField);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del nombre corto del proyecto.
    final FeedbackLabel shortnameFeedbackLabel = new FeedbackLabel("shortnameAPFFeedback", shortnameField);
    shortnameFeedbackLabel.setOutputMarkupId(true);
    this.add(shortnameFeedbackLabel);
  }

  /**
   * Crea el componente para la carga del nombre lde proyecto.
   */
  private void createComponentForName() {
    final TextField<String> nameField = new TextField<String>("nameAPF", new PropertyModel<String>(this,
        "nameAPF"));
    nameField.setRequired(true);
    nameField.add(new ItemSimpleStringValidator(nameField));
    nameField.add(new FormComponentAjaxBehavior(this, "nameAPFFeedback"));
    nameField.add(StringValidator.maximumLength(new Integer(this
        .getSystemProperty("AddProjectForm.name.length"))));
    this.add(nameField);

    // construye el label para mostrar los mensajes de error relacionados
    // con el componente del nombre del proyecto.
    final FeedbackLabel nameFeedbackLabel = new FeedbackLabel("nameAPFFeedback", nameField);
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
  public String getNameAPF() {
    return this.nameAPF;
  }

  /**
   * Setter.
   * 
   * @param aName
   *          es el nombre del proyecto.
   */
  public void setNameAPF(String aName) {
    this.nameAPF = aName;
  }

  /**
   * Getter.
   * 
   * @return es el nombre corto del nuevo proyecto.
   */
  public String getShortnameAPF() {
    return this.shortnameAPF;
  }

  /**
   * Setter.
   * 
   * @param aName
   *          es el nombre corto del proyecto.
   */
  public void setShortnameAPF(String aName) {
    this.shortnameAPF = aName;
  }

  /**
   * Getter.
   * 
   * @return el link asociado al proyecto.
   */
  public String getLinkAPF() {
    return this.linkAPF;
  }

  /**
   * Setter.
   * 
   * @param aLink
   *          es el link asociado al proyecto.
   */
  public void setLinkAPF(String aLink) {
    this.linkAPF = aLink;
  }

  /**
   * Este método se dispara a raíz de la actualización del campo name por parte de un evento AJAX.<br>
   * Este método invoca al servicio de proyectos para verificar que no exista un proyecto con el nombre
   * ingresado. En caso de que exista ya un proyecto se muestra un cartel de error.
   */
  public void ajaxComponentUpdated() {

    try {
      if (this.getProjectsService().existsProjectWithName(this.getNameAPF())) {

        this.get("nameAPF").error(getString("ProjectNameNotUniqueException"));

      }
    } catch (Exception e) {
      // no se hace nada en caso de error. Si existe un proyecto con el
      // mismo nombre el servicio de alta de proyectos detectará la
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
  public boolean isPublicProjectAPF() {
    return this.publicProjectAPF;
  }

  /**
   * Setter.
   * 
   * @param aBoolean
   *          es una indicación si el proyecto nuevo es público o no.
   */
  public void setPublicProjectAPF(boolean aBoolean) {
    this.publicProjectAPF = aBoolean;
  }

  /**
   * Getter.
   * 
   * @return el nombre del conjunto de prioridades seleccionado.
   */
  public PrioritySetDTO getPrioritySetAPF() {
    return this.prioritySetAPF;
  }

  /**
   * Setter.
   * 
   * @param aPrioritySetDto
   *          es el dto que representa al nivel de prioridad seleccionado.
   */
  public void setPrioritySetAPF(PrioritySetDTO aPrioritySetDto) {
    this.prioritySetAPF = aPrioritySetDto;
  }

  /**
   * Getter.
   * 
   * @return el nombre de la clase de la estrategia de asignación de responsables de los ítems.
   */
  public String getSelectedAssignmentStrategyAPF() {
    return this.selectedAssignmentStrategyAPF;
  }

  /**
   * Setter.
   * 
   * @param aClassName
   *          es el nombre de la clase de la estrategia de asignación de responsables de los ítems.
   */
  public void setSelectedAssignmentStrategyAPF(String aClassName) {
    this.selectedAssignmentStrategyAPF = aClassName;
  }

  /**
   * Getter.
   * 
   * @return el dto del usuario seleccionado como líder.
   */
  public UserDTO getSelectedProjectLeader() {
    return this.selectedProjectLeader;
  }

  /**
   * Setter.
   * 
   * @param anUserDTO
   *          es el dto del usuario seleccionado como líder.
   */
  public void setSelectedProjectLeader(UserDTO anUserDTO) {
    this.selectedProjectLeader = anUserDTO;
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
   * Recupera todos los tipos de ítems para poder armar el componente que muestra los tipos de ítems
   * disponibles.
   * 
   * @return una colección que contiene los dtos de los tipos de ítems.
   */
  private List<ItemTypeDTO> constructItemTypesPaletteAvailableElements() {

    ItemsServiceBI itemsService = this.getItemsService();
    List<ItemTypeDTO> itemTypes = new ArrayList<ItemTypeDTO>();

    try {

      itemTypes.addAll(itemsService.getAllItemTypes());

    } catch (Exception e) {

    }

    return itemTypes;
  }

  /**
   * Getter.
   * 
   * @return el componente que se utiliza para presentar una lista con todos los tipos de ítems disponibles.
   */
  @SuppressWarnings("unchecked")
  private Palette<ItemTypeDTO> getItemTypesPalette() {
    return (Palette<ItemTypeDTO>) this.get("itemTypesPalette");
  }

  /**
   * Getter.
   * 
   * @return el nombre del líder de proyecto seleccionado.
   */
  public String getLeaderAPF() {
    return this.leaderAPF;
  }

  /**
   * Setter.
   * 
   * @param anUsername
   *          es el nombre del líder del proyecto.
   */
  public void setLeaderAPF(String anUsername) {
    this.leaderAPF = anUsername;
  }

}
