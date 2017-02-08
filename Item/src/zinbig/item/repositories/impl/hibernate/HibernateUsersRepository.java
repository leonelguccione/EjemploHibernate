/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Query;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.UserGroupUnknownException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.repositories.bi.UsersRepositoryBI;
import zinbig.item.util.dto.UserGroupDTO;

/**
 * Esta clase implementa un repositorio para acceder a los usuarios persistidos de manera eficiente a través
 * de Hibernate.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class HibernateUsersRepository extends HibernateBaseRepository implements UsersRepositoryBI {

  /**
   * Verifica si el modelo contiene ya un usuario con un nombre de usuario dado. Es una restricción del
   * negocio que solamente exista un usuario con un nombre de usuario determinado.<br>
   * Este método busca por defecto solamente usuarios activados (status C).
   * 
   * @param aTracker
   *          es el administrador de todos los usuarios.
   * @param anUsername
   *          es el nombre del usuario que se debe verificar dentro del administrador.
   * @return false si no existe un usuario con el nombre de usuario dado; true en caso contrario.
   */
  @Override
  public boolean containsUserWithUsername(Tracker aTracker, String anUsername) {
    boolean result = false;

    try {
      User anUser = this.findUserWithUsername(aTracker, anUsername, "C");
      result = (anUser != null);
    } catch (UserUnknownException e) {
      // no se debe hacer nada en este caso.
      result = false;
    }

    return result;
  }

  /**
   * Finder.<br>
   * La consulta ingresa por el Tracker ya que puede darse el caso de que un usuario exista en la base de
   * datos pero no esté vinculado a ningún administrador (este caso suele darse por errores en el borrado). De
   * esta manera solamente se consultan los usuarios a los cuales se puede acceder por la raíz del sistema.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema y que contiene los usuarios.
   * @param anUsername
   *          es el nombre de usuario a buscar.
   * @param status
   *          indica el status del usuario que se busca.
   * @return el usuario con el nombre de usuario dado.
   * @throws UserUnknownException
   *           en caso de que no exista el usuario.
   */
  @Override
  public User findUserWithUsername(Tracker aTracker, String anUsername, String status)
      throws UserUnknownException {

    Query aQuery = this.getNamedQuery("userByUsernameQuery");

    aQuery.setParameter("anUsername", anUsername);
    aQuery.setParameter("anStatus", status);
    aQuery.setMaxResults(1);

    User result = (User) aQuery.uniqueResult();
    if (result == null) {
      if (aTracker.getAdministrator().getUsername().equals(anUsername)) {
        result = aTracker.getAdministrator();
      } else {
        throw new UserUnknownException();
      }
    }
    return result;

  }

  /**
   * Recupera todos los usuarios que no han confirmado su inscripción.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema y que contiene los usuarios.
   * @return una colección con los usuarios no confirmados. Esta colección contiene a todos los usuarios no
   *         confirmados, sin distinguir su fecha de creación.
   */
  @SuppressWarnings("unchecked")
  public Collection<User> findUnconfirmedUsers(Tracker aTracker) {

    Query aQuery = this.getNamedQuery("unconfirmedUsersQuery");

    aQuery.setParameter("anOid", aTracker.getOid());

    return aQuery.list();

  }

  /**
   * Verifica si existe un usuario con un nombre de usuario dado dentro de un grupo de usuarios.
   * 
   * @param anUsername
   *          el nombre de usuario a buscar.
   * @param aGroup
   *          es el objeto responsable de administrar la colección de usuarios.
   * @return true en caso de que exista un usuario con el nombre de usuario dado; false en caso contrario.
   */
  @Override
  public boolean containsUserWithUsernameInUserGroup(UserGroup aGroup, String anUsername) {
    boolean result = false;

    try {
      User anUser = this.findUserWithUsernameInUserGroup(aGroup, anUsername);
      result = (anUser != null);
    } catch (UserUnknownException e) {
      // no se debe hacer nada en este caso.
      result = false;
    }

    return result;
  }

  /**
   * Recupera un usuario con un nombre de usuario dado, buscándolo dentro de un grupo de usuarios de sistema
   * en particular.
   * 
   * @param anUsername
   *          es el nombre de usuario del usuario que se está buscando.
   * @param aGroup
   *          es el objeto responsable de administrar la colección de usuarios.
   * @return el usuario encontrado.
   * @throws UserUnknownException
   *           esta excepción puede ser lanzada cuando no se encuentra a un usuario con el nombre de usuario
   *           dado.
   */
  @Override
  public User findUserWithUsernameInUserGroup(UserGroup aGroup, String anUsername)
      throws UserUnknownException {

    Query aQuery = this.getNamedQuery("usersInGroupQuery");

    aQuery.setParameter("anUsername", anUsername);
    aQuery.setParameter("anOid", aGroup.getOid());
    aQuery.setMaxResults(1);

    User result = (User) aQuery.uniqueResult();

    if (result == null) {
      throw new UserUnknownException();
    } else {
      return result;
    }

  }

  /**
   * Recupera un grupo con el nombre dado que contenga al usuario recibido.
   * 
   * @param anUser
   *          es el usuario que conoce al grupo que se está buscanado.
   * @param aName
   *          es el nombre del grupo que se está buscando.
   * @return el grupo cuyo nombre se recibió.
   * @throws UserGroupUnknownException
   *           esta excepción se levanta en caso de que no exista un grupo con el nombre dado para el usuario.
   */
  @Override
  public UserGroup findUserGroupByNameForUser(User anUser, String aName) throws UserGroupUnknownException {

    Query aQuery = this.getNamedQuery("usergroupForUserQuery");

    aQuery.setParameter("aName", aName);
    aQuery.setParameter("anOid", anUser.getOid());
    aQuery.setMaxResults(1);

    UserGroup result = (UserGroup) aQuery.uniqueResult();

    if (result == null) {
      throw new UserGroupUnknownException();
    } else {
      return result;
    }
  }

  /**
   * Recupera una colección de grupos de usuarios que esté contenida entre los índices recibidos. <br>
   * Se recuperan tanto los grupos de usuarios de sistema como de proyectos.
   * 
   * @param beginIndex
   *          es el índice de inicio.
   * @param endIndex
   *          es el índice de fin.
   * @param anOrdering
   *          es el orden que debe aplicarse al resultado.
   * @param aColumnName
   *          es el nombre de la propiedad por la que debe ordenarse el resultado.
   * @return una colección de grupos de usuarios de sistema.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<UserGroup> findUserGroups(int beginIndex, int endIndex, String aColumnName,
      String anOrdering) {

    Query aQuery = this.getNamedQuery("allUsergroupsQuery", aColumnName, anOrdering);
    Collection<UserGroup> result;
    aQuery.setMaxResults(endIndex);
    aQuery.setFirstResult(beginIndex);

    result = aQuery.list();

    return result;
  }

  /**
   * Getter.
   * 
   * @return la cantidad de grupos de usuarios, tanto de sistema como de proyectos.
   */
  @Override
  public int getUserGroupsCount() {
    Query aQuery = this.getNamedQuery("usergroupsCountQuery");

    aQuery.setMaxResults(1);

    return ((Long) aQuery.uniqueResult()).intValue();
  }

  /**
   * Getter.
   * 
   * @return la cantidad de usuarios.
   */
  @Override
  public int getUsersCount() {
    Query aQuery = this.getNamedQuery("usersCountQuery");

    aQuery.setMaxResults(1);

    return ((Long) aQuery.setCacheable(true).uniqueResult()).intValue();
  }

  /**
   * Verifica si el sistema cuenta con un grupo de usuarios con el nombre dado. <br>
   * Este método verifica tanto los grupos de usuarios de sistema como los de proyectos.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema.
   * @param aName
   *          es el nombre del grupo que se está buscando.
   * @return true en caso de que el sistema contenga un grupo con el nombre dado; false en caso contrario.
   */
  @Override
  public boolean containsUserGroupWithName(Tracker aTracker, String aName) {
    boolean result = false;
    try {
      UserGroup aGroup = this.findUserGroupByName(aTracker, aName);
      result = (aGroup != null);

    } catch (UserGroupUnknownException e) {
      // no se debe hacer nada en este caso.
      result = false;
    }

    return result;
  }

  /**
   * Recupera un grupo de usuarios con el nombre dado. <br>
   * Este método recupera tanto los grupos de usuarios de proyectos como los de sistema.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema y que contiene los grupos de usuarios.
   * @param aName
   *          es el nombre del grupo.
   * @return el grupo con el nombre dado.
   * @throws UserGroupUnknownException
   *           esta excepción se levanta en caso de no encontrar un grupo con el nombre dado.
   */
  @Override
  public UserGroup findUserGroupByName(Tracker aTracker, String aName) throws UserGroupUnknownException {

    Query aQuery = this.getNamedQuery("usergroupByNameQuery");

    aQuery.setParameter("aName", aName);

    aQuery.setMaxResults(1);

    UserGroup result = (UserGroup) aQuery.uniqueResult();

    if (result == null) {
      throw new UserGroupUnknownException();
    } else {
      return result;
    }
  }

  /**
   * Recupera una colección de usuarios que esté contenida entre los índices recibidos. <br>
   * 
   * @param beginIndex
   *          es el índice de inicio.
   * @param endIndex
   *          es el índice de fin.
   * @param anOrdering
   *          es el orden que se debe aplicar a los resultados.
   * @param aPropertyName
   *          es el nombre de la propiedad que se debe utilizar para ordenar los resultados.
   * @return una colección de usuarios de sistema.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<User> findUsers(int beginIndex, int endIndex, String aPropertyName, String anOrdering) {

    Query aQuery = this.getNamedQuery("allUsersQuery", aPropertyName, anOrdering);
    Collection<User> result;
    aQuery.setMaxResults(endIndex);
    aQuery.setFirstResult(beginIndex);

    result = aQuery.list();

    return result;
  }

  /**
   * Recupera todos los grupos de usuarios.
   * 
   * @return una colección que contiene todos los grupos de usuarios.
   */
  @SuppressWarnings("unchecked")
  public Collection<UserGroup> findAllUserGroups() {
    Query aQuery = this.getNamedQuery("allUsergroupsQuery");

    return aQuery.setCacheable(true).list();
  }

  /**
   * Recupera una colección de grupos de usuarios cuyos oids se encuentran en el iterador recibido.
   * 
   * @param iterator
   *          es el iterador que permite recorrer todos los grupos a retornar.
   * @return una colección con los grupos recuperados.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<UserGroup> findUserGroups(Iterator<UserGroupDTO> iterator) {
    Collection<String> ids = new ArrayList<String>();

    while (iterator.hasNext()) {
      ids.add(iterator.next().getOid());
    }

    Query aQuery = this.getNamedQuery("userGroupsByIdsQuery");
    aQuery.setParameterList("aList", ids);
    Collection<UserGroup> result = aQuery.setCacheable(true).list();
    return result;
  }

  /**
   * Recupera una colección de grupos de usuarios asignados a un usuario en particular.
   * 
   * @param anUser
   *          es el usuario que tiene asignado los grupos de usuarios.
   * @return una colección de grupos de usuarios.
   */
  @SuppressWarnings("unchecked")
  public Collection<UserGroup> findUserGroupsOfUser(User anUser) {
    Query aQuery = this.getNamedQuery("userGroupsOfUserQuery");

    aQuery.setParameter("anId", anUser.getOid());

    Collection result = aQuery.list();

    return result;

  }

  /**
   * Finder.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema.
   * @param anOid
   *          es el oid del grupo de usuarios o del usuario que se debe recuperar.
   * @return una instancia de la clase AbstractUser que representa a un usuario particular o a un grupo de
   *         usuarios.
   * @throws UserUnknownException
   *           esta excepción puede levantarse en caso de querer recuperar un usuario o grupo de usuarios con
   *           un id inexistente.
   */
  public AbstractUser find(Tracker aTracker, String anOid) throws UserUnknownException {

    AbstractUser result = (AbstractUser) this.findById(AbstractUser.class, anOid);

    if (result == null) {
      throw new UserUnknownException();
    } else {
      return result;
    }
  }

  /**
   * Finder.
   * 
   * @param anOid
   *          es el oid del grupo de usuarios o del usuario que se debe recuperar.
   * @return una instancia de la clase AbstractUser que representa a un usuario particular o a un grupo de
   *         usuarios.
   * @throws UserUnknownException
   *           esta excepción puede levantarse en caso de querer recuperar un usuario o grupo de usuarios con
   *           un id inexistente.
   */
  public AbstractUser findById(String anOid) throws UserUnknownException {

    AbstractUser result = (AbstractUser) this.findById(AbstractUser.class, anOid);

    if (result == null) {
      throw new UserUnknownException();
    } else {
      return result;
    }
  }

  /**
   * Finder.
   * 
   * @return una colección que contiene todos los usuarios del sistema, exceptuando al administrador.
   */
  @SuppressWarnings("unchecked")
  public Collection<User> findAllUsers() {
    Query aQuery = this.getNamedQuery("allUsersQuery");

    Collection<User> result;

    result = aQuery.list();

    return result;
  }

  /**
   * Finder.
   * 
   * @param someUserGroups
   *          es una colección que contiene los oids de grupos de usuarios para ser recuperados.
   * @return una colección que contiene los grupos de usuarios.
   */
  @SuppressWarnings("unchecked")
  public Collection<UserGroup> findUserGroupsById(Collection<String> someUserGroups) {

    Collection<UserGroup> userGroups = new ArrayList<UserGroup>();
    Query aQuery = this.getNamedQuery("userGroupsByIdsQuery");
    aQuery.setParameterList("aList", someUserGroups);
    if (!someUserGroups.isEmpty()) {
      userGroups.addAll(aQuery.list());
    }

    return userGroups;
  }

  /**
   * Finder.
   * 
   * @param someUsers
   *          es una colección que contiene los oids de usuarios para ser recuperados.
   * @return una colección que contiene los usuarios.
   */
  @SuppressWarnings("unchecked")
  public Collection<User> findUsersById(Collection<String> someUsers) {

    Collection<User> users = new ArrayList<User>();
    Query aQuery = this.getNamedQuery("usersByIdsQuery");
    aQuery.setParameterList("aList", someUsers);

    if (!someUsers.isEmpty()) {
      users.addAll(aQuery.list());
    }

    return users;
  }

  /**
   * Finder.
   * 
   * @param aTracker
   *          es el objeto que contiene todos los usuarios.
   * @param aString
   *          es el string que se debe buscar en los nombres de los usuarios.
   * @param aLimit
   *          es el límite de elementos a devolver.
   * @return una colección de usuarios que contiene aquellos usuarios cuyos nombre empiezan con el string
   *         recibido.
   */
  public Collection<User> findUsersWithUsernameLike(Tracker aTracker, String aString, int aLimit) {
    Collection<User> users = new ArrayList<User>();
    Query aQuery = this.getNamedQuery("usersWithUsernameLikeQuery");
    aQuery.setParameter("aString", aString + "%");
    aQuery.setMaxResults(aLimit);

    users.addAll(aQuery.list());

    return users;

  }

}
