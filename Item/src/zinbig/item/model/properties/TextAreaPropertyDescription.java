/**
 * Este paquete contiene las clases requeridas para modelar las propiedades
 * adicionales que se pueden definir en cada proyecto para ser completadas luego
 * en cada ítem.
 */
package zinbig.item.model.properties;

/**
 * Las instancias de esta clase se utilizan para representar descripciones de
 * propiedades largas, con un sólo valor que debe ser ingresado por el usuario
 * sin poder seleccionarlo de una lista. El texto puede ser de múltiples
 * renglones. <br>
 * Las subclases de PropertyDescription se utilizan para trabajar en base a
 * double-dispatching para definir qué componente gráfico se debe mostrar al
 * usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class TextAreaPropertyDescription extends PropertyDescription {

	/**
	 * Constructor.
	 * 
	 */
	public TextAreaPropertyDescription() {
		super();
		this.setType('T');
	}

	/**
	 * Configura al receptor con valores adicionales. Cada subclase debe
	 * reimplementar este método.
	 * 
	 * @param aValue
	 *            es un string que podría contener valores adicionales
	 *            requeridos.
	 */
	public void configureWithValueAdditionalValue(String aValue) {

	}

}
