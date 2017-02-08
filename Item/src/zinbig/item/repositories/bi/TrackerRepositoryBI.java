/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deber�n respetar las implementaciones del patr�n de dise�o Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Map;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.TrackerUnknownException;

/**
 * Esta interface establece el protocolo est�ndar que deber� ser respetado por
 * todas las clases que implementen el patr�n de dise�o Repository para acceder
 * eficientemente a las instancias de la clase Tracker.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface TrackerRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Recupera la �nica instancia de la clase Tracker.
	 * 
	 * @return la �nica instancia de la clase Tracker.<br>
	 *         Se asume que existe una �nica instancia ya que �sta representa la
	 *         ra�z del modelo persistente y no se proveen mecanismos desde el
	 *         sistema para generar nuevas instancias de esta clase.
	 * @throws TrackerUnknownException
	 *             es una excepci�n en caso de no hallar al tracker.
	 */
	public Tracker findTracker() throws TrackerUnknownException;

	/**
	 * Retorna un diccionario que contiene estad�sticas sobre el sistema y su
	 * utilizaci�n.<br>
	 * Estas estad�sticas son calculadas en forma peri�dica por un proceso batch
	 * que corre en forma asincr�nica.
	 * 
	 * @return un diccionario con una estad�stica para cada clave.
	 */
	public Map<String, Object> getStatitics();

}
