/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicaci�n desarrollados espec�ficamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import zinbig.item.application.forms.MoveItemInWorkflowForm;
import zinbig.item.application.pages.ViewItemDetailPage;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.WorkflowNodeDTO;

/**
 * Las instancias de este panel se utilizan para poder mover un �tem a trav�s
 * del workflow definido por el proyecto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class MoveItemInWorkflowPanel extends Panel {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 8961536659461520363L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 * @param anItemDTO
	 *            es el dto que representa al item que se est� moviendo en el
	 *            workflow.
	 * @param aFilterOID
	 *            es el oid del filtro que se aplic�.
	 */
	public MoveItemInWorkflowPanel(String anId, ItemDTO anItemDTO,
			String aFilterOID) {
		super(anId);

		MoveItemInWorkflowForm aForm = new MoveItemInWorkflowForm(
				"moveItemInWorkflowForm", anItemDTO, aFilterOID);
		aForm.setOutputMarkupId(true);
		this.add(aForm);
		this.setOutputMarkupId(true);

		// crea el componente para listar los estados anteriores.
		this.createOldWorkflowNodesComponent(anItemDTO);
	}

	/**
	 * Actualiza la versi�n del �tem que se est� editando.<BR>
	 * Esta actualziaci�n se debe a que el formulario que contiene este panel ha
	 * actualizado la informaci�n del �tem y por lo tanto se cuenta con una
	 * nueva versi�n del �tem.
	 * 
	 * @param newItemDTO
	 *            es el nuevo dto que representa al �tem.
	 */
	public void updateItemVersion(ItemDTO newItemDTO) {
		((ViewItemDetailPage) this.getPage()).setItemDTO(newItemDTO);

	}

	/**
	 * Crea un componente que permite listar todos los estados anteriores del
	 * �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que se est� mostrando.
	 */
	private void createOldWorkflowNodesComponent(final ItemDTO anItemDTO) {
		// crea el componente para el listado de los estados anteriores
		// existentes

		final List<WorkflowNodeDTO> oldWorkflowNodes = this
				.getOldWorkflowNodes(anItemDTO);

		final SortableDataProvider<WorkflowNodeDTO> provider = new SortableDataProvider<WorkflowNodeDTO>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Getter.
			 * 
			 * @param index
			 *            es un entero que indica a partir de que posici�n se
			 *            debe iterar.
			 * @param count
			 *            es un entero que indica la cantidad de posiciones que
			 *            se deben iterar.
			 */
			@Override
			public Iterator<WorkflowNodeDTO> iterator(int index, int count) {
				return oldWorkflowNodes.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aWorkflowNodeDTO
			 *            es el dto que representa a uno de los estados
			 *            anteriores que se est� iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<WorkflowNodeDTO> model(
					WorkflowNodeDTO aWorkflowNodeDTO) {
				return new Model<WorkflowNodeDTO>(aWorkflowNodeDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return oldWorkflowNodes.size();
			}
		};

		// crea el componente para listar los estados anteriores.
		final DataView<WorkflowNodeDTO> nodes = new DataView<WorkflowNodeDTO>(
				"oldNodesPageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada l�nea del listado, conteniendo un check para
			 * seleccionar el elemento, el comentario.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(Item item) {
				WorkflowNodeDTO dto = (WorkflowNodeDTO) item.getModelObject();

				Label aLabel = new Label("title", dto.getTitle());
				item.add(aLabel);

				Label responsibleLabel = new Label("responsible", dto
						.getResponsibleAlias());
				item.add(responsibleLabel);

				Label creationDateLabel = new Label("creationDate", dto
						.getCreationDate());
				item.add(creationDateLabel);

			}

		};

		this.add(nodes);

		// si no hay estados anteriores no se muestra este componente.
		if (oldWorkflowNodes.isEmpty()) {
			nodes.setVisible(false);
		}
	}

	/**
	 * Recupera la colecci�n de estados anteriores corespondientes al �tem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que se est� mostrando.
	 * 
	 * @return una colecci�n que contiene DTOs para cada uno de los estados
	 *         anteriores.
	 */
	private List<WorkflowNodeDTO> getOldWorkflowNodes(ItemDTO anItemDTO) {
		ArrayList<WorkflowNodeDTO> result = new ArrayList<WorkflowNodeDTO>();
		TreeSet<WorkflowNodeDTO> aux = new TreeSet<WorkflowNodeDTO>(
				new Comparator<WorkflowNodeDTO>() {
					DateFormat formatter = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss");

					@Override
					public int compare(WorkflowNodeDTO wn1, WorkflowNodeDTO wn2) {
						int result = 0;
						try {
							result = -1
									* formatter
											.parse(wn1.getCreationDate())
											.compareTo(
													formatter.parse(wn2
															.getCreationDate()));
						} catch (ParseException e) {
							e.printStackTrace();
						}

						return result;
					}
				});
		try {
			WorkflowsServiceBI aService = ServiceLocator.getInstance()
					.getWorkflowsService();
			aux.addAll(aService.getOldWorkflowNodesOfItem(anItemDTO));
			aux.add(anItemDTO.getCurrentNode());
			result.addAll(aux);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
