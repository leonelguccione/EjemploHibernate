/**
 * Este paquete contiene clases �tiles para internacionalizar la aplicaci�n. 
 */
package zinbig.item.util.i18n;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import zinbig.item.application.ItemApplication;
import zinbig.item.util.persistence.ItemQuery;

/**
 * Las instancias de esta clase se utilizan para leer de la base de datos los
 * mensajes internacionalizados y cargarlos en memoria para acceder en forma
 * eficiente.<br>
 * Cada mensaje est� asociado con una p�gina en particular de la aplicaci�n.<br>
 * El m�todo preferido es {@link #loadStringResource(Component, String)} donde
 * aComponent es el componente de la p�gina que est� solicitando el mensaje.
 * Este m�todo recupera el mensaje asociado a la clase de la p�gina del
 * componente y no al componente en s� mismo.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemStringResourceLoader implements IStringResourceLoader {

	/**
	 * Mantiene un diccionario con todos los mensajes internacionalizados.
	 */
	@SuppressWarnings("unchecked")
	private Map messages;

	/**
	 * Es una consulta que permite recuperar todos los mensajes
	 * internacionalizados.
	 */
	protected ItemQuery i18nMessagesQuery;

	/**
	 * Es una consulta que permite recuperar todos las nacionalidades soportadas
	 * por la aplicaci�n.
	 */
	protected ItemQuery itemLocalesQuery;

	/**
	 * Esta colecci�n contiene todos los locales soportados por la aplicaci�n.
	 */
	private Collection<Locale> supportedLocales;

	/**
	 * Constructor.
	 * 
	 * @param aSessionFactory
	 *            es el objeto requerido para iniciar sesiones de hibernate a
	 *            fin de recuperar los mensajes persistidos.
	 * @param messagesQuery
	 *            es un objeto que representa la consulta que se debe ejecutar
	 *            para poder recuperar todos los mensajes internacionalizados.
	 * @param localesQuery
	 *            es un objeto que representa la consulta que se debe ejecutar
	 *            para poder recuperar todas las nacionalidades soportadas por
	 *            la aplicaci�n.
	 * @param application
	 *            es la aplicaci�n que se est� configurando con este objeto.
	 */
	@SuppressWarnings("unchecked")
	public ItemStringResourceLoader(SessionFactory aSessionFactory,
			ItemQuery messagesQuery, ItemQuery localesQuery,
			ItemApplication application) {

		application.setResourceLoader(this);
		this.setI18nMessagesQuery(messagesQuery);
		this.setItemLocalesQuery(localesQuery);

		Session session = aSessionFactory.openSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// recupera todos los mensajes internacionalizados

			String aQueryString = this.getI18nMessagesQuery().getQueryString();
			Query query = session.createQuery(aQueryString);
			List list = query.list();

			Iterator iterator = list.iterator();

			I18NMessage message = null;

			HashMap messagesMap = new HashMap();
			while (iterator.hasNext()) {

				message = (I18NMessage) iterator.next();

				if (messagesMap.get(message.locale) == null) {
					messagesMap.put(message.locale, new HashMap());
				}

				((Map) messagesMap.get(message.locale)).put(message.messageKey,
						message.message);
			}
			this.setMessages(messagesMap);

			// recupera las instancias de los locales soportados

			aQueryString = this.getItemLocalesQuery().getQueryString();
			Query queryLocales = session.createQuery(aQueryString);
			List listLocales = queryLocales.list();

			Iterator<ItemLocale> iteratorLocales = listLocales.iterator();
			ItemLocale aLocale = null;
			Collection<Locale> supportedLocales = new HashSet<Locale>();
			while (iteratorLocales.hasNext()) {
				aLocale = iteratorLocales.next();

				supportedLocales.add(new Locale(aLocale.getLanguageCode()));
			}

			this.setSupportedLocales(supportedLocales);

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

			if (tx != null)
				tx.rollback();
			session.close();
		}
		session.disconnect();

	}

	/**
	 * Recupera un mensaje internacionalizado del diccionario de mensajes.
	 * 
	 * @param aClass
	 *            es la clase a la que est� asociado el mensaje.
	 * @param key
	 *            es la clave del mensaje que se est� buscando.
	 * @param aLocale
	 *            es el locale para el cual el mensaje se cre�.
	 * @param aStyle
	 *            es el estilo que se debe aplicar al mensaje.
	 * @return el mensaje correspondiente a los par�metros recibidos.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String loadStringResource(Class aClass, String key, Locale aLocale,
			String aStyle) {

		String result;
		try {

			result = ((Map) this.getMessages().get(aLocale.toString()))
					.get(key).toString();
		} catch (Exception e) {
			// System.out.println("clave no encontrada " + key);

			result = key;
		}

		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return el diccionario de mensajes.
	 */
	@SuppressWarnings("unchecked")
	private Map getMessages() {
		return this.messages;
	}

	/**
	 * Setter.
	 * 
	 * @param aMap
	 *            es el diccionario con los mensajes.
	 */
	@SuppressWarnings("unchecked")
	private void setMessages(Map aMap) {
		this.messages = aMap;
	}

	/**
	 * Recupera los mensajes internacionalizados.
	 * 
	 * @param aComponent
	 *            es el componente al cual se asocia el mensaje.
	 * @param key
	 *            es la clave del mensaje que se est� buscando.
	 * @return el mensaje correspondiente.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String loadStringResource(Component aComponent, String key) {

		String result;
		try {
			result = ((Map) messages.get(aComponent.getLocale().toString()))
					.get(key).toString();
		} catch (Exception e) {
			// System.out.println("clave no encontrada " + key);

			result = key;
		}

		return result;

	}

	/**
	 * Getter.
	 * 
	 * @return un objeto que representa la consulta que debe ejecutarse para
	 *         poder recuperar todos los mensajes internacionalizados.
	 */
	public ItemQuery getI18nMessagesQuery() {
		return this.i18nMessagesQuery;
	}

	/**
	 * Setter.
	 * 
	 * @param aQuery
	 *            es un objeto que representa la consulta que debe ejecutarse
	 *            para poder recuperar todos los mensajes internacionalizados.
	 */
	public void setI18nMessagesQuery(ItemQuery aQuery) {
		this.i18nMessagesQuery = aQuery;
	}

	/**
	 * Getter.
	 * 
	 * @return un objeto que representa la consulta que debe ejecutarse para
	 *         poder recuperar todas las nacionalidades soportadas por la
	 *         aplicaci�n.
	 */
	public ItemQuery getItemLocalesQuery() {
		return this.itemLocalesQuery;
	}

	/**
	 * Setter.
	 * 
	 * @param itemLocalesQuery
	 *            es un objeto que representa la consulta que debe ejecutarse
	 *            para poder recuperar todas las nacionalidades soportadas.
	 */
	public void setItemLocalesQuery(ItemQuery itemLocalesQuery) {
		this.itemLocalesQuery = itemLocalesQuery;
	}

	/**
	 * Getter.
	 * 
	 * @return una colecci�n que contiene todos los locales soportados por esta
	 *         aplicaci�n.
	 */
	public Collection<Locale> getSuppportedLocales() {
		return this.supportedLocales;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colecci�n que contiene todos los locales que van a ser
	 *            soportados por la aplicaci�n.
	 */
	public void setSupportedLocales(Collection<Locale> aCollection) {
		this.supportedLocales = aCollection;

	}

}
