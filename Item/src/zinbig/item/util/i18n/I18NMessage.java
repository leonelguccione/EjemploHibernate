/**
 * Este paquete contiene clases utilizadas para internacionalizar la aplicación.
 */
package zinbig.item.util.i18n;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase representan mensajes que se deben
 * internacionalizar y persistir en el repositorio para su posterior
 * recuperación.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class I18NMessage {

	/**
	 * Es el mensaje que se debe mostrar.
	 */
	protected String message;

	/**
	 * Es el identificador de este mensaje.
	 */
	public String oid;

	/**
	 * Es la representación como String del locale al que corresponde este
	 * mensaje.
	 */
	protected String locale;

	/**
	 * Es la clave por la cual se busca a este mensaje.
	 */
	protected String messageKey;

	/**
	 * Constructor.
	 */
	public I18NMessage() {
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el mensaje internacionalizado.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Setter.
	 * 
	 * @param aMessage
	 *            es el mensaje correspondiente a este objeto.
	 */
	public void setMessage(String aMessage) {
		this.message = aMessage;
	}

	/**
	 * Getter.
	 * 
	 * @return la representación como String del locale al que corresponde este
	 *         mensaje.
	 */
	public String getLocale() {
		return this.locale;
	}

	/**
	 * Setter.
	 * 
	 * @param aLocaleString
	 *            es la representación como String del locale al que corresponde
	 *            este mensaje.
	 */
	public void setLocale(String aLocaleString) {
		this.locale = aLocaleString;
	}

	/**
	 * Getter.
	 * 
	 * @return la clave a la cual se asocia este mensaje.
	 */
	public String getMessageKey() {
		return this.messageKey;
	}

	/**
	 * Setter.
	 * 
	 * @param aKey
	 *            es la clave bajo la cual se almacena este mensaje.
	 */
	public void setMessageKey(String aKey) {
		this.messageKey = aKey;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid que identificará a este objeto
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

}
