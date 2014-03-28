package uk.co.plogic.gwt.lib.ui;

import uk.co.plogic.gwt.lib.events.MapReadyEvent;
import uk.co.plogic.gwt.lib.events.MapReadyEventHandler;
import uk.co.plogic.gwt.lib.events.MouseClickEvent;
import uk.co.plogic.gwt.lib.events.MouseClickEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class OverlayOnOffSwitch { // extends Composite {

	boolean switchState = false;
	Element element;
	String overlayID = "";
	HandlerManager eventBus;

	public OverlayOnOffSwitch(HandlerManager eventBus, final Element e) {
		this.eventBus = eventBus;
		// TODO make this into a widget
		
		element = e;
				
		ElementScrapper es = new ElementScrapper();
		overlayID = es.findOverlayId(element, "span", "overlay_id");

    	//String raw = "<div class=\"bootstrap-switch bootstrap-switch-mini bootstrap-switch-animate bootstrap-switch-off\"><div style=\"\"><span class=\"bootstrap-switch-handle-on bootstrap-switch-primary\">ON</span><label>&nbsp;</label><span class=\"bootstrap-switch-handle-off bootstrap-switch-default\">OFF</span><input type=\"checkbox\" checked=\"\" data-size=\"mini\"></div></div>";
    	String raw = "<div style=\"\"><span class=\"bootstrap-switch-handle-on bootstrap-switch-primary\">ON</span><label>&nbsp;</label><span class=\"bootstrap-switch-handle-off bootstrap-switch-default\">OFF</span><input type=\"checkbox\" checked=\"\" data-size=\"mini\"></div>";
    	element.setInnerHTML(raw);

    	//bootstrap-switch-animate

    	element.addClassName("bootstrap-switch");
    	element.addClassName("bootstrap-switch-mini");
    	element.addClassName("bootstrap-switch-off");

    	Event.setEventListener(element, new EventListener() {
            
            @Override
            public void onBrowserEvent(Event event) {
                switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                	toggle();
                    break;
                }
            }
        });
        
        Event.sinkEvents(element, Event.ONCLICK);
        
        
        // if CSS class is set - turn the layer on once the map is ready
		if( element.hasClassName("dataset_switch_on") ) {
			
			eventBus.addHandler(MapReadyEvent.TYPE, new MapReadyEventHandler() {

				@Override
				public void onMapReadyEvent(MapReadyEvent event) {
					// opposite as toggle() will toggle this
					switchState = false;
					toggle();
				}
			});
			
		}

        

	}
	
	public void toggle() {

		if( switchState ) {
        	element.removeClassName("bootstrap-switch-on");
        	element.addClassName("bootstrap-switch-off");
    	} else {
    		element.removeClassName("bootstrap-switch-off");
        	element.addClassName("bootstrap-switch-on");
    	}
    	switchState = ! switchState;
    	eventBus.fireEvent(new OverlayVisibilityEvent(switchState, overlayID));
	}

}
