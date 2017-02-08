/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.AddPriorityForm;
import zinbig.item.application.forms.DeletePriorityForm;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.PrioritiesServiceBI;

/**
 * Las instancias de este panel se utilizan para presentar la opción de agregado
 * de nuevas prioridades a un conjunto de prioridades que se está editando.<BR>
 * Bajo el formulario de alta de nuevas prioridades se presenta un listado que
 * contiene todas las prioridades ya agregadas al conjunto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ManagePrioritiesPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -4853036222398017056L;

	/**
	 * Mantiene la referencia al id del conjunto de prioridades que se está
	 * editando.
	 */
	public String prioritySetId;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este panel.
	 * @param aPrioritySetId
	 *            es el id del conjunto de prioridades que se está editando.
	 */
	public ManagePrioritiesPanel(String id, String aPrioritySetId) {
		super(id);
		this.setPrioritySetId(aPrioritySetId);

		// agrega el formulario para agregar nuevas prioridades.
		this.add(new AddPriorityForm("addPriorityForm", aPrioritySetId));

		// agrega el formulario para dar de baja las prioridades.
		DeletePriorityForm deleteForm = new DeletePriorityForm(
				"deletePrioritiesForm", aPrioritySetId);
		this.add(deleteForm);
		deleteForm.setOutputMarkupId(true);

		// agrega una etiqueta que se muestra cuando no hay elementos para
		// listar.
		Label aLabel = new Label("noResultsLabel", this.getString("NO_RESULTS"));
		this.add(aLabel);

		this.updateComponents(aPrioritySetId, deleteForm, aLabel);
	}

	/**
	 * Reemplaza el formulario que se utiliza para mostrar la lista de
	 * prioridades ya existentes.
	 */
	public void updateListOfPriorities() {
		// agrega el formulario para dar de baja las prioridades.
		DeletePriorityForm oldForm = (DeletePriorityForm) this
				.get("deletePrioritiesForm");
		DeletePriorityForm newForm = new DeletePriorityForm(
				"deletePrioritiesForm", this.getPrioritySetId());
		oldForm.replaceWith(newForm);

		Label aLabel = (Label) this.get("noResultsLabel");

		this.updateComponents(this.getPrioritySetId(), newForm, aLabel);
	}

	/**
	 * Getter.
	 * 
	 * @return el id del conjunto de prioridad que se está editando.
	 */
	public String getPrioritySetId() {
		return this.prioritySetId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id del conjunto de prioridades que se está editando.
	 */
	public void setPrioritySetId(String anId) {
		this.prioritySetId = anId;
	}

	/**
	 * Actualiza el estado del formulario de baja de prioridades y de la
	 * etiqueta que se muestra en caso de no hallar resultados.
	 * 
	 * @param aPrioritySetId
	 *            es el identificador del conjunto de prioridades que se está
	 *            editando.
	 * @param aForm
	 *            es el formulario que se debe esconder en caso de no hallar
	 *            prioridades.
	 * @param aLabel
	 *            es la etiqueta que se debe esconder en caso de que existan
	 *            prioridades.
	 */
	private void updateComponents(String aPrioritySetId,
			DeletePriorityForm aForm, Label aLabel) {
		PrioritiesServiceBI service = ServiceLocator.getInstance()
				.getPrioritiesService();
		int count = service.getPrioritiesCountOfPrioritySet(aPrioritySetId);
		if (count == 0) {
			aForm.setVisible(false);
			aLabel.setVisible(true);
		} else {
			aForm.setVisible(true);
			aLabel.setVisible(false);
		}
	}

}
