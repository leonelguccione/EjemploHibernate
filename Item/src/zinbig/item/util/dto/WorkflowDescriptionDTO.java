/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

/**
 * Las instancias de esta clase se utilizan pare representar la información
 * relevante de las descripciones de los workflows y sus nodos.<br>
 * Esta clase implementa la interface Serializable ya que debe ser mantenida en
 * memoria y eventualmente se la puede enviar a otros servidores.<br>
 * Esta clase implementa la interface Versionable para poder controlar la
 * edición concurrente del workflow representado por este dto por parte de dos
 * usuarios.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class WorkflowDescriptionDTO extends ItemAbstractDTO implements
		Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -7795586789641700200L;

	/**
	 * Es el nombre de la descripción de workflow representada por este dto.
	 */
	public String name;

	/**
	 * Constructor.
	 * 
	 * @param anOid
	 *            es el oid del objeto representado por este dto.
	 * @param aName
	 *            es el nombre de la descripción de workflow representada por
	 *            este dto.
	 * @param aNumber
	 *            es el número de la versión de este objeto.
	 */
	public WorkflowDescriptionDTO(String anOid, String aName, int aNumber) {
		this.setOid(anOid);
		this.setName(aName);
		this.setVersion(aNumber);
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre de la descripción del workflow representada por este
	 *         dto.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre de la descripción del workflow representada por
	 *            este dto.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

}
