/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

import zinbig.item.model.visitors.Visitor;
import zinbig.item.util.IDGenerator;

/**
 * Esta clase abstracta define el comportamiento a ser implementado por los
 * filtros de ítems.<br>
 * 
 * Estos filtros pueden ser creados por el usuario y guardados tanto en su
 * perfil como para compartir con el resto de los usuarios.<br>
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */

public class Filter {

	/**
	 * Es el identificador de este objeto.
	 */
	public String oid;

	/**
	 * Es el nombre del filtro.
	 */
	public String name;

	/**
	 * Mantiene el string del filtro. Este string se crea para hacer más
	 * eficiente la ejecución del filtro.
	 */
	public String filterString;

	/**
	 * Mantiene una referencia a un colaborador que permite filtrar los items a
	 * partir de los proyectos.
	 */
	public String filterComponentByProject;

	/**
	 * Mantiene una referencia a un colaborador que permite filtrar los items a
	 * partir de los ids.
	 */
	public String filterComponentByItemId;

	/**
	 * Mantiene una referencia a un colaborador que permite filtrar los items a
	 * partir de los responsables.
	 */
	public String filterComponentByResponsible;

	/**
	 * Mantiene una referencia a un colaborador que permite filtrar los items a
	 * partir de los tipos.
	 */
	public String filterComponentByItemType;

	/**
	 * Mantiene una referencia a un texto por el cual se debe buscar los ítems.
	 */
	public String filterComponentByText;

	/**
	 * Mantiene una referencia a un colaborador que permite filtrar los items a
	 * partir de los nodos del workflow en el que se encuentran.
	 */
	public String filterComponentByNode;

	/**
	 * Mantiene la referencia a los enteros que representan los estados de los
	 * items por el que hay que filtrarlos.
	 */
	public String filterComponentByState;

	/**
	 * Indica si este filtro es un filtro favorito del usuario.
	 */
	public boolean favorite;

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
	 * Constructor vacío.
	 */
	public Filter() {
		this("");
	}

	/**
	 * Constructor.
	 * 
	 * @param aName
	 *            es el nombre del filtro.
	 */
	public Filter(String aName) {
		this.setName(aName);
		this.setFavorite(false);
		this.setOid(IDGenerator.getId());
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de este objeto.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de este objeto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del filtro.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del filtro.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el string que representa a este filtro.
	 */
	public String getFilterString() {
		return this.filterString;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterString
	 *            es el string que representa a este filtro.
	 */
	public void setFilterString(String aFilterString) {
		this.filterString = aFilterString;

	}

	/**
	 * Getter.
	 * 
	 * @return el componente de este filtro que permite filtrar los items a
	 *         partir del proyecto.
	 */
	public String getFilterComponentByProject() {
		return this.filterComponentByProject;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponentByProject
	 *            es el componente de este filtro que permitira filtrar los
	 *            items a partir de los proyectos..
	 * 
	 */
	public void setFilterComponentByProject(String aFilterComponentByProject) {
		this.filterComponentByProject = aFilterComponentByProject;
	}

	/**
	 * Getter.
	 * 
	 * @return si este filtro es un filtro favorito del usuario.
	 */
	public boolean isFavorite() {
		return this.favorite;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            indica si este filtro es favorito o no.
	 */
	public void setFavorite(boolean aBoolean) {
		this.favorite = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return un string que representa al componente de filtro por estado.
	 */
	public String getFilterComponentByState() {
		return this.filterComponentByState;
	}

	/**
	 * Setter.
	 * 
	 * @param aString
	 *            es un string que representa al componente de filtro por
	 *            estado.
	 */
	public void setFilterComponentByState(String aString) {
		this.filterComponentByState = aString;
	}

	/**
	 * Recibe un visitante que aplica una operación sobre este objeto.
	 * 
	 * @param aVisitor
	 *            es le visitante que se está recibiendo.
	 * @return un objeto que representa el resultado de la aplicación del
	 *         visitante en este objeto.
	 */
	public Object acceptVisitor(Visitor aVisitor) {
		return aVisitor.visitFilter(this);
	}

	/**
	 * Getter.
	 * 
	 * @return el componente de este filtro que permite filtrar por el id del
	 *         ítem.
	 */
	public String getFilterComponentByItemId() {
		return this.filterComponentByItemId;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el componente de este filtro que permite filtrar por el id
	 *            del ítem.
	 */
	public void setFilterComponentByItemId(String aFilterComponent) {
		this.filterComponentByItemId = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente de este filtro que permite filtrar por su
	 *         responsable.
	 */
	public String getFilterComponentByResponsible() {
		return this.filterComponentByResponsible;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el componente de este filtro que permite filtrar por su
	 *            responsable.
	 */
	public void setFilterComponentByResponsible(String aFilterComponent) {
		this.filterComponentByResponsible = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente de este filtro que permite filtrar por su tipo.
	 */
	public String getFilterComponentByItemType() {
		return this.filterComponentByItemType;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el componente de este filtro que permite filtrar por su
	 *            tipo.
	 */
	public void setFilterComponentByItemType(String aFilterComponent) {
		this.filterComponentByItemType = aFilterComponent;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente de este filtro que permite filtrar por su nodo de
	 *         workflow.
	 */
	public String getFilterComponentByNode() {
		return this.filterComponentByNode;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterComponent
	 *            es el componente de este filtro que permite filtrar por su
	 *            nodo de workflow.
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
	 * @return el componente de este filtro que permite buscar por texto.
	 */
	public String getFilterComponentByText() {
		return this.filterComponentByText;
	}

	/**
	 * Setter.
	 * 
	 * @param aText
	 *            es el componente de este filtro que permite buscar por texto.
	 */
	public void setFilterComponentByText(String aText) {
		this.filterComponentByText = aText;
	}

}
