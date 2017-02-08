/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import zinbig.item.application.components.DynamicListPropertyPanel;
import zinbig.item.application.components.FixedListPropertyPanel;
import zinbig.item.application.components.ItemAdditionalPropertiesPanel;
import zinbig.item.application.components.PropertyPanel;
import zinbig.item.application.components.SimplePropertyPanel;
import zinbig.item.application.components.TextAreaPropertyPanel;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para asignar valores a las
 * propiedades adicionales definidas dentro de un proyecto para los ítems.<BR>
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SetAdditionalPropertiesForm extends ItemForm {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -1354047591423302923L;

	/**
	 * Es una colección que permite referenciar de manera más directa los
	 * paneles que se utilizan para agregar las propiedades adicionales.
	 */
	protected Collection<PropertyPanel> propertyPanels;

	/**
	 * Es el DTO que representa al ítem que se está editando.
	 */
	protected ItemDTO itemDTO;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param anItemDTO
	 *            es el DTO que representa al ítem que se está editando.
	 */
	public SetAdditionalPropertiesForm(String anId, final ItemDTO anItemDTO) {
		super(anId);

		this.setPropertyPanels(new ArrayList<PropertyPanel>());
		this.setItemDTO(anItemDTO);

		// obtengo el DTO del proyecto para recuperar las definiciones de las
		// propiedades adicionales
		ProjectDTO aProjectDTO = anItemDTO.getProject();
		List<PropertyDescriptionDTO> additionalProperties = new ArrayList<PropertyDescriptionDTO>();

		try {
			additionalProperties.addAll(this.getProjectsService()
					.getPropertyDescriptionsOfProject(aProjectDTO));

		} catch (Exception e) {

		}

		ListView<PropertyDescriptionDTO> aListView = new ListView<PropertyDescriptionDTO>(
				"listView", additionalProperties) {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Itera sobre la colección de elementos de la lista dibujando las
			 * categorías de los ítems de menú.
			 */
			@Override
			public void populateItem(
					final ListItem<PropertyDescriptionDTO> listItem) {

				final PropertyDescriptionDTO aProperty = (PropertyDescriptionDTO) listItem
						.getModelObject();
				listItem.add(createPropertyPanel(aProperty, anItemDTO));

			}

		};

		this.add(aListView);
		aListView.setReuseItems(true);

		// construye el link de envío del formulario
		SubmitLink submitLink = new SubmitLink("submitLink") {
			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				Map<String, String> values = SetAdditionalPropertiesForm.this
						.gatherValues();

				ItemsServiceBI service = SetAdditionalPropertiesForm.this
						.getItemsService();

				try {
					ItemDTO newItemDTO = service.setAdditionalPropertiesToItem(
							getItemDTO(), values);
					SetAdditionalPropertiesForm.this.setItemDTO(newItemDTO);
					((ItemAdditionalPropertiesPanel) SetAdditionalPropertiesForm.this
							.getParent()).updateItemVersion(newItemDTO);
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		};
		this.add(submitLink);

	}

	/**
	 * Crea un panel que crea los componentes gráficos requeridos dependiendo
	 * del tipo de la propiedad adicional.
	 * 
	 * @param aProperty
	 *            es el DTO que representa a la propiedad adicional que se debe
	 *            cargar.
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está editando.
	 * 
	 * @return el panel que corresponde a la propiedad.
	 */
	private PropertyPanel createPropertyPanel(PropertyDescriptionDTO aProperty,
			ItemDTO anItemDTO) {

		PropertyPanel aPanel = null;
		switch (aProperty.getPropertyType()) {
		case 'S':
			aPanel = new SimplePropertyPanel("propertyPanel", aProperty,
					anItemDTO);
			break;
		case 'T':
			aPanel = new TextAreaPropertyPanel("propertyPanel", aProperty,
					anItemDTO);
			break;
		case 'F':
			aPanel = new FixedListPropertyPanel("propertyPanel", aProperty,
					anItemDTO);
			break;
		case 'D':
			aPanel = new DynamicListPropertyPanel("propertyPanel", aProperty,
					anItemDTO);
			break;
		default:
			break;
		}

		this.addPropertyPanel(aPanel);
		return aPanel;
	}

	/**
	 * Agrega un nuevo panel a este formulario.
	 * 
	 * @param aPanel
	 *            es el panel que se debe agregar.
	 */
	private void addPropertyPanel(PropertyPanel aPanel) {
		this.getPropertyPanels().add(aPanel);

	}

	/**
	 * Getter.
	 * 
	 * @return la colección de paneles de propiedades de este formulario.
	 */
	public Collection<PropertyPanel> getPropertyPanels() {
		return this.propertyPanels;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de paneles de propiedades adicionales de este
	 *            formulario.
	 */
	public void setPropertyPanels(Collection<PropertyPanel> aCollection) {
		this.propertyPanels = aCollection;
	}

	/**
	 * Obtiene los valores de cada uno de los paneles de propiedades
	 * adicionales.
	 * 
	 * @return un mapa (clave,valor) para cada uno de los valores ingresados por
	 *         el usuario.
	 */
	protected Map<String, String> gatherValues() {
		Map<String, String> values = new HashMap<String, String>();

		Iterator<PropertyPanel> iterator = this.getPropertyPanels().iterator();
		PropertyPanel aPanel = null;
		while (iterator.hasNext()) {
			aPanel = iterator.next();
			values.put(aPanel.getPropertyName(), aPanel.getValue());
		}

		return values;
	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al ítem que se está editando.
	 */
	public ItemDTO getItemDTO() {
		return itemDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está editando.
	 */
	public void setItemDTO(ItemDTO anItemDTO) {
		this.itemDTO = anItemDTO;
	}

}
