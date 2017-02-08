/**
 * Este paquete contiene clases útiles para trabajar con el framework Spring.
 */
package zinbig.item.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Las instancias de esta clase se utilizan para acceder a los beans declarados
 * en el contexto de la aplicación a través de Spring.<br>
 * Esta clase implementa la interface ApplicationContextAware de modo que el
 * propio framework Spring pueda inyectar el contexto cuando se crea.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemApplicationContextProvider implements ApplicationContextAware {

	/**
	 * Constructor.
	 */
	public ItemApplicationContextProvider() {

		super();
	}

	/**
	 * Setter.
	 * 
	 * @param aContext
	 *            es el contexto que se debe inyectar en la clase
	 *            ItemApplicationContext.
	 * @throws BeansException
	 *             esta excepción puede levantarse debido a problemas con el
	 *             contexto.
	 */
	@Override
	public void setApplicationContext(ApplicationContext aContext)
			throws BeansException {
		ItemApplicationContext.setApplicationContext(aContext);
	}

}
