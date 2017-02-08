/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.PropertyPanelContainer;
import zinbig.item.application.pages.AddItemPage;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.application.pages.ViewItemsPage;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PriorityDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.ProjectDTOComparator;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase se utilizan para dar de alta nuevo ítems.<br>
 * Para crear un nuevo ítem es necesario elegir el proyecto en el cual se creará
 * el ítem, pudiendo elegir entre todos los proyectos públicos y/o los proyectos
 * privados en los que esté asignado el usuario.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AddItemForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 790940473175772351L;

	/**
	 * Es el título del nuevo ítem.
	 */
	protected String titleAIF;

	/**
	 * Es la descripción del nuevo ítem.
	 */
	protected String descriptionAIF;

	/**
	 * Es el estado del nuevo ítem.
	 */
	protected ItemStateEnum selectedState;

	/**
	 * Mantiene el dto del proyecto seleccionado por el usuario.
	 */
	protected ProjectDTO selectedProject;

	/**
	 * Mantiene la selección del candidato a responsable del nuevo ítem.
	 */
	protected AbstractUserDTO selectedResponsible;

	/**
	 * Mantiene el dto de la prioridad seleccionada por el usuario.
	 */
	protected PriorityDTO selectedPriority;

	/**
	 * Mantiene el dto del tipo de ítem seleccionado por el usuario.
	 */
	protected ItemTypeDTO selectedItemType;

	/**
	 * Este diccionario contiene para cada dto de un proyecto la lista de dtos
	 * de las prioridades.
	 */
	protected Map<ProjectDTO, Collection<PriorityDTO>> prioritiesMap;

	/**
	 * Este diccionario contiene para cada dto de un proyecto la lista de dtos
	 * de sus tipos de ítems.
	 */
	protected Map<ProjectDTO, Collection<ItemTypeDTO>> itemTypesMap;

	/**
	 * Es una colección que contiene los identificadores de los ítems que se
	 * desea unificar. Si esta colección no está vacía entonces al agregar un
	 * ítem nuevo todos los ítems de esta colección se cerrarán automáticamente.
	 */
	protected Collection<String> itemsToAggregate;

	/**
	 * Es un componente para adjuntar archivos.
	 */
	protected FileUploadField attachedFile;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aPage
	 *            es la página a la cual se ha agregado este formulario.
	 * @param anItemId
	 *            es el identificador del último ítem agregado. En caso de que
	 *            exista este parámetro se muestra un cartel con esta
	 *            información al usuario.
	 * @param params
	 *            es un objeto que contiene los parámetros pasados a la página
	 *            en donde se agregó este formulario.
	 */
	@SuppressWarnings("unchecked")
	public AddItemForm(String anId, AddItemPage aPage, String anItemId,
			PageParameters params) {
		super(anId);

		this.setItemsToAggregate(new ArrayList<String>());

		if (params.containsKey("AGGREGATE_ITEMS")) {
			String oids = params.getString("AGGREGATE_ITEMS");
			StringTokenizer tokenizer = new StringTokenizer(oids, ";");
			while (tokenizer.hasMoreTokens()) {
				this.getItemsToAggregate().add(tokenizer.nextToken());
			}

		}

		// crea el diccionario para guardar las prioridades de cada proyecto
		this
				.setPrioritiesMap(new HashMap<ProjectDTO, Collection<PriorityDTO>>());

		// crea el diccionario para guardar los tipos de ítems de cada proyecto
		this
				.setItemTypesMap(new HashMap<ProjectDTO, Collection<ItemTypeDTO>>());

		// recupera el dto del usuario en la sesión
		UserDTO anUserDTO = this.getUserDTO();

		// si hay un proyecto en la sesión lo pone como seleccionado en la lista
		// de proyectos

		ProjectDTO aProjectDTO = this.getProjectDTO();
		if (aProjectDTO != null) {
			this.setSelectedProject(aProjectDTO);
		}

		// recupera toda la información de los proyectos
		this.loadProjectsInformation(anUserDTO);

		// crea el componente para listar los proyectos
		DropDownChoice projectsList = this.createProjectsListComponent();
		this.add(projectsList);

		// agrega el componente para mostrar el feedback del proyecto al usuario
		// construye el componente para dar feedback al usuario
		final FeedbackLabel projectFeedbackLabel = new FeedbackLabel(
				"projectAIFFeedback", projectsList);
		projectFeedbackLabel.setOutputMarkupId(true);
		this.add(projectFeedbackLabel);

		// construye el input field para el título
		final TextField<String> titleField = this.createTitleFieldComponent();
		this.add(titleField);

		// construye el componente para dar feedback al usuario respecto del
		// título del ítem.
		final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
				"titleAIFFeedback", titleField);
		titleFeedbackLabel.setOutputMarkupId(true);
		this.add(titleFeedbackLabel);

		// construye el componente para la descripción
		final TextArea<String> descriptionTextArea = this
				.createDescriptionComponent();
		this.add(descriptionTextArea);

		// construye el componente para dar feedback al usuario respecto de la
		// descripcion del ítem.
		final FeedbackLabel descriptionFeedbackLabel = new FeedbackLabel(
				"descriptionAIFFeedback", descriptionTextArea);
		descriptionFeedbackLabel.setOutputMarkupId(true);
		this.add(descriptionFeedbackLabel);

		// construye el componente que permite mostrar una lista con todas las
		// prioridades correspondientes al proyecto seleccionado.
		DropDownChoice prioritiesList = this.createPrioritiesListComponent();
		this.add(prioritiesList);

		// construye el componente para dar feedback al usuario respecto de la
		// lista de prioridades
		final FeedbackLabel priorityFeedbackLabel = new FeedbackLabel(
				"priorityAIFFeedback", prioritiesList);
		priorityFeedbackLabel.setOutputMarkupId(true);
		this.add(priorityFeedbackLabel);

		// construye el componente que permite mostrar una lista con todos los
		// tipos de ítems del proyecto seleccionado.
		DropDownChoice itemTypesList = this.createItemTypesListComponent();
		this.add(itemTypesList);

		// construye el componente para dar feedback al usuario respecto de la
		// lista de tipos de ítems
		final FeedbackLabel itemTypeFeedbackLabel = new FeedbackLabel(
				"itemTypeAIFFeedback", itemTypesList);
		itemTypeFeedbackLabel.setOutputMarkupId(true);
		this.add(itemTypeFeedbackLabel);

		// construye el componente que permite mostrar una lista con todas las
		// alternativas para los estados del nuevo ítem.
		DropDownChoice statesList = this.createStatesListComponent();
		this.add(statesList);

		// construye el componente para dar feedback al usuario respecto de la
		// lista de estados posibles para el item
		final FeedbackLabel statesFeedbackLabel = new FeedbackLabel(
				"stateAIFFeedback", statesList);
		statesFeedbackLabel.setOutputMarkupId(true);
		this.add(statesFeedbackLabel);

		// construye el componente que permite mostrar una lista con todas los
		// usuarios candidatos a ser responsables del ítem
		DropDownChoice responsiblesList = this
				.createResponsiblesListComponent();
		this.add(responsiblesList);

		// crea los componentes requeridos para las propiedades adicionales
		Component aPanel = this.createAdditionalPropertyPanel();
		this.add(aPanel);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				ItemsServiceBI aService = AddItemForm.this.getItemsService();
				Map<String, String> additionalProperties = AddItemForm.this
						.gatherAdditionalProperties();
				try {

					ItemDTO newItem = aService.addItem(AddItemForm.this
							.getSelectedProject(), Utils
							.encodeString(AddItemForm.this.getTitleAIF()),
							Utils.encodeString(AddItemForm.this
									.getDescriptionAIF()), new Date(),
							AddItemForm.this.getUserDTO(), AddItemForm.this
									.getSelectedPriority(), AddItemForm.this
									.getSelectedState(), AddItemForm.this
									.getSelectedResponsible(), AddItemForm.this
									.getSelectedItemType(), AddItemForm.this
									.getItemsToAggregate(), AddItemForm.this
									.getString("aggregationComment"),
							additionalProperties);

					AddItemForm.this.saveAttachedFiles(newItem);

					PageParameters params = new PageParameters();
					params.put("NEW_ITEM", newItem.getId());
					this.setResponsePage(AddItemPage.class, params);
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		};
		this.add(submitLink);

		// construye el link de cancelación del formulario. Si el usuario tiene
		// permiso de ver todos los permisos se retorna a la página de listado
		// de ítems, sino no se va al dashboard.
		Class aPageClass = this
				.verifyPermissionAssigmentToUser("VIEW_ALL_ITEMS") ? ViewItemsPage.class
				: DashboardPage.class;

		Link cancelLink = new BookmarkablePageLink("cancelLink", aPageClass);
		this.add(cancelLink);

		// crea el componente para subir archivos.
		this.setMultiPart(true);
		FileUploadField upload = new FileUploadField("attachedFile");
		this.setAttachedFile(upload);
		this.add(upload);

	}

	/**
	 * Obtiene todos los valores adicionales cargados por el usuario en cada uno
	 * de los paneles.
	 * 
	 * @return un diccionario que contiene los valores ingresados para cada una
	 *         de las propiedades adicionales.
	 */
	protected Map<String, String> gatherAdditionalProperties() {
		PropertyPanelContainer aPanel = (PropertyPanelContainer) this
				.get("propertyPanels");

		return aPanel.gatherAdditionalProperties();
	}

	/**
	 * Crea un componente que lista todos los paneles que permiten al usuario
	 * ingresar los valores de las propiedades adicionales definidas para un
	 * nuevo ítem según el proyecto seleccionado.
	 * 
	 */
	protected Component createAdditionalPropertyPanel() {

		PropertyPanelContainer aPanel = new PropertyPanelContainer(
				"propertyPanels", this.getSelectedProject(), this);
		aPanel.setOutputMarkupId(true);

		return aPanel;

	}

	/**
	 * Guarda los archivos adjuntos que se subieron con el ítem recientemente
	 * creado.
	 * 
	 * @param anItem
	 *            es el ítem recientemente creado.
	 */
	protected void saveAttachedFiles(ItemDTO anItemDTO) {

		String aPath = ((ItemApplication) AddItemForm.this.getApplication())
				.getPathForItem(anItemDTO);
		if (this.getAttachedFile().getFileUpload() != null) {
			FileUpload upload = this.getAttachedFile().getFileUpload();

			// crea el archivo nuevo
			if (new File(aPath).mkdirs()) {
				File newFile = new File(aPath, upload.getClientFileName());
				try {
					// guarda el archivo.
					newFile.createNewFile();
					upload.writeTo(newFile);

					this.getItemsService().attachFileToItem(anItemDTO,
							upload.getClientFileName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Crea el componente que permite seleccionar el posible responsable del
	 * nuevo ítem. <br>
	 * 
	 * @return la lista de usuarios.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice createResponsiblesListComponent() {

		// crea un modelo para acceder a la lista de usuarios
		IModel<List<AbstractUserDTO>> responsiblesChoices = new AbstractReadOnlyModel<List<AbstractUserDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de usuarios del proyecto.
			 * 
			 * @return una lista de posibles responsables para el nuevo ítem.
			 * 
			 */
			@Override
			public List<AbstractUserDTO> getObject() {
				List<AbstractUserDTO> users = new ArrayList<AbstractUserDTO>();
				ProjectsServiceBI service = AddItemForm.this
						.getProjectsService();

				if (AddItemForm.this.getSelectedProject() != null) {
					try {
						users.addAll(service
								.getUserGroupsOfProject(AddItemForm.this
										.getSelectedProject()));
						users.addAll(service.getUsersOfProject(AddItemForm.this
								.getSelectedProject()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return users;
			}

		};

		// construye la lista para mostrar todos los usuarios que serán
		// candidatos a ser responsables del ítem.

		DropDownChoice responsiblesList = new DropDownChoice("responsible",
				new PropertyModel(this, "selectedResponsible"),
				responsiblesChoices, new ChoiceRenderer<AbstractUserDTO>() {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Getter.
					 * 
					 * @param state
					 *            es el objeto al cual se debe representar.
					 * @return una representación visual del objeto recibido.
					 * 
					 */
					@Override
					public Object getDisplayValue(AbstractUserDTO aDTO) {

						return aDTO.toString();
					}

				});
		responsiblesList.setEnabled(false);
		responsiblesList.setOutputMarkupId(true);
		responsiblesList.setRequired(true);

		// construye el componente para dar feedback al usuario respecto de la
		// lista de estados posibles para el item
		final FeedbackLabel responsiblesFeedbackLabel = new FeedbackLabel(
				"responsibleAIFFeedback", responsiblesList);
		responsiblesFeedbackLabel.setOutputMarkupId(true);
		this.add(responsiblesFeedbackLabel);

		return responsiblesList;

	}

	/**
	 * Crea el componente que permite cargar la descripcion del nuevo ítem.<br>
	 * Este componente tiene asociados validadores para no permitir la carga de
	 * información que contenga caracteres inválidos.
	 * 
	 * @return el componente recientemente creado.
	 */
	private TextArea<String> createDescriptionComponent() {
		final TextArea<String> textArea = new TextArea<String>(
				"descriptionAIF", new PropertyModel<String>(this,
						"descriptionAIF"));
		textArea.setRequired(true);
		textArea.setOutputMarkupId(true);
		textArea.add(new ItemSimpleStringValidator(textArea));
		textArea.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddItemForm.description.length"))));

		return textArea;
	}

	/**
	 * Crea el componente que permite seleccionar un estado para el nuevo ítem.<br>
	 * El conjunto de estados (enum) se obtiene a partir de la invocación del
	 * servicio de ítems.
	 * 
	 * @return el componente para listar los estados posibles para el nuevo
	 *         ítem.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice createStatesListComponent() {

		// crea un modelo para acceder a la lista de estados que debe
		// mostrarse a través del dropdownchoice de estados.
		IModel<List<ItemStateEnum>> statesChoices = new AbstractReadOnlyModel<List<ItemStateEnum>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de estados.
			 * 
			 * @return una lista estados posibles para el nuevo ítem.
			 * 
			 */
			@Override
			public List<ItemStateEnum> getObject() {
				TreeSet<ItemStateEnum> aux = new TreeSet<ItemStateEnum>(
						new Comparator<ItemStateEnum>() {

							@Override
							public int compare(ItemStateEnum arg0,
									ItemStateEnum arg1) {
								return getString(arg0.toString()).compareTo(
										getString(arg1.toString()));
							}
						});
				List<ItemStateEnum> states = new ArrayList<ItemStateEnum>();
				aux
						.addAll(((ItemApplication) AddItemForm.this
								.getApplication())
								.getAvailableStatesForNewItem());

				states.addAll(aux);
				return states;
			}

		};

		// construye la lista para mostrar todas las prioridades
		// correspondientes al proyecto seleccionado.

		DropDownChoice statesList = new DropDownChoice("state",
				new PropertyModel(this, "selectedState"), statesChoices,
				new ChoiceRenderer<ItemStateEnum>() {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Getter.
					 * 
					 * @param state
					 *            es el objeto al cual se debe representar.
					 * @return una representación visual del objeto recibido, en
					 *         este caso el nombre internacionalizado del
					 *         estado.
					 * 
					 */
					@Override
					public Object getDisplayValue(ItemStateEnum state) {

						return AddItemForm.this.getString(state.name());
					}

				});

		// agrega un behavior para refrescar la lista de responsables
		// dependiendo de la estrategia de asignación de responsable del
		// proyecto y del estado seleccionado.
		statesList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Manejador del evento de actualización de la lista de estados. Se
			 * actualiza la lista de la visibilidad de la lista de responsables.
			 * 
			 * @param target
			 *            es el objetivo del pedido AJAX.
			 */
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				try {

					AddItemForm.this.getResponsiblesList().setEnabled(
							AddItemForm.this.getResponsiblesListEnabledState());

					target.addComponent(AddItemForm.this.getResponsiblesList());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		statesList.setOutputMarkupId(true);
		statesList.setRequired(true);

		return statesList;
	}

	/**
	 * Crea el componente que permite seleccionar una prioridad para el nuevo
	 * ítem.<br>
	 * El conjunto de prioridades se obtiene a través de un modelo que accede al
	 * diccionario de prioridades teniendo en cuenta el proyecto seleccionado.<br>
	 * La actualización de este componente se realiza mediante ajax acada vez
	 * que se hace una selección en la lista de proyectos.
	 * 
	 * @return el componente para listar las prioridades.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice createPrioritiesListComponent() {

		// crea un modelo para acceder a la lista de prioridades que debe
		// mostrarse a través del dropdownchoice de prioridades.
		IModel<List<PriorityDTO>> prioritiesChoices = new AbstractReadOnlyModel<List<PriorityDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de prioridades que contiene este
			 * modelo.
			 * 
			 * @return una lista de dtos de las prioridades almacenadas bajo la
			 *         clave correspondiente al proyecto seleccionado.
			 * 
			 */
			@Override
			public List<PriorityDTO> getObject() {
				List<PriorityDTO> priorities = new ArrayList<PriorityDTO>();
				Map aMap = AddItemForm.this.getPrioritiesMap();
				ProjectDTO aProjectDTO = AddItemForm.this.getSelectedProject();

				if (aMap.containsKey(aProjectDTO)) {
					priorities.addAll((List) aMap.get(aProjectDTO));
				}

				return priorities;
			}

		};

		// construye la lista para mostrar todas las prioridades
		// correspondientes al proyecto seleccionado.
		IChoiceRenderer priorityRenderer = new ChoiceRenderer("completeName",
				"oid");
		DropDownChoice prioritiesList = new DropDownChoice("priority",
				new PropertyModel(this, "selectedPriority"), prioritiesChoices,
				priorityRenderer);

		prioritiesList.setOutputMarkupId(true);
		prioritiesList.setRequired(true);

		return prioritiesList;
	}

	/**
	 * Crea el componente que permite seleccionar un tipo de ítem para el nuevo
	 * ítem.<br>
	 * El conjunto de tipos de ítems se obtiene a través de un modelo que accede
	 * al diccionario de tipos teniendo en cuenta el proyecto seleccionado.<br>
	 * La actualización de este componente se realiza mediante ajax acada vez
	 * que se hace una selección en la lista de proyectos.
	 * 
	 * @return el componente para listar los tipos de ítems.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice createItemTypesListComponent() {

		// crea un modelo para acceder a la lista de tipos de ítems.
		IModel<List<ItemTypeDTO>> itemTypeChoices = new AbstractReadOnlyModel<List<ItemTypeDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de tipos de ítems que contiene este
			 * modelo.
			 * 
			 * @return una lista de dtos de los tipos de ítems almacenados bajo
			 *         la clave correspondiente al proyecto seleccionado.
			 * 
			 */
			@Override
			public List<ItemTypeDTO> getObject() {
				List<ItemTypeDTO> itemTypes = new ArrayList<ItemTypeDTO>();
				Map aMap = AddItemForm.this.getItemTypesMap();
				ProjectDTO aProjectDTO = AddItemForm.this.getSelectedProject();

				if (aMap.containsKey(aProjectDTO)) {
					itemTypes.addAll((List) aMap.get(aProjectDTO));
				}

				return itemTypes;
			}

		};

		// construye la lista para mostrar todas los tipos de ítems
		// correspondientes al proyecto seleccionado.
		IChoiceRenderer itemTypeRenderer = new ChoiceRenderer("title", "oid");
		DropDownChoice itemTypesList = new DropDownChoice("itemType",
				new PropertyModel(this, "selectedItemType"), itemTypeChoices,
				itemTypeRenderer);

		itemTypesList.setOutputMarkupId(true);
		itemTypesList.setRequired(true);

		return itemTypesList;
	}

	/**
	 * Crea el componente que permite cargar el título del nuevo ítem.
	 * 
	 * @return un input field para cargar el título, con todos los behaviors
	 *         necesarios para que no se pueda dar de alta un título vacío, o
	 *         mayor que una longitud dada o con caracteres inválidos.
	 */
	private TextField<String> createTitleFieldComponent() {
		final TextField<String> titleField = new TextField<String>("titleAIF",
				new PropertyModel<String>(this, "titleAIF"));
		titleField.setRequired(true);
		titleField.setOutputMarkupId(true);
		titleField.add(new ItemSimpleStringValidator(titleField));
		titleField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddItemForm.title.length"))));

		return titleField;
	}

	/**
	 * Crea el componente para listar todos los proyectos en los cuales el
	 * usuario puede dar de alta un nuevo ítem.<br>
	 * Además de crear este componente se crean los behaviors de Ajax para
	 * actualizar las demás listas en base a la selección realizada por el
	 * usuario.
	 * 
	 * @return el componente creado para mostrar los proyectos.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<ProjectDTO> createProjectsListComponent() {

		// crea un modelo para acceder a la lista de proyectos que debe
		// mostrarse a través del dropdownchoice de proyectos.
		IModel<List<ProjectDTO>> projectChoices = new AbstractReadOnlyModel<List<ProjectDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de proyectos que contiene este modelo.
			 * 
			 * @return una lista de dtos de los proyectos almacenados como
			 *         claves. <br>
			 *         Como todos los mapas tienen las mismas claves se puede
			 *         elegir el conjunto de claves de cualquiera.
			 */
			@Override
			public List<ProjectDTO> getObject() {
				Set<ProjectDTO> keys = new TreeSet<ProjectDTO>(
						new ProjectDTOComparator());
				keys.addAll(AddItemForm.this.getPrioritiesMap().keySet());
				List<ProjectDTO> list = new ArrayList(keys);
				return list;
			}

		};

		// crea un renderer específico para los proyectos.
		IChoiceRenderer<String> projectRenderer = new ChoiceRenderer<String>(
				"toString", "oid");
		DropDownChoice projectsList = new DropDownChoice("project",
				new PropertyModel(this, "selectedProject"), projectChoices,
				projectRenderer);

		projectsList.setOutputMarkupId(true);

		// agrega un behavior para validar que se seleccione siempre un proyecto
		// antes de enviar el formulario
		projectsList.add(new AbstractValidator() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Este método es invocado cuando se debe validar el componente. <br>
			 * Si el componente no tiene una selección entonces se registra un
			 * error.
			 * 
			 * @param validatable
			 *            es el componente que se debe validar
			 */
			@Override
			protected void onValidate(IValidatable validatable) {
				// no hace nada
			}

			/**
			 * Getter.
			 * 
			 * @return por defecto siempre se devuelve false para que no se
			 *         valide en caso de tener un valor nulo el componente.
			 */
			@Override
			public boolean validateOnNullValue() {
				return false;
			}

		});

		// agrega un behavior para refrescar la lista de prioridades dependiendo
		// del proyecto seleccionado.
		projectsList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Manejador del evento de actualización de la lista de proyectos.
			 * Se actualiza la lista de prioridades con las prioridades
			 * correspondientes al proyecto seleccionado.
			 * 
			 * @param target
			 *            es el objetivo del pedido AJAX.
			 */
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				try {

					AddItemForm.this.getResponsiblesList().setEnabled(
							AddItemForm.this.getResponsiblesListEnabledState());

					target.addComponent(AddItemForm.this.getPrioritiesList());
					target.addComponent(AddItemForm.this.getResponsiblesList());
					target.addComponent(AddItemForm.this.getItemTypesList());

					AddItemForm.this.getPropertyPanelsList().replaceWith(
							AddItemForm.this.createAdditionalPropertyPanel());
					target.addComponent(AddItemForm.this
							.getPropertyPanelsList());

					target.addComponent(AddItemForm.this
							.get("titleAIFFeedback"));
					target.addComponent(AddItemForm.this
							.get("projectAIFFeedback"));
					target.addComponent(AddItemForm.this
							.get("descriptionAIFFeedback"));
					target.addComponent(AddItemForm.this
							.get("priorityAIFFeedback"));
					target.addComponent(AddItemForm.this
							.get("itemTypeAIFFeedback"));
					target.addComponent(AddItemForm.this
							.get("stateAIFFeedback"));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		return projectsList;

	}

	/**
	 * Carga la información de cada uno de los proyectos en los que el usuario
	 * puede cargar un ítem; la información relacionada con cada proyecto es
	 * almacenada en diccionarios (uno para las prioriades, otro para los
	 * módulos, etc).
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se debe
	 *            recuperar toda la información de sus proyectos.
	 * 
	 */
	private void loadProjectsInformation(UserDTO anUserDTO) {
		// recupera la lista de todos los proyectos en los que el usuario puede
		// dar de alta el item y los carga en todos los diccionarios
		Iterator<ProjectDTO> projectsIterator;
		try {
			projectsIterator = this.getProjectsService()
					.findPrivateProjectsOfUser(anUserDTO).iterator();
			ProjectDTO aProjectDTO;
			String aPropertyNameForOrdering = this
					.getSystemProperty("PRIORITIES_ADMINISTRATION_COLUMN_NAME");
			String anOrdering = this
					.getSystemProperty("PRIORITIES_ADMINISTRATION_COLUMN_ORDER");
			while (projectsIterator.hasNext()) {
				aProjectDTO = projectsIterator.next();

				this.getPrioritiesMap().put(
						aProjectDTO,
						(List<PriorityDTO>) this.getPrioritiesService()
								.findPrioritiesOfProject(aProjectDTO,
										aPropertyNameForOrdering, anOrdering));

				this
						.getItemTypesMap()
						.put(
								aProjectDTO,
								(List<ItemTypeDTO>) this
										.getItemsService()
										.findItemTypesOfProject(
												aProjectDTO,
												this
														.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_NAME),
												this
														.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_ORDER)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Getter.
	 * 
	 * @return el título del nuevo ítem.
	 */
	public String getTitleAIF() {
		return this.titleAIF;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del nuevo ítem.
	 */
	public void setTitleAIF(String aTitle) {
		this.titleAIF = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto seleccionado por el usuario.
	 */
	public ProjectDTO getSelectedProject() {
		return this.selectedProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto del proyecto seleccionado por el usuario.
	 */
	public void setSelectedProject(ProjectDTO aDTO) {
		this.selectedProject = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto de la prioridad seleccionada por el usuario.
	 */
	public PriorityDTO getSelectedPriority() {
		return this.selectedPriority;
	}

	/**
	 * Setter.
	 * 
	 * @param aPriorityDTO
	 *            es el dto de la prioridad seleccionada por el usuario.
	 */
	public void setSelectedPriority(PriorityDTO aPriorityDTO) {
		this.selectedPriority = aPriorityDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que contiene la lista de prioridades.
	 */
	@SuppressWarnings("unchecked")
	public DropDownChoice getPrioritiesList() {
		return (DropDownChoice) this.get("priority");
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que contiene la lista de tipos de ítems.
	 */
	@SuppressWarnings("unchecked")
	public DropDownChoice getItemTypesList() {
		return (DropDownChoice) this.get("itemType");
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que contiene la lista de paneles de propiedades
	 *         adicionales.
	 */
	public Panel getPropertyPanelsList() {
		return (Panel) this.get("propertyPanels");
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que contiene la lista de usuarios.
	 */
	@SuppressWarnings("unchecked")
	public DropDownChoice getResponsiblesList() {
		return (DropDownChoice) this.get("responsible");
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene para dto de proyecto una lista con
	 *         los dtos de las prioridades de dicho proyecto.
	 */
	public Map<ProjectDTO, Collection<PriorityDTO>> getPrioritiesMap() {
		return this.prioritiesMap;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es un diccionario que contiene como claves los dtos de los
	 *            proyectos del usuario y como valores listas de dtos de
	 *            prioridades pertenecientes a cada proyecto.
	 */
	public void setPrioritiesMap(Map<ProjectDTO, Collection<PriorityDTO>> aMap) {
		this.prioritiesMap = aMap;
	}

	/**
	 * Getter.
	 * 
	 * @return la descripción del nuevo ítem.
	 */
	public String getDescriptionAIF() {
		return this.descriptionAIF;
	}

	/**
	 * Setter.
	 * 
	 * @param aDescription
	 *            es la nueva descripción del ítem.
	 */
	public void setDescriptionAIF(String aDescription) {
		this.descriptionAIF = aDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return el estado seleccionado para el nuevo ítem.
	 */
	public ItemStateEnum getSelectedState() {
		return this.selectedState;
	}

	/**
	 * Setter.
	 * 
	 * @param aState
	 *            es el estado seleccionado para el ítem.
	 */
	public void setSelectedState(ItemStateEnum aState) {
		this.selectedState = aState;
	}

	/**
	 * Getter.
	 * 
	 * @return es el candidato a responsable del nuevo ítem.
	 */
	public AbstractUserDTO getSelectedResponsible() {
		return this.selectedResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param anUser
	 *            es el usuario candidato a ser responsable del nuevo ítem.
	 */
	public void setSelectedResponsible(AbstractUserDTO anUser) {
		this.selectedResponsible = anUser;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que se pueda habilitar el componente de listado
	 *         de responsables; false en caso contrario.
	 */
	private boolean getResponsiblesListEnabledState() {
		boolean result = false;

		if (this.getSelectedState() != null
				&& this.getSelectedState().equals(ItemStateEnum.OPEN)
				&& this.getSelectedProject() != null
				&& this
						.getSelectedProject()
						.getItemAssignmentStrategy()
						.equals(
								"zinbig.item.model.projects.UserSelectedAssignmentStrategy")) {
			result = true;

		} else {
			result = false;
		}

		return result;
	}

	/**
	 * Este método es ejecutado al finalizar la creación del html
	 * correspondiente a este componente.
	 */
	@Override
	protected void onAfterRender() {

		super.onAfterRender();
		final Response response = this.getResponse();
		response
				.write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[2].focus()</script>");
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al tipo de ítem seleccionado.
	 */
	public ItemTypeDTO getSelectedItemType() {
		return this.selectedItemType;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de ítem seleccionado.
	 */
	public void setSelectedItemType(ItemTypeDTO anItemTypeDTO) {
		this.selectedItemType = anItemTypeDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene para cada dto de proyecto una lista
	 *         de los dtos de sus tipos de ítems.
	 */
	public Map<ProjectDTO, Collection<ItemTypeDTO>> getItemTypesMap() {
		return this.itemTypesMap;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es un diccionario que contiene para cada dto de proyecto una
	 *            lista de los dtos de sus tipos de ítems.
	 */
	public void setItemTypesMap(Map<ProjectDTO, Collection<ItemTypeDTO>> aMap) {
		this.itemTypesMap = aMap;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de ítems que se desea unificar.
	 */
	public Collection<String> getItemsToAggregate() {
		return this.itemsToAggregate;
	}

	/**
	 * Setter.
	 * 
	 * @param someItemIds
	 *            es una colección que contiene los identificadores de los ítems
	 *            que se desea unificar.
	 */
	public void setItemsToAggregate(Collection<String> someItemIds) {
		this.itemsToAggregate = someItemIds;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente utilizado para adjuntar archivos.
	 */
	public FileUploadField getAttachedFile() {
		return this.attachedFile;
	}

	/**
	 * Setter.
	 * 
	 * @param aComponent
	 *            es el componente utilizado para adjuntar archivos.
	 */
	public void setAttachedFile(FileUploadField aComponent) {
		this.attachedFile = aComponent;
	}

}
