/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.io.Serializable;

import zinbig.item.util.IDGenerator;

/**
 * Las instancias de esta clase se utilizan para presentar información de los
 * componentes de filtros de ítems relacionados con los proyectos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class FilterComponentByProjectDTO extends ItemAbstractDTO implements
		Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 6938541037648507753L;

	/**
	 * Es el título del componente de filtro.
	 */
	public String title;

	/**
	 * Es el oid del proyecto utilizado por el componente del filtro
	 * representado por este dto.
	 */
	public String projectOid;

	/**
	 * Constructor.
	 */
	public FilterComponentByProjectDTO() {
		this("", IDGenerator.getId());
	}

	/**
	 * Constructor.
	 * 
	 * @param aProjectOid
	 *            es el oid del proyecto utilizado por el componente de filtro
	 *            representado por este dto..
	 * @param aTitle
	 *            es el título internacionalizado del componente de filtro
	 *            representado por este dto.
	 */
	public FilterComponentByProjectDTO(String aTitle, String aProjectOid) {
		this.setTitle(aTitle);
		this.setProjectOid(aProjectOid);
	}

	/**
	 * Getter.
	 * 
	 * @return el título del componente del filtro.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter.
	 * 
	 * @param aTitle
	 *            es el título del componente del filtro.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Verifica si el objeto receptor es igual al objeto recibido.
	 * 
	 * @param anObject
	 *            es el objeto contra el que se debe comparar.
	 * @return true si ambos objetos son instancias de la misma clase y si
	 *         representan al mismo proyecto (mismo oid); false en cualquier
	 *         otro caso.
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;

		try {
			FilterComponentByProjectDTO dto = (FilterComponentByProjectDTO) anObject;
			result = dto.getProjectOid().equals(this.getProjectOid());

		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Retorna el hashcode del receptor.
	 * 
	 * @return un entero calculado en base al hashcode del oid del proyecto.
	 */
	@Override
	public int hashCode() {

		return this.getProjectOid().hashCode();
	}

	/**
	 * Retorna un string que representa al receptor.
	 * 
	 * @return la representación está constituida por el título de este objeto.
	 */
	@Override
	public String toString() {
		return this.getTitle();
	}

	/**
	 * Getter.
	 * 
	 * @return el oid del proyecto utilizado por el componente de filtro
	 *         representado por este dto.
	 */
	public String getProjectOid() {
		return this.projectOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el oid del proyecto utilizado por el componente de filtro
	 *            representado por este dto.
	 */
	public void setProjectOid(String anOid) {
		this.projectOid = anOid;
	}

}
