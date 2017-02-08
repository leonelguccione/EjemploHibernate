/**
 * Este paquete contiene las clases que representan las diferentes p�ginas de la 
 * aplicaci�n.
 */
package zinbig.item.application.pages;

/**
 * Esta interface define el protocolo que debe ser implementado por toda p�gina
 * que tenga un componente de listado y que permita el cambio de la cantidad de
 * �tems en el listado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface Pageable {

	/**
	 * Getter.
	 * 
	 * @return la cantidad de �tems que se deben mostrar por p�gina.
	 */
	public int getItemsPerPage();

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la nueva cantidad de elementos que se deben mostrar por
	 *            p�gina.
	 */
	public void setItemsPerPage(int aNumber);

	/**
	 * Actualiza la cantidad de elementos que se muestran en las p�ginas con
	 * listados.
	 * 
	 */
	public void updateItemsPerPage();
}
