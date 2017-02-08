/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * Las instancias de esta clase se utilizan para representar los ítems
 * administrados por la herramienta.<br>
 * La representación de un ítem consta de su oid, versión, id del ítem, creador,
 * descripción, t;itulo, responsable actual, fecha de creación, prioridad,
 * estado, nodo del workflow en el que se encuentra, historia del requerimiento
 * y proyecto al que pertenece.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * Esta clase implementa la interface Versionable para poder controlar la
 * edición concurrente del proyecto representado por este dto por parte de dos
 * usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -9084766698041272003L;

	/**
	 * Es el id del ítem representado por este dto.
	 */
	public int id;

	/**
	 * Es el título del ítem representado por este dto.
	 */
	public String title;

	/**
	 * Es la descripción del ítem representado por este dto.
	 */
	public String description;

	/**
	 * Es un dto que representa al usuario responsable del ítem.
	 */
	public AbstractUserDTO responsible;

	/**
	 * Es el dto que representa al usuario líder del proyecto en donde se creó
	 * el ítem.
	 */
	public UserDTO projectLeader;

	/**
	 * Representa el estado de este ítem.
	 */
	public String state;

	/**
	 * Es el dto del proyecto de este ítem.
	 */
	public ProjectDTO project;

	/**
	 * Referencia a la prioridad del ítem representado por este dto.
	 */
	public PriorityDTO priority;

	/**
	 * Referencia al tipo del ítem representado por este dto.
	 */
	public ItemTypeDTO itemType;

	/**
	 * Es el dto que representa al nodo en el que se encuentra el ítem.
	 */
	public WorkflowNodeDTO currentNode;

	/**
	 * Representa la fecha en la que se creó el ítem representado por este dto.
	 */
	public String creationDate;

	/**
	 * Es el creador del ítem representado por este dto.
	 */
	public UserDTO creator;

	/**
	 * Define si el ítem representado por este dto está finalizado.
	 */
	public boolean finished;

	/**
	 * Es un diccionario que contiene los valores de las propiedades adicionales
	 * del ítem representado por este DTO.
	 */
	public Map<String, String> additionalProperties;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el título del ítem.
	 * @param aDescription
	 *            es la descripción del ítem.
	 * @param anId
	 *            es el id del ítem.
	 * @param aResponsible
	 *            es el dto que representa al responsable del ítem.
	 * @param aProjectLeader
	 *            es el líder del proyecto en donde se creó el ítem.
	 * @param aState
	 *            es el estado del ítem.
	 * @param aProject
	 *            es el dto del proyecto de este ítem.
	 * @param aPriorityDTO
	 *            es el dto de la prioridad de este ítem.
	 * @param anOid
	 *            es el oid de este item.
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo del ítem.
	 * @param aWorkflowNodeDTO
	 *            es el dto que representa al nodo actual del ítem.
	 * @param aVersion
	 *            es el número de versión de este ítem.
	 * @param aDate
	 *            es la fecha de creación del ítem.
	 * @param aCreator
	 *            es el dto que representa al creador del ítem.
	 * @param isFinished
	 *            establece si el ítem representado por este dto está
	 *            finalizado.
	 * @param someProperties
	 *            es un diccionario que contiene los valores de las propiedades
	 *            adicionales del ítem representado por este dto.
	 */
	public ItemDTO(String aTitle, String aDescription, int anId,
			AbstractUserDTO aResponsible, UserDTO aProjectLeader,
			String aState, ProjectDTO aProjectDTO, PriorityDTO aPriorityDTO,
			String anOid, ItemTypeDTO anItemTypeDTO,
			WorkflowNodeDTO aWorkflowNodeDTO, int aVersion, String aDate,
			UserDTO aCreator, boolean isFinished,
			Map<String, String> someProperties) {
		this.setTitle(aTitle);
		this.setDescription(aDescription);
		this.setId(anId);
		this.setResponsible(aResponsible);
		this.setProjectLeader(aProjectLeader);
		this.setState(aState);
		this.setProject(aProjectDTO);
		this.setPriority(aPriorityDTO);
		this.setOid(anOid);
		this.setItemType(anItemTypeDTO);
		this.setCurrentNode(aWorkflowNodeDTO);
		this.setVersion(aVersion);
		this.setCreationDate(aDate);
		this.setCreator(aCreator);
		this.setFinished(isFinished);
		this.setAdditionalProperties(someProperties);
	}

	/**
	 * Getter.
	 * 
	 * @return el id del ítem representado por este dto.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id del ítem representado por este dto.
	 */
	public void setId(int anId) {
		this.id = anId;
	}

	/**
	 * Getter.
	 * 
	 * @return el título del ítem representado por este dto.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del ítem representado por este dto.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return la descripción del ítem representado por este dto.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter.
	 * 
	 * @param aDescription
	 *            es la descripción del ítem representado por este dto.
	 */
	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al responsable del ítem representado por
	 *         este dto.
	 */
	public AbstractUserDTO getResponsible() {
		return this.responsible;
	}

	/**
	 * Setter.
	 * 
	 * @param aResponsible
	 *            es el dto que representa al responsable del ítem representado
	 *            por este dto.
	 */
	public void setResponsible(AbstractUserDTO aResponsible) {
		this.responsible = aResponsible;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al líder del proyecto en donde se creó el
	 *         ítem representado por este dto.
	 */
	public UserDTO getProjectLeader() {
		return this.projectLeader;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto que representa al líder del proyecto en donde se
	 *            creó el ítem representado por este dto.
	 */
	public void setProjectLeader(UserDTO aDTO) {
		this.projectLeader = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el estado de este ítem.
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * Setter.
	 * 
	 * @param aState
	 *            es el estado de este item.
	 */
	public void setState(String aState) {
		this.state = aState;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del proyecto de este ítem.
	 */
	public String getProjectName() {
		return this.getProject().getName();
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto de este ítem.
	 */
	public ProjectDTO getProject() {
		return this.project;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 */
	public void setProject(ProjectDTO aProjectDTO) {
		this.project = aProjectDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto de la prioridad.
	 */
	public PriorityDTO getPriority() {
		return this.priority;
	}

	/**
	 * Setter.
	 * 
	 * @param aPriorityDTO
	 *            es el dto de la prioridad.
	 */
	public void setPriority(PriorityDTO aPriorityDTO) {
		this.priority = aPriorityDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al tipo de ítem.
	 */
	public ItemTypeDTO getItemType() {
		return this.itemType;
	}

	/**
	 * Stter.
	 * 
	 * @param anItemType
	 *            es el dto que representa al tipo del ítem.
	 */
	public void setItemType(ItemTypeDTO anItemType) {
		this.itemType = anItemType;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al nodo en el que se encuentra el ítem.
	 */
	public WorkflowNodeDTO getCurrentNode() {
		return this.currentNode;
	}

	/**
	 * Setter.
	 * 
	 * @param currentNode
	 *            es el dto que representa al nodo en el que se encuentra el
	 *            ítem.
	 */
	public void setCurrentNode(WorkflowNodeDTO currentNode) {
		this.currentNode = currentNode;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creación de este ítem.
	 */
	public String getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creación de este ítem.
	 */
	public void setCreationDate(String aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al creador.
	 */
	public UserDTO getCreator() {
		return this.creator;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al creador.
	 */
	public void setCreator(UserDTO anUserDTO) {
		this.creator = anUserDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return true si el ítem representado por este dto está finalizado.
	 */
	public boolean isFinished() {
		return this.finished;
	}

	/**
	 * Setter.
	 * 
	 * @param isFinished
	 *            establece si el ítem representado por este dto está
	 *            finalizado.
	 */
	public void setFinished(boolean isFinished) {
		this.finished = isFinished;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene los valores de las propiedades
	 *         adicionales del ítem representado por este dto.
	 */
	public Map<String, String> getAdditionalProperties() {
		return this.additionalProperties;
	}

	/**
	 * Setter.
	 * 
	 * @param someProperties
	 *            es un diccionario que contiene los valores de las propiedades
	 *            adicionales del ítem representado por este dto.
	 */
	public void setAdditionalProperties(Map<String, String> someProperties) {
		this.additionalProperties = someProperties;
	}

	/**
	 * Verifica si el ítem representado por este DTO contiene una propiedad
	 * adicional con un nombre dado.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad que se está buscando.
	 * @return true en caso de que esté especificada la propiedad cuyo nombre se
	 *         ha recibido; false en cualquier otro caso.
	 */
	public boolean containsAdditionalProperty(String aName) {

		return this.getAdditionalProperties().containsKey(aName);
	}

	/**
	 * Getter.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad que se está recuperando.
	 * @return un string que representa el valor de la propiedad.
	 */
	public String getAdditionalProperty(String aName) {
		String result = "";

		if (this.containsAdditionalProperty(aName)) {
			result = this.getAdditionalProperties().get(aName);
		}

		return result;
	}

}
