package uk.co.plogic.gwt.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;


public class CarouselDemo implements EntryPoint {

	final Logger logger = Logger.getLogger("Carousel");

	@Override
	public void onModuleLoad() {

		Element containerElement = Document.get().getElementById("container");
		final HTMLPanel infoPanelContent = HTMLPanel.wrap(containerElement);

		DomParser domParser = new DomParser();
		final ArrayList<Element> carouselElements = new ArrayList<Element>();
	    domParser.addHandler(new DomElementByClassNameFinder("carousel") {
	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	carouselElements.add(element);
	        }
	    });
	    domParser.parseDom(containerElement);

	    final ArrayList<Carousel> Carousels = new ArrayList<Carousel>();
	    for(Element e : carouselElements) {
	    	// Carousel removes header and page items from this element
	    	// anything else will be left
			Carousel c = new Carousel(e);
			infoPanelContent.add(c, e);
			Carousels.add(c);
	    }

	    Window.addResizeHandler(new ResizeHandler() {

			  Timer resizeTimer = new Timer() {
				  @Override
				  public void run() {
					  int windowHeight = (int) (((double) Window.getClientHeight())*0.9);
					  logger.info("Window resize being passed to carousels. Height="+windowHeight);
					  infoPanelContent.setHeight(windowHeight+"px");

					  for(Carousel c : Carousels) {
						  c.onResize();
					  }
				  }
			  };

			  @Override
			  public void onResize(ResizeEvent event) {
				  resizeTimer.cancel();
				  resizeTimer.schedule(200);
			  }
		});

	}
}
