package uk.co.plogic.gwt.lib.dom;

import java.util.ArrayList;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This parses the DOM and "listens" for elements that have the given classes.
 * 
 */
public class DomParser {

    private ArrayList<DomElementHandler> handlers = 
        new ArrayList<DomElementHandler>();
    
    public void addHandler(DomElementHandler handler) {
        handlers.add(handler);
    }
    
    public void parseDom() {
        parseDom(RootPanel.getBodyElement());
    }
    
    public void parseDom(String id) {
        parseDom(RootPanel.get(id).getElement());
    }

    public void parseDom(Element element) {
        checkElement(element);
        int total = DOM.getChildCount(element);
        for(int i=0; i<total; i++) {
            parseDom(DOM.getChild(element, i));
        }
    }
    
    private void checkElement(Element element) {
        for(DomElementHandler handler : handlers) {
            handler.processElement(element);
        }
    }
}
