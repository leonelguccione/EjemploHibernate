/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase son utilizadas para representar usuarios del
 * sistema. Los usuarios representados por esta clase puede ser tanto usuarios
 * simples (instancias de la clase User) como grupos de usuarios (instancias de
 * la clase UserGroup). <br>
 * 
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * Es posible que este dto represente a un usuario en forma simple, es decir sin
 * cargar todos los datos relacionados en el modelo de objetos. El objetivo de
 * esto es minimizar el tiempo de carga en pantalla en aquellos casos en los que
 * no se requiere de toda la información asociado al usuario. Para crear un dto
 * completo se debe invocar a la clase DTOFactory con el parámetro
 * mustCreateCompleteDTO en true.<br>
 * 
 * Esta clase implementa la interface Versionable para poder controlar la
 * edición concurrente del usuario/grupo de usuarios representado por este dto
 * por parte de dos usuarios.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class AbstractUserDTO extends ItemAbstractDTO implements
		Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 4806014069751231043L;

	/**
	 * Es el email del usuario representado por este dto.
	 */
	protected String email;

	/**
	 * Es el nombre del usuario.
	 */
	protected String name;

	/**
	 * Indica si el usuario representado por este dto puede ser eliminado.
	 */
	protected boolean deletable;

	/**
	 * Es el alias del usuario representado por este dto.
	 */
	protected String alias;

	/**
	 * Mantiene el nombre del idioma del usuario.
	 */
	protected String language;

	/**
	 * Constructor.<br>
	 * 
	 * 
	 * @param anEmail
	 *            es el email del usuario representado por este dto.
	 * @param aName
	 *            es el nombre del usuario representado por este dto.
	 * @param aBoolean
	 *            indica si el usuario representado por este dto puede ser
	 *            eliminado.
	 * @param aNumber
	 *            es el número de la versión del grupo representado por este
	 *            dto.
	 * @param aLanguage
	 *            es el idioma del usuario.
	 */
	public AbstractUserDTO(String anEmail, String aName, boolean aBoolean,
			int aNumber, String aLanguage) {

		this.setEmail(anEmail);
		this.setName(aName);
		this.setDeletable(aBoolean);
		this.setVersion(aNumber);
		this.setLanguage(aLanguage);

	}

	/**
	 * Getter.
	 * 
	 * @return el email del usuario representado por este dto.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el email del usuario representado por este dto.
	 */
	public void setEmail(String anEmail) {
		this.email = anEmail;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del usuario representado por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del usuario representado por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el usuario representado por este dto pueda
	 *         ser eliminado.
	 */
	public boolean isDeletable() {
		return this.deletable;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si el usuario representado por este dto puede ser
	 *            eliminado.
	 */
	public void setDeletable(boolean aBoolean) {
		this.deletable = aBoolean;
	}

	/**
	 * Retorna una representación como string del receptor.
	 * 
	 * @return el nombre seguido del email.
	 */
	public String toString() {
		return this.getName() + "[" + this.getEmail() + "]";
	}

	/**
	 * Verifica si este dto representa un usuario simple o a un grupo de
	 * usuarios.
	 * 
	 * @return cada subclase debe definir el resultado.
	 */
	public abstract boolean isUserGroup();

	/**
	 * Getter.
	 * 
	 * @return el alias del usuario representado por este dto.
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Setter.
	 * 
	 * @param anAlias
	 *            es el alias del usuario representado por este dto.
	 */
	public void setAlias(String anAlias) {
		this.alias = anAlias;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del idioma del usuario.
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * Setter.
	 * 
	 * @param aLanguage
	 *            es el nombre del idioma del usuario.
	 */
	public void setLanguage(String aLanguage) {
		this.language = aLanguage;
	}

}
