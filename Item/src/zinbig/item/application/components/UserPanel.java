/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.Locale;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemSession;
import zinbig.item.application.forms.LoginForm;
import zinbig.item.application.forms.LogoutForm;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.DashboardPage;
import zinbig.item.services.ServiceLocator;
import zinbig.item.util.dto.UserDTO;

/**
 * Este panel se utiliza para permitir al usuario entrar y salir del sistema.<Br>
 * Siempre se muestra un link que permite cambiar de idioma.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class UserPanel extends Panel {

  /**
   * UID por defecto para la serialización.
   */
  private static final long serialVersionUID = 8869846139353579717L;

  /**
   * Constructor.
   * 
   * @param id
   *          es el id de este componente.
   */
  public UserPanel(String id) {
    super(id);

    // crea y agrega el formulario de ingreso.
    LoginForm aForm = new LoginForm("loginForm");
    this.add(aForm);

    // crea y agrega el formulario de salida.
    LogoutForm exitForm = new LogoutForm("logoutForm");
    this.add(exitForm);

    final UserDTO dto = ((ItemSession) this.getSession()).getUserDTO();
    if (dto != null) {
      // agrega el link para salir de la aplicación.
      aForm.setVisible(false);
      exitForm.setVisible(true);

    } else {
      // no hay usuario en la sesión así que mostramos el link de ingreso.
      aForm.setVisible(true);
      exitForm.setVisible(false);

    }

    // agrega la bandera que representa al locale que está siendo utilizado.
    String imageName = "/Item/images/bandera_" + this.getSession().getLocale().toString() + ".gif";
    ContextImage image = new ContextImage("img", new Model<String>(imageName));
    Label languageLabel = new Label("selectedLanguage", this.getString(this.getSession().getLocale()
        .toString()));
    this.add(languageLabel);

    // agrega el link del idioma español.
    Link<String> spanishLink = new Link<String>("spanishLink") {

      /**
       * UID por defecto para la serialización.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Manejador del evento de click en el link.<br>
       * Cambia el idioma al español, y si existe un usuario en la sesión actualiza el perfil del mismo con el
       * nuevo idioma.
       */
      public void onClick() {
        getSession().setLocale(new Locale("es"));

        String imageName = "/Item/images/bandera_es.gif";
        ContextImage image = new ContextImage("img", new Model<String>(imageName));

        this.getParent().get("img").replaceWith(image);
        image.add(new SimpleAttributeModifier("alt", "Español"));
        UserDTO dto = ((ItemSession) this.getSession()).getUserDTO();

        if (dto != null) {
          dto.setLanguage("es");
          // actualiza el modelo
          try {
            ServiceLocator.getInstance().getUsersService().updateUserInformation(dto, null);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        // actualiza el componente de menú
        ((BasePage) this.getPage()).updateMenu();
        this.setResponsePage(DashboardPage.class);

      }
    };
    this.add(spanishLink);

    // agrega el link que permite seleccionar el idioma inglés.
    Link<String> englishLink = new Link<String>("englishLink") {

      /**
       * UID por defecto para la serialización.
       */
      private static final long serialVersionUID = 1L;

      /**
       * Manejador del evento de click en el link.<br>
       * Cambia el idioma al inglés, y si existe un usuario en la sesión actualiza el perfil del mismo con el
       * nuevo idioma.
       */
      public void onClick() {
        getSession().setLocale(new Locale("en"));

        String imageName = "/Item/images/bandera_en.gif";
        ContextImage image = new ContextImage("img", new Model<String>(imageName));

        this.getParent().get("img").replaceWith(image);
        image.add(new SimpleAttributeModifier("alt", "English"));
        UserDTO dto = ((ItemSession) this.getSession()).getUserDTO();

        if (dto != null) {
          dto.setLanguage("en");
          // actualiza el modelo
          try {
            ServiceLocator.getInstance().getUsersService().updateUserInformation(dto, null);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        // actualiza el componente de menú
        ((BasePage) this.getPage()).updateMenu();

        this.setResponsePage(DashboardPage.class);
      }
    };
    this.add(englishLink);

    // actualiza los links de los idiomas para ocultar el idioma ya
    // seleccionado.
    if (this.getSession().getLocale().toString().equals("es")) {
      spanishLink.setVisible(false);
      englishLink.setVisible(true);
    } else {
      spanishLink.setVisible(true);
      englishLink.setVisible(false);
    }

    // agrega la imagen de la bandera
    this.add(image);

  }

}
