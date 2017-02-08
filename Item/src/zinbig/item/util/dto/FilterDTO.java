/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan para representar un filtro de ítems.
 * Esta clase implementa la interface Serializable de modo de poder almacenarla
 * en una sesión web de trabajo.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterDTO extends ItemAbstractDTO implements Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -125418223102280127L;

	/**
	 * Mantiene el nombre del filtro.
	 */
	protected String name;

	/**
	 * Es un string que representa al filtro.
	 */
	protected String filterString;

	/**
	 * Es un string que representa al componente del filtro que se utiliza para
	 * filtrar por proyecto.
	 */
	protected String filterComponentByProject;

	/**
	 * Es un string que representa al componente del filtro que se utiliza para
	 * filtrar por id.
	 */
	protected String filterComponentByItemId;

	/**
	 * Es un string que representa al componente del filtro que se utiliza para
	 * filtrar por responsable.
	 */
	protected String filterComponentByResponsible;

	/**
	 * Es un string que representa al componente del filtro que se utiliza para
	 * filtrar por tipo.
	 */
	protected String filterComponentByItemType;

	/**
	 * Es un string que representa al componente del filtro que se utiliza para
	 * filtrar por un texto.
	 */
	protected String filterComponentByText;

	/**
	 * Es un string que representa al componente del filtro que se utiliza para
	 * filtrar por nodo de workflow.
	 */
	protected String filterComponentByNode;

	/**
	 * Es un string que representa los estados por los cuales se deben filtrar
	 * los items.
	 */
	protected String filterComponentByState;

	/**
	 * Indica si el filtro representado por este dto es favorito.
	 */
	protected boolean favorite;

	/**
	 * Indica si se debe negar o no la condición del filtro de ítems por su
	 * tipo.
	 */
	protected boolean negateItemType;

	/**
	 * Indica si se debe negar o no la condición del filtro de ítems por su nodo
	 * de workflow.
	 */
	protected boolean negateNode;

	/**
	 * Indica si se debe negar o no la condición del filtro de ítems por su
	 * proyecto.
	 */
	protected boolean negateProject;

	/**
	 * Indica si se debe negar o no la condición del filtro de ítems por su
	 * responsable.
	 */
	protected boolean negateResponsible;

	/**
	 * Indica si se debe negar o no la condición del filtro de ítems por su
	 * estado.
	 */
	protected boolean negateState;

	/**
	 * Constructor.
	 * 
	 * @param anOid
	 *            es el oid del objeto representado por este dto.
	 * @param aName
	 *            es el nombre del filtro representado por este dto.
	 * @param aBoolean
	 *            indica si el filtro representado por este dto es un filtro
	 *            favorito.
	 * 
	 */
	public FilterDTO(String anOid, String aName, boolean aBoolean) {

		this.setOid(anOid);
		this.setName(aName);
		this.setFavorite(aBoolean);
	}

	/**
	 * Constructor.
	 */
	public FilterDTO() {
		this("", "", false);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del filtro representado por este dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del filtro representado por este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return un entero que presenta el hashcode de este objeto. Este entero se
	 *         calcula a partir del nombre del filtro.
	 */
	@Override
	public int hashCode() {

		return this.getName().hashCode();
	}

	/**
	 * Verifica si el objeto recibido es igual al receptor.
	 * 
	 * @param anObject
	 *            es el objeto que hay que comparar.
	 * @return true en caso de que ambos objetos sean instancias de la clase
	 *         FilterDTO y que representen al mismo filtro (mismo oid); false en
	 *         caso contrario.
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;

		try {
			FilterDTO aDTO = (FilterDTO) anObject;
			if (aDTO.getOid().equals(this.getOid())) {
				result = true;
			}
		} catch (Exception e) {
			// no se debe hacer nada ya que probablemente haya fallado debido a
			// una excepción de casting.
		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return un string que representa al filtro.
	 */
	public String getFilterString() {
		return this.filterString;
	}

	/**
	 * Setter.
	 * 
	 * @param aString
	 *            es el string que representa al filtro.
	 */
	public void setFilterString(String aString) {
		this.filterString = aString;
	}

	/**
	 * Getter.
	 * 
	 * @return un string que representa al componente del filtro que se utiliza
	 *         para filtrar por proyecto.
	 */
	public String getFilterComponentByProject() {
		return this.filterComponentByProject;
	}

	/**
	 * Setter.
	 * 
	 * @param filterComponentByProject
	 *            es un string que representa al componente del filtro que se
	 *            utiliza para filtrar por proyecto.
	 */
	public void setFilterComponentByProject(String filterComponentByProject) {
		this.filterComponentByProject = filterComponentByProject;
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que este dto represente a un filtro favorito.
	 */
	public boolean isFavorite() {
		return this.favorite;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si este dto representa a un filtro favorito.
	 */
	public void setFavorite(boolean aBoolean) {
		this.favorite = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el string que representa los estados por los cuales hay que
	 *         filtrar a los items.
	 */
	public String getFilterComponentByState() {
		return this.filterComponentByState;
	}

	/**
	 * Setter.
	 * 
	 * @param aString
	 *            es un string que representa los estados por los cuales hay que
	 *            filtrar a los items.
	 */
	public void setFilterComponentByState(String aString) {
		this.filterComponentByState = aString;
	}

	/**
	 * Obtiene una representación como string del receptor.
	 * 
	 * @return el nombre del filtro representado por este dto.
	 */
	public String toString() {
		return this.getName();
	}

	/**
	 * Getter.
	 * 
	 * @return el string que representa el componente del filtro por id.
	 */
	public String getFilterComponentByItemId() {
		return this.filterComponentByItemId;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el string que representa el componente del filtro por id.
	 */
	public void setFilterComponentByItemId(String aFilterComponent) {
		this.filterComponentByItemId = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return el string que representa el componente del filtro por
	 *         responsable.
	 */
	public String getFilterComponentByResponsible() {
		return this.filterComponentByResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el string que representa el componente del filtro por
	 *            responsable.
	 */
	public void setFilterComponentByResponsible(String aFilterComponent) {
		this.filterComponentByResponsible = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return el string que representa el componente del filtro por tipo.
	 */
	public String getFilterComponentByItemType() {
		return this.filterComponentByItemType;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el string que representa el componente del filtro por tipo.
	 */
	public void setFilterComponentByItemType(String aFilterComponent) {
		this.filterComponentByItemType = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return el string que representa el componente del filtro por nodo de
	 *         workflow.
	 */
	public String getFilterComponentByNode() {
		return this.filterComponentByNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el string que representa el componente del filtro por nodo
	 *            de workflow.
	 */
	public void setFilterComponentByNode(String aFilterComponent) {
		this.filterComponentByNode = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del filtro por el tipo del
	 *         ítem.
	 */
	public boolean isNegateItemType() {
		return this.negateItemType;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si se debe negar la condición del filtro por el tipo
	 *            del ítem.
	 */
	public void setNegateItemType(boolean aBoolean) {
		this.negateItemType = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del filtro por el nodo del
	 *         workflow.
	 */
	public boolean isNegateNode() {
		return this.negateNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si se debe negar la condición del filtro de ítems por
	 *            el nodo del workflow.
	 */
	public void setNegateNode(boolean aBoolean) {
		this.negateNode = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del filtro de ítems por el
	 *         proyecto.
	 */
	public boolean isNegateProject() {
		return this.negateProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si se debe negar la condición del filtro de ítems por
	 *            el proyecto.
	 */
	public void setNegateProject(boolean aBoolean) {
		this.negateProject = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del filtro de ítems por el
	 *         responsable.
	 */
	public boolean isNegateResponsible() {
		return this.negateResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si se debe negar la condición del filtro de ítems por
	 *            el responsable.
	 */
	public void setNegateResponsible(boolean aBoolean) {
		this.negateResponsible = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return true si se debe negar la condición del filtro de ítems por su
	 *         estado.
	 */
	public boolean isNegateState() {
		return this.negateState;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si se debe negar la condición del filtro de ítems por
	 *            el estado.
	 */
	public void setNegateState(boolean aBoolean) {
		this.negateState = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el texto por el cual se deben filtrar los ítems.
	 */
	public String getFilterComponentByText() {
		return this.filterComponentByText;
	}

	/**
	 * Setter.
	 * 
	 * @param aText
	 *            es el texto por el cual se deben filtrar los ítems.
	 */
	public void setFilterComponentByText(String aText) {
		this.filterComponentByText = aText;
	}

}
