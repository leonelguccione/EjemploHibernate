/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deberán respetar las implementaciones del patrón de diseño Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Map;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.TrackerUnknownException;

/**
 * Esta interface establece el protocolo estándar que deberá ser respetado por
 * todas las clases que implementen el patrón de diseño Repository para acceder
 * eficientemente a las instancias de la clase Tracker.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface TrackerRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Recupera la única instancia de la clase Tracker.
	 * 
	 * @return la única instancia de la clase Tracker.<br>
	 *         Se asume que existe una única instancia ya que ésta representa la
	 *         raíz del modelo persistente y no se proveen mecanismos desde el
	 *         sistema para generar nuevas instancias de esta clase.
	 * @throws TrackerUnknownException
	 *             es una excepción en caso de no hallar al tracker.
	 */
	public Tracker findTracker() throws TrackerUnknownException;

	/**
	 * Retorna un diccionario que contiene estadísticas sobre el sistema y su
	 * utilización.<br>
	 * Estas estadísticas son calculadas en forma periódica por un proceso batch
	 * que corre en forma asincrónica.
	 * 
	 * @return un diccionario con una estadística para cada clave.
	 */
	public Map<String, Object> getStatitics();

}
