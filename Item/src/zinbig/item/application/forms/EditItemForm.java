/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import zinbig.item.application.components.EditItemBasicDataPanel;
import zinbig.item.application.components.FeedbackLabel;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.PriorityDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.UserGroupDTO;
import zinbig.item.util.validators.ItemSimpleStringValidator;

/**
 * Las instancias de esta clase se utilizan para editar los datos básicos de un
 * ítem. Si el usuario es nulo (anónimo) o no es el responsable de este ítem,
 * entonces todos los campos aparecen deshabilitados. Si el ítem está asignado a
 * un grupo al cual pertenece el usuario entonces éste primero deberá "tomarlo"
 * para que el ítem pase a ser de su responsabilidad.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class EditItemForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 6373964324028743652L;

	/**
	 * Representa el título del ítem.
	 */
	public String title;

	/**
	 * Es la descripción del ítem.
	 */
	public String description;

	/**
	 * Es el dto que representa la prioridad del ítem.
	 */
	public PriorityDTO priority;

	/**
	 * Mantiene el dto del tipo de ítem seleccionado por el usuario.
	 */
	protected ItemTypeDTO itemType;

	/**
	 * Es el dto que representa la ítem.
	 */
	public ItemDTO itemDTO;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está editando.
	 */
	public EditItemForm(String anId, final ItemDTO anItemDTO) {
		super(anId);

		this.setItemDTO(anItemDTO);

		// agrega un link para tomar este ítem en caso de que esté asignado a un
		// grupo de usuarios del usuario.
		Link<String> takeLink = new AjaxFallbackLink<String>("takeLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Asigna al usuario como responsable del ítem.
			 */
			@Override
			public void onClick(AjaxRequestTarget aTarget) {

				try {
					EditItemForm.this.setItemDTO(EditItemForm.this
							.getItemsService().takeItem(
									EditItemForm.this.getUserDTO(),
									EditItemForm.this.getItemDTO()));
					this.setVisible(false);
					EditItemForm.this.enableComponents(true);
					aTarget.addComponent(EditItemForm.this);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		};
		this.add(takeLink);
		takeLink.setVisible(false);

		// agrega un link par que el usuario que no es responsable de este ítem
		// puede anotarse como interesado en recibir las notificaciones de
		// cambios.
		Link<String> followLink = new AjaxFallbackLink<String>("followLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Asigna al usuario como observador del ítem.
			 */
			@Override
			public void onClick(AjaxRequestTarget aTarget) {
				if (EditItemForm.this.getUserDTO() != null) {
					try {
						EditItemForm.this.getItemsService().addObserverToItem(
								EditItemForm.this.getUserDTO(),
								EditItemForm.this.getItemDTO());
						this.setVisible(false);
						aTarget.addComponent(this);
					} catch (Exception e) {

					}
				}

			}

		};
		this.add(followLink);
		followLink.setOutputMarkupId(true);

		boolean mayFollow = false;
		if (this.getUserDTO() == null) {
			mayFollow = false;// asignamos true así no aparece visible en caso
			// de un usuario anónimo
		} else {
			try {
				mayFollow = !this.getItemDTO().getResponsible().equals(
						this.getUserDTO())
						&& !this.getItemsService().isUserObserverOfItem(
								this.getUserDTO(), this.getItemDTO());
			} catch (Exception e) {
				mayFollow = false;
				e.printStackTrace();
			}

		}
		followLink.setVisible(mayFollow);

		// recupera el usuario actual de la sesión. Podría ser nulo en caso
		// de un usuario anónimo.
		UserDTO userDTO = this.getUserDTO();
		boolean canEdit = false;
		if (userDTO != null) {
			// si el usuario no es nulo entonces verifica si es el responsable
			// de este ítem
			if (userDTO.equals(this.getItemDTO().getResponsible())) {
				canEdit = true;
				takeLink.setVisible(false);
			} else {
				// tenemos usuario pero no es el responsable así que se verifica
				// que el responsable sea un grupo que contenga a este usuario
				if (this.getItemDTO().getResponsible().isUserGroup()) {

					try {
						canEdit = false;
						Collection<UserGroupDTO> userGroups = this
								.getUsersService().getUserGroupsOfUser(
										this.getUserDTO());
						if (userGroups.contains(this.getItemDTO()
								.getResponsible())) {

							takeLink.setVisible(true);
						} else {
							takeLink.setVisible(false);
						}
					} catch (Exception e) {

					}

				}
			}
		}

		Label itemIdLabel = new Label("itemId", new Long(this.getItemDTO()
				.getId()).toString());
		this.add(itemIdLabel);

		Label itemResponsibleLabel = new Label("itemResponsible", this
				.getItemDTO().getResponsible().toString());
		this.add(itemResponsibleLabel);

		// carga los valores del ítem
		this.setTitle(Utils.decodeString(this.getItemDTO().getTitle()));
		this.setDescription(Utils.decodeString(this.getItemDTO()
				.getDescription()));
		this.setPriority(this.getItemDTO().getPriority());
		this.setItemType(this.getItemDTO().getItemType());

		// construye el input field para el título
		final TextField<String> titleField = this
				.createTitleFieldComponent(canEdit);
		this.add(titleField);

		// construye el componente para dar feedback al usuario respecto del
		// título del ítem.
		final FeedbackLabel titleFeedbackLabel = new FeedbackLabel(
				"titleEIFFeedback", titleField);
		titleFeedbackLabel.setOutputMarkupId(true);
		this.add(titleFeedbackLabel);

		// construye el componente para la descripción
		final TextArea<String> descriptionTextArea = this
				.createDescriptionComponent(canEdit);
		this.add(descriptionTextArea);

		// construye el componente para dar feedback al usuario respecto de
		// la descripcion del ítem.
		final FeedbackLabel descriptionFeedbackLabel = new FeedbackLabel(
				"descriptionEIFFeedback", descriptionTextArea);
		descriptionFeedbackLabel.setOutputMarkupId(true);
		this.add(descriptionFeedbackLabel);

		// construye el componente que permite mostrar una lista con todas
		// las prioridades correspondientes al proyecto seleccionado.
		DropDownChoice<PriorityDTO> prioritiesList = this
				.createPrioritiesListComponent(canEdit, this.getItemDTO()
						.getProject());
		this.add(prioritiesList);

		// construye el componente para dar feedback al usuario respecto de
		// la lista de prioridades
		final FeedbackLabel priorityFeedbackLabel = new FeedbackLabel(
				"priorityEIFFeedback", prioritiesList);
		priorityFeedbackLabel.setOutputMarkupId(true);
		this.add(priorityFeedbackLabel);

		// construye el componente que permite mostrar una lista con todos los
		// tipos de ítems correspondientes al proyecto seleccionado.
		DropDownChoice<ItemTypeDTO> itemTypesList = this
				.createItemTypesListComponent(canEdit, this.getItemDTO()
						.getProject());
		this.add(itemTypesList);

		// construye el componente para dar feedback al usuario respecto de
		// la lista de tipos de ítems
		final FeedbackLabel itemTypesFeedbackLabel = new FeedbackLabel(
				"itemTypeEIFFeedback", itemTypesList);
		itemTypesFeedbackLabel.setOutputMarkupId(true);
		this.add(itemTypesFeedbackLabel);

		// construye el link de envío del formulario
		SubmitLink submitLink = this.createSubmitComponent(canEdit, this
				.getItemDTO());
		this.add(submitLink);

		// crea un link para restablecer los datos.
		Link<String> resetLink = new Link<String>("resetLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Se realizó un click sobre el link. Restablece los datos del
			 * formulario.
			 */
			@Override
			public void onClick() {
				EditItemForm.this.clearInput();

			}

		};
		this.add(resetLink);
		resetLink.setVisible(canEdit);

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
			 * Envía el formulario con todos los datos editados.
			 */
			@Override
			public void onSubmit() {
				ItemsServiceBI aService = EditItemForm.this.getItemsService();

				try {

					ItemDTO newItemDTO = aService.editItem(anItemDTO, Utils
							.encodeString(EditItemForm.this.getTitle()), Utils
							.encodeString(EditItemForm.this.getDescription()),
							EditItemForm.this.getPriority(), EditItemForm.this
									.getItemType());

					((EditItemBasicDataPanel) EditItemForm.this.getParent())
							.updateItemVersion(newItemDTO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		submitLink.setVisible(visible);
		return submitLink;
	}

	/**
	 * Getter.
	 * 
	 * @return el título del ítem.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del ítem.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Crea el componente que permite cargar el título del nuevo ítem.
	 * 
	 * @return un input field para cargar el título, con todos los behaviors
	 *         necesarios para que no se pueda dar de alta un título vacío, o
	 *         mayor que una longitud dada o con caracteres inválidos.
	 */
	private TextField<String> createTitleFieldComponent(boolean enabled) {
		final TextField<String> titleField = new TextField<String>("title",
				new PropertyModel<String>(this, "title"));
		titleField.setRequired(true);
		titleField.setOutputMarkupId(true);
		titleField.add(new ItemSimpleStringValidator(titleField));
		titleField.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddItemForm.title.length"))));
		titleField.setEnabled(enabled);
		return titleField;
	}

	/**
	 * Getter.
	 * 
	 * @return la descripción del ítem.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter.
	 * 
	 * @param aDescription
	 *            es la descripción del ítem.
	 */
	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	/**
	 * Crea el componente que permite cargar la descripcion del ítem.<br>
	 * Este componente tiene asociados validadores para no permitir la carga de
	 * información que contenga caracteres inválidos.
	 * 
	 * @param enabled
	 *            define si el componente debe estar habilitado o no.
	 * 
	 * @return el componente recientemente creado.
	 */
	private TextArea<String> createDescriptionComponent(boolean enabled) {
		final TextArea<String> textArea = new TextArea<String>("description",
				new PropertyModel<String>(this, "description"));
		textArea.setRequired(true);
		textArea.setOutputMarkupId(true);
		textArea.add(new ItemSimpleStringValidator(textArea));
		textArea.add(StringValidator.maximumLength(new Integer(this
				.getSystemProperty("AddItemForm.description.length"))));
		textArea.setEnabled(enabled);
		return textArea;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa la prioridad del ítem.
	 */
	public PriorityDTO getPriority() {
		return this.priority;
	}

	/**
	 * Setter.
	 * 
	 * @param aPriorityDTO
	 *            es el dto que representa la prioridad del ítem.
	 */
	public void setPriority(PriorityDTO aPriorityDTO) {
		this.priority = aPriorityDTO;
	}

	/**
	 * Crea el componente que permite seleccionar una prioridad para el ítem.<br>
	 * 
	 * 
	 * @param enabled
	 *            indica si el componente debe estar habilitado o no.
	 * 
	 * @return el componente para listar las prioridades.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<PriorityDTO> createPrioritiesListComponent(
			boolean enabled, final ProjectDTO aProjectDTO) {

		// crea un modelo para acceder a la lista de prioridades que debe
		// mostrarse a través del dropdownchoice de prioridades.
		IModel<List<PriorityDTO>> prioritiesChoices = new AbstractReadOnlyModel<List<PriorityDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de prioridades que contiene este
			 * modelo.
			 * 
			 * @return una lista de dtos de las prioridades.
			 * 
			 */
			@Override
			public List<PriorityDTO> getObject() {
				List<PriorityDTO> priorities = new ArrayList<PriorityDTO>();
				String aPropertyNameForOrdering = EditItemForm.this
						.getSystemProperty("PRIORITIES_ADMINISTRATION_COLUMN_NAME");
				String anOrdering = EditItemForm.this
						.getSystemProperty("PRIORITIES_ADMINISTRATION_COLUMN_ORDER");
				try {
					priorities.addAll(EditItemForm.this.getPrioritiesService()
							.findPrioritiesOfProject(aProjectDTO,
									aPropertyNameForOrdering, anOrdering));
				} catch (Exception e) {

				}

				return priorities;
			}

		};

		// construye la lista para mostrar todas las prioridades
		// correspondientes al proyecto.
		IChoiceRenderer priorityRenderer = new ChoiceRenderer("completeName",
				"oid");
		DropDownChoice prioritiesList = new DropDownChoice("priority",
				new PropertyModel(this, "priority"), prioritiesChoices,
				priorityRenderer);

		prioritiesList.setOutputMarkupId(true);
		prioritiesList.setRequired(true);
		prioritiesList.setEnabled(enabled);
		return prioritiesList;
	}

	/**
	 * Crea el componente que permite seleccionar un tipo para el ítem.<br>
	 * 
	 * 
	 * @param enabled
	 *            indica si el componente debe estar habilitado o no.
	 * 
	 * @return el componente para listar los tipos de ítems.
	 */
	@SuppressWarnings("unchecked")
	private DropDownChoice<ItemTypeDTO> createItemTypesListComponent(
			boolean enabled, final ProjectDTO aProjectDTO) {

		// crea un modelo para acceder a la lista de tipos de ítems que debe
		// mostrarse a través del dropdownchoice de tipos de ítems.
		IModel<List<ItemTypeDTO>> itemTypesChoices = new AbstractReadOnlyModel<List<ItemTypeDTO>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de dtos de tipos de ítems que contiene este
			 * modelo.
			 * 
			 * @return una lista de dtos de los tipos de ítems.
			 * 
			 */
			@Override
			public List<ItemTypeDTO> getObject() {
				List<ItemTypeDTO> itemTypes = new ArrayList<ItemTypeDTO>();

				try {
					itemTypes
							.addAll(EditItemForm.this
									.getItemsService()
									.findItemTypesOfProject(
											aProjectDTO,
											EditItemForm.this
													.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_NAME),
											EditItemForm.this
													.getSystemProperty(Constants.MANAGE_ITEM_TYPES_COLUMN_ORDER)));
				} catch (Exception e) {

				}

				return itemTypes;
			}

		};

		// construye la lista para mostrar todas los tipos de ítems
		// correspondientes al proyecto.
		IChoiceRenderer itemTypeRenderer = new ChoiceRenderer("title", "oid");
		DropDownChoice itemTypesList = new DropDownChoice("itemType",
				new PropertyModel(this, "itemType"), itemTypesChoices,
				itemTypeRenderer);

		itemTypesList.setOutputMarkupId(true);
		itemTypesList.setRequired(true);
		itemTypesList.setEnabled(enabled);
		return itemTypesList;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa la ítem.
	 */
	public ItemDTO getItemDTO() {
		return this.itemDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem.
	 */
	public void setItemDTO(ItemDTO anItemDTO) {
		this.itemDTO = anItemDTO;
	}

	/**
	 * Define el estado de habilitación de todos los componentes de este
	 * formulario.
	 * 
	 * @param aBoolean
	 *            define si deben estar habilitados o no los componentes.
	 */
	protected void enableComponents(boolean aBoolean) {
		this.get("title").setEnabled(aBoolean);
		this.get("description").setEnabled(aBoolean);
		this.get("priority").setEnabled(aBoolean);
		this.get("itemType").setEnabled(aBoolean);
		this.get("resetLink").setEnabled(aBoolean);
		this.get("submitLink").setEnabled(aBoolean);
		this.get("resetLink").setVisible(aBoolean);
		this.get("submitLink").setVisible(aBoolean);
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
	 * @return el dto que representa al tipo de ítem seleccionado.
	 */
	public ItemTypeDTO getItemType() {
		return this.itemType;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemTypeDTO
	 *            es el dto que representa al tipo de ítem seleccionado.
	 */
	public void setItemType(ItemTypeDTO anItemTypeDTO) {
		this.itemType = anItemTypeDTO;
	}

}
