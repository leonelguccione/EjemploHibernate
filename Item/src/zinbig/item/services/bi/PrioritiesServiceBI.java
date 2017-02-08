/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deberán implementar.<br>
 * 
 */
package zinbig.item.services.bi;

import java.util.Collection;

import zinbig.item.util.dto.PriorityDTO;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Esta interface define el protocolo de los servicios relacionados con los
 * conjuntos de prioridades del sistema que deberán ser implementados por clases
 * concretas.<br>
 * Todo servicio en ningún momento debe devolver un objeto de dominio, sino que
 * siempre devolverá DTOs.<br>
 * Por último, los servicios no contienen lógica de negocios propiamente dicha
 * sino que son encargados de ejecutar dicha lógica presente en el modelo de
 * dominio.<br>
 * Todos los métodos de esta interface declaran el lanzamiento de excepciones.
 * Algunas serán excepciones de negocio y otras inesperadas (como la caída de la
 * conexión a la base de datos). La declaración del lanzamiento tiene como
 * objetivo lograr que el cliente tenga que manejar excepciones cada vez que
 * invoque a un servicio. Si bien aquí se declaran excepciones generales, en la
 * documentación de cada método también figura la excepción particular de
 * negocio que podría ser lanzada por la implementación en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y además en forma
 * particular alguna excepción de negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface PrioritiesServiceBI {

	/**
	 * Getter.
	 * 
	 * @return la cantidad de conjuntos de prioridades que existen.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public int getPrioritySetCount() throws Exception;

	/**
	 * Getter.
	 * 
	 * @param firstIndex
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad de elementos a recuperar.
	 * @param anOrdering
	 *            es el orden que se debe aplicar a los resultados.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            los resultados.
	 * @return la colección de dtos de los conjuntos de prioridades del sistema
	 *         empezando por el índice recibido y devolviendo únicamente la
	 *         cantidad especificada de elementos.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<PrioritySetDTO> getPrioritySets(int firstIndex,
			int count, String aPropertyName, String anOrdering)
			throws Exception;

	/**
	 * Verifica si existe un grupo de prioridades con el nombre dado.
	 * 
	 * @param aName
	 *            es el nombre del grupo de prioridades.
	 * @return true en caso de que exista un grupo de prioridades con el nombre
	 *         recibido; false en caso contrario.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public boolean containsPrioritySetWithName(String aName) throws Exception;

	/**
	 * Getter.
	 * 
	 * @return un DTO que representa al conjunto de prioridades por defecto.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO getDefaultPrioritySet() throws Exception;

	/**
	 * Recupera una colección de DTOs que representan a todos los conjuntos de
	 * prioridades del sistema.
	 * 
	 * @return una colección de DTOs que representa a todos los conjuntos de
	 *         prioridades, incluso al conjunto por defecto.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<PrioritySetDTO> getAllPrioritySets() throws Exception;

	/**
	 * Agrega un nuevo conjunto de prioridades al sistema.
	 * 
	 * @param aName
	 *            es el nombre del nuevo conjunto de prioridades.
	 * @param isDefaultPrioritySet
	 *            indica si el conjunto de prioridades será el conjunto por
	 *            defecto.
	 * @return un DTO que representa al conjunto de prioridades recién creado.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO addPrioritySet(String aName,
			boolean isDefaultPrioritySet) throws Exception;

	/**
	 * Recupera un conjunto de prioridades por su oid.
	 * 
	 * @param oid
	 *            es el identificador del conjunto de prioridades que se debe
	 *            recuperar.
	 * @return el conjunto de prioridades.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO findPrioritySetById(String oid) throws Exception;

	/**
	 * Recupera un conjunto de prioridades por su nombre.
	 * 
	 * @param aName
	 *            es el nombre del conjunto de prioridades que se debe
	 *            recuperar.
	 * @return el conjunto de prioridades.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public PrioritySetDTO findPrioritySetByName(String aName) throws Exception;

	/**
	 * Edita la información básica de un conjunto de prioridades.
	 * 
	 * @param prioritySetDTO
	 *            es el dto que representa al conjunto de prioridades que se
	 *            está editando.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void editPrioritySet(PrioritySetDTO prioritySetDTO) throws Exception;

	/**
	 * Agrega una nueva prioridad individual al conjunto de prioridades cuyo
	 * nombre se ha recibido.
	 * 
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades al cual se debe agregar
	 *            la nueva prioridad.
	 * @param aName
	 *            es el nombre de la nueva prioridad.
	 * @param aValue
	 *            es el valor de la nueva prioridad.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void addPriorityToPrioritySet(String aPrioritySetId, String aName,
			String aValue) throws Exception;

	/**
	 * Getter.
	 * 
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades al cual pertenecen las
	 *            prioridades que deben ser recuperadas.
	 * @return la colección de dtos de las prioridades pertenecientes a un
	 *         conjunto de prioridades (PrioritySet).
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultad.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<PriorityDTO> getPriorities(String aPrioritySetId,
			String aPropertyName, boolean isAscending) throws Exception;

	/**
	 * Getter.
	 * 
	 * @param aPrioritySetName
	 *            es el nombre del conjunto de prioridades que se debe consultar
	 *            para ver la cantidad de elementos que contiene.
	 * @return la cantidad de prioridades que tiene el conjunto de prioridades
	 *         cuyo nombre se ha recibido.
	 * 
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public int getPrioritiesCount(String aPrioritySetName) throws Exception;

	/**
	 * Borra las prioridades representadas por los dtos recibidos del conjunto
	 * de prioridades cuyo id también se ha recibido.
	 * 
	 * @param prioritySetId
	 *            es el nombre del conjunto de prioridades del cual se deben
	 *            borrar las prioridades.
	 * @param selectedPriorities
	 *            es la colección de oids de prioridades que deben ser borradas.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deletePrioritiesOfPrioritySet(String prioritySetId,
			Collection<String> selectedPriorities) throws Exception;

	/**
	 * Recupera las prioridades del proyecto recibido.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto del cual se deben
	 *            recuperar las prioridades.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se desea ordernar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden ascendente o descendente.
	 * @return una colección que contiene los dtos de las prioridades asignadas
	 *         al proyecto.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public Collection<PriorityDTO> findPrioritiesOfProject(
			ProjectDTO aProjectDTO, String aPropertyName, String anOrdering)
			throws Exception;

	/**
	 * Elimina del sistema una colección de conjuntos de prioridades.
	 * 
	 * @param selectedPrioritySets
	 *            es una colección que contiene los identificadores de los
	 *            conjuntos de prioridades a ser eliminados.
	 * @throws Exception
	 *             esta excepción se levanta en caso de algún error en la
	 *             ejecución de este servicio.
	 */
	public void deletePrioritySets(Collection<String> selectedPrioritySets)
			throws Exception;

	/**
	 * Recupera la cantidad de prioridades que tiene un conjunto de prioridades.
	 * 
	 * @param aPrioritySetId
	 *            es el identificador del conjunto de prioridades.
	 * @return un entero que representa la cantidad de prioridades que tiene el
	 *         conjunto.
	 */
	public int getPrioritiesCountOfPrioritySet(String aPrioritySetId);

}
