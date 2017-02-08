/**
 * Este paquete contiene clases �tiles para armar el men� de la aplicaci�n.
 */
package zinbig.item.util.menu;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Las instancias de esta clase se utilizan como agrupaciones de �tems de men�.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Menu implements Serializable, Comparable<Menu> {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -3450921749215954491L;

	/**
	 * Es el t�tulo de este men�.
	 */
	protected String title;

	/**
	 * Es una colecci�n que contiene todos los elementos que componen este men�.
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
	 *            es el t�tulo del men�.
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
	 * @return es el t�tulo del men�.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el t�tulo del men�.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Getter.
	 * 
	 * @return la colecci�n de �tems de este men�.
	 */
	public Collection<MenuItem> getMenuItems() {
		return this.menuItems;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colecci�n de �tems de este men�.
	 */
	public void setMenuItems(Collection<MenuItem> aCollection) {
		this.menuItems = aCollection;
	}

	/**
	 * Compara al receptor con el par�metro para definir el orden. La
	 * comparaci�n se lleva a cabo por el t�tutlo del men�.
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
