/**
 * Este paquete contiene clases �tiles para trabajar con el framework Spring.
 */
package zinbig.item.util.spring;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

/**
 * Esta clase se utiliza para acceder al contexto de la aplicaci�n administrado
 * por el framewrok Spring.<br>
 * La clase ItemApplicationContextProvider es la que asigna el contexto a esta
 * clase.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemApplicationContext implements Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -8314525704793980700L;

	/**
	 * Mantiene una referencia del contexto de la aplicaci�n.
	 */
	private static ApplicationContext applicationContext;

	/**
	 * Setter.
	 * 
	 * @param aContext
	 *            es el contexto de la aplicaci�n inyectado a trav�s de la clase
	 *            ApplicationContextoProvider.
	 */
	public static void setApplicationContext(ApplicationContext aContext) {
		applicationContext = aContext;
	}

	/**
	 * Getter.
	 * 
	 * @return el contexto de la aplicaci�n.
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
