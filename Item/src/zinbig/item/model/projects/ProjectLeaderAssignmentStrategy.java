/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;

/**
 * Esta clase implementa una estrategia de asignación de responsable de ítem que
 * asigna al líder del proyecto como responsable del nuevo ítem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ProjectLeaderAssignmentStrategy extends
		AbstractItemResponsibleAssignmentStrategy {

	/**
	 * Asigna el responsable al ítem según la estrategia que corresponda.
	 * 
	 * @param aProject
	 *            es el proyecto en el cual se está agregando el ítem.
	 * @param creator
	 *            es el usuario que está creando el ítem.
	 * @param responsibleCandidate
	 *            es el usuario o grupo de usuarios candidato a ser el
	 *            responsable del ítme creado.
	 * @return el usuario que debe ser asignado como responsable.
	 */
	public AbstractUser getResponsibleForNewItem(Project aProject,
			User creator, AbstractUser responsibleCandidate) {

		return aProject.getProjectLeader();
	}

}
