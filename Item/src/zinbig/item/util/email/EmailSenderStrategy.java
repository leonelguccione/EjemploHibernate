/**
 * Este paquete contiene las definiciones de las estrategias de envío de emails 
 * desde el sistema.
 * 
 */
package zinbig.item.util.email;

/**
 * Esta interface define el protocolo básico que debe implementar toda
 * estrategia de envío de emails desde el sistema a los usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface EmailSenderStrategy {

	/**
	 * Envía un administratorEmail a un usuario.
	 * 
	 * @param subject
	 *            es el título del administratorEmail.
	 * @param message
	 *            es el mensaje que deberá contener el administratorEmail.
	 * @param recipientEmail
	 *            es el administratorEmail del usuario.
	 */
	public void sendEmail(String subject, String message, String recipientEmail);

}
