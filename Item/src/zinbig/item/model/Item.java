/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zinbig.item.model.projects.Priority;
import zinbig.item.model.projects.Project;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.workflow.WorkflowNode;
import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Las instancias de esta clase representan los items que son admnistrados en
 * los diferentes proyectos que mantiene la herramienta.<br>
 * Un �tem tiene un estado que define que operaciones se pueden realizar sobre
 * el mismo. Por el momento este estado es un enumerativo (fijo), el cual no se
 * puede cambiar desde la misma aplicaci�n. <br>
 * Adem�s cada �tem puede estar en un nodo del workflow en particular, a partir
 * del cual se define el camino que puede seguir. <br>
 * Los cambios de estado de un �tem se pueden dar por dos razones: o bien porque
 * el usuario expl�citamente decide cambiarlo de estado (pasarlo a Cerrado por
 * ejemplo, en cuyo caso se almacenar� el �ltimo nodo del workflow en donde se
 * cerr� el �tem) o tambi�n en forma m�s autom�tica, cuando se pasa a un �tem a
 * un nodo final del workflow (en donde se pasar� al �tem al estado cerrado). <br>
 * Otros cambios manuales o autom�ticos son posibles, los descriptos no son los
 * �nicos.<br>
 * Esta clase implementa la interfaz Versionable a fin de poder controlar la
 * edici�n concurrente por parte de dos usuarios.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Item implements Versionable, Comparable<Item> {

	/**
	 * Mantiene la versi�n de este objeto.
	 */
	public int version;

	/**
	 * Mantiene el oid que permite identificar a esta instancia.
	 */
	protected String oid;

	/**
	 * Es una identificaci�n funcional del item. Esta identificaci�n es
	 * dependiente de cada proyecto.
	 */
	protected int itemId;

	/**
	 * Es el t�tulo del item.
	 */
	protected String title;

	/**
	 * Es la descripci�n del item.
	 */
	protected String description;

	/**
	 * Es el usuario que cre� este item.
	 */
	protected User creator;

	/**
	 * Es la fecha de creaci�n de este item.
	 */
	protected Date creationDate;

	/**
	 * Indica si este item ha llegado a un estado final del workflow.
	 */
	protected boolean finished;

	/**
	 * Es el nivel de prioridad del item.
	 */
	protected Priority priority;

	/**
	 * Es el proyecto en el cual se encuentra el item.
	 */
	protected Project project;

	/**
	 * Referencia al tipo de este �tem.
	 */
	protected ItemType itemType;

	/**
	 * Mantiene una referencia a un enumerativo que establece el estado en el
	 * que se encuentra un �tem. Este estado no es el nodo del workflow en el
	 * que se encuentra, sino que establece qu� acciones se pueden llevar a cabo
	 * sobre el �tem, como por ejemplo Pasarlo a un nuevo nodo, cerrarlo, etc.
	 */
	protected ItemStateEnum state;

	/**
	 * Es el usuario responsable de este �tem en este momento.
	 */
	protected AbstractUser responsible;

	/**
	 * Es el nodo actual en el que se encuentra el �tem dentro del workflow.
	 */
	protected WorkflowNode currentWorkflowNode;

	/**
	 * Esta colecci�n mantiene una referencia a cada usuario o grupo de usuarios
	 * que expres� su inter�s en recibir notificaciones de este �tem.
	 */
	protected Collection<AbstractUser> observers;

	/**
	 * Esta colecci�n mantiene una referencia a cada comentario que se realiz�
	 * sobre este �tem.
	 */
	protected Collection<Comment> comments;

	/**
	 * Es una colecci�n que contiene los archivos adjuntos de este �tem.
	 */
	protected Collection<ItemFile> files;

	/**
	 * Es una colecci�n que contiene los nodos de workflow por los que ha pasado
	 * este �tem.
	 */
	protected Collection<WorkflowNode> oldWorkflowNodes;

	/**
	 * Es un diccionario que contiene los valores asignados a cada una de las
	 * propiedades adicionales del �tem.
	 */
	protected Map<String, String> additionalProperties;

	/**
	 * Constructor por defecto. <br>
	 * Este constructor no deber�a ser utilizado directamente, existe para poder
	 * realizar pruebas de unidad sobre esta clase.
	 */
	protected Item() {
		this.setOid(IDGenerator.getId());
		this.setObservers(new ArrayList<AbstractUser>());
		this.setComments(new ArrayList<Comment>());
		this.setFiles(new ArrayList<ItemFile>());
		this.setOldWorkflowNodes(new ArrayList<WorkflowNode>());
		this.setAdditionalProperties(new HashMap<String, String>());
	}

	/**
	 * Constructor.
	 * 
	 * @param nextId
	 *            es el identificador de este item dentro del proyecto.
	 * @param anUser
	 *            es el usuario creador de este requerimiento.
	 * @param aProject
	 *            es el proyecto en donde se agrega el �tem.
	 * @param aTitle
	 *            es el t�tulo del �tem.
	 * @param aDescription
	 *            es la descripci�n del �tem.
	 * @param aPriority
	 *            es la prioridad del �tem.
	 * @param aState
	 *            es el estado del item.
	 * @param aDate
	 *            es la fecha de creaci�n del �tem.
	 * @param responsibleUser
	 *            es el usuario o grupo de usuarios seleccionado para ser
	 *            responsable del nuevo �tem. En caso de que la estrategia de
	 *            asignaci�n del proyecto sea la adecuada, este usuario o grupo
	 *            ser� el responsable del proyecto.
	 * @param anItemType
	 *            es el tipo de este nuevo �tem.
	 * @param aMap
	 *            es un diccionario que contiene las propiedades adicionales de
	 *            este �tem.
	 */
	public Item(int nextId, User anUser, Project aProject, String aTitle,
			String aDescription, Priority aPriority, ItemStateEnum aState,
			Date aDate, AbstractUser responsibleUser, ItemType anItemType,
			Map<String, String> aMap) {

		this();
		this.setItemId(nextId);
		this.setCreationDate(aDate);
		this.setCreator(anUser);
		this.setFinished(false);
		this.setDescription(aDescription);
		this.setTitle(aTitle);
		this.assignPriority(aPriority);
		this.setState(aState);
		this.setProject(aProject);
		this.setResponsible(responsibleUser);
		this.setItemType(anItemType);
		this.setAdditionalProperties(aMap);
	}

	/**
	 * Asigna una prioridad al �tem y actualiza la cantidad de las referencias a
	 * la misma.
	 * 
	 * @param aPriority
	 *            es la nueva prioridad del �tem.
	 */
	public void assignPriority(Priority aPriority) {

		if (this.getPriority() != null) {
			this.getPriority().decreaseReferencesCount();
		}
		this.setPriority(aPriority);
		aPriority.increaseReferencesCount();

	}

	/**
	 * Asigna un tipo de �tem al �tem y actualiza la cantidad de las referencias
	 * al mismo.
	 * 
	 * @param anItemType
	 *            es el nuevo tipo de �tem.
	 */
	public void assignItemType(ItemType anItemType) {

		if (this.getItemType() != null) {
			this.getItemType().decreaseReferencesCount();
		}
		this.setItemType(anItemType);
		anItemType.increaseReferencesCount();

	}

	/**
	 * Getter.
	 * 
	 * @return el oid del item.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de esta instancia.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el id funcional de este item.
	 */
	public int getItemId() {
		return this.itemId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id funcional de este item. Este n�mero depende de cada
	 *            proyecto.
	 */
	public void setItemId(int anId) {
		this.itemId = anId;
	}

	/**
	 * Getter.
	 * 
	 * @return el t�tulo de este item.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo de este item.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return es la descripci�n de este item.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter.
	 * 
	 * @param aDescription
	 *            es la descripci�n de este item.
	 */
	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return el miembro de un proyecto creador de este item.
	 */
	public User getCreator() {
		return this.creator;
	}

	/**
	 * Setter.
	 * 
	 * @param anUser
	 *            es el miembro de alg�n proyecto creador de este item.
	 */
	public void setCreator(User anUser) {
		this.creator = anUser;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creaci�n de este item.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creaci�n de este item.
	 */
	public void setCreationDate(Date aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return indica si este item est� finalizado o no.
	 */
	public boolean isFinished() {
		return this.finished;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            es una indicaci�n de si este item est� finalizado o no.
	 */
	public void setFinished(boolean aBoolean) {
		this.finished = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return la prioridad de este item.
	 */
	public Priority getPriority() {
		return this.priority;
	}

	/**
	 * Setter.
	 * 
	 * @param aPriority
	 *            es la prioridad de este item.
	 */
	public void setPriority(Priority aPriority) {
		this.priority = aPriority;
	}

	/**
	 * Getter.
	 * 
	 * @return el proyecto al cual pertenece este item.
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Setter.
	 * 
	 * @param aProject
	 *            es el proyecto al cual pertenece este item.
	 */
	public void setProject(Project aProject) {
		this.project = aProject;
	}

	/**
	 * Getter.
	 * 
	 * @return el estado de este �tem.
	 */
	public ItemStateEnum getState() {
		return this.state;
	}

	/**
	 * Setter.
	 * 
	 * @param aState
	 *            es el estado de este �tem.
	 */
	public void setState(ItemStateEnum aState) {
		this.state = aState;
	}

	/**
	 * Getter.
	 * 
	 * @return el usuario responsable de este �tem.
	 */
	public AbstractUser getResponsible() {
		return this.responsible;
	}

	/**
	 * Setter.
	 * 
	 * @param anUser
	 *            es el usuario responsable de este �tem.
	 */
	public void setResponsible(AbstractUser anUser) {
		this.responsible = anUser;
	}

	/**
	 * Getter.
	 * 
	 * @return el nodo actual del workflow en donde se encuentra el �tem.
	 */
	public WorkflowNode getCurrentWorkflowNode() {
		return this.currentWorkflowNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aWorkflowNode
	 *            es el nodo actual del workflow en donde se encuentra el �tem.
	 */
	protected void setCurrentWorkflowNode(WorkflowNode aWorkflowNode) {
		this.currentWorkflowNode = aWorkflowNode;
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
	 * Compara el receptor contra el objeto recibido para ordenarlos.
	 */
	@Override
	public int compareTo(Item anItem) {
		int result = this.getOid().compareTo(anItem.getOid());

		return result;
	}

	/**
	 * Notifica al receptor que se prepare para ser eliminado del sistema.<br>
	 * En el caso de un �tem, esta preparaci�n consiste en romper todas las
	 * asociaciones con los objetos del dominio que no deban ser eliminados en
	 * cascada a ra�z de la eliminaci�n del �tem.<br>
	 * Para cada nodo (estado) del workflow por el que pas� el �tem hay que
	 * romper los v�nculos con su definici�n.
	 */
	public void prepareForDeletion() {

		// rompe el v�nculo con el proyecto
		this.setProject(null);

		// rompe el v�nculo con el creador del �tem.
		this.setCreator(null);

		// rompe el v�nculo con la prioridad actuald el �tem
		this.setPriority(null);

		// rompe el v�nculo con el estado actual del �tem
		this.setState(null);

		// rompe el v�nculo con el usuario responsable del �tem actualmente
		this.setResponsible(null);

		// rompe el v�nculo con el nodo actual del �tem
		if (this.getCurrentWorkflowNode() != null) {
			this.getCurrentWorkflowNode().prepareForDeletion();
		}

		// borra los v�nculos con los observadores
		this.setObservers(null);

		// borra el v�nculo con le tipo de �tem
		this.setItemType(null);

		// se notifica a cada estado anterior para que se prepare para la
		// eliminaci�n, pero no se lo elimina de la lista as� se borra en
		// cascada autom�ticamente.
		Iterator<WorkflowNode> oldNodesIterator = this.getOldWorkflowNodes()
				.iterator();
		while (oldNodesIterator.hasNext()) {
			oldNodesIterator.next().prepareForDeletion();
		}

		// los comentarios no se borran expl�citamente para que se eliminen de
		// la base en cascada

		// los archivos no se borrar expl�citamente para que se eliminen en
		// forma autom�tica.

	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene a todos los usuarios o grupos de
	 *         usuarios interesados en recibir notificaciones de este �tem.
	 */
	public Collection<AbstractUser> getObservers() {
		return this.observers;
	}

	/**
	 * Setter.
	 * 
	 * @param someObservers
	 *            es una colecci�n que contiene a todos los usuarios o grupos de
	 *            usuarios interesados en recibir notificaciones de este �tem.
	 */
	public void setObservers(Collection<AbstractUser> someObservers) {
		this.observers = someObservers;
	}

	/**
	 * Agrega al usuario como nuevo observador de este �tem.
	 * 
	 * @param anUser
	 *            es el nuevo observador de este �tem.
	 */
	public void addObserver(AbstractUser anUser) {
		this.getObservers().add(anUser);

	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los comentarios de este �tem.
	 */
	public Collection<Comment> getComments() {
		return this.comments;
	}

	/**
	 * Setter.
	 * 
	 * @param someComments
	 *            es la colecci�n de comentarios de este �tem.
	 */
	public void setComments(Collection<Comment> someComments) {
		this.comments = someComments;
	}

	/**
	 * Agrega un nuevo comentario a este �tem.
	 * 
	 * @param aCommentText
	 *            es el nuevo comentario que se debe agregar a este �tem.
	 * @param anUser
	 *            es el usuario que cre� este comentario.
	 */
	public void addComment(String aCommentText, User anUser) {

		Comment aComment = new Comment(aCommentText, new Date(), anUser
				.getUsername());
		this.getComments().add(aComment);
	}

	/**
	 * Getter.
	 * 
	 * @return el tipo de este �tem.
	 */
	public ItemType getItemType() {
		return this.itemType;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemType
	 *            es el tipo de este �tem.
	 */
	public void setItemType(ItemType anItemType) {
		this.itemType = anItemType;
	}

	/**
	 * Asigna un nodo a este �tem.
	 * 
	 * @param aWorkflowNode
	 *            es el nuevo nodo del �tem.
	 */
	public void assignCurrentWorkflowNode(WorkflowNode aWorkflowNode) {
		Date aDate = new Date();
		if (this.getCurrentWorkflowNode() != null) {
			this.getCurrentWorkflowNode().setFinishDate(aDate);
			this.getCurrentWorkflowNode().decreaseReferencesCount();
			this.getOldWorkflowNodes().add(this.getCurrentWorkflowNode());
		}

		this.setCurrentWorkflowNode(aWorkflowNode);
		aWorkflowNode.setCreationDate(aDate);
		aWorkflowNode.increaseReferencesCount();

		// si el nuevo nodo del workflow es final entonces se cambia el estado
		// del �tem
		if (aWorkflowNode.getNodeDescription().isFinalNode()) {
			this.setState(ItemStateEnum.CLOSED);
			this.setFinished(true);
		} else {
			this.setState(ItemStateEnum.OPEN);
			this.setFinished(false);
		}

	}

	/**
	 * Notifica al �tem que elimine toda referencia al usuario.
	 * 
	 * @param anUser
	 *            es el usuario que debe ser eliminado.
	 */
	public void deleteUser(User anUser) {
		// remueve al usuario de los observadores
		this.getObservers().remove(anUser);

		User projectLeader = this.getProject().getProjectLeader();
		// actualiza el nodo actual del usuario
		if (this.getCurrentWorkflowNode() != null
				&& this.getCurrentWorkflowNode().getCurrentResponsible()
						.equals(anUser)) {
			this.getCurrentWorkflowNode().deleteUser(anUser, projectLeader);
		}

		// actualiza el responsable
		if (this.getResponsible().equals(anUser)) {
			this.setResponsible(this.getProject().getProjectLeader());
		}

		// actualiza el creador
		if (this.getCreator().equals(anUser)) {
			this.setCreator(this.getProject().getProjectLeader());
		}

		// borra al usuario de los estados anteriores
		Iterator<WorkflowNode> oldNodesIterator = this.getOldWorkflowNodes()
				.iterator();
		while (oldNodesIterator.hasNext()) {
			oldNodesIterator.next().deleteUser(anUser, projectLeader);
		}
	}

	/**
	 * Getter.
	 * 
	 * @return la colecci�n de archivos adjuntos de este �tem.
	 */
	public Collection<ItemFile> getFiles() {
		return this.files;
	}

	/**
	 * Setter.
	 * 
	 * @param someFiles
	 *            es la colecci�n de archivos adjuntos de este �tem.
	 */
	public void setFiles(Collection<ItemFile> someFiles) {
		this.files = someFiles;
	}

	/**
	 * Agrega un nuevo archivo adjunto a este �tem.
	 * 
	 * @param aFile
	 *            es el nuevo archivo que se debe agregar.
	 */
	public void attachFile(ItemFile aFile) {
		this.getFiles().add(aFile);

	}

	/**
	 * Elimina un archivo adjunto de este �tem.
	 * 
	 * @param anItemFile
	 *            es el archivo que debe ser eliminado.
	 */
	public void removeAttachedFile(ItemFile anItemFile) {
		this.getFiles().remove(anItemFile);

	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los nodos del workflow por los que
	 *         pas� este �tem.
	 */
	public Collection<WorkflowNode> getOldWorkflowNodes() {
		return this.oldWorkflowNodes;
	}

	/**
	 * Setter.
	 * 
	 * @param someWorkflowNodes
	 *            es una colecci�n que contiene los nodos del workflow por los
	 *            que pas� este �tem.
	 */
	protected void setOldWorkflowNodes(
			Collection<WorkflowNode> someWorkflowNodes) {
		this.oldWorkflowNodes = someWorkflowNodes;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene las propiedades adicionales de este
	 *         �tem.
	 */
	public Map<String, String> getAdditionalProperties() {
		return this.additionalProperties;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es un diccionario que contiene las propiedades adicionales de
	 *            este �tem.
	 */
	public void setAdditionalProperties(Map<String, String> aMap) {
		this.additionalProperties = aMap;
	}

}
