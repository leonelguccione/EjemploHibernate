/**
 * Este paquete contiene las clases requeridas para modelar las propiedades
 * adicionales que se pueden definir en cada proyecto para ser completadas luego
 * en cada �tem.
 */
package zinbig.item.model.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Las instancias de esta clase se utilizan para representar descripciones de
 * propiedades basadas en listas de valores fijas, definidas al momento de crear
 * esta descripci�n y que no se pueden cambiar. <br>
 * Los valores son almacenados dentro de un string utiliz�ndose el caracter #
 * como separador.<br>
 * Las subclases de PropertyDescription se utilizan para trabajar en base a
 * double-dispatching para definir qu� componente gr�fico se debe mostrar al
 * usuario.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FixedValuesListPropertyDescription extends ListPropertyDescription {

	/**
	 * Mantiene los valores fijos de esta descripci�n de propiedad.
	 */
	protected String values;

	/**
	 * Constructor.
	 */
	public FixedValuesListPropertyDescription() {
		super();
		this.setValues("");
		this.setType('F');
	}

	/**
	 * Getter.
	 * 
	 * @return un string que contiene los valores de esta descripci�n de
	 *         propiedad, con un caracter separador.
	 */
	public String getValues() {
		return this.values;
	}

	/**
	 * Setter.
	 * 
	 * @param values
	 *            es un string que contiene los valores de esta descripci�n de
	 *            propiedad.
	 */
	public void setValues(String values) {
		this.values = values;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene los valores fijos de esta descripci�n.
	 */
	public Collection<String> getValuesAsCollection() {
		Collection<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(this.getValues(), "#");
		while (tokenizer.hasMoreTokens()) {
			result.add(tokenizer.nextToken());
		}

		return result;
	}

	/**
	 * Agrega un nuevo valor a los valores fijos. El nuevo valor se pone
	 * autom�ticamente al final de la lista.
	 * 
	 * @param aValue
	 *            es el nuevo valor que debe agregarse.
	 */
	public void addValue(String aValue) {
		this.setValues(this.getValues() + "#" + aValue);
	}

	/**
	 * Genera el string que contiene los valores a partir de la colecci�n de
	 * valores recibidos.
	 * 
	 * @param aCollection
	 *            es la colecci�n de valores que tendr� esta descripci�n.
	 */
	public void createFromCollectionOfValues(Collection<String> aCollection) {
		Iterator<String> iterator = aCollection.iterator();
		StringBuffer sb = new StringBuffer("");
		while (iterator.hasNext()) {
			sb.append("#");
			sb.append(iterator.next());
		}

		this.setValues(sb.toString());
	}

	/**
	 * Configura al receptor con valores adicionales. Cada subclase debe
	 * reimplementar este m�todo.
	 * 
	 * @param aValue
	 *            es un string que podr�a contener valores adicionales
	 *            requeridos.
	 */
	public void configureWithValueAdditionalValue(String aValue) {
		this.setValues(aValue);
	}
}
