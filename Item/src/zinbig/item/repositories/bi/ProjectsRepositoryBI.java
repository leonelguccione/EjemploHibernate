/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deber�n respetar las implementaciones del patr�n de dise�o Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Collection;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.ProjectUnknownException;
import zinbig.item.model.projects.Project;
import zinbig.item.model.properties.PropertyDescription;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Esta interface establece el protocolo est�ndar que deber� ser respetado por
 * todas las clases que implementen el patr�n de dise�o Repository para acceder
 * eficientemente a las instancias de la clase Project.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface ProjectsRepositoryBI extends ItemAbstractRepositoryBI {

	/**
	 * Recupera todos los proyectos en los cuales participa un usuario
	 * determinado.
	 * 
	 * @param anUser
	 *            es el usuario para el cual se deben buscar todos los
	 *            proyectos.
	 * @return una colecci�n que contiene todos los proyectos del usuario.
	 */
	public Collection<Project> findAllProjectsOfUser(User anUser);

	/**
	 * Recupera una lista con los proyectos p�blicos.
	 * 
	 * @param projectsCount
	 *            es la m�xima cantidad de elementos que deben recuperarse.
	 * @return una colecci�n que contiene los proyectos p�blicos.
	 */
	public Collection<Project> findPublicProjects(int projectsCount);

	/**
	 * Recupera una lista con todos los proyectos p�blicos.
	 * 
	 * @return una colecci�n que contiene todos los proyectos p�blicos.
	 */
	public Collection<Project> findAllPublicProjects();

	/**
	 * Recupera una colecci�n con todos los proyectos.
	 * 
	 * @return una colecci�n que contiene todos los proyectos.
	 */
	public Collection<Project> findAllProjects();

	/**
	 * Recupera una colecci�n con los proyectos cuyos identificadores se
	 * recibieron.
	 * 
	 * @param projectDTOs
	 *            es una colecci�n que contiene dtos que representan a los
	 *            proyectos que se deben recuperar.
	 * @return una colecci�n que contiene todos los proyectos identificados.
	 */
	public Collection<Project> findProjects(Collection<ProjectDTO> projectDTOs);

	/**
	 * Recupera una colecci�n de proyectos que est� contenida entre los �ndices
	 * recibidos. <br>
	 * 
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden con el que se debe devolver el resultado.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            el resultado.
	 * @return una colecci�n de proyectos del sistema.
	 */
	public Collection<Project> findProjects(int index, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Recupera una colecci�n de proyectos que est� contenida entre los �ndices
	 * recibidos, considerando el usuario recibido. <br>
	 * 
	 * @param index
	 *            es el �ndice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden con el que se debe devolver el resultado.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            el resultado.
	 * @param anUserDTO
	 *            es un dto que representa a un usuario.
	 * @return una colecci�n de proyectos del sistema.
	 */
	public Collection<Project> findProjects(int index, int count,
			String aPropertyName, String anOrdering, UserDTO anUserDTO);

	/**
	 * Verifica si existe un proyecto con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aName
	 *            es el nombre del proyecto que se est� buscando.
	 * @return true en caso de que exista ya un proyecto; false en caso
	 *         contrario.
	 */
	public boolean containsProjectWithName(Tracker aTracker, String aName);

	/**
	 * Recupera un proyecto con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el objeto que contiene los proyectos y que representa el
	 *            sistema.
	 * @param aName
	 *            es el nombre del proyecto que se est� buscando.
	 * @return el proyecto con el nombre dado.
	 * @throws ProjectUnknownException
	 *             esta excepci�n puede levantarse en caso de tratar de
	 *             recuperar un proyecto que no existe.
	 */
	public Project findProjectWithName(Tracker aTracker, String aName)
			throws ProjectUnknownException;

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos.
	 */
	public int getProjectsCount();

	/**
	 * Recupera un proyecto con el oid dado.
	 * 
	 * 
	 * @param anOid
	 *            es el oid del proyecto que se est� buscando.
	 * @return el proyecto con el nombre dado.
	 * @throws ProjectUnknownException
	 *             esta excepci�n puede levantarse en caso de tratar de
	 *             recuperar un proyecto que no existe.
	 */
	public Project findById(String anOid) throws ProjectUnknownException;

	/**
	 * Getter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto para el cual se debe recuperar todos
	 *            sus usuarios.
	 * @return una colecci�n que contiene los usuarios asignados al proyecto
	 *         cuyo oid se ha recibido.
	 */
	public Collection<User> getUsersOfProject(String anOid);

	/**
	 * Getter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto para el cual se debe recuperar todos
	 *            sus grupos de usuarios.
	 * @return una colecci�n que contiene los grupos de usuarios asignados al
	 *         proyecto cuyo oid se ha recibido.
	 */
	public Collection<UserGroup> getUserGroupsOfProject(String anOid);

	/**
	 * Getter.
	 * 
	 * @param anUser
	 *            es el usuario para el cual hay que recuperar los proyectos
	 *            marcados como favoritos.
	 * @return una colecci�n que contiene los proyectos marcados como favoritos.
	 */
	public Collection<Project> findFavoriteProjectsOfUser(User anUser);

	/**
	 * Finder.
	 * 
	 * @param someProjectsIds
	 *            es una colecci�n que contiene los ids de los proyectos que se
	 *            deben recuperar.
	 * @return una colecci�n con los proyectos cuyos ids se recibieron.
	 */
	public Collection<Project> findProjectsByIds(
			Collection<String> someProjectsIds);

	/**
	 * Verifica si el proyecto recibido como par�metro contiene una descripci�n
	 * de propiedad para los �tems con el nombre dado.
	 * 
	 * @param aProject
	 *            es el proyecto en donde se debe buscar la descripci�n de
	 *            propiedad.
	 * @param aName
	 *            es el nombre de la descripci�n de propiedad.
	 * @return true en caso de que el proyecto contenga la descripci�n de
	 *         propiedad con el nombre dado; false en caso contrario.
	 */
	public boolean containsPropertyDescriptionWithName(Project aProject,
			String aName);

	/**
	 * Recupera todas las propiedades adicionales del proyecto cuyo
	 * identificador se ha recibido.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto.
	 * @return una colecci�n con las propiedades adicionales definidas para el
	 *         proyecto recibido.
	 */
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			String anOid);

	/**
	 * Recupera todos los valores din�micos de una propiedad adicional.
	 * 
	 * @param aQuery
	 *            es la consulta que se debe ejecutar para recuperar los valores
	 *            adicionales.
	 * @return una colecci�n de strings para cada uno de los valores
	 *         adicionales.
	 */
	public Collection<String> getValuesForDynamicProperty(String aQuery);

	/**
	 * Recupera la cantidad de propiedades adicionales del proyecto cuyo
	 * identificador se ha recibido.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto.
	 * @return un entero que representa la cantidad de propiedades adicionales.
	 */
	public int getAdditionalPropertiesCountOfProject(String anOid);

	/**
	 * Recupera todas las propiedades adicionales pertenecientes al proyecto you
	 * oid se ha recibido.
	 * 
	 * @param aProjectOid
	 *            es el identificador del proyecto.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la cual se debe ordenar el
	 *            resultado.
	 * @param isAscending
	 *            indica si el orden es ascendente o descendente.
	 * @return una colecci�n que contiene todas las propiedades del proyecto.
	 * @throws ProjectUnknownException
	 *             esta excepci�n puede levantarse si no existe un proyecto con
	 *             el identificador dado.
	 */
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			String aProjectOid, String aPropertyName, boolean isAscending)
			throws ProjectUnknownException;

	/**
	 * Finder.
	 * 
	 * @param selectedProperties
	 *            es una colecci�n que contiene los identificadores de las
	 *            propiedades adicionales que se est�n buscando.
	 * @return una colecci�n que contiene las descripciones de las propiedades
	 *         adicionales con los identificadores recibidos.
	 */
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			Collection<String> selectedProperties);
}
