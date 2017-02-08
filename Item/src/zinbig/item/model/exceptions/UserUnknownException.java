/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción puede ser lanzada cuando se está tratando de registrar un
 * usuario desconocido o cuando se está buscando un usuario que no existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserUnknownException extends Exception {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 4059128345901056872L;

}
