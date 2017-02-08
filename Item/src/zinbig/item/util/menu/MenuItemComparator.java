/**
 * Este paquete contiene clases �tiles para armar el men� de la aplicaci�n.
 */
package zinbig.item.util.menu;

import java.util.Comparator;

/**
 * Esta clase implementa un comparador que permite ordenar los �tems de men�. <br>
 * Primero considera la secci�n de men� de cada �tem, y si siguen siendo iguales
 * se considera el t�tulo del �tem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MenuItemComparator implements Comparator<MenuItem> {

	/**
	 * Compara dos �tems de men� para ordenarlos.<br>
	 * Primero se comparan las secciones de cada uno, luego se toma en cuenta el
	 * t�tulo de cada �tem.
	 * 
	 * @param aMenuItem
	 *            es el primer �tem de men� a comparar.
	 * @param anotherMenuItem
	 *            es el segundo �tem de men� a comparar.
	 * @return un negativo si el primer �tem debe ir primero en el men� y un
	 *         n�mero positivo si es al rev�s.
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
