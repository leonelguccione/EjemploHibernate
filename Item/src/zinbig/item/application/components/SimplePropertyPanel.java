/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;

/**
 * Este panel se utiliza para permitir al usuario cargar la información
 * relacionada con una propiedad simple adicional definida para el proeycto en
 * donde se encuentra el ítem que se está editando.<br>
 * Para funcionar, este panel necesita recibir como parámetro el DTO que
 * representa a la propiedad adicional, y en base a éste irá creando los
 * componentes gráficos necesarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class SimplePropertyPanel extends PropertyPanel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8869846139353579717L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el id de este componente.
	 * @param aPropertyDescriptionDTO
	 *            es el DTO que representa la descripción de una propiedad
	 *            adicional.
	 */
	public SimplePropertyPanel(String id,
			PropertyDescriptionDTO aPropertyDescriptionDTO) {
		super(id);
		this.setPropertyName(aPropertyDescriptionDTO.getName());

		Label inputLabel = new Label("simpleTitle", aPropertyDescriptionDTO
				.getName());
		Label requiredLabel = new Label("requiredSimpleTitle", "*");
		final TextField<String> input = new TextField<String>("simple",
				new PropertyModel<String>(this, "value"));
		input.setRequired(aPropertyDescriptionDTO.isRequired());
		requiredLabel.setVisible(aPropertyDescriptionDTO.isRequired());

		String styleClass = aPropertyDescriptionDTO.isRequired() ? "headingRequired2"
				: "headingF";
		inputLabel.add(new AttributeModifier("class", true, new Model<String>(
				styleClass)));

		// construye el componente para dar feedback al usuario respecto del
		// componente.
		FeedbackLabel feedbackLabel = new FeedbackLabel("simpleFeedback", input);
		this.add(input);
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
	public SimplePropertyPanel(String id,
			PropertyDescriptionDTO aPropertyDescriptionDTO, ItemDTO anItemDTO) {
		this(id, aPropertyDescriptionDTO);

		if (anItemDTO.containsAdditionalProperty(aPropertyDescriptionDTO
				.getName())) {
			this.setValue(anItemDTO
					.getAdditionalProperty(aPropertyDescriptionDTO.getName()));
		}

	}

}
