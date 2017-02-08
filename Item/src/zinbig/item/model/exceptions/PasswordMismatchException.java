/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepci�n puede ser lanzada cuando se est� verificando una clave
 * ingresada contra la clave almacenada.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PasswordMismatchException extends Exception {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -7724667061729185535L;

}
