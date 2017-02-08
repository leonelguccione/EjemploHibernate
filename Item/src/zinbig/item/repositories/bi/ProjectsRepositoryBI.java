/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deberán respetar las implementaciones del patrón de diseño Repository.
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
 * Esta interface establece el protocolo estándar que deberá ser respetado por
 * todas las clases que implementen el patrón de diseño Repository para acceder
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
	 * @return una colección que contiene todos los proyectos del usuario.
	 */
	public Collection<Project> findAllProjectsOfUser(User anUser);

	/**
	 * Recupera una lista con los proyectos públicos.
	 * 
	 * @param projectsCount
	 *            es la máxima cantidad de elementos que deben recuperarse.
	 * @return una colección que contiene los proyectos públicos.
	 */
	public Collection<Project> findPublicProjects(int projectsCount);

	/**
	 * Recupera una lista con todos los proyectos públicos.
	 * 
	 * @return una colección que contiene todos los proyectos públicos.
	 */
	public Collection<Project> findAllPublicProjects();

	/**
	 * Recupera una colección con todos los proyectos.
	 * 
	 * @return una colección que contiene todos los proyectos.
	 */
	public Collection<Project> findAllProjects();

	/**
	 * Recupera una colección con los proyectos cuyos identificadores se
	 * recibieron.
	 * 
	 * @param projectDTOs
	 *            es una colección que contiene dtos que representan a los
	 *            proyectos que se deben recuperar.
	 * @return una colección que contiene todos los proyectos identificados.
	 */
	public Collection<Project> findProjects(Collection<ProjectDTO> projectDTOs);

	/**
	 * Recupera una colección de proyectos que esté contenida entre los índices
	 * recibidos. <br>
	 * 
	 * @param index
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden con el que se debe devolver el resultado.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            el resultado.
	 * @return una colección de proyectos del sistema.
	 */
	public Collection<Project> findProjects(int index, int count,
			String aPropertyName, String anOrdering);

	/**
	 * Recupera una colección de proyectos que esté contenida entre los índices
	 * recibidos, considerando el usuario recibido. <br>
	 * 
	 * @param index
	 *            es el índice de inicio.
	 * @param count
	 *            es la cantidad a recuperar.
	 * @param anOrdering
	 *            es el orden con el que se debe devolver el resultado.
	 * @param aPropertyName
	 *            es el nombre de la propiedad que se debe utilizar para ordenar
	 *            el resultado.
	 * @param anUserDTO
	 *            es un dto que representa a un usuario.
	 * @return una colección de proyectos del sistema.
	 */
	public Collection<Project> findProjects(int index, int count,
			String aPropertyName, String anOrdering, UserDTO anUserDTO);

	/**
	 * Verifica si existe un proyecto con el nombre dado.
	 * 
	 * @param aTracker
	 *            es el objeto que representa al sistema.
	 * @param aName
	 *            es el nombre del proyecto que se está buscando.
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
	 *            es el nombre del proyecto que se está buscando.
	 * @return el proyecto con el nombre dado.
	 * @throws ProjectUnknownException
	 *             esta excepción puede levantarse en caso de tratar de
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
	 *            es el oid del proyecto que se está buscando.
	 * @return el proyecto con el nombre dado.
	 * @throws ProjectUnknownException
	 *             esta excepción puede levantarse en caso de tratar de
	 *             recuperar un proyecto que no existe.
	 */
	public Project findById(String anOid) throws ProjectUnknownException;

	/**
	 * Getter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto para el cual se debe recuperar todos
	 *            sus usuarios.
	 * @return una colección que contiene los usuarios asignados al proyecto
	 *         cuyo oid se ha recibido.
	 */
	public Collection<User> getUsersOfProject(String anOid);

	/**
	 * Getter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto para el cual se debe recuperar todos
	 *            sus grupos de usuarios.
	 * @return una colección que contiene los grupos de usuarios asignados al
	 *         proyecto cuyo oid se ha recibido.
	 */
	public Collection<UserGroup> getUserGroupsOfProject(String anOid);

	/**
	 * Getter.
	 * 
	 * @param anUser
	 *            es el usuario para el cual hay que recuperar los proyectos
	 *            marcados como favoritos.
	 * @return una colección que contiene los proyectos marcados como favoritos.
	 */
	public Collection<Project> findFavoriteProjectsOfUser(User anUser);

	/**
	 * Finder.
	 * 
	 * @param someProjectsIds
	 *            es una colección que contiene los ids de los proyectos que se
	 *            deben recuperar.
	 * @return una colección con los proyectos cuyos ids se recibieron.
	 */
	public Collection<Project> findProjectsByIds(
			Collection<String> someProjectsIds);

	/**
	 * Verifica si el proyecto recibido como parámetro contiene una descripción
	 * de propiedad para los ítems con el nombre dado.
	 * 
	 * @param aProject
	 *            es el proyecto en donde se debe buscar la descripción de
	 *            propiedad.
	 * @param aName
	 *            es el nombre de la descripción de propiedad.
	 * @return true en caso de que el proyecto contenga la descripción de
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
	 * @return una colección con las propiedades adicionales definidas para el
	 *         proyecto recibido.
	 */
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			String anOid);

	/**
	 * Recupera todos los valores dinámicos de una propiedad adicional.
	 * 
	 * @param aQuery
	 *            es la consulta que se debe ejecutar para recuperar los valores
	 *            adicionales.
	 * @return una colección de strings para cada uno de los valores
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
	 * @return una colección que contiene todas las propiedades del proyecto.
	 * @throws ProjectUnknownException
	 *             esta excepción puede levantarse si no existe un proyecto con
	 *             el identificador dado.
	 */
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			String aProjectOid, String aPropertyName, boolean isAscending)
			throws ProjectUnknownException;

	/**
	 * Finder.
	 * 
	 * @param selectedProperties
	 *            es una colección que contiene los identificadores de las
	 *            propiedades adicionales que se están buscando.
	 * @return una colección que contiene las descripciones de las propiedades
	 *         adicionales con los identificadores recibidos.
	 */
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			Collection<String> selectedProperties);
}
