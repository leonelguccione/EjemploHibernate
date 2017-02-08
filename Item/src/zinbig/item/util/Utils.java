/**
 * Este paquete contiene clases �tiles del sistema.
 * 
 */
package zinbig.item.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Esta clase se utiliza para poder ejecutar algunas funciones �tiles para el
 * sistema.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Utils {

	/**
	 * Codifica el string recibido utilizando la codificaci�n UTF-8.
	 * 
	 * @param aString
	 *            es el string que debe codificarse.
	 * @return el string codificado. En caso de excepciones en la cidificaci�n
	 *         se devuelve el mismo string.
	 */
	public static String encodeString(String aString) {
		String result = aString;

		try {
			result = URLEncoder.encode(aString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// no se debe hacer nada.
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Decodifica el string recibido utilizando la codificaci�n UTF-8.
	 * 
	 * @param aString
	 *            es el string que debe decodificarse.
	 * @return el string decodificado. En caso de excepciones en la cidificaci�n
	 *         se devuelve el mismo string.
	 */
	public static String decodeString(String aString) {
		String result = aString;

		try {
			result = URLDecoder.decode(aString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// no se debe hacer nada.
			e.printStackTrace();
		}

		return result;
	}

}
