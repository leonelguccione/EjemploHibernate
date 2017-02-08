/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

import java.util.Date;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase representan los comentarios que se agregan a los
 * ítems. <BR>
 * Dependiendo de la configuración del sistema, a través de la propiedad de
 * sistema EVERYONE_CAN_COMMENT_AN_ITEM, se puede configurar que cualquier
 * usuario pueda agregar comentarios a un ítem, aún cuando no es el responsable
 * directo del ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Comment {

	/**
	 * Mantiene el oid que permite identificar a esta instancia.
	 */
	protected String oid;

	/**
	 * Referencia el comentario propiamente dicho.
	 */
	protected String comment;

	/**
	 * Es la fecha de creación de este comentario.
	 */
	protected Date creationDate;

	/**
	 * Es el nombre de usuario de la persona que creó este comentario.
	 */
	protected String creatorUsername;

	/**
	 * Constructor por defecto.
	 */
	public Comment() {

	}

	/**
	 * Constructor.
	 * 
	 * @param aComment
	 *            es el comentario propiamente dicho.
	 * @param aDate
	 *            es la fecha de creación de este comentario.
	 * @param anUsername
	 *            es el nombre del usuario que creó este comentario.
	 */
	public Comment(String aComment, Date aDate, String anUsername) {
		this.setOid(IDGenerator.getId());
		this.setComment(aComment);
		this.setCreationDate(aDate);
		this.setCreatorUsername(anUsername);
	}

	/**
	 * Getter.
	 * 
	 * @return el comentario propiamente dicho.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * Setter.
	 * 
	 * @param aComment
	 *            es el comentario propiamente dicho.
	 */
	public void setComment(String aComment) {
		this.comment = aComment;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de creación de este comentario.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creación de este comentario.
	 */
	public void setCreationDate(Date aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del usuario que creó este comentario.
	 */
	public String getCreatorUsername() {
		return this.creatorUsername;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el nombre del usuario que creó este comentario.
	 */
	public void setCreatorUsername(String anUsername) {
		this.creatorUsername = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de este objeto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

}
