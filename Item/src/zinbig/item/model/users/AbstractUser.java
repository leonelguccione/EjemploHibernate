/**
 * Este paquete contiene las clases del modelo de negocios de la aplicación que 
 * representan usuarios.
 */
package zinbig.item.model.users;

import java.util.Date;

import zinbig.item.util.IDGenerator;
import zinbig.item.util.persistence.Versionable;

/**
 * Esta clase representa el tope de la jerarquía de los usuarios del sistema.<br>
 * Esta clase establece el protocolo común que deben implementar las subclases.<br>
 * El patrón de diseño Composite brinda las pautas básicas para el diseño de
 * esta jerarquía.<br>
 * 
 * Esta clase implementa la interfaz Versionable a fin de poder controlar la
 * edición concurrente por parte de dos usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class AbstractUser implements Versionable {

	/**
	 * Mantiene la versión de este objeto.
	 */
	public int version;

	/**
	 * Es el oid que permite identificar a una instancia de esta clase.
	 */
	protected String oid;

	/**
	 * Es el nombre de este usuario abstracto.
	 */
	protected String name;

	/**
	 * Es el email del usuario abstracto.
	 */
	protected String email;

	/**
	 * Es la fecha en la que se creó el usuario abstracto.
	 */
	protected Date creationDate;

	/**
	 * Indica si el grupo puede ser eliminado del sistema. <br>
	 * El grupo de los administradores es el único que no puede ser eliminado.
	 */
	public boolean deletable;

	/**
	 * Es el alias que se debe utilizar para mostrar a este usuario. Cada
	 * subclase es responsable de definir el contenido de este campo.
	 */
	public String alias;

	/**
	 * Mantiene una referencia al idioma del usuario.
	 */
	protected String language;

	/**
	 * Constructor.
	 */
	public AbstractUser() {
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el oid de esta instancia.
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
	 * @return el nombre del usuario.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del usuario.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el email del usuario.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el nuevo email del usuario.
	 */
	public void setEmail(String anEmail) {
		this.email = anEmail;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha en la que fue creado el usuario.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha en la que se creó el usuario.
	 */
	public void setCreationDate(Date aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el usuario pueda ser eliminado; false en caso
	 *         contrario.
	 */
	public boolean isDeletable() {
		return this.deletable;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si el usuario puede ser eliminado.
	 */
	public void setDeletable(boolean aBoolean) {
		this.deletable = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return la versión de este objeto.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es el número de versión de esta instancia.
	 */
	public void setVersion(int aNumber) {
		this.version = aNumber;
	}

	/**
	 * Retorna si el receptor es un usuario individual o un grupo.
	 * 
	 * @return cada subclase define el resultado de este mensaje.
	 */
	public abstract boolean isUserGroup();

	/**
	 * Obtiene una copia de este objeto.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}

	/**
	 * Getter.
	 * 
	 * @return el alias de este usuario.
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Setter.
	 * 
	 * @param anAlias
	 *            es el alias de este usuario.
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
