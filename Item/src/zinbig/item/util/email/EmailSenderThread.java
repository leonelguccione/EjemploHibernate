/**
 * Este paquete contiene las definiciones de las estrategias de envío de emails 
 * desde el sistema.
 * 
 */
package zinbig.item.util.email;

/**
 * Las instancias de esta clase se utilizan para enviar emails a los usuarios en
 * un thread de ejecución distinto al de la aplicación.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EmailSenderThread extends Thread {
	/**
	 * Es la estrategia de envío de emails.
	 */
	private EmailSenderStrategy strategy;

	/**
	 * Es el título del administratorEmail.
	 */
	private String subject;

	/**
	 * Es el mensaje que se debe enviar.
	 */
	private String message;

	/**
	 * Es el administratorEmail del receptor.
	 */
	private String email;

	/**
	 * Constructor.
	 * 
	 * @param aStrategy
	 *            es la estrategia de envío de administratorEmail.
	 * @param aSubject
	 *            es el título del administratorEmail.
	 * @param aMessage
	 *            es el contenido del administratorEmail.
	 * @param anEmal
	 *            es la dirección de correo del administratorEmail.
	 */
	public EmailSenderThread(EmailSenderStrategy aStrategy, String aSubject,
			String aMessage, String anEmal) {
		this.setEmail(anEmal);
		this.setSubject(aSubject);
		this.setMessage(aMessage);
		this.setStrategy(aStrategy);
	}

	/**
	 * Arranca la ejecución de este hilo.<br>
	 * Envía el administratorEmail al usuario.
	 * 
	 */
	@Override
	public void run() {

		super.run();

		this.getStrategy().sendEmail(this.getSubject(), this.getMessage(),
				this.getEmail());

	}

	/**
	 * Getter.
	 * 
	 * @return la estrategia de envío de administratorEmail.
	 */
	public EmailSenderStrategy getStrategy() {
		return this.strategy;
	}

	/**
	 * Setter.
	 * 
	 * @param aStrategy
	 *            es la estrategia de envío de administratorEmail.
	 */
	public void setStrategy(EmailSenderStrategy aStrategy) {
		this.strategy = aStrategy;
	}

	/**
	 * Getter.
	 * 
	 * @return el título del administratorEmail.
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * Setter.
	 * 
	 * @param aSubject
	 *            es el título del administratorEmail.
	 */
	public void setSubject(String aSubject) {
		this.subject = aSubject;
	}

	/**
	 * Getter.
	 * 
	 * @return el mensaje que se debe enviar al usuario.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Setter.
	 * 
	 * @param aMessage
	 *            es el mensaje que se debe enviar al usuario.
	 */
	public void setMessage(String aMessage) {
		this.message = aMessage;
	}

	/**
	 * Getter.
	 * 
	 * @return el administratorEmail del destinatario.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el administratorEmail del destinatario.
	 */
	public void setEmail(String anEmail) {
		this.email = anEmail;
	}

}
