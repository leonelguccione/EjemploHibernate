/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.util.Comparator;

/**
 * Las instancias de esta clase se utilizan para ordenar los dtos que
 * representan a los proyectos.<br>
 * El orden se establece comparando la cantidad de �tems que tiene cada uno y
 * luego los nombres de los proyectos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PublicProjectDTOComparator implements Comparator<ProjectDTO> {

	/**
	 * Compara dos proyectos para ordenarlos seg�n su nombre.
	 * 
	 * @return un entero que representa el orden que se les debe dar, de acuerdo
	 *         a su nombre.
	 */
	@Override
	public int compare(ProjectDTO aProject, ProjectDTO anotherProject) {

		int result = -1
				* aProject.getItemsCount().compareTo(
						anotherProject.getItemsCount());
		if (result == 0) {
			result = aProject.getName().compareTo(anotherProject.getName());
		}
		return result;
	}

}
