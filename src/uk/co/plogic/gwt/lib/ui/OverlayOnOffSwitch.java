package uk.co.plogic.gwt.lib.ui;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
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
		
		// messy looking - just finds the overlay ID
        NodeList<com.google.gwt.dom.client.Element> overlayMetaData = element.getElementsByTagName("span");
    	for( int ii=0; ii<overlayMetaData.getLength(); ii++ ) {
    		Element overlayMetaElement = (Element) overlayMetaData.getItem(ii);
    		String className = overlayMetaElement.getClassName();
    		ArrayList<String> allClasses = new ArrayList<String>();
    		// handle multiple classes in class="..."
    		if( className.contains(" ") ) {
    			for( String cn : className.split(" ") ) {
    				allClasses.add(cn);
    			}
    		} else {
    			allClasses.add(className);
    		}
    		
    		if( allClasses.contains("overlay_id") ) {
    			overlayID = overlayMetaElement.getInnerText().trim();
    		}
    	}

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

		//FlowPanel fp = new FlowPanel();
		//initWidget(fp);
    	//SafeHtml safeHtml = SafeHtmlUtils.fromString("<div class=\"bootstrap-switch bootstrap-switch-mini bootstrap-switch-animate bootstrap-switch-off\"><div style=\"\"><span class=\"bootstrap-switch-handle-on bootstrap-switch-primary\">ON</span><label>&nbsp;</label><span class=\"bootstrap-switch-handle-off bootstrap-switch-default\">OFF</span><input type=\"checkbox\" checked=\"\" data-size=\"mini\"></div></div>");
		//HTML b = new HTML(safeHtml);
		//fp.add(b);
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
