/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para se�alar el hecho de que se est�
 * intentando borrar un tipo de �tem que tiene �tems referenci�ndolo a�n.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTypeInUserException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 7130757044003815274L;

}
