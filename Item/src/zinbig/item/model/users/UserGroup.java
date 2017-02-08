/**
 * Este paquete contiene las clases del modelo de negocios de la aplicación que 
 * representan usuarios.
 */
package zinbig.item.model.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import zinbig.item.model.Operation;
import zinbig.item.model.projects.Project;
import zinbig.item.util.IDGenerator;

/**
 * Esta clase representa a un grupo de usuarios. Todos los usuarios que
 * pertenecen al grupo tienen los mismos proyectos y operaciones permitidos.
 * 
 * Esta clase implementa la interfaz Versionable a fin de poder controlar la
 * edición concurrente por parte de dos usuarios.
 * 
 * <br>
 * En el caso de un grupo de usuarios, el alias es el nombre del grupo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserGroup extends AbstractUser {

	/**
	 * Es una colección que mantiene todos los usuarios de este grupo.
	 */
	public Collection<User> users;

	/**
	 * Es una colección que contiene todas las operaciones asignadas a este
	 * grupo.
	 */
	public Collection<Operation> operations;

	/**
	 * Es una colección que contiene todos los proyectos para los cuales está
	 * definido este grupo de usuarios.
	 */
	public Collection<Project> projects;

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre del grupo.
	 * @param anEmail
	 *            es el email del grupo.
	 * @param aLanguage
	 *            es el lenguaje del nuevo grupo de usuarios.
	 * @param aBoolean
	 *            indica si el grupo puede ser eliminado o no.
	 */
	public UserGroup(String aName, String anEmail, boolean aBoolean,
			String aLanguage) {

		this.setOid(IDGenerator.getId());
		this.setProjects(new ArrayList<Project>());
		this.setName(aName);
		this.setEmail(anEmail);
		this.setCreationDate(new Date());
		this.setUsers(new ArrayList<User>());
		this.setOperations(new ArrayList<Operation>());
		this.setDeletable(aBoolean);
		this.setLanguage(aLanguage);
		this.setAlias(aName);

	}

	/**
	 * Constructor. <br>
	 * Este constructor no debería utilizarse directamente; existe para poder
	 * realizar pruebas de unidad sobre esta clase.
	 */
	public UserGroup() {
		this("", "", true, "es");
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de usuarios de este grupo.
	 */
	public Collection<User> getUsers() {
		return this.users;
	}

	/**
	 * Setter.
	 * 
	 * @param someUsers
	 *            es una colección que contiene los usuarios de este grupo.
	 */
	public void setUsers(Collection<User> someUsers) {
		this.users = someUsers;
	}

	/**
	 * Agrega un nuevo usuario a la colección de usuarios. <br>
	 * Por restricciones del dominio, un grupo de usuarios no puede contener dos
	 * usuarios distintos con el mismo username; por lo que la clase Tracker
	 * realiza esta verificación antes de invocar este método.
	 * 
	 * @param anUser
	 *            es el usuario que se debe agregar.
	 */
	public void addUser(User anUser) {

		this.getUsers().add(anUser);

	}

	/**
	 * Getter.
	 * 
	 * @return la colección de operaciones asignadas a este grupo.
	 */
	public Collection<Operation> getOperations() {
		return this.operations;
	}

	/**
	 * Setter.
	 * 
	 * @param operations
	 *            es una colección que contiene todas las operaciones asignadas
	 *            a este grupo.
	 */
	public void setOperations(Collection<Operation> operations) {
		this.operations = operations;
	}

	/**
	 * Retorna una representación del receptor como String.
	 * 
	 * @return el nombre del grupo.
	 */
	public String toString() {
		return this.getName();
	}

	/**
	 * Remueve al usuario seleccionado del grupo de usuarios e indica al usuario
	 * que haga lo mismo con este grupo.
	 * 
	 * @param anUser
	 *            es el usuario que se debe eliminar.
	 */
	public void removeUser(User anUser) {
		this.getUsers().remove(anUser);

	}

	/**
	 * Getter.
	 * 
	 * @return una colección de proyectos en los cuales está definido este
	 *         grupo.
	 */
	public Collection<Project> getProjects() {
		return this.projects;
	}

	/**
	 * Setter.
	 * 
	 * @param someProjects
	 *            es una colección de proyectos para los cuales está definido
	 *            este grupo.
	 */
	public void setProjects(Collection<Project> someProjects) {
		this.projects = someProjects;
	}

	/**
	 * Remueve la proyecto de la lista de proyectos de este grupo de usuarios.
	 * 
	 * @param aProject
	 *            es el proyecto que se debe eliminar.
	 */
	public void removeProject(Project aProject) {
		this.getProjects().remove(aProject);

	}

	/**
	 * Getter.
	 * 
	 * @return true ya que se trata de un grupo de usuarios.
	 */
	@Override
	public boolean isUserGroup() {
		return true;
	}

	/**
	 * Getter.
	 * 
	 * @return el hashCode de este usuario. Se arma en base al username.
	 */
	@Override
	public int hashCode() {

		return this.getName().hashCode();
	}

	/**
	 * Notifica al grupo de usuarios que se prepare para ser eliminado.
	 */
	public void prepareForDeletion() {
		this.setOperations(null);
		this.setUsers(null);

		Iterator<Project> projectsIterator = this.getProjects().iterator();
		Project aProject = null;
		while (projectsIterator.hasNext()) {
			aProject = projectsIterator.next();
			aProject.getUserGroups().remove(this);
		}

		this.setProjects(null);

	}

}
