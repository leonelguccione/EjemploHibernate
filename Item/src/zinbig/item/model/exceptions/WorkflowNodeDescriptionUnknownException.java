/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se levanta cuando se intenta recuperar una descripción de nodo
 * de workflow que no existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowNodeDescriptionUnknownException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -4992332953391795122L;

}
