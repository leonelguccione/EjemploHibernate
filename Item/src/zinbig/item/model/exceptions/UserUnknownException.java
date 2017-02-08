/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepci�n puede ser lanzada cuando se est� tratando de registrar un
 * usuario desconocido o cuando se est� buscando un usuario que no existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserUnknownException extends Exception {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 4059128345901056872L;

}
