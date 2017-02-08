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
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para retornar los dtos de los �tems
 * para los listados. <br>
 * El listado puede ser paginado.<br>
 * Si el dto que representa al usuario no es null entonces se listan los �tems
 * de todos los proyectos a los que est� asociado el usuario (incluyendo los
 * proyectos p�blicos). En caso de que sea nulo solamente se muestran los �tems
 * pertenecientes a los proyectos p�blicos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemDTODataProvider extends SortableDataProvider<ItemDTO> {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 2273914353200595730L;

	/**
	 * Es el dto del usuario en la sesi�n.
	 */
	protected UserDTO userDTO;

	/**
	 * Es el dto del proyecto en la sesi�n.
	 */
	protected ProjectDTO projectDTO;

	/**
	 * Es el dto que representa al filtro que se debe aplicar a los �tems.
	 */
	protected FilterDTO filterDTO;

	/**
	 * Constructor.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario en la sesi�n.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto en la sesi�n.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que se debe aplicar a los
	 *            �tems.
	 * @param anOrdering
	 *            es el orden que debe aplicarse al resultado.
	 * @param aColumnName
	 *            es el nombre de la propiedad por la que debe ordenarse el
	 */
	public ItemDTODataProvider(UserDTO anUserDTO, ProjectDTO aProjectDTO,
			FilterDTO aFilterDTO, String aColumnName, String anOrdering) {
		this.setUserDTO(anUserDTO);
		this.setFilterDTO(aFilterDTO);
		this.setProjectDTO(aProjectDTO);
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
	public Iterator<ItemDTO> iterator(int firstIndex, int count) {
		Collection<ItemDTO> result = new ArrayList<ItemDTO>();

		try {
			SortParam sp = this.getSort();
			String ordering = sp.isAscending() ? "ASC" : "DESC";
			result.addAll(this.getItemsService().getItems(this.getUserDTO(),
					this.getProjectDTO(), this.getFilterDTO(), firstIndex,
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
	public IModel<ItemDTO> model(ItemDTO aDto) {

		return new Model<ItemDTO>(aDto);
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
			result = this.getItemsService().getItemsCount(this.getUserDTO(),
					this.getProjectDTO(), this.getFilterDTO());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * M�todo presente por compatibilidad. <br>
	 * No contiene l�gica.
	 */
	@Override
	public void detach() {

	}

	/**
	 * Getter.
	 * 
	 * @return el dto del usuario que ingres� en la aplicaci�n.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserDTO
	 *            es el dto que representa al usuario en la sesi�n.
	 */
	public void setUserDTO(UserDTO anUserDTO) {
		this.userDTO = anUserDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al filtro que se debe aplicar a los �tems.
	 */
	public FilterDTO getFilterDTO() {
		return this.filterDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que se debe aplicar a los
	 *            �tems.
	 */
	public void setFilterDTO(FilterDTO aFilterDTO) {
		this.filterDTO = aFilterDTO;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al proyecto en la sesi�n.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            es el dto del proyecto en la sesi�n.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
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
