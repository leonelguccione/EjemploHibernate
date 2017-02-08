/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Las instancias de esta clase se utilizan para representar las descripciones
 * de propiedades adicionales. Esta clase almacena los datos relacionados con el
 * oid, el nombre y si es o no requerida la propiedad.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PropertyDescriptionDTO extends ItemAbstractDTO {

	/**
	 * UID por defecto.
	 */
	private static final long serialVersionUID = -5338691581390119905L;

	/**
	 * Define si una propiedad es requerida o no.
	 */
	public boolean required;

	/**
	 * Mantiene el nombre de la propiedad descripta por este objeto.
	 */
	public String name;

	/**
	 * Mantiene el tipo de la descripción de propiedad representada por este
	 * DTO.
	 */
	public char propertyType;

	/**
	 * Es una colección que contiene los posibles valores para las propiedades
	 * adicionales que tienen una lista de valores.
	 */
	public Collection<String> values;

	/**
	 * Constructor por defecto. Este constructor no debería utilizarse.
	 */
	public PropertyDescriptionDTO() {
		this("", "", false, 'S');
	}

	/**
	 * Constructor.
	 * 
	 * @param anOid
	 *            es el oid del objeto representado por este dto.
	 * @param aName
	 *            es el nombre de la descripción de propiedad.
	 * @param isRequired
	 *            establece si la propiedad es requerida o no.
	 * @param aType
	 *            es el tipo de la descripción de propiedad representada por
	 *            este DTO.
	 * 
	 */
	public PropertyDescriptionDTO(String anOid, String aName,
			boolean isRequired, char aType) {
		this.setOid(anOid);
		this.setName(aName);
		this.setRequired(isRequired);
		this.setPropertyType(aType);
		this.setValues(new ArrayList<String>());
	}

	/**
	 * Getter.
	 * 
	 * @return true en caso de que la propiedad sea requerida; false en caso
	 *         contrario.
	 */
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * Setter.
	 * 
	 * @param aBoolean
	 *            define si la propiedad es requerida.
	 */
	public void setRequired(boolean aBoolean) {
		this.required = aBoolean;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la propiedad.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el tipo de la propiedad adicional representada por este DTO.
	 */
	public char getPropertyType() {
		return this.propertyType;
	}

	/**
	 * Setter.
	 * 
	 * @param aType
	 *            es el tipo de la propiedad adicional representada por este
	 *            DTO.
	 */
	public void setPropertyType(char aType) {
		this.propertyType = aType;
	}

	/**
	 * Getter.
	 * 
	 * @return la colección de posibles valores para la propiedad adicional
	 *         representada por este dto.
	 */
	public Collection<String> getValues() {
		return this.values;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es la colección de posibles valores para la propiedad
	 *            adicional representada por este dto.
	 */
	public void setValues(Collection<String> aCollection) {
		this.values = aCollection;
	}

}
