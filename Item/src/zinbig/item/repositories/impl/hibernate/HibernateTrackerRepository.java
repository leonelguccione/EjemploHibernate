/**
 * Este paquete contiene las implementaciones de los repositorios para 
 * recuperar en forma eficiente los objetos del modelo. Las implementaciones
 * utilizan Hibernate para acceder al modelo persistente.
 * 
 */
package zinbig.item.repositories.impl.hibernate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import zinbig.item.model.Tracker;
import zinbig.item.model.exceptions.TrackerUnknownException;
import zinbig.item.repositories.bi.TrackerRepositoryBI;
import zinbig.item.util.Constants;
import zinbig.item.util.ItemStatistic;

/**
 * Esta clase implementa un repositorio para acceder a la raíz del sistema de
 * manera eficiente a través de Hibernate.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class HibernateTrackerRepository extends HibernateBaseRepository
		implements TrackerRepositoryBI {

	/**
	 * Recupera el tracker con el oid dado.
	 * 
	 * @param anOid
	 *            es el oid del tracker que se está buscando.
	 * @return el tracker con el nombre dado.
	 * @throws TrackerUnknownException
	 *             esta excepción puede levantarse en caso de tratar de
	 *             recuperar un tracker que no existe.
	 */
	public Tracker findById(String anId) throws TrackerUnknownException {

		Tracker manager = (Tracker) this.findById(Tracker.class, "1");

		return manager;

	}

	/**
	 * Retorna un diccionario que contiene estadísticas sobre el sistema y su
	 * utilización.<br>
	 * Estas estadísticas son calculadas en forma periódica por un proceso batch
	 * que corre en forma asincrónica.
	 * 
	 * @return un diccionario con una estadística para cada clave.
	 */

	@SuppressWarnings("unchecked")
	public Map<String, Object> getStatitics() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		// carga datos vacíos para el caso inicial
		result.put(Constants.UPDATE_TIME, "");
		result.put(Constants.ITEMS_COUNT, new Long(0));
		result.put(Constants.USERS_COUNT, new Long(0));
		result.put(Constants.PROJECTS_COUNT, new Long(0));
		result.put(Constants.OPEN_ITEMS_COUNT, new Long(0));
		result.put(Constants.PUBLIC_PROJECTS_COUNT, new Long(0));
		result.put(Constants.MOST_ACTIVE_PROJECT_NAME, "");
		result.put(Constants.MOST_ACTIVE_PROJECT_OID, "");
		result.put(Constants.MOST_ACTIVE_USER_1, "");
		result.put(Constants.MOST_ACTIVE_USER_2, "");
		result.put(Constants.MOST_ACTIVE_USER_3, "");
		result.put(Constants.MOST_INTERESTING_ITEM_ID, "");
		result.put(Constants.MOST_INTERESTING_ITEM_OID, "");

		Session session = this.getSession();
		Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			Query aQuery = session
					.createQuery("SELECT s FROM zinbig.item.util.ItemStatistic s ORDER BY update_time  DESC");
			Collection<ItemStatistic> statistics = aQuery.list();

			if (!statistics.isEmpty()) {
				ItemStatistic anStatistic = statistics.iterator().next();

				String updateTime = formatter.format(anStatistic
						.getUpdateTime());
				Long itemsCount = anStatistic.getItemsCount();
				Long usersCount = anStatistic.getUsersCount();
				Long projectsCount = anStatistic.getProjectsCount();
				Long openItemsCount = anStatistic.getOpenItemsCount();
				Long publicProjectsCount = anStatistic.getPublicProjectsCount();
				String mostActiveProjectName = anStatistic
						.getMostActiveProjectName();
				String mostActiveProjectOID = anStatistic
						.getMostActiveProjectOid();
				String mostActiveUser1 = anStatistic.getFirstMostActiveUser();
				String mostActiveUser2 = anStatistic.getSecondMostActiveUser();
				String mostActiveUser3 = anStatistic.getThirdMostActiveUser();
				String mostInterestingItemId = anStatistic
						.getMostInterestingItemId();
				String mostInterestingItemOid = anStatistic
						.getMostInterestingItemOid();

				result.put(Constants.UPDATE_TIME, updateTime);
				result.put(Constants.ITEMS_COUNT, itemsCount);
				result.put(Constants.USERS_COUNT, usersCount);
				result.put(Constants.PROJECTS_COUNT, projectsCount);
				result.put(Constants.OPEN_ITEMS_COUNT, openItemsCount);
				result
						.put(Constants.PUBLIC_PROJECTS_COUNT,
								publicProjectsCount);
				result.put(Constants.MOST_ACTIVE_PROJECT_NAME,
						mostActiveProjectName);
				result.put(Constants.MOST_ACTIVE_PROJECT_OID,
						mostActiveProjectOID);
				result.put(Constants.MOST_ACTIVE_USER_1, mostActiveUser1);
				result.put(Constants.MOST_ACTIVE_USER_2, mostActiveUser2);
				result.put(Constants.MOST_ACTIVE_USER_3, mostActiveUser3);
				result.put(Constants.MOST_INTERESTING_ITEM_ID,
						mostInterestingItemId);
				result.put(Constants.MOST_INTERESTING_ITEM_OID,
						mostInterestingItemOid);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Recupera la única instancia de clase Tracker, que actúa como raíz del
	 * modelo persistido.<br>
	 * En el caso de Hibernate, la raíz del sistema siempre toda el oid=1.
	 * 
	 * @throws una
	 *             excepción en caso de no hallar al tracker.
	 * @return la raíz del modelo.
	 */
	@Override
	public Tracker findTracker() throws TrackerUnknownException {
		return this.findById("1");
	}

}
