/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.util.Comparator;

/**
 * Las instancias de esta clase se utilizan para comparar y ordenar los dtos que
 * representan los filtros de ítems.<br>
 * 
 * La comparación considera solamente los nombres de los filtros.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterDTOComparator implements Comparator<FilterDTO> {

	/**
	 * Compara los objetos recibidos.
	 * 
	 * @param aDTO
	 *            es el primer objeto a comparar.
	 * @param anotherDTO
	 *            es el segundo objeto a comparar.
	 * @return un entero que representa el orden de los objetos recibidos. Este
	 *         entero se obtiene a partir de la comparación de los nombres de
	 *         los dtos de filtros recibidos.
	 */
	@Override
	public int compare(FilterDTO aDTO, FilterDTO anotherDTO) {

		return aDTO.getName().compareTo(anotherDTO.getName());
	}

}
