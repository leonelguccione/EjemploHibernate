/**
 * Este paquete contiene las implementaciones de los repositorios para hacer 
 * pruebas con mock objects.
 */
package zinbig.item.repositories.impl.mock;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.UserGroupUnknownException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.util.dto.UserGroupDTO;

/**
 * Esta clase representa un mock object para los repositorios de usuarios. <br>
 * Permite realizar pruebas de unidad del modelo sin tener que tener una base de
 * datos instalada.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class MockUsersRepository implements UsersRepositoryBI {

	@Override
	public boolean containsUserGroupWithName(Tracker aTracker, String aName) {

		return false;
	}

	@Override
	public boolean containsUserWithUsername(Tracker aTracker, String anUsername) {

		return false;
	}

	@Override
	public boolean containsUserWithUsernameInUserGroup(UserGroup aGroup, String anUsername) {

		return false;
	}

	@Override
	public UserGroup findUserGroupByNameForUser(User anUser, String aName) throws UserGroupUnknownException {

		return null;
	}

	@Override
	public UserGroup findUserGroupByName(Tracker aTracker, String aName) throws UserGroupUnknownException {

		return null;
	}

	@Override
	public Collection<UserGroup> findUserGroups(int beginIndex, int endIndex, String aColumnName, String anOrdering) {

		return null;
	}

	@Override
	public User findUserWithUsername(Tracker aTracker, String anUsername, String status) throws UserUnknownException {

		Iterator<User> iterator = aTracker.getUsers().iterator();
		// System.out.println("tipo de la colección
		// "+aTracker.getUsers().getClass());
		// System.out.println("tipo del iterador "+iterator.getClass());

		User anUser = null;
		User currentUser = null;

		while (iterator.hasNext()) {
			currentUser = iterator.next();

			if (currentUser.getUsername().equals(anUsername)) {
				anUser = currentUser;
			}
		}
		if (anUser == null) {
			if (aTracker.getAdministrator().getUsername().equals(anUsername)) {
				anUser = aTracker.getAdministrator();
			}
		}

		return anUser;

	}

	@Override
	public User findUserWithUsernameInUserGroup(UserGroup aGroup, String anUsername) throws UserUnknownException {

		return null;
	}

	@Override
	public int getUserGroupsCount() {

		return 0;
	}

	public Object find(Class<?> aClass, Object anOId) {

		return null;
	}

	@Override
	public Collection<User> findUsers(int index, int count, String aPropertyName, String anOrdering) {

		return null;
	}

	/**
	 * Recupera todos los grupos de usuarios.
	 * 
	 * @return una colección que contiene todos los grupos de usuarios.
	 */
	@Override
	public Collection<UserGroup> findAllUserGroups() {
		return null;
	}

	/**
	 * Recupera una colección de grupos de usuarios cuyos oids se encuentran en
	 * el iterador recibido.
	 * 
	 * @param iterator
	 *            es el iterador que permite recorrer todos los grupos a
	 *            retornar.
	 * @return una colección con los grupos recuperados.
	 */
	@Override
	public Collection<UserGroup> findUserGroups(Iterator<UserGroupDTO> iterator) {
		return null;
	}

	@Override
	public int getUsersCount() {

		return 0;
	}

	@Override
	public Collection<UserGroup> findUserGroupsOfUser(User anUser) {

		return null;
	}

	@Override
	public AbstractUser find(Tracker aTracker, String anOid) {

		return null;
	}

	@Override
	public void delete(Object anObject) {

	}

	@Override
	public void deleteObjects(Collection<? extends Object> someObjects) {

	}

	@Override
	public Collection<User> findAllUsers() {

		return null;
	}

	@Override
	public Object findById(String anOId) throws Exception {

		return null;
	}

	@Override
	public Collection<UserGroup> findUserGroupsById(Collection<String> someUserGroups) {

		return null;
	}

	@Override
	public Collection<User> findUsersById(Collection<String> someUsers) {

		return null;
	}

	@Override
	public Collection<User> findUsersWithUsernameLike(Tracker aTracker, String aString, int aLimit) {

		return null;
	}

}
