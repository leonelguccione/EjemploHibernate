/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicaci�n.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.EditItemCommentsPanel;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.CommentDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase se utilizan para editar los comentarios de los
 * �tems. Se pueden agregar nuevos comentarios o eventualmente eliminar alguno
 * ya cargado.<br>
 * Este formulario controla que el usuario no sea nulo. Si el sistema tiene
 * configurada la propiedad EVERYONE_CAN_COMMENT_AN_ITEM en true entonces
 * cualquier usuario puede comentar el �tem. En caso contrario solamente el
 * responsable podr� comentarlo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemCommentsForm extends ItemForm {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = -7543559775646640476L;

	/**
	 * Es el texto del comentario.
	 */
	protected String comment;

	/**
	 * Es el DTO que representa al �tem que se est� comentando.
	 */
	protected ItemDTO itemDTO;

	/**
	 * Es una colecci�n que contiene los identificadores de los comentarios
	 * seleccionados.
	 */
	public Collection<String> selectedComments;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param anItemDTO
	 *            es el dto que representa la item que se est� comentando.
	 */
	public EditItemCommentsForm(String anId, ItemDTO anItemDTO) {
		super(anId);

		this.setItemDTO(anItemDTO);
		this.setSelectedComments(new ArrayList<String>());

		boolean mayEdit = this.getUserDTO() != null
				&& (this.getUserDTO().equals(anItemDTO.getResponsible()) | new Boolean(
						this.getSystemProperty("EVERYONE_CAN_COMMENT_AN_ITEM"))
						.booleanValue());

		Label aLabel = new Label("commentTitle", this
				.getString("editItemCommentsForm.title"));
		this.add(aLabel);
		aLabel.setVisible(mayEdit);

		Label asterixLabel = new Label("asterix", "*");
		this.add(asterixLabel);
		asterixLabel.setVisible(mayEdit);

		// construye el componente para la descripci�n
		final TextArea<String> commentTextArea = this
				.createCommentComponent(mayEdit);
		this.add(commentTextArea);

		// construye el componente para dar feedback al usuario respecto del
		// comentario del �tem.
		final FeedbackLabel commentFeedbackLabel = new FeedbackLabel(
				"commentEICFFeedback", commentTextArea);
		commentFeedbackLabel.setOutputMarkupId(true);
		this.add(commentFeedbackLabel);

		// construye el link de env�o del formulario
		SubmitLink submitLink = this.createSubmitComponent(mayEdit, this
				.getItemDTO());
		this.add(submitLink);

		// crea un link para restablecer los datos.
		Link<String> resetLink = new Link<String>("resetLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Se realiz� un click sobre el link. Restablece los datos del
			 * formulario.
			 */
			@Override
			public void onClick() {
				EditItemCommentsForm.this.clearInput();

			}

		};
		this.add(resetLink);
		resetLink.setVisible(mayEdit);

		// agrega una etiqueta que se muestra cuando no hay elementos para
		// listar.
		Label noResultsLabel = new Label("noResultsLabel", this
				.getString("NO_RESULTS"));
		this.add(noResultsLabel);

		// crea el componente que lista los comentarios existentes.
		this.add(createCommentsListComponent());
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private CheckGroup createCommentsListComponent() {
		// crea el componente para el listado de los comentarios existentes
		final CheckGroup group = new CheckGroup("group", this
				.getSelectedComments());
		group.add(new CheckGroupSelector("groupselector"));

		final SortableDataProvider<CommentDTO> provider = new SortableDataProvider<CommentDTO>() {

			List<CommentDTO> comments = getComments();
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
			public Iterator<CommentDTO> iterator(int index, int count) {
				return comments.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aCommentDTO
			 *            es el dto que representa a uno de los comentarios que
			 *            se est� iterando.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<CommentDTO> model(CommentDTO aCommentDTO) {
				return new Model<CommentDTO>(aCommentDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return comments.size();
			}
		};

		// crea el componente para listar los comentarios.
		final DataView<CommentDTO> comments = new DataView<CommentDTO>(
				"pageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada l�nea del listado, conteniendo un check para
			 * seleccionar el elemento, el comentario.
			 */
			@Override
			protected void populateItem(Item item) {
				CommentDTO dto = (CommentDTO) item.getModelObject();

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				Label aLabel = new Label("comment", Utils.decodeString(dto
						.getComment()));
				item.add(aLabel);

				Label dateLabel = new Label("date", dto.getCreationDate());
				item.add(dateLabel);

				Label creatorLabel = new Label("creator", dto
						.getCreatorUsername());
				item.add(creatorLabel);

			}

		};

		// agrega una etiqueta que se muestra cuando no hay elementos para
		// listar.
		Label noResultsLabel = (Label) this.get("noResultsLabel");

		if (comments.getRowCount() == 0) {
			group.setVisible(false);
			noResultsLabel.setVisible(true);
		} else {
			group.setVisible(true);
			noResultsLabel.setVisible(false);
		}

		group.add(comments);
		return group;
	}

	/**
	 * Crea el componente que permite cargar el comentario del �tem.<br>
	 * Este componente tiene asociados validadores para no permitir la carga de
	 * informaci�n que contenga caracteres inv�lidos.
	 * 
	 * @param isVisible
	 *            define si el componente debe estar visible o no.
	 * 
	 * @return el componente recientemente creado.
	 */
	private TextArea<String> createCommentComponent(boolean isVisible) {
		final TextArea<String> textArea = new TextArea<String>("comment",
				new PropertyModel<String>(this, "comment"));
		textArea.setRequired(true);
		textArea.setOutputMarkupId(true);
		textArea.add(new ItemSimpleStringValidator(textArea));
		textArea.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("EditItemForm.description.length"))));
		textArea.setVisible(isVisible);
		return textArea;
	}

	/**
	 * Getter.
	 * 
	 * @return el texto del comentario.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * Setter.
	 * 
	 * @param aComment
	 *            es el texto del comentario.
	 */
	public void setComment(String aComment) {
		this.comment = aComment;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al �tem que se est� comentando.
	 */
	public ItemDTO getItemDTO() {
		return itemDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al �tem que se est� comentando.
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
	 *            es el dto que representa al �tem.
	 * @return un link para enviar la informaci�n editada.
	 */
	private SubmitLink createSubmitComponent(boolean visible,
			final ItemDTO anItemDTO) {
		SubmitLink submitLink = new SubmitLink("submitLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				ItemsServiceBI aService = EditItemCommentsForm.this
						.getItemsService();

				try {

					ItemDTO newItemDTO = aService.addCommentToItem(
							EditItemCommentsForm.this.getItemDTO(), Utils
									.encodeString(EditItemCommentsForm.this
											.getComment()),
							EditItemCommentsForm.this.getUserDTO());
					EditItemCommentsForm.this.setItemDTO(newItemDTO);
					EditItemCommentsForm.this.setComment("");

					EditItemCommentsForm.this.get("group").replaceWith(
							EditItemCommentsForm.this
									.createCommentsListComponent());
					((EditItemCommentsPanel) EditItemCommentsForm.this
							.getParent()).updateItemVersion(newItemDTO);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		submitLink.setVisible(visible);
		return submitLink;
	}

	/**
	 * Este m�todo es ejecutado al finalizar la creaci�n del html
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
	 * @return la colecci�n de comentarios seleccionados.
	 */
	public Collection<String> getSelectedComments() {
		return this.selectedComments;
	}

	/**
	 * Setter.
	 * 
	 * @param someComments
	 *            es la colecci�n de comentarios seleccionados.
	 */
	public void setSelectedComments(Collection<String> someComments) {
		this.selectedComments = someComments;
	}

	/**
	 * Recupera la colecci�n de comentarios corespondientes al item.
	 * 
	 * @return una colecci�n que contiene DTOs para cada uno de los comentarios.
	 */
	private List<CommentDTO> getComments() {
		ArrayList<CommentDTO> result = new ArrayList<CommentDTO>();

		try {
			ItemsServiceBI aService = this.getItemsService();
			result.addAll(aService.getCommentsOfItem(this.getItemDTO()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
