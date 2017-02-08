/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Las instancias de esta clase se utilizan para se�alar el error que se da al
 * intentar subir un archivo adjunto con un nombre repetido.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemFileAlreadyExistsException extends Exception {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 7669453764832584629L;

}
