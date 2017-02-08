/**
 * Este paquete contiene las clases del modelo de negocios de la aplicación que 
 * representan usuarios.
 */
package zinbig.item.model.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import zinbig.item.model.exceptions.FilterNameNotUniqueException;
import zinbig.item.model.filters.Filter;
import zinbig.item.model.projects.Project;
import zinbig.item.repositories.RepositoryLocator;
import zinbig.item.repositories.bi.ProjectsRepositoryBI;
import zinbig.item.util.IDGenerator;

/**
 * Esta clase representa a los usuarios del sistema.<br>
 * Como restricción del negocio no pueden existir dos usuarios con el mismo
 * nombre de usuario.<br>
 * Un usuario tiene dos status posibles, C que indica que el usuario está
 * confirmado y NC que indica que no se ha confirmado al usuario. Un usuario
 * debe ingresar al menos una vez al sistema en un período de tiempo acotado
 * desde su creación (por parte del administrador del sistema) para confirmar su
 * status. <br>
 * En caso de que no lo haga, el sistema lo eliminará automáticamente.<br>
 * Esta clase implementa la interfaz Versionable a fin de poder controlar la
 * edición concurrente por parte de dos usuarios.<br>
 * En el caso de un usuario individual, el alias es el mismo username.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class User extends AbstractUser {

	/**
	 * Es el nombre de usuario utilizado por este usuario.
	 */
	protected String username;

	/**
	 * Es la clave del usuario.
	 */
	protected String password;

	/**
	 * Representa el estado de confirmación del usuario. Por defecto tiene un
	 * valor NC (No Confirmado). Cuando el usuario confirma su incorporación el
	 * estado pasa a ser C (Confirmado).
	 */
	protected String status;

	/**
	 * Es una colección que referencia todos los grupos a los que pertenece el
	 * usuario.
	 */
	protected Collection<UserGroup> userGroups;

	/**
	 * Es un diccionario que contiene las preferencias del usuario.
	 */
	protected Map<String, String> userPreferences;

	/**
	 * Es el apellido de este usuario.
	 */
	protected String surname;

	/**
	 * Es una colección que contiene todos los filtros de este usuario.
	 */
	protected Collection<Filter> filters;

	/**
	 * Constructor.<br>
	 * Este constructor es llamado por la instancia de la clase Tracker; no
	 * debería ser invocado directamente.
	 * 
	 * 
	 * @param anUsername
	 *            es el nombre de usuario del usuario.
	 * @param aPassword
	 *            es la clave del usuario.
	 * @param aStatus
	 *            indica el estado de la confirmación del nuevo usuario. Cada
	 *            usuario tiene un período de tiempo para confirmar su
	 *            situación. Si no confirma su ingreso se lo borra del sistema.
	 * @param anEmail
	 *            es el mail del usuario.
	 * @param aDate
	 *            es la fecha de creación del usuario.
	 * @param aLanguage
	 *            es el nombre del idioma del usuario.
	 * @param aSurname
	 *            es el apellido del usuario.
	 * @param aBoolean
	 *            indica si este usuario puede ser eliminado o no.
	 */
	public User(String aName, String anUsername, String aPassword,
			String aStatus, String anEmail, Date aDate, String aLanguage,
			String aSurname, boolean aBoolean) {
		this.setOid(IDGenerator.getId());
		this.setName(aName);
		this.setCreationDate(aDate);
		this.setEmail(anEmail);
		this.setPassword(aPassword);
		this.setUsername(anUsername);
		this.setStatus(aStatus);
		this.setLanguage(aLanguage);
		this.setUserGroups(new ArrayList<UserGroup>());
		this.setSurname(aSurname);
		this.setDeletable(aBoolean);
		this.setUserPreferences(new HashMap<String, String>());
		this.setFilters(new ArrayList<Filter>());

		this.setAlias(anUsername);
	}

	/**
	 * Contructor sin parámetros.<br>
	 * No debería ser llamado directamente. Existe para poder realizar tests de
	 * unidad sobre esta clase.
	 */
	public User() {
		this("", "", "", "", "", new Date(), "es", "", true);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de usuario de este usuario.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el nombre de usuario de este usuario.
	 */
	public void setUsername(String anUsername) {
		this.username = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return la clave del usuario.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Setter.
	 * 
	 * @param aPassword
	 *            es la clave del usuario.
	 */
	public void setPassword(String aPassword) {
		this.password = aPassword;
	}

	/**
	 * Getter.
	 * 
	 * @return el estado de confirmación de la incorporación del usuario.
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Setter.
	 * 
	 * @param anStatus
	 *            es el estado de confirmación de la incorporación del usuario.
	 *            Si está confirmado debe ser C, en caso contrario deberá ser
	 *            NC.
	 */
	public void setStatus(String anStatus) {
		this.status = anStatus;
	}

	/**
	 * Getter.
	 * 
	 * @return el hashCode de este usuario. Se arma en base al username.
	 */
	@Override
	public int hashCode() {

		return this.getUsername().hashCode();
	}

	/**
	 * Compara el receptor con el objeto recibido para verificar si son iguales. <br>
	 * La igualdad de dos usuarios se da cuando tienen el mismo username.
	 * 
	 * @return true en caso de que ambos objetos sean usuarios y tenga el mismo
	 *         username. False en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		try {
			User anUser = (User) obj;
			result = anUser.getUsername().equals(this.getUsername());
		} catch (ClassCastException e) {
			result = false;
		}
		return result;

	}

	/**
	 * Agrega un nuevo grupo a este usuario.<br>
	 * Un usuario no puede estar en dos grupos con el mismo nombre por lo que el
	 * Tracker verifica esta condición previamente.
	 * 
	 * @param aGroup
	 *            es el grupo al cual se debe agregar el usuario.
	 */
	public void addUserGroup(UserGroup aGroup) {
		this.getUserGroups().add(aGroup);
	}

	/**
	 * Obtiene un diccionario con información de cada proyecto y la cantidad de
	 * items sin finalizar, categorizados por sus prioridades.
	 * 
	 * @return un mapa con todos los proyectos como clave y para cada uno
	 *         información de las prioridades y la cantidad de items por cada
	 *         una.
	 */
	public Map<String, Object> getUnfinishedItemsCountByPriorityLevel() {
		Map<String, Object> result = new HashMap<String, Object>();

		Iterator<Project> iterator = this.getProjects().iterator();
		Project aProject;

		while (iterator.hasNext()) {
			aProject = iterator.next();

			result.put(aProject.getName(), aProject
					.getUnfinishedItemsCountByPriorityLevel());
		}

		return result;
	}

	/**
	 * Getter. <br>
	 * A esta colección se accede mediante el uso de un repositorio ya que el
	 * usuario no conoce directamente a sus proyectos.
	 * 
	 * @return una colección con los proyectos del usuario.
	 */
	public Collection<Project> getProjects() {
		return this.getProjectsRepository().findAllProjectsOfUser(this);
	}

	/**
	 * Getter.
	 * 
	 * @return una implementación del patrón Repository utilizado para acceder
	 *         en forma eficiente a los proyectos.
	 */
	private ProjectsRepositoryBI getProjectsRepository() {
		return RepositoryLocator.getInstance().getProjectsRepository();
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene todos los grupos a los que pertenece
	 *         el usuario.
	 */
	public Collection<UserGroup> getUserGroups() {
		return this.userGroups;
	}

	/**
	 * Setter.
	 * 
	 * @param someGroups
	 *            es una colección que contiene todos los grupos a los que
	 *            pertenece este usuario.
	 */
	public void setUserGroups(Collection<UserGroup> someGroups) {
		this.userGroups = someGroups;
	}

	/**
	 * Getter.
	 * 
	 * @return el apellido de este usuario.
	 */
	public String getSurname() {
		return this.surname;
	}

	/**
	 * Setter.
	 * 
	 * @param aSurname
	 *            es el apellido de este usuario.
	 */
	public void setSurname(String aSurname) {
		this.surname = aSurname;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene las preferencias del usuario.
	 */
	public Map<String, String> getUserPreferences() {
		return this.userPreferences;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es un diccionario que contiene las preferencias del usuario.
	 */
	public void setUserPreferences(Map<String, String> aMap) {
		this.userPreferences = aMap;
	}

	/**
	 * Actualiza una preferencia del usuario. En caso de que no exista
	 * previamente la preferencia, la crea automáticamente al insertarla en el
	 * diccionario.
	 * 
	 * @param aKey
	 *            es la clave bajo la cual se guarda la preferencia.
	 * @param aValue
	 *            es el valor de la preferencia.
	 */
	public void updatePreference(String aKey, String aValue) {
		this.getUserPreferences().put(aKey, aValue);
	}

	/**
	 * Remueve al grupo de usuarios del usuario.
	 * 
	 * @param anUserGroup
	 *            es el grupo que se debe remover.
	 */
	public void removeUserGroup(UserGroup anUserGroup) {
		this.getUserGroups().remove(anUserGroup);
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de filtros de este usuario.
	 */
	public Collection<Filter> getFilters() {
		return this.filters;
	}

	/**
	 * Setter.
	 * 
	 * @param someFilters
	 *            es la colección que contiene los filtros de este usuario.
	 */
	public void setFilters(Collection<Filter> someFilters) {
		this.filters = someFilters;
	}

	/**
	 * Agrega un nuevo filtro al usuario.
	 * 
	 * @param aFilter
	 *            es el filtro que se debe agregar.
	 * @throws
	 */
	public void addFilter(Filter aFilter) throws FilterNameNotUniqueException {

		Filter existingFilter = null;
		Iterator<Filter> iterator = this.getFilters().iterator();

		while (iterator.hasNext()) {
			existingFilter = iterator.next();
			if (existingFilter.getName().equals(aFilter.getName())) {
				throw new FilterNameNotUniqueException();
			}
		}

		// si llegamos hasta aca es que no hay filtros con el mismo nombre
		this.getFilters().add(aFilter);
	}

	/**
	 * Agrega un nuevo proyecto a la colección de favoritos de este usuario.
	 * 
	 * @param aProject
	 *            es el proyecto que se marcó como favorito.
	 */
	public void addFavoriteProject(Project aProject) {
		String favoriteProjects = null;
		if (!this.getUserPreferences().containsKey("FAVORITE_PROJECT")) {
			favoriteProjects = aProject.getOid().toString();
		} else {
			favoriteProjects = ((String) this.getUserPreferences().get(
					"FAVORITE_PROJECT"))
					+ "," + aProject.getOid().toString();
		}
		this.getUserPreferences().put("FAVORITE_PROJECT", favoriteProjects);

	}

	/**
	 * Getter.
	 * 
	 * @return false ya que se trata de un usuario individual.
	 */
	@Override
	public boolean isUserGroup() {

		return false;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos favoritos que tiene este usuario.
	 */
	public int getFavoriteProjectsCount() {
		int result = 0;

		if (this.getUserPreferences().containsKey("FAVORITE_PROJECT")) {
			result = StringUtils.countMatches(((String) this
					.getUserPreferences().get("FAVORITE_PROJECT")), ",") + 1;

		}
		return result;

	}

	/**
	 * Remueve un proyecto de la colección de favoritos de este usuario.
	 * 
	 * @param aProject
	 *            es el proyecto que se desmarcó como favorito.
	 */
	public void removeFavoriteProject(Project aProject) {
		String favoriteProjects = ((String) this.getUserPreferences().get(
				"FAVORITE_PROJECT"));

		favoriteProjects = favoriteProjects.replace(aProject.getOid(), "");
		if (favoriteProjects.endsWith(",")) {
			favoriteProjects = favoriteProjects.substring(favoriteProjects
					.length() - 1);
		}
		if (favoriteProjects.length() > 0) {
			this.getUserPreferences().put("FAVORITE_PROJECT", favoriteProjects);
		} else {
			this.getUserPreferences().remove("FAVORITE_PROJECT");
		}

	}

	/**
	 * Notifica al usuario que se prepare para ser eliminado.
	 * 
	 * 
	 */
	public void prepareForDeletion() {

		// actualiza cada uno de los proyectos
		Collection<Project> projects = this.getProjects();
		Iterator<Project> projectsIterator = projects.iterator();
		Project aProject = null;
		while (projectsIterator.hasNext()) {
			aProject = projectsIterator.next();
			if (!aProject.getProjectLeader().equals(this)) {
				aProject.removeUser(this);
			}
		}

		// actualiza los grupos del usuario.
		Collection<UserGroup> userGroups = this.getUserGroups();
		Iterator<UserGroup> userGroupsIterator = userGroups.iterator();
		UserGroup anUserGroup = null;
		while (userGroupsIterator.hasNext()) {
			anUserGroup = userGroupsIterator.next();
			anUserGroup.removeUser(this);

		}
		this.setUserGroups(null);

		// las preferencias del usuario no se eliminan explícitamente así se
		// eliminarán en cascada.

		// los filtros no se eliminan explícitamente así se eliminarán en
		// cascada

	}

	/**
	 * Getter.
	 * 
	 * @return true si el usuario es líder de alguno de sus proyectos; false en
	 *         caso contrario.
	 */
	public boolean isProjectLeader() {
		Iterator<Project> iterator = this.getProjects().iterator();
		boolean result = false;
		Project aProject = null;
		while (iterator.hasNext()) {
			aProject = iterator.next();
			result = result | aProject.getProjectLeader().equals(this);
		}
		return result;
	}

	/**
	 * Elimina el filtro recibido de la colección de filtros de este usuario.
	 * 
	 * @param aFilter
	 *            es el filtro que se debe eliminar.
	 */
	public void removeFilter(Filter aFilter) {
		this.getFilters().remove(aFilter);

	}

}
