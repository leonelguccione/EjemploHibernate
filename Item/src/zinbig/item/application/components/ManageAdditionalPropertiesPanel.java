/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AddAdditionalPropertyForm;
import zinbig.item.application.forms.DeleteAdditionalPropertyForm;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de este panel se utilizan para presentar la opción de agregado
 * de nuevas propiedades adicionales al proyecto. Estas propiedades adicionales
 * luego deberán ser completadas por los usuarios para cada uno de los ítems.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ManageAdditionalPropertiesPanel extends Panel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -826906370716150627L;

	/**
	 * Mantiene la referencia al proyecto que se está editando.
	 */
	public ProjectDTO projectDTO;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto que se está editando.
	 */
	public ManageAdditionalPropertiesPanel(String id, ProjectDTO aProjectDTO) {
		super(id);
		this.setProjectDTO(aProjectDTO);

		// agrega el formulario para agregar nuevas prioridades.
		this.add(new AddAdditionalPropertyForm("addAdditionalPropertyForm",
				aProjectDTO));

		// agrega el formulario para dar de baja las prioridades.
		DeleteAdditionalPropertyForm deleteForm = new DeleteAdditionalPropertyForm(
				"deleteAdditionalPropertyForm", aProjectDTO);
		this.add(deleteForm);
		deleteForm.setOutputMarkupId(true);

		// agrega una etiqueta que se muestra cuando no hay elementos para
		// listar.
		Label aLabel = new Label("noResultsLabel", this.getString("NO_RESULTS"));
		this.add(aLabel);

		this.updateComponents(aProjectDTO, deleteForm, aLabel);
	}

	/**
	 * Getter.
	 * 
	 * @return el DTO que representa al proyecto que se está editando.
	 */
	public ProjectDTO getProjectDTO() {
		return this.projectDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param aDTO
	 *            es el DTO que representa al proyecto que se está editando.
	 */
	public void setProjectDTO(ProjectDTO aDTO) {
		this.projectDTO = aDTO;
	}

	/**
	 * Reemplaza el formulario que se utiliza para mostrar la lista de
	 * propiedades ya existentes.
	 */
	public void updateListOfProperties() {
		// agrega el formulario para dar de baja las propiedades.
		DeleteAdditionalPropertyForm oldForm = (DeleteAdditionalPropertyForm) this
				.get("deleteAdditionalPropertyForm");
		DeleteAdditionalPropertyForm newForm = new DeleteAdditionalPropertyForm(
				"deleteAdditionalPropertyForm", this.getProjectDTO());
		oldForm.replaceWith(newForm);

		Label aLabel = (Label) this.get("noResultsLabel");

		this.updateComponents(this.getProjectDTO(), newForm, aLabel);
	}

	/**
	 * Actualiza el estado del formulario de baja de propiedades adicionales y
	 * de la etiqueta que se muestra en caso de no hallar resultados.
	 * 
	 * @param aProjectDTO
	 *            es el DTO que representa al proyecto que se está editando.
	 * @param aForm
	 *            es el formulario que se debe esconder en caso de no hallar
	 *            propiedades adicionales.
	 * @param aLabel
	 *            es la etiqueta que se debe esconder en caso de que existan
	 *            propiedades adicionales.
	 */
	private void updateComponents(ProjectDTO aProjectDTO,
			DeleteAdditionalPropertyForm aForm, Label aLabel) {

		ProjectsServiceBI service = ServiceLocator.getInstance()
				.getProjectsService();

		int count = service.getAdditionalPropertiesCountOfProject(aProjectDTO);
		if (count == 0) {
			aForm.setVisible(false);
			aLabel.setVisible(true);
		} else {
			aForm.setVisible(true);
			aLabel.setVisible(false);
		}
	}

}
