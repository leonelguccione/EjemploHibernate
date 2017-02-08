/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Esta clase representa el tope de la jerarquía de los paneles que permiten
 * cargar los datos relacionados con cada una de las propiedades adicionales
 * especificadas para un ítem de un proyecto en particular.<br>
 * Cada subclase es responsable de definir los componentes gráficos requeridos
 * para cada tipo de descripción de propiedad.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PropertyPanel extends Panel {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = 2312865027021918434L;

	/**
	 * Mantiene el valor ingresado por el usuario.
	 */
	public String value;

	/**
	 * Mantiene el nombre de la propiedad adicional.
	 */
	public String propertyName;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el identificador de este panel.
	 */
	public PropertyPanel(String anId) {
		super(anId);
	}

	/**
	 * Getter.
	 * 
	 * @return el valor ingresado por el usuario para esta propiedad.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter.
	 * 
	 * @param aValue
	 *            es el valor ingresado por el usuario para esta propiedad.
	 */
	public void setValue(String aValue) {
		this.value = aValue;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la propiedad que se está configurando.
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad que se está configurando.
	 */
	public void setPropertyName(String aName) {
		this.propertyName = aName;
	}

}
