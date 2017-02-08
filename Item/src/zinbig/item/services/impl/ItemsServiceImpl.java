/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import zinbig.item.model.Item;
import zinbig.item.model.ItemFile;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.model.ItemType;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.FilterUnknownException;
import zinbig.item.model.exceptions.ItemClosedException;
import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.ItemEditionException;
import zinbig.item.model.exceptions.ItemTypeUnknownException;
import zinbig.item.model.exceptions.ItemUnknownException;
import zinbig.item.model.exceptions.ProjectUnknownException;
import zinbig.item.model.filters.ConcreteFilterComponentByItemId;
import zinbig.item.model.filters.ConcreteFilterComponentByItemType;
import zinbig.item.model.filters.ConcreteFilterComponentByNode;
import zinbig.item.model.filters.ConcreteFilterComponentByResponsible;
import zinbig.item.model.filters.ConcreteFilterComponentByState;
import zinbig.item.model.filters.ConcreteFilterComponentByText;
import zinbig.item.model.filters.Filter;
import zinbig.item.model.filters.FilterComponentByItemId;
import zinbig.item.model.filters.FilterComponentByItemType;
import zinbig.item.model.filters.FilterComponentByNode;
import zinbig.item.model.filters.FilterComponentByProject;
import zinbig.item.model.filters.FilterComponentByProjectForRegisteredUser;
import zinbig.item.model.filters.FilterComponentByProjectForSelectedProject;
import zinbig.item.model.filters.FilterComponentByProjectForUnregisteredUser;
import zinbig.item.model.filters.FilterComponentByResponsible;
import zinbig.item.model.filters.FilterComponentByState;
import zinbig.item.model.filters.FilterComponentByText;
import zinbig.item.model.filters.FilterStringCreationStrategy;
import zinbig.item.model.filters.NullFilterComponentByItemId;
import zinbig.item.model.filters.NullFilterComponentByItemType;
import zinbig.item.model.filters.NullFilterComponentByNode;
import zinbig.item.model.filters.NullFilterComponentByResponsible;
import zinbig.item.model.filters.NullFilterComponentByState;
import zinbig.item.model.filters.NullFilterComponentByText;
import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.Project;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.workflow.WorkflowNodeDescription;
import zinbig.item.repositories.RepositoryLocator;
import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.EmailServiceBI;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.CommentDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTO;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemFileDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PriorityDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para acceder a la l�gica de negocios
 * relacionada con los �tems administrados por el sistema.<br>
 * La l�gica de negocios propiamente dicha no se encuentra en los servicios sino
 * que est� definida en los objetos de modelo.<br>
 * 
 * Los colaboradores de esta clase se inyectan mediante IoC.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemsServiceImpl extends BaseServiceImpl implements ItemsServiceBI {

	/**
	 * Mantiene la estrategia seleccionada para crear el string de un filtro de
	 * items. Este colaborador se inyecta a trav�s de Spring.
	 */
	protected FilterStringCreationStrategy filterStringCreationStrategy;

	/**
	 * Agrega un nuevo �tem al sistema.
	 * 
	 * @param aProjectDTO
	 *            es el proyecto en el cual se est� agregando el �tem.
	 * @param aTitle
	 *            es el t�tulo del nuevo �tem.
	 * @param aDescription
	 *            es un texto descriptivo del �tem.
	 * @param aDate
	 *            es la fecha de creaci�n del �tem.
	 * @param anUserDTO
	 *            es el usuario que ha creado este �tem.
	 * @param aPriorityDTO
	 *            es la prioridad del �tem.
	 * @param aState
	 *            es el estado del item. Puede ser CREADO o ABIERTO.
	 * @param responsible
	 *            es el dto que representa al usuario o grupo de usuarios que
	 *            ser� responsable del nuevo �tem.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo del nuevo �tem.
	 * @param someItemIds
	 *            es una colecci�n que contiene los identificadores de los �tems
	 *            que se deben unificar al dar de alta este nuevo �tem.
	 * @param aComment
	 *            es el comentario que se debe agregar a cada �tem unificado
	 *            para dejar asentado que se lo ha unificado.
	 * @param somePropertyDescriptions
	 *            es un diccionario que contiene los valores asignados a cada
	 *            una de las propiedades adicionales del nuevo �tem.
	 * 
	 * @return un dto que representa al nuevo �tem recientemente creado.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de que ocurra un
	 *             error con el alta del �tem.
	 */
	public ItemDTO addItem(ProjectDTO aProjectDTO, String aTitle,
			String aDescription, Date aDate, UserDTO anUserDTO,
			PriorityDTO aPriorityDTO, ItemStateEnum aState,
			AbstractUserDTO responsible, ItemTypeDTO anItemTypeDTO,
			Collection<String> someItemIds, String aComment,
			Map<String, String> somePropertyDescriptions) throws Exception {

		AbstractUser responsibleUser = null;

		// recupera todos los objetos del modelo representados por sus dtos
		Tracker aTracker = this.getTrackerRepository().findTracker();
		Project aProject = this.getProjectsRepository().findById(
				aProjectDTO.getOid());
		Priority aPriority = this.getPrioritiesRepository().findPriorityById(
				aPriorityDTO.getOid());
		User anUser = this.getUsersRepository().findUserWithUsername(aTracker,
				anUserDTO.getUsername(), "C");
		if (responsible != null) {
			responsibleUser = this.getUsersRepository().find(aTracker,
					responsible.getOid());
		}
		ItemType anItemType = this.getItemsRepository().findItemTypeById(
				anItemTypeDTO.getOid());

		Item anItem = aTracker.addItem(anUser, aProject, aTitle, aDescription,
				aPriority, aState, aDate, responsibleUser, anItemType,
				somePropertyDescriptions);

		EmailServiceBI emailService = ServiceLocator.getInstance()
				.getEmailService();
		if (!someItemIds.isEmpty()) {
			// verifica si al dar de alta el nuevo �tem no se deben unificar
			// otros �tems seleccionados.
			Collection<Item> itemsToAggregate = this.itemsRepository
					.findItemsById(someItemIds);
			aProject.aggregateItems(itemsToAggregate, anItem, aComment);

			Collection<ItemDTO> itemDTOs = this.getDtoFactory()
					.createDTOForItems(itemsToAggregate);
			for (ItemDTO dto : itemDTOs) {
				emailService.sendEmailForItemToUser(dto, dto.getCreator());
				emailService.sendEmailForItemToUser(dto, dto.getResponsible());
			}
		}
		ItemDTO newItemDTO = this.getDtoFactory().createDTOForItem(anItem);

		// notifica al creador del �tem
		emailService
				.sendEmailForItemToUser(newItemDTO, newItemDTO.getCreator());
		if (newItemDTO.getResponsible() != null) {
			emailService.sendEmailForItemToUser(newItemDTO, newItemDTO
					.getResponsible());
		}

		return newItemDTO;
	}

	/**
	 * Getter.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario que est� solicitando el
	 *            listado de los �tems. En caso de que sea un usuario an�nimo
	 *            este par�metro ser� nulo y por lo tanto se listar�n solamente
	 *            los �tems pertenecientes a proyectos p�blicos. En caso de que
	 *            no sea nulo entonces se listar�n adem�s los �tems
	 *            pertencientes a los proyectos en los cuales el usuario est�
	 *            asignado.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto actual. Este par�metro
	 *            podr�a ser nulo en caso de no haber ingresado previamente a un
	 *            proyecto en particular.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que se debe aplicar a los
	 *            �tems.
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return la colecci�n de dtos de los �tems del sistema empezando por el
	 *         �ndice recibido y devolviendo �nicamente la cantidad especificada
	 *         de elementos.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	@Override
	public Collection<ItemDTO> getItems(UserDTO anUserDTO,
			ProjectDTO aProjectDTO, FilterDTO aFilterDTO, int index, int count,
			String aPropertyName, String anOrdering) throws Exception {

		Collection<Item> items = new ArrayList<Item>();

		User anUser = null;

		if (anUserDTO != null) {
			anUser = RepositoryLocator.getInstance().getUsersRepository()
					.findUserWithUsername(null, anUserDTO.getUsername(), "C");
		}

		Project aProject = null;
		if (aProjectDTO != null) {

			aProject = RepositoryLocator.getInstance().getProjectsRepository()
					.findById(aProjectDTO.getOid());

		}

		// agrega los �tems p�blicos
		items.addAll(this.getItemsRepository().findItems(anUser, aProject,
				aFilterDTO.getFilterString(), index, count, aPropertyName,
				anOrdering));

		Collection<ItemDTO> result = this.getDtoFactory().createDTOForItems(
				items);

		return result;
	}

	/**
	 * Obtiene la cantidad de �tems para el listado.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario. Este par�metro puede ser
	 *            nulo, en cuyo caso solamente se contar�n los �tems
	 *            pertenecientes a los proyectos p�blicos. Si no es nulo
	 *            entonces tambi�n se considerar�n los �tems pertenecientes a
	 *            los proyectos en los cuales est� asociado el usuario.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto actual. Este par�metro
	 *            podr�a ser nulo en caso de no haber ingresado previamente a un
	 *            proyecto en particular.
	 * @param aFilterDTO
	 *            es el dto que representa la filtro que se debe aplicar a los
	 *            items.
	 * @return el n�mero de �tems.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public int getItemsCount(UserDTO userDTO, ProjectDTO aProjectDTO,
			FilterDTO aFilterDTO) throws Exception {
		User anUser = null;
		if (userDTO != null) {

			anUser = RepositoryLocator.getInstance().getUsersRepository()
					.findUserWithUsername(null, userDTO.getUsername(), "C");

		}

		Project aProject = null;
		if (aProjectDTO != null) {

			aProject = RepositoryLocator.getInstance().getProjectsRepository()
					.findById(aProjectDTO.getOid());

		}

		return this.getItemsRepository().getItemsCount(anUser, aProject,
				aFilterDTO.getFilterString());
	}

	/**
	 * Crea un filtro a partir de la informaci�n seleccionada por el usuario.<BR>
	 * Este m�todo carga en el filtro los datos recibidos para filtrar siempre
	 * como strings, de modo de optimizar su almacenamiento. Adem�s, con el
	 * mimso fin, crea una jerarqu�a de componentes de filtros, los que se
	 * instancian esta �nica vez y no son persistentes, para poder crear el
	 * string de filtrado que se requiere en las consultas. El filtro finalmente
	 * almacena este filtro como string tambi�n, no los componentes que los
	 * crearon.
	 * 
	 * @param anUserDTO
	 *            es el dto del usuario para el cual se est� creando este
	 *            filtro. Este par�metro puede ser nulo en caso de que no exista
	 *            usuario en la sesi�n.
	 * @param aFilterName
	 *            es el nombre para el nuevo filtro.
	 * @param someFilterComponentByProjects
	 *            es el componente que filtra los �tems a partir de sus
	 *            proyectos.
	 * @param aFilterComponentByItemId
	 *            es el componente que filtra a partir del id del �tem.
	 * @param itemStates
	 *            define los estados del �tems por los cuales se debe filtrar.
	 * @param responsibles
	 *            establece por cuales responsables se debe filtrar la colecci�n
	 *            de �tems.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los t�tulos de los tipos por los
	 *            cuales hay que filtrar los �tems.
	 * @param someNodeDescriptions
	 *            es una colecci�n que contiene los t�tulos de las descripciones
	 *            de nodo por los cuales hay que filtrar los �tems.
	 * @param negateResponsibles
	 *            establece si se debe negar la condici�n de filtro por
	 *            responsables.
	 * @param negateItemStates
	 *            establece si se debe negar la condici�n de filtro por estados
	 *            de los �tems.
	 * @param negateItemTypes
	 *            establece si se debe negar la condici�n de filtro por tipos de
	 *            los �tems.
	 * @param negateProjects
	 *            establece si se debe negar la condici�n de filtro por
	 *            proyectos de los �tems.
	 * @param negateNodes
	 *            establece si se debe negar la condici�n de filtro por nodo de
	 *            los �tems.
	 * @param aText
	 *            es el texto por el cual se debe filtrar los �tems.
	 * @return un dto que representa al nuevo filtro.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	@Override
	public FilterDTO createFilter(
			UserDTO anUserDTO,
			String aFilterName,
			Collection<FilterComponentByProjectDTO> someFilterComponentByProjects,
			String aFilterComponentByItemId,
			Collection<ItemStateEnum> itemStates,
			Collection<AbstractUserDTO> responsibles,
			Collection<ItemTypeDTO> someItemTypes,
			Collection<WorkflowNodeDescriptionDTO> someNodeDescriptions,
			boolean negateResponsibles, boolean negateItemStates,
			boolean negateItemTypes, boolean negateProjects,
			boolean negateNodes, String aText) throws Exception {

		Collection<String> aux = null;

		Filter aFilter = new Filter(aFilterName);

		// crea el componente encargado de filtrar los items por proyecto.
		FilterComponentByProject filterComponentByProject;
		aux = new ArrayList<String>();
		if (someFilterComponentByProjects.isEmpty()) {
			// se ha seleccionado la opci�n "cualquiera". Hay que ver si hay
			// usuario o no.
			if (anUserDTO == null) {
				// no hay usuario en la sesi�n
				filterComponentByProject = new FilterComponentByProjectForUnregisteredUser();
			} else {
				filterComponentByProject = new FilterComponentByProjectForRegisteredUser();
			}
		} else {

			for (FilterComponentByProjectDTO dto : someFilterComponentByProjects) {
				aux.add("'" + dto.getProjectOid().toString() + "'");
			}
			filterComponentByProject = new FilterComponentByProjectForSelectedProject(
					aux, negateProjects);
		}
		aFilter.setFilterComponentByProject(aux.toString().replace("[", "(")
				.replace("]", ")"));

		// crea el componente responsable de filtrar los items por su estado.
		FilterComponentByState aFilterComponentByState;
		if (itemStates.isEmpty()) {
			aFilterComponentByState = new NullFilterComponentByState();
			aFilter.setFilterComponentByState("");
		} else {
			aux = new ArrayList<String>();
			for (ItemStateEnum e : itemStates) {
				aux.add("'" + e.toInt().toString() + "'");
			}
			aFilterComponentByState = new ConcreteFilterComponentByState(aux,
					negateItemStates);
			aFilter.setFilterComponentByState(aux.toString().replace("[", "")
					.replace("]", ""));
		}

		// crea el componente que filtra los items por sus
		// responsables.
		FilterComponentByResponsible aFilterComponentByResponsible;
		if (responsibles.isEmpty()) {
			aFilterComponentByResponsible = new NullFilterComponentByResponsible();
			aFilter.setFilterComponentByResponsible("");
		} else {
			aux = new ArrayList<String>();
			for (AbstractUserDTO dto : responsibles) {
				aux.add("'" + dto.getOid() + "'");
			}
			aFilterComponentByResponsible = new ConcreteFilterComponentByResponsible(
					aux, negateResponsibles);
			aFilter.setFilterComponentByResponsible(aux.toString().replace("[",
					"").replace("]", ""));
		}

		// crea el componente responsable de filtrar los items por su
		// tipo.
		FilterComponentByItemType aFilterComponentByItemType;
		if (someItemTypes.isEmpty()) {
			aFilterComponentByItemType = new NullFilterComponentByItemType();
			aFilter.setFilterComponentByItemType("");
		} else {
			aux = new ArrayList<String>();
			for (ItemTypeDTO dto : someItemTypes) {
				aux.add("'" + dto.getTitle() + "'");
			}
			aFilterComponentByItemType = new ConcreteFilterComponentByItemType(
					aux, negateItemTypes);
			aFilter.setFilterComponentByItemType(aux.toString()
					.replace("[", "").replace("]", ""));
		}

		// crea el componente responsable de filtrar los items por su
		// nodo de workflow.
		FilterComponentByNode aFilterComponentByNode;
		if (someNodeDescriptions.isEmpty()) {
			aFilterComponentByNode = new NullFilterComponentByNode();
			aFilter.setFilterComponentByNode("");
		} else {
			aux = new ArrayList<String>();
			for (WorkflowNodeDescriptionDTO dto : someNodeDescriptions) {
				aux.add("'" + dto.getTitle() + "'");
			}
			aFilterComponentByNode = new ConcreteFilterComponentByNode(aux,
					negateNodes);
			aFilter.setFilterComponentByNode(aux.toString().replace("[", "")
					.replace("]", ""));
		}

		// crea el componente que filtra los items por su texto.
		FilterComponentByText aFilterComponentByText;
		if (aText == null || aText.equals("")) {
			aFilterComponentByText = new NullFilterComponentByText();
			aFilter.setFilterComponentByText("");
		} else {

			aFilterComponentByText = new ConcreteFilterComponentByText(aText);
			aFilter.setFilterComponentByText(aText);
		}

		// crea el componente responsable de filtrar los items por su id.
		FilterComponentByItemId aFilterComponentById;
		if (aFilterComponentByItemId.equals("")) {
			aFilterComponentById = new NullFilterComponentByItemId();
		} else {
			aFilterComponentById = new ConcreteFilterComponentByItemId(
					aFilterComponentByItemId);

		}
		aFilter.setFilterComponentByItemId(aFilterComponentByItemId);

		this.getFilterStringCreationStrategy().createFilterString(aFilter,
				filterComponentByProject, aFilterComponentByState,
				aFilterComponentById, aFilterComponentByResponsible,
				aFilterComponentByItemType, aFilterComponentByNode,
				aFilterComponentByText);

		// "" significa que todav�a no se ha guardado este filtro.
		FilterDTO aFilterDTO = new FilterDTO("", aFilterName, false);
		aFilterDTO.setFilterString(aFilter.getFilterString());
		aFilterDTO.setFilterComponentByProject(aFilter
				.getFilterComponentByProject());
		aFilterDTO.setNegateProject(negateProjects);
		aFilterDTO.setFilterComponentByState(aFilter
				.getFilterComponentByState());
		aFilterDTO.setNegateState(negateItemStates);
		aFilterDTO.setFilterComponentByResponsible(aFilter
				.getFilterComponentByResponsible());
		aFilterDTO.setNegateResponsible(negateResponsibles);
		aFilterDTO.setFilterComponentByItemType(aFilter
				.getFilterComponentByItemType());
		aFilterDTO.setNegateItemType(negateItemTypes);
		aFilterDTO.setFilterComponentByNode(aFilter.getFilterComponentByNode());
		aFilterDTO.setNegateNode(negateNodes);
		aFilterDTO.setFilterComponentByItemId(aFilter
				.getFilterComponentByItemId());
		aFilterDTO.setFilterComponentByText(aFilter.getFilterComponentByText());
		return aFilterDTO;
	}

	/**
	 * Edita un filtro cuyo oid se recibe.
	 * 
	 * 
	 * @param aFilterOid
	 *            es el oid del filtro que se debe editar.
	 * @param someFilterComponentByProjects
	 *            es el componente que filtra los �tems a partir de sus
	 *            proyectos.
	 * @param itemStates
	 *            define los estados del �tems por los cuales se debe filtrar.
	 * @param responsibles
	 *            establece por cuales responsables se debe filtrar la colecci�n
	 *            de �tems.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los t�tulos de los tipos por los
	 *            cuales hay que filtrar los �tems.
	 * @param someNodeDescriptions
	 *            es una colecci�n que contiene los t�tulos de las descripciones
	 *            de nodo por los cuales hay que filtrar los �tems.
	 * @param negateResponsibles
	 *            establece si se debe negar la condici�n de filtro por
	 *            responsables.
	 * @param negateItemStates
	 *            establece si se debe negar la condici�n de filtro por estado
	 *            de los �tems.
	 * @param negateItemTypes
	 *            establece si se debe negar la condici�n de filtro por tipo de
	 *            los �tems.
	 * @param negateProjects
	 *            establece si se debe negar la condici�n de filtro por proyecto
	 *            de los �tems.
	 * @param negateNodes
	 *            establece si se debe negar la condici�n de filtro por nodo de
	 *            los �tems.
	 * @param aText
	 *            es el texto por el cual se debe filtrar los �tems.
	 * @return un dto que representa al nuevo filtro.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public FilterDTO updateFilter(
			String aFilterOid,
			Collection<FilterComponentByProjectDTO> someFilterComponentByProjects,
			Collection<ItemStateEnum> itemStates,
			Collection<AbstractUserDTO> responsibles,
			Collection<ItemTypeDTO> someItemTypes,
			Collection<WorkflowNodeDescriptionDTO> someNodeDescriptions,
			boolean negateResponsibles, boolean negateItemStates,
			boolean negateItemTypes, boolean negateProjects,
			boolean negateNodes, String aText) throws Exception {
		Collection<String> aux = null;

		ItemsRepositoryBI repository = this.getItemsRepository();
		Filter aFilter = repository.findFilterById(aFilterOid);

		// crea el componente encargado de filtrar los items por proyecto.
		FilterComponentByProject filterComponentByProject;
		aux = new ArrayList<String>();
		if (someFilterComponentByProjects.isEmpty()) {
			// se ha seleccionado la opci�n "cualquiera". Hay que ver si hay
			// usuario o no.
			filterComponentByProject = new FilterComponentByProjectForRegisteredUser();

		} else {

			for (FilterComponentByProjectDTO dto : someFilterComponentByProjects) {
				aux.add("'" + dto.getProjectOid().toString() + "'");
			}
			filterComponentByProject = new FilterComponentByProjectForSelectedProject(
					aux, negateProjects);
		}
		aFilter.setFilterComponentByProject(aux.toString().replace("[", "(")
				.replace("]", ")"));

		// crea el componente responsable de filtrar los items por su estado.
		FilterComponentByState aFilterComponentByState;
		if (itemStates.isEmpty()) {
			aFilterComponentByState = new NullFilterComponentByState();
			aFilter.setFilterComponentByState("");
		} else {
			aux = new ArrayList<String>();
			for (ItemStateEnum e : itemStates) {
				aux.add("'" + e.toInt().toString() + "'");
			}
			aFilterComponentByState = new ConcreteFilterComponentByState(aux,
					negateItemStates);
			aFilter.setFilterComponentByState(aux.toString().replace("[", "")
					.replace("]", ""));
		}

		// crea el componente que filtra los items por sus
		// responsables.
		FilterComponentByResponsible aFilterComponentByResponsible;
		if (responsibles.isEmpty()) {
			aFilterComponentByResponsible = new NullFilterComponentByResponsible();
			aFilter.setFilterComponentByResponsible("");
		} else {
			aux = new ArrayList<String>();
			for (AbstractUserDTO dto : responsibles) {
				aux.add("'" + dto.getOid() + "'");
			}
			aFilterComponentByResponsible = new ConcreteFilterComponentByResponsible(
					aux, negateResponsibles);
			aFilter.setFilterComponentByResponsible(aux.toString().replace("[",
					"").replace("]", ""));
		}

		// crea el componente responsable de filtrar los items por su
		// tipo.
		FilterComponentByItemType aFilterComponentByItemType;
		if (someItemTypes.isEmpty()) {
			aFilterComponentByItemType = new NullFilterComponentByItemType();
			aFilter.setFilterComponentByItemType("");
		} else {
			aux = new ArrayList<String>();
			for (ItemTypeDTO dto : someItemTypes) {
				aux.add("'" + dto.getTitle() + "'");
			}
			aFilterComponentByItemType = new ConcreteFilterComponentByItemType(
					aux, negateItemTypes);
			aFilter.setFilterComponentByItemType(aux.toString()
					.replace("[", "").replace("]", ""));
		}

		// crea el componente responsable de filtrar los items por su
		// nodo de workflow.
		FilterComponentByNode aFilterComponentByNode;
		if (someNodeDescriptions.isEmpty()) {
			aFilterComponentByNode = new NullFilterComponentByNode();
			aFilter.setFilterComponentByNode("");
		} else {
			aux = new ArrayList<String>();
			for (WorkflowNodeDescriptionDTO dto : someNodeDescriptions) {
				aux.add("'" + dto.getTitle() + "'");
			}
			aFilterComponentByNode = new ConcreteFilterComponentByNode(aux,
					negateNodes);
			aFilter.setFilterComponentByNode(aux.toString().replace("[", "")
					.replace("]", ""));
		}

		// crea el componente que filtra los items por su texto.
		FilterComponentByText aFilterComponentByText;
		if (aText.equals("")) {
			aFilterComponentByText = new NullFilterComponentByText();
			aFilter.setFilterComponentByText("");
		} else {

			aFilterComponentByText = new ConcreteFilterComponentByText(aText);
			aFilter.setFilterComponentByText(aText);
		}

		// crea el componente responsable de filtrar los items por su id.
		FilterComponentByItemId aFilterComponentById = new NullFilterComponentByItemId();
		aFilter.setFilterComponentByItemId("");

		this.getFilterStringCreationStrategy().createFilterString(aFilter,
				filterComponentByProject, aFilterComponentByState,
				aFilterComponentById, aFilterComponentByResponsible,
				aFilterComponentByItemType, aFilterComponentByNode,
				aFilterComponentByText);
		aFilter.setNegateItemType(negateItemTypes);
		aFilter.setNegateNode(negateNodes);
		aFilter.setNegateProject(negateProjects);
		aFilter.setNegateResponsible(negateResponsibles);
		aFilter.setNegateState(negateItemStates);

		FilterDTO aFilterDTO = new FilterDTO(aFilter.getOid(), aFilter
				.getName(), aFilter.isFavorite());
		aFilterDTO.setFilterString(aFilter.getFilterString());
		aFilterDTO.setFilterComponentByProject(aFilter
				.getFilterComponentByProject());
		aFilterDTO.setNegateProject(aFilter.isNegateProject());
		aFilterDTO.setFilterComponentByState(aFilter
				.getFilterComponentByState());
		aFilterDTO.setNegateState(aFilter.isNegateState());
		aFilterDTO.setFilterComponentByResponsible(aFilter
				.getFilterComponentByResponsible());
		aFilterDTO.setNegateResponsible(aFilter.isNegateResponsible());
		aFilterDTO.setFilterComponentByItemType(aFilter
				.getFilterComponentByItemType());
		aFilterDTO.setNegateItemType(aFilter.isNegateItemType());
		aFilterDTO.setFilterComponentByNode(aFilter.getFilterComponentByNode());
		aFilterDTO.setNegateNode(aFilter.isNegateNode());
		aFilterDTO.setFilterComponentByItemId(aFilter
				.getFilterComponentByItemId());
		aFilterDTO.setFilterComponentByText(aFilter.getFilterComponentByText());

		return aFilterDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return la estrategia de generaci�n del string del filtro de �tems.
	 */
	public FilterStringCreationStrategy getFilterStringCreationStrategy() {
		return this.filterStringCreationStrategy;
	}

	/**
	 * Setter.
	 * 
	 * @param anStrategy
	 *            es la estrategia de generaci�n del string del filtro de �tems.
	 */
	public void setFilterStringCreationStrategy(
			FilterStringCreationStrategy anStrategy) {
		this.filterStringCreationStrategy = anStrategy;
	}

	/**
	 * Obtiene una colecci�n que contiene los filtros favoritos de un usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se deben
	 *            recuperar los filtros favoritos.
	 * @return una colecci�n con DTOs que representan los filtros favoritos del
	 *         usuario.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	@Override
	public Collection<FilterDTO> findFavoritiesItemsFiltersOfUser(
			UserDTO anUserDTO) throws Exception {

		User anUser = null;
		if (anUserDTO != null) {
			Tracker aTracker = this.getTrackerRepository().findTracker();
			anUser = RepositoryLocator.getInstance().getUsersRepository()
					.findUserWithUsername(aTracker, anUserDTO.getUsername(),
							"C");

		}

		Collection<Filter> favorites = this.getItemsRepository()
				.getFavoriteItemsFiltersOfUser(anUser);

		return this.getDtoFactory().createDTOForItemFilters(favorites, true);
	}

	/**
	 * Obtiene una colecci�n que contiene los filtros de un usuario.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se deben
	 *            recuperar los filtros.
	 * @return una colecci�n con DTOs que representan los filtros del usuario.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public Collection<FilterDTO> findItemsFiltersOfUser(UserDTO anUserDTO)
			throws Exception {
		User anUser = null;
		if (anUserDTO != null) {

			anUser = RepositoryLocator.getInstance().getUsersRepository()
					.findUserWithUsername(null, anUserDTO.getUsername(), "C");

		}

		Collection<Filter> favorites = this.getItemsRepository()
				.getItemsFiltersOfUser(anUser);

		return this.getDtoFactory().createDTOForItemFilters(favorites, true);
	}

	/**
	 * Almacena un nuevo filtro para el usuario recibido.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario para el cual se debe
	 *            almacenar el filtro.
	 * @param filterDTO
	 *            es el DTO que representa al nuevo filtro a guardar.
	 * 
	 * @return el DTO del filtro recientemente creado.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	@Override
	public FilterDTO saveItemsFilterForUser(UserDTO anUserDTO,
			FilterDTO filterDTO) throws Exception {
		User anUser = this.getUsersRepository().findUserWithUsername(null,
				anUserDTO.getUsername(), "C");

		Filter aFilter = new Filter(filterDTO.getName());
		aFilter.setFavorite(filterDTO.isFavorite());
		aFilter.setFilterComponentByProject(filterDTO
				.getFilterComponentByProject());
		aFilter
				.setFilterComponentByState(filterDTO
						.getFilterComponentByState());

		aFilter.setFilterComponentByResponsible(filterDTO
				.getFilterComponentByResponsible());

		aFilter
				.setFilterComponentByState(filterDTO
						.getFilterComponentByState());

		aFilter.setFilterComponentByNode(filterDTO.getFilterComponentByNode());
		aFilter.setFilterComponentByItemType(filterDTO
				.getFilterComponentByItemType());
		aFilter.setFilterString(filterDTO.getFilterString());

		aFilter.setNegateItemType(filterDTO.isNegateItemType());
		aFilter.setNegateNode(filterDTO.isNegateNode());
		aFilter.setNegateProject(filterDTO.isNegateProject());
		aFilter.setNegateResponsible(filterDTO.isNegateResponsible());
		aFilter.setNegateState(filterDTO.isNegateState());
		aFilter.setFilterComponentByText(filterDTO.getFilterComponentByText());
		anUser.addFilter(aFilter);

		filterDTO.setOid(aFilter.getOid());

		return filterDTO;

	}

	/**
	 * Finder.
	 * 
	 * @param filterId
	 *            es el id del filtro que se est� buscando.
	 * @return un dto que representa al filtro que se estaba buscando.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	@Override
	public FilterDTO findItemFilterById(String filterId) throws Exception {
		Filter aFilter = this.getItemsRepository().findFilterById(filterId);
		return this.getDtoFactory().createDTOForItemFilter(aFilter, true);
	}

	/**
	 * Verifica si existe un filtro con el nombre dado para el usuario recibido.
	 * 
	 * @param aName
	 *            es el nombre del filtro que se debe buscar.
	 * @param anUserDTO
	 *            es un dto que representa al usuario en el que se debe buscar
	 *            el filtro.
	 * @return true en caso de que el usuario ya tenga un filtro con el nombre
	 *         dado, false en caso contrario.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public boolean containsFilterWithNameForUser(String aName, UserDTO anUserDTO)
			throws Exception {

		boolean result = false;

		try {
			Tracker aTracker = this.getTrackerRepository().findTracker();
			User anUser = this.getUsersRepository().findUserWithUsername(
					aTracker, anUserDTO.getUsername(), "C");
			Filter aFilter = this.getItemsRepository().findFilterByNameForUser(
					aName, anUser);
			result = aFilter != null;
		} catch (FilterUnknownException fue) {

		}
		return result;

	}

	/**
	 * Actualiza un filtro.
	 * 
	 * @param filterDTO
	 *            es el DTO que representa al nuevo filtro a guardar.
	 * @param anUserDTO
	 *            es el DTO que representa al usuario.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public void updateItemsFilter(FilterDTO filterDTO, UserDTO anUserDTO)
			throws Exception {

		User anUser = this.getUsersRepository().findUserWithUsername(null,
				anUserDTO.getUsername(), "C");
		Filter aFilter = this.getItemsRepository().findFilterByNameForUser(
				filterDTO.getName(), anUser);

		aFilter.setFavorite(filterDTO.isFavorite());
		aFilter.setName(filterDTO.getName());
	}

	/**
	 * Finder.
	 * 
	 * @param anOid
	 *            es el identificador del item que debe recuperarse.
	 * @return el �tem identificado por el oid recibido.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public ItemDTO findItemByOid(String anOid) throws Exception {
		Item anItem = (Item) this.getItemsRepository().findById(anOid);

		return this.getDtoFactory().createDTOForItem(anItem);
	}

	/**
	 * Actualiza los datos b�sicos de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa la item que se est� editando.
	 * @param aTitle
	 *            es el nuevo t�tulo de este �tem.
	 * @param aDescription
	 *            es la nueva descripci�n de este �tem.
	 * @param aPriorityDTO
	 *            es la nueva prioridad de este �tem.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de �tem seleccionado.
	 * 
	 * @return una nueva versi�n del dto que representa la �tem.
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public ItemDTO editItem(ItemDTO anItemDTO, String aTitle,
			String aDescription, PriorityDTO aPriorityDTO,
			ItemTypeDTO anItemTypeDTO) throws Exception {

		try {
			Item item = (Item) this.getItemsRepository().findById(
					anItemDTO.getOid());
			// controla que se est� editando la �ltima versi�n del objeto
			if (!this.verifyLatestVersion(item, anItemDTO)) {

				throw new ItemConcurrentModificationException();

			} else {
				if (item.getState().equals(ItemStateEnum.CREATED)
						| item.getState().equals(ItemStateEnum.OPEN)) {
					Priority aPriority = this.getPrioritiesRepository()
							.findPriorityById(aPriorityDTO.getOid());
					ItemType anItemType = this.getItemsRepository()
							.findItemTypeById(anItemTypeDTO.getOid());

					item.getProject().updateItem(item, aTitle, aDescription,
							aPriority, anItemType);

					ItemDTO newItemDTO = this.getDtoFactory().createDTOForItem(
							item);
					newItemDTO.setVersion(item.getVersion() + 1);
					return newItemDTO;
				} else {
					throw new ItemClosedException();
				}
			}
		} catch (ItemUnknownException pue) {

			pue.printStackTrace();
			throw new ItemEditionException();

		}

	}

	/**
	 * Verifica si un usuario puede ser seguidor de un �tem.
	 * 
	 * @param anAbstractUserDTO
	 *            es el dto que representa la usuario.
	 * @param anItemDTO
	 *            es el dto que representa al �tem.
	 * @return true en caso de que el usuario no sea ya un observador de este
	 *         �tem.
	 * 
	 * @throws Exception
	 *             esta excepci�n puede levantarse en caso de alg�n error al
	 *             ejecutar este servicio.
	 */
	public boolean isUserObserverOfItem(AbstractUserDTO anAbstractUserDTO,
			ItemDTO anItemDTO) throws Exception {
		boolean result = this.getItemsRepository().isUserObserverOfItem(
				anAbstractUserDTO.getOid(), anItemDTO.getOid());

		return result;
	}

	/**
	 * Agrega al usuario o grupo de usuarios como observador del �tem.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario o grupo de usuarios
	 *            observadores del �tem.
	 * @param anItemDTO
	 *            es el �tem observado.
	 * @throws Exception
	 */
	public void addObserverToItem(AbstractUserDTO userDTO, ItemDTO anItemDTO)
			throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();
		AbstractUser anUser = this.getUsersRepository().find(aTracker,
				userDTO.getOid());
		Item anItem = (Item) this.getItemsRepository().findById(
				anItemDTO.getOid());

		anItem.addObserver(anUser);

	}

	/**
	 * El usuario ha decidido tomar este �tem bajo su responsabilidad. <br>
	 * Este mensaje se utiliza cuando un grupo de usuarios es responsable de un
	 * �tem y uno de los usuarios de dicho grupo ha tomado la responsabilidad
	 * del �tem.
	 * 
	 * @param userDTO
	 *            es el DTO que representa al usuario que ser� el nuevo
	 *            responsable.
	 * @param anItemDTO
	 *            es el DTO que representa al �tem en cuesti�n.
	 * @return un dto que representa al �tem.
	 * 
	 * @throws Exception
	 */
	public ItemDTO takeItem(UserDTO userDTO, ItemDTO anItemDTO)
			throws Exception {

		ItemDTO result = null;
		try {
			Tracker aTracker = this.getTrackerRepository().findTracker();
			User anUser = (User) this.getUsersRepository().find(aTracker,
					userDTO.getOid());
			Item anItem = (Item) this.getItemsRepository().findById(
					anItemDTO.getOid());

			Project aProject = anItem.getProject();
			aProject.userWantsToTakeItem(anUser, anItem);

			result = this.getDtoFactory().createDTOForItem(anItem);

			EmailServiceBI emailService = ServiceLocator.getInstance()
					.getEmailService();
			emailService.sendEmailForItemToUser(result, userDTO);
			emailService.sendEmailForItemToUser(result, result.getCreator());

			Collection<UserDTO> observers = this.getObserversOfItem(result);
			for (UserDTO observer : observers) {
				emailService.sendEmailForItemToUser(result, observer);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Retorna los comentarios de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem.
	 * @return una colecci�n que contiene los dtos de los comentarios.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este m�todo.
	 */
	public Collection<CommentDTO> getCommentsOfItem(ItemDTO anItemDTO)
			throws Exception {
		TreeSet<CommentDTO> result = new TreeSet<CommentDTO>(
				new Comparator<CommentDTO>() {

					@Override
					public int compare(CommentDTO aComment,
							CommentDTO anotherComment) {
						int result = 0;
						try {
							result = -1
									* new SimpleDateFormat()
											.parse(aComment.getCreationDate())
											.compareTo(
													new SimpleDateFormat()
															.parse(anotherComment
																	.getCreationDate()));
						} catch (ParseException e) {

						}
						return result;
					}
				});
		Item anItem = (Item) this.getItemsRepository().findById(
				anItemDTO.getOid());
		result.addAll(this.getDtoFactory().createDTOsForComments(
				anItem.getComments()));

		return result;

	}

	/**
	 * Agrega un nuevo comentario al �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem al que se debe agregar el
	 *            nuevo comentario.
	 * @param aCommentText
	 *            es el texto del nuevo comentario.
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * @return el dto del �tem modificado.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse por la ejecuci�n
	 *             de este servicio.
	 */
	public ItemDTO addCommentToItem(ItemDTO anItemDTO, String aCommentText,
			UserDTO anUserDTO) throws Exception {

		Item anItem = (Item) this.getItemsRepository().findById(
				anItemDTO.getOid());

		// controla que se est� editando la �ltima versi�n del objeto
		if (!this.verifyLatestVersion(anItem, anItemDTO)) {

			throw new ItemConcurrentModificationException();

		} else {

			User anUser = this.getUsersRepository().findUserWithUsername(null,
					anUserDTO.getUsername(), "C");

			anItem.addComment(aCommentText, anUser);
			// incrementa en uno la versi�n del �tem
			anItemDTO.setVersion(anItem.getVersion() + 1);
			return anItemDTO;
		}
	}

	/**
	 * Retorna DTOs de todos los tipos de �tems de un proyecto dado.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto.
	 * @param aPropertyName
	 *            establece por cual propiedad se deber�a devolver ordenado el
	 *            resultado.
	 * @param anOrdering
	 *            establece si el orden es ascendente o no.
	 * @return una colecci�n que contiene los dtos de todos los tipos de �tems
	 *         del proyecto.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse por la ejecuci�n
	 *             de este servicio.
	 */
	public Collection<ItemTypeDTO> findItemTypesOfProject(
			ProjectDTO aProjectDTO, String aPropertyName, String anOrdering)
			throws Exception {
		Collection<ItemType> itemTypes = new ArrayList<ItemType>();
		Collection<ItemTypeDTO> result = new ArrayList<ItemTypeDTO>();

		itemTypes.addAll(this.getItemsRepository().findItemTypesOfProject(
				aProjectDTO, aPropertyName, anOrdering));

		result.addAll(this.getDtoFactory().createDTOForItemTypes(itemTypes));

		return result;

	}

	/**
	 * Getter.
	 * 
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return la colecci�n de dtos de los tipos de �tems del sistema empezando
	 *         por el �ndice recibido y devolviendo �nicamente la cantidad
	 *         especificada de elementos.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	@Override
	public Collection<ItemTypeDTO> getItemTypes(int index, int count,
			String aColumnName, String anOrdering) throws Exception {

		Collection<ItemType> itemTypes = this.getItemsRepository()
				.findItemTypes(index, count, aColumnName, anOrdering);

		return this.getDtoFactory().createDTOForItemTypes(itemTypes);
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de tipos de �tems que tiene el sistema.
	 */
	@Override
	public int getItemTypesCount() throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();

		return aTracker.getItemTypes().size();
	}

	/**
	 * Finder.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems disponibles.
	 */
	public Collection<ItemTypeDTO> getAllItemTypes() throws Exception {
		int count = this.getItemTypesCount();
		return this.getItemTypes(0, count, "title", "ASC");
	}

	/**
	 * Agrega un nuevo tipo de �tem al sistema.
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo tipo de �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void addItemType(String aTitle) throws Exception {
		Tracker aTracker = this.getTrackerRepository().findTracker();

		aTracker.addItemType(aTitle);
	}

	/**
	 * Verifica si existe un tipo de �tem con un t�tulo dado.
	 * 
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que exista el tipo de �tem; false en caso
	 *         contrario.
	 */
	public boolean existsItemTypeWithTitle(String aTitle) {
		ItemsRepositoryBI aRepository = this.getItemsRepository();

		return aRepository.containsItemTypeWithTitle(null, aTitle);
	}

	/**
	 * Verifica si existe un tipo de �tem con un t�tulo dado, pero dentro de un
	 * proyecto determinado.
	 * 
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto en el cual se debe buscar
	 *            el tipo de �tem.
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @return true en caso de que exista el tipo de �tem; false en caso
	 *         contrario.
	 */
	public boolean existsItemTypeWithTitleInProject(ProjectDTO aProjectDTO,
			String aTitle) {
		boolean result = false;

		ItemsRepositoryBI aRepository = this.getItemsRepository();
		ProjectsRepositoryBI projectsRepository = this.getProjectsRepository();

		Project aProject;
		try {
			aProject = projectsRepository.findById(aProjectDTO.getOid());
			result = aRepository.containsItemTypeWithTitleInProject(aProject,
					aTitle);
		} catch (ProjectUnknownException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Finder.
	 * 
	 * @param anItemTypeOid
	 *            es el oid del tipo de �tem que se est� tratando de recuperar.
	 * @return un dto que representa al tipo de �tem recuperado.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemTypeDTO findItemTypeByOid(String anItemTypeOid) throws Exception {
		ItemType anItemType = this.getItemsRepository().findItemTypeById(
				anItemTypeOid);

		return this.getDtoFactory().createDTOForItemType(anItemType);
	}

	/**
	 * Edita los datos de un tipo de �tem. Esta edici�n solamente afecta a la
	 * instancia que se est� editando, no as� a todas las copias de la misma.
	 * 
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de �tem que se debe editar.
	 * @param aTitle
	 *            es el nuevo t�tulo del tipo de �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void editItemType(ItemTypeDTO anItemTypeDTO, String aTitle)
			throws Exception {
		try {
			ItemType anItemType = this.getItemsRepository().findItemTypeById(
					anItemTypeDTO.getOid());

			// controla que se est� editando la �ltima versi�n del objeto
			if (!this.verifyLatestVersion(anItemType, anItemTypeDTO)) {

				throw new ItemConcurrentModificationException();

			} else {

				Tracker aTracker = this.getTrackerRepository().findTracker();

				aTracker.editTypeItem(anItemType, aTitle);
			}
		} catch (ItemTypeUnknownException iue) {

			iue.printStackTrace();
			throw new ItemEditionException();

		}
	}

	/**
	 * Borra los tipos de �tems cuyos identificadores se han recibido.
	 * 
	 * @param selectedItemTypes
	 *            es una colecci�n que contiene los identificadores de los tipos
	 *            de �tems que deben ser eliminados.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteItemTypes(Collection<String> selectedItemTypes)
			throws Exception {

		ItemsRepositoryBI repository = this.getItemsRepository();

		// recupera todos los tipos de �tems a partir de sus correspondientes
		// DTOs.
		Collection<ItemType> itemTypes = repository
				.findItemTypeByIds(selectedItemTypes);
		Tracker aTracker = this.getTrackerRepository().findTracker();

		ItemType anItemType = null;
		for (Iterator<ItemType> i = itemTypes.iterator(); i.hasNext();) {
			anItemType = i.next();

			aTracker.deleteItemType(anItemType);
			repository.delete(anItemType);

		}

	}

	/**
	 * Elimina de un proyecto un conjunto de �tems.
	 * 
	 * @param userDTO
	 *            es el dto que representa al usuario que est� tratando de
	 *            eliminar los �tems.
	 * 
	 * @param selectedItems
	 *            es una colecci�n que contiene los identificadores de los �tems
	 *            que se deben eliminar.
	 * 
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteItems(UserDTO userDTO, Collection<String> selectedItems)
			throws Exception {
		// recupera el usuario
		UsersRepositoryBI usersRepository = this.getUsersRepository();
		User anUser = (User) usersRepository.findById(userDTO.getOid());

		ItemsRepositoryBI itemsRepository = this.getItemsRepository();
		Collection<Item> items = itemsRepository.findItemsById(selectedItems);
		Iterator<Item> itemsIterator = items.iterator();
		Item anItem = null;

		Project aProject = null;
		// reparte cada �tem con el proyecto al que pertenece.
		Map<Project, Collection<Item>> map = new HashMap<Project, Collection<Item>>();
		while (itemsIterator.hasNext()) {
			anItem = itemsIterator.next();
			aProject = anItem.getProject();

			if (!map.containsKey(aProject)) {

				map.put(aProject, new ArrayList<Item>());
			}
			map.get(aProject).add(anItem);

		}

		// recorre cada proyecto notific�ndolo para que elimine los �tems que le
		// corresponde
		Collection<Item> deletedItems = null;
		Iterator<Entry<Project, Collection<Item>>> entrySetIterator = map
				.entrySet().iterator();
		Entry<Project, Collection<Item>> entry = null;
		while (entrySetIterator.hasNext()) {
			entry = entrySetIterator.next();
			aProject = entry.getKey();
			deletedItems = aProject.deleteItems(entry.getValue(), anUser);
			itemsRepository.deleteObjects(deletedItems);
		}

	}

	/**
	 * Elimina un filtro de �tems de un usuario particular.
	 * 
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que debe ser eliminado.
	 * @param anUserDTO
	 *            es el dto que representa al usuario.
	 * @return el dto del usuario actualizado.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public UserDTO deleteItemsFilterOfUser(FilterDTO aFilterDTO,
			UserDTO anUserDTO) throws Exception {
		UsersRepositoryBI usersRepository = this.getUsersRepository();
		User anUser = (User) usersRepository.findById(anUserDTO.getOid());

		ItemsRepositoryBI itemsRepository = this.getItemsRepository();
		Filter aFilter = itemsRepository.findFilterById(aFilterDTO.getOid());

		anUser.removeFilter(aFilter);
		itemsRepository.delete(aFilter);

		anUserDTO.removeFilter(aFilterDTO);

		return anUserDTO;
	}

	/**
	 * Agrega un nuevo tipo de �tem a un proyecto dado.
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo tipo de �tem.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto que se est� editando.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void addItemTypeToProject(String aTitle, ProjectDTO aProjectDTO)
			throws Exception {
		Project aProject = this.getProjectsRepository().findById(
				aProjectDTO.getOid());

		aProject.addItemType(aTitle);
	}

	/**
	 * Elimina un tipo de �tem de un proyecto dado.
	 * 
	 * @param projectDTO
	 *            es el dto que representa la proyecto que se est� editando.
	 * @param someItemTypes
	 *            es una colecci�n que contiene los tipos de �tems
	 *            seleccionados.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteItemTypeOfProject(ProjectDTO projectDTO,
			Collection<String> someItemTypes) throws Exception {
		ItemsRepositoryBI itemTypesRepository = this.getItemsRepository();
		Collection<ItemType> itemTypes = itemTypesRepository
				.findItemTypeByIds(someItemTypes);

		ProjectsRepositoryBI projectsRepository = this.getProjectsRepository();
		Project aProject = projectsRepository.findById(projectDTO.getOid());

		aProject.deleteItemTypes(itemTypes);
		projectsRepository.deleteObjects(itemTypes);

	}

	/**
	 * Reasigna un �tem a un nuevo responsable.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al �tem.
	 * @param nextNodeDTO
	 *            es el dto que representa al nuevo estado al que se est�
	 *            pasando el �tem.
	 * @param nextResponsibleDTO
	 *            es el dto que representa al nuevo responsable.
	 * 
	 * @return el dto que representa al nuevo �tem.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemDTO assignItemToUser(ItemDTO itemDTO,
			WorkflowNodeDescriptionDTO nextNodeDTO,
			AbstractUserDTO nextResponsibleDTO) throws Exception {

		Item anItem = this.getItemsRepository().findById(itemDTO.getOid());
		AbstractUser nextResponsible = (AbstractUser) this.getUsersRepository()
				.findById(nextResponsibleDTO.getOid());
		WorkflowNodeDescription nextNodeDescription = this
				.getWorkflowsRepository().findWorkflowNodeDescriptionById(
						nextNodeDTO.getOid());

		anItem.getProject().assignItemToUser(anItem, nextResponsible,
				nextNodeDescription);

		ItemDTO newItemDTO = this.getDtoFactory().createDTOForItem(anItem);
		newItemDTO.setVersion(anItem.getVersion() + 1);

		EmailServiceBI emailService = ServiceLocator.getInstance()
				.getEmailService();
		emailService
				.sendEmailForItemToUser(newItemDTO, newItemDTO.getCreator());
		emailService.sendEmailForItemToUser(newItemDTO, newItemDTO
				.getResponsible());

		Collection<UserDTO> observers = this.getObserversOfItem(newItemDTO);
		for (UserDTO observer : observers) {
			emailService.sendEmailForItemToUser(newItemDTO, observer);
		}

		return newItemDTO;

	}

	/**
	 * Adjunta un nuevo archivo al �tem representado por el dto. <BR>
	 * La subida concreta del archivo se debe realizar por otro medio, este
	 * servicio no realiza esta funci�n.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem al cual se debe adjuntar un
	 *            archivo.
	 * @param aFileName
	 *            es el nombre del archivo que se ha adjuntado.
	 * @return el dto que representa al �tem.
	 * 
	 * @throws Exception
	 *             esta excepci�n se puede levantar al intentar almacenar el
	 *             archivo recibido.
	 */
	public ItemDTO attachFileToItem(ItemDTO anItemDTO, String aFileName)
			throws Exception {

		Item anItem = this.getItemsRepository().findById(anItemDTO.getOid());
		ItemFile aFile = new ItemFile(aFileName, new Date());

		anItem.attachFile(aFile);

		anItemDTO.setVersion(anItem.getVersion() + 1);

		return anItemDTO;
	}

	/**
	 * Obtiene la colecci�n de archivos adjuntos de un �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem para el cual se debe
	 *            recuperar la colecci�n de archivos adjuntos.
	 * @return una colecci�n que contiene los DTOs de los archivos adjuntos.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<ItemFileDTO> getAttachedFilesOfItem(ItemDTO anItemDTO)
			throws Exception {
		Collection<ItemFile> attachedFiles = new ArrayList<ItemFile>();

		attachedFiles.addAll(this.getItemsRepository().findAttachedFilesOfItem(
				anItemDTO));

		return this.getDtoFactory().createDTOForItemFilters(attachedFiles);
	}

	/**
	 * Elimina los archivos adjuntos del �tem recibido.
	 * 
	 * @param itemDTO
	 *            es el dto que representa al �tem que actualmente tiene los
	 *            archivos.
	 * @param someFilesOids
	 *            es una colecci�n que contiene los identificadores de los
	 *            archivos adjuntos que deben eliminarse.
	 * 
	 * @return el dto que representa al �tem editado.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	public ItemDTO detachFilesFromItem(ItemDTO itemDTO,
			Collection<String> someFilesOids) throws Exception {
		Item anItem = this.getItemsRepository().findById(itemDTO.getOid());

		Collection<ItemFile> itemFiles = this.getItemsRepository()
				.findAttachedFilesById(someFilesOids);
		Iterator<ItemFile> iterator = itemFiles.iterator();
		while (iterator.hasNext()) {
			anItem.removeAttachedFile(iterator.next());
		}

		this.getItemsRepository().deleteObjects(itemFiles);

		itemDTO.setVersion(anItem.getVersion() + 1);

		return itemDTO;

	}

	/**
	 * Recupera la cantidad de tipos de �tems que tiene un proyecto dado.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto que se est� consultando.
	 * @return un entero que representa la cantidad de tipos de �tems.
	 */
	public int getItemTypesCountOfProject(String anOid) {
		int result = 0;

		try {
			Project aProject = this.getProjectsRepository().findById(anOid);
			result = aProject.getItemTypes().size();
		} catch (ProjectUnknownException e) {

		}

		return result;
	}

	/**
	 * Recupera todos los tipos de �tems utilizados por los proyectos.
	 * 
	 * @return una colecci�n que contiene todos los tipos de �tems utilizados en
	 *         los proyectos.
	 */
	public Collection<ItemTypeDTO> getAllItemTypesOfProjects() {
		Collection<ItemType> result = new ArrayList<ItemType>();

		result.addAll(this.getItemsRepository().findAllItemTypesOfProjects());

		return this.getDtoFactory().createDTOForItemTypes(result);
	}

	/**
	 * Recupera los observadores de un �tem dado.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa el �tem para el cual se deben buscar
	 *            los observadores.
	 * @return una colecci�n que contiene los observadores del �tem.
	 * @throws ItemUnknownException
	 *             esta excepci�n se puede lanzar en caso de que no exista el
	 *             �tem.
	 */
	public Collection<UserDTO> getObserversOfItem(ItemDTO anItemDTO)
			throws ItemUnknownException {
		ItemsRepositoryBI repository = this.getItemsRepository();
		Collection<User> observers = repository.getObserversOfItem(anItemDTO
				.getOid());

		return this.getDtoFactory().createDTOForUsers(observers);
	}

	/**
	 * Actualiza el valor de las propiedades adicionales del �tem representado
	 * por el DTO recibido.<br>
	 * Si el �tem ya cuenta con las propiedades, �stas ser�n sobreescritas.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa al �tem.
	 * @param someProperties
	 *            es un diccionario que contiene los valores de las propiedades
	 *            adicionales.
	 * @return un DTO que representa al �tem editando.
	 * @throws Exception
	 *             esta excepci�n se puede lanzar al ejecutar este servicio
	 *             debido a que no se encuentra el �tem o que no se est�
	 *             editando la �ltima versi�n del mismo.
	 */
	public ItemDTO setAdditionalPropertiesToItem(ItemDTO anItemDTO,
			Map<String, String> someProperties) throws Exception {

		try {
			Item item = (Item) this.getItemsRepository().findById(
					anItemDTO.getOid());
			// controla que se est� editando la �ltima versi�n del objeto
			if (!this.verifyLatestVersion(item, anItemDTO)) {

				throw new ItemConcurrentModificationException();

			} else {

				item.setAdditionalProperties(someProperties);
				ItemDTO newItemDTO = this.getDtoFactory()
						.createDTOForItem(item);

				return newItemDTO;

			}
		} catch (ItemUnknownException pue) {

			pue.printStackTrace();
			throw new ItemEditionException();

		}

	}

	/**
	 * Finder.
	 * 
	 * @param someSelectedItems
	 *            es una colecci�n que contiene los oids de los �tems que se
	 *            deben recuperar.
	 * @return una colecci�n que contiene los DTOs de los �tems cuyos
	 *         identificadores se ha recibido.
	 * @throws Exception
	 *             es una excepci�n que se puede levantar en caso de no
	 *             encontrar alg�n �tem.
	 */
	public Collection<ItemDTO> findItemsByOid(
			Collection<String> someSelectedItems) throws Exception {
		Collection<ItemDTO> result = new ArrayList<ItemDTO>();
		ItemsRepositoryBI repository = this.getItemsRepository();

		result.addAll(this.getDtoFactory().createDTOForItems(
				repository.findItemsById(someSelectedItems)));

		return result;
	}

}
