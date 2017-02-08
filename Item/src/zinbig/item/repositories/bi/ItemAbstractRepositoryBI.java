/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deber�n respetar las implementaciones del patr�n de dise�o Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Collection;

/**
 * Esta interface representa el tope de la jerarqu�a de interfaces que definen
 * el protocolo a implementarse por los diferentes repositorios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface ItemAbstractRepositoryBI {

	/**
	 * Recupera un objeto del repositorio en forma eficiente.
	 * 
	 * 
	 * @param anOId
	 *            es el oid de la instancia a ser recuperada.
	 * 
	 * @return el objeto persistido que coincide con el oid recibido.
	 * @throws cada
	 *             clase que implemente esta interface debe definir la excepci�n
	 *             que se levanta cuando no se encuentra la instancia.
	 */
	public Object findById(String anOId) throws Exception;

	/**
	 * Borra de la base de datos el objeto recibido.
	 * 
	 * @param anObject
	 *            es el objeto persistente que debe ser eliminado.
	 */
	public void delete(Object anObject);

	/**
	 * Borra de la base de datos el objeto recibido.
	 * 
	 * @param someObjects
	 *            es una colecci�n que contiene los objetos que deben ser
	 *            eliminados.
	 */
	public void deleteObjects(Collection<? extends Object> someObjects);
}
