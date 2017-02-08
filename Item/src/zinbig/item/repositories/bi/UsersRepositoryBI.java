/**
 * Este paquete contiene las definiciones de las interfaces de negocio que 
 * deberán respetar las implementaciones del patrón de diseño Repository.
 */
package zinbig.item.repositories.bi;

import java.util.Collection;
import java.util.Iterator;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.UserGroupUnknownException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.users.AbstractUser;
import zinbig.item.model.users.User;
import zinbig.item.model.users.UserGroup;
import zinbig.item.util.dto.UserGroupDTO;

/**
 * Esta interface establece el protocolo estándar que deberá ser respetado por todas las clases que
 * implementen el patrón de diseño Repository para acceder eficientemente a las instancias de la clase User.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public interface UsersRepositoryBI extends ItemAbstractRepositoryBI {

  /**
   * Verifica si existe un usuario con un nombre de usuario dado en el sistema.
   * 
   * @param anUsername
   *          el nombre de usuario a buscar.
   * @param aTracker
   *          es el objeto responsable de administrar la colección de usuarios.
   * @return true en caso de que exista un usuario con el nombre de usuario dado; false en caso contrario.
   */
  public boolean containsUserWithUsername(Tracker aTracker, String anUsername);

  /**
   * Verifica si existe un usuario con un nombre de usuario dado dentro de un grupo de usuarios.
   * 
   * @param anUsername
   *          el nombre de usuario a buscar.
   * @param aGroup
   *          es el objeto responsable de administrar la colección de usuarios.
   * @return true en caso de que exista un usuario con el nombre de usuario dado; false en caso contrario.
   */
  public boolean containsUserWithUsernameInUserGroup(UserGroup aGroup, String anUsername);

  /**
   * Recupera un usuario con un nombre de usuario dado.
   * 
   * @param anUsername
   *          es el nombre de usuario del usuario que se está buscando.
   * @param aTracker
   *          es el objeto responsable de administrar la colección de usuarios.
   * @param status
   *          indica que status debe tener el usuario a ser buscado.
   * @return el usuario encontrado.
   * @throws UserUnknownException
   *           esta excepción puede ser lanzada cuando no se encuentra a un usuario con el nombre de usuario
   *           dado.
   */
  public User findUserWithUsername(Tracker aTracker, String anUsername, String status)
      throws UserUnknownException;

  /**
   * Recupera un usuario con un nombre de usuario dado, buscándolo dentro de un grupo de usuarios en
   * particular.
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
  public User findUserWithUsernameInUserGroup(UserGroup aGroup, String anUsername)
      throws UserUnknownException;

  /**
   * Recupera un grupo de usuarios con el nombre dado que contenga al usuario recibido.
   * 
   * @param anUser
   *          es el usuario que conoce al grupo que se está buscanado.
   * @param aName
   *          es el nombre del grupo que se está buscando.
   * @return el grupo cuyo nombre se recibió.
   * @throws UserGroupUnknownException
   *           esta excepción se levanta en caso de que no exista un grupo con el nombre dado para el usuario.
   */
  public UserGroup findUserGroupByNameForUser(User anUser, String aName) throws UserGroupUnknownException;

  /**
   * Verifica si el sistema cuenta con un grupo de usuarios con el nombre dado. <br>
   * Este método considera tanto los grupos de usuarios de sistema como los de proyectos.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema.
   * @param aName
   *          es el nombre del grupo que se está buscando.
   * @return true en caso de que el sistema contenga un grupo con el nombre dado; false en caso contrario.
   */
  public boolean containsUserGroupWithName(Tracker aTracker, String aName);

  /**
   * Recupera una colección de grupos de usuarios que esté contenida entre los índices recibidos.
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
  public Collection<UserGroup> findUserGroups(int beginIndex, int endIndex, String aColumnName,
      String anOrdering);

  /**
   * Recupera un grupo de usuarios con el nombre dado. <br>
   * Este método recupera tanto grupos de usuarios de sistema como de proyectos.
   * 
   * @param aTracker
   *          es el objeto que representa al sistema y que contiene los grupos de usuarios.
   * @param aName
   *          es el nombre del grupo.
   * @return el grupo con el nombre dado.
   * @throws UserGroupUnknownException
   *           esta excepción se levanta en caso de no encontrar un grupo con el nombre dado.
   */
  public UserGroup findUserGroupByName(Tracker aTracker, String aName) throws UserGroupUnknownException;

  /**
   * Getter.
   * 
   * @return la cantidad de grupos de usuarios que existen.
   */
  public int getUserGroupsCount();

  /**
   * Getter.
   * 
   * @return la cantidad de usuarios que existen.
   */
  public int getUsersCount();

  /**
   * Recupera una colección de usuarios que esté contenida entre los índices recibidos.
   * 
   * @param anOrdering
   *          es el orden que se debe aplicar a los resultados.
   * @param aPropertyName
   *          es el nombre de la propiedad que se debe utilizar para ordenar los resultados.
   * @param beginIndex
   *          es el índice de inicio.
   * @param endIndex
   *          es el índice de fin.
   * @return una colección de usuarios de sistema.
   */
  public Collection<User> findUsers(int index, int count, String aPropertyName, String anOrdering);

  /**
   * Recupera todos los grupos de usuarios.
   * 
   * @return una colección que contiene todos los grupos de usuarios.
   */
  public Collection<UserGroup> findAllUserGroups();

  /**
   * Recupera una colección de grupos de usuarios cuyos oids se encuentran en el iterador recibido.
   * 
   * @param iterator
   *          es el iterador que permite recorrer todos los grupos a retornar.
   * @return una colección con los grupos recuperados.
   */
  public Collection<UserGroup> findUserGroups(Iterator<UserGroupDTO> iterator);

  /**
   * Recupera una colección de grupos de usuarios asignados a un usuario en particular.
   * 
   * @param anUser
   *          es el usuario que tiene asignado los grupos de usuarios.
   * @return una colección de grupos de usuarios.
   */
  public Collection<UserGroup> findUserGroupsOfUser(User anUser);

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
  public AbstractUser find(Tracker aTracker, String anOid) throws UserUnknownException;

  /**
   * Finder.
   * 
   * @return una colección que contiene todos los usuarios del sistema, exceptuando al administrador.
   */
  public Collection<User> findAllUsers();

  /**
   * Finder.
   * 
   * @param someUserGroups
   *          es una colección que contiene los oids de grupos de usuarios para ser recuperados.
   * @return una colección que contiene los grupos de usuarios.
   */
  public Collection<UserGroup> findUserGroupsById(Collection<String> someUserGroups);

  /**
   * Finder.
   * 
   * @param someUsers
   *          es una colección que contiene los oids de usuarios para ser recuperados.
   * @return una colección que contiene los usuarios.
   */
  public Collection<User> findUsersById(Collection<String> someUsers);

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
  public Collection<User> findUsersWithUsernameLike(Tracker aTracker, String aString, int aLimit);

}
