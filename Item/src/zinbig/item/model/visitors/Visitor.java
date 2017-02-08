/**
 * Este paquete contiene las clases que implementan el patrón de diseño Visitor.
 * Este patrón se utiliza para recorrer objetos de dominio aplicando operaciones
 * sobre las diferentes clases que lo componen.
 */
package zinbig.item.model.visitors;

import zinbig.item.model.filters.Filter;

/**
 * Esta clase es el tope de la jerarquía de los visitantes que se deben
 * implementar para representar las diferentes operaciones que se pueden
 * realizar sobre los objetos del modelo. Define el comportamiento en común para
 * todas las subclases concretas.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class Visitor {

	/**
	 * Visita un filtro de ítems. Este método se invoca a través de
	 * double-dispatching.
	 * 
	 * @param filter
	 *            es el filtro que se debe visitar.
	 * @return un objeto que representa el resultado de haber visitado un filtro
	 *         de ítems.
	 */
	public abstract Object visitFilter(Filter filter);

}
