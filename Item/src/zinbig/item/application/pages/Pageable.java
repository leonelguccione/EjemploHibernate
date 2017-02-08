/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

/**
 * Esta interface define el protocolo que debe ser implementado por toda página
 * que tenga un componente de listado y que permita el cambio de la cantidad de
 * ítems en el listado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface Pageable {

	/**
	 * Getter.
	 * 
	 * @return la cantidad de ítems que se deben mostrar por página.
	 */
	public int getItemsPerPage();

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la nueva cantidad de elementos que se deben mostrar por
	 *            página.
	 */
	public void setItemsPerPage(int aNumber);

	/**
	 * Actualiza la cantidad de elementos que se muestran en las páginas con
	 * listados.
	 * 
	 */
	public void updateItemsPerPage();
}
