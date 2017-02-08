/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se lanza cuando se trata de agregar un filtro con un nombre
 * que ya se está utilizando.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterNameNotUniqueException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 5716486485897003884L;

}
