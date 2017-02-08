/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para señalar el intento de edición
 * de un ítem que se encuentra cerrado o bloqueado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemClosedException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 409474572046433073L;

}
