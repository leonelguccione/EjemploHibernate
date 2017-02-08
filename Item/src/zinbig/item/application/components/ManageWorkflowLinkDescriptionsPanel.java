/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AddWorkflowLinkDescriptionForm;
import zinbig.item.application.forms.DeleteWorkflowLinkDescriptionForm;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.WorkflowsServiceBI;

/**
 * Las instancias de este panel se utilizan para poder editar el workflow del
 * proyecto agregando o eliminando enlaces entre los distintos nodos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ManageWorkflowLinkDescriptionsPanel extends Panel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -1893511294640517454L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param anOid
	 *            es el oid del workflow que se está editando.
	 * @param aProjectOid
	 *            es el oid que representa al proyecto que se está editando.
	 *            Este parámetro podría ser null en caso de que se esté editando
	 *            un workflow del sistema.
	 * 
	 */
	public ManageWorkflowLinkDescriptionsPanel(String id, String anOid,
			String aProjectOid) {
		super(id);

		AddWorkflowLinkDescriptionForm addForm = new AddWorkflowLinkDescriptionForm(
				"addWorkflowLinkDescriptionForm", anOid, aProjectOid);
		this.add(addForm);

		// agrega el formulario para dar de baja las prioridades.
		DeleteWorkflowLinkDescriptionForm deleteForm = new DeleteWorkflowLinkDescriptionForm(
				"deleteWorkflowLinkDescriptionsForm", anOid, aProjectOid);
		this.add(deleteForm);
		deleteForm.setOutputMarkupId(true);

		// agrega una etiqueta para mostrar en caso de que no hayan resultados.
		Label aLabel = new Label("noResultsLabel", this.getString("NO_RESULTS"));
		this.add(aLabel);

		this.updateComponents(aLabel, deleteForm, anOid);

	}

	/**
	 * Reemplaza el formulario que se utiliza para mostrar la lista de
	 * descripciones de enlace ya existentes.
	 * 
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow que se está
	 *            editando.
	 * @param aProjectOid
	 *            es el identificador del proyecto que se está editando.
	 */
	public void updateListOfWorkflowLinkDescriptions(
			String aWorkflowDescriptionOid, String aProjectOid) {
		// agrega el formulario para dar de baja los enlaces.
		DeleteWorkflowLinkDescriptionForm oldForm = (DeleteWorkflowLinkDescriptionForm) this
				.get("deleteWorkflowLinkDescriptionsForm");
		DeleteWorkflowLinkDescriptionForm newForm = new DeleteWorkflowLinkDescriptionForm(
				"deleteWorkflowLinkDescriptionsForm", aWorkflowDescriptionOid,
				aProjectOid);

		oldForm.replaceWith(newForm);
		Label aLabel = (Label) this.get("noResultsLabel");
		this.updateComponents(aLabel, newForm, aWorkflowDescriptionOid);
	}

	/**
	 * Actualiza la visibilidad del formulario de baja de enlaces y de la
	 * etiqueta que muestra que no hubo resultados dependiendo si hay o no
	 * enlaces para eliminar.
	 * 
	 * @param aLabel
	 *            es la etiqueta que se muestra cuando no hay resultados.
	 * @param aForm
	 *            es el formulario que se muestra cuando hay enlaces para
	 *            borrar.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador del workflow que se está editando.
	 */
	private void updateComponents(Label aLabel,
			DeleteWorkflowLinkDescriptionForm aForm,
			String aWorkflowDescriptionOid) {

		WorkflowsServiceBI service = ServiceLocator.getInstance()
				.getWorkflowsService();
		int count = service
				.getWorkflowLinkDescriptionCountOfWorkflowDescription(aWorkflowDescriptionOid);
		if (count == 0) {
			aForm.setVisible(false);
			aLabel.setVisible(true);
		} else {
			aForm.setVisible(true);
			aLabel.setVisible(false);
		}
	}
}
