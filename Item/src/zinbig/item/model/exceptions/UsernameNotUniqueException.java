/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepci�n puede ser lanzada cuando se est� tratando de dar de alta un
 * nuevo usuario con un nombre de usuario (username) ya existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UsernameNotUniqueException extends Exception {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 5760906320799989013L;

}
