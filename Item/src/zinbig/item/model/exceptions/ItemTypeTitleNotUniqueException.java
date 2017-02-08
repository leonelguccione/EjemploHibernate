/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se levanta cuando se intenta dar de alta un tipo de ítem con
 * un título ya existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@gmail.com
 * 
 */
public class ItemTypeTitleNotUniqueException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -2427710511150046473L;

}
