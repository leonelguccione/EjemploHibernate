package zinbig.item.misc;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Tester {

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

			// new SchemaExport(cfg).drop(true, true);

			// SchemaExport se=new SchemaExport(cfg);
			//
			// se.create(true, true);

			sessions = cfg.buildSessionFactory();
			// System.out.println("eeeeeeeeeeeeeeeeee" + cfg.getProperties());

			System.out.println("------------------------ DONE.------------------------");
			createObjects();

			System.out.println("------------------------FINISH.------------------------");

		} catch (Exception e) {
			System.out.println("------------------------FAIL.------------------------");
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
			long inicio = new Date().getTime();
			Query aQuery = session.createQuery("SELECT t FROM zinbig.item.model.Tracker t fetch all properties ");

			System.out.println("consulta de filtro " + aQuery.list());
			long fin = new Date().getTime();
			System.out.println("tiempo " + (fin - inicio));
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
