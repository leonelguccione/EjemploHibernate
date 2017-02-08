/**
 * Este paquete contiene todas las clases que modelan los distintos formularios 
 * utilizados en la aplicación.
 */
package zinbig.item.application.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;

import zinbig.item.application.components.ButtonPagingNavigator;
import zinbig.item.application.dataproviders.PrioritySetDTODataProvider;
import zinbig.item.application.pages.AddPrioritySetPage;
import zinbig.item.application.pages.BasePage;
import zinbig.item.application.pages.EditPrioritySetPage;
import zinbig.item.application.pages.Pageable;
import zinbig.item.util.Constants;
import zinbig.item.util.Utils;
import zinbig.item.util.dto.PrioritySetDTO;
import zinbig.item.util.dto.ProjectDTO;

/**
 * Las instancias de esta clase se utilizan para presentar al usuario una lista
 * de conjuntos de prioridades. Estos conjuntos pueden ser seleccionados para
 * realizar operaciones sobre ellos como eliminarlos o editarlos.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class PrioritySetsAdministrationForm extends ItemForm {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = 6727159892012901701L;

	/**
	 * Es una colección de Oids de los conjuntos de prioridades que han sido
	 * seleccionados.
	 */
	private Collection<String> selectedPrioritySets;

	/**
	 * Cosntructor.
	 * 
	 * @param anId
	 *            es el identificador de este formulario.
	 * @param aPage
	 *            es la página en la que se agregó este formulario.
	 */
	public PrioritySetsAdministrationForm(String anId, Pageable aPage) {
		super(anId);

		// agrega el link para poder dar de alta un nuevo conjunto de
		// prioridades
		Link<BasePage> aLink = new BookmarkablePageLink<BasePage>(
				"newPrioritySetLink", AddPrioritySetPage.class);
		this.add(aLink);

		// crea el link para eliminar los conjuntos de prioridades

		// crea el componente para mostrar los errores
		Collection<String> messages = new ArrayList<String>();
		messages
				.add(this
						.getString("prioritySetsAdministrationForm.noPrioritySetSelected"));
		messages
				.add(this
						.getString("prioritySetsAdministrationForm.errorDeletingPrioritySets"));
		this.add(this.createFeedbackPanel(messages));

		this.setSelectedPrioritySets(new HashSet<String>());

		SubmitLink deleteLink = new SubmitLink("deletePrioritySetsLink") {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Envía el requerimiento para borrar los conjuntos de prioridades
			 * seleccionados. En caso de que no se hayan seleccionado conjuntos
			 * de prioridades se muestra un cartel de error al usuario.
			 */
			@Override
			public void onSubmit() {
				if (PrioritySetsAdministrationForm.this
						.getSelectedPrioritySets().isEmpty()) {
					this
							.error(this
									.getString("prioritySetsAdministrationForm.noPrioritySetSelected"));
				} else {
					try {
						PrioritySetsAdministrationForm.this
								.getPrioritiesService().deletePrioritySets(
										PrioritySetsAdministrationForm.this
												.getSelectedPrioritySets());
					} catch (Exception e) {
						e.printStackTrace();
						this
								.error(this
										.getString("prioritySetsAdministrationForm.errorDeletingPrioritySets"));
					}
				}
			}

		};

		this.add(deleteLink);

		CheckGroup<ProjectDTO> group = new CheckGroup<ProjectDTO>("group",
				new PropertyModel<Collection<ProjectDTO>>(this,
						"selectedPrioritySets"));
		this.add(group);
		group.add(new CheckGroupSelector("groupselector"));

		// recupera las preferencias de ordenamiento del usuario
		String columnName = this.getUserDTO().getUserPreference(
				Constants.PRIORITY_SETS_ADMINISTRATION_COLUMN_NAME);
		String columnOrder = this.getUserDTO().getUserPreference(
				Constants.PRIORITY_SETS_ADMINISTRATION_COLUMN_ORDER);

		if (columnName == null) {
			columnName = this
					.getSystemProperty(Constants.PRIORITY_SETS_ADMINISTRATION_COLUMN_NAME);
			columnOrder = this
					.getSystemProperty(Constants.PRIORITY_SETS_ADMINISTRATION_COLUMN_ORDER);
		}

		final PrioritySetDTODataProvider dataProvider = new PrioritySetDTODataProvider(
				columnName, columnOrder);

		// crea el componente para el listado de las prioridades existentes
		final DataView<PrioritySetDTO> dataView = new DataView<PrioritySetDTO>(
				"pageable", dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Define el contenido de cada una de las filas del listado.
			 * 
			 * @param item
			 *            es objeto que contiene la información que se utilizará
			 *            para armar la fila.
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(final Item<PrioritySetDTO> item) {
				PrioritySetDTO dto = item.getModelObject();

				PageParameters parameters = new PageParameters();
				parameters.put("prioritySetId", Utils
						.encodeString(dto.getOid()));

				Check check = new Check("cb", new PropertyModel(dto, "oid"));
				item.add(check);

				Link aLink = new BookmarkablePageLink("editLink",
						EditPrioritySetPage.class, parameters);
				aLink.add(new Label("name", dto.getName()));
				item.add(aLink);

			}
		};

		dataView.setItemsPerPage(aPage.getItemsPerPage());

		// agrega el link para ordenar por el nombre de los proyectos.
		OrderByLink sortByNameLink = new OrderByLink("sortByNameLink", "name",
				dataProvider) {

			/**
			 * UID por defecto.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Notifica a este objeto que se cambió el orden. Reinicia el
			 * componente para dejarlo en la primer página.
			 */
			@Override
			protected void onSortChanged() {
				super.onSortChanged();
				dataView.setCurrentPage(0);
				SortParam sp = dataProvider.getSort();

				BasePage page = (BasePage) this.getPage();
				page.updateUserPreferences(
						Constants.PROJECTS_ADMINISTRATION_COLUMN_NAME, sp
								.getProperty());
				page.updateUserPreferences(
						Constants.PROJECTS_ADMINISTRATION_COLUMN_ORDER, sp
								.isAscending() ? "ASC" : "DESC");
			}

		};
		// agrega el checkbox group.
		group.add(dataView);

		// agrega el link para ordenar por el nombre.
		group.add(sortByNameLink);

		// agrega el componente de navegación
		ButtonPagingNavigator navigator = new ButtonPagingNavigator(
				"navigator", dataView);
		this.add(navigator);

		if (dataProvider.size() > aPage.getItemsPerPage()) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

		// agrega el componente que muestra la cantidad de elementos.
		Label countLabel = new Label("count", new Long(dataProvider.size())
				.toString());
		this.add(countLabel);
	}

	/**
	 * Actualiza el componente de navegación para verificar que hay más páginas
	 * para mostrar tomando como entrada la cantidad de elementos que se
	 * muestran en la página. Si la cantidad de elementos que se deben mostrar
	 * es mayor a la cantidad total de elementos, el navegador no aparece.
	 * 
	 * @param aNumber
	 *            es el número de elementos a mostrar por página.
	 */
	public void updateItemsPerPage(int aNumber) {

		DataView<PrioritySetDTO> dataView = this.getDataView();
		ButtonPagingNavigator navigator = (ButtonPagingNavigator) this
				.get("navigator");

		dataView.setItemsPerPage(aNumber);

		if (dataView.getDataProvider().size() > aNumber) {
			navigator.setVisible(true);
		} else {
			navigator.setVisible(false);
		}

	}

	/**
	 * Getter.
	 * 
	 * @return el componente que se utiliza para armar el listado.
	 */
	@SuppressWarnings("unchecked")
	public DataView<PrioritySetDTO> getDataView() {
		return (DataView<PrioritySetDTO>) ((CheckGroup<PrioritySetDTO>) this
				.get("group")).get("pageable");
	}

	/**
	 * Getter.
	 * 
	 * @return una colección que contiene los OIDs de los conjuntos de
	 *         prioridades seleccionados.
	 */
	public Collection<String> getSelectedPrioritySets() {
		return this.selectedPrioritySets;
	}

	/**
	 * Setter.
	 * 
	 * @param somePrioritySets
	 *            es una colección que contiene los OIDs de los conjuntos de
	 *            prioridades seleccionados.
	 */
	public void setSelectedPrioritySets(Collection<String> somePrioritySets) {
		this.selectedPrioritySets = somePrioritySets;
	}

}
