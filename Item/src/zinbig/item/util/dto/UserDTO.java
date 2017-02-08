/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Las instancias de esta clase son utilizadas para representar usuarios del
 * sistema. <br>
 * La representación de un usuario está compuesta por su OID, nombre de usuario. <br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * Es posible que este dto represente a un usuario en forma simple, es decir sin
 * cargar todos los datos relacionados en el modelo de objetos. El objetivo de
 * esto es minimizar el tiempo de carga en pantalla en aquellos casos en los que
 * no se requiere de toda la información asociado al usuario. Para crear un dto
 * completo se debe invocar a la clase DTOFactory con el parámetro
 * mustCreateCompleteDTO en true.<br>
 * 
 * Esta clase implementa la interface Versionable para poder controlar la
 * edición concurrente del grupo de usuarios representado por este dto por parte
 * de dos usuarios.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserDTO extends AbstractUserDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2902290945190946834L;

	/**
	 * Es el nombre de usuario del usuario.
	 */
	protected String username;

	/**
	 * Contiene la password encriptada del usuario.
	 */
	protected String password;

	/**
	 * Es una colección que mantiene los dtos de las operaciones asignadas al
	 * usuario representado por este dto.
	 */
	protected Collection<OperationDTO> operations;

	/**
	 * Es el apellido del usuario.
	 */
	protected String surname;

	/**
	 * Es un diccionario que contiene las preferencias realizadas por el
	 * usuario.
	 */
	protected Map<String, String> userPreferences;

	/**
	 * Indica si el usuario representado por este dto es el administrador del
	 * sistema.
	 */
	public boolean adminUser;

	/**
	 * Es un DTO que representa el proyecto por defecto del usuario representado
	 * por este DTO.
	 */
	public ProjectDTO defaultProject;

	/**
	 * Mantiene la cantidad de proyectos marcados como favoritos.
	 */
	public int favoriteProjectsCount;

	/**
	 * Define si el usuario representado por este dto es líder de alguno de sus
	 * proyectos.
	 */
	public boolean projectLeader;

	/**
	 * Constructor.<br>
	 * 
	 * @param anOid
	 *            es el oid de la instancia recibida.
	 * @param anUsername
	 *            es el nombre de usuario del usuario.
	 * @param aPassword
	 *            es la clave encriptada del usuario.
	 * @param aLanguage
	 *            es el nombre del idioma del usuario.
	 * @param anEmail
	 *            es el email del usuario representado por este dto.
	 * @param aName
	 *            es el nombre del usuario representado por este dto.
	 * @param aSurname
	 *            es el nombre del usuario representado por este dto.
	 * @param aBoolean
	 *            indica si el usuario representado por este dto puede ser
	 *            eliminado.
	 * @param aNumber
	 *            es el número de la versión del grupo representado por este
	 *            dto.
	 * @param isAdminUser
	 *            indica si el usuario representado por este dto es
	 *            administrador del sistema.
	 * @param aCount
	 *            es la cantidad de proyectos marcados como favoritos.
	 * @param isProjectLeader
	 *            define si el usuario es líder en alguno de sus proyectos.
	 */
	public UserDTO(String anOid, String anUsername, String aPassword,
			String aLanguage, String anEmail, String aName, String aSurname,
			boolean aBoolean, int aNumber, boolean isAdminUser, int aCount,
			boolean isProjectLeader) {
		super(anEmail, aName, aBoolean, aNumber, aLanguage);
		this.setOid(anOid);
		this.setUsername(anUsername);
		this.setPassword(aPassword);
		this.setOperations(new ArrayList<OperationDTO>());
		this.setSurname(aSurname);
		this.setUserPreferences(new HashMap<String, String>());
		this.setAdminUser(isAdminUser);
		this.setFavoriteProjectsCount(aCount);
		this.setProjectLeader(isProjectLeader);

		this.setAlias(anUsername);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de usuario.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el nombre de usuario.
	 */
	public void setUsername(String anUsername) {
		this.username = anUsername;
	}

	/**
	 * Retorna una representación de este DTO como un string.
	 * 
	 * @return un string que contiene el nombre del usuario representado por
	 *         este DTO.
	 */
	public String toString() {
		return this.getUsername();

	}

	/**
	 * Getter.
	 * 
	 * @return la clave encriptada del usuario representado por este dto.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Setter.
	 * 
	 * @param aPassword
	 *            es la clave del usuario representado por este dto.
	 */
	public void setPassword(String aPassword) {
		this.password = aPassword;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de dtos que representan las operaciones asignadas al
	 *         usuario representado por este dto.
	 */
	public Collection<OperationDTO> getOperations() {
		return this.operations;
	}

	/**
	 * Setter.
	 * 
	 * @param someOperationsDTOs
	 *            es una colección con todos los dtos de las operaciones
	 *            asignadas al usuario representado por este dto.
	 */
	public void setOperations(Collection<OperationDTO> someOperationsDTOs) {
		this.operations = someOperationsDTOs;
	}

	/**
	 * Agrega un nuevo elemento a la colección de dtos de operaciones.
	 * 
	 * @param aDTO
	 *            es el dto que se debe guardar.
	 */
	public void addOperationDto(OperationDTO aDTO) {
		this.getOperations().add(aDTO);
	}

	/**
	 * Getter.
	 * 
	 * @return el email del usuario representado por este dto.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Setter.
	 * 
	 * @param anEmail
	 *            es el email del usuario representado por este dto.
	 */
	public void setEmail(String anEmail) {
		this.email = anEmail;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del usuario representado por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del usuario representado por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el apellido del usuario representado por este dto.
	 */
	public String getSurname() {
		return this.surname;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el apellido del usuario representado por este dto.
	 */
	public void setSurname(String aSurname) {
		this.surname = aSurname;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el usuario representado por este dto pueda
	 *         ser eliminado.
	 */
	public boolean isDeletable() {
		return this.deletable;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si el usuario representado por este dto puede ser
	 *            eliminado.
	 */
	public void setDeletable(boolean aBoolean) {
		this.deletable = aBoolean;
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
	 * Recupera una preferencia del usuario.
	 * 
	 * @param aKey
	 *            es la clave de la preferencia.
	 * @return un String que contiene el valor de la propiedad; o null si no
	 *         existe.
	 */
	public String getUserPreference(String aKey) {
		String result = null;
		if (this.getUserPreferences().containsKey(aKey)) {
			result = this.getUserPreferences().get(aKey);
		}
		return result;
	}

	/**
	 * Verifica si el DTO que representa a un usuario contiene un dto de
	 * operación que representa a la operación cuyo nombre se ha recibido.
	 * 
	 * @param aName
	 *            es el nombre de la operación que se está verificando.
	 * @return true en caso de que este DTO contenga un dto de la operación;
	 *         false en caso contrario.
	 */
	public boolean containsOperationWithName(String aName) {
		Iterator<OperationDTO> iterator = this.getOperations().iterator();
		OperationDTO dto = null;
		boolean found = false;

		while (!found && iterator.hasNext()) {
			dto = iterator.next();
			if (dto.getName().equals(aName)) {
				found = true;
			}
		}

		return found;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que el usuario representado por este dto sea el
	 *         administrador del sistema.
	 */
	public boolean isAdminUser() {
		return this.adminUser;
	}

	/**
	 * Setter.
	 * 
	 * @param adminUser
	 *            indica si el usuario representado por este dto es el
	 *            administrador del sistema.
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * Verifica si el proyecto representado por el dto es un proyecto marcado
	 * como favorito por el usuario.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto que se debe verificar.
	 * @return true si el proyecto fue marcado como favorito; false en cualquier
	 *         otro caso.
	 * 
	 */
	public boolean isFavoriteProject(ProjectDTO aProjectDTO) {
		boolean result = false;
		if (this.getUserPreferences().containsKey("FAVORITE_PROJECT")) {
			String favoriteProjects = ((String) this.getUserPreferences().get(
					"FAVORITE_PROJECT"));
			result = favoriteProjects.contains(aProjectDTO.getOid().toString());
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return el DTO del proyecto por defecto del usuario representado por este
	 *         DTO.
	 */
	public ProjectDTO getDefaultProject() {
		return this.defaultProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el DTO del proyecto por defecto del usuario representado
	 *            por este DTO.
	 */
	public void setDefaultProject(ProjectDTO aProjectDTO) {
		this.defaultProject = aProjectDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return false ya que este dto representa a un usuario simple.
	 */
	@Override
	public boolean isUserGroup() {

		return false;
	}

	/**
	 * Verifica si dos objetos son iguales.
	 * 
	 * @return true en caso de que ambos objetos sean DTO de usuarios y que el
	 *         usuario representado sea el mismo (mismo username).
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;
		try {
			UserDTO anUserDTO = (UserDTO) anObject;
			result = anUserDTO.getUsername().equals(this.getUsername());
		} catch (Exception e) {

		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos marcados como favoritos.
	 */
	public int getFavoriteProjectsCount() {
		return this.favoriteProjectsCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es el número de proyectos marcados como favoritos.
	 */
	public void setFavoriteProjectsCount(int aCount) {
		this.favoriteProjectsCount = aCount;
	}

	/**
	 * Elimina la información de la operación que permite acceder al dashboard
	 * del proyecto favorito.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa la proyecto que se ha desmarcado como
	 *            favorito.
	 */
	public void removeFavoriteProject(ProjectDTO aProjectDTO) {
		Iterator<OperationDTO> operations = this.getOperations().iterator();
		OperationDTO anOperation = null;

		while (operations.hasNext()) {
			anOperation = operations.next();
			if (anOperation.getCategoryName().equals("PROJECTS_CATEGORY")
					&& anOperation.getName().equals("VIEW_PROJECT_DASHBOARD")
					&& anOperation.getParameters().containsValue(
							aProjectDTO.getOid())) {
				operations.remove();
			}
		}

	}

	/**
	 * Agrega la información de la operación que permite acceder al dashboard
	 * del proyecto favorito.
	 * 
	 * @param aProjectDTO
	 *            es el dto que representa la proyecto que se ha marcado como
	 *            favorito.
	 */
	public void addFavoriteProject(ProjectDTO aProjectDTO) {

		OperationDTO anOperation = null;

		anOperation = new OperationDTO("", "VIEW_PROJECT_DASHBOARD",
				"PROJECTS_CATEGORY",
				"zinbig.item.application.pages.ProjectDashboardPage",
				new Integer(3), true);
		anOperation.getParameters()
				.put(
						"PROJECT_MENU_TITLE",
						"[" + aProjectDTO.getShortName() + "] "
								+ aProjectDTO.getName());
		try {
			anOperation.getParameters()
					.put(
							"PROJECT_OID",
							URLEncoder.encode(aProjectDTO.getOid().toString(),
									"UTF-8"));
			this.getOperations().add(anOperation);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Actualiza este DTO agregando un nuevo filtro favorito.
	 * 
	 * @param aFilterDTO
	 *            es el filtro que se está agregando.
	 */
	public void addFavoriteFilter(FilterDTO aFilterDTO) {

		OperationDTO dto = new OperationDTO("", "FILTER_ITEMS",
				"ITEMS_CATEGORY",
				"zinbig.item.application.pages.ViewItemsPage", new Integer(2),
				true);
		dto.getParameters().put("FILTER_MENU_TITLE", aFilterDTO.getName());
		try {
			dto.getParameters().put("FILTER_OID",
					URLEncoder.encode(aFilterDTO.getOid().toString(), "UTF-8"));
			this.getOperations().add(dto);
		} catch (Exception e) {

		}

	}

	/**
	 * Getter.
	 * 
	 * @return true si el usuario representado por este dto es líder de alguno
	 *         de sus proyectos; false en caso contrario.
	 */
	public boolean isProjectLeader() {
		return this.projectLeader;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            define si el usuario representado por este dto es líder de
	 *            alguno de sus proyectos.
	 */
	public void setProjectLeader(boolean aBoolean) {
		this.projectLeader = aBoolean;
	}

	/**
	 * Actualiza este DTO eliminando la operación del menú relacionada con el
	 * filtro eliminado.
	 * 
	 * @param aFilterDTO
	 *            es el filtro que se está eliminando.
	 */
	public void removeFilter(FilterDTO aFilterDTO) {
		Iterator<OperationDTO> operations = this.getOperations().iterator();
		OperationDTO anOperation = null;

		while (operations.hasNext()) {
			anOperation = operations.next();
			if (anOperation.getCategoryName().equals("ITEMS_CATEGORY")
					&& anOperation.getName().equals("FILTER_ITEMS")
					&& anOperation.getParameters().containsValue(
							aFilterDTO.getName())) {
				operations.remove();
			}
		}
	}

}
