/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Las instancias de este componente se utilizan para presentar al usuario un
 * checkbox que tiene una imagen en vez de la forma habitual. cada vez que el
 * usuario hace click sobre este componente se cambia el valor booleano que
 * mantiene este componente.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemCheckboxWithImage extends Panel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 970917699847387826L;

	/**
	 * Mantiene el estado de este componente.
	 */
	protected boolean state;

	/**
	 * Nombre de la imagen que se debe utilizar para representar el estado
	 * "true".
	 */
	protected String trueStateImageName;

	/**
	 * Nombre de la imagen que se debe utilizar para representar el estado
	 * "false".
	 */
	protected String falseStateImageName;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este componente.
	 * @param initialState
	 *            indica el estado inicial que debe tener este componente.
	 * @param falseStateImageName
	 *            nombre de la imagen para el estado "true".
	 * @param trueStateImageName
	 *            nombre de la imagen para el estado "false".
	 */
	public ItemCheckboxWithImage(String id, boolean initialState,
			String falseStateImageName, String trueStateImageName) {

		super(id);
		this.setState(initialState);
		this.setTrueStateImageName(trueStateImageName);
		this.setFalseStateImageName(falseStateImageName);

		// creo la primer imagen
		ContextImage image = this.createImage();
		this.add(image);

		// agrego el behavior ajax para actualizar el componente con cada click.
		this.add(new AjaxEventBehavior("onClick") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget anAjaxTarget) {

				// cambia el estado del componente
				ItemCheckboxWithImage.this.setState(!ItemCheckboxWithImage.this
						.getState());

				ContextImage image = ItemCheckboxWithImage.this.createImage();

				// reemplazo la imagen dependiendo del estado del panel
				((Panel) this.getComponent()).get("img").replaceWith(image);

				// actualizo el panel a través de la llamada ajax
				anAjaxTarget.addComponent(this.getComponent());

			}
		});
	}

	/**
	 * Getter.
	 * 
	 * @return el estado de este componente.
	 */
	public boolean getState() {
		return this.state;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            establece el estado de este componente.
	 */
	public void setState(boolean aBoolean) {
		this.state = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la imagen para representar el estado "true".
	 */
	public String getTrueStateImageName() {
		return this.trueStateImageName;
	}

	/**
	 * Setter.
	 * 
	 * @param anImageName
	 *            es el nombre de la imagen para representar el estado "true".
	 */
	public void setTrueStateImageName(String anImageName) {
		this.trueStateImageName = anImageName;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la imagen para representar el estado "false".
	 */
	public String getFalseStateImageName() {
		return this.falseStateImageName;
	}

	/**
	 * Setter.
	 * 
	 * @param anImageName
	 *            es el nombre de la imagen para representar el estado "false".
	 */
	public void setFalseStateImageName(String anImageName) {
		this.falseStateImageName = anImageName;
	}

	/**
	 * Crea la imagen que se debe mostrar para representar el estado de este
	 * componente.
	 * 
	 * @return una imagen para representar el estado.
	 */
	protected ContextImage createImage() {

		String imageName = this.getState() ? this.getTrueStateImageName()
				: this.getFalseStateImageName();

		ContextImage image = new ContextImage("img", new Model<String>(
				imageName));
		image.setOutputMarkupId(true);

		return image;
	}

}
