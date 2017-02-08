/**
 * Este paquete contiene las definiciones de las estrategias de envío de emails 
 * desde el sistema.
 * 
 */
package zinbig.item.util.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Las instancias de esta clase se utilizan para enviar emails a los usuarios a
 * través de una cuenta segura de Gmail. <br>
 * Esta clase implementa la interface EmailSenderStrategy par poder hacerla
 * polimórfica con otras posibles formas de envío de emails.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SMTPSEmailSenderStrategy implements EmailSenderStrategy {

	/**
	 * Mantiene el nombre del servidor de salida.
	 */
	private String smtpHostName;

	/**
	 * Mantiene el número del puerto del servidor de salida.
	 */
	private int smtpHostPort;

	/**
	 * Mantiene el nombre del usuario propietario de la cuenta de
	 * administratorEmail.
	 * 
	 */
	private String smtpAuthUser;

	/**
	 * Mantiene la clave de la cuenta de administratorEmail.
	 */
	private String smtpAuthPwd;

	/**
	 * Envía un administratorEmail con el título y contenido recibido.
	 * 
	 * @param subject
	 *            es el título del administratorEmail.
	 * @param emailContent
	 *            es el mensaje que debe enviarse.
	 * @param recipientEmail
	 *            es el administratorEmail del destinatario.
	 */
	@Override
	public void sendEmail(String subject, String emailContent,
			String recipientEmail) {

		try {
			Properties props = new Properties();

			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", this.getSmtpHostName());
			props.put("mail.smtps.auth", "true");
			props.put("mail.smtps.quitwait", "false");

			Session mailSession = Session.getDefaultInstance(props);
			mailSession.setDebug(false);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.setContent(emailContent, "text/html");

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recipientEmail));

			transport.connect(this.getSmtpHostName(), this.getSmtpHostPort(),
					this.getSmtpAuthUser(), this.getSmtpAuthPwd());

			transport.sendMessage(message, message
					.getRecipients(Message.RecipientType.TO));
			transport.close();

		} catch (AddressException e) {

			e.printStackTrace();
		} catch (NoSuchProviderException e) {

			e.printStackTrace();
		} catch (MessagingException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del servidor de salida.
	 */
	public String getSmtpHostName() {
		return this.smtpHostName;
	}

	/**
	 * Setter.
	 * 
	 * @param aHostName
	 *            es el nombre del servidor de salida.
	 */
	public void setSmtpHostName(String aHostName) {
		this.smtpHostName = aHostName;
	}

	/**
	 * Getter.
	 * 
	 * @return el número del puerto del servidor de salida.
	 */
	public int getSmtpHostPort() {
		return this.smtpHostPort;
	}

	/**
	 * Setter.
	 * 
	 * @param aPort
	 *            es el número del puerto de salida.
	 */
	public void setSmtpHostPort(int aPort) {
		this.smtpHostPort = aPort;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del usuario propietario de la cuenta de
	 *         administratorEmail.
	 */
	public String getSmtpAuthUser() {
		return this.smtpAuthUser;
	}

	/**
	 * Setter.
	 * 
	 * @param aSmtpAuthUser
	 *            es el nombre del usuario propietario de la cuenta de
	 *            administratorEmail.
	 */
	public void setSmtpAuthUser(String aSmtpAuthUser) {
		this.smtpAuthUser = aSmtpAuthUser;
	}

	/**
	 * Getter.
	 * 
	 * @return la clave de la cuenta de administratorEmail.
	 */
	public String getSmtpAuthPwd() {
		return this.smtpAuthPwd;
	}

	/**
	 * Setter.
	 * 
	 * @param aPassword
	 *            es la clave de la cuenta de administratorEmail.
	 */
	public void setSmtpAuthPwd(String aPassword) {
		this.smtpAuthPwd = aPassword;
	}

}
