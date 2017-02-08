/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.Model;

import zinbig.item.application.components.ItemAjaxTabbedPanel;
import zinbig.item.application.components.ItemTab;
import zinbig.item.application.components.itemsfilter.ItemsFilterPanel;
import zinbig.item.model.ItemStateEnum;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.FilterComponentByProjectDTO;
import zinbig.item.util.dto.FilterDTO;
import zinbig.item.util.dto.ItemTypeDTO;
import zinbig.item.util.dto.WorkflowNodeDescriptionDTO;

/**
 * Las instancias de esta clase se utilizan para visualizar los ítems. <br>
 * Esta página puede ser accedida tanto por usuarios identificados como por
 * usuarios anónimos. <br>
 * Esta página contiene un panel que permite trabajar con los filtros de los
 * ítems, y luego un conjunto de solapas que se van agregando a medida que se
 * ejecutan los filtros.
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ViewItemsPage extends BasePage implements Pageable {

	/**
	 * Mantiene la cantidad de ítems que se deben mostrar en el listado.
	 */
	protected int itemsPerPage;

	/**
	 * Es el panel con las solapas para los resultados de los filtros.
	 */
	public ItemAjaxTabbedPanel tabsPanel;

	/**
	 * Constructor.
	 */
	public ViewItemsPage(PageParameters parameters) {
		try {
			// recupera la cantidad de ítems que se deben mostrar en el listado.
			this.setItemsPerPage(new Integer(this
					.getPropertyValue("ItemsAdministrationPage.itemsPerPage"))
					.intValue());

			// agrega el panel que contendrá las solapas de los listados.
			List<ITab> tabs = new ArrayList<ITab>();
			ItemAjaxTabbedPanel tabsPanel = new ItemAjaxTabbedPanel("tabs",
					tabs);
			tabsPanel.setOutputMarkupId(true);
			this.add(tabsPanel);
			this.setTabsPanel(tabsPanel);

			// agrega el componente que permite ver y armar nuevos filtros.
			// el componente adecúa su comportamiento dependiendo si el usuario
			// el nulo o no
			ItemsFilterPanel filterPanel = new ItemsFilterPanel("filterPanel",
					this.getUserDTO(), this.getProjectDTO(), tabsPanel, this);
			filterPanel.setOutputMarkupId(true);
			this.add(filterPanel);

			// si se accedió a esta página mediante la selección de un filtro
			// favorito entonces se muestra una solapa con el resultado de la
			// ejecución del filtro y se oculta además el componente de armado
			// de filtros.
			String filterId = parameters.getString("FILTER_OID");

			ItemsServiceBI service = this.getItemsService();
			FilterDTO aFilterDTO = null;
			if (filterId != null) {
				try {
					aFilterDTO = service.findItemFilterById(filterId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (parameters.containsKey("FILTER_DTO_TITLE")) {
					String title = parameters.getString("FILTER_DTO_TITLE");

					String searchString = parameters
							.getString("FILTER_DTO_SEARCH_STRING");

					try {

						Collection<FilterComponentByProjectDTO> aCol = new ArrayList<FilterComponentByProjectDTO>();

						aFilterDTO = service.createFilter(this.getUserDTO(),
								title, aCol, searchString,
								new ArrayList<ItemStateEnum>(),
								new ArrayList<AbstractUserDTO>(),
								new ArrayList<ItemTypeDTO>(),
								new ArrayList<WorkflowNodeDescriptionDTO>(),
								false, false, false, false, false, "");
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

			if (aFilterDTO != null) {
				try {

					ItemTab newTab = new ItemTab(new Model<String>(aFilterDTO
							.getName()), aFilterDTO, this.getUserDTO(), this
							.getProjectDTO(), this, tabsPanel);
					tabs.add(newTab);

				} catch (Exception e) {
					e.printStackTrace();

				}
			} else {
				tabsPanel.setVisible(false);

			}

			this.setOutputMarkupId(true);

		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}
	}

	/**
	 * Getter.
	 * 
	 * @return la cantidad de ítems que se deben mostrar en la página.
	 */
	public int getItemsPerPage() {
		return this.itemsPerPage;
	}

	/**
	 * Setter.
	 * 
	 * @param aNumber
	 *            es la cantidad de ítems que se deben mostrar por cada página.
	 */
	public void setItemsPerPage(int aNumber) {
		this.itemsPerPage = aNumber;
	}

	/**
	 * Actualiza la cantidad de elementos que se deben mostrar por solapa.
	 */
	@Override
	public void updateItemsPerPage() {

		int itemsPerPage = this.getItemsPerPage();

		this.updateUserPreferences("ItemsAdministrationPage.itemsPerPage",
				new Integer(itemsPerPage).toString());

		ItemTab itemTab = this.getTabsPanel().getSelectedItemTab();
		itemTab.updateItemsPerPage(itemsPerPage);

	}

	/**
	 * Getter.
	 * 
	 * @return el panel de solapas para los resultados de los filtros.
	 */
	public ItemAjaxTabbedPanel getTabsPanel() {
		return this.tabsPanel;
	}

	/**
	 * Setter.
	 * 
	 * @param aTabPanel
	 *            es el panel de solapas para los resultados de los filtros.
	 */
	public void setTabsPanel(ItemAjaxTabbedPanel aTabPanel) {
		this.tabsPanel = aTabPanel;
	}

}
