/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import zinbig.item.application.components.ItemDetailedInformationPanel;
import zinbig.item.services.ServiceLocator;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de esta clase se utilizan para permitir la impresión del
 * detalle de un ítem dado.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrintableItemDetailedInformationPage extends WebPage {

	/**
	 * Constructor.
	 */
	public PrintableItemDetailedInformationPage(PageParameters params) {
		super();

		try {
			String item_oid = params.getString("ITEM_OID");
			ItemDTO anItemDTO = ServiceLocator.getInstance().getItemsService()
					.findItemByOid(item_oid);

			Date aDate = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Label dateLabel = new Label("dateLabel", formatter.format(aDate));
			this.add(dateLabel);

			this
					.add(new ItemDetailedInformationPanel("panel", anItemDTO,
							false));
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponsePage(ErrorPage.class);
		}

	}

}
