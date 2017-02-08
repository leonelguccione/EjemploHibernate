/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.ProjectUnknownException;
import zinbig.item.model.projects.Project;
import zinbig.item.model.properties.PropertyDescription;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Este repositorio se utiliza para acceder en forma eficiente (a trav�s de
 * Hibernate) a las instancias de clase Project.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernateProjectsRepository extends HibernateBaseRepository
		implements ProjectsRepositoryBI {

	/**
	 * Recupera la colecci�n de proyectos en los cuales el usuario recibido est�
	 * asignado.
	 * 
	 * @param user
	 *            es el usuario para el cual se deben buscar todos sus
	 *            proyectos.
	 * @return una colecci�n con todos los proyectos del usuario.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> findAllProjectsOfUser(User user) {

		Query aQuery = this.getNamedQuery("projectsOfUserQuery");

		aQuery.setParameter("anUsername", user.getUsername());

		return aQuery.list();
	}

	/**
	 * Recupera una lista con los proyectos p�blicos.
	 * 
	 * @param projectsCount
	 *            es la m�xima cantidad de elementos que deben recuperarse.
	 * @return una colecci�n que contiene los proyectos p�blicos.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> findPublicProjects(int projectsCount) {

		Query aQuery = this.getNamedQuery("orderedPublicProjectsQuery");

		aQuery.setMaxResults(projectsCount);

		return aQuery.list();
	}

	/**
	 * Recupera una colecci�n con todos los proyectos p�blicos.
	 * 
	 * @return una colecci�n que contiene los proyectos p�blicos.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> findAllPublicProjects() {

		Query aQuery = this.getNamedQuery("publicProjectsQuery");

		return aQuery.list();
	}

	/**
	 * Recupera una colecci�n con todos los proyectos.
	 * 
	 * @return una colecci�n que contiene todos los proyectos.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> findAllProjects() {
		Query aQuery = this.getNamedQuery("allProjectsQuery");

		return aQuery.list();
	}

	/**
	 * Recupera una colecci�n con los proyectos cuyos identificadores se
	 * recibieron.
	 * 
	 * @param projectDTOs
	 *            es una colecci�n que contiene dtos que representan a los
	 *            proyectos que se deben recuperar.
	 * @return una colecci�n que contiene todos los proyectos identificados.
	 */
	@Override
	public Collection<Project> findProjects(Collection<ProjectDTO> projectDTOs) {
		Collection<String> ids = new ArrayList<String>();

		Iterator<ProjectDTO> iterator = projectDTOs.iterator();
		while (iterator.hasNext()) {
			ids.add(iterator.next().getOid());
		}

		return this.findProjectsByIds(ids);
	}

	/**
	 * Recupera una colecci�n de proyectos que est� contenida entre los �ndices
	 * recibidos. <br>
	 * 
	 * @param beginIndex
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
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> findProjects(int beginIndex, int count,
			String aPropertyName, String anOrdering) {

		Query aQuery = this.getNamedQuery("allProjectsQuery", aPropertyName,
				anOrdering);
		Collection<Project> result;
		aQuery.setMaxResults(count);
		aQuery.setFirstResult(beginIndex);

		result = aQuery.list();

		return result;
	}

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
	public boolean containsProjectWithName(Tracker aTracker, String aName) {

		boolean result = false;

		try {
			Project aProject = this.findProjectWithName(aTracker, aName);
			result = (aProject != null);
		} catch (ProjectUnknownException e) {
			// no se debe hacer nada en este caso.
			result = false;
		}

		return result;
	}

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
			throws ProjectUnknownException {

		Query aQuery = this.getNamedQuery("projectByNameQuery");

		aQuery.setParameter("aName", aName);
		aQuery.setMaxResults(1);

		Project result = (Project) aQuery.uniqueResult();

		if (result == null) {

			throw new ProjectUnknownException();

		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos.
	 */
	@Override
	public int getProjectsCount() {
		Query aQuery = this.getNamedQuery("projectsCountQuery");

		aQuery.setMaxResults(1);

		return ((Long) aQuery.uniqueResult()).intValue();
	}

	/**
	 * Recupera un proyecto con el oid dado.
	 * 
	 * @param anOid
	 *            es el oid del proyecto que se est� buscando.
	 * @return el proyecto con el id dado.
	 * @throws ProjectUnknownException
	 *             esta excepci�n puede levantarse en caso de tratar de
	 *             recuperar un proyecto que no existe.
	 */
	public Project findById(String anOid) throws ProjectUnknownException {

		Project result = (Project) this.findById(Project.class, anOid);

		if (result == null) {

			throw new ProjectUnknownException();

		}
		return result;
	}

	/**
	 * Getter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto para el cual se debe recuperar todos
	 *            sus usuarios.
	 * @return una colecci�n que contiene los usuarios asignados al proyecto
	 *         cuyo oid se ha recibido.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<User> getUsersOfProject(String anOid) {

		Query aQuery = this.getNamedQuery("allUsersOfProjectQuery");
		aQuery.setParameter("anOid", anOid);

		return aQuery.list();

	}

	/**
	 * Getter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto para el cual se debe recuperar todos
	 *            sus grupos de usuarios.
	 * @return una colecci�n que contiene los grupos de usuarios asignados al
	 *         proyecto cuyo oid se ha recibido.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<UserGroup> getUserGroupsOfProject(String anOid) {

		Query aQuery = this.getNamedQuery("allUserGroupsOfProjectQuery");
		aQuery.setParameter("anOid", anOid);

		return aQuery.list();

	}

	/**
	 * Getter.
	 * 
	 * @param anUser
	 *            es el usuario para el cual hay que recuperar los proyectos
	 *            marcados como favoritos.
	 * @return una colecci�n que contiene los proyectos marcados como favoritos.
	 */
	public Collection<Project> findFavoriteProjectsOfUser(User anUser) {

		// obtiene los ids de las preferencias del usuario.
		Query aQuery = this.getNamedQuery("favoriteProjectsOfUserQuery");
		aQuery.setParameter("anUsername", anUser.getUsername());

		// arma una colecci�n con los ids de los proyectos a recuperar.
		Collection<String> ids = new ArrayList<String>();

		String result = aQuery.list().toString();
		StringTokenizer sb = new StringTokenizer(result.substring(1, result
				.length() - 1), ",");
		while (sb.hasMoreTokens()) {
			ids.add(sb.nextToken());
		}

		return this.findProjectsByIds(ids);

	}

	/**
	 * Finder.
	 * 
	 * @param someProjectsIds
	 *            es una colecci�n que contiene los ids de los proyectos que se
	 *            deben recuperar.
	 * @return una colecci�n con los proyectos cuyos ids se recibieron.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Project> findProjectsByIds(
			Collection<String> someProjectsIds) {

		Collection<Project> projects = new ArrayList<Project>();
		Query aQuery = this.getNamedQuery("projectsByIdsQuery");
		aQuery.setParameterList("aList", someProjectsIds);
		if (!someProjectsIds.isEmpty()) {
			projects.addAll(aQuery.list());
		}

		return projects;
	}

	/**
	 * Recupera una colecci�n de proyectos que est� contenida entre los �ndices
	 * recibidos. <br>
	 * 
	 * @param beginIndex
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
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Project> findProjects(int beginIndex, int count,
			String aPropertyName, String anOrdering, UserDTO anUserDTO) {

		Query aQuery = null;
		if (anUserDTO == null) { // hay que listar solamente los proyectos
			// p�blicos

			aQuery = this.getNamedQuery("publicProjectsQuery", aPropertyName,
					anOrdering);
		} else {
			// solamente hay que listar los proyectos del usuario
			aQuery = this.getNamedQuery("allProjectsOfUserQuery",
					aPropertyName, anOrdering);

			aQuery.setParameter("anUsername", anUserDTO.getUsername());
		}

		Collection<Project> result;
		aQuery.setMaxResults(count);
		aQuery.setFirstResult(beginIndex);

		result = aQuery.list();

		return result;
	}

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
			String aName) {

		boolean result = false;

		Query aQuery = this.getNamedQuery("propertyDescriptionByNameQuery");

		aQuery.setParameter("aName", aName);
		aQuery.setParameter("anOid", aProject.getOid());
		aQuery.setMaxResults(1);

		PropertyDescription aPropertyDescription = (PropertyDescription) aQuery
				.uniqueResult();

		if (aPropertyDescription != null) {

			result = true;

		}
		return result;
	}

	/**
	 * Recupera todas las propiedades adicionales del proyecto cuyo
	 * identificador se ha recibido.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto.
	 * @return una colecci�n con las propiedades adicionales definidas para el
	 *         proyecto recibido.
	 */
	@SuppressWarnings("unchecked")
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			String anOid) {
		Query aQuery = this
				.getNamedQuery("allPropertyDescriptionsOfProjectQuery");
		aQuery.setParameter("anOid", anOid);

		return aQuery.list();
	}

	/**
	 * Recupera todos los valores din�micos de una propiedad adicional.
	 * 
	 * @param aQuery
	 *            es la consulta que se debe ejecutar para recuperar los
	 *            valores.
	 * @return una colecci�n de strings para cada uno de los valores
	 *         adicionales.
	 */
	@SuppressWarnings("deprecation")
	public Collection<String> getValuesForDynamicProperty(String aQuery) {
		Collection<String> result = new ArrayList<String>();

		Session session = this.getSession();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = session.connection().createStatement();
			rs = stmt.executeQuery(aQuery);
			String aValue = "";
			while (rs.next()) {
				aValue = rs.getString(1);
				result.add(aValue);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Recupera la cantidad de propiedades adicionales del proyecto cuyo
	 * identificador se ha recibido.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto.
	 * @return un entero que representa la cantidad de propiedades adicionales.
	 */
	public int getAdditionalPropertiesCountOfProject(String anOid) {
		Query aQuery = this.getNamedQuery("additionalPropertiesCountQuery");
		aQuery.setParameter("anOid", anOid);
		aQuery.setMaxResults(1);

		return ((Long) aQuery.uniqueResult()).intValue();
	}

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
	@SuppressWarnings("unchecked")
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			String aProjectOid, String aPropertyName, boolean isAscending)
			throws ProjectUnknownException {
		Query aQuery = this.getNamedQuery("propertiesOfProjectQuery",
				aPropertyName, isAscending ? "ASC" : "DESC");

		aQuery.setParameter("anId", aProjectOid);
		Collection<PropertyDescription> result;

		result = aQuery.list();

		return result;
	}

	/**
	 * Finder.
	 * 
	 * @param selectedProperties
	 *            es una colecci�n que contiene los identificadores de las
	 *            propiedades adicionales que se est�n buscando.
	 * @return una colecci�n que contiene las descripciones de las propiedades
	 *         adicionales con los identificadores recibidos.
	 */
	@SuppressWarnings("unchecked")
	public Collection<PropertyDescription> findPropertyDescriptionsOfProject(
			Collection<String> selectedProperties) {

		Collection<PropertyDescription> properties = new ArrayList<PropertyDescription>();
		Query aQuery = this.getNamedQuery("additionalPropertiesByIdsQuery");
		aQuery.setParameterList("aList", selectedProperties);
		if (!selectedProperties.isEmpty()) {
			properties.addAll(aQuery.list());
		}

		return properties;
	}

}
