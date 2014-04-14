package uk.co.plogic.gwt.lib.dom;


import java.util.ArrayList;
import java.util.HashMap;

import uk.co.plogic.gwt.lib.events.MouseClickEvent;
import uk.co.plogic.gwt.lib.events.MouseOutEvent;
import uk.co.plogic.gwt.lib.events.MouseOutEventHandler;
import uk.co.plogic.gwt.lib.events.MouseOverEvent;
import uk.co.plogic.gwt.lib.events.MouseOverEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;


/**
 * 
 * Setup the DOM to fire the {@link uk.co.plogic.gwt.lib.events.MouseOverEvent},  
 * {@link uk.co.plogic.gwt.lib.events.MouseOutEvent} and {@link uk.co.plogic.gwt.lib.events.MouseClickEvent}
 * events when an element with the given class is hovered over. The activeClassName css class is added to
 * elements when they are moused over.
 * 
 * The element with the given class must also have the class suffixed with _<id>.
 * e.g.
 * <p class="mouse_over mouse_over_1">one</p>
 * 
 * Having two classes makes styling easy as all elements in a group can be styled together
 * and the active item can be highlighted.
 * 
 * 
 * @author si
 *
 */
public class AttachActiveElementsEvent {

	private HashMap<String, ArrayList<Element>> mouseOverElements = new HashMap<String, ArrayList<Element>>();
	private final String activeClassName;
	/**
	 * 
	 * @param className 
	 */
	public AttachActiveElementsEvent(final HandlerManager eventBus, final String className,
								final String activeClassName ) {

		
		this.activeClassName = activeClassName;
		

		eventBus.addHandler(MouseOverEvent.TYPE, new MouseOverEventHandler() {
			@Override
			public void onMouseOver(MouseOverEvent e) {
				setElementActive(true, e.getMouseOver_id());
			}
		});

		eventBus.addHandler(MouseOutEvent.TYPE, new MouseOutEventHandler() {
			@Override
			public void onMouseOut(MouseOutEvent e) {
				setElementActive(false, e.getMouseOut_id());
			}
		});


		DomParser domParser = new DomParser();
	    domParser.addHandler(new DomElementByClassNameFinder(className) {

	    	
	        @Override
	        public void onDomElementFound(Element element, String id) {

	        	String mouseOverID = null;
	        	
	        	// find additional class name, i.e. "1" in class="mouse_over mouse_over_1"
	        	for( String aClass : element.getClassName().split(" ") ) {
	        		if( aClass.startsWith(className+'_')) {
	        			mouseOverID = aClass.substring(className.length()+1);
	        			if( ! mouseOverElements.containsKey(mouseOverID) ) {
	        				mouseOverElements.put(mouseOverID, new ArrayList<Element>());
	        			}
	        			mouseOverElements.get(mouseOverID).add(element);
	        			break;
	        		}
	        	}

	        	if( mouseOverID != null ) {
	        		
	        		final String mouseOverID_f = mouseOverID;
	        		
		        	Event.setEventListener(element, new EventListener() {
		                
		                @Override
		                public void onBrowserEvent(Event event) {
		                    switch (DOM.eventGetType(event)) {
		                    case Event.ONMOUSEOVER:
		                    	eventBus.fireEvent(new MouseOverEvent(mouseOverID_f));
		                        break;
		                    case Event.ONMOUSEOUT:
		                    	eventBus.fireEvent(new MouseOutEvent(mouseOverID_f));
	                        break;
		                    case Event.ONCLICK:
		                    	eventBus.fireEvent(new MouseClickEvent(mouseOverID_f));
	                        break;
		                    }
		                }
		            });
		            
		            Event.sinkEvents(element, Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK);
	        	}
	        }
	    });
	    domParser.parseDom();
	}


	/**
	 * Add remove "active" class into mouseover element
	 * @param active
	 * @param mouseOverID
	 */
	protected void setElementActive(boolean active, String mouseOverID) {
		if( mouseOverElements.containsKey(mouseOverID) ) {
			for( Element ee : mouseOverElements.get(mouseOverID) ) {
				if(active) ee.addClassName(activeClassName);
				else ee.removeClassName(activeClassName);
			}
		}

	}

}
