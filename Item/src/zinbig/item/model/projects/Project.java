/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import zinbig.item.model.Item;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.model.ItemType;
import zinbig.item.model.exceptions.ItemTypeTitleNotUniqueException;
import zinbig.item.model.exceptions.PropertyDescriptionNameNotUniqueException;
import zinbig.item.model.properties.PropertyDescription;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.model.workflow.WorkflowNode;
import zinbig.item.model.workflow.WorkflowNodeDescription;
import zinbig.item.repositories.RepositoryLocator;
import zinbig.item.repositories.bi.ItemsRepositoryBI;
import zinbig.item.repositories.bi.PrioritiesRepositoryBI;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase representan a los proyectos administrados por
 * esta aplicaci�n. <br>
 * Un proyecto tiene un nombre, y un conjunto de usuarios asignados al mismo que
 * trabajan sobre los items que se van creando dentro del proyecto.<br>
 * Por defecto todo proyecto es p�blico, es decir que cualquier persona puede
 * ver su contenido.<br>
 * Esta clase implementa la interfaz Versionable a fin de poder controlar la
 * edici�n concurrente por parte de dos usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Project extends Object implements Versionable {

	/**
	 * Mantiene la versi�n de este objeto.
	 */
	public int version;

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Es el nombre de este proyecto.
	 */
	public String name;

	/**
	 * Es el nombre corto de este proyecto.
	 */
	public String shortName;

	/**
	 * Es una colecci�n con todos los items que tiene este proyecto.
	 */
	public Collection<Item> items;

	/**
	 * Es una colecci�n con todos los tipos de items que tiene este proyecto.
	 */
	public Collection<ItemType> itemTypes;

	/**
	 * Es el conjunto de prioridades de este proyecto.
	 */
	public PrioritySet prioritySet;

	/**
	 * Es la fecha de creaci�n de este proyecto.
	 */
	public Date creationDate;

	/**
	 * Es una colecci�n que contiene todos los grupos de usuarios definidos para
	 * este proyecto.
	 */
	public Collection<UserGroup> userGroups;

	/**
	 * Indica si este proyecto es de acceso p�blico o privado.
	 */
	public boolean publicProject;

	/**
	 * Representa el link de la p�gina de este proyecto.
	 */
	public String projectLink;

	/**
	 * Mantiene un contador con el n�mero de id del pr�ximo �tem a ser creado.
	 */
	public int nextItemId;

	/**
	 * Es la descripci�n del workflow asociado a este proyecto.
	 */
	public WorkflowDescription workflowDescription;

	/**
	 * Contiene el nombre de la clase de la estrategia de asignaci�n de
	 * responsables de los �tems asignada a este proyecto.
	 */
	public String itemResponsibleAssignmentStrategyClassName;

	/**
	 * Es el usuario que figura como l�der de este proyecto.
	 */
	public User projectLeader;

	/**
	 * Es una colecci�n que contiene las definiciones de las propiedades
	 * adicionales para los �tems de este proyecto.
	 */
	public Collection<PropertyDescription> itemProperties;

	/**
	 * Constructor por defecto. <br>
	 * Este constructor no deber�a utilizarse en forma directa; existe para
	 * poder realizar test de unidad sobre esta clase.
	 */
	protected Project() {
		this.setUserGroups(new ArrayList<UserGroup>());
		this.setItems(new ArrayList<Item>());
		this.setItemTypes(new ArrayList<ItemType>());
		this.setPublicProject(true);
		this.setProjectLink("");
		this.setNextItemId(0);
		this.setOid(IDGenerator.getId());
		this.setItemProperties(new ArrayList<PropertyDescription>());
	}

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre del proyecto.
	 * @param aShortName
	 *            es el nombre corto del proyecto.
	 * @param aCreationDate
	 *            es la fecha de creaci�n del proyecto.
	 * @param aBoolean
	 *            indica si el proyecto es p�blico o no.
	 * @param aLink
	 *            es el link de esta p�gina.
	 * @param aPrioritySet
	 *            es el conjunto de prioridades asignado a este proyecto.
	 * @param aWorkflowDescription
	 *            es la descripci�n del workflow asociada a este proyecto.
	 * @param aClassName
	 *            es el nombre de la clase de la estrategia de asignaci�n de
	 *            responsables de los �tems.
	 * @param aProjectLeader
	 *            es el usuario seleccionado como l�der del proyecto.
	 */
	public Project(String aName, String aShortName, Date aCreationDate,
			boolean aBoolean, String aLink, PrioritySet aPrioritySet,
			WorkflowDescription aWorkflowDescription, String aClassName,
			User aProjectLeader, Collection<ItemType> someItemTypes) {
		this();
		this.setCreationDate(aCreationDate);
		this.setName(aName);
		this.setShortName(aShortName);
		this.setPublicProject(aBoolean);
		this.setProjectLink(aLink);

		this.setPrioritySet(aPrioritySet);
		this.setWorkflowDescription(aWorkflowDescription);
		this.setItemResponsibleAssignmentStrategyClassName(aClassName);
		this.setProjectLeader(aProjectLeader);
		this.setItemTypes(someItemTypes);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de este proyecto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del proyecto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creaci�n de este proyecto.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creaci�n de este proyecto.
	 */
	public void setCreationDate(Date aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de esta instancia.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de esta instancia.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todos los items de este proyecto.
	 */
	public Collection<Item> getItems() {
		return this.items;
	}

	/**
	 * Setter.
	 * 
	 * @param someItems
	 *            es una colecci�n que contiene los items de este proyecto.
	 */
	public void setItems(Collection<Item> someItems) {
		this.items = someItems;
	}

	/**
	 * Getter.
	 * 
	 * @return el conjunto de prioridades de este proyecto
	 */
	public PrioritySet getPrioritySet() {
		return this.prioritySet;
	}

	/**
	 * Setter.
	 * 
	 * @param aPrioritySet
	 *            es el conjunto de prioridades de este proyecto
	 */
	public void setPrioritySet(PrioritySet aPrioritySet) {
		this.prioritySet = aPrioritySet;
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente a los niveles de prioridad.
	 */
	public PrioritiesRepositoryBI getPrioritiesRepository() {
		return RepositoryLocator.getInstance().getPrioritiesRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente a los proyectos y sus componentes.
	 */
	public ProjectsRepositoryBI getProjectsRepository() {
		return RepositoryLocator.getInstance().getProjectsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una implementaci�n del patr�n Repository utilizado para acceder
	 *         en forma eficiente a los �tems y sus objetos asociados.
	 */
	public ItemsRepositoryBI getItemsRepository() {
		return RepositoryLocator.getInstance().getItemsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene como entrada cada nivel de prioridad
	 *         y como valor la cantidad de items no finalizados que tienen dicho
	 *         nivel de prioridad.
	 */
	public Object getUnfinishedItemsCountByPriorityLevel() {

		return this.getPrioritiesRepository()
				.findUnfinishedItemsCountByPriorityForProject(this);
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todos los grupos de usuarios definidos
	 *         para este proyecto.
	 */
	public Collection<UserGroup> getUserGroups() {
		return this.userGroups;
	}

	/**
	 * Setter.
	 * 
	 * @param someGroups
	 *            es una colecci�n que contiene todos los grupos de usuarios
	 *            definidos para este proyecto.
	 */
	public void setUserGroups(Collection<UserGroup> someGroups) {
		this.userGroups = someGroups;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que este proyecto sea p�blico; false en caso
	 *         contrario.
	 */
	public boolean isPublicProject() {
		return this.publicProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si este proyecto es p�blico o no.
	 */
	public void setPublicProject(boolean aBoolean) {
		this.publicProject = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre corto de este proyecto.
	 */
	public String getShortName() {
		return this.shortName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre corto de este proyecto.
	 */
	public void setShortName(String aName) {
		this.shortName = aName;
	}

	/**
	 * Agrega un nuevo grupo de usuarios a este proyecto.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que se debe agregar.
	 */
	public void addUserGroup(UserGroup anUserGroup) {

		this.getUserGroups().add(anUserGroup);
		this.getWorkflowDescription()
				.addInitialNodeAuthorizedUsers(anUserGroup);
	}

	/**
	 * Getter.
	 * 
	 * @return el link de este usuario.
	 */
	public String getProjectLink() {
		return this.projectLink;
	}

	/**
	 * Setter.
	 * 
	 * @param aLink
	 *            es el link de esta p�gina.
	 */
	public void setProjectLink(String aLink) {
		this.projectLink = aLink;
	}

	/**
	 * Getter.
	 * 
	 * @return la versi�n de este objeto.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es el n�mero de versi�n de esta instancia.
	 */
	public void setVersion(int aNumber) {
		this.version = aNumber;
	}

	/**
	 * Asigna al proyecto el conjunto de prioridades y actualiza el n�mero de
	 * proyectos que hacen referencia a dicho conjunto de prioridades.
	 * 
	 * @param aPrioritySet
	 *            es el conjunto de prioridades que debe asignarse a este
	 *            proyecto.
	 */
	public void assignPrioritySet(PrioritySet aPrioritySet) {
		// remueve el conjunto de prioridades anterior en caso de que exista.
		if (this.getPrioritySet() != null) {
			this.getPrioritySet().decreaseReferencesCount();
		}

		this.setPrioritySet(aPrioritySet);
		aPrioritySet.increaseReferencesCount();
	}

	/**
	 * Getter.
	 * 
	 * @param anUser
	 *            es el usuario responsable del nuevo estado.
	 * 
	 * @return el nodo del workflow identificado como el nodo inicial.
	 */
	private WorkflowNode getInitialWorkflowNodeForUser(AbstractUser anUser) {

		return this.getWorkflowDescription().getInitialNode(anUser);

	}

	/**
	 * Getter.
	 * 
	 * @return el id del pr�ximo �tem.
	 */
	private int getNextItemId() {
		return this.nextItemId;
	}

	/**
	 * Setter.
	 * 
	 * @param anInt
	 *            es el id del pr�ximo �tem.
	 */
	public void setNextItemId(int anInt) {
		this.nextItemId = anInt;
	}

	/**
	 * Devuelve el pr�ximo n�mero de id para el nuevo �tem.
	 * 
	 * @return al id del �tem, que es una unidad m�s que el anterior �tem
	 *         creado.
	 */
	public int getNextItemIdForItem() {
		this.setNextItemId(this.getNextItemId() + 1);
		return this.getNextItemId();
	}

	/**
	 * Getter.
	 * 
	 * @return la descripci�n del workflow asociada a este proyecto.
	 */
	public WorkflowDescription getWorkflowDescription() {
		return this.workflowDescription;
	}

	/**
	 * Setter.
	 * 
	 * @param aWorkflowDescription
	 *            es la descripci�n del workflow asociada a este proyecto.
	 */
	public void setWorkflowDescription(WorkflowDescription aWorkflowDescription) {
		this.workflowDescription = aWorkflowDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase de la estrategia de asignaci�n de
	 *         responsables de los �tems asignada a este proyecto.
	 */
	public String getItemResponsibleAssignmentStrategyClassName() {
		return this.itemResponsibleAssignmentStrategyClassName;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            el nombre de la clase de la estrategia de asignaci�n de
	 *            responsables de los �tems asignada a este proyecto.
	 */
	public void setItemResponsibleAssignmentStrategyClassName(String aClassName) {
		this.itemResponsibleAssignmentStrategyClassName = aClassName;
	}

	/**
	 * Este m�todo instancia mediante reflection la estrategia de asignaci�n de
	 * usuarios.
	 * 
	 * @return una estrategia de asignaci�n de responsables de los �tems.
	 */
	@SuppressWarnings("unchecked")
	private AbstractItemResponsibleAssignmentStrategy getItemResponsibleAssignmentStrategy() {

		AbstractItemResponsibleAssignmentStrategy strategy = null;
		try {
			Class aClass = Class.forName(this
					.getItemResponsibleAssignmentStrategyClassName());
			Object o = aClass.newInstance();
			strategy = (AbstractItemResponsibleAssignmentStrategy) o;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return strategy;
	}

	/**
	 * Getter.
	 * 
	 * @return el usuario l�der de este proyecto.
	 */
	public User getProjectLeader() {
		return this.projectLeader;
	}

	/**
	 * Setter.
	 * 
	 * @param anUser
	 *            es el usuario l�der de este proyecto.
	 */
	public void setProjectLeader(User anUser) {
		this.projectLeader = anUser;
	}

	/**
	 * Crea un nuevo item y se lo agrega a su colecci�n de �tems.
	 * 
	 * @param anUser
	 *            es el usuario creador del �tem.
	 * @param aTitle
	 *            es el t�tulo del nuevo �tem.
	 * @param aDescription
	 *            es la descripci�n del nuevo �tem.
	 * @param aPriority
	 *            es la prioridad del nuevo �tem.
	 * @param aState
	 *            es el estado del nuevo �tem.
	 * @param aDate
	 *            es la fecha de creaci�n del nuevo �tem.
	 * @param responsibleCandidate
	 *            es el candidato a ser responsable del nuevo �tem. Este
	 *            candidato puede ser tanto un usuario individual como un grupo.
	 * @param anItemType
	 *            es el tipo del nuevo �tem.
	 * @param somePropertyDescriptions
	 *            es un diccionario que contiene los valores ingresados para
	 *            cada una de las propiedades adicionales del nuevo �tem.
	 * 
	 * @return el �tem recientemente creado.
	 */
	public Item createItem(User anUser, String aTitle, String aDescription,
			Priority aPriority, ItemStateEnum aState, Date aDate,
			AbstractUser responsibleCandidate, ItemType anItemType,
			Map<String, String> somePropertyDescriptions) {

		WorkflowNode currentNode = null;
		AbstractUser itemResponsible = null;

		if (aState.equals(ItemStateEnum.CREATED)) {
			// el estado del �tem es creado, as� que se le asigna el nodo nulo
			// (para el usuario esto significa que no ha iniciado el workflow
			// para este �tem).
			currentNode = null;

			itemResponsible = anUser;

		} else { // si no est� en estado creado entonces se encuentra en estado
			// abierto por lo que se le asigna el nodo inicial del
			// workflow invocando a la estrategia de selecci�n de
			// responsable del proyecto.

			itemResponsible = this.getItemResponsibleAssignmentStrategy()
					.getResponsibleForNewItem(this, anUser,
							responsibleCandidate);
			currentNode = this.getInitialWorkflowNodeForUser(itemResponsible);
		}
		int nextId = this.getNextItemIdForItem();
		Item anItem = new Item(nextId, anUser, this, aTitle, aDescription,
				aPriority, aState, aDate, itemResponsible, anItemType,
				somePropertyDescriptions);
		anItemType.increaseReferencesCount();
		if (currentNode != null) {
			anItem.assignCurrentWorkflowNode(currentNode);
		}

		this.getItems().add(anItem);
		return anItem;
	}

	/**
	 * Getter.
	 * 
	 * @return una representaci�n como String del proyecto. La representaci�n se
	 *         basa en el nombre del proyecto.
	 */
	@Override
	public String toString() {
		return "Project [name=" + name + "]";
	}

	/**
	 * Compara al receptor con el objeto recibido para ver si son los mismos.
	 * 
	 * @param anObject
	 *            es el objeto contra el cual se debe comparar al receptor.
	 * @return true en caso de que los oids de los dos proyectos coincidan;
	 *         false en caso contrario.
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;

		try {
			Project anotherProject = (Project) anObject;
			if (anotherProject.getOid().equals(this.getOid())) {
				result = true;
			}
		} catch (Exception e) {

		}
		return result;

	}

	/**
	 * Notifica al proyecto que se prepare para ser eliminado. <br>
	 * Prepararse para ser eliminado significa romper todos los v�nculos
	 * existentes con los objetos que no ser�n eliminados. Por ejemplo, no se
	 * eliminan directamente los �tems del proyecto, sino que se recorre cada
	 * uno de �stos para que los mismos vayan realizando la preparaci�n para su
	 * borrado.
	 */
	public void prepareForDeletion() {

		// rompe la asociaci�n entre el l�der y el proyecto.
		this.setProjectLeader(null);

		// elimina la descripci�n del workflow del proyecto. Como no se
		// comparte la definici�n de workflow con otros proyectos.
		this.getWorkflowDescription().prepareForDeletion();

		// rompe la asociaci�n entre los grupos de usuarios y el
		// proyecto. Como los grupos de usuarios pueden estar compartidos
		// con otros proyectos no se los puede borrar.
		UserGroup anUserGroup = null;
		for (Iterator<UserGroup> iterator = this.getUserGroups().iterator(); iterator
				.hasNext();) {
			anUserGroup = iterator.next();
			anUserGroup.removeProject(this);
		}
		this.setUserGroups(null);

		// rompe la asociaci�n entre el conjunto de prioridades y el
		// proyecto. Como el conjunto de prioridad puede estar compartido
		// con otros proyectos no se lo puede borrar.
		this.setPrioritySet(null);

		Item anItem = null;
		for (Iterator<Item> iterator = this.getItems().iterator(); iterator
				.hasNext();) {
			anItem = iterator.next();

			// notifica al �tem que se prepare para ser eliminado
			anItem.prepareForDeletion();

		}

		// borra los tipos de �tems.
		ItemType anItemType = null;
		for (Iterator<ItemType> iterator = this.getItemTypes().iterator(); iterator
				.hasNext();) {
			anItemType = iterator.next();

			// notifica al tipo �tem que se prepare para ser eliminado
			anItemType.prepareForDeletion();
		}
	}

	/**
	 * Actualiza los datos b�sicos de un �tem.
	 * 
	 * @param anItem
	 *            es el �tem que se debe actualizar.
	 * @param aTitle
	 *            es el nuevo titulo del �tem.
	 * @param aDescription
	 *            es la nueva descripci�n del �tem.
	 * @param aPriority
	 *            es la nueva prioridad del �tem.
	 * @param anItemType
	 *            es el nuevo tipo de �tem.
	 */
	public void updateItem(Item anItem, String aTitle, String aDescription,
			Priority aPriority, ItemType anItemType) {

		anItem.setDescription(aDescription);
		anItem.setTitle(aTitle);

		if (!aPriority.equals(anItem.getPriority())) {
			anItem.assignPriority(aPriority);
		}

		if (!anItemType.equals(anItem.getItemType())) {
			anItem.assignItemType(anItemType);
		}

	}

	/**
	 * El usuario recibido quiere tomar la responsabilidad del �tem recibido.
	 * 
	 * @param anUser
	 *            es el usuario que ser� responsable del �tem.
	 * @param anItem
	 *            es el �tem siendo editado.
	 */
	public void userWantsToTakeItem(User anUser, Item anItem) {

		WorkflowNode currentWorkflowNode = anItem.getCurrentWorkflowNode();
		WorkflowNode newWorkflowNode = new WorkflowNode(currentWorkflowNode
				.getTitle(), anUser, currentWorkflowNode.getNodeDescription());

		anItem.assignCurrentWorkflowNode(newWorkflowNode);
		anItem.setResponsible(anUser);

	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los tipos de �tems de este proyecto.
	 */
	public Collection<ItemType> getItemTypes() {
		return this.itemTypes;
	}

	/**
	 * Setter.
	 * 
	 * @param someItemTypes
	 *            es una colecci�n que contiene los tipos de �tems de este
	 *            proyecto.
	 */
	public void setItemTypes(Collection<ItemType> someItemTypes) {
		this.itemTypes = someItemTypes;
	}

	/**
	 * Elimina del proyecto los items recibidos.<br>
	 * Si el usuario no es el l�der del proyecto entonces solamente se eliminan
	 * los �tems cuyo responsable actual es el usuario.
	 * 
	 * @param someItems
	 *            es una colecci�n que contiene los �tems que deben eliminarse.
	 * @param anUser
	 *            es el usuario que est� intentando eliminar los �tems.
	 * 
	 * @return una colecci�n con los �tems que efectivamente se han podido
	 *         eliminar.
	 */
	public Collection<Item> deleteItems(Collection<Item> someItems, User anUser) {
		Item anItem = null;
		Iterator<Item> itemsIterator = someItems.iterator();
		// si el usuario es el l�der del proyecto intenta borrar todos los
		// �tems. Si no es el l�der entonces verifica que cada �tem est�
		// asignado al usuario
		if (anUser.equals(this.getProjectLeader())) {

			while (itemsIterator.hasNext()) {
				anItem = itemsIterator.next();
				anItem.prepareForDeletion();

			}
		} else {

			while (itemsIterator.hasNext()) {
				anItem = itemsIterator.next();
				if (!anItem.getResponsible().equals(anUser)) {
					itemsIterator.remove();
				} else {
					anItem.prepareForDeletion();
				}
			}

		}
		this.getItems().removeAll(someItems);

		return someItems;

	}

	/**
	 * Notifica al proyecto para que elimine al usuario recibido.
	 * 
	 * @param anUser
	 *            es el usuario que debe ser eliminado del proyecto.
	 */
	public void removeUser(User anUser) {

		// notifica al workflow que elimine al usuario de sus nodos.
		WorkflowDescription workflowDescription = this.getWorkflowDescription();
		workflowDescription.deleteUser(anUser);

		// notifica a cada �tem de este proyecto para que elimine toda
		// referencia a este usuario.
		Iterator<Item> itemsIterator = this.getItems().iterator();
		Item anItem = null;
		while (itemsIterator.hasNext()) {
			anItem = itemsIterator.next();
			anItem.deleteUser(anUser);
		}

	}

	/**
	 * Agrega un nuevo tipo de �tem a este proyecto siempre que no exista un
	 * tipo de �tem con el mismo t�tulo.
	 * 
	 * @param aTitle
	 *            es el t�tulo del nuevo tipo de �tem.
	 */
	public ItemType addItemType(String aTitle)
			throws ItemTypeTitleNotUniqueException {

		if (!this.getItemsRepository().containsItemTypeWithTitleInProject(this,
				aTitle)) {

			ItemType newItemType = new ItemType(aTitle);

			// agrega el nuevo tipo de �tem
			this.getItemTypes().add(newItemType);

			return newItemType;

		} else {

			throw new ItemTypeTitleNotUniqueException();
		}

	}

	/**
	 * Asigna el �tem recibido a un nuevo responsable en el nodo de workflow
	 * recibido.
	 * 
	 * @param anItem
	 *            es el �tem que se debe reasignar.
	 * @param nextResponsible
	 *            es el nuevo responsable del �tem.
	 * @param nextNodeDescription
	 *            es el nuevo estado del �tem.
	 */
	public void assignItemToUser(Item anItem, AbstractUser nextResponsible,
			WorkflowNodeDescription nextNodeDescription) {

		WorkflowNode newWorkflowNode = nextNodeDescription
				.createNodeForUser(nextResponsible);

		anItem.assignCurrentWorkflowNode(newWorkflowNode);
		anItem.setResponsible(nextResponsible);

	}

	/**
	 * Elimina de este proyecto los tipos de �tems recibidos.
	 * 
	 * @param someItemTypes
	 *            es una colecci�n con los tipos de �tems que se deben eliminar.
	 *            Solamente se pueden eliminar los tipos de �tems que no est�n
	 *            en uso (referencesCount=0).
	 */
	public void deleteItemTypes(Collection<ItemType> someItemTypes) {
		this.getItemTypes().removeAll(someItemTypes);

	}

	/**
	 * Unifica todos los �tems recibidos referenciando al �tem individual. Todos
	 * los �tems se pasan a un estado CLOSED y se les agrega un comentario que
	 * indica el n�mero del �tem creado al unificarse.
	 * 
	 * @param itemsToAggregate
	 *            es una colecci�n que contiene todos los �tems que se deben
	 *            unificar.
	 * @param newItem
	 *            es el nuevo �tem que se cre� al unificar los �tems.
	 * @param aComment
	 *            es el comentario que se debe agregar a cada �tem unificado
	 *            para dejar asentado que se encuentra Cerrado debido a una
	 *            unificaci�n.
	 */
	public void aggregateItems(Collection<Item> itemsToAggregate, Item newItem,
			String aComment) {
		Iterator<Item> iterator = itemsToAggregate.iterator();
		Item anItem = null;

		while (iterator.hasNext()) {
			anItem = iterator.next();
			anItem.setState(ItemStateEnum.CLOSED);
			anItem.addComment(aComment + newItem.getItemId(), newItem
					.getCreator());
		}

	}

	/**
	 * Remueve un grupo de usuarios del proyecto.<BR>
	 * Se notifica al workflow para que actualice todos sus nodos.
	 * 
	 * @param anUserGroup
	 *            es el grupo de usuarios que se debe remover.
	 */
	public void removeUserGroup(UserGroup anUserGroup) {
		this.getUserGroups().remove(anUserGroup);
		this.getWorkflowDescription().removeUserGroup(anUserGroup);

	}

	/**
	 * Getter.
	 * 
	 * @return la colecci�n de propiedades adicionales de los �tems de este
	 *         proyecto.
	 */
	public Collection<PropertyDescription> getItemProperties() {
		return this.itemProperties;
	}

	/**
	 * Setter.
	 * 
	 * @param someProperties
	 *            es la colecci�n de propiedades adicionales de los �tems de
	 *            este proyecto.
	 */
	public void setItemProperties(Collection<PropertyDescription> someProperties) {
		this.itemProperties = someProperties;
	}

	/**
	 * Agrega una nueva descripci�n de una propiedad al proyecto.
	 * 
	 * @param aPropertyDescription
	 *            es la nueva descripci�n de propiedad que se est� intentando
	 *            agregar.
	 * @throws PropertyDescriptionNameNotUniqueException
	 *             esta excepci�n se levanta en el caso de intentar agregar una
	 *             nueva descripci�n con un nombre ya existente.
	 */
	public void addPropertyDescription(PropertyDescription aPropertyDescription)
			throws PropertyDescriptionNameNotUniqueException {
		ProjectsRepositoryBI repository = this.getProjectsRepository();

		if (repository.containsPropertyDescriptionWithName(this,
				aPropertyDescription.getName())) {
			throw new PropertyDescriptionNameNotUniqueException();
		} else {
			this.getItemProperties().add(aPropertyDescription);
		}
	}

	/**
	 * Elimina una descripci�n de propiedad adicional de este proyecto.
	 * 
	 * @param aProperty
	 *            es la propiedad que debe ser eliminada.
	 */
	public void deleteItemProperty(PropertyDescription aProperty) {
		this.getItemProperties().remove(aProperty);

	}

	/**
	 * Mueve masivamente los �tems recibidos a un nuevo nodo del workflow del
	 * proyecto.<br>
	 * Los �tems que est�n cerrados o bloqueados no se pueden mover. Tampoco se
	 * pueden mover los �tems que est�n en un nodo del workflow que no est�
	 * conectado (aunque sea en forma indirecta) con el nodo destino.
	 * 
	 * @param items
	 *            es una colecci�n que contiene los �tems que se intenta mover.
	 * @param aNode
	 *            es la descripci�n de nodo a la cual se deber�a mover los
	 *            �tems.
	 * @return una colecci�n de strings que contiene los ids de los �tems que se
	 *         pudieron pasar al nuevo nodo.
	 */
	public Collection<String> massiveItemsMovement(Collection<Item> items,
			WorkflowNodeDescription aNode) {
		Collection<String> result = new ArrayList<String>();

		// itera los �tems para tratar de moverlos
		for (Item i : items) {
			// verifica que no est� ni bloqueado ni terminado
			if (!i.getState().equals(ItemStateEnum.BLOCKED)
					&& !i.getState().equals(ItemStateEnum.CLOSED)) {

				// intenta mover al siguiente nodo del workflow verificando si
				// hay un camino
				if (this.getWorkflowDescription().verifyPathBetweenNodes(
						i.getCurrentWorkflowNode().getNodeDescription(), aNode,
						i.getItemType())) {
					// mueve el �tem
					result.add(new Integer(i.getItemId()).toString());
				}

			}
		}

		return result;
	}

}
