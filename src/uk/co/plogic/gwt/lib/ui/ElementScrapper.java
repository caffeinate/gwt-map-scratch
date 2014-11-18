package uk.co.plogic.gwt.lib.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

// use HTML 5 data- attribute instead
@Deprecated
public class ElementScrapper {
	
	public String findOverlayId(Element element, String tagName, String className) {
		// messy looking - just finds the overlay ID
        NodeList<Element> overlayMetaData = element.getElementsByTagName(tagName);
    	for( int ii=0; ii<overlayMetaData.getLength(); ii++ ) {
    		Element overlayMetaElement = (Element) overlayMetaData.getItem(ii);
    		String aClassName = overlayMetaElement.getClassName();
    		ArrayList<String> allClasses = new ArrayList<String>();
    		// handle multiple classes in class="..."
    		if( aClassName.contains(" ") ) {
    			for( String cn : aClassName.split(" ") ) {
    				allClasses.add(cn);
    			}
    		} else {
    			allClasses.add(aClassName);
    		}
    		
    		if( allClasses.contains(className) ) {
    			return(overlayMetaElement.getInnerText().trim());
    		}
    	}
    	return null;
	}

}
