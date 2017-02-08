package com.apress.wicketbook.ajax;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class AjaxIndicator extends WebMarkupContainer {

	/**
	 * UID por defecto para la serialización.
	 */
	private static final long serialVersionUID = -8813481266179081971L;

	public AjaxIndicator(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	public void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("src", urlFor(AbstractDefaultAjaxBehavior.INDICATOR));
	}
}
