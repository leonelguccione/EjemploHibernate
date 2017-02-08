/**
 * Este paquete contiene clases e interfaces útiles para la creación de filtros
 * para los listados de ítems.
 */
package zinbig.item.model.filters;

import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para convertir a string los
 * diferentes componentes de los filtros. Este string se utiliza para acceder en
 * forma más óptima a los ítems. En particular esta clase hace conversiones
 * relacionadas con Hibernate y HQL.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernateFilterStringCreationStrategy extends
		FilterStringCreationStrategy {

	/**
	 * Crea el string para el filtro recibido.
	 * 
	 * @param aFilter
	 *            es el filtro que debe ser convertido en un string.
	 * @param aFilterComponentByProject
	 *            es el componente del filtro responsable de filtrar por
	 *            proyectos.
	 * @param aFilterComponentByState
	 *            es el componente del filtro responsable de filtrar por estado.
	 * @param aFilterComponentByItemId
	 *            es el componente del filtro responsable de filtrar por id.
	 * @param aFilterComponentByResponsible
	 *            es el componente del filtro que filtra por responsable.
	 * @param aFilterComponentByItemType
	 *            es el componente del filtro que filtra por tipo.
	 * @param aFilterComponentByNode
	 *            es el componente del filtro que filtra por nodo de workflow.
	 * @param aFilterComponentByText
	 *            es el componente del filtro que filtra por un texto.
	 */
	@Override
	public void createFilterString(Filter aFilter,
			FilterComponentByProject aFilterComponentByProject,
			FilterComponentByState aFilterComponentByState,
			FilterComponentByItemId aFilterComponentByItemId,
			FilterComponentByResponsible aFilterComponentByResponsible,
			FilterComponentByItemType aFilterComponentByItemType,
			FilterComponentByNode aFilterComponentByNode,
			FilterComponentByText aFilterComponentByText) {

		StringBuffer aFilterString = new StringBuffer("");

		aFilterString.append(" WHERE ");

		// crea el string para el componente del filtro por proyecto.
		aFilterString.append(aFilterComponentByProject.getFilterString(this,
				aFilter));

		// crea el string para el componente del filtro por estado.
		aFilterString.append(aFilterComponentByState.getFilterString(this,
				aFilter));

		// crea el string para el componente del filtro por id.
		aFilterString.append(aFilterComponentByItemId.getFilterString(this,
				aFilter));

		// crea el string para el componente del filtro por responsable.
		aFilterString.append(aFilterComponentByResponsible.getFilterString(
				this, aFilter));

		// crea el string para el componente del filtro por tipo.
		aFilterString.append(aFilterComponentByItemType.getFilterString(this,
				aFilter));

		// crea el string para el componente del filtro por nodo de workflow.
		aFilterString.append(aFilterComponentByNode.getFilterString(this,
				aFilter));

		// crea el string para el componente del filtro por un texto.
		aFilterString.append(aFilterComponentByText.getFilterString(this,
				aFilter));

		aFilter.setFilterString(aFilterString.toString());

	}

	/**
	 * Crea el string correspondiente al componente de filtro de proyectos que
	 * toma en cuenta un proyecto en particular para armar el string.
	 * 
	 * @param selectedProjectOids
	 *            es una colección que contiene los oids de los proyectos
	 *            seleccionados por el usuario.
	 * @param negate
	 *            define si se debe negar o no la condición de este filtro.
	 * @return el string HQL correspondiente para seleccionar solamente los
	 *         proyectos cuyo oid corresponda al oid del proyecto seleccionado.
	 */
	@Override
	public String getFilterStringForFilterComponentByProjectWithSelectedProject(
			String selectedProjectOids, boolean negate) {

		String aux = "";
		if (negate) {
			aux = " NOT ";
		}

		return "(i.project.oid " + aux + " in " + selectedProjectOids + ")";
	}

	/**
	 * Crea el string correspondiente al componente de filtro de proyectos que
	 * debe considerar los proyectos públicos y todos los proyectos privados en
	 * los que participa el usuario.
	 * 
	 * @return el string HQL que permite seleccionar los ítems pertenecientes a
	 *         los proyectos públicos y/o privados del usuario.
	 */
	@Override
	public String getFilterStringForFilterComponentByProjectWithUser() {

		return "(i.project.publicProject=true or i.project.oid in $projects)";
	}

	/**
	 * Crea el string correspondiente al componente de filtro de proyectos que
	 * debe considerar los proyectos públicos.
	 * 
	 * @return el string HQL que permite seleccionar los ítems pertenecientes a
	 *         los proyectos públicos.
	 */
	@Override
	public String getFilterStringForFilterComponentByProjectWithoutUser() {
		return "(i.project.publicProject=true)";
	}

	/**
	 * Crea el string correspondiente al componente del filtro que filtra los
	 * items por su estado.
	 * 
	 * @param values
	 *            es una colección de estados por los que hay que filtrar los
	 *            ítems.
	 * @param negate
	 *            define si se debe negar o no la condición que se está creando.
	 * @return el string HQL que permite filtrar los items por su estado.
	 */
	@Override
	public String getFilterStringForConcreteFilterComponentByState(
			Collection<String> values, boolean negate) {

		String aux = "";
		if (negate) {
			aux = " NOT ";
		}

		return " AND (i.state" + aux + " in "
				+ values.toString().replace("[", "(").replace("]", "") + "))";
	}

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * id.
	 * 
	 * @param aSelectedItemId
	 *            es el valor del filtro de estados.
	 * @return un string que permite filtrar los items por su id.
	 */
	public String getFilterStringForConcreteFilterComponentByItemId(
			String aSelectedItemId) {
		return " AND (i.itemId='" + aSelectedItemId + "')";
	}

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
	@Override
	public String getFilterStringForConcreteFilterComponentByResponsible(
			Collection<String> someOids, boolean negate) {
		String aux = "";
		if (negate) {
			aux = " NOT ";
		}
		return " AND (i.responsible.id" + aux + " in "
				+ someOids.toString().replace("[", "(").replace("]", "") + "))";
	}

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * tipo.
	 * 
	 * @param someTitles
	 *            es una colección de títulos de tipos de ítems por los que se
	 *            debe filtrar.
	 * @param negate
	 *            define si se debe negar o no la condición que se está creando.
	 * @return un string que permite filtrar los items por su responsable.
	 */
	@Override
	public String getFilterStringForConcreteFilterComponentByItemType(
			Collection<String> someTitles, boolean negate) {
		String aux = "";
		if (negate) {
			aux = " NOT ";
		}
		return " AND (i.itemType.title" + aux + " in "
				+ someTitles.toString().replace("[", "(").replace("]", ")")
				+ ")";
	}

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
	public String getFilterStringForConcreteFilterComponentByNode(
			Collection<String> someTitles, boolean negate) {
		String aux = "";
		if (negate) {
			aux = " NOT ";
		}
		return " AND (i.currentWorkflowNode.title" + aux + " in "
				+ someTitles.toString().replace("[", "(").replace("]", ")")
				+ ")";

	}

	/**
	 * Crea el string correspondiente al componente de filtro de items por su
	 * texto.
	 * 
	 * @param aText
	 *            es el texto por el cual se deben filtrar los ítems.
	 * @return un string que permite filtrar los items por su texto.
	 */
	public String getFilterStringForConcreteFilterComponentByText(String aText) {

		return " AND (i.title like '%" + aText + "%' OR i.description like '%"
				+ aText + "%'" + ")";

	}

}
