package uk.co.plogic.gwt.lib.ui.activatedElements;

import java.util.HashMap;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.events.ActiveUpdateElementEvent;
import uk.co.plogic.gwt.lib.events.ActiveUpdateElementEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;


/**
 * 
 * Find and keep a mapping of all the elements with a given class. Listen for the
 * ActiveUpdateElementEvent event which broadcasts new innerHTML and the element's ID. Update the
 * element when a matching id is found.
 * 
 * Note that if the DOM changes to loose elements referenced here then I have no idea
 * what will happen but I'd imagine nothing.
 * 
 * @author si
 *
 */
public class ActiveUpdateElement {
	
	HashMap<String, Element> idElementMap = new HashMap<String, Element>();


	/**
	 * 
	 * @param className 
	 */
	public ActiveUpdateElement(DomParser domParser, final HandlerManager eventBus, final String elementClass) {

	    domParser.addHandler(new DomElementByClassNameFinder(elementClass) {

	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	idElementMap.put(id, element);
	        }

	    });
	    
	    eventBus.addHandler(ActiveUpdateElementEvent.TYPE, new ActiveUpdateElementEventHandler() {

			@Override
			public void onUpdate(ActiveUpdateElementEvent e) {
				
				if( idElementMap.containsKey(e.getElement_id()) ) {
					idElementMap.get(e.getElement_id()).setInnerHTML(e.getNewInnerHtml());
				}
				
			}

		});

	}

}
