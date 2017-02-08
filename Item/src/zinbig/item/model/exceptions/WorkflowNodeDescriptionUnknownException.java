/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepci�n se levanta cuando se intenta recuperar una descripci�n de nodo
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
