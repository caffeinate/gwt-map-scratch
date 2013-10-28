package uk.co.plogic.gwt.lib.dom;

import com.google.gwt.user.client.Element;

/**
 * Finds DOM elements with a given class.
 *
 */
public abstract class DomElementByClassNameFinder extends DomElementHandler {

    private String className;

    public DomElementByClassNameFinder(String className) {
        this.className = className;
    }


    @Override
    protected boolean isElementSearchingFor(Element element) {
        return DomElementUtils.isClassNameInElement(element, className);
    }

    
    
}
