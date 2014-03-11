package uk.co.plogic.gwt.lib.dom;

import com.google.gwt.dom.client.Element;

/**
 * Utility class for processing on DOM elements
 *
 */
public class DomElementUtils {

    private DomElementUtils() {
        // Object should not be constructed
    }

    public static boolean isClassNameInElement(Element element, String className) {
        String classNames = element.getClassName();
        if (!"".equals(classNames)) {
            for(String name : classNames.split(" ")) {
                if (className.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    public static boolean isTagNameInElement(Element element, String tagName) {
        return tagName.equalsIgnoreCase(element.getTagName());
    }


    public static boolean isAttributeInElement(Element element, 
                                                String attribute, 
                                                String value) {
    	
    	// known cross browser issue with getAttribute(). work around
    	// for class
    	//String msg = "Generalised method ! X browser. Only for 'class' at present";
    	//assert attribute.equalsIgnoreCase("class") : msg;
    	
    	if( attribute.equalsIgnoreCase("class") ) {
    		return element.getClassName().equalsIgnoreCase(value);
    	} else {
    		// the dom version is apparently more cross browser safe
    		// if this doesn't work, try com.google.gwt.dom.client.Element;
    		return element.getAttribute(attribute).equalsIgnoreCase(value);
    	}
    	
    	// general version looks like this-
        //return element.hasAttribute(attribute) && 
        //       value.equalsIgnoreCase(element.getAttribute(attribute));
    }
    
}
