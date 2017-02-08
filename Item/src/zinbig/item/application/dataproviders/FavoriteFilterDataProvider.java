/**
 * Este paquete contiene las implementaciones de la interface IDataProvider
 * que permiten acceder a la informaci�n de la base de datos (a trav�s de DTOs).<br>
 * Estas implementaciones son utilizadas por los componentes de la interfaz 
 * gr�fica.
 */
package zinbig.item.application.dataproviders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.ListDataProvider;

import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para acceder a los filtros favoritos
 * del usuario desde componentes de la interfaz gr�fica.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FavoriteFilterDataProvider extends ListDataProvider<FilterDTO> {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -2897574431015943421L;

	/**
	 * Es el DTO que representa al usuario.
	 */
	public UserDTO userDTO;

	/**
	 * Constructor.
	 * 
	 * @param aList
	 *            es la lista que se utiliza para acceder a los resultados.
	 */
	public FavoriteFilterDataProvider(List<FilterDTO> aList) {
		super(aList);
	}

	/**
	 * Constructor.
	 * 
	 * @param aList
	 *            es la lista que se utiliza para acceder a los resultados.
	 * @param anUserDTO
	 *            es el DTO que representa al usuario.
	 */
	public FavoriteFilterDataProvider(List<FilterDTO> aList, UserDTO anUserDTO) {
		super(aList);
		this.setUserDTO(anUserDTO);
	}

	/**
	 * Getter.
	 * 
	 * @return el DTO que representa al usuario.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserDTO
	 *            es el DTO que representa al usuario.
	 */
	public void setUserDTO(UserDTO anUserDTO) {
		this.userDTO = anUserDTO;
	}

	/**
	 * Accede a los resultados.
	 * 
	 * @param index
	 *            es el �ndice del primer elemento a devolver.
	 * @param count
	 *            es la cantidad de elementos a retornar.
	 */
	@Override
	public Iterator<FilterDTO> iterator(int index, int count) {
		List<FilterDTO> filters = new ArrayList<FilterDTO>();
		ItemsServiceBI service = ServiceLocator.getInstance().getItemsService();

		try {
			filters.addAll(service.findFavoritiesItemsFiltersOfUser(this
					.getUserDTO()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filters.iterator();
	}

}
