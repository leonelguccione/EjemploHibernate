/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se levanta cuando se intenta recuperar un tipo de ítem que no
 * existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ItemTypeUnknownException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 5024048313351793532L;

}
