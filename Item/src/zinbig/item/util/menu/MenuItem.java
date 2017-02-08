/**
 * Este paquete contiene clases �tiles para armar el men� de la aplicaci�n.
 */
package zinbig.item.util.menu;

import java.io.Serializable;
import java.text.Collator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import zinbig.item.application.pages.ErrorPage;
import zinbig.item.util.dto.OperationDTO;

/**
 * Las instancias de esta clase se utilizan para representar opciones
 * individuales del men�.<br>
 * Las instancias tienen un t�tulo (que se muestra) y el nombre de una clase que
 * representa la p�gina a la que se debe enviar al usuario al seleccionar este
 * �tem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MenuItem implements Serializable, Comparable<MenuItem> {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 7435024119163663120L;

	/**
	 * Es el t�tulo de este �tem de men�.
	 */
	protected String title;

	/**
	 * Es el nombre de clase que representa la p�gina a la que se debe acceder a
	 * trav�s de este �tem.
	 */
	protected String targetPageClassName;

	/**
	 * Es un diccionario que contiene par�metros requeridos por la p�gina de
	 * destino.
	 */
	protected Map<String, String> parameters;

	/**
	 * Es el objeto que representa la localidad actual.
	 */
	protected Locale locale;

	/**
	 * Es un entero que indica en que secci�n dle men� debe aparecer este �tem.
	 * Se utiliza para ordenar los �tems de men�.
	 */
	protected int menuSection;

	/**
	 * Constructor.
	 * 
	 * @param aLocale
	 *            es el objeto que representa la localidad.
	 */
	public MenuItem(Locale aLocale) {
		this.setTitle("");
		this.setTargetPageClassName("");
		this.setParameters(new HashMap<String, String>());
		this.setLocale(aLocale);
		this.setMenuSection(0);

	}

	/**
	 * Constructor.
	 * 
	 * @param aDTO
	 *            es el dto que representa a la operaci�n que se intenta
	 *            ejecutar por medio de este �tem de men�.
	 * @param aLocale
	 *            es el objeto que representa la localidad.
	 */
	public MenuItem(OperationDTO aDTO, Locale aLocale) {
		this.setTitle(aDTO.getName());
		this.setTargetPageClassName(aDTO.getTargetPageClassName());
		this.setParameters(new HashMap<String, String>());
		if (aDTO.hasParameters()) {
			this.getParameters().putAll(aDTO.getParameters());
		}
		this.setLocale(aLocale);
		this.setMenuSection(aDTO.getMenuSection());
	}

	/**
	 * Getter.
	 * 
	 * @return el t�tulo de este �tem de men�.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo de este �tem de men�.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return la clase que representa la p�gina a la que hay que dirigir al
	 *         usuario cuando selecciona este �tem del men�.
	 */
	@SuppressWarnings("unchecked")
	public Class getTargetPageClass() {
		Class result = null;
		try {
			result = Class.forName(this.getTargetPageClassName());
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			result = ErrorPage.class;
		}
		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase que representa la p�gina a la que hay que
	 *         enviar al usuario al seleccionar este �tem del men�.
	 */
	public String getTargetPageClassName() {
		return this.targetPageClassName;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            es el nombre de la clase que representa la p�gina a la que hay
	 *            que enviar al usuario al seleccionar este �tem del men�.
	 */
	public void setTargetPageClassName(String aClassName) {
		this.targetPageClassName = aClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario con los pa�metros requeridos por la p�gina a la
	 *         que se env�a al usuario.
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es un diccionario con los pa�metros requeridos por la p�gina a
	 *            la que se env�a al usuario.
	 */
	public void setParameters(Map<String, String> aMap) {
		this.parameters = aMap;
	}

	/**
	 * Agrega un nuevo par�metro.
	 * 
	 * @param aKey
	 *            es la clave del nuevo par�metro.
	 * @param aValue
	 *            es el valor del nuevo par�metro.
	 */
	public void addParameter(String aKey, String aValue) {
		this.getParameters().put(aKey, aValue);
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que este �tem de men� tenga par�metros; false en
	 *         caso contrario.
	 */
	public boolean hasParameters() {
		return !this.getParameters().isEmpty();
	}

	/**
	 * Compara al receptor con el par�metro para definir el orden. La
	 * comparaci�n se lleva a cabo por el t�tutlo del men�.
	 * 
	 * @return -1 si el receptor es menor que el objeto recibido; 0 si son
	 *         iguales y 1 si el objeto recibido es menor que receptor.
	 */
	@Override
	public int compareTo(MenuItem aMenuItem) {
		Collator collator = Collator.getInstance(locale);
		return collator.compare(this.getTitle(), aMenuItem.getTitle());
	}

	/**
	 * Retorna una representaci�n como String del receptor.
	 * 
	 * @return el t�tulo del �tem del men�.
	 */
	public String toString() {
		return this.getTitle();
	}

	/**
	 * Getter.
	 * 
	 * @return el objeto que representa la localidad.
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * Setter.
	 * 
	 * @param aLocale
	 *            es el objeto que representa la localidad.
	 */
	public void setLocale(Locale aLocale) {
		this.locale = aLocale;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que define la posici�n de este �tem en el men�.
	 */
	public int getMenuSection() {
		return this.menuSection;
	}

	/**
	 * Setter.
	 * 
	 * @param anInt
	 *            indica en que secci�n del men� deber�a aparecer este �tem.
	 */
	public void setMenuSection(int anInt) {
		this.menuSection = anInt;
	}

}
