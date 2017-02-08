/**
 * Este paquete contiene clases e interfaces útiles para brindar servicios de 
 * seguridad.
 */
package zinbig.item.util.security;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * Las instancias de esta clase se utilizan para encriptar información
 * utilizando el algoritmo SHA64.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SHA64EncryptionStrategy implements EncryptionStrategy,
		Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -6602233366344640446L;

	/**
	 * Encripta el mensaje recibido utilizando el algoritmo SHA64.
	 * 
	 * @param plaintext
	 *            texto a ser encriptado.
	 * @return un string con el texto encriptado con el algoritmo SHA64.
	 */
	public synchronized String encrypt(String plaintext) {

		MessageDigest md = null;

		try {

			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {

		}

		try {

			md.update(plaintext.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {

		}

		byte[] raw = md.digest();
		String hash = (new BASE64Encoder()).encode(raw);

		return hash;
	}

	/**
	 * Este algoritmo es simétrico, por lo que no se puede desencriptar el texto
	 * recibido.
	 * 
	 * @param encryptedText
	 *            es el texto que se debe desencriptar.
	 * @return el string recibido.
	 */
	public String decrypt(String encryptedText) {
		return encryptedText;
	}

}
