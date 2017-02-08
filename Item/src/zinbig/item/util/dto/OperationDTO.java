/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Las instancias de esta clase se utilizan pare representar la información
 * relevante de las operaciones del sistema.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class OperationDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -5011109673036135097L;

	/**
	 * Es el nombre de la instancia de la operación representada por este dto.
	 */
	protected String name;

	/**
	 * Es el nombre de la categoría del menú en donde debe aparecer la operación
	 * representada por este dto.
	 */
	protected String categoryName;

	/**
	 * Es el nombre de la clase que representa la página a la cual se debe
	 * enviar al usuario para que ejecutte esta operación.
	 */
	protected String targetPageClassName;

	/**
	 * Es un diccionario que contiene una lista de parámetros requeridos para la
	 * ejecución de la operación representada por este dto.
	 */
	protected Map<String, String> parameters;

	/**
	 * Es un entero que establece la sección del menú en donde se debe mostrar
	 * la operación representada por este dto.
	 */
	protected Integer menuSection;

	/**
	 * Define si la operación representada por este dto es visible en el menú.
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
	 *            es el nombre de la categoría del menú en donde debe aparecer
	 *            la operación representada por este dto.
	 * @param aClassName
	 *            es el nombre de la clase que representa la página a la cual
	 *            hay que enviar al usuario para que ejecute la operación
	 *            representada por este dto.
	 * @param anInteger
	 *            es un entero que representa la sección del menú en donde debe
	 *            aparecer la operación representada por este dto.
	 * @param isVisibleInMenu
	 *            establece si la operación representada por este dto es visible
	 *            en el menú.
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
	 * @return el nombre de la operación representada por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la operación representada por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la categoría del menú en donde debe aparecer la
	 *         operación representada por este dto.
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la categroría del menú en donde debe aparecer
	 *            la operación representada por este dto.
	 */
	public void setCategoryName(String aName) {
		this.categoryName = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la clase que representa la página a la cual hay que
	 *         enviar al usuario para que ejecute la operación representada por
	 *         este dto.
	 */
	public String getTargetPageClassName() {
		return this.targetPageClassName;
	}

	/**
	 * Setter.
	 * 
	 * @param aClassName
	 *            es el nombre de la clase que representa la página a la cual
	 *            hay que enviar al usuario para que ejecute la operación
	 *            representada por este dto.
	 */
	public void setTargetPageClassName(String aClassName) {
		this.targetPageClassName = aClassName;
	}

	/**
	 * Getter.
	 * 
	 * @return un diccionario que contiene los parámetros requeridos para la
	 *         ejecución de la operación representada por este dto.
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Setter.
	 * 
	 * @param someParameters
	 *            es un diccionario que contiene los parámetros requeridos para
	 *            la ejecución de la operación representada por este dto.
	 */
	public void setParameters(Map<String, String> someParameters) {
		this.parameters = someParameters;
	}

	/**
	 * Verifica si el dto tiene parámetros.
	 * 
	 * @return true en caso de que este dto tenga parámetros; false en caso
	 *         contrario.
	 */
	public boolean hasParameters() {
		return !this.getParameters().isEmpty();
	}

	/**
	 * Getter.
	 * 
	 * @return un string que representa el nombre completo de la operación
	 *         representada por este dto. Este nombre completo se compone del
	 *         nombre de la categoría seguido por el nombre de la operación en
	 *         sí.
	 */
	public String completeName() {
		return this.getCategoryName() + " - " + this.getName();
	}

	/**
	 * Retorna una representación como string del receptor.
	 * 
	 * @return el nombre de la categoría de la operación, el nombre de la
	 *         operación y la sección en donde se debe mostrar.
	 */
	@Override
	public String toString() {
		return "OperationDTO [categoryName=" + categoryName + ", name=" + name
				+ "]" + menuSection;
	}

	/**
	 * Getter.
	 * 
	 * @return la sección del menú en donde se debe mostrar la operación
	 *         representada por este dto.
	 */
	public Integer getMenuSection() {
		return this.menuSection;
	}

	/**
	 * Setter.
	 * 
	 * @param anInteger
	 *            es la sección del menú en donde se debe mostrar la operación
	 *            representada por este dto.
	 */
	public void setMenuSection(Integer anInteger) {
		this.menuSection = anInteger;
	}

	/**
	 * Retorna el hashCode de este objeto.
	 * 
	 * @return un entero calculado como el hashcode de la suma del nombre de la
	 *         categoría y del nombre de la operación.
	 */
	@Override
	public int hashCode() {

		return (this.getCategoryName() + this.getName()).hashCode();
	}

	/**
	 * Verifica si este objeto es igual al objeto recibido.
	 * 
	 * @return true en caso de que sean ambos objetos instancias de la clase
	 *         OperationDTO y que ambos coincidan en nombre de la categoría y
	 *         nombre de la operación representada; false en caso contrario.
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
	 * @return si la operación representada por este dto es visible en el menú.
	 */
	public boolean isVisibleInMenu() {
		return this.visibleInMenu;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece si la operación representada por este dto es visible
	 *            en el menú.
	 */
	public void setVisibleInMenu(boolean aBoolean) {
		this.visibleInMenu = aBoolean;
	}

}
