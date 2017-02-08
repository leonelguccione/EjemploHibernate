/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.ManageWorkflowNodeDescriptionsPanel;
import zinbig.item.application.pages.EditWorkflowNodeDescriptionPage;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de este formulario se utilizan para listar y seleccionar las
 * descripciones de nodo y eliminarlas. Solamente se pueden eliminar aquellas
 * descripciones de nodos que no contengan en este momento un ítem en un nodo
 * propio.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DeleteWorkflowNodeDescriptionsForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -8089088914898826692L;

	/**
	 * Es una colección que contiene los oids de las descripciones de nodos
	 * seleccionadas.
	 */
	public Collection<String> selectedWorkflowNodeDescriptions;

	/**
	 * Es el oid de la descripción de workflow que se está editando.
	 */
	public String workflowDescriptionOid;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aWorkflowDescriptionOid
	 *            es el oid de la descripción de workflow que se está editando.
	 * @param aProjectOid
	 *            es el oid del proyecto que se está editando.
	 */
	@SuppressWarnings("unchecked")
	public DeleteWorkflowNodeDescriptionsForm(String anId,
			final String aWorkflowDescriptionOid, final String aProjectOid) {
		super(anId);

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages
				.add(this
						.getString("DeleteWorkflowNodeDescriptionsForm.noWorkflowNodeDescriptionSelected"));
		messages
				.add(this
						.getString("DeleteWorkflowNodeDescriptionsForm.errorDeletingNodes"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedWorkflowNodeDescriptions(new ArrayList<String>());
		this.setWorkflowDescriptionOid(aWorkflowDescriptionOid);

		// crea el componente para el listado de las prioridades existentes
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedWorkflowNodeDescriptions());
		group.add(new CheckGroupSelector("groupselector"));

		final SortableDataProvider<WorkflowNodeDescriptionDTO> provider = new SortableDataProvider<WorkflowNodeDescriptionDTO>() {

			List<WorkflowNodeDescriptionDTO> nodes = getWorkflowNodeDescriptions();

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Getter.
			 * 
			 * @param index
			 *            es un entero que indica a partir de que posición se
			 *            debe iterar.
			 * @param count
			 *            es un entero que indica la cantidad de posiciones que
			 *            se deben iterar.
			 */
			@Override
			public Iterator<WorkflowNodeDescriptionDTO> iterator(int index,
					int count) {
				return nodes.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aWorkflowNodeDescriptionDTO
			 *            es el dto que representa a una de las descripciones de
			 *            nodo que se está iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<WorkflowNodeDescriptionDTO> model(
					WorkflowNodeDescriptionDTO aWorkflowNodeDescriptionDTO) {
				return new Model<WorkflowNodeDescriptionDTO>(
						aWorkflowNodeDescriptionDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return nodes.size();
			}
		};

		// crea el componente para listar las descripciones de nodos.
		final DataView workflowNodeDescriptions = new DataView("pageable",
				provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el título de la descripción, si es final
			 * y un número que indica la cantidad de elementos que referencian a
			 * esta descripción.
			 */
			@Override
			protected void populateItem(Item item) {
				WorkflowNodeDescriptionDTO dto = (WorkflowNodeDescriptionDTO) item
						.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);
				check.setEnabled(dto.getReferencesCount() == 0);

				Label aLabel = new Label("title", dto.getTitle());

				PageParameters params = new PageParameters();
				params.put("NODE_OID", dto.getOid());
				params.put("projectOID", aProjectOid);
				params.put("workflowDescriptionOid", aWorkflowDescriptionOid);
				Link editLink = new BookmarkablePageLink("editLink",
						EditWorkflowNodeDescriptionPage.class, params);
				editLink.add(aLabel);
				item.add(editLink);

				Label finalLabel = new Label("finalNode", this
						.getString(new Boolean(dto.isFinalNode()).toString()));
				item.add(finalLabel);

				Label referencesLabel = new Label("referencesCount", new Long(
						dto.getReferencesCount()).toString());
				item.add(referencesLabel);

			}

		};

		group.add(workflowNodeDescriptions);
		this.add(group);

		// construye el link de borrado de las definiciones de workflow.
		SubmitLink deleteLink = new SubmitLink(
				"deleteWorkflowNodeDescriptionsLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Borra las prioridades seleccionadas.
			 */
			@Override
			public void onSubmit() {

				if (!DeleteWorkflowNodeDescriptionsForm.this
						.getSelectedWorkflowNodeDescriptions().isEmpty()) {
					try {
						DeleteWorkflowNodeDescriptionsForm.this
								.getWorkflowsService()
								.deleteWorkflowNodeDescriptions(
										DeleteWorkflowNodeDescriptionsForm.this
												.getWorkflowDescriptionOid(),
										DeleteWorkflowNodeDescriptionsForm.this
												.getSelectedWorkflowNodeDescriptions());

						// actualiza el panel que contiene este formulario
						((ManageWorkflowNodeDescriptionsPanel) DeleteWorkflowNodeDescriptionsForm.this
								.getParent())
								.updateListOfWorkflowLinkDescriptions(
										aWorkflowDescriptionOid, aProjectOid);

					} catch (Exception e) {
						this
								.error(this
										.getString("DeleteWorkflowNodeDescriptionsForm.errorDeletingNodes"));

					}

				} else {
					this
							.error(this
									.getString("DeleteWorkflowNodeDescriptionsForm.noWorkflowNodeDescriptionSelected"));
				}
			}

		};
		this.add(deleteLink);
		group.setVisible(workflowNodeDescriptions.getRowCount() != 0);

		deleteLink
				.setVisible(workflowNodeDescriptions.getRowCount() != 0
						&& this
								.verifyPermissionAssigmentToUser("DELETE_WORKFLOW_NODE_DESCRIPTIONS"));
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los identificadores de todos las
	 *         descripciones de nodos seleccionadas.
	 */
	public Collection<String> getSelectedWorkflowNodeDescriptions() {
		return this.selectedWorkflowNodeDescriptions;
	}

	/**
	 * Setter.
	 * 
	 * @param someSelectedWorkflowNodeDescriptions
	 *            es una colección que contiene los identificadores de todos las
	 *            descripciones de nodos seleccionadas.
	 */
	public void setSelectedWorkflowNodeDescriptions(
			Collection<String> someSelectedWorkflowNodeDescriptions) {
		this.selectedWorkflowNodeDescriptions = someSelectedWorkflowNodeDescriptions;
	}

	/**
	 * Recupera la colección de descripciones de nodos correspondientes a esta
	 * descripción de workflow.
	 * 
	 * @return una colección que contiene DTOs para cada una de las
	 *         desripciones.
	 */
	private List<WorkflowNodeDescriptionDTO> getWorkflowNodeDescriptions() {
		ArrayList<WorkflowNodeDescriptionDTO> result = new ArrayList<WorkflowNodeDescriptionDTO>();

		WorkflowsServiceBI service = this.getWorkflowsService();

		try {
			result.addAll(service
					.getAllWorkflowNodeDescriptionOfWorkflowDescription(this
							.getWorkflowDescriptionOid()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return el oid de la descripción de workflow que se está editando.
	 */
	public String getWorkflowDescriptionOid() {
		return this.workflowDescriptionOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid de la descripción de workflow que se está editando.
	 */
	public void setWorkflowDescriptionOid(String anOid) {
		this.workflowDescriptionOid = anOid;
	}

}
