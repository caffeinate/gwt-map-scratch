package uk.co.plogic.gwt.lib.widget;

import uk.co.plogic.gwt.lib.events.OverlayInRangeEvent;
import uk.co.plogic.gwt.lib.events.OverlayInRangeEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class OverlayOnOffSwitch  extends Composite {

	boolean switchState = false;
	boolean inRangeState = true;
	String overlayID = "";
	HandlerManager eventBus;
	HTML switchHtml;
	HTML closedEyeHtml;
	FlowPanel container;

	public OverlayOnOffSwitch(HandlerManager eventBus, final String overlayID) {
		this.eventBus = eventBus;
		this.overlayID = overlayID;

		String raw = "<div style=\"\"><span class=\"bootstrap-switch-handle-on bootstrap-switch-primary\">ON</span><label>&nbsp;</label><span class=\"bootstrap-switch-handle-off bootstrap-switch-default\">OFF</span><input type=\"checkbox\" checked=\"\" data-size=\"mini\"></div>";
		switchHtml = new HTML(SafeHtmlUtils.fromTrustedString(raw));

		String rawEye = "<h2><span class=\"glyphicon glyphicon-eye-close\" aria-hidden=\"true\" title=\"Not visible at this zoom level.\"></span></h2>";
		closedEyeHtml = new HTML(SafeHtmlUtils.fromTrustedString(rawEye));

    	switchHtml.setStyleName("bootstrap-switch");
    	switchHtml.addStyleName("bootstrap-switch-mini");
    	switchHtml.addStyleName("bootstrap-switch-off");

    	switchHtml.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				toggle();
		        onToggle();
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

        // layer should be visible but zoom or extent data isn't available for current map view
        eventBus.addHandler(OverlayInRangeEvent.TYPE, new OverlayInRangeEventHandler() {

            @Override
            public void onOverlayInRangeChange(OverlayInRangeEvent e) {
                if(    overlayID != null
                        && overlayID.equals(e.getOverlayId())
                        && e.isInRange() != inRangeState ) {
                        // warning, toggle also fires this event so take care not to cause a cascade storm
                        showSwitch(e.isInRange());
                    }
            }
        });

        container = new FlowPanel();
        showSwitch(true);
    	initWidget(container);
	}

	public void toggle() {
		if( switchState ) {
        	switchHtml.removeStyleName("bootstrap-switch-on");
        	switchHtml.addStyleName("bootstrap-switch-off");
    	} else {
    		switchHtml.removeStyleName("bootstrap-switch-off");
        	switchHtml.addStyleName("bootstrap-switch-on");
    	}
    	switchState = ! switchState;
    	eventBus.fireEvent(new OverlayVisibilityEvent(switchState, overlayID));
	}

	/**
	 * if the switch isn't available, the eye will be shown.
	 * @param showSwitch
	 */
	public void showSwitch(boolean showSwitch) {
	    inRangeState = showSwitch;
	    container.clear();
	    if( showSwitch ) container.add(switchHtml);
	    else container.add(closedEyeHtml);
	}

	/**
	 * to be overridden. Is called when a toggle happens and after
	 * OverlayVisibilityEvent.
	 */
	public void onToggle() {}
}
