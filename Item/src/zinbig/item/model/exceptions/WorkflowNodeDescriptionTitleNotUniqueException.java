/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se levanta cuando se está tratando de dar de alta una nueva
 * descripción de nodo con un título ya existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowNodeDescriptionTitleNotUniqueException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 1816240525117283729L;

}
