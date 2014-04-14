package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.ui.activatedElements.MouseInteractions;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;

public class MouseOvers implements EntryPoint {

	protected HandlerManager eventBus;
	final static String DOM_MOUSEOVER_CLASS = "mouse_over";
	final static String DOM_MOUSEOVER_ACTIVE_CLASS = "active";
	
	@Override
	public void onModuleLoad() {
		eventBus = new HandlerManager(null);
		
		new MouseInteractions(eventBus, DOM_MOUSEOVER_CLASS, DOM_MOUSEOVER_ACTIVE_CLASS);

	}

}
