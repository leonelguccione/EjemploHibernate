/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepci�n puede ser lanzada cuando se est� intentando agregar una nueva
 * prioridad a un grupo de prioridades con un nombre que ya existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PriorityNameNotUniqueException extends Exception {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -1010157744861871373L;

}
