/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;
import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar la información de
 * los proyectos.<br>
 * La representación de un proyecto consiste de su nombre y su oid.<br>
 * 
 * Esta clase implementa la interface Versionable para poder controlar la
 * edición concurrente del proyecto representado por este dto por parte de dos
 * usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ProjectDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 7650660489068964349L;

	/**
	 * Es el nombre del proyecto representado por este dto.
	 */
	public String name;

	/**
	 * Es el nombre corto del proyecto.
	 */
	public String shortName;

	/**
	 * Es el link a la página web del proyecto.
	 */
	public String link;

	/**
	 * Indica si el proyecto representado por este dto es público.
	 */
	public boolean publicProject;

	/**
	 * Mantiene la referencia la dto que representa al grupo de prioridades del
	 * proyecto representado por este dto.
	 */
	public PrioritySetDTO prioritySetDTO;

	/**
	 * Mantiene el nombre de la clase de la estrategia de asignación de
	 * responsables a los ítems.
	 */
	public String itemAssignmentStrategy;

	/**
	 * Es el dto que representa al líder del proyecto representado por este dto.
	 */
	public UserDTO projectLeaderDTO;

	/**
	 * Mantiene la cantidad de ítems del proyecto representado por este dto.
	 */
	public Long itemsCount;

	/**
	 * Es el DTo que representa al workflow de este proyecto.
	 */
	public WorkflowDescriptionDTO workflowDescriptionDTO;

	/**
	 * Esta colección contiene las definiciones de propiedades adicionales
	 * definidas para este proyecto.
	 */
	public Collection<PropertyDescriptionDTO> additionalPropertyDescriptions;

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre del proyecto que será representado por este dto.
	 * @param anOid
	 *            es el identificador del proyecto representado por este dto.
	 * @param aShortName
	 *            es el nombre corto del proyecto.
	 * @param aLink
	 *            es el link a la página web del proyecto representado por este
	 *            dto.
	 * @param aNumber
	 *            representa la versión del proyecto representado por este dto.
	 * @param aBoolean
	 *            indica si el proyecto representado por este dto es público.
	 * @param aStrategyClassName
	 *            es el nombre de la clase de la estrategia de asignación de
	 *            responsables para los ítems.
	 * @param anUserDTO
	 *            es el dto que representa al líder del proyecto.
	 * @param aCount
	 *            es la cantidad de ítems del proyecto representado por este
	 *            dto.
	 * @param aWorkflowDescriptionDTO
	 *            es el dto que representa a la descripción del workflow del
	 *            proyecto.
	 * @param somePropertyDescriptions
	 *            es una colección que contiene las descripciones de propiedades
	 *            adicionales de este proyecto.
	 */
	public ProjectDTO(String aName, String aShortName, String anOid,
			String aLink, int aNumber, boolean aBoolean,
			String aStrategyClassName, UserDTO anUserDTO, Long aCount,
			WorkflowDescriptionDTO aWorkflowDescriptionDTO,
			Collection<PropertyDescriptionDTO> somePropertyDescriptions) {
		this.setName(aName);
		this.setShortName(aShortName);
		this.setOid(anOid);
		this.setLink(aLink);
		this.setVersion(aNumber);
		this.setPublicProject(aBoolean);
		this.setItemAssignmentStrategy(aStrategyClassName);
		this.setProjectLeaderDTO(anUserDTO);
		this.setItemsCount(aCount);
		this.setWorkflowDescriptionDTO(aWorkflowDescriptionDTO);
		this.setAdditionalPropertyDescriptions(somePropertyDescriptions);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del proyecto representado por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del proyecto representado por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre corto del proyecto representado por este dto.
	 */
	public String getShortName() {
		return this.shortName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre corto del proyecto representado por este dto.
	 */
	public void setShortName(String aName) {
		this.shortName = aName;
	}

	/**
	 * Verifica si el objeto recibido es igual al receptor.
	 * 
	 * @param anObject
	 *            es el objeto que hay que comparar.
	 * @return true en caso de que ambos objetos sean instancias de la clase
	 *         ProjectDTO y que representen al mismo proyecto (mismo oid); false
	 *         en caso contrario.
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;

		try {
			ProjectDTO aDTO = (ProjectDTO) anObject;
			if (aDTO.getOid().equals(this.getOid())) {
				result = true;
			}
		} catch (Exception e) {
			// no se debe hacer nada ya que probablemente haya fallado debido a
			// una excepción de casting.
		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return el link a la página web del proyecto representado por este DTO.
	 */
	public String getLink() {
		return this.link;
	}

	/**
	 * Setter.
	 * 
	 * @param aLink
	 *            es el link a la página web del proyecto representado por este
	 *            dto.
	 */
	public void setLink(String aLink) {
		this.link = aLink;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el proyecto representado por este dto sea
	 *         público; false en caso contrario.
	 */
	public boolean isPublicProject() {
		return this.publicProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si el proyecto representado por este dto es público o
	 *            no.
	 */
	public void setPublicProject(boolean aBoolean) {
		this.publicProject = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del grupo de prioridades del proyecto representado por
	 *         este dto.
	 */
	public PrioritySetDTO getPrioritySetDTO() {
		return this.prioritySetDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto del grupo de prioridades del proyecto representado
	 *            por este dto.
	 */
	public void setPrioritySetDTO(PrioritySetDTO aDTO) {
		this.prioritySetDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que presenta el hashcode de este objeto.
	 */
	@Override
	public int hashCode() {

		return (this.getName() + this.getShortName()).hashCode();
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase de la estrategia de asignación de
	 *         responsables.
	 */
	public String getItemAssignmentStrategy() {

		return this.itemAssignmentStrategy;
	}

	/**
	 * Setter.
	 * 
	 * @param aString
	 *            es el nombre de la clase de la estrategia de asignación de
	 *            responsables.
	 */
	public void setItemAssignmentStrategy(String aString) {
		this.itemAssignmentStrategy = aString;
	}

	/**
	 * Getter.
	 * 
	 * @return un string que contiene el nombre de este dto de proyecto.
	 */
	public String toString() {
		return this.getName();
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al líder del proyecto representado por este
	 *         dto.
	 */
	public UserDTO getProjectLeaderDTO() {
		return this.projectLeaderDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al líder del proyecto representado
	 *            por este dto.
	 */
	public void setProjectLeaderDTO(UserDTO anUserDTO) {
		this.projectLeaderDTO = anUserDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de ítems del proyecto representado por este dto.
	 */
	public Long getItemsCount() {
		return this.itemsCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de ítems del proyecto representado por este
	 *            dto.
	 */
	public void setItemsCount(Long aNumber) {
		this.itemsCount = aNumber;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa la descripción del workflow de este
	 *         proyecto.
	 */
	public WorkflowDescriptionDTO getWorkflowDescriptionDTO() {
		return this.workflowDescriptionDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el dto que representa la descripción del workflow de este
	 *            proyecto.
	 */
	public void setWorkflowDescriptionDTO(WorkflowDescriptionDTO aDTO) {
		this.workflowDescriptionDTO = aDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de descripciones de propiedades adicionales del
	 *         proyecto representado por este dto.
	 */
	public Collection<PropertyDescriptionDTO> getAdditionalPropertyDescriptions() {
		return this.additionalPropertyDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de descripciones de propiedades adicionales
	 *            del proyecto representado por este dto.
	 */
	public void setAdditionalPropertyDescriptions(
			Collection<PropertyDescriptionDTO> aCollection) {
		this.additionalPropertyDescriptions = aCollection;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el proyecto representado por este dto
	 *         contenga definiciones de propiedades adicionales; false en caso
	 *         contrario.
	 */
	public boolean hasAdditionalProperties() {
		return !this.getAdditionalPropertyDescriptions().isEmpty();
	}

}
