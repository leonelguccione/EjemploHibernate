/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.io.File;

import org.apache.wicket.Response;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.application.components.ItemAttachedFilesPanel;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de esta clase se utilizan para agregar nuevos adjuntos a un
 * ítem que se está editando.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class AttachFileForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -7543559775646640476L;

	/**
	 * Es un componente para adjuntar archivos.
	 */
	protected FileUploadField attachedFile;

	/**
	 * Es el DTO que representa al ítem que se está comentando.
	 */
	protected ItemDTO itemDTO;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param anItemDTO
	 *            es el dto que representa la item que se está editando.
	 */
	public AttachFileForm(String anId, ItemDTO anItemDTO) {
		super(anId);

		this.setItemDTO(anItemDTO);

		// crea el componente para subir archivos.
		this.setMultiPart(true);
		FileUploadField upload = new FileUploadField("attachedFile");
		this.setAttachedFile(upload);
		upload.setRequired(true);
		this.add(upload);

		// crea el componente para mostrar errores relacionados con el archivo
		// seleccionado.
		final FeedbackLabel feedbackLabel = new FeedbackLabel(
				"attachedAFFFeedback", upload);
		feedbackLabel.setOutputMarkupId(true);
		this.add(feedbackLabel);

		// valida que el usuario actual pueda agregar o borrar los archivos
		// adjuntos.
		boolean mayEdit = this.getUserDTO() != null
				&& (this.getUserDTO().equals(anItemDTO.getResponsible()));

		// construye el link de envío del formulario
		SubmitLink submitLink = this.createSubmitComponent(mayEdit, this
				.getItemDTO());
		this.add(submitLink);

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
	 * Crea el componente para enviar los datos del formulario.
	 * 
	 * @param visible
	 *            define si este componente tiene que ser visible o no.
	 * @param anItemDTO
	 *            es el dto que representa al ítem.
	 * @return un link para enviar la información editada.
	 */
	private SubmitLink createSubmitComponent(boolean visible,
			final ItemDTO anItemDTO) {
		SubmitLink submitLink = new SubmitLink("submitLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Maneja el evento de envío del formulario.
			 */
			@Override
			public void onSubmit() {
				String aPath = ((ItemApplication) AttachFileForm.this
						.getApplication()).getPathForItem(anItemDTO);
				if (AttachFileForm.this.getAttachedFile().getFileUpload() != null) {
					FileUpload upload = AttachFileForm.this.getAttachedFile()
							.getFileUpload();

					// crea el archivo nuevo
					if (new File(aPath).mkdirs() || (new File(aPath).exists())) {
						File newFile = new File(aPath, upload
								.getClientFileName());
						if (newFile.exists()) {
							this
									.error(this
											.getString("ItemFileAlreadyExistsException"));
						} else {
							try {
								// guarda el archivo.
								newFile.createNewFile();
								upload.writeTo(newFile);

								ItemDTO newItemDTO = AttachFileForm.this
										.getItemsService().attachFileToItem(
												anItemDTO,
												upload.getClientFileName());
								ItemAttachedFilesPanel parent = (ItemAttachedFilesPanel) AttachFileForm.this
										.getParent();
								parent.updateItemVersion(newItemDTO);
								parent.updateListOfAttachedFiles(anItemDTO);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}
			}
		};
		submitLink.setVisible(visible);
		return submitLink;
	}

	/**
	 * Este método es ejecutado al finalizar la creación del html
	 * correspondiente a este componente.
	 */
	@Override
	protected void onAfterRender() {

		super.onAfterRender();
		final Response response = this.getResponse();
		response
				.write("<script type=\"text/javascript\" language=\"javascript\">document.forms[2].elements[1].focus()</script>");
	}

	/**
	 * Getter.
	 * 
	 * @return el componente utilizado para adjuntar archivos.
	 */
	public FileUploadField getAttachedFile() {
		return this.attachedFile;
	}

	/**
	 * Setter.
	 * 
	 * @param aComponent
	 *            es el componente utilizado para adjuntar archivos.
	 */
	public void setAttachedFile(FileUploadField aComponent) {
		this.attachedFile = aComponent;
	}

	/**
	 * Guarda los archivos adjuntos que se subieron con el ítem recientemente
	 * creado.
	 * 
	 * @param anItem
	 *            es el ítem recientemente creado.
	 */
	protected void saveAttachedFiles(ItemDTO anItemDTO) {

		String aPath = ((ItemApplication) AttachFileForm.this.getApplication())
				.getPathForItem(anItemDTO);
		if (this.getAttachedFile().getFileUpload() != null) {
			FileUpload upload = this.getAttachedFile().getFileUpload();

			// crea el archivo nuevo
			if (new File(aPath).mkdirs()) {
				File newFile = new File(aPath, upload.getClientFileName());
				try {
					// guarda el archivo.
					newFile.createNewFile();
					upload.writeTo(newFile);

					this.getItemsService().attachFileToItem(anItemDTO,
							upload.getClientFileName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

}
