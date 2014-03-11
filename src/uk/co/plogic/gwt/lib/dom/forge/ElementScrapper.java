package uk.co.plogic.gwt.lib.dom.forge;

import java.util.ArrayList;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

public class ElementScrapper {
	
	public String findOverlayId(Element element) {
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
    			return(overlayMetaElement.getInnerText().trim());
    		}
    	}
    	return null;
	}

}
