package uk.co.plogic.gwt.lib.dom.forge;

import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;

import com.google.gwt.dom.client.Element;

/**
 * Util to show and hide DOM elements by id
 * @author si
 *
 */
public class ShowHide {

	private Element e;
	
	//TODO this class assumes there isn't an existing element style
	
	/**
	 * contructor for when you have the element already
	 * @param ee
	 */
	public ShowHide(Element ee) {
		e = ee;
	}
	
	
	/**
	 * get element by id
	 * @param elementID
	 * @param show
	 */
	public ShowHide(final String elementID) {
		DomParser domParser = new DomParser();
	    domParser.addHandler(new DomElementByAttributeFinder("id", elementID) {

	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	e = element;
	        }
	    });
	    domParser.parseDom();
	}
	
	public void show() {
		e.removeAttribute("style");
	}
	
	public void hide() {
		e.setAttribute("style", "display:none;");
	}
	

}
