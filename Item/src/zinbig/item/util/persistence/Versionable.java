/**
 * Este paquete contiene clases e interfaces �tiles para dar soporte a los
 * aspectos de persistencia de la aplicaci�n.
 */
package zinbig.item.util.persistence;

/**
 * Esta interface define el protocolo que debe ser implementado por todas las
 * instancias persistentes que soporten el versionamiento para el control de la
 * edici�n concurrente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface Versionable {

	/**
	 * Getter.
	 * 
	 * @return el n�mero de versi�n.
	 */
	public int getVersion();

	/**
	 * Setter.
	 * 
	 * @param aVersionNumber
	 *            es el n�mero de versi�n.
	 */
	public void setVersion(int aVersionNumber);
}
