package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.dom.AttachMouseOverEvent;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;

public class MouseOvers implements EntryPoint {

	protected HandlerManager eventBus;
	final static String DOM_MOUSEOVER_CLASS = "mouse_over";
	final static String DOM_MOUSEOVER_ACTIVE_CLASS = "active";
	
	@Override
	public void onModuleLoad() {
		eventBus = new HandlerManager(null);
		
		new AttachMouseOverEvent(eventBus, DOM_MOUSEOVER_CLASS, DOM_MOUSEOVER_ACTIVE_CLASS);

	}

}
