/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se levanta cuando se intenta buscar un grupo de usuarios con
 * un nombre que no existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserGroupUnknownException extends Exception {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 1412565519396614827L;

}
