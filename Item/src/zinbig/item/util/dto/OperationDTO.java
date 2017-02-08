/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Las instancias de esta clase se utilizan pare representar la informaci�n
 * relevante de las operaciones del sistema.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class OperationDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -5011109673036135097L;

	/**
	 * Es el nombre de la instancia de la operaci�n representada por este dto.
	 */
	protected String name;

	/**
	 * Es el nombre de la categor�a del men� en donde debe aparecer la operaci�n
	 * representada por este dto.
	 */
	protected String categoryName;

	/**
	 * Es el nombre de la clase que representa la p�gina a la cual se debe
	 * enviar al usuario para que ejecutte esta operaci�n.
	 */
	protected String targetPageClassName;

	/**
	 * Es un diccionario que contiene una lista de par�metros requeridos para la
	 * ejecuci�n de la operaci�n representada por este dto.
	 */
	protected Map<String, String> parameters;

	/**
	 * Es un entero que establece la secci�n del men� en donde se debe mostrar
	 * la operaci�n representada por este dto.
	 */
	protected Integer menuSection;

	/**
	 * Define si la operaci�n representada por este dto es visible en el men�.
	 */
	protected boolean visibleInMenu;

	/**
	 * Constructor.
	 * 
	 * @param anOid
	 *            es el oid que permite recuperar de la base de datos la
	 *            instancia de la clase Operation representada por este dto.
	 * @param aName
	 *            es el nombre de la instancia de la clase Operation
	 *            representada por este dto.
	 * @param aCategoryName
	 *            es el nombre de la categor�a del men� en donde debe aparecer
	 *            la operaci�n representada por este dto.
	 * @param aClassName
	 *            es el nombre de la clase que representa la p�gina a la cual
	 *            hay que enviar al usuario para que ejecute la operaci�n
	 *            representada por este dto.
	 * @param anInteger
	 *            es un entero que representa la secci�n del men� en donde debe
	 *            aparecer la operaci�n representada por este dto.
	 * @param isVisibleInMenu
	 *            establece si la operaci�n representada por este dto es visible
	 *            en el men�.
	 */
	public OperationDTO(String anOid, String aName, String aCategoryName,
			String aClassName, Integer anInteger, boolean isVisibleInMenu) {
		this.setName(aName);
		this.setOid(anOid);
		this.setCategoryName(aCategoryName);
		this.setTargetPageClassName(aClassName);
		this.setParameters(new HashMap<String, String>());
		this.setMenuSection(anInteger);
		this.setVisibleInMenu(isVisibleInMenu);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la operaci�n representada por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la operaci�n representada por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la categor�a del men� en donde debe aparecer la
	 *         operaci�n representada por este dto.
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la categror�a del men� en donde debe aparecer
	 *            la operaci�n representada por este dto.
	 */
	public void setCategoryName(String aName) {
		this.categoryName = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase que representa la p�gina a la cual hay que
	 *         enviar al usuario para que ejecute la operaci�n representada por
	 *         este dto.
	 */
	public String getTargetPageClassName() {
		return this.targetPageClassName;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            es el nombre de la clase que representa la p�gina a la cual
	 *            hay que enviar al usuario para que ejecute la operaci�n
	 *            representada por este dto.
	 */
	public void setTargetPageClassName(String aClassName) {
		this.targetPageClassName = aClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene los par�metros requeridos para la
	 *         ejecuci�n de la operaci�n representada por este dto.
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Setter.
	 * 
	 * @param someParameters
	 *            es un diccionario que contiene los par�metros requeridos para
	 *            la ejecuci�n de la operaci�n representada por este dto.
	 */
	public void setParameters(Map<String, String> someParameters) {
		this.parameters = someParameters;
	}

	/**
	 * Verifica si el dto tiene par�metros.
	 * 
	 * @return true en caso de que este dto tenga par�metros; false en caso
	 *         contrario.
	 */
	public boolean hasParameters() {
		return !this.getParameters().isEmpty();
	}

	/**
	 * Getter.
	 * 
	 * @return un string que representa el nombre completo de la operaci�n
	 *         representada por este dto. Este nombre completo se compone del
	 *         nombre de la categor�a seguido por el nombre de la operaci�n en
	 *         s�.
	 */
	public String completeName() {
		return this.getCategoryName() + " - " + this.getName();
	}

	/**
	 * Retorna una representaci�n como string del receptor.
	 * 
	 * @return el nombre de la categor�a de la operaci�n, el nombre de la
	 *         operaci�n y la secci�n en donde se debe mostrar.
	 */
	@Override
	public String toString() {
		return "OperationDTO [categoryName=" + categoryName + ", name=" + name
				+ "]" + menuSection;
	}

	/**
	 * Getter.
	 * 
	 * @return la secci�n del men� en donde se debe mostrar la operaci�n
	 *         representada por este dto.
	 */
	public Integer getMenuSection() {
		return this.menuSection;
	}

	/**
	 * Setter.
	 * 
	 * @param anInteger
	 *            es la secci�n del men� en donde se debe mostrar la operaci�n
	 *            representada por este dto.
	 */
	public void setMenuSection(Integer anInteger) {
		this.menuSection = anInteger;
	}

	/**
	 * Retorna el hashCode de este objeto.
	 * 
	 * @return un entero calculado como el hashcode de la suma del nombre de la
	 *         categor�a y del nombre de la operaci�n.
	 */
	@Override
	public int hashCode() {

		return (this.getCategoryName() + this.getName()).hashCode();
	}

	/**
	 * Verifica si este objeto es igual al objeto recibido.
	 * 
	 * @return true en caso de que sean ambos objetos instancias de la clase
	 *         OperationDTO y que ambos coincidan en nombre de la categor�a y
	 *         nombre de la operaci�n representada; false en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		try {
			OperationDTO dto = (OperationDTO) obj;
			result = (this.getCategoryName() + this.getName()).equals(dto
					.getCategoryName()
					+ dto.getName());
		} catch (Exception e) {

		}
		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return si la operaci�n representada por este dto es visible en el men�.
	 */
	public boolean isVisibleInMenu() {
		return this.visibleInMenu;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si la operaci�n representada por este dto es visible
	 *            en el men�.
	 */
	public void setVisibleInMenu(boolean aBoolean) {
		this.visibleInMenu = aBoolean;
	}

}
