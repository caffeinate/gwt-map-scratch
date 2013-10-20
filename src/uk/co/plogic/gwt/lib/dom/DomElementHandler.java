package uk.co.plogic.gwt.lib.dom;

import com.google.gwt.user.client.Element;

/**
 * All DOM element handlers must extends this class. 
 *
 * This class is compare each element in the DOM to see if it is one
 * that if of interest. If the element is of interest the onDomElementFound
 * method is called. 
 *
 */
public abstract class DomElementHandler {
    
    
    private static final String DEFAULT_ID_NAME = "__dom_id_";
    private static int nextDomId = 1;
    
    
    /**
     * Called to see if the element is of interest.
     * 
     * @param element DOM element
     */
    public void processElement(Element element) {
        if (isElementSearchingFor(element)) {
            onDomElementFound(element, getId(element));
        }
    }

    /**
     * Called when a DOM element we are interested in has been found.
     * 
     * @param element - DOM element.
     * @param id - ID of element. 
     */
    public abstract void onDomElementFound(Element element, String id);
    
    /**
     * Return true if the DOM element is one we are interested in.
     * 
     * @param element
     * @return
     */
    protected abstract boolean isElementSearchingFor(Element element);
    
    
    // Gets the ID of the DOM element. If none exist one is given to it.
    private String getId(Element element) {
        String id = element.getId();
        if ((id == null) || (id.equals(""))) {
            id = DEFAULT_ID_NAME + nextDomId;
            nextDomId++;
            element.setId(id);
        }
        return id;
    }
}
