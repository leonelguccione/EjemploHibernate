/** Este paquete contiene las implementaciones de la interface IDataProvider
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
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para retornar los dtos de los
 * proyectos para los listados. <br>
 * El listado puede ser paginado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ProjectDTODataProviderForUser extends
		SortableDataProvider<ProjectDTO> {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -5195955506393438116L;

	/**
	 * Mantiene una referencia al dto del usuario. Esta referencia puede ser
	 * nula.
	 */
	private UserDTO userDTO;

	/**
	 * Constructor.
	 * 
	 * @param anOrdering
	 *            es el orden que debe aplicarse al resultado.
	 * @param aColumnName
	 *            es el nombre de la propiedad por la que debe ordenarse el
	 *            resultado.
	 * @param anUserDTO
	 *            es el dto del usuario en la sesión.
	 */
	public ProjectDTODataProviderForUser(UserDTO anUserDTO, String aColumnName,
			String anOrdering) {
		this.setUserDTO(anUserDTO);
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
	public Iterator<ProjectDTO> iterator(int firstIndex, int count) {
		Collection<ProjectDTO> result;

		try {
			SortParam sp = this.getSort();
			String ordering = sp.isAscending() ? "ASC" : "DESC";

			result = this.getProjectsService().getProjects(firstIndex, count,
					sp.getProperty(), ordering, this.getUserDTO());

		} catch (Exception e) {
			result = new ArrayList<ProjectDTO>();
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
	public IModel<ProjectDTO> model(ProjectDTO aDto) {

		return new Model<ProjectDTO>(aDto);
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
			return this.getProjectsService()
					.getProjectsCount(this.getUserDTO());

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return el servicio que se debe utilizar para acceder a los servicios
	 *         relacionados con los proyectos.
	 */
	protected ProjectsServiceBI getProjectsService() {
		return ServiceLocator.getInstance().getProjectsService();
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del usuario en la sesión.
	 */
	public UserDTO getUserDTO() {
		return this.userDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anUserDTO
	 *            es el dto del usuario en la sesión.
	 */
	public void setUserDTO(UserDTO anUserDTO) {
		this.userDTO = anUserDTO;
	}

}
