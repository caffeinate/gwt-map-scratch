package uk.co.plogic.gwt.lib.dom;

import com.google.gwt.dom.client.Element;

/**
 * Finds DOM elements with a given attribute.
 *
 */
public abstract class DomElementByAttributeFinder extends DomElementHandler {

    private String attribute;
    private String value;

    public DomElementByAttributeFinder(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }
    
    
    /**
     * This checks a DOM element and if it is one we are interested
     * 
     * @param element
     */
    @Override
    protected boolean isElementSearchingFor(Element element) {
    	return DomElementUtils.isAttributeInElement(element, attribute, value);
    }
    
}
