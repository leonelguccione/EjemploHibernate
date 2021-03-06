/**
 * Este paquete contiene las implementaciones de la interface IDataProvider
 * que permiten acceder a la informaci�n de la base de datos (a trav�s de DTOs).<br>
 * Estas implementaciones son utilizadas por los componentes de la interfaz 
 * gr�fica.
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
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ItemTypeDTO;

/**
 * Las instancias de esta clase se utilizan para retornar los dtos de los tipos
 * de �tems para los listados. <br>
 * El listado puede ser paginado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemTypeDTODataProvider extends SortableDataProvider<ItemTypeDTO> {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -318471446586083944L;

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
	public ItemTypeDTODataProvider(String aColumnName, String anOrdering) {
		this.setSort(aColumnName, anOrdering.equals("ASC"));
	}

	/**
	 * Itera sobre la colecci�n de dtos recuperados por esta instancia,
	 * recuperando solamente los elementos posicionados a partir del �ndice
	 * recibido y contando solo la cantidad especificada.
	 * 
	 * @param firstIndex
	 *            es el �ndice a partir del cual se deben devolver los
	 *            elementos.
	 * @param count
	 *            es la cantidad de elementos a ser devueltos como m�ximo.
	 */
	@Override
	public Iterator<ItemTypeDTO> iterator(int firstIndex, int count) {
		Collection<ItemTypeDTO> result = new ArrayList<ItemTypeDTO>();

		try {
			SortParam sp = this.getSort();
			String ordering = sp.isAscending() ? "ASC" : "DESC";

			result.addAll(this.getItemsService().getItemTypes(firstIndex,
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
	 * @return una implementaci�n de la interface IModel que decora al dto
	 *         recibido.
	 */
	@Override
	public IModel<ItemTypeDTO> model(ItemTypeDTO aDto) {

		return new Model<ItemTypeDTO>(aDto);
	}

	/**
	 * Getter.
	 * 
	 * @return el tama�o de la colecci�n de elementos recuperados por esta
	 *         clase.
	 */
	@Override
	public int size() {
		int result = 0;
		try {
			result = this.getItemsService().getItemTypesCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return el servicio que se debe utilizar para acceder a los servicios
	 *         relacionados con los �tems.
	 */
	protected ItemsServiceBI getItemsService() {
		return ServiceLocator.getInstance().getItemsService();
	}

}
