/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deber�n implementar.<br>
 * 
 */
package zinbig.item.services.bi;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import zinbig.item.model.exceptions.ItemConcurrentModificationException;
import zinbig.item.model.exceptions.PasswordMismatchException;
import zinbig.item.model.exceptions.UserGroupNotUniqueException;
import zinbig.item.model.exceptions.UserGroupUnknownException;
import zinbig.item.model.exceptions.UserUnknownException;
import zinbig.item.model.exceptions.UsernameNotUniqueException;
import zinbig.item.model.users.User;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.UserGroupDTO;

/**
 * Esta interface define el protocolo de los servicios relacionados con los usuarios del sistema que deber�n
 * ser implementados por clases concretas.<br>
 * Todo servicio en ning�n momento debe devolver un objeto de dominio, sino que siempre devolver� DTOs.<br>
 * Por �ltimo, los servicios no contienen l�gica de negocios propiamente dicha sino que son encargados de
 * ejecutar dicha l�gica presente en el modelo de dominio.<br>
 * Todos los m�todos de esta interface declaran el lanzamiento de excepciones. Algunas ser�n excepciones de
 * negocio y otras inesperadas (como la ca�da de la conexi�n a la base de datos). La declaraci�n del
 * lanzamiento tiene como objetivo lograr que el cliente tenga que manejar excepciones cada vez que invoque a
 * un servicio. Si bien aqu� se declaran excepciones generales, en la documentaci�n de cada m�todo tambi�n
 * figura la excepci�n particular de negocio que podr�a ser lanzada por la implementaci�n en particular. <br>
 * Cada cliente puede elegir trapear excepciones generales y adem�s en forma particular alguna excepci�n de
 * negocio en concreto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public interface UsersServiceBI {

  /**
   * Intenta ingresar al sistema con un nombre de usuario y clave.
   * 
   * @param username
   *          es el nombre de usuario a utilizar.
   * @param password
   *          es la clave del usuario que est� intentando ingresar.
   * @return un DTO que representa al usuario en caso de que la informaci�n provista sea correcta.
   * @throws UserUnknownException
   *           esta excepci�n puede ser lanzada en caso de que no exista el usuario.
   * @throws PasswordMismatchException
   *           esta excepci�n puede ser lanzada en caso de que la clave provista con coincida con la clave
   *           almacenada.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public UserDTO loginUser(String username, String password) throws Exception;

  /**
   * Agrega un nuevo usuario al sistema.
   * 
   * @param aName
   *          es el nombre del usuario.
   * @param username
   *          es el nombre de usuario.
   * @param password
   *          es la clase del nuevo usuario.
   * @param anEmail
   *          es el administratorEmail del nuevo usuario.
   * @param aDate
   *          es la fecha de creaci�n del nuevo usuario.
   * @param aLanguage
   *          es el nombre del idioma del usuario.
   * @param aSurname
   *          es el apellido del usuario.
   * @param userGroupsIterator
   *          es un iterador que permite recorrer los grupos de usuarios asignados al usuario.
   * @return un DTO que representa al nuevo usuario creado.
   * @throws UsernameNotUniqueException
   *           esta excepci�n puede ser lanzada en caso de que ya exista un usuario con el nombre de usuario
   *           provisto.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public UserDTO addUser(String aName, String username, String password, String anEmail, Date aDate,
      String aLanguage, String aSurname, Iterator<UserGroupDTO> userGroupsIterator) throws Exception;

  /**
   * Busca un usuario con el nombre de usuario provisto.
   * 
   * @param anUsername
   *          es el nombre de usuario que se est� buscando.
   * @param status
   *          indica el status del usuario que se busca.
   * @return un DTO que representa al usuario hallado.
   * @throws UserUnknownException
   *           esta excepci�n puede ser levantada en caso de que no exista un usuario con el nombre de usuario
   *           dado.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public UserDTO findUserWithUsername(String anUsername, String status) throws Exception;

  /**
   * Retorna verdadero si existe un usuario con el nombre de usuario dado.
   * 
   * @param anUsername
   *          es el nombre de usuario a buscar.
   * @return true en caso de que exista un usuario; false en caso contrario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public boolean existsUserWithUsername(String anUsername) throws Exception;

  /**
   * Retorna verdadero si existe un grupo de usuarios con el nombre dado. <br>
   * Este m�todo considera tanto los grupos de usuarios de sistema como los de proyectos.
   * 
   * @param aName
   *          es el nombre a buscar.
   * @return true en caso de que exista un grupo de usuarios; false en caso contrario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public boolean containsUserGroupWithName(String aName) throws Exception;

  /**
   * Actualiza la informaci�n b�sica del usuario, es decir email, password, idioma preferido y algunos datos
   * b�sicos m�s.
   * 
   * @param anUserDTO
   *          es el dto que mantiene la informaci�n actualizada que debe ser enviada a la capa del modelo.
   * @param userGroupDTOs
   *          es una colecci�n que contiene todos los dtos de los grupos de usuarios a los cuales se ha
   *          asignado al usuario.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public void updateUserInformation(UserDTO anUserDTO, Collection<UserGroupDTO> userGroupDTOs)
      throws Exception;

  /**
   * Recupera una colecci�n que contiene todas las operaciones que se pueden ejecutar sin un usuario en la
   * sesi�n.
   * 
   * @return una colecci�n con todas las operaciones "an�nimas".
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<OperationDTO> getListOfAnonymousOperations() throws Exception;

  /**
   * Agrega un nuevo grupo de usuarios al sistema.<br>
   * 
   * @param aName
   *          es el nombre del nuevo grupo de usuarios.
   * @param anEmail
   *          es el email del nuevo grupo.
   * @param aLanguage
   *          representa el lenguaje del nuevo grupo de usuarios.
   * @return un dto que representa al nuevo grupo de usuarios.
   * @throws UserGroupNotUniqueException
   *           esta excepci�n puede levantarse en caso de querer agregar un grupo de usuarios con un nombre
   *           que ya se est� utilizando.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public UserGroupDTO addUserGroup(String aName, String anEmail, String aLanguage) throws Exception;

  /**
   * Getter.
   * 
   * @return la cantidad de grupos de usuarios de sistema que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public int getUserGroupsCount() throws Exception;

  /**
   * Getter.
   * 
   * @param index
   *          es el �ndice de inicio.
   * @param count
   *          es la cantidad de elementos a recuperar.
   * @param anOrdering
   *          es el orden que debe aplicarse al resultado.
   * @param aColumnName
   *          es el nombre de la propiedad por la que debe ordenarse el resultado.
   * @return la colecci�n de dtos de los grupos de usuarios del sistema empezando por el �ndice recibido y
   *         devolviendo �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<UserGroupDTO> getUserGroups(int index, int count, String aColumnName, String anOrdering)
      throws Exception;

  /**
   * Recupera todas las operaciones que que no son administrativas.
   * 
   * @return una colecci�n de dtos que representan las operaciones que pueden ser asignadas a otros usuarios.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<OperationDTO> getAllNonAdministrativeOperations() throws Exception;

  /**
   * Edita la informaci�n almacenada del grupo de usuarios recibido.
   * 
   * @param aName
   *          es el nombre actual del grupo de usuarios. Es necesario ya que en la edici�n de un grupo se pudo
   *          haber cambiado incluso el nombre.
   * @param aDto
   *          es el dto que contiene la informaci�n del grupo de usuarios que se debe actualizar.
   * @throws ItemConcurrentModificationException
   *           esta excepci�n puede darse en caso de que dos usuarios traten de modificar el mismo grupo de
   *           usuarios al mismo tiempo.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public void editUserGroup(String aName, UserGroupDTO aDto) throws Exception;

  /**
   * Recupera un grupo de usuarios con el nombre dado.
   * 
   * @param aName
   *          es el nombre del grupo de usuarios.
   * @return el grupo de usuarios.
   * @throws UserGroupUnknownException
   *           esta excepci�n puede ser lanzada en caso de que se trate de recuperar un grupo de usuarios
   *           inexistente.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public UserGroupDTO findUserGroup(String aName) throws Exception;

  /**
   * Getter.
   * 
   * @param index
   *          es el �ndice de inicio.
   * @param count
   *          es la cantidad de elementos a recuperar.
   * @param anOrdering
   *          es el orden que se debe aplicar a los resultados.
   * @param aPropertyName
   *          es el nombre de la propiedad que se debe utilizar para ordenar los resultados.
   * @return la colecci�n de dtos de los usuarios del sistema empezando por el �ndice recibido y devolviendo
   *         �nicamente la cantidad especificada de elementos.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<UserDTO> getUsers(int firstIndex, int count, String aPropertyName, String anOrdering)
      throws Exception;

  /**
   * Getter.
   * 
   * @return la cantidad de usuarios de sistema que existen.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public int getUsersCount() throws Exception;

  /**
   * Recupera todos los grupos de usuarios.
   * 
   * @return una colecci�n de dtos que representan a todos los grupos de usuarios.
   * @throws Exception
   *           esta excepci�n se levanta en caso de alg�n error en la ejecuci�n de este servicio.
   */
  public Collection<UserGroupDTO> getAllUsergroups() throws Exception;

  /**
   * Actualiza una preferencia del usuario.
   * 
   * @param aDto
   *          es el dto que representa la usuario en la sesi�n.
   * @param aKey
   *          es la clave bajo la cual se debe actualizar la preferencia.
   * @param aValue
   *          es el valor de la preferencia.
   * @throws Exception
   *           esta excepci�n se puede levantar por alg�n error al ejecutar este servicio.
   */
  public void updateUserPreference(UserDTO aDto, String aKey, String aValue) throws Exception;

  /**
   * Recupera todos los grupos de usuarios asignados al usuario recibido.
   * 
   * @param anUserDTO
   *          es el dto que representa al usuario para el cual se deben recuperar todos los grupos de usuarios
   *          asignados.
   * @return una colecci�n de dtos de grupos de usuarios.
   * @throws Exception
   *           esta excepci�n se puede levantar por alg�n error al ejecutar este servicio.
   */
  public Collection<UserGroupDTO> getUserGroupsOfUser(UserDTO anUserDTO) throws Exception;

  /**
   * Recupera todos los usuarios del sistema, excepto el administrador.
   * 
   * @return una colecci�n que contiene todos los usuarios del sistema.
   * @throws Exception
   *           esta excepci�n se puede levantar por alg�n error al ejecutar este servicio.
   */
  public Collection<UserDTO> getAllUsers() throws Exception;

  /**
   * Elimina del sistema un grupo de usuarios seleccionados.
   * 
   * @param someUserGroups
   *          es una colecci�n que contiene los oids de los grupos de usuarios que hay que eliminar.
   * @throws Exception
   *           es cualquier excepci�n que puede levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public void deleteUserGroups(Collection<String> someUserGroups) throws Exception;

  /**
   * Carga la informaci�n relacionada con las operaciones del usuario.
   * 
   * @param anUser
   *          es el usuario.
   * @param anUserDTO
   *          es el dto que representa al usuario.
   * @return el dto del usuario con la informaci�n actualizada de las operaciones.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public UserDTO loadOperationsInformation(User anUser, UserDTO anUserDTO) throws Exception;

  /**
   * Elimina del sistema una colecci�n de usuarios.
   * 
   * @param selectedUsers
   *          es la colecci�n que contiene los identificadores de los usuarios seleccionados para ser
   *          eliminados.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public void deleteUsers(Collection<String> selectedUsers) throws Exception;

  /**
   * Obtiene una lista de DTOs que representan a los usuarios cuyos nombre de usuario comienzan con el string
   * recibido.
   * 
   * @param aString
   *          es el string que se debe utilizar para la b�squeda.
   * @param aLimit
   *          es el l�mite de elementos a devolver.
   * @return una colecci�n con los DTOs de los usuarios.
   * @throws Exception
   *           es cualquier excepci�n que podr�a levantarse a ra�z de la ejecuci�n de este servicio.
   */
  public Collection<UserDTO> findUsersWithUsernameLike(String aString, int aLimit) throws Exception;

}
