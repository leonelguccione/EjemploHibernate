/**
 * Este paquete contiene las definiciones de las clases que representan a los
 * componentes de la aplicación desarrollados específicamente para encapsular
 * comportamiento y permitir el reuso.
 */
package zinbig.item.application.components;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;

import zinbig.item.util.dto.FilterDTO;

/**
 * Las instancias de esta clase se utilizan para representar un conjunto de tabs
 * en una página que contiene múltiples tabs. Este tab funciona con AJAX.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ItemAjaxTabbedPanel extends AjaxTabbedPanel {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -3528027147842472980L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            es el identificador de este componente.
	 * @param tabs
	 *            es una colección que contiene los tabs de este componente.
	 */
	public ItemAjaxTabbedPanel(String id, List<ITab> tabs) {
		super(id, tabs);
	}

	/**
	 * Elimina el tab actual de la colección de tabs.
	 * 
	 * Si el tab removido es el único, entonces este componente se oculta.
	 */
	public void removeCurrentTab() {
		int index = this.getSelectedTab();
		int size = this.getTabs().size();

		if (index == 0) {

			this.getTabs().remove(0);

			if (size == 1) {
				this.setVisible(false);
			} else {
				this.setSelectedTab(0);
			}

		} else {

			this.setSelectedTab(index - 1);
			this.getTabs().remove(index);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return la solapa actualmente seleccionada.
	 */
	public ItemTab getSelectedItemTab() {
		return (ItemTab) this.getTabs().get(this.getSelectedTab());
	}

	/**
	 * Actualiza el estado del filtro de la solapa correspondiente al filtro (si
	 * es que está abierta dicha solapa).
	 * 
	 * @param aFilterDTO
	 *            es el dto del filtro que se ha actualizado.
	 */
	public void updateFilter(FilterDTO aFilterDTO) {
		Iterator<ITab> iterator = this.getTabs().iterator();
		ItemTab aTab = null;

		while (iterator.hasNext()) {
			aTab = (ItemTab) iterator.next();

			aTab.updateFilter(aFilterDTO);

		}
	}

}
