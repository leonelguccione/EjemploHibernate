/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.ItemsAdministrationForm;
import zinbig.item.application.pages.Pageable;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de este panel se utilizan para mostrar los resultados de las
 * ejecuciones de los filtros de �tems.<br>
 * Cada instancia recuerda el filtro que fue ejecutado, de modo de poder enviar
 * esta informaci�n tanto a la capa de servicios como al formulario que permite
 * editar el filtro.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ViewItemsPanel extends Panel {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -584600624970600370L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param aFilterDTO
	 *            es el dto que representa al filtro que se ejecut�.
	 * @param anUserDTO
	 *            es el dto que representa al usuario en la sesi�n. Este
	 *            par�metro puede ser nulo en caso de que est� listando un
	 *            usuario an�nimo.
	 * @param aProjectDTO
	 *            es el dto que representa al proyecto en la sesi�n. Este
	 *            par�metro podr�a ser nulo en caso de que no se haya ingresado
	 *            previamente en un proyecto.
	 * @param aPageable
	 *            es la p�gina en la cual se ha agregado este panel.
	 * @param aPanel
	 *            es el panel en donde se muestran ls solapas con los resultados
	 *            de los filtros.
	 */
	public ViewItemsPanel(String anId, FilterDTO aFilterDTO, UserDTO anUserDTO,
			ProjectDTO aProjectDTO, Pageable aPageable, AjaxTabbedPanel aPanel) {
		super(anId);

		// agrega el formulario que permite trabajar con m�ltiples �tems.
		ItemsAdministrationForm form = new ItemsAdministrationForm(
				"listItemsForm", aFilterDTO, anUserDTO, aProjectDTO, aPageable,
				aPanel);
		this.add(form);

	}

	/**
	 * Notifica al receptor que se ha cambiado la cantidad de elementos por
	 * p�gina.
	 * 
	 * @param aNumber
	 *            es el nuevo n�mero de elementos por p�gina.
	 */
	public void updateItemsPerPage(int aNumber) {

		ItemsAdministrationForm aForm = (ItemsAdministrationForm) this
				.get("listItemsForm");
		aForm.updateItemsPerPage(aNumber);
	}

	/**
	 * Actualiza el estado del filtro recibido.
	 * 
	 * @param aFilterDTO
	 *            es el dto que representa al filtro recibido.
	 */
	public void updateFilter(FilterDTO aFilterDTO) {
		((ItemsAdministrationForm) this.get("listItemsForm"))
				.updateFilter(aFilterDTO);

	}
}
