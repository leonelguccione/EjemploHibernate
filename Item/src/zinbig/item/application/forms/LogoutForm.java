/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemSession;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.application.pages.EditUserPage;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.UserDTO;

/**
 * Las instancias de esta clase se utilizan para enviar la información que
 * permite sallir a un usuario registrado de la aplicación. Además se presenta
 * un link para que el usuario pueda editar su perfil.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class LogoutForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 2917054885459384485L;

	/**
	 * Constructor.
	 * 
	 * @param anId
	 *            es el id de este formulario.
	 */
	public LogoutForm(String anId) {
		super(anId);

		final UserDTO dto = ((ItemSession) this.getSession()).getUserDTO();
		String username = dto == null ? "" : dto.getUsername();
		String name = dto == null ? "" : dto.getName() + " " + dto.getSurname();

		PageParameters parameters = new PageParameters();
		parameters.put("username", Utils.encodeString(username));
		Link<String> userLink = new BookmarkablePageLink<String>(
				"editUserLink", EditUserPage.class, parameters) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

		};
		Label userLabel = new Label("userLabel", name);
		userLink.add(userLabel);
		this.add(userLink);

		Button exitButton = new Button("exitButton") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {

				ItemSession session = (ItemSession) this.getSession();
				session.setUserDTO(null);
				session.invalidate();
				this.setResponsePage(DashboardPage.class);
			}

		};

		exitButton
				.setModel(new Model<String>(this.getString("logoutForm.exit")));
		this.add(exitButton);

	}

}
