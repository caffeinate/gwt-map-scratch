package uk.co.plogic.gwt.lib.ui.activatedElements;

import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.events.ClickFireEvent;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;


/**
 * 
 * Setup the DOM to fire the {@link uk.co.plogic.gwt.lib.events.ClickFireEvent} event when an
 * element with the given ID is clicked.
 * 
 * @author si
 *
 */
public class ClickFireInteraction {


	/**
	 * 
	 * @param className 
	 */
	public ClickFireInteraction(DomParser domParser, final HandlerManager eventBus, final String elementID) {

	    domParser.addHandler(new DomElementByAttributeFinder("id", elementID) {

	    	
	        @Override
	        public void onDomElementFound(Element element, String id) {

	        	Event.setEventListener(element, new EventListener() {
	                
	                @Override
	                public void onBrowserEvent(Event event) {
	                    switch (DOM.eventGetType(event)) {
	                    case Event.ONCLICK:
	                    	eventBus.fireEvent(new ClickFireEvent(elementID));
	                        break;
	                    }
	                }
	            });
	            
	            Event.sinkEvents(element, Event.ONCLICK);

	        }
	    });

	}

}
