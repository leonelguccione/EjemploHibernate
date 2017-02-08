/**
 * Este paquete contiene clases útiles para armar el menú de la aplicación.
 */
package zinbig.item.util.menu;

import java.util.Comparator;

/**
 * Esta clase implementa un comparador que permite ordenar los ítems de menú. <br>
 * Primero considera la sección de menú de cada ítem, y si siguen siendo iguales
 * se considera el título del ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MenuItemComparator implements Comparator<MenuItem> {

	/**
	 * Compara dos ítems de menú para ordenarlos.<br>
	 * Primero se comparan las secciones de cada uno, luego se toma en cuenta el
	 * título de cada ítem.
	 * 
	 * @param aMenuItem
	 *            es el primer ítem de menú a comparar.
	 * @param anotherMenuItem
	 *            es el segundo ítem de menú a comparar.
	 * @return un negativo si el primer ítem debe ir primero en el menú y un
	 *         número positivo si es al revés.
	 */
	@Override
	public int compare(MenuItem aMenuItem, MenuItem anotherMenuItem) {

		int result = 0;
		if (aMenuItem.getMenuSection() < anotherMenuItem.getMenuSection()) {
			result = -1;
		} else {
			if (aMenuItem.getMenuSection() > anotherMenuItem.getMenuSection()) {
				result = 1;
			} else {
				result = aMenuItem.getTitle().compareTo(
						anotherMenuItem.getTitle());
			}
		}
		return result;
	}

}
