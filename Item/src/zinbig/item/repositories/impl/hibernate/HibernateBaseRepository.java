/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import zinbig.item.repositories.bi.ItemAbstractRepositoryBI;
import zinbig.item.util.persistence.ItemQuery;
import zinbig.item.util.spring.ItemApplicationContext;

/**
 * Esta clase representa el tope de la jerarquía de repositorios basados en
 * Hibernate. <br>
 * Todo repositorio debería extender esta clase.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class HibernateBaseRepository extends HibernateDaoSupport
		implements ItemAbstractRepositoryBI {

	/**
	 * Recupera un objeto del repositorio en forma eficiente.
	 * 
	 * @param aClass
	 *            es la clase del objeto que debe recuperarse.
	 * @param anOid
	 *            es el oid de la instancia a ser recuperada.
	 * 
	 * @return el objeto persistido que coincide con el oid recibido.
	 */
	protected Object findById(Class<?> aClass, Object anOid) {

		return this.getSession().get(aClass, (String) anOid);
	}

	/**
	 * Recupera un objeto del repositorio en forma eficiente.
	 * 
	 * @param aClass
	 *            es la clase del objeto que debe recuperarse.
	 * @param anOid
	 *            es el oid de la instancia a ser recuperada.
	 * 
	 * @return el objeto persistido que coincide con el oid recibido.
	 * @throws cada
	 *             subclase define la excepción que se levanta cuando no se
	 *             encuentra la instancia.
	 */
	public abstract Object findById(String anOid) throws Exception;

	/**
	 * Recupera del contexto de la aplicación una consulta nombrada.
	 * 
	 * @param aName
	 *            es el nombre de la consulta que se debe recuperar.
	 * @return una consulta de hibernate.
	 */
	protected Query getNamedQuery(String aName) {
		ApplicationContext aContext = ItemApplicationContext
				.getApplicationContext();

		ItemQuery itemQuery = (ItemQuery) aContext.getBean(aName);

		Query aQuery = this.getSession()
				.createQuery(itemQuery.getQueryString());

		return aQuery;
	}

	/**
	 * Recupera del contexto de la aplicación una consulta nombrada.
	 * 
	 * @param aName
	 *            es el nombre de la consulta que se debe recuperar.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar en la consulta.
	 * @return una consulta de hibernate.
	 */
	protected Query getNamedQuery(String aName, String aFilterString) {
		ApplicationContext aContext = ItemApplicationContext
				.getApplicationContext();

		ItemQuery itemQuery = (ItemQuery) aContext.getBean(aName);

		Query aQuery = this.getSession().createQuery(
				itemQuery.getQueryString() + aFilterString);

		return aQuery;
	}

	/**
	 * Borra de la base de datos el objeto recibido.
	 * 
	 * @param anObject
	 *            es el objeto que debe eliminarse de la base.
	 */
	@Override
	public void delete(Object anObject) {
		this.getSession().delete(anObject);

	}

	/**
	 * Borra de la base de datos el objeto recibido.
	 * 
	 * @param someObjects
	 *            es una colección que contiene los objetos que deben ser
	 *            eliminados.
	 */
	public void deleteObjects(Collection<? extends Object> someObjects) {

		Session aSession = this.getSession();
		for (Iterator<? extends Object> i = someObjects.iterator(); i.hasNext();) {

			aSession.delete(i.next());

		}
	}

	/**
	 * Recupera del contexto de la aplicación una consulta nombrada.
	 * 
	 * @param aName
	 *            es el nombre de la consulta que se debe recuperar.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la que hay que ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            es el orden que se debe aplicar al resultado.
	 * @return una consulta de hibernate.
	 */
	protected Query getNamedQuery(String aName, String aPropertyName,
			String anOrdering) {
		ApplicationContext aContext = ItemApplicationContext
				.getApplicationContext();

		ItemQuery itemQuery = (ItemQuery) aContext.getBean(aName);
		itemQuery.setPropertyForOrdering(aPropertyName);
		itemQuery.setOrdering(anOrdering);

		Query aQuery = this.getSession().createQuery(
				itemQuery.createQueryString());

		return aQuery;
	}

	/**
	 * Recupera del contexto de la aplicación una consulta nombrada.
	 * 
	 * @param aName
	 *            es el nombre de la consulta que se debe recuperar.
	 * @param aFilterString
	 *            es el filtro que se debe aplicar en la consulta.
	 * @param aPropertyName
	 *            es el nombre de la propiedad por la que hay que ordenar el
	 *            resultado.
	 * @param anOrdering
	 *            es el orden que se debe aplicar al resultado.
	 * @return una consulta de hibernate.
	 */
	protected Query getNamedQuery(String aName, String aFilterString,
			String aPropertyName, String anOrdering) {
		ApplicationContext aContext = ItemApplicationContext
				.getApplicationContext();

		ItemQuery itemQuery = (ItemQuery) aContext.getBean(aName);
		itemQuery.setPropertyForOrdering(aPropertyName);
		itemQuery.setOrdering(anOrdering);

		Query aQuery = this.getSession().createQuery(
				itemQuery.createQueryString(aFilterString));

		return aQuery;
	}

}
