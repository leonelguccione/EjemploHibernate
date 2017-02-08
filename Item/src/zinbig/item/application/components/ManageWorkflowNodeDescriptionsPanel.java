/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AddWorkflowNodeDescriptionForm;
import zinbig.item.application.forms.DeleteWorkflowNodeDescriptionsForm;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.WorkflowsServiceBI;

/**
 * Las instancias de este panel se utilizan para poder editar el workflow del
 * proyecto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ManageWorkflowNodeDescriptionsPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -3760270068151215365L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param anOid
	 *            es el oid del workflow que se está editando.
	 * @param aProjectOid
	 *            es el oid del proyecto que se está editando. Este parámetro
	 *            podría ser nulo.
	 */
	public ManageWorkflowNodeDescriptionsPanel(String id, String anOid,
			String aProjectOid) {
		super(id);

		AddWorkflowNodeDescriptionForm addForm = new AddWorkflowNodeDescriptionForm(
				"addWorkflowNodeDescriptionForm", anOid, aProjectOid);
		this.add(addForm);

		DeleteWorkflowNodeDescriptionsForm deleteForm = new DeleteWorkflowNodeDescriptionsForm(
				"deleteWorkflowNodeDescriptionsForm", anOid, aProjectOid);
		this.add(deleteForm);

		// agrega una etiqueta para mostrar en caso de que no hayan resultados.
		Label aLabel = new Label("noResultsLabel", this.getString("NO_RESULTS"));
		this.add(aLabel);

		this.updateComponents(aLabel, deleteForm, anOid);

	}

	/**
	 * Reemplaza el formulario que se utiliza para mostrar la lista de
	 * descripciones de nodos ya existentes.
	 * 
	 * @param aWorkflowDescriptionOid
	 *            es el identificador de la descripción de workflow que se está
	 *            editando.
	 * @param aProjectOid
	 *            es el identificador del proyecto que se está editando.
	 */
	public void updateListOfWorkflowLinkDescriptions(
			String aWorkflowDescriptionOid, String aProjectOid) {
		// agrega el formulario para dar de baja los nodos.
		DeleteWorkflowNodeDescriptionsForm oldForm = (DeleteWorkflowNodeDescriptionsForm) this
				.get("deleteWorkflowNodeDescriptionsForm");
		DeleteWorkflowNodeDescriptionsForm newForm = new DeleteWorkflowNodeDescriptionsForm(
				"deleteWorkflowNodeDescriptionsForm", aWorkflowDescriptionOid,
				aProjectOid);

		oldForm.replaceWith(newForm);
		Label aLabel = (Label) this.get("noResultsLabel");
		this.updateComponents(aLabel, newForm, aWorkflowDescriptionOid);
	}

	/**
	 * Actualiza la visibilidad del formulario de baja de nodos y de la etiqueta
	 * que muestra que no hubo resultados dependiendo si hay o no nodos para
	 * eliminar.
	 * 
	 * @param aLabel
	 *            es la etiqueta que se muestra cuando no hay resultados.
	 * @param aForm
	 *            es el formulario que se muestra cuando hay nodos para borrar.
	 * @param aWorkflowDescriptionOid
	 *            es el identificador del workflow que se está editando.
	 */
	private void updateComponents(Label aLabel,
			DeleteWorkflowNodeDescriptionsForm aForm,
			String aWorkflowDescriptionOid) {

		WorkflowsServiceBI service = ServiceLocator.getInstance()
				.getWorkflowsService();
		int count = service
				.getWorkflowNodeDescriptionCountOfWorkflowDescription(aWorkflowDescriptionOid);
		if (count == 0) {
			aForm.setVisible(false);
			aLabel.setVisible(true);
		} else {
			aForm.setVisible(true);
			aLabel.setVisible(false);
		}
	}

}
