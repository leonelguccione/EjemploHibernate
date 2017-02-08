package zinbig.item.application.components;

/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
import java.util.ArrayList;
import java.util.Collection;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

/**
 * Las instancias de esta clase se utilizan para filtrar los mensajes de error
 * que deben ser mostrados. Por la forma en la que Wicket maneja los errores, si
 * hay dos componentes para mostrar errores, ambos siempre mostraran los
 * errores, aún cuando uno sólo de ellos deba hacerlo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FeedbackPanelFilter implements IFeedbackMessageFilter {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 2966935830414978626L;

	/**
	 * Es una colección que contiene los mensajes que debe permitir mostrar el
	 * componente al cual se ha asociado este filtro.
	 */
	public Collection<String> acceptedMessages;

	/**
	 * Constructor.
	 * 
	 * @param someMessages
	 *            es una colección que contiene los mensajes que debe permitir
	 *            mostrar el componente al cual se ha asociado este filtro.
	 */
	public FeedbackPanelFilter(Collection<String> someMessages) {
		this.setAcceptedMessages(new ArrayList<String>());
		this.getAcceptedMessages().addAll(someMessages);
	}

	/**
	 * Verifica si el componente asociado a este filtro debe mostrar o no el
	 * mensaje recibido.
	 * 
	 * @param aFeedbackMessage
	 *            es el mensaje que se debe filtrar.
	 * @return true en caso de que la colección de mensajes contenga el string
	 *         de este mensaje; false en caso contrario.
	 */
	@Override
	public boolean accept(FeedbackMessage aFeedbackMessage) {

		return acceptedMessages.contains(aFeedbackMessage.getMessage());
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los mensajes que debe permitir mostrar
	 *         el componente al cual se ha asociado este filtro.
	 */
	public Collection<String> getAcceptedMessages() {
		return this.acceptedMessages;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colección que contiene los mensajes que debe permitir
	 *            mostrar el componente al cual se ha asociado este filtro.
	 */
	public void setAcceptedMessages(Collection<String> aCollection) {
		this.acceptedMessages = aCollection;
	}

}
