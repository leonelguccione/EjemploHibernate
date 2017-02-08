/**
 * Este paquete contiene las clases que representan las diferentes páginas de la 
 * aplicación.
 */
package zinbig.item.application.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import zinbig.item.application.ItemSession;
import zinbig.item.application.components.EditItemBasicDataPanel;
import zinbig.item.application.components.EditItemCommentsPanel;
import zinbig.item.application.components.ItemAdditionalPropertiesPanel;
import zinbig.item.application.components.ItemAttachedFilesPanel;
import zinbig.item.application.components.ItemDetailedInformationPanel;
import zinbig.item.application.components.MoveItemInWorkflowPanel;
import zinbig.item.services.bi.ItemsServiceBI;
import zinbig.item.util.dto.ItemDTO;

/**
 * Las instancias de esta clase se utilizan para mostrar los detalles básicos de
 * un ítem. A esta página se puede acceder siempre y cuando el ítem pertenezca a
 * un proyecto público o en caso de pertenecer a un proyecto privado, el usuario
 * pertenezca al mismo proyecto.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class ViewItemDetailPage extends BasePage {

	/**
	 * Es el dto que representa al item que se está editando.
	 */
	public ItemDTO itemDTO;

	/**
	 * Constructor.
	 */
	public ViewItemDetailPage(PageParameters parameters) {
		super();
		try {
			// recupera los datos enviados como parámetros

			String itemOID = parameters.getString("ITEM_OID");
			final String filterOID = parameters.getString("FILTER_OID");

			// recupera el item seleccionado
			try {
				ItemsServiceBI service = this.getItemsService();
				ItemDTO itemDTO = service.findItemByOid(itemOID);

				Label aLabel = new Label("itemId", new Integer(itemDTO.getId())
						.toString());
				this.add(aLabel);

				this.setItemDTO(itemDTO);

				if (this.getProjectDTO() == null) {
					((ItemSession) this.getSession()).setProjectDTO(itemDTO
							.getProject());
				}
				// agrega el link que permite volver a ver todos los ítems ya
				// existentes.
				PageParameters params = new PageParameters();
				if (filterOID != null && !filterOID.equals("")) {
					params.put("FILTER_OID", filterOID);
				}
				Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
						"viewAllLink", ViewItemsPage.class, params);
				this.add(aLink);

				// crea las solapas para editar los datos básicos, editar las
				// prioridades y editar el workflow.
				List<ITab> tabs = new ArrayList<ITab>();
				tabs.add(new AbstractTab(new Model<String>(this
						.getString("ViewItemDetailPage.basicData"))) {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Getter.
					 * 
					 * @param anId
					 *            es el identificador del panel.
					 * 
					 * @return el panel que se utiliza para editar los detalles
					 *         básicos del ítem.
					 */
					@Override
					public Panel getPanel(String anId) {
						return new EditItemBasicDataPanel(anId, getItemDTO());
					}

				});

				if (this.getUserDTO() != null
						|| !this.getItemsService().getCommentsOfItem(itemDTO)
								.isEmpty()) {

					tabs.add(new AbstractTab(new Model<String>(this
							.getString("ViewItemDetailPage.comments"))) {
						/**
						 * UID por defecto.
						 */
						private static final long serialVersionUID = 1L;

						/**
						 * Getter.
						 * 
						 * @param anId
						 *            es el identificador del panel.
						 * 
						 * @return el panel que se utiliza para agregar
						 *         comentarios al ítem.
						 */
						@Override
						public Panel getPanel(String anId) {
							return new EditItemCommentsPanel(anId, getItemDTO());
						}

					});
				}
				if (this.getUserDTO() != null
						&& (this.getItemDTO().getResponsible().equals(this
								.getUserDTO()))
						| this.getItemDTO().getProjectLeader().equals(
								this.getUserDTO())) {

					tabs.add(new AbstractTab(new Model<String>(this
							.getString("ViewItemDetailPage.workflow"))) {

						/**
						 * UID por defecto.
						 */
						private static final long serialVersionUID = 1L;

						/**
						 * Getter.
						 * 
						 * @param anId
						 *            es el identificador del panel.
						 * 
						 * @return el panel que se utiliza para mover al ítem en
						 *         el workflow.
						 */
						@Override
						public Panel getPanel(String anId) {
							return new MoveItemInWorkflowPanel(anId,
									getItemDTO(), filterOID);
						}

					});
				}

				tabs.add(new AbstractTab(new Model<String>(this
						.getString("ViewItemDetailPage.attachedFiles"))) {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Getter.
					 * 
					 * @param anId
					 *            es el identificador del panel.
					 * 
					 * @return el panel que se utiliza para administrar los
					 *         adjuntos de un ítem.
					 */
					@Override
					public Panel getPanel(String anId) {
						return new ItemAttachedFilesPanel(anId, getItemDTO());
					}

				});

				if (this.getProjectDTO().hasAdditionalProperties()) {
					tabs
							.add(new AbstractTab(
									new Model<String>(
											this
													.getString("ViewItemDetailPage.additionalProperties"))) {

								/**
								 * UID por defecto.
								 */
								private static final long serialVersionUID = 1L;

								/**
								 * Getter.
								 * 
								 * @param anId
								 *            es el identificador del panel.
								 * 
								 * @return el panel que se utiliza para ingresar
								 *         los valores de las propiedades
								 *         adicionales de los ítems definidas
								 *         para el proyecto.
								 */
								@Override
								public Panel getPanel(String anId) {
									return new ItemAdditionalPropertiesPanel(
											anId, getItemDTO());
								}

							});
				}
				tabs.add(new AbstractTab(new Model<String>(this
						.getString("ViewItemDetailPage.detailedInformation"))) {

					/**
					 * UID por defecto.
					 */
					private static final long serialVersionUID = 1L;

					/**
					 * Getter.
					 * 
					 * @param anId
					 *            es el identificador del panel.
					 * 
					 * @return el panel que se utiliza para presentar un resumen
					 *         de toda la información del ítem.
					 */
					@Override
					public Panel getPanel(String anId) {
						return new ItemDetailedInformationPanel(anId,
								getItemDTO(), true);
					}

				});

				TabbedPanel tabPanel = new TabbedPanel("tabs", tabs);
				this.add(tabPanel);
				if (parameters.containsKey("TAB")) {
					tabPanel.setSelectedTab(parameters.getInt("TAB"));
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
				;
				this.setResponsePage(ErrorPage.class);
			} catch (Exception e) {
				e.printStackTrace();
				this.setResponsePage(ErrorPage.class);
			}
		} catch (Exception e) {
			this.setResponsePage(DashboardPage.class);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return el dto que representa al item que se está editando.
	 */
	public ItemDTO getItemDTO() {
		return this.itemDTO;
	}

	/**
	 * Setter.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al item que se está editando.
	 */
	public void setItemDTO(ItemDTO anItemDTO) {
		this.itemDTO = anItemDTO;
	}

}
