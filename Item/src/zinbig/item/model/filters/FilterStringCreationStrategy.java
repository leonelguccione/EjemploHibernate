/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Esta clase representa el tope de la jerarquía de estrategias de conversión de
 * los filtros de ítems a su forma expresada como Strings. Estos strings se
 * almacenan dentro del filtro para ser ejecutados de manera más eficiente que a
 * través del filtro en sí. El string se crea cuando se crea el filtro, siendo
 * actualizado posiblemente cuando se editen los componentes del filtro. Cada
 * estrategia en particular responde a un mapeador.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class FilterStringCreationStrategy {

	/**
	 * Crea el string para el filtro recibido.
	 * 
	 * @param aFilter
	 *            es el filtro para el cual debe ser creado el string. Dicho
	 *            string luego será asignado al filtro.
	 * @param aFilterComponentByProject
	 *            es el componente del filtro responsable de filtrar por
	 *            proyectos.
	 * @param aFilterComponentByState
	 *            es el componente del filtro responsable de filtrar por estado.
	 * @param aFilterComponentByItemId
	 *            es el componente del filtro responsable de filtrar por id.
	 * @param aFilterComponentByResponsible
	 *            es el componente del filtro que filtra por el responsable.
	 * @param aFilterComponentByItemType
	 *            es el componente del filtro que filtra por tipo.
	 * @param aFilterComponentByNode
	 *            es el componente del filtro que filtra por nodo de workflow.
	 * @param aFilterComponentByText
	 *            es el componente del filtro que filtra por un texto.
	 */
	public abstract void createFilterString(Filter aFilter,
			FilterComponentByProject aFilterComponentByProject,
			FilterComponentByState aFilterComponentByState,
			FilterComponentByItemId aFilterComponentByItemId,
			FilterComponentByResponsible aFilterComponentByResponsible,
			FilterComponentByItemType aFilterComponentByItemType,
			FilterComponentByNode aFilterComponentByNode,
			FilterComponentByText aFilterComponentByText);

	/**
	 * Crea el string correspondiente al componente de filtro de proyectos que
	 * toma en cuenta un proyecto en particular para armar el string.
	 * 
	 * @param selectedProjectOids
	 *            es una colección que contiene los oids seleccionados.
	 * @param negate
	 *            define si se debe negar la condición establecida por el
	 *            filtro.
	 * @return el string correspondiente para seleccionar solamente los
	 *         proyectos cuyo oid corresponda al oid del proyecto seleccionado.
	 */
	public abstract String getFilterStringForFilterComponentByProjectWithSelectedProject(
			String selectedProjectOids, boolean negate);

	/**
	 * Crea el string correspondiente al componente de filtro de proyectos que
	 * debe considerar los proyectos públicos y todos los proyectos privados en
	 * los que participa el usuario.
	 * 
	 * @return el string que permite seleccionar los ítems pertenecientes a los
	 *         proyectos públicos y/o privados del usuario.
	 */
	public abstract String getFilterStringForFilterComponentByProjectWithUser();

	/**
	 * Crea el string correspondiente al componente de filtro de proyectos que
	 * debe considerar los proyectos públicos.
	 * 
	 * @return el string que permite seleccionar los ítems pertenecientes a los
	 *         proyectos públicos.
	 */
	public abstract String getFilterStringForFilterComponentByProjectWithoutUser();

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * estado.
	 * 
	 * @param values
	 *            es una colección de estados por los que hay que filtrar los
	 *            ítems.
	 * @param negate
	 *            define si se debe negar o no la condición que se está creando.
	 * @return un string que permite filtrar los items por su estado.
	 */
	public abstract String getFilterStringForConcreteFilterComponentByState(
			Collection<String> values, boolean negate);

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * id.
	 * 
	 * @param aSelectedItemId
	 *            es el valor del filtro..
	 * @return un string que permite filtrar los items por su id.
	 */
	public abstract String getFilterStringForConcreteFilterComponentByItemId(
			String aSelectedItemId);

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * responsable.
	 * 
	 * @param someOids
	 *            es una colección que contiene los identificadores para filtrar
	 *            por el responsable.
	 * @param negate
	 *            define si se debe negar o no la condición que se está creando.
	 * @return un string que permite filtrar los items por sus responsables.
	 */
	public abstract String getFilterStringForConcreteFilterComponentByResponsible(
			Collection<String> someOids, boolean negate);

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * tipo.
	 * 
	 * @param someTitles
	 *            es una colección de títulos de tipos de ítems por los que se
	 *            debe filtrar.
	 * @param negate
	 *            define si se debe negar o no la condición que se está creando.
	 * @return un string que permite filtrar los items por su tipo.
	 */
	public abstract String getFilterStringForConcreteFilterComponentByItemType(
			Collection<String> someTitles, boolean negate);

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * nodo de workflow.
	 * 
	 * @param someTitles
	 *            es una colección de títulos de nodos de workflow por los que
	 *            se debe filtrar.
	 * @param negate
	 *            define si se debe negar o no la condición que se está creando.
	 * @return un string que permite filtrar los items por su nodo de workflow.
	 */
	public abstract String getFilterStringForConcreteFilterComponentByNode(
			Collection<String> someTitles, boolean negate);

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * texto.
	 * 
	 * @param aText
	 *            es el texto por el cual se debe filtrar los ítems.
	 * @return un string que permite filtrar los items por su texto.
	 */
	public abstract String getFilterStringForConcreteFilterComponentByText(
			String aText);
}
