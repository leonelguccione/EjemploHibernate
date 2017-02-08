/**
 * Este paquete contiene clases e interfaces útiles para dar soporte a los
 * aspectos de persistencia de la aplicación.
 */
package zinbig.item.util.persistence;

/**
 * Esta interface define el protocolo que debe ser implementado por todas las
 * instancias persistentes que soporten el versionamiento para el control de la
 * edición concurrente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface Versionable {

	/**
	 * Getter.
	 * 
	 * @return el número de versión.
	 */
	public int getVersion();

	/**
	 * Setter.
	 * 
	 * @param aVersionNumber
	 *            es el número de versión.
	 */
	public void setVersion(int aVersionNumber);
}
