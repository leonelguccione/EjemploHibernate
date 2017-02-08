/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para señalar el error al intentar
 * agregar una nueva descripción de propiedad a un proyecto con un nombre ya
 * existente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PropertyDescriptionNameNotUniqueException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 9174879995679587251L;

}
