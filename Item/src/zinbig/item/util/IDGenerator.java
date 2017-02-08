/**
 * Este paquete contiene clases útiles para la herramienta.
 */
package zinbig.item.util;

import java.util.UUID;

/**
 * Esta clase se utiliza para generar números aleatorios para asignarlos como
 * identificadores de los objetos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class IDGenerator {

	/**
	 * Genera un número aleatorio de 16 bytes.
	 * 
	 * @return un string en base a un número aleatorio de 16 bytes.
	 */
	public static String getId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
