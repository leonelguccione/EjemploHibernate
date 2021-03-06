/**
 * Este paquete contiene las clases e interfaces que representan los proyectos
 * y componentes administrados por el ITeM.
 */
package zinbig.item.model.projects;

import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;

/**
 * Esta clase implementa una estrategia de asignaci�n de responsable que asigna
 * al �tem un usuario seleccionado como responsable del �tem.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserSelectedAssignmentStrategy extends
		AbstractItemResponsibleAssignmentStrategy {

	/**
	 * Asigna el responsable al �tem seg�n la estrategia que corresponda.
	 * 
	 * @param aProject
	 *            es el proyecto en el cual se est� agregando el �tem.
	 * @param creator
	 *            es el usuario que est� creando el �tem.
	 * @param responsibleCandidate
	 *            es el usuario o grupo de usuarios candidato a ser el
	 *            responsable del �tme creado.
	 * @return el usuario que debe ser asignado como responsable.
	 */
	public AbstractUser getResponsibleForNewItem(Project aProject,
			User creator, AbstractUser responsibleCandidate) {

		return responsibleCandidate;

	}

}
