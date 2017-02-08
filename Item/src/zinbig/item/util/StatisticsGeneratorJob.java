/**
 * Este paquete contiene clases útiles para la herramienta.
 */
package zinbig.item.util;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import zinbig.item.model.Item;
import zinbig.item.model.projects.Project;
import zinbig.item.util.spring.ItemApplicationContext;

/**
 * Las instancias de esta clase se utilizan para generar estadísticas en forma
 * periódica. El resultado de estas consultas se almacena en la base de datos
 * para ser consultado desde la página de inicio.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class StatisticsGeneratorJob implements Job {

	/**
	 * Ejecuta esta tarea.
	 * 
	 * @param aJobContext
	 *            es el contexto de ejecución de esta tarea.
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext aJobContext)
			throws JobExecutionException {

		try {
			ApplicationContext aContext = ItemApplicationContext
					.getApplicationContext();
			SessionFactory aSessionFactory = (SessionFactory) aContext
					.getBean("sessionFactory");

			Session aSession = aSessionFactory.openSession();
			Transaction tx = aSession.beginTransaction();

			// obtengo la cantidad de ítems creados.
			Query aQuery = aSession
					.createQuery("select count(i) from zinbig.item.model.Item i");
			aQuery.setMaxResults(1);
			Long itemsCount = (Long) aQuery.uniqueResult();

			// obtengo la cantidad de usuarios.
			aQuery = aSession
					.createQuery("select count(u) from zinbig.item.model.users.User u");
			aQuery.setMaxResults(1);
			Long usersCount = (Long) aQuery.uniqueResult();

			// obtengo la cantidad de proyectos.
			aQuery = aSession
					.createQuery("select count(p) from zinbig.item.model.projects.Project p");
			aQuery.setMaxResults(1);
			Long projectsCount = (Long) aQuery.uniqueResult();

			// obtengo la cantidad de ítems abiertos (todos los que no están en
			// estado CLOSED(3)).
			aQuery = aSession
					.createQuery("select count(i) from zinbig.item.model.Item i where i.finished=false");
			aQuery.setMaxResults(1);
			Long openItemsCount = (Long) aQuery.uniqueResult();

			// obtengo la cantidad de proyectos públicos.
			aQuery = aSession
					.createQuery("select count(p) from zinbig.item.model.projects.Project p where p.publicProject=true");
			aQuery.setMaxResults(1);
			Long publicProjectsCount = (Long) aQuery.uniqueResult();

			// obtengo el proyecto más activo. Éste queda definido como el
			// proyecto que más ítems y nodos tiene creados.
			aQuery = aSession
					.createQuery("select p from zinbig.item.model.projects.Project p "
							+ "where p.publicProject=true order by p.items.size desc");

			String mostActiveProjectOid = "";
			String mostActiveProjectName = "";
			Collection<Project> projects = aQuery.list();
			if (!projects.isEmpty()) {
				Project aProject = projects.iterator().next();
				mostActiveProjectOid = aProject.getOid();
				mostActiveProjectName = aProject.getName();
			}

			// obtengo los tres usuarios más activos.
			aQuery = aSession
					.createQuery("SELECT item,count(*) FROM zinbig.item.model.Item item group by item.creator order by count(*)");

			String mostActiveUser1 = "";
			String mostActiveUser2 = "";
			String mostActiveUser3 = "";
			Collection<Object[]> activeUsers = aQuery.list();
			Iterator<Object[]> iterator = activeUsers.iterator();
			if (iterator.hasNext()) {
				mostActiveUser1 = ((Item) iterator.next()[0]).getCreator()
						.getUsername();
			}
			if (iterator.hasNext()) {
				mostActiveUser2 = ((Item) iterator.next()[0]).getCreator()
						.getUsername();
			}
			if (iterator.hasNext()) {
				mostActiveUser3 = ((Item) iterator.next()[0]).getCreator()
						.getUsername();
			}

			// obtengo el ítem más interesante. Éste queda definido como el
			// ítem que tiene más observadores
			aQuery = aSession
					.createQuery("SELECT item FROM zinbig.item.model.Item item order by item.observers.size DESC");
			Collection<Item> items = aQuery.list();
			String mostInterestingItemId = "";
			String mostInterestingItemOid = "";
			if (!items.isEmpty()) {
				Item anItem = items.iterator().next();
				mostInterestingItemId = new Integer(anItem.getItemId())
						.toString();
				mostInterestingItemOid = anItem.getOid();
			}

			// obtengo la fecha actual.
			Date aDate = new Date();
			ItemStatistic newStatictic = new ItemStatistic(aDate, itemsCount,
					usersCount, projectsCount, openItemsCount,
					publicProjectsCount, mostActiveProjectName,
					mostActiveProjectOid, mostActiveUser1, mostActiveUser2,
					mostActiveUser3, mostInterestingItemId,
					mostInterestingItemOid);

			aSession.save(newStatictic);

			tx.commit();
			aSession.close();
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
