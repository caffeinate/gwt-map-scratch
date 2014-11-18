package uk.co.plogic.gwt.lib.widget;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class OverlayOnOffSwitch  extends Composite {

	boolean switchState = false;
	String overlayID = "";
	HandlerManager eventBus;
	HTML h;

	public OverlayOnOffSwitch(HandlerManager eventBus, final String overlayID) {
		this.eventBus = eventBus;
		this.overlayID = overlayID;

		String raw = "<div style=\"\"><span class=\"bootstrap-switch-handle-on bootstrap-switch-primary\">ON</span><label>&nbsp;</label><span class=\"bootstrap-switch-handle-off bootstrap-switch-default\">OFF</span><input type=\"checkbox\" checked=\"\" data-size=\"mini\"></div>";
		h = new HTML(SafeHtmlUtils.fromTrustedString(raw));

    	h.setStyleName("bootstrap-switch");
    	h.addStyleName("bootstrap-switch-mini");
    	h.addStyleName("bootstrap-switch-off");

    	h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				toggle();
			}
	    });

        // other parts of the system will turn this layer on and off
        eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {

				if(    overlayID != null
					&& overlayID.equals(e.getOverlayId())
					&& e.isVisible() != switchState ) {
					// warning, toggle also fires this event so take care not to cause a cascade storm
					toggle();
				}
			}
		});
    	
    	initWidget(h);
	}

	public void toggle() {
		if( switchState ) {
        	h.removeStyleName("bootstrap-switch-on");
        	h.addStyleName("bootstrap-switch-off");
    	} else {
    		h.removeStyleName("bootstrap-switch-off");
        	h.addStyleName("bootstrap-switch-on");
    	}
    	switchState = ! switchState;
    	eventBus.fireEvent(new OverlayVisibilityEvent(switchState, overlayID));
	}
}
