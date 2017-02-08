/**
 * Este paquete contiene clases útiles para armar el menú de la aplicación.
 */
package zinbig.item.util.menu;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Las instancias de esta clase se utilizan como agrupaciones de ítems de menú.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Menu implements Serializable, Comparable<Menu> {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -3450921749215954491L;

	/**
	 * Es el título de este menú.
	 */
	protected String title;

	/**
	 * Es una colección que contiene todos los elementos que componen este menú.
	 */
	protected Collection<MenuItem> menuItems;

	/**
	 * Es el objeto que representa la localidad actual.
	 */
	protected Locale locale;

	/**
	 * Constructor.
	 * 
	 * @param aTitle
	 *            es el título del menú.
	 * @param aLocale
	 *            es el objeto que representa la localidad actual.
	 */
	public Menu(String aTitle, Locale aLocale) {
		this.setTitle(aTitle);
		this.setMenuItems(new ArrayList<MenuItem>());
		this.setLocale(aLocale);
	}

	/**
	 * Getter.
	 * 
	 * @return es el título del menú.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del menú.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de ítems de este menú.
	 */
	public Collection<MenuItem> getMenuItems() {
		return this.menuItems;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de ítems de este menú.
	 */
	public void setMenuItems(Collection<MenuItem> aCollection) {
		this.menuItems = aCollection;
	}

	/**
	 * Compara al receptor con el parámetro para definir el orden. La
	 * comparación se lleva a cabo por el títutlo del menú.
	 * 
	 * @return -1 si el receptor es menor que el objeto recibido; 0 si son
	 *         iguales y 1 si el objeto recibido es menor que receptor.
	 */
	@Override
	public int compareTo(Menu aMenu) {
		String aTitle = aMenu.getTitle();
		Collator collator = Collator.getInstance(locale);
		return collator.compare(this.getTitle(), aTitle);
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

}
