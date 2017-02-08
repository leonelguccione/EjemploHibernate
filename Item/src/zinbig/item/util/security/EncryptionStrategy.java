/**
 * Este paquete contiene clases e interfaces útiles para brindar servicios de 
 * seguridad.
 */
package zinbig.item.util.security;

/**
 * Esta interface establece el comportamiento esperado de una estrategia de
 * encriptación.<br>
 * Clases concretas implementarán algoritmos en particular.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public interface EncryptionStrategy {

	/**
	 * Encripta el string recibido utilizando un algoritmo.
	 * 
	 * @param plaintext
	 *            es el texto a encriptar.
	 * @return un texto encriptado con un algoritmo en particular.
	 */
	public String encrypt(String plaintext);

	/**
	 * Desencripta el string recibido.
	 * 
	 * @param encryptedText
	 *            es el texto que se debe desencriptar.
	 * @return el string desencriptado.
	 */
	public String decrypt(String encryptedText);
}
