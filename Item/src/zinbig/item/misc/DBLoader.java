/**
 * Este paquete contiene clases útiles del sistema.
 * 
 * 
 */
package zinbig.item.misc;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import zinbig.item.util.i18n.I18NMessage;

/**
 * Las instancias de esta clase se utilizan para generar el esquema de la base
 * de datos y cargar los objetos iniciales.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DBLoader {

	/**
	 * Es el objeto que permite crear sesiones de trabajo con hibernate.
	 */
	private static SessionFactory sessionFactory;

	/**
	 * Constructor.
	 */
	public DBLoader() {
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

			new SchemaExport(cfg).drop(true, true);

			SchemaExport se = new SchemaExport(cfg);

			se.create(true, true);

			sessionFactory = cfg.buildSessionFactory();

			System.out.println("------------------------ DONE.------------------------");
			createObjects();

			System.out.println("------------------------FINISH.------------------------");

		} catch (Exception e) {
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

		Session session = sessionFactory.openSession();
		I18NMessage message = new I18NMessage();
		message.setLocale("es");
		message.setMessage("un mensaje");
		message.setMessageKey("unaClave");
		message.setOid("11");

		Transaction tx = null;
		try {

			tx = session.beginTransaction();
			session.save(message);

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
