/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.apache.wicket.markup.html.WebPage;

import zinbig.item.application.ItemApplication;
import zinbig.item.application.ItemSession;
import zinbig.item.application.components.DefaultProjectPanel;
import zinbig.item.application.components.MenuPanel;
import zinbig.item.application.components.SubmenuPanel;
import zinbig.item.application.components.UserPanel;
import zinbig.item.application.components.itemsfilter.SearchItemPanel;
import zinbig.item.services.ServiceLocator;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.services.bi.ProjectsServiceBI;
import zinbig.item.services.bi.UsersServiceBI;
import zinbig.item.util.dto.OperationDTO;
import zinbig.item.util.dto.ProjectDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Esta clase es la base de la jerarquía de páginas. <br>
 * En el constructor se valida que el locale utilizado por el cliente esté
 * soportado por la aplicación. En caso de no ser así se utiliza por defecto el
 * locale "es". <br>
 * Esta página además contiene la definición del footer, para que sea igual en
 * todas las páginas que extiendan de esta (ver la página html).
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public abstract class BasePage extends WebPage implements Serializable {

	/**
	 * Constructor por defecto.
	 * 
	 * Este constructor verifica que el locale utilizado por el cliente sea
	 * efectivamente soportado por la aplicación. En caso contrario el locale
	 * por defecto que se utiliza es "es".
	 */
	public BasePage() {
		try {
			Locale aLocale = this.getLocale();

			ItemApplication application = (ItemApplication) this
					.getApplication();

			if (!application.getSuppportedLocales().contains(aLocale)) {

				this.getSession().setLocale(
						new Locale(application
								.getSystemProperty("default_locale")));

			}

			// agrega el panel para ingresar al sistema o salir del mismo.
			UserPanel aPanel = new UserPanel("userPanel");
			this.add(aPanel);
			aPanel.setVisible(true);
			Collection<OperationDTO> operations = new ArrayList<OperationDTO>();
			UserDTO aDTO = this.getUserDTO();

			if (aDTO == null) {
				try {
					operations.addAll(ServiceLocator.getInstance()
							.getUsersService().getListOfAnonymousOperations());
				} catch (Exception e) {

					e.printStackTrace();
				}
			} else {
				operations.addAll(aDTO.getOperations());
			}

			// agrega el panel del menú

			this.add(new MenuPanel("menuPanel", operations));
			// agrega el panel del submenú
			SubmenuPanel submenu = new SubmenuPanel("submenuPanel", operations);
			submenu.setOutputMarkupId(true);
			this.add(submenu);

			DefaultProjectPanel defaultPanel = new DefaultProjectPanel(
					"defaultProjectPanel", this.getUserDTO(), this
							.getProjectDTO());
			this.add(defaultPanel);
			defaultPanel.setVisible(aDTO != null);

			SearchItemPanel searchPanel = new SearchItemPanel("searchItemPanel");
			this.add(searchPanel);
		} catch (Exception e) {
			this.setResponsePage(ErrorPage.class);
		}

	}

	/**
	 * Actualiza el menú. <br>
	 * Generalmente debido a cambios de idioma.
	 */
	public void updateMenu() {

		((MenuPanel) this.get("menuPanel")).updateMenu();
		((SubmenuPanel) this.get("submenuPanel")).updateMenu();
	}

	/**
	 * Recupera el valor de una propiedad de sistema.
	 * 
	 * @param aPropertyKey
	 *            es la clave de la propiedad que se debe recuperar.
	 * @return un String que contiene el valor de la propiedad.
	 */
	protected String getSystemProperty(String aPropertyKey) {
		ItemApplication application = (ItemApplication) this.getApplication();

		return application.getSystemProperty(aPropertyKey);
	}

	/**
	 * Recupera un valor para una propiedad.
	 * 
	 * @param aPropertyKey
	 *            es la clave para la cual se debe buscar el valor.
	 * @return un String que contiene el valor de la propiedad.
	 */
	protected String getPropertyValue(String aPropertyKey) {

		String propertyValue = null;

		UserDTO dto = this.getUserDTO();
		if ((dto != null)
				&& (this.getUserDTO().getUserPreference(aPropertyKey) != null)) {

			propertyValue = this.getUserDTO().getUserPreference(aPropertyKey);
		} else {

			propertyValue = this.getSystemProperty(aPropertyKey);

		}

		return propertyValue;

	}

	/**
	 * Actualiza las prefencias del usuario en la sesión. <br>
	 * En caso de error no se actualizan las preferencias.
	 * 
	 * @param aKey
	 *            es la clave de la propiedad que se debe actualizar.
	 * @param aValue
	 *            es el valor de la propiedad que se debe actualizar.
	 */
	public void updateUserPreferences(String aKey, String aValue) {

		if (this.getUserDTO() != null) {
			try {
				// actualiza las preferencias en el modelo.
				UsersServiceBI usersService = ServiceLocator.getInstance()
						.getUsersService();
				usersService.updateUserPreference(this.getUserDTO(), aKey,
						aValue);

				// actualiza las preferencias en el dto.
				this.getUserDTO().getUserPreferences().put(aKey, aValue);

			} catch (Exception e) {
			}
		}

	}

	/**
	 * Getter.<br>
	 * Importante: este método puede devolver un valor nulo, por lo que el
	 * objeto que invoque este método está obligado a tratar esta posibilidad.
	 * 
	 * @return obtiene el dto que contiene la información del usuario.
	 */
	public UserDTO getUserDTO() {
		return ((ItemSession) this.getSession()).getUserDTO();
	}

	/**
	 * Getter.<br>
	 * Importante: este método puede devolver un valor nulo, por lo que el
	 * objeto que invoque este método está obligado a tratar esta posibilidad.
	 * 
	 * @return obtiene el dto que contiene la infomración del proyecto
	 *         seleccionado.
	 */
	public ProjectDTO getProjectDTO() {
		return ((ItemSession) this.getSession()).getProjectDTO();
	}

	/**
	 * Verifica si el usuario logueado en la sesión tiene asignada una operación
	 * con el nombre recibido.
	 * 
	 * @param anOperationName
	 *            es el nombre de la operación para verificar.
	 * @return true en caso de que el usuario tenga asignada la opeeración;
	 *         false en caso contrario.
	 */
	public boolean verifyPermissionAssigmentToUser(String anOperationName) {
		boolean result = false;
		UserDTO userDTO = this.getUserDTO();
		if (userDTO != null
				&& userDTO.containsOperationWithName(anOperationName)) {
			result = true;
		}
		return result;
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación de los servicios de usuarios.
	 */
	public UsersServiceBI getUsersService() {
		return ServiceLocator.getInstance().getUsersService();
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación de los servicios de proyectos.
	 */
	public ProjectsServiceBI getProjectsService() {
		return ServiceLocator.getInstance().getProjectsService();
	}

	/**
	 * Getter.
	 * 
	 * @return la implementación de los servicios de ítems.
	 */
	public ItemsServiceBI getItemsService() {
		return ServiceLocator.getInstance().getItemsService();
	}

}
