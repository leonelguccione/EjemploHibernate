/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.wicket.Response;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.ItemTab;
import zinbig.item.application.pages.Pageable;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTOComparator;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para crear nuevos filtros de ítems.
 * Estos nuevos filtros pueden ser ejecutados directamente o se pueden guardar
 * previamente sin ejecutarlos. <br>
 * En otras palabras, la ejecución y el almacenamiento de un filtro son
 * independientes. <br>
 * Este formulario se basa en su estado (implementación del patrón de diseño
 * State) para definir cuales componentes visuales deben aparecer y como se
 * deben popular los mismos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class AddItemFilterForm extends ItemForm implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 6916942063104088504L;

	/**
	 * Mantiene los dtos de los componentes de filtro por proyectos.
	 */
	protected Collection<FilterComponentByProjectDTO> selectedFilterComponentByProjects;

	/**
	 * Mantiene la referencia al dto que representa al usuario en la sesión.
	 * Este dto puede ser nulo en caso de que el filtro se esté creando para un
	 * usuario anónimo.
	 */
	protected UserDTO userDTO;

	/**
	 * Es el dto que representa al proyecto en la sesión. Este dto podría ser
	 * nulo en caso de no haber ingresado previamente en un proyecto.
	 */
	protected ProjectDTO projectDTO;

	/**
	 * Mantiene una referencia al estado de este componente.
	 */
	protected AddFilterFormState state;

	/**
	 * Es el panel (AJAX) que contiene las solapas que se deben ir creando para
	 * cada ejecución de un filtro.
	 */
	protected AjaxTabbedPanel panel;

	/**
	 * Mantiene la selección de los estados de los ítems para filtrar.
	 */
	protected Collection<ItemStateEnum> selectedItemStates;

	/**
	 * Mantiene la selección de los responsables de los ítems para filtrar.
	 */
	protected Collection<AbstractUserDTO> selectedResponsibles;

	/**
	 * Mantiene la selección de los tipos de ítem para filtrar.
	 */
	protected Collection<ItemTypeDTO> selectedItemTypes;

	/**
	 * Mantiene la selección de los nodos de los ítems para filtrar.
	 */
	protected Collection<WorkflowNodeDescriptionDTO> selectedNodes;

	/**
	 * Es el componente al cual se ha agregado este formulario.
	 */
	public Pageable pageable;

	/**
	 * Mantiene la referencia al texto por el cual se debe filtrar los ítems.
	 */
	public String freeText;

	/**
	 * Define si se debe negar o no la condición establecida por el filtro por
	 * responsables.
	 */
	public boolean negateResponsible;

	/**
	 * Define si se debe negar o no la condición establecida por el filtro por
	 * estados de los ítems.
	 */
	public boolean negateItemState;

	/**
	 * Define si se debe negar o no la condición establecida por el filtro por
	 * tipos de los ítems.
	 */
	public boolean negateItemType;

	/**
	 * Define si se debe negar o no la condición establecida por el filtro por
	 * proyecto de los ítems.
	 */
	public boolean negateProject;

	/**
	 * Define si se debe negar o no la condición establecida por el filtro por
	 * nodo de los ítems.
	 */
	public boolean negateNode;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param anUserDTO
	 *            es el dto que representa al usuario. Puede ser nulo en caso de
	 *            que sea un usuario anónimo.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto en la sesión. Este
	 *            parámetro podría ser nulo.
	 * @param aPanel
	 *            es el panel que contendrá las solapas para cada una de las
	 *            ejecuciones de los filtros.
	 * @param aPageable
	 *            es el componente (página) a la cual se ha agregado este
	 *            formulario.
	 */
	public AddItemFilterForm(String anId, UserDTO anUserDTO,
			AjaxTabbedPanel aPanel, ProjectDTO aProjectDTO, Pageable aPageable) {
		super(anId);

		this.setPanel(aPanel);

		this.setPageable(aPageable);

		// dependiendo del usuario se asigna el estado a este formulario.
		if (anUserDTO == null) {
			this.setState(new AddItemFilterFormWithoutUserState(this));

		} else {
			this.setState(new AddItemFilterFormWithUserState(this));

		}

		this.setUserDTO(anUserDTO);

		this.setProjectDTO(aProjectDTO);

		// agrega el link que permite ver el listado de ítems filtrado
		this.addViewLink();

		// crea el componente para listar los proyectos
		this.createProjectsListComponent();

		// crea el componente para listar los estados de los ítems
		this.createItemStatesListComponent();

		// crea el componente para listar los responsables de los ítems
		this.createResponsibleListComponent();

		// crea el componente para listar los tipos de los ítems
		this.createItemTypesListComponent();

		// crea el componente para listar los nodos de los ítems
		this.createNodesListComponent();

		// crea el componente para filtrar por texto libre
		this.createFreeTextComponent();

	}

	/**
	 * Crea el componente que permite cargar el texto libre por el cual se
	 * filtrarán los ítems.
	 * 
	 */
	private void createFreeTextComponent() {
		this.setFreeText("");
		// construye el input field para el texto
		final TextField<String> freeTextField = new TextField<String>(
				"freeText", new PropertyModel<String>(this, "freeText"));

		this.add(freeTextField);

	}

	/**
	 * Getter.
	 * 
	 * @return el dto del usuario en la sesión.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto que representa al usuario.
	 */
	private void setUserDTO(UserDTO aDTO) {
		this.userDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el estado de este componente.
	 */
	public AddFilterFormState getState() {
		return this.state;
	}

	/**
	 * Setter.
	 * 
	 * @param aState
	 *            es el estado de este componente.
	 */
	private void setState(AddFilterFormState aState) {
		this.state = aState;
	}

	/**
	 * Getter.<br>
	 * Este método es únicamente invocado por el estado del formulario
	 * correspondiente a un usuario en la sesión.
	 * 
	 * @return una colección que contiene dtos que representan a todos los
	 *         proyectos (públicos y privados del usuario) administrados por el
	 *         sistema.
	 */
	public Collection<FilterComponentByProjectDTO> getAllProjects() {
		Collection<FilterComponentByProjectDTO> result = this
				.getPublicProjects();

		Collection<ProjectDTO> projectsDTOs = new ArrayList<ProjectDTO>();
		try {

			projectsDTOs.addAll(this.getProjectsService().findProjectsOfUser(
					this.getUserDTO()));

			if (this.getProjectDTO() != null) {
				FilterComponentByProjectDTO dto = new FilterComponentByProjectDTO(
						this.getProjectDTO().getName(), this.getProjectDTO()
								.getOid());
				this.getSelectedFilterComponentByProjects().add(dto);
			}

			Iterator<ProjectDTO> iterator = projectsDTOs.iterator();
			ProjectDTO aProjectDTO;
			while (iterator.hasNext()) {
				aProjectDTO = iterator.next();
				result.add(new FilterComponentByProjectDTO(aProjectDTO
						.getName(), aProjectDTO.getOid()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene únicamente los proyectos públicos
	 *         administrados por el sistema.
	 */
	public Collection<FilterComponentByProjectDTO> getPublicProjects() {
		Collection<FilterComponentByProjectDTO> result = new TreeSet<FilterComponentByProjectDTO>(
				new FilterComponentByProjectDTOComparator());
		Collection<ProjectDTO> projectsDTOs = new ArrayList<ProjectDTO>();

		try {
			projectsDTOs.addAll(this.getProjectsService()
					.findAllPublicProjects());

			Iterator<ProjectDTO> iterator = projectsDTOs.iterator();
			ProjectDTO aProjectDTO;
			while (iterator.hasNext()) {
				aProjectDTO = iterator.next();
				result.add(new FilterComponentByProjectDTO(aProjectDTO
						.getName(), aProjectDTO.getOid()));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Agrega un link que permite ver el resultado de la aplicación del filtro.
	 */
	public void addViewLink() {
		SubmitLink aLink = new SubmitLink("viewLink") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el formulario para ver el resultado del filtro creado.
			 */
			@Override
			public void onSubmit() {

				try {
					AddItemFilterForm.this.createNewTabForFilter();
					AddItemFilterForm.this.clearInput();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		};
		aLink.setEnabled(true);
		aLink.setOutputMarkupId(true);
		this.add(aLink);

	}

	/**
	 * Crea un DTO de un filtro a partir de la información ingresada.
	 * 
	 * @param aTitle
	 *            es el título del nuevo filtro.
	 * 
	 * @return el dto que contiene toda la información requerida para la
	 *         ejecución de un filtro de ítems.
	 */
	protected FilterDTO createFilter(String aTitle) throws Exception {

		// al crear un nuevo filtro por defecto se le asigna el oid 0 para
		// identificar que no está guardado.
		ItemsServiceBI service = this.getItemsService();

		return service.createFilter(this.getUserDTO(), aTitle, this
				.getSelectedFilterComponentByProjects(), "", this
				.getSelectedItemStates(), this.getSelectedResponsibles(), this
				.getSelectedItemTypes(), this.getSelectedNodes(), this
				.isNegateResponsible(), this.isNegateItemState(), this
				.isNegateItemType(), this.isNegateProject(), this
				.isNegateNode(), getFreeText());
	}

	/**
	 * Getter.
	 * 
	 * @return el panel que contiene las solapas de las ejecuciones de los
	 *         filtros.
	 */
	public AjaxTabbedPanel getPanel() {
		return this.panel;
	}

	/**
	 * Setter.
	 * 
	 * @param aPanel
	 *            es el panel que contiene las solapas de las ejecuciones de los
	 *            filtros.
	 */
	public void setPanel(AjaxTabbedPanel aPanel) {
		this.panel = aPanel;
	}

	/**
	 * Crea el componente para listar todos los proyectos en los cuales el
	 * usuario quiere filtrar los ítems.<br>
	 * 
	 * @return el componente creado para mostrar los proyectos.
	 */
	@SuppressWarnings("unchecked")
	private void createProjectsListComponent() {

		this
				.setSelectedFilterComponentByProjects(new ArrayList<FilterComponentByProjectDTO>());

		// crea un modelo para acceder a la lista de proyectos que debe
		// mostrarse a través del dropdownchoice de proyectos.
		IModel<List<FilterComponentByProjectDTO>> projectChoices = new AbstractReadOnlyModel<List<FilterComponentByProjectDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de componentes de filtrado de proyectos
			 * que contiene este modelo.
			 * 
			 * @return una colección con los dtos de los proyectos. Dependiendo
			 *         del estado del formulario este listado contendrá todos
			 *         los proyectos del usuario y los públicos o solamente los
			 *         públicos.
			 */
			@Override
			public List<FilterComponentByProjectDTO> getObject() {
				// obtiene la lista de proyectos con los que se debe armar el
				// componente de proyectos
				ArrayList<FilterComponentByProjectDTO> projects = new ArrayList<FilterComponentByProjectDTO>();

				projects.addAll(AddItemFilterForm.this.getState()
						.getProjectsList());

				return projects;
			}

		};

		// crea un renderer específico para los proyectos.
		IChoiceRenderer<String> projectRenderer = new ChoiceRenderer<String>(
				"title", "projectOid");
		final ListMultipleChoice projectsList = new ListMultipleChoice(
				"selectedFilterComponentByProjects", new PropertyModel(this,
						"selectedFilterComponentByProjects"), projectChoices,

				projectRenderer);
		projectsList.setOutputMarkupId(true);
		projectsList.setMaxRows(3);
		projectsList.setEnabled(true);

		this.add(projectsList);

		// agrega el checkbox para negar esta condición
		CheckBox aCheckBox = new CheckBox("negateProjectCheckbox",
				new PropertyModel(this, "negateProject"));
		this.add(aCheckBox);

	}

	/**
	 * Crea el componente para listar todos los estados en los que podrían estar
	 * los ítems.<br>
	 * 
	 * @return el componente creado para mostrar los estados de los proyectos.
	 */
	@SuppressWarnings("unchecked")
	private void createItemStatesListComponent() {

		// crea un modelo para acceder a la lista de estados de los ítems.
		IModel<List<ItemStateEnum>> stateChoices = new AbstractReadOnlyModel<List<ItemStateEnum>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de estados de los ítems para armar la lista.
			 * 
			 * @return una colección con los estados de los ítems.
			 */
			@Override
			public List<ItemStateEnum> getObject() {
				// obtiene la lista de estados
				TreeSet<ItemStateEnum> aTreeSet = new TreeSet<ItemStateEnum>(
						new Comparator<ItemStateEnum>() {

							/**
							 * Compara dos enumerativos por su nombre
							 * internacionalizado.
							 * 
							 * @param firstEnum
							 *            es el primer enumerativo a comparar.
							 * @param secondEnum
							 *            es el segundo enumerativo a comparar.
							 * @return un entero que representa el orden
							 *         teniendo en cuenta su traducción.
							 */
							@Override
							public int compare(ItemStateEnum firstEnum,
									ItemStateEnum secondEnum) {
								return getString(firstEnum.toString())
										.compareTo(
												getString(secondEnum.toString()));
							}

						});
				aTreeSet.add(ItemStateEnum.CREATED);
				aTreeSet.add(ItemStateEnum.BLOCKED);
				aTreeSet.add(ItemStateEnum.OPEN);
				aTreeSet.add(ItemStateEnum.CLOSED);
				ArrayList<ItemStateEnum> states = new ArrayList<ItemStateEnum>();
				states.addAll(aTreeSet);

				return states;
			}

		};

		// crea un renderer específico para los estados enumerados.
		IChoiceRenderer<ItemStateEnum> stateRenderer = new ChoiceRenderer<ItemStateEnum>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Retorna el string internacionalizado para mostrar en la lista.
			 * 
			 * @param object
			 *            es el objeto que se debe internacionalizar.
			 * @return un string que representa al objeto recibido en el idioma
			 *         actual del usuario.
			 */
			@Override
			public Object getDisplayValue(ItemStateEnum object) {

				return getString(object.name());
			}

		};

		final ListMultipleChoice statesList = new ListMultipleChoice(
				"itemStates", new PropertyModel(this, "selectedItemStates"),
				stateChoices, stateRenderer);
		statesList.setMaxRows(3);

		statesList.setOutputMarkupId(true);

		this.add(statesList);

		// agrega el checkbox para negar esta condición
		CheckBox aCheckBox = new CheckBox("negateItemStateCheckbox",
				new PropertyModel(this, "negateItemState"));
		this.add(aCheckBox);

	}

	/**
	 * Getter.
	 * 
	 * @return los dtos de los componente del filtro que permite filtrar por
	 *         proyectos.
	 */
	public Collection<FilterComponentByProjectDTO> getSelectedFilterComponentByProjects() {
		return this.selectedFilterComponentByProjects;
	}

	/**
	 * Setter.
	 * 
	 * @param someDTOs
	 *            son los dtos de los componentes del filtro que permite filtrar
	 *            por proyectos.
	 */
	public void setSelectedFilterComponentByProjects(
			Collection<FilterComponentByProjectDTO> someDTOs) {
		this.selectedFilterComponentByProjects = someDTOs;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto que está en la sessión. Este dto puede ser
	 *         nulo en caso de que no se haya ingresado a ninún proyecto en
	 *         particular para listar sus ítems.
	 */
	public Object getProjectDTOFromSession() {
		return ((ItemSession) this.getSession()).getProjectDTO();

	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto en la sesión.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el dto del proyecto en la sesión.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el id del contenedor de la imagen utilizada por el componente
	 *         AJAX.
	 */
	public String getMarkupIdForImageContainer() {
		return this.get("indicatorImg").getMarkupId();
	}

	/**
	 * Getter.
	 * 
	 * @return los estados de los ítems seleccionados para el filtro.
	 */
	public Collection<ItemStateEnum> getSelectedItemStates() {
		return this.selectedItemStates;
	}

	/**
	 * Setter.
	 * 
	 * @param someStates
	 *            son los estados de los ítems seleccionados para el filtro.
	 */
	public void setSelectedItemStates(Collection<ItemStateEnum> someStates) {
		this.selectedItemStates = someStates;
	}

	/**
	 * Crea una nueva solapa para el nuevo filtro y retorna el filtro
	 * recientemente creado.
	 * 
	 * @return el dto del filtro recientemente creado.
	 * @throws Exception
	 *             esta excepción puede levantarse en caso de error al ejecutar
	 *             el servicio de filtros.
	 */
	private FilterDTO createNewTabForFilter() throws Exception {

		AjaxTabbedPanel panel = AddItemFilterForm.this.getPanel();
		panel.setVisible(true);
		List<ITab> tabs = panel.getTabs();

		String titleString = this.getString("NEW_TAB_FOR_FILTER");

		FilterDTO filterDTO = AddItemFilterForm.this.createFilter(titleString);
		ItemTab newTab = new ItemTab(new Model<String>(titleString), filterDTO,
				AddItemFilterForm.this.getUserDTO(), AddItemFilterForm.this
						.getProjectDTO(), this.getPageable(), panel);
		tabs.add(newTab);

		panel.setSelectedTab(tabs.size() - 1);
		AddItemFilterForm.this
				.setSelectedItemStates(new ArrayList<ItemStateEnum>());
		AddItemFilterForm.this
				.setSelectedResponsibles(new ArrayList<AbstractUserDTO>());
		AddItemFilterForm.this
				.setSelectedItemTypes(new ArrayList<ItemTypeDTO>());
		AddItemFilterForm.this.setNegateItemState(false);
		AddItemFilterForm.this.setNegateItemType(false);
		AddItemFilterForm.this.setNegateNode(false);
		AddItemFilterForm.this.setNegateProject(false);
		AddItemFilterForm.this.setNegateResponsible(false);
		AddItemFilterForm.this.setFreeText("");
		return filterDTO;

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
				.write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[1].focus()</script>");
	}

	/**
	 * Getter.
	 * 
	 * @return la página a la cual se ha agregado este formulario.
	 */
	public Pageable getPageable() {
		return this.pageable;
	}

	/**
	 * Setter.
	 * 
	 * @param aPage
	 *            es la página a la cual se ha agregado este formulario.
	 */
	public void setPageable(Pageable aPage) {
		this.pageable = aPage;
	}

	/**
	 * Crea el componente para listar todos los tipos de ítems.<br>
	 * 
	 * @return el componente creado para mostrar los tipos de ítems.
	 */
	@SuppressWarnings("unchecked")
	private void createItemTypesListComponent() {

		// crea un modelo para acceder a la lista de tipos de los ítems.
		IModel<List<ItemTypeDTO>> typesChoices = new AbstractReadOnlyModel<List<ItemTypeDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de tipos de los ítems para armar la lista.
			 * 
			 * @return una colección con los posibles tipos de ítems.
			 */
			@Override
			public List<ItemTypeDTO> getObject() {

				TreeSet<ItemTypeDTO> types = new TreeSet<ItemTypeDTO>(
						new Comparator<ItemTypeDTO>() {

							/**
							 * Compara dos elementos para ordenarlos y descartar
							 * repetidos. En este caso solamente necesitamos el
							 * nombre de los tipos de ítems, asi que no se
							 * aceptan repetidos.
							 * 
							 * @param anItemTypeDTO
							 * @param anotherItemTypeDTO
							 * @return
							 */
							@Override
							public int compare(ItemTypeDTO anItemTypeDTO,
									ItemTypeDTO anotherItemTypeDTO) {
								return anItemTypeDTO.getTitle().compareTo(
										anotherItemTypeDTO.getTitle());
							}
						});

				try {
					types.addAll(AddItemFilterForm.this.getItemsService()
							.getAllItemTypesOfProjects());

				} catch (Exception e) {
					e.printStackTrace();
				}

				ArrayList<ItemTypeDTO> result = new ArrayList<ItemTypeDTO>();
				result.addAll(types);
				return result;
			}

		};

		// crea un renderer específico para tipos de ítems recuperados
		IChoiceRenderer<ItemTypeDTO> typesRenderer = new ChoiceRenderer<ItemTypeDTO>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Retorna el string internacionalizado para mostrar en la lista.
			 * 
			 * @param object
			 *            es el objeto que se debe internacionalizar.
			 * @return un string que representa al objeto recibido en el idioma
			 *         actual del usuario.
			 */
			@Override
			public Object getDisplayValue(ItemTypeDTO object) {

				return object.getTitle();
			}

			/**
			 * Retorna el valor que debe asociarse al elemento recibido.
			 * 
			 * @param object
			 *            es el objeto que debe representarse.
			 * @param index
			 *            es la posición en la lista.
			 * 
			 */
			@Override
			public String getIdValue(ItemTypeDTO object, int index) {

				return object.getTitle();
			}

		};

		final ListMultipleChoice typesList = new ListMultipleChoice(
				"itemTypes", new PropertyModel(this, "selectedItemTypes"),
				typesChoices,

				typesRenderer);
		typesList.setOutputMarkupId(true);
		this.add(typesList);

		// agrega el checkbox para negar esta condición
		CheckBox aCheckBox = new CheckBox("negateItemTypeCheckbox",
				new PropertyModel(this, "negateItemType"));
		this.add(aCheckBox);

	}

	/**
	 * Crea el componente para listar todos los nodos de ítems.<br>
	 * En este método se toman todas las descripciones de nodo de todos los
	 * proyectos, quedándose solamente con los títulos distintos.
	 * 
	 * @return el componente creado para mostrar los nodos de ítems.
	 */
	@SuppressWarnings("unchecked")
	private void createNodesListComponent() {

		// crea un modelo para acceder a la lista de nodos de los ítems.
		IModel<List<WorkflowNodeDescriptionDTO>> nodesChoices = new AbstractReadOnlyModel<List<WorkflowNodeDescriptionDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de nodos de los ítems para armar la lista. No
			 * importa el oid del nodo, sino simplemente su título.
			 * 
			 * @return una colección con los posibles nodos de ítems.
			 */
			@Override
			public List<WorkflowNodeDescriptionDTO> getObject() {

				TreeSet<WorkflowNodeDescriptionDTO> nodes = new TreeSet<WorkflowNodeDescriptionDTO>(
						new Comparator<WorkflowNodeDescriptionDTO>() {

							/**
							 * Compara dos elementos para ordenarlos y descartar
							 * repetidos. En este caso solamente necesitamos el
							 * título de los nodos de ítems, asi que no se
							 * aceptan repetidos.
							 * 
							 * @param aNode
							 *            es el primer nodo a comparar.
							 * @param anotherNode
							 *            es el segundo nodo a comparar.
							 * @return un entero que representa el resultado de
							 *         comparar los títulos de los nodos.
							 */
							@Override
							public int compare(
									WorkflowNodeDescriptionDTO aNode,
									WorkflowNodeDescriptionDTO anotherNode) {
								return aNode.getTitle().compareTo(
										anotherNode.getTitle());
							}
						});

				try {
					nodes.addAll(AddItemFilterForm.this.getWorkflowsService()
							.getAllWorkflowNodeDescriptionOfProjects());

				} catch (Exception e) {
					e.printStackTrace();
				}

				ArrayList<WorkflowNodeDescriptionDTO> result = new ArrayList<WorkflowNodeDescriptionDTO>();
				result.addAll(nodes);
				return result;
			}

		};

		// crea un renderer específico para nodos de ítems recuperados
		IChoiceRenderer<WorkflowNodeDescriptionDTO> nodesRenderer = new ChoiceRenderer<WorkflowNodeDescriptionDTO>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Retorna el string internacionalizado para mostrar en la lista.
			 * 
			 * @param object
			 *            es el objeto que se debe internacionalizar.
			 * @return un string que representa al objeto recibido en el idioma
			 *         actual del usuario.
			 */
			@Override
			public Object getDisplayValue(WorkflowNodeDescriptionDTO object) {

				return object.getTitle();
			}

			/**
			 * Retorna el valor que debe asociarse al elemento recibido.
			 * 
			 * @param object
			 *            es el objeto que debe representarse.
			 * @param index
			 *            es la posición en la lista.
			 * 
			 */
			@Override
			public String getIdValue(WorkflowNodeDescriptionDTO object,
					int index) {

				return object.getTitle();
			}

		};

		final ListMultipleChoice nodesList = new ListMultipleChoice("nodes",
				new PropertyModel(this, "selectedNodes"), nodesChoices,
				nodesRenderer);
		nodesList.setOutputMarkupId(true);

		this.add(nodesList);

		// agrega el checkbox para negar esta condición
		CheckBox aCheckBox = new CheckBox("negateNodeCheckbox",
				new PropertyModel(this, "negateNode"));
		this.add(aCheckBox);

	}

	/**
	 * Getter.
	 * 
	 * @return los dtos que representan a los usuarios seleccionados como
	 *         responsables para filtrar.
	 */
	public Collection<AbstractUserDTO> getSelectedResponsibles() {
		return this.selectedResponsibles;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            son los dtos que representan a los usuarios seleccionados como
	 *            responsables para filtrar.
	 */
	public void setSelectedResponsibles(Collection<AbstractUserDTO> aCollection) {
		this.selectedResponsibles = aCollection;
	}

	/**
	 * Crea el componente para listar todos los usuarios que podrían ser
	 * responsables de los ítems.<br>
	 * 
	 * @return el componente creado para mostrar los usuarios del sistema.
	 */
	@SuppressWarnings("unchecked")
	private void createResponsibleListComponent() {

		// crea un modelo para acceder a la lista de responsables de los ítems.
		IModel<List<AbstractUserDTO>> usersChoices = new AbstractReadOnlyModel<List<AbstractUserDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de responsables de los ítems para armar la
			 * lista.
			 * 
			 * @return una colección con los posibles responsables.
			 */
			@Override
			public List<AbstractUserDTO> getObject() {

				ArrayList<AbstractUserDTO> users = new ArrayList<AbstractUserDTO>();

				try {
					users.addAll(AddItemFilterForm.this.getUsersService()
							.getAllUsergroups());
					users.addAll(AddItemFilterForm.this.getUsersService()
							.getAllUsers());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return users;
			}

		};

		// crea un renderer específico para usuarios recuperados
		IChoiceRenderer<AbstractUserDTO> usersRenderer = new ChoiceRenderer<AbstractUserDTO>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Retorna el string internacionalizado para mostrar en la lista.
			 * 
			 * @param object
			 *            es el objeto que se debe internacionalizar.
			 * @return un string que representa al objeto recibido en el idioma
			 *         actual del usuario.
			 */
			@Override
			public Object getDisplayValue(AbstractUserDTO object) {

				return getString(object.getAlias());
			}

		};

		final ListMultipleChoice usersList = new ListMultipleChoice(
				"itemResponsibles", new PropertyModel(this,
						"selectedResponsibles"), usersChoices,

				usersRenderer);
		usersList.setOutputMarkupId(true);
		usersList.setMaxRows(3);

		this.add(usersList);

		// agrega el checkbox para negar esta condición
		CheckBox aCheckBox = new CheckBox("negateItemResponsibleCheckbox",
				new PropertyModel(this, "negateResponsible"));
		this.add(aCheckBox);
	}

	/**
	 * Getter.
	 * 
	 * @return los tipos de ítems seleccionados para filtrar.
	 */
	public Collection<ItemTypeDTO> getSelectedItemTypes() {
		return this.selectedItemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            son los tipos de ítems seleccionados para filtrar.
	 */
	public void setSelectedItemTypes(Collection<ItemTypeDTO> aCollection) {
		this.selectedItemTypes = aCollection;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición de responsable; false en caso
	 *         contrario.
	 */
	public boolean isNegateResponsible() {
		return this.negateResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condición del filtro por
	 *            responsable.
	 */
	public void setNegateResponsible(boolean aBoolean) {
		this.negateResponsible = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del estado; false en caso
	 *         contrario.
	 */
	public boolean isNegateItemState() {
		return this.negateItemState;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condición del filtro por
	 *            estado del ítem.
	 */
	public void setNegateItemState(boolean aBoolean) {
		this.negateItemState = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del tipo; false en caso
	 *         contrario.
	 */
	public boolean isNegateItemType() {
		return this.negateItemType;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condición del filtro por
	 *            tipo del ítem.
	 */
	public void setNegateItemType(boolean aBoolean) {
		this.negateItemType = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del proyecto; false en caso
	 *         contrario.
	 */
	public boolean isNegateProject() {
		return this.negateProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condición del filtro por
	 *            proyecto.
	 */
	public void setNegateProject(boolean aBoolean) {
		this.negateProject = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del nodo; false en caso
	 *         contrario.
	 */
	public boolean isNegateNode() {
		return this.negateNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condición del filtro por
	 *            nodo.
	 */
	public void setNegateNode(boolean aBoolean) {
		this.negateNode = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los dtos de los nodos seleccionados
	 *         para filtrar los ítems.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getSelectedNodes() {
		return this.selectedNodes;
	}

	/**
	 * Setter.
	 * 
	 * @param someNodes
	 *            es una colección que contiene los dtos de los nodos
	 *            seleccionados para filtrar los ítems.
	 */
	public void setSelectedNodes(
			Collection<WorkflowNodeDescriptionDTO> someNodes) {
		this.selectedNodes = someNodes;
	}

	/**
	 * Getter.
	 * 
	 * @return el texto libre por el cual se debe filtrar los ítems.
	 */
	public String getFreeText() {
		return this.freeText;
	}

	/**
	 * Setter.
	 * 
	 * @param aText
	 *            es el texto libre por el cual se debe filtrar los ítems.
	 */
	public void setFreeText(String aText) {
		this.freeText = aText;
	}

}
