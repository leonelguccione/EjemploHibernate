/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para señalar el hecho de que se está
 * intentando borrar un tipo de ítem que tiene ítems referenciándolo aún.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTypeInUserException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 7130757044003815274L;

}
