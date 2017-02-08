/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentaci�n <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.util.Comparator;

import org.apache.wicket.Component;

/**
 * Las instancias de esta clase se utilizan para comparar y poder ordenar dos
 * operaciones.<br>
 * La compraci�n tiene varios pasos: <br>
 * 1- se comparan las secciones de men� establecidas por las dos operaciones. Si
 * son iguales las secciones entonces se continua evaluando.<br>
 * 2- se comparan los nombres de las operaciones, internacionalizadas. Si son
 * iguales entonces se continua con la evaluaci�n.<br>
 * 3- se comparan los nombres de las categor�as de cada operaci�n. Si son
 * iguales entonces se continua con la evaluaci�n.<br>
 * 4- se comparan los par�metros para ordenar las operaciones en el men�.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class OperationDTOComparator implements Comparator<OperationDTO> {

	/**
	 * Es el componente que est� solicitando la comparaci�n entre dos
	 * operaciones y que se utiliza para obtener el texto internacionalizado de
	 * las operaciones.
	 */
	private Component component;

	/**
	 * Constructor.
	 * 
	 * @param aComponent
	 *            es el componente que se utilizar� para acceder al texto
	 *            internacionalizado de las operaciones.
	 */
	public OperationDTOComparator(Component aComponent) {
		this.setComponent(aComponent);
	}

	/**
	 * Compara las dos operaciones para ordenarlas.
	 * 
	 * @param anOperation
	 *            es la primer operaci�n a comparar.
	 * @param anotherOperation
	 *            es la segunda operaci�n a comparar.
	 * @return un entero que representa el orden de las dos operaciones. Ver la
	 *         documentaci�n de esta clase para conocer el criterio de
	 *         ordenaci�n.
	 */
	@Override
	public int compare(OperationDTO anOperation, OperationDTO anotherOperation) {

		int result = anOperation.getMenuSection().compareTo(
				anotherOperation.getMenuSection());
		if (result == 0) {
			result = this.getComponent().getString(anOperation.getName())
					.compareTo(
							this.getComponent().getString(
									anotherOperation.getName()));
		}
		if (result == 0) {
			result = this.getComponent().getString(
					anOperation.getCategoryName()).compareTo(
					this.getComponent().getString(
							anotherOperation.getCategoryName()));
		}
		if (result == 0) {
			result = anOperation.getParameters().toString().compareTo(
					anotherOperation.getParameters().toString());
		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para internacionalizar el texto de
	 *         las operaciones.
	 */
	public Component getComponent() {
		return this.component;
	}

	/**
	 * Setter.
	 * 
	 * @param aComponent
	 *            es el componente que se utiliza para internacionalizar el
	 *            texto de las operaciones.
	 */
	public void setComponent(Component aComponent) {
		this.component = aComponent;
	}

}
