/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;

/**
 * Esta clase abstracta representa el tope de la jerarquía de las diferentes
 * estrategias de asignación de responsables para los ítems.<br>
 * Cada subclase puede definir una estrategia particular para seleccionar el
 * responsable que será asignado
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class AbstractItemResponsibleAssignmentStrategy {

	/**
	 * Asigna el responsable al ítem según la estrategia que corresponda.
	 * 
	 * @param aProject
	 *            es el proyecto en el cual se está agregando el ítem.
	 * @param creator
	 *            es el usuario que está creando el ítem.
	 * @param responsibleCandidate
	 *            es el usuario candidato a ser el responsable del ítme creado.
	 * @return el usuario que debe ser asignado como responsable.
	 */
	public abstract AbstractUser getResponsibleForNewItem(Project aProject,
			User creator, AbstractUser responsibleCandidate);

}
