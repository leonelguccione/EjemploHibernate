/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para se�alar el intento de edici�n
 * de un �tem que se encuentra cerrado o bloqueado.
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
