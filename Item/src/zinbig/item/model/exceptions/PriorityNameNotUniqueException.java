/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción puede ser lanzada cuando se está intentando agregar una nueva
 * prioridad a un grupo de prioridades con un nombre que ya existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PriorityNameNotUniqueException extends Exception {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -1010157744861871373L;

}
