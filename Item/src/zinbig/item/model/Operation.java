/**
 * Este paquete contiene las clases del modelo de negocios de la aplicación.
 */
package zinbig.item.model;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para representar las operaciones que
 * pueden realizar los usuarios en el sistema. <br>
 * Las operaciones luego son asignadas a roles, tanto de sistema como de
 * proyectos.<br>
 * Una operación está definida por su nombre (que debe ser único), una
 * indicación de si una operación de sistema o de proyecto, una indicación de si
 * es una operación asignable a usuario o no y una posición en el menú.<br>
 * El sistema no permite agregar nuevas operaciones, sino que deben ser
 * agregadas en forma manual a través de inserts en la base de datos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class Operation {

	/**
	 * Es el nombre de la operación.
	 */
	protected String name;

	/**
	 * Es el oid de esta operación.
	 */
	protected String oid;

	/**
	 * Indica si esta operación se muestra o no en el menú.
	 */
	protected boolean visibleInMenu;

	/**
	 * Indica si esta operación es asignable a otros usuarios o solamente para
	 * el administrador. Las operaciones no asignables a otros usuarios no
	 * aparecen en el menú de operaciones disponibles al momento de crear
	 * grupos.
	 */
	protected boolean administrativeOperation;

	/**
	 * Indica el nombre de la categoría del menú que agrupa a esta operación.
	 */
	protected String categoryName;

	/**
	 * Indica si esta operación es ejecutable sin requerir un usuario en la
	 * sesión.
	 */
	protected boolean executableByAnonymousUser;

	/**
	 * Mantiene el nombre de la clase que representa la página a la cual hay que
	 * enviar al usuario para que ejecute esta operación.
	 */
	protected String targetPageClassName;

	/**
	 * Establece en que sección del menú debe aparecer esta operación.
	 */
	protected Integer menuSection;

	/**
	 * Constructor por defecto. <br>
	 * Este constructor no debería utilizarse. Existe para poder realizar tests
	 * de unidad sobre esta clase.
	 */
	public Operation() {
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre de la nueva operación.
	 * @param aBoolean
	 *            indica si esta operación puede aparecer en el menú.
	 * @param isAdministrativeOperation
	 *            indica si es una operación asignable a otros usuarios o no.
	 * @param aCategoryName
	 *            es el nombre de la categoría del menú en donde debería
	 *            aparecer esta operación.
	 * @param isExecutableWithoutUser
	 *            indica si esta operación se puede ejecutar sin un usuario en
	 *            la sesión.
	 * @param aClassName
	 *            es el nombre de la clase que representa la página a la cual
	 *            hay que enviar al usuario para que ejecute esta operación.
	 * @param anInteger
	 *            establece en que sección del menú debe mostrarse esta
	 *            operación.
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
	 * @return el nombre de la operación.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la operación.
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
	 * @return true en caso de que esta operación sea solamente para el
	 *         administrador del sistema; false en cualquier otro caso.
	 */
	public boolean isAdministrativeOperation() {
		return this.administrativeOperation;
	}

	/**
	 * Setter.
	 * 
	 * @param administrativeOperation
	 *            debe ser true en caso de que esta operación sea solamente para
	 *            el administrador; false en cualquier otro caso.
	 */
	public void setAdministrativeOperation(boolean administrativeOperation) {
		this.administrativeOperation = administrativeOperation;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la categoría del menú en donde debe aparecer esta
	 *         operación.
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * Setter.
	 * 
	 * @param aCategoryName
	 *            es el nombre de la categoría del menú en donde debe aparecer
	 *            esta operación.
	 */
	public void setCategoryName(String aCategoryName) {
		this.categoryName = aCategoryName;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que esta operación se pueda ejecutar sin un
	 *         usuario en la sesión; false en caso contrario.
	 */
	public boolean isExecutableByAnonymousUser() {
		return this.executableByAnonymousUser;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si esta operación es ejecutable sin un usuario en la
	 *            sesión.
	 */
	public void setExecutableByAnonymousUser(boolean aBoolean) {
		this.executableByAnonymousUser = aBoolean;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            es el nombre de la clase que representa la página a la cual
	 *            hay que enviar al usuario para que ejecute esta operación.
	 */
	public void setTargetPageClassName(String aClassName) {
		this.targetPageClassName = aClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase que representa la página a la cual hay que
	 *         enviar al usuario para que ejecute esta operación.
	 */
	public String getTargetPageClassName() {
		return this.targetPageClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return true si esta operación puede aparecer en el menú; false en caso
	 *         contrario.
	 */
	public boolean isVisibleInMenu() {
		return this.visibleInMenu;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si esta operación puede aparecer en el menú.
	 */
	public void setVisibleInMenu(boolean aBoolean) {
		this.visibleInMenu = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que representa la sección del menú en donde debe
	 *         aparecer esta operación.
	 */
	public Integer getMenuSection() {
		return this.menuSection;
	}

	/**
	 * Setter.
	 * 
	 * @param anInteger
	 *            es un entero que representa la sección del menú en donde debe
	 *            aparecer esta operación.
	 */
	public void setMenuSection(Integer anInteger) {
		this.menuSection = anInteger;
	}

}
