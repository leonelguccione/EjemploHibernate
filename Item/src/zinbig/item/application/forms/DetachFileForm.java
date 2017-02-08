/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.FeedbackPanelFilter;
import zinbig.item.application.components.ItemAttachedFilesPanel;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemFileDTO;

/**
 * Las instancias de esta clase se utilizan para eliminar archivos adjuntos de
 * un ítem que se está editando.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DetachFileForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 1474018911013121900L;

	/**
	 * Es una coelcción que mantiene los identificadores de los archivos
	 * adjuntos seleccionados.
	 */
	protected Collection<String> selectedAttachedFiles;

	/**
	 * Es el DTO que representa al ítem que se está editando.
	 */
	protected ItemDTO itemDTO;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param anItemDTO
	 *            es el dto que representa la item que se está comentando.
	 */
	@SuppressWarnings("unchecked")
	public DetachFileForm(String anId, ItemDTO anItemDTO) {
		super(anId);

		this.setItemDTO(anItemDTO);
		this.setSelectedAttachedFiles(new ArrayList<String>());

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages.add(this.getString("AttachFileForm.noFilesSelected"));
		messages.add(this
				.getString("AttachFileFormForm.errorDeletingAttachedFiles"));

		// crea el componente para mostrar errores relacionados con los archivos
		// seleccionados
		FeedbackPanel feedback = new FeedbackPanel("feedback");

		IFeedbackMessageFilter filter = new FeedbackPanelFilter(messages);
		feedback.setFilter(filter);
		this.add(feedback);

		// valida que el usuario actual pueda borrar los archivos
		// adjuntos.
		boolean mayEdit = this.getUserDTO() != null
				&& (this.getUserDTO().equals(anItemDTO.getResponsible()) && this
						.verifyPermissionAssigmentToUser("DETACH_FILES"));

		// crea el componente para el listado de los archivos adjuntos
		// existentes.
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedAttachedFiles());
		group.add(new CheckGroupSelector("groupselector"));

		final SortableDataProvider<ItemFileDTO> provider = new SortableDataProvider<ItemFileDTO>() {

			List<ItemFileDTO> files = getAttachedFiles();

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
			public Iterator<ItemFileDTO> iterator(int index, int count) {
				return files.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param anItemFileDTO
			 *            es el dto que representa a uno de los archivos
			 *            adjuntos que se está iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<ItemFileDTO> model(ItemFileDTO anItemFileDTO) {
				return new Model<ItemFileDTO>(anItemFileDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return files.size();
			}
		};

		// crea el componente para listar los archivos adjuntos.
		final DataView attachedFiles = new DataView("pageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento.
			 */
			@Override
			protected void populateItem(Item item) {
				ItemFileDTO dto = (ItemFileDTO) item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				Label aLabel = new Label("filename", dto.getFilename());
				item.add(aLabel);

				Label dateLabel = new Label("creationDate", dto
						.getCreationDate());
				item.add(dateLabel);

			}

		};

		group.add(attachedFiles);
		this.add(group);

		// construye el link de borrado de archivos adjuntos.
		SubmitLink deleteLink = new SubmitLink("deleteAttachedFilesLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Borra los archivos adjuntos seleccionados.
			 */
			@Override
			public void onSubmit() {

				if (!DetachFileForm.this.getSelectedAttachedFiles().isEmpty()) {
					try {
						ItemDTO newItemDTO = DetachFileForm.this
								.getItemsService().detachFilesFromItem(
										getItemDTO(),
										getSelectedAttachedFiles());

						((ItemAttachedFilesPanel) DetachFileForm.this
								.getParent()).updateItemVersion(newItemDTO);

					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("AttachFileFormForm.errorDeletingAttachedFiles"));
					}

				} else {
					this
							.error(this
									.getString("AttachFileForm.noFilesSelected"));
				}
			}

		};
		this.add(deleteLink);

		group.setVisible(attachedFiles.getRowCount() != 0);
		this.setVisible(attachedFiles.getRowCount() != 0);
		deleteLink.setVisible(attachedFiles.getRowCount() != 0 && mayEdit);

	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al ítem que se está comentando.
	 */
	public ItemDTO getItemDTO() {
		return itemDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está comentando.
	 */
	public void setItemDTO(ItemDTO anItemDTO) {
		this.itemDTO = anItemDTO;
	}

	/**
	 * Recupera la colección de archivos adjuntos corespondientes al ítem.
	 * 
	 * @return una colección que contiene DTOs para cada uno de los archivos
	 *         adjuntos.
	 */
	private List<ItemFileDTO> getAttachedFiles() {
		ArrayList<ItemFileDTO> result = new ArrayList<ItemFileDTO>();

		try {
			ItemsServiceBI aService = this.getItemsService();
			result.addAll(aService.getAttachedFilesOfItem(this.getItemDTO()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return la colección de identificadores de los archivos adjuntos
	 *         seleccionados.
	 */
	public Collection<String> getSelectedAttachedFiles() {
		return this.selectedAttachedFiles;
	}

	/**
	 * Setter.
	 * 
	 * @param someStrings
	 *            es la colección de identificadores de los archivos adjuntos
	 *            seleccionados.
	 */
	public void setSelectedAttachedFiles(Collection<String> someStrings) {
		this.selectedAttachedFiles = someStrings;
	}

}
