/**
 * Este paquete contiene clases útiles del sistema.
 * 
 * 
 */
package zinbig.item.misc;

import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Las instancias de esta clase se utilizan para generar el esquema de la base
 * de datos y cargar los objetos iniciales.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DBLoader2 {

	/**
	 * Es el objeto que permite crear sesiones de trabajo con hibernate.
	 */
	private static SessionFactory sessions;

	/**
	 * Constructor.
	 */
	public DBLoader2() {
	}

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
			System.out.println("eeeeeeeeeeeeeeeeee" + cfg.getProperties());

			System.out
					.println("------------------------ DONE.------------------------");
			createObjects();

			System.out
					.println("------------------------FINISH.------------------------");

		} catch (Exception e) {
			System.out
					.println("------------------------FAIL.------------------------");
			e.printStackTrace();
		}

	}

	/**
	 * Crea los objetos de la base de datos.
	 * 
	 * @throws HibernateException
	 *             esta excepción se puede levantar cuando hay un error con
	 *             hibernate.
	 */

	public static void createObjects() throws HibernateException {

		Session session = sessions.openSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			// Query query = session
			// .createQuery("SELECT p FROM zinbig.item.model.projects.Project p  ");

			Query query = session
					.createQuery("SELECT pref FROM zinbig.item.model.users.User u join u.userPreferences pref where u.username='admin' and index(pref)='FAVORITE_PROJECT' ");

			String result = query.list().toString();
			StringTokenizer sb = new StringTokenizer(result.substring(1, result
					.length() - 1), ",");
			while (sb.hasMoreTokens()) {
				System.out.println("token " + sb.nextToken());
			}
			System.out.println("cantidad de objetos en la base " + result);

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			session.close();
		}
		session.disconnect();

	}
}
