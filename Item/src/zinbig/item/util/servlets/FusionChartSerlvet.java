/**
 * Este paquete contiene las implementaciones de los servlets de apoyo que se
 * utilizan para generar información de manera asincrónica para la herramienta.
 */
package zinbig.item.util.servlets;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import zinbig.item.util.i18n.ItemStringResourceLoader;
import zinbig.item.util.spring.ItemApplicationContext;

/**
 * Servlet implementation class FusionChartSerlvet
 */
public class FusionChartSerlvet extends HttpServlet {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -232400264747788429L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FusionChartSerlvet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter writer = response.getWriter();

		try {
			// recupero el oid del proyecto y el tipo de gráfico que se está
			// solicitando.
			// el primer caracter del proyecto representa que tipo de gráfico se
			// requiere.
			String projectOid = request.getParameter("projectOid");
			char requestedGraph = projectOid.charAt(0);
			projectOid = projectOid.substring(1);
			String result = "";

			// recupera todos los objetos requeridos para interactuar con la
			// base de datos.
			ApplicationContext aContext = ItemApplicationContext
					.getApplicationContext();
			SessionFactory aSessionFactory = (SessionFactory) aContext
					.getBean("sessionFactory");
			Session aSession = aSessionFactory.openSession();

			Locale aLocale = new Locale(request.getLocale().getLanguage());

			ItemStringResourceLoader resourceLoader = (ItemStringResourceLoader) aContext
					.getBean("itemStringResourceLoader");

			switch (requestedGraph) {
			case 'A':
				result = this.createGraphForTodayItems(projectOid,
						resourceLoader, aLocale, aSession);
				break;

			case 'B':
				result = this.createGraphByPriority(projectOid, resourceLoader,
						aLocale, aSession);
				break;

			case 'C':
				result = this.createGraphByNodes(projectOid, resourceLoader,
						aLocale, aSession);
				break;

			case 'D':
				result = this.createGraphForProgression(projectOid,
						resourceLoader, aLocale, aSession);
				break;
			default:
				break;
			}

			writer.write(result);
			writer.flush();
			writer.close();
			aSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Crea un gráfico para mostrar los ítems creados en el día de hoy.
	 * 
	 * @param aProjectOid
	 *            es el oid del proyecto para consultar los datos.
	 * @param aLoader
	 *            es el objeto que permite internacionalizar los mensajes.
	 * @param aLocale
	 *            es el objeto utilizado para identificar el idioma del usuario.
	 * @param aSession
	 *            es la sesión de hibernate que se utiliza para acceder a la
	 *            base de datos.
	 * @return un string que representa el XML que requiere el componente Flash
	 *         para mostrar el gráfico.
	 */
	@SuppressWarnings("unchecked")
	private String createGraphForTodayItems(String aProjectOid,
			ItemStringResourceLoader aLoader, Locale aLocale, Session aSession)
			throws Exception {

		StringBuffer buffer = new StringBuffer("");
		String caption = aLoader.loadStringResource(null, "todayItemsCaption",
				aLocale, "");
		String xAxis = aLoader.loadStringResource(null, "todayItemsXAxis",
				aLocale, "");
		String yAxis = aLoader.loadStringResource(null, "todayItemsYAxis",
				aLocale, "");
		buffer.append("<graph caption=\"" + caption + "\" xAxisName=\"" + xAxis
				+ "\" yAxisName=\"" + yAxis
				+ "\" decimalPrecision=\"0\" formatNumberScale=\"0\">");

		Query aQuery = aSession
				.createQuery("select count(item),item.itemType.title from "
						+ "zinbig.item.model.projects.Project p join p.items item "
						+ "where p.id=:aProjectOid and "
						+ "year(item.creationDate)=year(current_date) and "
						+ "month(item.creationDate)=month(current_date) "
						+ "group by item.itemType.title");

		aQuery.setParameter("aProjectOid", aProjectOid);
		ArrayList<Object> result = (ArrayList<Object>) aQuery.list();
		for (Object o : result) {

			buffer.append("<set name=\"" + ((Object[]) o)[1] + "\" value=\""
					+ ((Object[]) o)[0] + "\" color=\""
					+ Integer.toHexString(this.getColor().getRGB()) + "\"/>");

		}

		buffer.append("</graph>");

		return buffer.toString();
	}

	/**
	 * Crea un gráfico para mostrar los ítems en un gráfico de torta por
	 * prioridad.
	 * 
	 * @param aProjectOid
	 *            es el oid del proyecto para consultar los datos.
	 * @param aLoader
	 *            es el objeto que permite internacionalizar los mensajes.
	 * @param aLocale
	 *            es el objeto utilizado para identificar el idioma del usuario.
	 * @param aSession
	 *            es la sesión de trabajo de hibernate utilizada para acceder a
	 *            la base.
	 * @return un string que representa el XML que requiere el componente Flash
	 *         para mostrar el gráfico.
	 */
	@SuppressWarnings("unchecked")
	private String createGraphByPriority(String aProjectOid,
			ItemStringResourceLoader aLoader, Locale aLocale, Session aSession)
			throws Exception {
		StringBuffer buffer = new StringBuffer("");

		String caption = aLoader.loadStringResource(null,
				"itemsByPriorityCaption", aLocale, "");

		buffer.append("<graph caption=\"" + caption
				+ "\" showNames=\"1\" decimalPrecision=\"0\">");

		Query aQuery = aSession
				.createQuery("select count(item),item.priority.title from zinbig.item.model.projects.Project p join p.items item "
						+ "where p.id=:aProjectOid group by item.priority.title");

		aQuery.setParameter("aProjectOid", aProjectOid);
		ArrayList<Object> result = (ArrayList<Object>) aQuery.list();
		for (Object o : result) {

			buffer.append("<set name=\"" + ((Object[]) o)[1] + "\" value=\""
					+ ((Object[]) o)[0] + "\" color=\""
					+ Integer.toHexString(this.getColor().getRGB()) + "\"/>");

		}

		buffer.append("</graph>");

		return buffer.toString();
	}

	/**
	 * Crea un gráfico para mostrar los ítems por su estado del workflow.
	 * 
	 * @param aProjectOid
	 *            es el oid del proyecto para consultar los datos.
	 * @param aLoader
	 *            es el objeto que permite internacionalizar los mensajes.
	 * @param aLocale
	 *            es el objeto utilizado para identificar el idioma del usuario.
	 * @param aSession
	 *            es la sesión de trabajo de hibernate utilizada para acceder a
	 *            la base.
	 * @return un string que representa el XML que requiere el componente Flash
	 *         para mostrar el gráfico.
	 */
	@SuppressWarnings("unchecked")
	private String createGraphByNodes(String aProjectOid,
			ItemStringResourceLoader aLoader, Locale aLocale, Session aSession)
			throws Exception {

		StringBuffer buffer = new StringBuffer("");
		String caption = aLoader.loadStringResource(null, "itemsByNodeCaption",
				aLocale, "");
		String xAxis = aLoader.loadStringResource(null, "nodeItemsXAxis",
				aLocale, "");
		String yAxis = aLoader.loadStringResource(null, "nodeItemsYAxis",
				aLocale, "");
		buffer.append("<graph caption=\"" + caption + "\" xAxisName=\"" + xAxis
				+ "\" yAxisName=\"" + yAxis
				+ "\" decimalPrecision=\"0\" formatNumberScale=\"0\">");

		Query aQuery = aSession
				.createQuery("select count(item),item.currentWorkflowNode.title from zinbig.item.model.projects.Project p join p.items item  "
						+ "where p.id=:aProjectOid group by item.currentWorkflowNode.title");

		aQuery.setParameter("aProjectOid", aProjectOid);
		ArrayList<Object> result = (ArrayList<Object>) aQuery.list();

		for (Object o : result) {

			buffer.append("<set name=\"" + ((Object[]) o)[1] + "\" value=\""
					+ ((Object[]) o)[0] + "\" color=\""
					+ Integer.toHexString(this.getColor().getRGB()) + "\"/>");

		}

		buffer.append("</graph>");

		return buffer.toString();
	}

	/**
	 * Crea un gráfico para mostrar los ítems creados y cerrados por mes y año.
	 * El gráfico muestra la información acumulada.
	 * 
	 * @param aProjectOid
	 *            es el oid del proyecto para consultar los datos.
	 * @param aLoader
	 *            es el objeto que permite internacionalizar los mensajes.
	 * @param aLocale
	 *            es el objeto utilizado para identificar el idioma del usuario.
	 * @param aSession
	 *            es la sesión de trabajo de hibernate utilizada para acceder a
	 *            la base.
	 * @return un string que representa el XML que requiere el componente Flash
	 *         para mostrar el gráfico.
	 */
	@SuppressWarnings("unchecked")
	private String createGraphForProgression(String aProjectOid,
			ItemStringResourceLoader aLoader, Locale aLocale, Session aSession)
			throws Exception {

		StringBuffer buffer = new StringBuffer("");
		String caption = aLoader.loadStringResource(null,
				"itemsProgressionCaption", aLocale, "");
		String firstAreaCaption = aLoader.loadStringResource(null,
				"itemsProgressionFirstArea", aLocale, "");
		String secondAreaCaption = aLoader.loadStringResource(null,
				"itemsProgressionSecondArea", aLocale, "");
		buffer
				.append("<graph caption=\""
						+ caption
						+ "\" divlinecolor=\"F47E00\" numdivlines=\"4\" showAreaBorder=\"1\" areaBorderColor=\"000000\" showNames=\"1\" vDivLineAlpha=\"30\" formatNumberScale=\"1\" rotateNames=\"1\" decimalPrecision=\"0\">");

		StringBuffer categories = new StringBuffer("<categories>");
		StringBuffer firstArea = new StringBuffer(
				"<dataset seriesname=\""
						+ firstAreaCaption
						+ "\" color=\"FF5904\" showValues=\"0\" areaAlpha=\"50\" showAreaBorder=\"1\" areaBorderThickness=\"2\" areaBorderColor=\"FF0000\">");
		StringBuffer secondArea = new StringBuffer(
				"<dataset seriesname=\""
						+ secondAreaCaption
						+ "\" color=\"99cc99\" showValues=\"0\" areaAlpha=\"50\" showAreaBorder=\"1\" areaBorderThickness=\"2\" areaBorderColor=\"006600\">");

		Query aQuery = aSession
				.createQuery("select count(item), year(item.creationDate), month(item.creationDate), item.finished from zinbig.item.model.projects.Project p join p.items item  "
						+ "where p.id=:aProjectOid group by year(item.creationDate),month(item.creationDate),item.finished");
		aQuery.setParameter("aProjectOid", aProjectOid);
		ArrayList<Object> result = (ArrayList<Object>) aQuery.list();

		// crea un comparador para ordenar correctamente los resultados
		// considerando sus fechas.
		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String y1 = o1.substring(0, 4);
				String y2 = o2.substring(0, 4);
				String m1 = o1.substring(4);
				String m2 = o2.substring(4);

				int result = new Integer(y1).compareTo(new Integer(y2));
				if (result == 0) {
					result = new Integer(m1).compareTo(new Integer(m2));
				}

				return result;
			}
		};

		Map<String, Integer> created = new TreeMap<String, Integer>(comparator);
		Map<String, Integer> finished = new TreeMap<String, Integer>(comparator);

		String yearAndMonth = "";
		for (Object o : result) {
			yearAndMonth = ((Object[]) o)[1].toString()
					+ ((Object[]) o)[2].toString();
			if (!created.containsKey(yearAndMonth)) {
				created.put(yearAndMonth, new Integer(0));
			}
			created.put(yearAndMonth, created.get(yearAndMonth).intValue()
					+ new Integer(((Object[]) o)[0].toString()).intValue());
			if (!finished.containsKey(yearAndMonth)) {
				finished.put(yearAndMonth, new Integer(0));
			}
			if (new Boolean(((Object[]) o)[3].toString()).booleanValue()) {
				finished.put(yearAndMonth, finished.get(yearAndMonth)
						.intValue()
						+ new Integer(((Object[]) o)[0].toString()).intValue());
			}
		}
		int finishedAcum = 0;
		int createdAcum = 0;
		Iterator<Map.Entry<String, Integer>> iterator = created.entrySet()
				.iterator();
		Map.Entry<String, Integer> entry = null;
		while (iterator.hasNext()) {
			entry = iterator.next();
			categories.append("<category name=\"" + entry.getKey() + "\" />");
			createdAcum = createdAcum + entry.getValue().intValue();
			finishedAcum = finishedAcum
					+ finished.get(entry.getKey()).intValue();
			firstArea.append("<set value=\"" + createdAcum + "\"/>");
			secondArea.append("<set value=\"" + finishedAcum + "\"/>");

		}

		categories.append("</categories>");
		buffer.append(categories);
		firstArea.append("</dataset>");
		secondArea.append("</dataset>");
		buffer.append(firstArea);
		buffer.append(secondArea);
		buffer.append("</graph>");

		return buffer.toString();
	}

	/**
	 * Recupera un color aleatorio para los gráficos.
	 * 
	 * @return
	 */
	private Color getColor() {
		Random rand = new Random();
		int r = rand.nextInt(254);
		int g = rand.nextInt(254);
		int b = rand.nextInt(254);
		return new Color(r, g, b);
	}
}
