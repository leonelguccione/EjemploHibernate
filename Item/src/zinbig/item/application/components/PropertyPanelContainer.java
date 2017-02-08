/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import zinbig.item.application.forms.ItemForm;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan como contenedoras de los paneles
 * para las propiedades adicionales definidas por el proyecto.<br>
 * Este panel es requerido para poder repintar con AJAX la lista de paneles
 * individuales.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PropertyPanelContainer extends Panel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 6586172834082195860L;

	/**
	 * Es una colección que mantiene una referencia a cada panel de propiedad
	 * adicional que se ha creado. Es una forma más directa de acceder luego a
	 * los valores ingresados por el usuario.
	 */
	protected Collection<PropertyPanel> propertyPanels;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este componente.
	 * @param selectedProject
	 *            es el proyecto seleccionado para crear el nuevo ítem.
	 * @param aForm
	 *            es el formulario que está creando este componente.
	 */
	public PropertyPanelContainer(String anId, ProjectDTO selectedProject,
			ItemForm aForm) {
		super(anId);

		this.setPropertyPanels(new ArrayList<PropertyPanel>());

		// crea la lista de paneles
		List<PropertyDescriptionDTO> additionalProperties = new ArrayList<PropertyDescriptionDTO>();

		try {

			if (selectedProject != null) {
				additionalProperties.addAll(aForm.getProjectsService()
						.getPropertyDescriptionsOfProject(selectedProject));
			}

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
				listItem.add(createPropertyPanel(aProperty));

			}

		};

		this.add(aListView);
		aListView.setOutputMarkupId(true);
		aListView.setReuseItems(true);
	}

	/**
	 * Crea un panel que crea los componentes gráficos requeridos dependiendo
	 * del tipo de la propiedad adicional.
	 * 
	 * @param aProperty
	 *            es el DTO que representa a la propiedad adicional que se debe
	 *            cargar.
	 * @return el panel que corresponde a la propiedad.
	 */
	private PropertyPanel createPropertyPanel(PropertyDescriptionDTO aProperty) {

		PropertyPanel aPanel = null;
		switch (aProperty.getPropertyType()) {
		case 'S':
			aPanel = new SimplePropertyPanel("propertyPanel", aProperty);
			break;
		case 'T':
			aPanel = new TextAreaPropertyPanel("propertyPanel", aProperty);
			break;
		case 'F':
			aPanel = new FixedListPropertyPanel("propertyPanel", aProperty);
			break;
		case 'D':
			aPanel = new DynamicListPropertyPanel("propertyPanel", aProperty);
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
	 * Recupera los valores cargados por el usuario en cada uno de los paneles
	 * que se utilizan para ingresar las propiedades adicionales.
	 * 
	 * @return un diccionario que contiene pares clave/valor para cada una de
	 *         las propiedades adicionales.
	 */
	public Map<String, String> gatherAdditionalProperties() {
		Iterator<PropertyPanel> panelsIterator = this.getPropertyPanels()
				.iterator();
		Map<String, String> result = new HashMap<String, String>();
		PropertyPanel aPanel = null;
		while (panelsIterator.hasNext()) {
			aPanel = panelsIterator.next();
			result.put(aPanel.getPropertyName(), aPanel.getValue());
		}
		return result;
	}

}
