/**
 * Este paquete contiene clases útiles para trabajar con el framework Spring.
 */
package zinbig.item.util.spring;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.ItemGenericException;

/**
 * Las instancias de esta clase se utilizan para interceptar las invocaciones a
 * los diferentes servicio a fin de poder manejar excepciones como las de
 * persistencia, que no deberían llegar directamente a la vista ni ser manejadas
 * por los servicios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ServiceExceptionInterceptor implements MethodInterceptor,
		Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -8154257445586066644L;

	/**
	 * Constructor.
	 */
	public ServiceExceptionInterceptor() {

	}

	/**
	 * Intercepta la invocación a un método. <br>
	 * Esta intercepción solamente se utiliza para transformar las excepciones
	 * que puedan levantarse en excepciones propias del Item, las cuales son más
	 * significativas para mostrar en la vista.
	 * 
	 * @param aMethodInvocation
	 *            es la invocación al método que se está interceptando.
	 * @return el resultado de invocar al método.
	 * @throws ItemConcurrentModificationException
	 *             esta excepción puede levantarse en caso de que dos usuarios
	 *             modifiquen algun objeto del modelo en forma concurrente.
	 * @throws ItemGenericException
	 *             esta excepción se devuelve ante cualquier situación
	 *             excepcional que no se la descripta más arriba.
	 */
	@Override
	public Object invoke(MethodInvocation aMethodInvocation) throws Throwable {
		try {

			return aMethodInvocation.proceed();

		} catch (Throwable e) {

			if (e.getClass().equals(
					HibernateOptimisticLockingFailureException.class)) {

				throw new ItemConcurrentModificationException();

			} else {

				// throw new ItemGenericException();
				throw e;

			}
		}
	}

}
