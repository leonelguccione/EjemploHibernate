/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando est� ejcutando la l�gica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepci�n se lanza cada vez que dos usuarios est�n modificando
 * concurrente el mismo objeto del modelo y uno de los dos guarda sus cambios,
 * haciendo inv�lida la edici�n del segundo usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemConcurrentModificationException extends Exception {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 2082046965228011290L;

}
