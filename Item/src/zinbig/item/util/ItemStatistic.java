/**
 * Este paquete contiene clases �tiles para la herramienta.
 */
package zinbig.item.util;

import java.util.Date;

/**
 * Las instancias de esta clase se utilizan para reflejar las estad�sticas de
 * uso que se realizan sobre la herramienta.<br>
 * Esta informaci�n es generada por un JOB de Quartz en forma autom�tica todos
 * los mediod�as para que sea consumida por las p�ginas iniciales de los
 * proyectos.<br>
 * Por una decisi�n de dise�o, estas estad�sticas no tienen una referencia
 * directa a los objetos que muestran (por ejemplo usuarios, �tems o proyectos),
 * sino que copian sus valores, de manera que el borrado sea m�s simple.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemStatistic {

	/**
	 * Es el identificador de esta instancia.
	 */
	public String oid;

	/**
	 * Mantiene la fecha en la cual se cre� esta estad�stica.
	 */
	public Date updateTime;

	/**
	 * Es la cantidad de �tems que existen.
	 */
	public Long itemsCount;

	/**
	 * Cantidad de usuarios cargados en la herramienta.
	 */
	public Long usersCount;

	/**
	 * Cantidad de proyectos administrados por la herramienta.
	 */
	public Long projectsCount;

	/**
	 * Cantidad de �tems abiertos al momento de generar esta estad�stica.
	 */
	public Long openItemsCount;

	/**
	 * Cantidad de proyectos que son p�blicos.
	 */
	public Long publicProjectsCount;

	/**
	 * Nombre del proyecto que m�s actividad ha tenido.
	 */
	public String mostActiveProjectName;

	/**
	 * Identificador del proyecto que m�s actividad ha tenido.
	 */
	public String mostActiveProjectOid;

	/**
	 * Username del usuario m�s activo.
	 */
	public String firstMostActiveUser;

	/**
	 * Username del segundo usuario m�s activo.
	 */
	public String secondMostActiveUser;

	/**
	 * Username del tercer usuario m�s activo.
	 */
	public String thirdMostActiveUser;

	/**
	 * Id del �tem que tiene m�s observadores.
	 */
	public String mostInterestingItemId;

	/**
	 * Identificador del �tem que tiene m�s observadores.
	 */
	public String mostInterestingItemOid;

	/**
	 * Constructor vac�o por defecto.
	 */
	public ItemStatistic() {
		this(new Date(), new Long(0), new Long(0), new Long(0), new Long(0),
				new Long(0), "", "", "", "", "", "", "");
	}

	/**
	 * Constructor.
	 * 
	 * @param anUpdateTime
	 *            es la fecha en la que se est� creando esta instancia.
	 * @param someItemsCount
	 *            es la cantidad de �tems encontrados.
	 * @param someUsersCount
	 *            es la cantidad de usuarios encontrados.
	 * @param someProjectsCount
	 *            es la cantidad de proyectos encontrados.
	 * @param someOpenItemsCount
	 *            es la cantidad de �tems que no est�n finalizados.
	 * @param somePublicProjectsCount
	 *            es la cantidad de proyectos de car�cter p�blico.
	 * @param aProjectName
	 *            es el nombre del proyecto m�s activo.
	 * @param aProjectOid
	 *            es el identificador del proyecto m�s activo.
	 * @param anUsername
	 *            es el username del usuario m�s activo.
	 * @param anotherUsername
	 *            es el username del segundo usuario m�s activo.
	 * @param yetAnotherUsername
	 *            es el username del tercer usuario m�s activo.
	 * @param anItemId
	 *            es el id del �tem que tiene m�s seguidores.
	 * @param anItemOid
	 *            es el identificador del �tem que tiene m�s seguidores.
	 */
	public ItemStatistic(Date anUpdateTime, Long someItemsCount,
			Long someUsersCount, Long someProjectsCount,
			Long someOpenItemsCount, Long somePublicProjectsCount,
			String aProjectName, String aProjectOid, String anUsername,
			String anotherUsername, String yetAnotherUsername, String anItemId,
			String anItemOid) {

		this.setOid(IDGenerator.getId());
		this.setUpdateTime(anUpdateTime);
		this.setItemsCount(someOpenItemsCount);
		this.setUsersCount(someUsersCount);
		this.setProjectsCount(someProjectsCount);
		this.setOpenItemsCount(someOpenItemsCount);
		this.setPublicProjectsCount(somePublicProjectsCount);
		this.setMostActiveProjectName(aProjectName);
		this.setMostActiveProjectOid(aProjectOid);
		this.setFirstMostActiveUser(anUsername);
		this.setSecondMostActiveUser(anotherUsername);
		this.setThirdMostActiveUser(yetAnotherUsername);
		this.setMostInterestingItemId(anItemId);
		this.setMostInterestingItemOid(anItemOid);

	}

	/**
	 * Getter.
	 * 
	 * @return el identificador de esta instancia.
	 */
	public String getOid() {
		return this.oid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador de este objecto.
	 */
	public void setOid(String anOid) {
		this.oid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return la fecha de actualizaci�n de este objeto.
	 */
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * Setter.
	 * 
	 * @param aDate
	 *            es la fecha de actualizaci�n de este objeto.
	 */
	public void setUpdateTime(Date aDate) {
		this.updateTime = aDate;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de �tems encontrados.
	 */
	public Long getItemsCount() {
		return this.itemsCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de �tems encontrados.
	 */
	public void setItemsCount(Long aCount) {
		this.itemsCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de usuarios encontrados.
	 */
	public Long getUsersCount() {
		return this.usersCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de usuarios encontrados.
	 */
	public void setUsersCount(Long aCount) {
		this.usersCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos encontrados.
	 */
	public Long getProjectsCount() {
		return this.projectsCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de proyectos encontrados.
	 */
	public void setProjectsCount(Long aCount) {
		this.projectsCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de �tems abiertos.
	 */
	public Long getOpenItemsCount() {
		return this.openItemsCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de �tems abiertos.
	 */
	public void setOpenItemsCount(Long aCount) {
		this.openItemsCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de proyectos p�blicos.
	 */
	public Long getPublicProjectsCount() {
		return this.publicProjectsCount;
	}

	/**
	 * Setter.
	 * 
	 * @param aCount
	 *            es la cantidad de proyectos p�blicos.
	 */
	public void setPublicProjectsCount(Long aCount) {
		this.publicProjectsCount = aCount;
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del proyecto m�s activo.
	 */
	public String getMostActiveProjectName() {
		return this.mostActiveProjectName;
	}

	/**
	 * Setter.
	 * 
	 * @param aName
	 *            es el nombre del proyecto m�s activo.
	 */
	public void setMostActiveProjectName(String aName) {
		this.mostActiveProjectName = aName;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador del proyecto m�s activo.
	 */
	public String getMostActiveProjectOid() {
		return this.mostActiveProjectOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador del proyecto m�s activo.
	 */
	public void setMostActiveProjectOid(String anOid) {
		this.mostActiveProjectOid = anOid;
	}

	/**
	 * Getter.
	 * 
	 * @return el username del usuario m�s activo.
	 */
	public String getFirstMostActiveUser() {
		return this.firstMostActiveUser;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el username del usuario m�s activo.
	 */
	public void setFirstMostActiveUser(String anUsername) {
		this.firstMostActiveUser = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return el username del segundo usuario m�s activo.
	 */
	public String getSecondMostActiveUser() {
		return this.secondMostActiveUser;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el username del segundo usuario m�s activo.
	 */
	public void setSecondMostActiveUser(String anUsername) {
		this.secondMostActiveUser = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return el username del tercer usuario m�s activo.
	 */
	public String getThirdMostActiveUser() {
		return this.thirdMostActiveUser;
	}

	/**
	 * Setter.
	 * 
	 * @param anUsername
	 *            es el username del tercer usuario m�s activo.
	 */
	public void setThirdMostActiveUser(String anUsername) {
		this.thirdMostActiveUser = anUsername;
	}

	/**
	 * Getter.
	 * 
	 * @return el id del �tem que tiene m�s seguidores.
	 */
	public String getMostInterestingItemId() {
		return this.mostInterestingItemId;
	}

	/**
	 * Setter.
	 * 
	 * @param anId
	 *            es el id del �tem que tiene m�s seguidores.
	 */
	public void setMostInterestingItemId(String anId) {
		this.mostInterestingItemId = anId;
	}

	/**
	 * Getter.
	 * 
	 * @return el identificador del �tem que tiene m�s seguidores.
	 */
	public String getMostInterestingItemOid() {
		return this.mostInterestingItemOid;
	}

	/**
	 * Setter.
	 * 
	 * @param anOid
	 *            es el identificador del �tem que tiene m�s seguidores.
	 */
	public void setMostInterestingItemOid(String anOid) {
		this.mostInterestingItemOid = anOid;
	}

}
