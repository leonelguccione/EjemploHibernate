/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para señalar el error que se da
 * cuando se intenta agregar un nuevo enlace en el workflow con un nombre que ya
 * existe.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowLinkDescriptionTitleNotUniqueException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -6530819266732568123L;

}
