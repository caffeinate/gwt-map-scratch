package uk.co.plogic.gwt.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class CarouselDemo implements EntryPoint {

	final Logger logger = Logger.getLogger("Carousel");

	@Override
	public void onModuleLoad() {

		RootPanel r = RootPanel.get("container");

		DomParser domParser = new DomParser();
		final ArrayList<Element> carouselElements = new ArrayList<Element>();
	    domParser.addHandler(new DomElementByClassNameFinder("carousel") {
	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	carouselElements.add(element);
	        }
	    });
	    domParser.parseDom();

	    for(Element e : carouselElements) {
			Carousel c = new Carousel();
			r.add(c);
			c.pagesFromDomElement(e);
	    }

	}
}
