/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar los comentarios de
 * los �tems.<br>
 * Esta clase implementa la interface Serializable para permitir que se
 * almacenen sus instancias en la sesi�n de trabajo Web.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class CommentDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 1485372682464989396L;

	/**
	 * Es el texto propiamente dicho del comentario.
	 */
	protected String comment;

	/**
	 * Es la fecha de creaci�n del comentario representado por este dto.
	 */
	protected String creationDate;

	/**
	 * Es el nombre del usuario que cre� el comentario representado por este
	 * dto.
	 */
	protected String creatorUsername;

	/**
	 * Constructor.
	 * 
	 * @param aComment
	 *            es el texto del comentario.
	 * @param aDate
	 *            es la fecha de creaci�n del comentario.
	 * @param anUsername
	 *            es el usuario que cre� el comentario.
	 */
	public CommentDTO(String aComment, String aDate, String anUsername) {
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
	 * @return la fecha de creaci�n del comentario.
	 */
	public String getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de creaci�n del comentario.
	 */
	public void setCreationDate(String aDate) {
		this.creationDate = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del usuario que cre� el comentario.
	 */
	public String getCreatorUsername() {
		return this.creatorUsername;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el nombre del usuario que cre� el comentario.
	 */
	public void setCreatorUsername(String anUsername) {
		this.creatorUsername = anUsername;
	}

}
