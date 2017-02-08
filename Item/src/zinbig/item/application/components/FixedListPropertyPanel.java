/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;

/**
 * Este panel se utiliza para permitir al usuario cargar la información
 * relacionada con una propiedad que tiene definida una lista fija de posibles
 * valores.<br>
 * Para funcionar, este panel necesita recibir como parámetro el DTO que
 * representa a la propiedad adicional, y en base a éste irá creando los
 * componentes gráficos necesarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FixedListPropertyPanel extends PropertyPanel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 409851184807546517L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el id de este componente.
	 * @param aPropertyDescriptionDTO
	 *            es el DTO que representa la descripción de una propiedad
	 *            adicional.
	 */
	public FixedListPropertyPanel(String id,
			final PropertyDescriptionDTO aPropertyDescriptionDTO) {
		super(id);
		this.setPropertyName(aPropertyDescriptionDTO.getName());

		Label inputLabel = new Label("simpleTitle", aPropertyDescriptionDTO
				.getName());
		Label requiredLabel = new Label("requiredSimpleTitle", "*");

		// crea un modelo para acceder a la lista de valores
		IModel<List<String>> choices = new AbstractReadOnlyModel<List<String>>() {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Recupera la lista de valores que contiene este modelo.
			 * 
			 * @return una lista de posibles valores para la lista.
			 * 
			 */
			@Override
			public List<String> getObject() {
				List<String> result = new ArrayList<String>();
				result.addAll(aPropertyDescriptionDTO.getValues());
				return result;
			}

		};

		// construye la lista.
		DropDownChoice<String> list = new DropDownChoice<String>("fixedList",
				new PropertyModel<String>(this, "value"), choices);
		list.setOutputMarkupId(true);
		list.setRequired(aPropertyDescriptionDTO.isRequired());
		requiredLabel.setVisible(aPropertyDescriptionDTO.isRequired());
		this.add(list);
		String styleClass = aPropertyDescriptionDTO.isRequired() ? "headingRequired2"
				: "headingF";
		inputLabel.add(new AttributeModifier("class", true, new Model<String>(
				styleClass)));

		// construye el componente para dar feedback al usuario respecto del
		// componente.
		FeedbackLabel feedbackLabel = new FeedbackLabel("fixedListFeedback",
				list);

		feedbackLabel.setOutputMarkupId(true);
		this.add(feedbackLabel);
		this.add(inputLabel);
		this.add(requiredLabel);

	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el id de este componente.
	 * @param aPropertyDescriptionDTO
	 *            es el DTO que representa la descripción de una propiedad
	 *            adicional.
	 * @param anItemDTO
	 *            es DTO que representa al ítem que se está editando.
	 */
	public FixedListPropertyPanel(String id,
			PropertyDescriptionDTO aPropertyDescriptionDTO, ItemDTO anItemDTO) {
		this(id, aPropertyDescriptionDTO);

		if (anItemDTO.containsAdditionalProperty(aPropertyDescriptionDTO
				.getName())) {
			this.setValue(anItemDTO
					.getAdditionalProperty(aPropertyDescriptionDTO.getName()));
		}

	}

}
