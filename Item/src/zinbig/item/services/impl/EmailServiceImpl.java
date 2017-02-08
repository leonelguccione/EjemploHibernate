/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import zinbig.item.application.ItemApplication;
import zinbig.item.services.bi.EmailServiceBI;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.email.EmailSenderStrategy;
import zinbig.item.util.email.EmailSenderThread;
import zinbig.item.util.i18n.ItemStringResourceLoader;

/**
 * Las instancias de esta clase se utilizan para enviar emails a los usuarios
 * del sistema.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EmailServiceImpl extends BaseServiceImpl implements EmailServiceBI {

	/**
	 * Es el colaborador de esta clase que representa la estrategia de envío de
	 * emails que se debe utilizar. Una estrategia puede ser por ejemplo IMAP.
	 */
	protected EmailSenderStrategy emailSenderStrategy;

	/**
	 * Es un colaborador que permite recuperar los strings internacionalizados.
	 */
	protected ItemStringResourceLoader itemStringResourceLoader;

	/**
	 * Es el objeto que representa la aplicación.
	 */
	public ItemApplication application;

	/**
	 * Envía un email al usuario apara recordarle la clave generada
	 * automáticamente.
	 * 
	 * @param anUserDTO
	 *            es el usuario al que se le debe enviar la clave.
	 * @return true en caso de que se haya podido enviar el correo; false en
	 *         caso contrario.
	 */
	@Override
	public boolean sendEmailToRememberPassword(UserDTO anUserDTO) {

		String contents = this.getEmailContent("rememberPassword.html");

		if (!contents.equals("")) {
			Locale aLocale = new Locale(anUserDTO.getLanguage());
			contents = contents.toString().replace("${password}",
					anUserDTO.getPassword());
			contents = contents.replace("${header}", this.getString(aLocale,
					"PasswordResetEmailHeader"));
			contents = contents.replace("${title}", this.getString(aLocale,
					"PasswordResetEmailTitle")
					+ ":");

			this.sendEmail(
					this.getString(aLocale, "PasswordResetEmailSubject"),
					contents, anUserDTO.getEmail());
		}
		return true;
	}

	/**
	 * Recupera un string internacionalizado.
	 * 
	 * @param aLocale
	 *            es le objeto que representa la ubicación del usuario.
	 * @param aKey
	 *            es la clave para recuperar un mensaje internacionalizado.
	 * @return el string que corresponde a la clave recibida.
	 */
	private String getString(Locale aLocale, String aKey) {

		return this.getItemStringResourceLoader().loadStringResource(null,
				aKey, aLocale, null);
	}

	/**
	 * Envía un administratorEmail a un usuario en un thread para no bloquear la
	 * ejecución de la aplicación.
	 * 
	 * @param aSubject
	 *            es el título del mail.
	 * @param aMessage
	 *            es el mensaje que se debe enviar el usuario.
	 * @param anEmail
	 *            es el administratorEmail del usuario
	 */
	public void sendEmail(String aSubject, String aMessage, String anEmail) {

		EmailSenderThread thread = new EmailSenderThread(this
				.getEmailSenderStrategy(), aSubject, aMessage, anEmail);
		thread.start();

	}

	/**
	 * Getter.
	 * 
	 * @return la estrategia de envío de emails que se debe utilizar en la
	 *         aplicación.
	 */
	public EmailSenderStrategy getEmailSenderStrategy() {
		return this.emailSenderStrategy;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmailSenderStrategy
	 *            es la estrategia de envío de emails que se debe utilizar en la
	 *            aplicación.
	 */
	public void setEmailSenderStrategy(EmailSenderStrategy anEmailSenderStrategy) {
		this.emailSenderStrategy = anEmailSenderStrategy;
	}

	/**
	 * Getter.
	 * 
	 * @return el colaborador que permite recuperar los mensajes
	 *         internacionalizados.
	 */
	public ItemStringResourceLoader getItemStringResourceLoader() {
		return this.itemStringResourceLoader;
	}

	/**
	 * Setter.
	 * 
	 * @param aResourceLoader
	 *            es el colaborador que permite recuperar los mensajes
	 *            internacionalizados.
	 */
	public void setItemStringResourceLoader(
			ItemStringResourceLoader aResourceLoader) {
		this.itemStringResourceLoader = aResourceLoader;
	}

	/**
	 * Getter.
	 * 
	 * @return el objeto que representa la aplicación que utiliza este servicio.
	 */
	public ItemApplication getApplication() {
		return this.application;
	}

	/**
	 * Setter.
	 * 
	 * @param anApplication
	 *            es el objeto que representa la aplicación que utiliza este
	 *            servicio.
	 */
	public void setApplication(ItemApplication anApplication) {
		this.application = anApplication;
	}

	/**
	 * Envía un email con información de un ítem a un usuario en particular.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está notificando.
	 * @param anUserDTO
	 *            es el dto que representa al usuario al cual se debe enviar el
	 *            email.
	 * @return true en caso de que se haya podido enviar el email; false en caso
	 *         contrario.
	 */
	public boolean sendEmailForItemToUser(ItemDTO anItemDTO,
			AbstractUserDTO anUserDTO) {
		String contents = this.getEmailContent("itemEmail.html");

		if (!contents.equals("")) {
			Locale aLocale = new Locale(anUserDTO.getLanguage());
			contents = contents.toString().replace("${id}",
					new Integer(anItemDTO.getId()).toString());
			contents = contents.toString().replace("${title}",
					Utils.decodeString(anItemDTO.getTitle()));
			contents = contents.toString().replace("${description}",
					Utils.decodeString(anItemDTO.getDescription()));
			contents = contents.toString().replace("${creation}",
					anItemDTO.getCreationDate());
			contents = contents.toString().replace("${creator}",
					anItemDTO.getCreator().getAlias());
			contents = contents.toString().replace("${responsible}",
					anItemDTO.getResponsible().getAlias());
			contents = contents.toString().replace("${node}",
					anItemDTO.getCurrentNode().getTitle());
			contents = contents.toString().replace("${type}",
					anItemDTO.getItemType().getTitle());
			contents = contents.toString().replace("${priority}",
					anItemDTO.getPriority().getName());
			contents = contents.toString().replace("${project}",
					anItemDTO.getProjectName());

			contents = contents.toString().replace("${projectLabel}",
					this.getString(aLocale, "projectLabel"));
			contents = contents.toString().replace("${idLabel}",
					this.getString(aLocale, "idLabel"));
			contents = contents.toString().replace("${titleLabel}",
					this.getString(aLocale, "titleLabel"));
			contents = contents.toString().replace("${descriptionLabel}",
					this.getString(aLocale, "descriptionLabel"));
			contents = contents.toString().replace("${creationLabel}",
					this.getString(aLocale, "creationLabel"));
			contents = contents.toString().replace("${creatorLabel}",
					this.getString(aLocale, "creatorLabel"));
			contents = contents.toString().replace("${responsibleLabel}",
					this.getString(aLocale, "responsibleLabel"));
			contents = contents.toString().replace("${nodeLabel}",
					this.getString(aLocale, "nodeLabel"));
			contents = contents.toString().replace("${typeLabel}",
					this.getString(aLocale, "typeLabel"));
			contents = contents.toString().replace("${priorityLabel}",
					this.getString(aLocale, "priorityLabel"));

			this.sendEmail(this.getString(aLocale, "ItemEmailSubject"),
					contents, anUserDTO.getEmail());
		}
		return true;
	}

	/**
	 * Recupera el contenido de un archivo que luego será utilizado para
	 * componer el email.
	 * 
	 * @param aFilename
	 *            es el nombre del archivo que contiene el template.
	 * @return un string que representa el contenido del email.
	 */
	private String getEmailContent(String aFilename) {
		StringBuilder contents = new StringBuilder();

		try {

			BufferedReader input = new BufferedReader(new FileReader(new File(
					this.getApplication().getPath(aFilename))));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return contents.toString();
	}

}
