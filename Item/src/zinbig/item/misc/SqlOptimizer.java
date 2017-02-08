package zinbig.item.misc;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SqlOptimizer {

	/**
	 * Es el objeto que permite crear sesiones de trabajo con hibernate.
	 */
	private static SessionFactory sessions;

	/**
	 * Genera el esquema de la base y carga los datos iniciales.
	 * 
	 * @param args
	 *            son los argumentos pasados como parámetro. Para la ejecución
	 *            de esta clase no se requieren parámetros.
	 */
	public static void main(String[] args) {

		try {

			Configuration cfg = new Configuration();
			cfg.configure();
			sessions = cfg.buildSessionFactory();
			HqlToSqlTranslator translator = new HqlToSqlTranslator();
			translator.setSessionFactory(sessions);
			System.out
					.println("SQL "
							+ translator
									.toSql("select distinct l.finalNodeDescription from zinbig.item.model.workflow.WorkflowLinkDescription l where l.initialNodeDescription.oid =:anOid ORDER BY upper(nodeTitle) ASC"));

		} catch (Exception e) {
			System.out
					.println("------------------------FAIL.------------------------");
			e.printStackTrace();
		}

	}

}
