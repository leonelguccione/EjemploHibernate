/**
 * Este paquete contiene clases que implementan las interfaces de negocio 
 * definidas en el paquete lifia.item.services.bi.
 * 
 */
package zinbig.item.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import zinbig.item.model.Item;
import zinbig.item.model.ItemType;
import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.WorkflowDescriptionUnknownException;
import zinbig.item.model.exceptions.WorkflowNodeDescriptionTitleNotUniqueException;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.workflow.WorkflowDescription;
import zinbig.item.model.workflow.WorkflowLinkDescription;
import zinbig.item.model.workflow.WorkflowNode;
import zinbig.item.model.workflow.WorkflowNodeDescription;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.repositories.bi.WorkflowsRepositoryBI;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.WorkflowDescriptionDTO;
import zinbig.item.util.dto.WorkflowLinkDescriptionDTO;
import zinbig.item.util.dto.WorkflowNodeDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para acceder a la l�gica de negocios
 * relacionada con los workflows del sistema.<br>
 * La l�gica de negocios propiamente dicha no se encuentra en los servicios sino
 * que est� definida en los objetos de modelo. La �nica excepci�n a esta regla
 * es la actualizaci�n de la informaci�n del usuario que se realiza en este
 * servicio.<br>
 * Los colaboradores de esta clase se inyectan mediante IoC.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowsServiceImpl extends BaseServiceImpl implements
		WorkflowsServiceBI {

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene dtos que representan a todos las
	 *         descripciones de workflows creadas por el sistema.
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	@Override
	public Collection<WorkflowDescriptionDTO> getAllWorkflowDescriptions()
			throws Exception {
		Collection<WorkflowDescriptionDTO> result = new ArrayList<WorkflowDescriptionDTO>();

		Tracker aTracker = this.getTrackerRepository().findTracker();

		result.addAll(this.getDtoFactory().createDTOForWorkflowDescriptions(
				aTracker.getWorkflowDescriptions()));
		return result;
	}

	/**
	 * Verifica si la descripci�n de workflow contiene una descripci�n de nodo
	 * con un t�tulo determinado.
	 * 
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow.
	 * @return true en caso de que exista el t�tulo; false en caso contrario.
	 * 
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	@Override
	public boolean existsWorkflowNodeDescriptionInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid) throws Exception {
		WorkflowNodeDescription node = null;
		boolean result = false;
		try {
			node = this.getWorkflowsRepository()
					.findWorkflowNodeDescriptionByTitleInWorkflowDescription(
							aTitle, aWorkflowDescriptionOid);
			result = node != null;
		} catch (Exception e) {

		}
		return result;
	}

	/**
	 * Verifica si la descripci�n de workflow contiene una descripci�n de enlace
	 * con un t�tulo determinado.
	 * 
	 * @param aTitle
	 *            es el t�tulo que se est� buscando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow.
	 * @return true en caso de que exista el t�tulo; false en caso contrario.
	 * 
	 * @throws Exception
	 *             esta excepci�n se levanta en caso de alg�n error en la
	 *             ejecuci�n de este servicio.
	 */
	@Override
	public boolean existsWorkflowLinkDescriptionInWorkflowDescription(
			String aTitle, String aWorkflowDescriptionOid) throws Exception {
		WorkflowLinkDescription link = null;
		boolean result = false;
		try {
			link = this.getWorkflowsRepository()
					.findWorkflowLinkDescriptionByTitleInWorkflowDescription(
							aTitle, aWorkflowDescriptionOid);
			result = link != null;
		} catch (Exception e) {

		}
		return result;
	}

	/**
	 * Agrega una nueva descripi�n de nodo a la descripci�n de workflow.
	 * 
	 * @param aTitle
	 *            es el t�tulo de la nueva descripci�n de nodo.
	 * @param isFinalNode
	 *            establece si esta nueva descripci�n de nodo es final o no.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow.
	 * @param usersIterator
	 *            es un iterador de todos los usuarios y grupos de usuarios
	 *            seleccionados como autorizados para el nuevo nodo.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a partir de la
	 *             ejecuci�n de este servicio.
	 */
	public void addWorkflowNodeDescription(String aTitle, boolean isFinalNode,
			String aWorkflowDescriptionOid,
			Iterator<AbstractUserDTO> usersIterator) throws Exception {

		if (this.existsWorkflowNodeDescriptionInWorkflowDescription(aTitle,
				aWorkflowDescriptionOid)) {
			throw new WorkflowNodeDescriptionTitleNotUniqueException();

		} else {

			UsersRepositoryBI repository = this.getUsersRepository();
			Collection<AbstractUser> users = new ArrayList<AbstractUser>();
			while (usersIterator.hasNext()) {
				users.add((AbstractUser) repository.findById(usersIterator
						.next().getOid()));
			}

			WorkflowDescription aWorkflowDescription = this
					.getWorkflowsRepository().findById(aWorkflowDescriptionOid);
			aWorkflowDescription.addWorkflowNodeDescription(aTitle,
					isFinalNode, users);

		}
	}

	/**
	 * Recupera las descripciones de nodos de una descripci�n de workflow.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow.
	 * @return una colecci�n que contiene los dtos que representan a las
	 *         descripciones de nodos de la descripci�n de workflow.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a partir de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getAllWorkflowNodeDescriptionOfWorkflowDescription(
			String workflowDescriptionOid) throws Exception {

		Collection<WorkflowNodeDescription> nodes = this
				.getWorkflowsRepository()
				.findWorkflowNodeDescriptionsOfWorkflowDescription(
						workflowDescriptionOid);

		return this.getDtoFactory().createDTOForWorkflowNodeDescriptions(nodes);
	}

	/**
	 * Elimina del sistema las descripciones de nodos cuyos oids se han
	 * recibido.
	 * 
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripci�n de nodo de la cual se deben
	 *            eliminar las descripciones de nodos.
	 * @param selectedWorkflowNodeDescriptions
	 *            es una colecci�n que contiene los oids de todos los elementos
	 *            que deben ser eliminados.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a partir de la
	 *             ejecuci�n de este servicio.
	 */
	public void deleteWorkflowNodeDescriptions(String aWorkflowDescriptionOid,
			Collection<String> selectedWorkflowNodeDescriptions)
			throws Exception {

		WorkflowsRepositoryBI repository = this.getWorkflowsRepository();
		WorkflowDescription aWorkflowDescription = repository
				.findById(aWorkflowDescriptionOid);

		// recupera todas las descripciones de nodos que se deben eliminar.
		Collection<WorkflowNodeDescription> nodeDescriptions = repository
				.findWorkflowNodeDescriptionsById(selectedWorkflowNodeDescriptions);

		// recupera todos los nodos anteriores que ten�an a las descripciones
		// como su descripci�n
		Collection<WorkflowNode> nodes = repository
				.findReferencingWorkflowNodes(selectedWorkflowNodeDescriptions);

		// recupera todas las descripciones de links que tienen como origen y/o
		// fin a las descripciones de nodos.
		Collection<WorkflowLinkDescription> links = repository
				.findReferencingWorkflowLinkDescriptions(selectedWorkflowNodeDescriptions);

		aWorkflowDescription.deleteWorkflowNodeDescriptions(nodeDescriptions,
				nodes, links);

		repository.deleteObjects(nodeDescriptions);
		repository.deleteObjects(links);

	}

	/**
	 * Agrega un nuevo enlace entre dos descripciones de nodo.
	 * 
	 * @param aTitle
	 *            es el t�tulo del enlace.
	 * @param aWorkflowDescriptionDTO
	 *            es el dto que representa la descripci�n de workflow que se
	 *            est� editando.
	 * @param initialWorkflowNodeDescriptionDTO
	 *            es el dto que representa al nodo inicial del enlace.
	 * @param finalWorkflowNodeDescriptionDTO
	 *            es el dto que representa al nodo final del enlace.
	 * @param someItemTypes
	 *            es un iterador de una colecci�n que contiene los dtos de los
	 *            tipos de �tems seleccionados para el enlace.
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public void addWorkflowLinkDescriptionToWorkflowDescription(String aTitle,
			WorkflowDescriptionDTO aWorkflowDescriptionDTO,
			WorkflowNodeDescriptionDTO initialWorkflowNodeDescriptionDTO,
			WorkflowNodeDescriptionDTO finalWorkflowNodeDescriptionDTO,
			Iterator<ItemTypeDTO> someItemTypesDTO) throws Exception {

		WorkflowDescription aWorkflowDescription = this
				.getWorkflowsRepository().findById(
						aWorkflowDescriptionDTO.getOid());
		Collection<String> oids = new ArrayList<String>();
		oids.add(initialWorkflowNodeDescriptionDTO.getOid());
		oids.add(finalWorkflowNodeDescriptionDTO.getOid());

		WorkflowNodeDescription initialNode = this.getWorkflowsRepository()
				.findWorkflowNodeDescriptionById(
						initialWorkflowNodeDescriptionDTO.getOid());
		WorkflowNodeDescription finalNode = initialWorkflowNodeDescriptionDTO
				.getOid().equals(finalWorkflowNodeDescriptionDTO.getOid()) ? initialNode
				: this.getWorkflowsRepository()
						.findWorkflowNodeDescriptionById(
								finalWorkflowNodeDescriptionDTO.getOid());

		Collection<ItemType> itemTypes = this.getItemsRepository()
				.findItemTypeByIds(someItemTypesDTO);

		aWorkflowDescription.addWorkflowLinkDescription(aTitle, initialNode,
				finalNode, itemTypes);
	}

	/**
	 * Getter.
	 * 
	 * @param workflowDescriptionOid
	 *            es el oid de la descripci�n de workflow para la cual hay que
	 *            recuperar todos los enlaces.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la que hay que ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @return una colecci�n que contiene los dtos que representan a todos los
	 *         enlaces de la descripci�n de workflow.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que podr�a levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowLinkDescriptionDTO> getAllWorkflowLinkDescriptionsOfWorkflowDescription(
			String workflowDescriptionOid, String aPropertyName,
			boolean isAscending) throws Exception {
		Collection<WorkflowLinkDescription> result = new ArrayList<WorkflowLinkDescription>();

		WorkflowsRepositoryBI aRepository = this.getWorkflowsRepository();

		result.addAll(aRepository
				.findWorkflowLinkDescriptionsOfWorkflowDescription(
						workflowDescriptionOid, aPropertyName, isAscending));

		return this.getDtoFactory()
				.createDTOForWorkflowLinkDescriptions(result);

	}

	/**
	 * Elimina del workflow los enlaces recibidos.
	 * 
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripci�n de workflow que se est� editando.
	 * @param selectedWorkflowLinkDescriptions
	 *            es una colecci�n que contiene los identificadores de los
	 *            enlaces que se deben eliminar.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */

	public void deleteWorflowLinkDescriptions(String aWorkflowDescriptionOid,
			Collection<String> selectedWorkflowLinkDescriptions)
			throws Exception {

		WorkflowsRepositoryBI repository = this.getWorkflowsRepository();
		WorkflowDescription aWorkflowDescription = repository
				.findById(aWorkflowDescriptionOid);

		Collection<WorkflowLinkDescription> links = repository
				.findWorkflowLinkDescriptionsById(selectedWorkflowLinkDescriptions);

		aWorkflowDescription.deleteWorkflowLinkDescriptions(links);

		repository.deleteObjects(links);

	}

	/**
	 * Recupera todas las descripciones de nodo a la cual se puede llegar a
	 * partir de una descripci�n dada.
	 * 
	 * @param anItemDTO
	 *            es el DTO que representa el �tem que se est� moviendo. El
	 *            estado actual puede ser nulo, lo que significa que el �tem
	 *            est� en estado CREATED. En este caso se retorna solamente el
	 *            nodo inicial del workflow.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            establece el orden del resultado.
	 * 
	 * @return una colecci�n que contiene las descripciones de nodo halladas.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<WorkflowNodeDescriptionDTO> findNextWorkflowNodesForItem(
			ItemDTO anItemDTO, String aPropertyName, String anOrdering)
			throws Exception {
		Collection<WorkflowNodeDescription> result = new ArrayList<WorkflowNodeDescription>();
		Item anItem = this.getItemsRepository().findById(anItemDTO.getOid());

		if (anItem.getCurrentWorkflowNode() == null) {
			// el �tem est� en estado CREATED as� que se retorna el nodo inicial
			result.add(anItem.getProject().getWorkflowDescription()
					.getInitialNodeDescription());
		} else {
			WorkflowsRepositoryBI repository = this.getWorkflowsRepository();

			result
					.addAll(repository
							.findAdjacentWorkflowNodeDescriptionsOfWorkflowNodeDescription(
									anItem.getCurrentWorkflowNode()
											.getNodeDescription(),
									aPropertyName, anOrdering));

		}

		return this.getDtoFactory()
				.createDTOForWorkflowNodeDescriptions(result);
	}

	/**
	 * Recupera todos los usuarios y grupos de usuarios autorizados en un nodo
	 * del workflow.
	 * 
	 * @param aWorkflowNodeDescriptionDTO
	 *            es el nodo del cual se deben recuperar los usuarios y grupos
	 *            de usuarios.
	 * @return una colecci�n que contiene todos los usuarios autorizados.
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public Collection<AbstractUserDTO> getAllAuthorizedUsersOfWorkflowNodeDescription(
			WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO)
			throws Exception {

		WorkflowNodeDescription aWorkflowNodeDescription = this
				.getWorkflowsRepository().findWorkflowNodeDescriptionById(
						aWorkflowNodeDescriptionDTO.getOid());

		Collection<AbstractUserDTO> result = new TreeSet<AbstractUserDTO>(
				new Comparator<AbstractUserDTO>() {

					/**
					 * Compara dos dto de usuarios/grupos de usuarios para
					 * ordenarlos.
					 * 
					 * @param aDto
					 *            es el primer elemento a comparar.
					 * @param anotherDto
					 *            es el segundo elemento a comparar.
					 * @return un entero que representa el orden entre los
					 *         elementos.
					 */
					@Override
					public int compare(AbstractUserDTO aDto,
							AbstractUserDTO anotherDto) {
						return aDto.toString().compareTo(anotherDto.toString());
					}
				});
		result.addAll(this.getDtoFactory().createDTOForAbstractUsers(
				aWorkflowNodeDescription.getAuthorizedUsers()));

		return result;

	}

	/**
	 * Obtiene la cantidad de descripciones de links que tiene un workflow dado.
	 * 
	 * @param anOid
	 *            es el identificador del workflow que se est� consultando.
	 * @return un entero que representa la cantidad de links que tiene el
	 *         workflow.
	 */
	public int getWorkflowLinkDescriptionCountOfWorkflowDescription(String anOid) {
		int result = 0;
		try {
			WorkflowDescription aWorkflow = this.getWorkflowsRepository()
					.findById(anOid);
			result = aWorkflow.getWorkflowLinkDescriptions().size();
		} catch (WorkflowDescriptionUnknownException e) {

		}
		return result;

	}

	/**
	 * Obtiene la cantidad de descripciones de nodos que tiene un workflow dado.
	 * 
	 * @param anOid
	 *            es el identificador del workflow que se est� consultando.
	 * @return un entero que representa la cantidad de nodos que tiene el
	 *         workflow.
	 */
	public int getWorkflowNodeDescriptionCountOfWorkflowDescription(String anOid) {
		int result = 0;
		try {
			WorkflowDescription aWorkflow = this.getWorkflowsRepository()
					.findById(anOid);
			result = aWorkflow.getWorkflowNodeDescriptions().size();
		} catch (WorkflowDescriptionUnknownException e) {

		}
		return result;

	}

	/**
	 * Recupera todas descripciones de nodos utilizadas por los proyectos.
	 * 
	 * @return una colecci�n que contiene todos las descripciones de los nodos
	 *         utilizadas en los proyectos.
	 */
	public Collection<WorkflowNodeDescriptionDTO> getAllWorkflowNodeDescriptionOfProjects() {
		Collection<WorkflowNodeDescription> result = new ArrayList<WorkflowNodeDescription>();

		result.addAll(this.getWorkflowsRepository()
				.findAllWorkflowNodeDescriptionsOfProjects());

		return this.getDtoFactory()
				.createDTOForWorkflowNodeDescriptions(result);
	}

	/**
	 * Recupera una descripci�n de nodo por su identificador.
	 * 
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripci�n de nodo que se debe
	 *            recuperar.
	 * @return la descripci�n de nodo que se est� buscando.
	 * @throws Exception
	 *             es cualquier excepci�n que se puede dar a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public WorkflowNodeDescriptionDTO findWorkflowNodeDescription(
			String aWorkflowNodeDescriptionOid) throws Exception {

		WorkflowsRepositoryBI repository = this.getWorkflowsRepository();
		return this
				.getDtoFactory()
				.createDTOForWorkflowNodeDescription(
						repository
								.findWorkflowNodeDescriptionById(aWorkflowNodeDescriptionOid));
	}

	/**
	 * Edita la informaci�n de una descripci�n de nodo.
	 * 
	 * @param aWorkflowNodeDescriptionOid
	 *            es el identificador de la descripci�n de nodo que se est�
	 *            editando.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripci�n de workflow a la que
	 *            pertenece al nodo que se est� editando.
	 * @param aTitle
	 *            es el nuevo t�tulo que se desea asignar.
	 * @param isFinalNode
	 *            establece si el nodo es final o no.
	 * @param someUsers
	 *            es una colecci�n que contiene los dtos de los usuarios y
	 *            grupos de usuarios seleccionados como posibles responsables de
	 *            los �tems en este nodo.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse a ra�z de la
	 *             ejecuci�n de este servicio.
	 */
	public void editWorkflowNodeDescription(String aWorkflowNodeDescriptionOid,
			String aWorkflowDescriptionOid, String aTitle, boolean isFinalNode,
			Iterator<AbstractUserDTO> someUsers) throws Exception {

		WorkflowsRepositoryBI repository = this.getWorkflowsRepository();
		WorkflowDescription aWorkflowDescription = repository
				.findById(aWorkflowDescriptionOid);
		WorkflowNodeDescription aWorkflowNodeDescription = repository
				.findWorkflowNodeDescriptionById(aWorkflowNodeDescriptionOid);

		UsersRepositoryBI usersRepository = this.getUsersRepository();
		Collection<AbstractUser> users = new ArrayList<AbstractUser>();
		while (someUsers.hasNext()) {
			users.add((AbstractUser) usersRepository.findById(someUsers.next()
					.getOid()));
		}

		aWorkflowDescription.editWorkflowNodeDescription(
				aWorkflowNodeDescription, aTitle, isFinalNode, users);
	}

	/**
	 * Recupera una lista de nodos que representan los estados anteriores por
	 * los que pas� el �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que pas� por los estados.
	 * @return una colecci�n de DTOs que representan los estados anteriores del
	 *         �tem.
	 * 
	 * @throws Exception
	 *             es cualquier excepci�n que puede levantarse al ejecutar este
	 *             servicio.
	 */
	public Collection<WorkflowNodeDTO> getOldWorkflowNodesOfItem(
			ItemDTO anItemDTO) throws Exception {

		WorkflowsRepositoryBI repository = this.getWorkflowsRepository();
		Collection<WorkflowNode> nodes = repository
				.findOldWorkflowNodesOfItem(anItemDTO.getOid());

		return this.getDtoFactory().createDTOForWorkflowNodes(nodes);
	}
}
