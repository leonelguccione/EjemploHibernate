/**
 * Este paquete contiene las clases que componen la aplicación Item.<br>
 * Este desarrollo se basa en el framework web Wicket y utiliza Spring para la 
 * mayoría de las configuraciones.
 * 
 */
package zinbig.item.application;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.SpringWebApplication;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;

import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.util.SystemProperty;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.i18n.ItemStringResourceLoader;
import zinbig.item.util.persistence.ItemQuery;
import zinbig.item.util.spring.ItemApplicationContext;

/**
 * Esta clase representa la aplicación Wicket y provee algunos métodos útiles a
 * los diferentes componentes de la aplicación como componentes y/o páginas.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
@SuppressWarnings("deprecation")
public class ItemApplication extends SpringWebApplication implements
		Serializable {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 8428888216722701231L;

	/**
	 * Mantiene información acerca de las propiedades del sistema.<br>
	 * Este diccionario contiene propiedades que pueden ser editadas por el
	 * administrador y que lugo deben ser almacenadas en la base de datos.
	 */
	protected Map<String, String> systemProperties;

	/**
	 * Es una colección que contiene los nombres de las clases que representan
	 * las diferentes estrategias de asignación de primer responsable de un ítem
	 * recientemente creado.<br>
	 * Esta colección se inyecta directamente mediante Spring. Los valores se
	 * encuentran definidos en el archivo applicationContext.xml.
	 */
	protected Collection<String> itemAssignmentStrategies;

	/**
	 * Es un objeto que se utiliza para recuperar todos los strings que se
	 * utilizarán para internacionalizar todos los mensajes y componentes de la
	 * itnerfaz gráfica.
	 */
	protected IStringResourceLoader resourceLoader;

	/**
	 * Contructor.
	 */
	public ItemApplication() {
		this.setSystemProperties(new HashMap<String, String>());
		this.setItemAssignmentStrategies(new ArrayList<String>());

	}

	/**
	 * Getter.
	 * 
	 * @return la clase de la página que debe ser utilizada como home page.
	 */
	@Override
	public Class<? extends BasePage> getHomePage() {

		return DashboardPage.class;

	}

	/**
	 * Inicializa la aplicación.
	 * 
	 */
	@Override
	public void init() {
		super.init();

		// carga todos los mensajes para la internacionalización
		this.getResourceSettings().addStringResourceLoader(1,
				this.getResourceLoader());

		// carga todas las propiedades de sistema
		this.loadSystemProperties();

		this.getApplicationSettings().setPageExpiredErrorPage(
				DashboardPage.class);
		this.getDebugSettings().setAjaxDebugModeEnabled(false);
		// this.getApplicationSettings().setInternalErrorPage(ErrorPage.class);
		this.getExceptionSettings().setUnexpectedExceptionDisplay(
				IExceptionSettings.SHOW_EXCEPTION_PAGE);
		this.getResourceSettings().setThrowExceptionOnMissingResource(false);

	}

	/**
	 * Crea una nueva sesión.
	 * 
	 * @param request
	 *            es el request web enviado a la aplicación web.
	 * @param response
	 *            es el response que se enviará al cliente.
	 * 
	 * @return una nueva sesión web para el usuario.
	 */
	@Override
	public Session newSession(Request request, Response response) {

		return new ItemSession(request, response);
	}

	/**
	 * Carga todas las propiedades de sistema en un mapa para que éstas estén
	 * disponibles sin tener que acceder a la base de datos.
	 */
	@SuppressWarnings("unchecked")
	private void loadSystemProperties() {

		ApplicationContext aContext = ItemApplicationContext
				.getApplicationContext();
		SessionFactory aSessionFactory = (SessionFactory) aContext
				.getBean("sessionFactory");
		org.hibernate.Session session = aSessionFactory.openSession();

		Transaction tx = null;
		try {
			ItemQuery itemQuery = (ItemQuery) aContext
					.getBean("systemPropertiesQuery");
			tx = session.beginTransaction();

			Query query = session.createQuery(itemQuery.getQueryString());
			List list = query.list();

			Iterator<SystemProperty> iterator = list.iterator();

			SystemProperty aProperty = null;

			HashMap<String, String> aMap = new HashMap<String, String>();
			while (iterator.hasNext()) {

				aProperty = iterator.next();

				aMap.put(aProperty.getPropertyName(), aProperty
						.getPropertyValue());
			}
			this.setSystemProperties(aMap);

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
	 * Getter.
	 * 
	 * @return un diccionario con las propiedades del sistema.
	 */
	protected Map<String, String> getSystemProperties() {
		return this.systemProperties;
	}

	/**
	 * Setter.
	 * 
	 * @param someProperties
	 *            es un diccionario con las propiedades del sistema.
	 */
	public void setSystemProperties(Map<String, String> someProperties) {
		this.systemProperties = someProperties;
	}

	/**
	 * Recupera el valor de una propiedad de sistema.
	 * 
	 * @param aName
	 *            es el nombre de la propiedad de sistema.
	 * @return el valor de la propiedad.
	 */
	public String getSystemProperty(String aName) {
		return this.getSystemProperties().get(aName);
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los nombres de las clases que
	 *         representan las diferentes estrategias de asignación de primer
	 *         responsable de un ítem recientemente creado.
	 */
	public Collection<String> getItemAssignmentStrategies() {
		return this.itemAssignmentStrategies;
	}

	/**
	 * Setter.
	 * 
	 * @param aCollection
	 *            es una colección que contiene los nombres de las clases que
	 *            representan las diferentes estrategias de asignación de primer
	 *            responsable de un ítem recientemente creado.
	 */
	public void setItemAssignmentStrategies(Collection<String> aCollection) {
		this.itemAssignmentStrategies = aCollection;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene todos los estados disponibles como
	 *         estado inicial de un nuevo ítem.<br>
	 *         Los estados posibles son ItemStateEnum.CREATED en caso de que se
	 *         quiera que el nuevo ítem no circule por el workflow del proyecto;
	 *         ItemStateEnum.OPEN en caso de que se quiera que el ítem comience
	 *         a transitar el workflow. En este último caso el nodo del ítem
	 *         será el nodo inicial del workflow.
	 */
	public Collection<ItemStateEnum> getAvailableStatesForNewItem() {
		ArrayList<ItemStateEnum> list = new ArrayList<ItemStateEnum>();

		list.add(ItemStateEnum.CREATED);
		list.add(ItemStateEnum.OPEN);

		return list;
	}

	/**
	 * Getter.
	 * 
	 * @return un objeto que se utiliza para recuperar los strings que se
	 *         utilziarán para mostrar los mensajes y el contenido de los
	 *         componentes.
	 */
	public IStringResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}

	/**
	 * Setter.
	 * 
	 * @param aResourceLoader
	 *            es un objeto que se utiliza para recuperar los strings que se
	 *            utilziarán para mostrar los mensajes y el contenido de los
	 *            componentes.
	 */
	public void setResourceLoader(IStringResourceLoader aResourceLoader) {
		this.resourceLoader = aResourceLoader;
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene todos los locales soportados por esta
	 *         aplicación.
	 */
	public Collection<Locale> getSuppportedLocales() {
		return ((ItemStringResourceLoader) this.getResourceLoader())
				.getSuppportedLocales();
	}

	/**
	 * Getter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem para el cual se está buscando
	 *            el path para sus adjuntos.
	 * @return el nombre completo de la ruta al archivo recibido.
	 */
	public String getPathForItem(ItemDTO anItemDTO) {
		String aPath = this.getServletContext().getRealPath("");

		return aPath + File.separator + anItemDTO.getProject().getShortName()
				+ File.separator + "attachedFiles" + File.separator
				+ anItemDTO.getId();
	}

	/**
	 * Getter.
	 * 
	 * @param aProjectName
	 *            es el nombre del proyecto para el cual se debe retornar el
	 *            path de los adjuntos.
	 * @return el nombre completo de la ruta al archivo recibido.
	 */
	public String getPathForProject(String aProjectName) {
		String aPath = this.getServletContext().getRealPath("");

		return aPath + File.separator + aProjectName + File.separator
				+ "attachedFiles" + File.separator;
	}

	/**
	 * Getter.
	 * 
	 * @param aFilename
	 *            es el nombre del archivo para el cual se está buscando el
	 *            path.
	 * @return el nombre completo de la ruta al archivo recibido.
	 */
	public String getPath(String aFilename) {
		String aPath = this.getServletContext().getRealPath(aFilename);

		return aPath;
	}
}
