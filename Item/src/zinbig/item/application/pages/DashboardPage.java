/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import zinbig.item.repositories.RepositoryLocator;
import zinbig.item.repositories.bi.TrackerRepositoryBI;
import zinbig.item.util.Constants;

/**
 * Las instancias de esta página se utilizan para presentar información general
 * del estado de todos los proyectos e ítems administrados por la herramienta.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class DashboardPage extends BasePage {

	/**
	 * Constructor.<br>
	 * 
	 */
	public DashboardPage() {
		super();

		try {
			TrackerRepositoryBI repository = RepositoryLocator.getInstance()
					.getTrackerRepository();
			Map<String, Object> stats = repository.getStatitics();
			// construye el label para la fecha de actualización
			final Label updateTimeLabel = new Label("updateTime",
					(String) stats.get(Constants.UPDATE_TIME));
			this.add(updateTimeLabel);

			// construye el label para la cantidad de ítems
			final Label itemsCountLabel = new Label("itemsCount", ((Long) stats
					.get(Constants.ITEMS_COUNT)).toString());
			this.add(itemsCountLabel);

			// construye el label para la cantidad de usuarios
			final Label usersCountLabel = new Label("usersCount", ((Long) stats
					.get(Constants.USERS_COUNT)).toString());
			this.add(usersCountLabel);

			// construye el label para la cantidad de proyectos
			final Label projectsCountLabel = new Label("projectsCount",
					((Long) stats.get(Constants.PROJECTS_COUNT)).toString());
			this.add(projectsCountLabel);

			// construye el label para la cantidad de proyectos públicos
			final Label publicProjectsCountLabel = new Label(
					"publicProjectsCount", ((Long) stats
							.get(Constants.PUBLIC_PROJECTS_COUNT)).toString());
			this.add(publicProjectsCountLabel);

			// construye el label para la cantidad de proyectos privados
			final Label privateProjectsCountLabel = new Label(
					"privateProjectsCount", new Integer(((Long) stats
							.get(Constants.PROJECTS_COUNT)).intValue()
							- ((Long) stats
									.get(Constants.PUBLIC_PROJECTS_COUNT))
									.intValue()).toString());
			this.add(privateProjectsCountLabel);

			// construye el label para la cantidad de ítems abiertos
			final Label openItemsCountLabel = new Label("openItemsCount",
					((Long) stats.get(Constants.OPEN_ITEMS_COUNT)).toString());
			this.add(openItemsCountLabel);

			// construye el label para la cantidad de ítems cerrados
			final Label closedItemsCountLabel = new Label("closedItemsCount",
					new Integer(((Long) stats.get(Constants.ITEMS_COUNT))
							.intValue()
							- ((Long) stats.get(Constants.OPEN_ITEMS_COUNT))
									.intValue()).toString());
			this.add(closedItemsCountLabel);

			// agrega el link para el proyecto más activo
			PageParameters pageParameters = new PageParameters();
			pageParameters.put("PROJECT_OID", (String) stats
					.get(Constants.MOST_ACTIVE_PROJECT_OID));
			Link<String> aLink = new BookmarkablePageLink<String>(
					"mostActiveProjectLink", ProjectDashboardPage.class,
					pageParameters);
			String mostActiveProject = (String) stats
					.get(Constants.MOST_ACTIVE_PROJECT_NAME);
			aLink.add(new Label("mostActiveProjectName", mostActiveProject));
			this.add(aLink);

			Label mostActiveProjectLabel = new Label("mostActiveProject", this
					.getString("dashboard.mostActiveProject")
					+ ":");
			mostActiveProjectLabel.setVisible(!mostActiveProject.equals(""));
			this.add(mostActiveProjectLabel);

			// construye el label para el primer usuario más activo
			String mostActiveUser = (String) stats
					.get(Constants.MOST_ACTIVE_USER_1);
			final Label mostActiveUser1 = new Label("mostActiveUser1",
					mostActiveUser);
			this.add(mostActiveUser1);

			Label mostActiveUsersLabel = new Label("mostActiveUsers", this
					.getString("dashboard.mostActiveUsers")
					+ ":");
			mostActiveUsersLabel.setVisible(!mostActiveUser.equals(""));
			this.add(mostActiveUsersLabel);

			// construye el label para el segundo usuario más activo
			final Label mostActiveUser2 = new Label("mostActiveUser2",
					(String) stats.get(Constants.MOST_ACTIVE_USER_2));
			this.add(mostActiveUser2);

			// construye el label para el tercer usuario más activo
			final Label mostActiveUser3 = new Label("mostActiveUser3",
					(String) stats.get(Constants.MOST_ACTIVE_USER_3));
			this.add(mostActiveUser3);

			// agrega el link para el ítem más interesante
			PageParameters anotherPageParameters = new PageParameters();
			anotherPageParameters.put("ITEM_OID", (String) stats
					.get(Constants.MOST_INTERESTING_ITEM_OID));
			Link<String> itemLink = new BookmarkablePageLink<String>(
					"mostInterestingItemLink", ViewItemDetailPage.class,
					anotherPageParameters);
			String mostVotedItem = (String) stats
					.get(Constants.MOST_INTERESTING_ITEM_ID);
			itemLink.add(new Label("mostInterestingItem", mostVotedItem));
			this.add(itemLink);

			Label mostVotedItemLabel = new Label("mostVotedItem", this
					.getString("dashboard.mostVotedItem")
					+ ":");
			mostVotedItemLabel.setVisible(!mostVotedItem.equals(""));
			this.add(mostVotedItemLabel);

		} catch (Exception e) {
			this.setResponsePage(ErrorPage.class);
		}

	}

}
