/**
 * Este paquete contiene clases útiles para armar el menú de la aplicación.
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
 * individuales del menú.<br>
 * Las instancias tienen un título (que se muestra) y el nombre de una clase que
 * representa la página a la que se debe enviar al usuario al seleccionar este
 * ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MenuItem implements Serializable, Comparable<MenuItem> {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 7435024119163663120L;

	/**
	 * Es el título de este ítem de menú.
	 */
	protected String title;

	/**
	 * Es el nombre de clase que representa la página a la que se debe acceder a
	 * través de este ítem.
	 */
	protected String targetPageClassName;

	/**
	 * Es un diccionario que contiene parámetros requeridos por la página de
	 * destino.
	 */
	protected Map<String, String> parameters;

	/**
	 * Es el objeto que representa la localidad actual.
	 */
	protected Locale locale;

	/**
	 * Es un entero que indica en que sección dle menú debe aparecer este ítem.
	 * Se utiliza para ordenar los ítems de menú.
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
	 *            es el dto que representa a la operación que se intenta
	 *            ejecutar por medio de este ítem de menú.
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
	 * @return el título de este ítem de menú.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título de este ítem de menú.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return la clase que representa la página a la que hay que dirigir al
	 *         usuario cuando selecciona este ítem del menú.
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
	 * @return el nombre de la clase que representa la página a la que hay que
	 *         enviar al usuario al seleccionar este ítem del menú.
	 */
	public String getTargetPageClassName() {
		return this.targetPageClassName;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            es el nombre de la clase que representa la página a la que hay
	 *            que enviar al usuario al seleccionar este ítem del menú.
	 */
	public void setTargetPageClassName(String aClassName) {
		this.targetPageClassName = aClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario con los paámetros requeridos por la página a la
	 *         que se envía al usuario.
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es un diccionario con los paámetros requeridos por la página a
	 *            la que se envía al usuario.
	 */
	public void setParameters(Map<String, String> aMap) {
		this.parameters = aMap;
	}

	/**
	 * Agrega un nuevo parámetro.
	 * 
	 * @param aKey
	 *            es la clave del nuevo parámetro.
	 * @param aValue
	 *            es el valor del nuevo parámetro.
	 */
	public void addParameter(String aKey, String aValue) {
		this.getParameters().put(aKey, aValue);
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que este ítem de menú tenga parámetros; false en
	 *         caso contrario.
	 */
	public boolean hasParameters() {
		return !this.getParameters().isEmpty();
	}

	/**
	 * Compara al receptor con el parámetro para definir el orden. La
	 * comparación se lleva a cabo por el títutlo del menú.
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
	 * Retorna una representación como String del receptor.
	 * 
	 * @return el título del ítem del menú.
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
	 * @return un entero que define la posición de este ítem en el menú.
	 */
	public int getMenuSection() {
		return this.menuSection;
	}

	/**
	 * Setter.
	 * 
	 * @param anInt
	 *            indica en que sección del menú debería aparecer este ítem.
	 */
	public void setMenuSection(int anInt) {
		this.menuSection = anInt;
	}

}
