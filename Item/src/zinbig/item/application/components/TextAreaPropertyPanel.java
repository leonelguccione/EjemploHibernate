/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.PropertyDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para crear los paneles que permiten
 * al usuario ingresar los datos para una propiedad adicional de tipo TextArea.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class TextAreaPropertyPanel extends PropertyPanel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 7828831090377086106L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el id de este componente.
	 * @param aPropertyDescriptionDTO
	 *            es el DTO que representa la descripción de la propiedad
	 *            adicional.
	 */
	public TextAreaPropertyPanel(String id,
			PropertyDescriptionDTO aPropertyDescriptionDTO) {
		super(id);
		this.setPropertyName(aPropertyDescriptionDTO.getName());

		Label inputLabel = new Label("simpleTitle", aPropertyDescriptionDTO
				.getName());
		Label requiredLabel = new Label("requiredSimpleTitle", "*");
		TextArea<String> textArea = new TextArea<String>("textarea",
				new PropertyModel<String>(this, "value"));
		textArea.setRequired(aPropertyDescriptionDTO.isRequired());
		requiredLabel.setVisible(aPropertyDescriptionDTO.isRequired());

		String styleClass = aPropertyDescriptionDTO.isRequired() ? "headingRequired2"
				: "headingF";
		inputLabel.add(new AttributeModifier("class", true, new Model<String>(
				styleClass)));

		this.add(inputLabel);
		this.add(requiredLabel);
		this.add(textArea);

		// construye el componente para dar feedback al usuario respecto del
		// componente.
		FeedbackLabel feedbackLabel = new FeedbackLabel("textAreaFeedback",
				textArea);
		feedbackLabel.setOutputMarkupId(true);
		this.add(feedbackLabel);

	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el id de este componente.
	 * @param aPropertyDescriptionDTO
	 *            es el DTO que representa la descripción de la propiedad
	 *            adicional.
	 * @param anItemDTO
	 *            es DTO que representa al ítem que se está editando.
	 */
	public TextAreaPropertyPanel(String id,
			PropertyDescriptionDTO aPropertyDescriptionDTO, ItemDTO anItemDTO) {
		this(id, aPropertyDescriptionDTO);

		if (anItemDTO.containsAdditionalProperty(aPropertyDescriptionDTO
				.getName())) {
			this.setValue(anItemDTO
					.getAdditionalProperty(aPropertyDescriptionDTO.getName()));
		}

	}

}
