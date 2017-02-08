/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;

/**
 * Esta clase abstracta representa el tope de la jerarqu�a de las diferentes
 * estrategias de asignaci�n de responsables para los �tems.<br>
 * Cada subclase puede definir una estrategia particular para seleccionar el
 * responsable que ser� asignado
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class AbstractItemResponsibleAssignmentStrategy {

	/**
	 * Asigna el responsable al �tem seg�n la estrategia que corresponda.
	 * 
	 * @param aProject
	 *            es el proyecto en el cual se est� agregando el �tem.
	 * @param creator
	 *            es el usuario que est� creando el �tem.
	 * @param responsibleCandidate
	 *            es el usuario candidato a ser el responsable del �tme creado.
	 * @return el usuario que debe ser asignado como responsable.
	 */
	public abstract AbstractUser getResponsibleForNewItem(Project aProject,
			User creator, AbstractUser responsibleCandidate);

}
