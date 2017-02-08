/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AddItemTypeForm;
import zinbig.item.application.forms.DeleteItemTypeForm;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de este panel se utilizan para presentar la opción de agregado
 * de nuevos tipos de ítems a un proyecto que se está editando.<BR>
 * Bajo el formulario de alta de nuevos tipos se presenta un listado que
 * contiene todos los tipos existentes.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ManageItemTypesPanel extends Panel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -709053935166814739L;

	/**
	 * Es el dto del proyecto que se está editando.
	 */
	protected ProjectDTO projectDTO;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param aProjectDTO
	 *            es el dto del proyecto que se está editando.
	 */
	public ManageItemTypesPanel(String id, ProjectDTO aProjectDTO) {
		super(id);

		this.setProjectDTO(aProjectDTO);
		// agrega el formulario para agregar nuevos tipos de items.
		this.add(new AddItemTypeForm("addItemTypeForm", aProjectDTO));

		// agrega el formulario para dar de baja los tipos de ítems.
		DeleteItemTypeForm deleteForm = new DeleteItemTypeForm(
				"deleteItemTypesForm", aProjectDTO);
		this.add(deleteForm);
		deleteForm.setOutputMarkupId(true);

		// agrega una etiqueta que se muestra cuando no hay elementos para
		// listar.
		Label aLabel = new Label("noResultsLabel", this.getString("NO_RESULTS"));
		this.add(aLabel);

		this.updateComponents(aLabel, deleteForm, aProjectDTO.getOid());
	}

	/**
	 * Reemplaza el formulario que se utiliza para mostrar la lista de tipos de
	 * ítems ya existentes.
	 */
	public void updateListOfItemTypes() {
		// agrega el formulario para dar de baja los tipos de ítems.
		DeleteItemTypeForm oldForm = (DeleteItemTypeForm) this
				.get("deleteItemTypesForm");
		DeleteItemTypeForm newForm = new DeleteItemTypeForm(
				"deleteItemTypesForm", this.getProjectDTO());

		oldForm.replaceWith(newForm);
		Label aLabel = (Label) this.get("noResultsLabel");
		this.updateComponents(aLabel, newForm, this.getProjectDTO().getOid());
	}

	/**
	 * Getter.
	 * 
	 * @return el dto del proyecto que se está editando.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aProjectDTO
	 *            el dto del proyecto que se está editando.
	 */
	public void setProjectDTO(ProjectDTO aProjectDTO) {
		this.projectDTO = aProjectDTO;
	}

	/**
	 * Actualiza la visibilidad del formulario de baja de tipos de ítems y de la
	 * etiqueta que muestra que no hubo resultados dependiendo si hay o no tipos
	 * de ítems para eliminar.
	 * 
	 * @param aLabel
	 *            es la etiqueta que se muestra cuando no hay resultados.
	 * @param aForm
	 *            es el formulario que se muestra cuando hay tipos de ítems para
	 *            borrar.
	 * @param aProjectOid
	 *            es el identificador del proyecto que se está editando.
	 */
	private void updateComponents(Label aLabel, DeleteItemTypeForm aForm,
			String aProjectOid) {

		ItemsServiceBI service = ServiceLocator.getInstance().getItemsService();
		int count = service.getItemTypesCountOfProject(aProjectOid);
		if (count == 0) {
			aForm.setVisible(false);
			aLabel.setVisible(true);
		} else {
			aForm.setVisible(true);
			aLabel.setVisible(false);
		}
	}

}
