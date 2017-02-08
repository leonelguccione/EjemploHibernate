/**
 * Este paquete contiene las clases del modelo de negocios de la aplicaci�n.
 */
package zinbig.item.model;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para representar las operaciones que
 * pueden realizar los usuarios en el sistema. <br>
 * Las operaciones luego son asignadas a roles, tanto de sistema como de
 * proyectos.<br>
 * Una operaci�n est� definida por su nombre (que debe ser �nico), una
 * indicaci�n de si una operaci�n de sistema o de proyecto, una indicaci�n de si
 * es una operaci�n asignable a usuario o no y una posici�n en el men�.<br>
 * El sistema no permite agregar nuevas operaciones, sino que deben ser
 * agregadas en forma manual a trav�s de inserts en la base de datos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Operation {

	/**
	 * Es el nombre de la operaci�n.
	 */
	protected String name;

	/**
	 * Es el oid de esta operaci�n.
	 */
	protected String oid;

	/**
	 * Indica si esta operaci�n se muestra o no en el men�.
	 */
	protected boolean visibleInMenu;

	/**
	 * Indica si esta operaci�n es asignable a otros usuarios o solamente para
	 * el administrador. Las operaciones no asignables a otros usuarios no
	 * aparecen en el men� de operaciones disponibles al momento de crear
	 * grupos.
	 */
	protected boolean administrativeOperation;

	/**
	 * Indica el nombre de la categor�a del men� que agrupa a esta operaci�n.
	 */
	protected String categoryName;

	/**
	 * Indica si esta operaci�n es ejecutable sin requerir un usuario en la
	 * sesi�n.
	 */
	protected boolean executableByAnonymousUser;

	/**
	 * Mantiene el nombre de la clase que representa la p�gina a la cual hay que
	 * enviar al usuario para que ejecute esta operaci�n.
	 */
	protected String targetPageClassName;

	/**
	 * Establece en que secci�n del men� debe aparecer esta operaci�n.
	 */
	protected Integer menuSection;

	/**
	 * Constructor por defecto. <br>
	 * Este constructor no deber�a utilizarse. Existe para poder realizar tests
	 * de unidad sobre esta clase.
	 */
	public Operation() {
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre de la nueva operaci�n.
	 * @param aBoolean
	 *            indica si esta operaci�n puede aparecer en el men�.
	 * @param isAdministrativeOperation
	 *            indica si es una operaci�n asignable a otros usuarios o no.
	 * @param aCategoryName
	 *            es el nombre de la categor�a del men� en donde deber�a
	 *            aparecer esta operaci�n.
	 * @param isExecutableWithoutUser
	 *            indica si esta operaci�n se puede ejecutar sin un usuario en
	 *            la sesi�n.
	 * @param aClassName
	 *            es el nombre de la clase que representa la p�gina a la cual
	 *            hay que enviar al usuario para que ejecute esta operaci�n.
	 * @param anInteger
	 *            establece en que secci�n del men� debe mostrarse esta
	 *            operaci�n.
	 */
	public Operation(String aName, boolean aBoolean,
			boolean isAdministrativeOperation, String aCategoryName,
			boolean isExecutableWithoutUser, String aClassName,
			Integer anInteger) {
		this.setName(aName);
		this.setAdministrativeOperation(isAdministrativeOperation);
		this.setVisibleInMenu(aBoolean);
		this.setCategoryName(aCategoryName);
		this.setExecutableByAnonymousUser(isExecutableWithoutUser);
		this.setTargetPageClassName(aClassName);
		this.setMenuSection(anInteger);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la operaci�n.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la operaci�n.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el oid que permite identificar a esta instancia.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid que permite identificar a esta instancia.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que esta operaci�n sea solamente para el
	 *         administrador del sistema; false en cualquier otro caso.
	 */
	public boolean isAdministrativeOperation() {
		return this.administrativeOperation;
	}

	/**
	 * Setter.
	 * 
	 * @param administrativeOperation
	 *            debe ser true en caso de que esta operaci�n sea solamente para
	 *            el administrador; false en cualquier otro caso.
	 */
	public void setAdministrativeOperation(boolean administrativeOperation) {
		this.administrativeOperation = administrativeOperation;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la categor�a del men� en donde debe aparecer esta
	 *         operaci�n.
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * Setter.
	 * 
	 * @param aCategoryName
	 *            es el nombre de la categor�a del men� en donde debe aparecer
	 *            esta operaci�n.
	 */
	public void setCategoryName(String aCategoryName) {
		this.categoryName = aCategoryName;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que esta operaci�n se pueda ejecutar sin un
	 *         usuario en la sesi�n; false en caso contrario.
	 */
	public boolean isExecutableByAnonymousUser() {
		return this.executableByAnonymousUser;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si esta operaci�n es ejecutable sin un usuario en la
	 *            sesi�n.
	 */
	public void setExecutableByAnonymousUser(boolean aBoolean) {
		this.executableByAnonymousUser = aBoolean;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            es el nombre de la clase que representa la p�gina a la cual
	 *            hay que enviar al usuario para que ejecute esta operaci�n.
	 */
	public void setTargetPageClassName(String aClassName) {
		this.targetPageClassName = aClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase que representa la p�gina a la cual hay que
	 *         enviar al usuario para que ejecute esta operaci�n.
	 */
	public String getTargetPageClassName() {
		return this.targetPageClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return true si esta operaci�n puede aparecer en el men�; false en caso
	 *         contrario.
	 */
	public boolean isVisibleInMenu() {
		return this.visibleInMenu;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si esta operaci�n puede aparecer en el men�.
	 */
	public void setVisibleInMenu(boolean aBoolean) {
		this.visibleInMenu = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que representa la secci�n del men� en donde debe
	 *         aparecer esta operaci�n.
	 */
	public Integer getMenuSection() {
		return this.menuSection;
	}

	/**
	 * Setter.
	 * 
	 * @param anInteger
	 *            es un entero que representa la secci�n del men� en donde debe
	 *            aparecer esta operaci�n.
	 */
	public void setMenuSection(Integer anInteger) {
		this.menuSection = anInteger;
	}

}
