/**
 * Este paquete contiene clases que se utilizan en el proceso de 
 * internacionalizaci�n de los mensajes y texto en general de la aplicaci�n.
 */
package zinbig.item.util.i18n;

import zinbig.item.util.IDGenerator;

/**
 * Representa un LOCALE soportado por la aplicaci�n. Contiene b�sicamente el
 * idioma y el pa�s al que pertenece.<br>
 * La aplicaci�n recupera una propiedad de sistema que define el valor por
 * defecto de la localidad.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemLocale {

	/**
	 * Es el identificador de este objeto.
	 */
	protected String oid;

	/**
	 * Mantiene una referencia al c�digo del pa�s representado por este objeto.
	 */
	protected String countryCode;

	/**
	 * Mantiene una referencia al lenguaje del pa�s.
	 */
	protected String languageCode;

	/**
	 * Constructor por defecto.
	 */
	public ItemLocale() {
		this("es", "AR");
	}

	/**
	 * Constructor.
	 * 
	 * @param aLanguageCode
	 *            es el c�digo correspondiente al lenguaje.
	 * @param aCountryCode
	 *            es el c�digo del pa�s al que representa este LOCALE.
	 */
	public ItemLocale(String aLanguageCode, String aCountryCode) {
		this.setCountryCode(aCountryCode);
		this.setLanguageCode(aLanguageCode);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el c�digo del pa�s representado por este objeto.
	 */
	public String getCountryCode() {
		return this.countryCode;
	}

	/**
	 * Setter.
	 * 
	 * @param aCountryCode
	 *            es el c�digo del pa�s al que representar� este objeto.
	 */
	public void setCountryCode(String aCountryCode) {
		this.countryCode = aCountryCode;
	}

	/**
	 * Getter.
	 * 
	 * @return el c�digo del lenguaje representado por este objeto.
	 */
	public String getLanguageCode() {
		return this.languageCode;
	}

	/**
	 * Setter.
	 * 
	 * @param aLanguageCode
	 *            es el c�digo del lenguaje representado por este objeto.
	 */
	public void setLanguageCode(String aLanguageCode) {
		this.languageCode = aLanguageCode;
	}

	/**
	 * Getter.
	 * 
	 * @return el oid de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de este objeto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Verifica si el objeto recibido es igual al receptor. <br>
	 * Este objeto es igual a otro cuando el objeto recibido es una instancia de
	 * la misma clase y ambos tienen los mismos c�digos de lenguaje y pa�s.
	 * 
	 * @return true si son iguales los objetos; false en cualquier otro caso.
	 */
	public boolean equals(Object anObject) {

		boolean result = false;
		try {
			ItemLocale aLocale = (ItemLocale) anObject;
			if (aLocale.getCountryCode().equals(this.getCountryCode())
					&& aLocale.getLanguageCode().equals(this.getLanguageCode())) {
				result = true;
			}
		} catch (ClassCastException cce) {
			result = false;
		}
		return result;

	}
}
