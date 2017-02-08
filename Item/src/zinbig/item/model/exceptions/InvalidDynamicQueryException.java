/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta excepción se utilizan para señalar el error al
 * intentar crear una propiedad adicional para los ítems con una consulta
 * inválida.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class InvalidDynamicQueryException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 292394614854725347L;

}
