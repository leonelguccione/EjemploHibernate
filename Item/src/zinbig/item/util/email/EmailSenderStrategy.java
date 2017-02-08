/**
 * Este paquete contiene las definiciones de las estrategias de env�o de emails 
 * desde el sistema.
 * 
 */
package zinbig.item.util.email;

/**
 * Esta interface define el protocolo b�sico que debe implementar toda
 * estrategia de env�o de emails desde el sistema a los usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface EmailSenderStrategy {

	/**
	 * Env�a un administratorEmail a un usuario.
	 * 
	 * @param subject
	 *            es el t�tulo del administratorEmail.
	 * @param message
	 *            es el mensaje que deber� contener el administratorEmail.
	 * @param recipientEmail
	 *            es el administratorEmail del usuario.
	 */
	public void sendEmail(String subject, String message, String recipientEmail);

}
