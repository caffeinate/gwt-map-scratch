package uk.co.plogic.gwt.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;
import uk.co.plogic.gwt.lib.widget.Carousel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;


public class CarouselDemo implements EntryPoint {

	final Logger logger = Logger.getLogger("Carousel");
	final ArrayList<Carousel> Carousels = new ArrayList<Carousel>();

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


	    for(Element e : carouselElements) {
	    	// Carousel removes header and page items from this element
	    	// anything else will be left
			Carousel c = new Carousel(e);
			infoPanelContent.add(c);//, e);
			Carousels.add(c);
	    }

	    // first carousel will be 50% size of infoPanelContent and the HTML
	    // element attribute 'data-height="40%"' means it will be 40% of this
	    // space.
	    // second will be fixed size
	    ResponsiveSizing rs = new ResponsiveSizing(200,300);
	    Carousels.get(0).setSizing(rs);
	    Carousels.get(1).setSizing(rs);

	    // programmatic way to create a carousel...
	    final Carousel c = new Carousel();
	    c.setSizing(rs);

	    // add pages
	    final HTML h1 = new HTML("I'm h1");
	    h1.setStyleName("orange");
	    c.addWidget(h1, null, null);
	    HTML h2 = new HTML("I'm h2");
	    h2.setStyleName("blue");
	    c.addWidget(h2, null, null);
	    HTML h3 = new HTML("I'm h3");
	    h3.setStyleName("green");
	    c.addWidget(h3, null, null);

	    // attaching to DOM adjusts carousel to fit pages
	    infoPanelContent.add(c);
	    
	    Button toggle = new Button("Toggle Orange Page Visibility");
	    toggle.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				h1.setVisible(!h1.isVisible());
				c.onResize();
			}
	    });
	    infoPanelContent.add(toggle);
	    
	    repaint(infoPanelContent);

	    Window.addResizeHandler(new ResizeHandler() {

			  Timer resizeTimer = new Timer() {
				  @Override
				  public void run() {
					  repaint(infoPanelContent);
				  }
			  };

			  @Override
			  public void onResize(ResizeEvent event) {
				  resizeTimer.cancel();
				  resizeTimer.schedule(200);
			  }
		});

	}

	private void repaint(HTMLPanel i) {
		int windowHeight = (int) (((double) Window.getClientHeight())*0.9);
		i.setHeight(windowHeight+"px");

		for(Carousel c : Carousels) {
			c.onResize();
		}
	}
}
