/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se levantan cuando se intenta recuperar una
 * descripci�n de un enlace en un workflow y dicho enlace no existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowLinkDescriptionUnknownException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -6318150398190100090L;

}
