/**
 * Este paquete contiene las implementaciones de la interface IDataProvider
 * que permiten acceder a la información de la base de datos (a través de DTOs).<br>
 * Estas implementaciones son utilizadas por los componentes de la interfaz 
 * gráfica.
 */
package zinbig.item.application.dataproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.dto.UserGroupDTO;

/**
 * Las instancias de esta clase se utilizan para retornar los dtos de los grupos
 * de usuarios para los listados. <br>
 * El listado puede ser paginado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class UserGroupDTODataProvider extends
		SortableDataProvider<UserGroupDTO> {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -1848297563199022641L;

	/**
	 * Constructor.
	 * 
	 * @param anOrdering
	 *            es el orden que debe aplicarse al resultado.
	 * @param aColumnName
	 *            es el nombre de la propiedad por la que debe ordenarse el
	 *            resultado.
	 * 
	 */
	public UserGroupDTODataProvider(String aColumnName, String anOrdering) {
		this.setSort(aColumnName, anOrdering.equals("ASC"));
	}

	/**
	 * Itera sobre la colección de dtos recuperados por esta instancia,
	 * recuperando solamente los elementos posicionados a partir del índice
	 * recibido y contando solo la cantidad especificada.
	 * 
	 * @param firstIndex
	 *            es el índice a partir del cual se deben devolver los
	 *            elementos.
	 * @param count
	 *            es la cantidad de elementos a ser devueltos como máximo.
	 */
	@Override
	public Iterator<UserGroupDTO> iterator(int firstIndex, int count) {
		Collection<UserGroupDTO> result = new ArrayList<UserGroupDTO>();

		try {
			SortParam sp = this.getSort();
			String ordering = sp.isAscending() ? "ASC" : "DESC";

			result.addAll(this.getUsersService().getUserGroups(firstIndex,
					count, sp.getProperty(), ordering));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.iterator();
	}

	/**
	 * Genera un decorador de Wicket para cada uno de los elementos recuperados.
	 * 
	 * @param aDto
	 *            es el dto que debe ser decorado.
	 * @return una implementación de la interface IModel que decora al dto
	 *         recibido.
	 */
	@Override
	public IModel<UserGroupDTO> model(UserGroupDTO aDto) {

		return new Model<UserGroupDTO>(aDto);
	}

	/**
	 * Getter.
	 * 
	 * @return el tamaño de la colección de elementos recuperados por esta
	 *         clase.
	 */
	@Override
	public int size() {
		int result = 0;
		try {
			result = this.getUsersService().getUserGroupsCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return el servicio que se debe utilizar para acceder a los servicios
	 *         relacionados con los usuarios.
	 */
	protected UsersServiceBI getUsersService() {
		return ServiceLocator.getInstance().getUsersService();
	}

}
