/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.util.Comparator;

/**
 * Las instancias de esta clase se utilizan para comparar dos instancias de DTOs
 * que representan a los componentes de filtros por proyecto. <br>
 * 
 * La comparación se realiza por nombre del proyecto representado, pero si
 * existe un dto que representa al proyecto "Cualquiera" entonces este siempre
 * se ordena primero. Para este caso se considera el oid = 0.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterComponentByProjectDTOComparator implements
		Comparator<FilterComponentByProjectDTO> {

	/**
	 * Compara dos instancias para ordenarlas de acuerdo a su título
	 * internacionalizado.
	 * 
	 * @param aDTO
	 *            es el primer objeto a comparar.
	 * @param anotherDTO
	 *            es el segundo objeto a comparar.
	 * @return un entero que representa el orden de ambos objetos.
	 */
	@Override
	public int compare(FilterComponentByProjectDTO aDTO,
			FilterComponentByProjectDTO anotherDTO) {
		int result = 0;
		if (aDTO.getProjectOid().equals("")
				| anotherDTO.getProjectOid().equals("")) {
			result = aDTO.getProjectOid().compareTo(anotherDTO.getProjectOid());
		} else {
			result = aDTO.getTitle().compareTo(anotherDTO.getTitle());
		}
		return result;
	}

}
