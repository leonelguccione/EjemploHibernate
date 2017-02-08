/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicaci�n.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.pages.ViewItemsPage;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTOComparator;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para editar un tipo de �tem en la
 * aplicaci�n. <br>
 * 
 * La �nica restricci�n que se aplica es que no se est� utilizando ya el mismo
 * t�tulo en otro tipo existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemFilterForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 2633207689442370543L;

	/**
	 * Es el identificador del filtro de �tems que se est� editando.
	 */
	public String filterOid;

	/**
	 * Es el DTO que representa al filtro que se est� editando.
	 */
	public FilterDTO filterDTO;

	/**
	 * Mantiene los dtos de los componentes de filtro por proyectos.
	 */
	protected Collection<FilterComponentByProjectDTO> selectedFilterComponentByProjects;

	/**
	 * Define si se debe negar o no la condici�n establecida por el filtro por
	 * proyecto de los �tems.
	 */
	public boolean negateProject;

	/**
	 * Define si se debe negar o no la condici�n establecida por el filtro por
	 * responsables.
	 */
	public boolean negateResponsible;

	/**
	 * Mantiene la selecci�n de los responsables de los �tems para filtrar.
	 */
	protected Collection<AbstractUserDTO> selectedResponsibles;

	/**
	 * Define si se debe negar o no la condici�n establecida por el filtro por
	 * estados de los �tems.
	 */
	public boolean negateItemState;

	/**
	 * Mantiene la selecci�n de los estados de los �tems para filtrar.
	 */
	protected Collection<ItemStateEnum> selectedItemStates;

	/**
	 * Mantiene la selecci�n de los nodos de los �tems para filtrar.
	 */
	protected Collection<WorkflowNodeDescriptionDTO> selectedNodes;

	/**
	 * Define si se debe negar o no la condici�n establecida por el filtro por
	 * nodo de los �tems.
	 */
	public boolean negateNode;

	/**
	 * Mantiene la selecci�n de los tipos de �tem para filtrar.
	 */
	protected Collection<ItemTypeDTO> selectedItemTypes;

	/**
	 * Define si se debe negar o no la condici�n establecida por el filtro por
	 * tipos de los �tems.
	 */
	public boolean negateItemType;

	/**
	 * Mantiene la referencia al texto por el cual se debe filtrar los �tems.
	 */
	public String freeText;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este formulario dentro del panel.
	 * @param aFilterOid
	 *            es el oid del filtro de �tems que se est� editando.
	 */
	public EditItemFilterForm(String id, String aFilterOid) {
		super(id);

		this.setFilterOid(aFilterOid);

		try {
			ItemsServiceBI service = this.getItemsService();
			FilterDTO aFilterDTO = service.findItemFilterById(aFilterOid);
			this.setFilterDTO(aFilterDTO);

			// crea el componente para listar los proyectos
			this.createProjectsListComponent();

			// crea el componente para listar los estados de los �tems
			this.createItemStatesListComponent();

			// crea el componente para listar los responsables de los �tems
			this.createResponsibleListComponent();

			// crea el componente para listar los tipos de los �tems
			this.createItemTypesListComponent();

			// crea el componente para listar los nodos de los �tems
			this.createNodesListComponent();

			// crea el componente para enviar los cambios al filtro
			this.addSubmitLink();

			// crea el componente para filtrar por texto libre
			this.createFreeTextComponent();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Este m�todo es ejecutado al finalizar la creaci�n del html
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
	 * @return el identificador del filtro de �tems que se est� editando.
	 */
	public String getFilterOid() {
		return this.filterOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador del filtro de �tems que se est� editando.
	 */
	public void setFilterOid(String anOid) {
		this.filterOid = anOid;
	}

	/**
	 * Crea el componente para listar todos los proyectos en los cuales el
	 * usuario quiere filtrar los �tems.<br>
	 * 
	 * @return el componente creado para mostrar los proyectos.
	 */
	@SuppressWarnings("unchecked")
	private void createProjectsListComponent() {

		this
				.setSelectedFilterComponentByProjects(new ArrayList<FilterComponentByProjectDTO>());
		this.setNegateProject(this.getFilterDTO().isNegateProject());

		// crea un modelo para acceder a la lista de proyectos que debe
		// mostrarse a trav�s del dropdownchoice de proyectos.
		IModel<List<FilterComponentByProjectDTO>> projectChoices = new AbstractReadOnlyModel<List<FilterComponentByProjectDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de componentes de filtrado de proyectos
			 * que contiene este modelo.
			 * 
			 * @return una colecci�n con los dtos de los proyectos. Dependiendo
			 *         del estado del formulario este listado contendr� todos
			 *         los proyectos del usuario y los p�blicos o solamente los
			 *         p�blicos.
			 */
			@Override
			public List<FilterComponentByProjectDTO> getObject() {
				// obtiene la lista de proyectos con los que se debe armar el
				// componente de proyectos
				ArrayList<FilterComponentByProjectDTO> projects = new ArrayList<FilterComponentByProjectDTO>();

				projects.addAll(EditItemFilterForm.this.getAllProjects());

				return projects;
			}

		};

		// crea un renderer espec�fico para los proyectos.
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

		// agrega el checkbox para negar esta condici�n
		CheckBox aCheckBox = new CheckBox("negateProjectCheckbox",
				new PropertyModel(this, "negateProject"));
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
	 * Getter.<br>
	 * Este m�todo es �nicamente invocado por el estado del formulario
	 * correspondiente a un usuario en la sesi�n.
	 * 
	 * @return una colecci�n que contiene dtos que representan a todos los
	 *         proyectos (p�blicos y privados del usuario) administrados por el
	 *         sistema.
	 */
	public Collection<FilterComponentByProjectDTO> getAllProjects() {
		Collection<FilterComponentByProjectDTO> result = this
				.getPublicProjects();

		Collection<ProjectDTO> projectsDTOs = new ArrayList<ProjectDTO>();
		try {

			projectsDTOs.addAll(this.getProjectsService().findProjectsOfUser(
					this.getUserDTO()));

			FilterDTO filterDTO = this.getFilterDTO();
			String filterComponentByProject = filterDTO
					.getFilterComponentByProject();
			FilterComponentByProjectDTO dto;

			Iterator<ProjectDTO> iterator = projectsDTOs.iterator();
			ProjectDTO aProjectDTO;
			while (iterator.hasNext()) {
				aProjectDTO = iterator.next();
				result.add(new FilterComponentByProjectDTO(aProjectDTO
						.getName(), aProjectDTO.getOid()));

				if (filterComponentByProject.contains(aProjectDTO.getOid())) {
					dto = new FilterComponentByProjectDTO(
							aProjectDTO.getName(), aProjectDTO.getOid());
					this.getSelectedFilterComponentByProjects().add(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene �nicamente los proyectos p�blicos
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
	 * Getter.
	 * 
	 * @return true si se debe negar la condici�n del proyecto; false en caso
	 *         contrario.
	 */
	public boolean isNegateProject() {
		return this.negateProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condici�n del filtro por
	 *            proyecto.
	 */
	public void setNegateProject(boolean aBoolean) {
		this.negateProject = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el DTO que representa el filtro que se est� editando.
	 */
	public FilterDTO getFilterDTO() {
		return this.filterDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el DTO que representa el filtro que se est� editando.
	 */
	public void setFilterDTO(FilterDTO aDTO) {
		this.filterDTO = aDTO;
	}

	/**
	 * Crea el componente para listar todos los usuarios que podr�an ser
	 * responsables de los �tems.<br>
	 * 
	 * @return el componente creado para mostrar los usuarios del sistema.
	 */
	@SuppressWarnings("unchecked")
	private void createResponsibleListComponent() {
		this.setSelectedResponsibles(new ArrayList<AbstractUserDTO>());
		this.setNegateResponsible(this.getFilterDTO().isNegateResponsible());

		// crea un modelo para acceder a la lista de responsables de los �tems.
		IModel<List<AbstractUserDTO>> usersChoices = new AbstractReadOnlyModel<List<AbstractUserDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de responsables de los �tems para armar la
			 * lista.
			 * 
			 * @return una colecci�n con los posibles responsables.
			 */
			@Override
			public List<AbstractUserDTO> getObject() {

				ArrayList<AbstractUserDTO> users = new ArrayList<AbstractUserDTO>();

				try {
					users.addAll(EditItemFilterForm.this.getUsersService()
							.getAllUsergroups());
					users.addAll(EditItemFilterForm.this.getUsersService()
							.getAllUsers());

					String responsibleComponent = getFilterDTO()
							.getFilterComponentByResponsible();
					for (AbstractUserDTO dto : users) {
						if (responsibleComponent.contains(dto.getOid())) {
							getSelectedResponsibles().add(dto);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return users;
			}

		};

		// crea un renderer espec�fico para usuarios recuperados
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

		// agrega el checkbox para negar esta condici�n
		CheckBox aCheckBox = new CheckBox("negateItemResponsibleCheckbox",
				new PropertyModel(this, "negateResponsible"));
		this.add(aCheckBox);
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condici�n de responsable; false en caso
	 *         contrario.
	 */
	public boolean isNegateResponsible() {
		return this.negateResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condici�n del filtro por
	 *            responsable.
	 */
	public void setNegateResponsible(boolean aBoolean) {
		this.negateResponsible = aBoolean;
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
	 * Crea el componente para listar todos los estados en los que podr�an estar
	 * los �tems.<br>
	 * 
	 * @return el componente creado para mostrar los estados de los proyectos.
	 */
	@SuppressWarnings("unchecked")
	private void createItemStatesListComponent() {

		this.setSelectedItemStates(new ArrayList<ItemStateEnum>());
		this.setNegateItemState(this.getFilterDTO().isNegateState());

		// crea un modelo para acceder a la lista de estados de los �tems.
		IModel<List<ItemStateEnum>> stateChoices = new AbstractReadOnlyModel<List<ItemStateEnum>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de estados de los �tems para armar la lista.
			 * 
			 * @return una colecci�n con los estados de los �tems.
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
							 *         teniendo en cuenta su traducci�n.
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

				String itemStates = getFilterDTO().getFilterComponentByState();
				for (ItemStateEnum e : states) {
					if (itemStates.contains(e.toInt().toString())) {
						getSelectedItemStates().add(e);
					}
				}
				return states;
			}

		};

		// crea un renderer espec�fico para los estados enumerados.
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

		// agrega el checkbox para negar esta condici�n
		CheckBox aCheckBox = new CheckBox("negateItemStateCheckbox",
				new PropertyModel(this, "negateItemState"));
		this.add(aCheckBox);

	}

	/**
	 * Getter.
	 * 
	 * @return los estados de los �tems seleccionados para el filtro.
	 */
	public Collection<ItemStateEnum> getSelectedItemStates() {
		return this.selectedItemStates;
	}

	/**
	 * Setter.
	 * 
	 * @param someStates
	 *            son los estados de los �tems seleccionados para el filtro.
	 */
	public void setSelectedItemStates(Collection<ItemStateEnum> someStates) {
		this.selectedItemStates = someStates;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condici�n del estado; false en caso
	 *         contrario.
	 */
	public boolean isNegateItemState() {
		return this.negateItemState;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condici�n del filtro por
	 *            estado del �tem.
	 */
	public void setNegateItemState(boolean aBoolean) {
		this.negateItemState = aBoolean;
	}

	/**
	 * Crea el componente para listar todos los nodos de �tems.<br>
	 * En este m�todo se toman todas las descripciones de nodo de todos los
	 * proyectos, qued�ndose solamente con los t�tulos distintos.
	 * 
	 * @return el componente creado para mostrar los nodos de �tems.
	 */
	@SuppressWarnings("unchecked")
	private void createNodesListComponent() {
		this.setSelectedNodes(new ArrayList<WorkflowNodeDescriptionDTO>());
		this.setNegateNode(this.getFilterDTO().isNegateNode());

		// crea un modelo para acceder a la lista de nodos de los �tems.
		IModel<List<WorkflowNodeDescriptionDTO>> nodesChoices = new AbstractReadOnlyModel<List<WorkflowNodeDescriptionDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de nodos de los �tems para armar la lista. No
			 * importa el oid del nodo, sino simplemente su t�tulo.
			 * 
			 * @return una colecci�n con los posibles nodos de �tems.
			 */
			@Override
			public List<WorkflowNodeDescriptionDTO> getObject() {

				TreeSet<WorkflowNodeDescriptionDTO> nodes = new TreeSet<WorkflowNodeDescriptionDTO>(
						new Comparator<WorkflowNodeDescriptionDTO>() {

							/**
							 * Compara dos elementos para ordenarlos y descartar
							 * repetidos. En este caso solamente necesitamos el
							 * t�tulo de los nodos de �tems, asi que no se
							 * aceptan repetidos.
							 * 
							 * @param aNode
							 *            es el primer nodo a comparar.
							 * @param anotherNode
							 *            es el segundo nodo a comparar.
							 * @return un entero que representa el resultado de
							 *         comparar los t�tulos de los nodos.
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
					nodes.addAll(EditItemFilterForm.this.getWorkflowsService()
							.getAllWorkflowNodeDescriptionOfProjects());

					String nodesString = getFilterDTO()
							.getFilterComponentByNode();

					for (WorkflowNodeDescriptionDTO dto : nodes) {
						if (nodesString.contains(getString(dto.getTitle()))) {
							getSelectedNodes().add(dto);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				ArrayList<WorkflowNodeDescriptionDTO> result = new ArrayList<WorkflowNodeDescriptionDTO>();
				result.addAll(nodes);
				return result;
			}

		};

		// crea un renderer espec�fico para nodos de �tems recuperados
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
			 *            es la posici�n en la lista.
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

		// agrega el checkbox para negar esta condici�n
		CheckBox aCheckBox = new CheckBox("negateNodeCheckbox",
				new PropertyModel(this, "negateNode"));
		this.add(aCheckBox);

	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condici�n del nodo; false en caso
	 *         contrario.
	 */
	public boolean isNegateNode() {
		return this.negateNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condici�n del filtro por
	 *            nodo.
	 */
	public void setNegateNode(boolean aBoolean) {
		this.negateNode = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los dtos de los nodos seleccionados
	 *         para filtrar los �tems.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getSelectedNodes() {
		return this.selectedNodes;
	}

	/**
	 * Setter.
	 * 
	 * @param someNodes
	 *            es una colecci�n que contiene los dtos de los nodos
	 *            seleccionados para filtrar los �tems.
	 */
	public void setSelectedNodes(
			Collection<WorkflowNodeDescriptionDTO> someNodes) {
		this.selectedNodes = someNodes;
	}

	/**
	 * Crea el componente para listar todos los tipos de �tems.<br>
	 * 
	 * @return el componente creado para mostrar los tipos de �tems.
	 */
	@SuppressWarnings("unchecked")
	private void createItemTypesListComponent() {

		this.setSelectedItemTypes(new ArrayList<ItemTypeDTO>());
		this.setNegateItemType(this.getFilterDTO().isNegateItemType());

		// crea un modelo para acceder a la lista de tipos de los �tems.
		IModel<List<ItemTypeDTO>> typesChoices = new AbstractReadOnlyModel<List<ItemTypeDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de tipos de los �tems para armar la lista.
			 * 
			 * @return una colecci�n con los posibles tipos de �tems.
			 */
			@Override
			public List<ItemTypeDTO> getObject() {

				TreeSet<ItemTypeDTO> types = new TreeSet<ItemTypeDTO>(
						new Comparator<ItemTypeDTO>() {

							/**
							 * Compara dos elementos para ordenarlos y descartar
							 * repetidos. En este caso solamente necesitamos el
							 * nombre de los tipos de �tems, asi que no se
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
					types.addAll(EditItemFilterForm.this.getItemsService()
							.getAllItemTypesOfProjects());

					String typesString = getFilterDTO()
							.getFilterComponentByItemType();

					for (ItemTypeDTO dto : types) {
						if (typesString.contains(dto.getTitle())) {
							getSelectedItemTypes().add(dto);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				ArrayList<ItemTypeDTO> result = new ArrayList<ItemTypeDTO>();
				result.addAll(types);
				return result;
			}

		};

		// crea un renderer espec�fico para tipos de �tems recuperados
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
			 *            es la posici�n en la lista.
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

		// agrega el checkbox para negar esta condici�n
		CheckBox aCheckBox = new CheckBox("negateItemTypeCheckbox",
				new PropertyModel(this, "negateItemType"));
		this.add(aCheckBox);

	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condici�n del tipo; false en caso
	 *         contrario.
	 */
	public boolean isNegateItemType() {
		return this.negateItemType;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si se debe negar o no la condici�n del filtro por
	 *            tipo del �tem.
	 */
	public void setNegateItemType(boolean aBoolean) {
		this.negateItemType = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return los tipos de �tems seleccionados para filtrar.
	 */
	public Collection<ItemTypeDTO> getSelectedItemTypes() {
		return this.selectedItemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            son los tipos de �tems seleccionados para filtrar.
	 */
	public void setSelectedItemTypes(Collection<ItemTypeDTO> aCollection) {
		this.selectedItemTypes = aCollection;
	}

	/**
	 * Agrega un link que permite enviar los datos editados del filtro.
	 */
	public void addSubmitLink() {
		SubmitLink aLink = new SubmitLink("viewLink") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Env�a el formulario para ver el resultado del filtro creado.
			 */
			@Override
			public void onSubmit() {

				try {
					// al crear un nuevo filtro por defecto se le asigna el oid
					// 0 para
					// identificar que no est� guardado.
					ItemsServiceBI service = EditItemFilterForm.this
							.getItemsService();

					FilterDTO result = service.updateFilter(getFilterOid(),
							getSelectedFilterComponentByProjects(),
							getSelectedItemStates(), getSelectedResponsibles(),
							getSelectedItemTypes(), getSelectedNodes(),
							isNegateResponsible(), isNegateItemState(),
							isNegateItemType(), isNegateProject(),
							isNegateNode(), getFreeText());

					PageParameters parameters = new PageParameters();
					parameters.put("FILTER_OID", result.getOid());
					this.setResponsePage(ViewItemsPage.class, parameters);

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
	 * Getter.
	 * 
	 * @return el texto libre por el cual se debe filtrar los �tems.
	 */
	public String getFreeText() {
		return this.freeText;
	}

	/**
	 * Setter.
	 * 
	 * @param aText
	 *            es el texto libre por el cual se debe filtrar los �tems.
	 */
	public void setFreeText(String aText) {
		this.freeText = aText;
	}

	/**
	 * Crea el componente que permite cargar el texto libre por el cual se
	 * filtrar�n los �tems.
	 * 
	 */
	private void createFreeTextComponent() {
		this.setFreeText(this.getFilterDTO().getFilterComponentByText());
		// construye el input field para el texto
		final TextField<String> freeTextField = new TextField<String>(
				"freeText", new PropertyModel<String>(this, "freeText"));

		this.add(freeTextField);

	}

}
