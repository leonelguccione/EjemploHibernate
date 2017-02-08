/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase de excepción se utilizan para señalar el hecho
 * de que no se ha encontrado una operación con el nombre dado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class OperationUnknownException extends Exception {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8727579968800652862L;

}
