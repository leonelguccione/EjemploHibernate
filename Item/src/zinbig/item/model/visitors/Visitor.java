/**
 * Este paquete contiene las clases que implementan el patr�n de dise�o Visitor.
 * Este patr�n se utiliza para recorrer objetos de dominio aplicando operaciones
 * sobre las diferentes clases que lo componen.
 */
package zinbig.item.model.visitors;

import zinbig.item.model.filters.Filter;

/**
 * Esta clase es el tope de la jerarqu�a de los visitantes que se deben
 * implementar para representar las diferentes operaciones que se pueden
 * realizar sobre los objetos del modelo. Define el comportamiento en com�n para
 * todas las subclases concretas.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class Visitor {

	/**
	 * Visita un filtro de �tems. Este m�todo se invoca a trav�s de
	 * double-dispatching.
	 * 
	 * @param filter
	 *            es el filtro que se debe visitar.
	 * @return un objeto que representa el resultado de haber visitado un filtro
	 *         de �tems.
	 */
	public abstract Object visitFilter(Filter filter);

}
