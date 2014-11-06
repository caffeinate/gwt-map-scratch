package uk.co.plogic.gwt.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


public class CarouselDemo implements EntryPoint {

	final Logger logger = Logger.getLogger("Carousel");

	@Override
	public void onModuleLoad() {

		Element containerElement = Document.get().getElementById("container");
		HTMLPanel infoPanelContent = HTMLPanel.wrap(containerElement);

//		Element tc = Document.get().getElementById("pp");
//		
//		FocusPanel fp = new FocusPanel();
//		
//		fp.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				logger.info("got a focus panel click");
//			}
//	    });
//		
////		fp.add(new HTML("aaaaaaaaaaaa"));
//		
//		infoPanelContent.add(fp, tc);
//		
		
		//containerElement.removeFromParent();

//		RootPanel r = RootPanel.get();
//		r.add(infoPanelContent);

		DomParser domParser = new DomParser();
		final ArrayList<Element> carouselElements = new ArrayList<Element>();
	    domParser.addHandler(new DomElementByClassNameFinder("carousel") {
	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	carouselElements.add(element);
	        }
	    });
	    domParser.parseDom(containerElement);


	    for(Element e : carouselElements) {
			Carousel c = new Carousel();
			c.pagesFromDomElement(e);
	    	//e.removeFromParent();
			infoPanelContent.add(c, e.getId());			
	    }

	}
}
