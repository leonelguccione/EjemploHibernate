/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar a los grupos de
 * usuarios. <br>
 * La representación contiene el nombre del grupo, su email y el oid que
 * identifica al grupo.<br>
 * Esta clase implementa la interface Versionable para poder controlar la
 * edición concurrente del grupo de usuarios representado por este dto por parte
 * de dos usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserGroupDTO extends AbstractUserDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 3421397614751725542L;

	/**
	 * Es una coelcción que contiene todos los dtos que representan las
	 * operaciones asignadas al grupo de usuarios representado por este dto.
	 */
	public Collection<OperationDTO> operationDTOs;

	/**
	 * Es una colección que contiene todos los dtos que representan a los
	 * proyectos asignados al grupo de usuarios representado por este dto.
	 */
	public Collection<ProjectDTO> projectDTOs;

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre del grupo representado por este dto.
	 * @param anEmail
	 *            es el email del grupo representado por este dto.
	 * @param aBoolean
	 *            indica si este dto representa un grupo de sistema o de
	 *            proyecto.
	 * @param isDeletable
	 *            indica si el dto representa a un grupo de usuarios que se
	 *            puede eliminar.
	 * @param aNumber
	 *            es el número de la versión del grupo representado por este
	 *            dto.
	 * @param anOid
	 *            es el oid del grupo.
	 * @param aLanguage
	 *            es el nombre del idioma del usuario.
	 * 
	 */
	public UserGroupDTO(String aName, String anEmail, boolean isDeletable,
			int aNumber, String anOid, String aLanguage) {
		super(anEmail, aName, isDeletable, aNumber, aLanguage);
		this.setOperationDTOs(new ArrayList<OperationDTO>());
		this.setProjectDTOs(new ArrayList<ProjectDTO>());
		this.setOid(anOid);

		this.setAlias(aName);

	}

	/**
	 * Getter.
	 * 
	 * @return una colección de dtos que representan las operaciones asignadas
	 *         al grupo de usuarios representado por este dto.
	 */
	public Collection<OperationDTO> getOperationDTOs() {
		return this.operationDTOs;
	}

	/**
	 * Setter.
	 * 
	 * @param someDTOs
	 *            es una colección de dtos que representan las operaciones
	 *            asignadas al grupo de usuarios representado por este dto.
	 */
	public void setOperationDTOs(Collection<OperationDTO> someDTOs) {
		this.operationDTOs = someDTOs;
	}

	/**
	 * Agrega un nuevo dto de operación a la colección.
	 * 
	 * @param aDTO
	 *            es el nuevo dto que debe ser agregado.
	 */
	public void addOperationDTO(OperationDTO aDTO) {
		this.getOperationDTOs().add(aDTO);
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de dtos que representan a los proyectos asignados al
	 *         grupo de usuarios representado por este dto.
	 */
	public Collection<ProjectDTO> getProjectDTOs() {

		return this.projectDTOs;
	}

	/**
	 * Setter.
	 * 
	 * @param someDTOs
	 *            es la colección de dtos que representan a los proyectos
	 *            asignados al grupo de usuarios representado por este dto.
	 */
	public void setProjectDTOs(Collection<ProjectDTO> someDTOs) {
		this.projectDTOs = someDTOs;
	}

	/**
	 * Retorna una representación como string de este objeto.
	 * 
	 * @return el nombre del grupo de usuarios representado por este dto.
	 */
	public String toString() {

		return this.getName();
	}

	/**
	 * Verifica si el objeto recibido es igual al receptor.
	 * 
	 * @param anObject
	 *            es el objeto que hay que comparar.
	 * @return true en caso de que ambos objetos sean instancias de la clase
	 *         UserGroupDTO y que representen al mismo grupo de usuarios (mismo
	 *         oid); false en caso contrario.
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;

		try {
			UserGroupDTO aDTO = (UserGroupDTO) anObject;
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
	 * @return true ya que este dto representa a un grupo de usuarios.
	 */
	@Override
	public boolean isUserGroup() {

		return true;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que representa el hashcode de este objeto. Se calcula
	 *         en base al hashcode del título del título.
	 */
	@Override
	public int hashCode() {

		return this.getOid().hashCode();
	}

}
