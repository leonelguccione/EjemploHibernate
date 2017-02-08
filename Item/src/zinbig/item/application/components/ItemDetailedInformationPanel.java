/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.pages.PrintableItemDetailedInformationPage;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.WorkflowsServiceBI;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.CommentDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ItemFileDTO;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.dto.WorkflowNodeDTO;

/**
 * Las instancias de este panel se utilizan para mostrar un resumen completo de
 * la información de un ítem.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemDetailedInformationPanel extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8961536659461520363L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 * @param anItemDTO
	 *            es el dto que representa al item que se está mostrando.
	 */
	public ItemDetailedInformationPanel(String anId, ItemDTO anItemDTO,
			boolean aBoolean) {
		super(anId);

		Label printerLabel = new Label("printLabel", this.getString("print"));
		PageParameters params = new PageParameters();
		params.put("ITEM_OID", anItemDTO.getOid());
		Link<String> aLink = new BookmarkablePageLink<String>("printLink",
				PrintableItemDetailedInformationPage.class, params);
		aLink.add(printerLabel);
		this.add(aLink);
		aLink.setVisible(aBoolean);

		Label itemId = new Label("itemId", new Integer(anItemDTO.getId())
				.toString());
		this.add(itemId);

		Label responsible = new Label("itemResponsible", anItemDTO
				.getResponsible().toString());
		this.add(responsible);

		Label title = new Label("title", Utils.decodeString(anItemDTO
				.getTitle()));
		this.add(title);

		Label description = new Label("itemType", anItemDTO.getItemType()
				.getTitle());
		this.add(description);

		Label itemType = new Label("description", Utils.decodeString(anItemDTO
				.getDescription()));
		this.add(itemType);

		Label priority = new Label("priority", anItemDTO.getPriority()
				.getName());
		this.add(priority);

		Label creationDate = new Label("creationDate", anItemDTO
				.getCreationDate());
		this.add(creationDate);

		Label project = new Label("project", anItemDTO.getProjectName());
		this.add(project);

		Label creator = new Label("creator", anItemDTO.getCreator().getAlias());
		this.add(creator);

		Label state = new Label("state", this.getString(anItemDTO.getState()));
		this.add(state);

		Label node = new Label("node", anItemDTO.getCurrentNode().getTitle());
		this.add(node);

		// crea el componente para listar los comentarios del ítem.
		this.createCommentsComponent(anItemDTO);

		// crea el componente para listar los archivos adjuntos del ítem.
		this.createAttachedFilesComponent(anItemDTO);

		// crea el componente para listar los estados anteriores.
		this.createOldWorkflowNodesComponent(anItemDTO);

		// crea el componente para listar los observadores del ítem.
		this.createObserversComponent(anItemDTO);

		// crea el componente para listar las propiedades adicionales.
		this.createAdditionalPropertiesComponent(anItemDTO);

	}

	/**
	 * Crea un componente que permite listar los archivos adjuntos del ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 */
	private void createAttachedFilesComponent(final ItemDTO anItemDTO) {
		final String aPath = ((ItemApplication) this.getApplication())
				.getPathForItem(anItemDTO);

		final List<ItemFileDTO> files = this.getAttachedFiles(anItemDTO);

		final SortableDataProvider<ItemFileDTO> provider = new SortableDataProvider<ItemFileDTO>() {

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
		final DataView<ItemFileDTO> attachedFiles = new DataView<ItemFileDTO>(
				"attachedPageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(Item item) {
				ItemFileDTO dto = (ItemFileDTO) item.getModelObject();

				Label aLabel = new Label("filename", dto.getFilename());
				File aFile = new File(aPath + dto.getFilename());
				Link fileLink = new DownloadLink("fileLink", aFile);
				fileLink.add(aLabel);
				item.add(fileLink);

				Label dateLabel = new Label("creationDate", dto
						.getCreationDate());
				item.add(dateLabel);

			}

		};

		this.add(attachedFiles);
		attachedFiles.setVisible(!files.isEmpty());

	}

	/**
	 * Crea un componente que permite listar todos los comentarios del ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 */
	private void createCommentsComponent(final ItemDTO anItemDTO) {
		// crea el componente para el listado de los comentarios existentes

		final List<CommentDTO> itemComments = this.getComments(anItemDTO);

		final SortableDataProvider<CommentDTO> provider = new SortableDataProvider<CommentDTO>() {

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
			public Iterator<CommentDTO> iterator(int index, int count) {
				return itemComments.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aCommentDTO
			 *            es el dto que representa a uno de los comentarios que
			 *            se está iterando.
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
				return itemComments.size();
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
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el comentario.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(Item item) {
				CommentDTO dto = (CommentDTO) item.getModelObject();

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

		this.add(comments);
		comments.setVisible(!itemComments.isEmpty());
	}

	/**
	 * Recupera la colección de comentarios corespondientes al item.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 * 
	 * @return una colección que contiene DTOs para cada uno de los comentarios.
	 */
	private List<CommentDTO> getComments(ItemDTO anItemDTO) {
		ArrayList<CommentDTO> result = new ArrayList<CommentDTO>();

		try {
			ItemsServiceBI aService = ServiceLocator.getInstance()
					.getItemsService();
			result.addAll(aService.getCommentsOfItem(anItemDTO));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Recupera la colección de archivos adjuntos corespondientes al ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 * 
	 * @return una colección que contiene DTOs para cada uno de los archivos
	 *         adjuntos.
	 */
	private List<ItemFileDTO> getAttachedFiles(ItemDTO anItemDTO) {
		ArrayList<ItemFileDTO> result = new ArrayList<ItemFileDTO>();

		try {
			ItemsServiceBI aService = ServiceLocator.getInstance()
					.getItemsService();
			result.addAll(aService.getAttachedFilesOfItem(anItemDTO));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Crea un componente que permite listar todos los estados anteriores del
	 * ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 */
	private void createOldWorkflowNodesComponent(final ItemDTO anItemDTO) {
		// crea el componente para el listado de los estados anteriores
		// existentes

		final List<WorkflowNodeDTO> nodes = this.getOldWorkflowNodes(anItemDTO);

		final SortableDataProvider<WorkflowNodeDTO> provider = new SortableDataProvider<WorkflowNodeDTO>() {

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
			public Iterator<WorkflowNodeDTO> iterator(int index, int count) {
				return nodes.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aWorkflowNodeDTO
			 *            es el dto que representa a uno de los estados
			 *            anteriores que se está iterando.
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
				return nodes.size();
			}
		};

		// crea el componente para listar los estados anteriores.
		final DataView<WorkflowNodeDTO> nodesView = new DataView<WorkflowNodeDTO>(
				"oldNodesPageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
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

		this.add(nodesView);
		nodesView.setVisible(!nodes.isEmpty());

	}

	/**
	 * Recupera la colección de estados anteriores corespondientes al ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 * 
	 * @return una colección que contiene DTOs para cada uno de los estados
	 *         anteriores.
	 */
	private List<WorkflowNodeDTO> getOldWorkflowNodes(ItemDTO anItemDTO) {
		ArrayList<WorkflowNodeDTO> result = new ArrayList<WorkflowNodeDTO>();
		TreeSet<WorkflowNodeDTO> aux = new TreeSet<WorkflowNodeDTO>(
				new Comparator<WorkflowNodeDTO>() {
					DateFormat formatter = new SimpleDateFormat();

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

	/**
	 * Crea un componente que permite listar todos los usuarios observadores del
	 * ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 */
	private void createObserversComponent(final ItemDTO anItemDTO) {
		// crea el componente para el listado de los observadores existentes

		final List<UserDTO> observers = this.getObservers(anItemDTO);

		final SortableDataProvider<UserDTO> provider = new SortableDataProvider<UserDTO>() {

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
			public Iterator<UserDTO> iterator(int index, int count) {
				return observers.iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param anUserDTO
			 *            es el dto que representa a uno de los observadores.
			 * @return un decorador como modelo del DTO recibido.
			 */
			@Override
			public IModel<UserDTO> model(UserDTO anUserDTO) {
				return new Model<UserDTO>(anUserDTO);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return observers.size();
			}
		};

		// crea el componente para listar los observadores
		final DataView<UserDTO> observersDataView = new DataView<UserDTO>(
				"observersPageable", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado, conteniendo un check para
			 * seleccionar el elemento, el comentario.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(Item item) {
				UserDTO dto = (UserDTO) item.getModelObject();

				Label aLabel = new Label("observer", dto.getAlias());
				item.add(aLabel);

			}

		};

		this.add(observersDataView);
		observersDataView.setVisible(!observers.isEmpty());

	}

	/**
	 * Recupera la colección de observadores corespondientes al ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 * 
	 * @return una colección que contiene DTOs para cada uno de los
	 *         observadores.
	 */
	private List<UserDTO> getObservers(ItemDTO anItemDTO) {
		ArrayList<UserDTO> result = new ArrayList<UserDTO>();

		try {
			ItemsServiceBI aService = ServiceLocator.getInstance()
					.getItemsService();
			result.addAll(aService.getObserversOfItem(anItemDTO));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Crea un componente que permite listar todos las propiedades adicionales
	 * del ítem.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está mostrando.
	 */
	private void createAdditionalPropertiesComponent(final ItemDTO anItemDTO) {
		// crea el componente para el listado de las propiedades adicionales

		final Map<String, String> additionalProperties = anItemDTO
				.getAdditionalProperties();

		final SortableDataProvider<String> provider = new SortableDataProvider<String>() {

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
			public Iterator<String> iterator(int index, int count) {
				return additionalProperties.keySet().iterator();
			}

			/**
			 * Getter.
			 * 
			 * @param aString
			 *            es la clave de la propiedad que se está mostrando.
			 * @return un decorador como modelo del string recibido.
			 */
			@Override
			public IModel<String> model(String aString) {
				return new Model<String>(aString);
			}

			/**
			 * Getter.
			 * 
			 * @return la cantidad de elementos que tiene este proveedor de
			 *         datos.
			 */
			@Override
			public int size() {
				return additionalProperties.keySet().size();
			}
		};

		// crea el componente para listar los comentarios.
		final DataView<String> properties = new DataView<String>(
				"pageableProperties", provider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Arma cada línea del listado.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(Item item) {
				String aString = (String) item.getModelObject();

				Label aLabel = new Label("propertyName", aString);
				item.add(aLabel);

				Label valueLabel = new Label("propertyValue",
						additionalProperties.get(aString));
				item.add(valueLabel);

			}

		};

		this.add(properties);
		properties.setVisible(!additionalProperties.isEmpty());

	}

}
