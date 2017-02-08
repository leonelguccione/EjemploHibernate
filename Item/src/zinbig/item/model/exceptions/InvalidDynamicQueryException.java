/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta excepci�n se utilizan para se�alar el error al
 * intentar crear una propiedad adicional para los �tems con una consulta
 * inv�lida.
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
