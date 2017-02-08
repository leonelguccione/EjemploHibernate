/**
 * Este paquete contiene todas las definiciones de las excepciones que pueden
 * ser lanzadas por el sistema cuando está ejcutando la lógica de negocios.
 */
package zinbig.item.model.exceptions;

/**
 * Esta excepción se lanza cada vez que dos usuarios están modificando
 * concurrente el mismo objeto del modelo y uno de los dos guarda sus cambios,
 * haciendo inválida la edición del segundo usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemConcurrentModificationException extends Exception {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2082046965228011290L;

}
