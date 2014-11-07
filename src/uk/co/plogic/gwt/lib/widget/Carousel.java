package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Widget which holds other widgets which can be rotated through showing
 * one at a time.
 * 
 * @author si
 *
 */
public class Carousel extends Composite {

	final Logger logger = Logger.getLogger("Carousel");
	FocusPanel holdingPanel = new FocusPanel();
	AbsolutePanel viewport = new AbsolutePanel();
	HTML fixedHeader; // optional - when it exists, it is added to viewport
	int width = 0;
	int height = 0;
	int headerOffset = 0; // if there is a fixed header section
	int currentWidget = 0;
	int widgetCount = 0;
	ArrayList<Widget> widgets = new ArrayList<Widget>();
	static int animationDuration = 350;
	
	final String CAROUSEL_PAGE_CLASS = "carousel_page";
	final String CAROUSEL_HEADER_CLASS = "carousel_header";
	final String CAROUSEL_CLASS = "carousel";

	class AnimateViewpoint extends Animation {

		int direction; Widget w1; Widget w2; double w1_start; double w2_start;

		public AnimateViewpoint(int direction, Widget w1, Widget w2) {
			this.direction = direction;
			this.w1 = w1;
			this.w2 = w2;
			w1_start = viewport.getWidgetLeft(w1);
			w2_start = viewport.getWidgetLeft(w2);
		}

		@Override
		protected void onUpdate(double progress) {
			int currentPos = (int) (w1_start + (width * progress * direction));
			viewport.setWidgetPosition(w1, currentPos, headerOffset);
			currentPos = (int) (w2_start + (width * progress * direction));
			viewport.setWidgetPosition(w2, currentPos, headerOffset);
		}

	}

	public Carousel(Element e) {
		this();
		pagesFromDomElement(e);
	}
	public Carousel() {
		//viewport.addStyleName("carousel_viewpoint");
		holdingPanel.addStyleName(CAROUSEL_CLASS);
	    holdingPanel.add(viewport);
	    holdingPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(1);
			}
	    });
	    holdingPanel.addAttachHandler(new Handler(){
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				logger.finer("just got attached "+viewport.getOffsetHeight()+" "+holdingPanel.getOffsetHeight());
				onResize();
			}
	    });
		initWidget(holdingPanel);
	    //setupControls();
	}

	/**
	 * Remove header (CAROUSEL_HEADER_CLASS) and page (CAROUSEL_PAGE_CLASS)
	 * elements from parentElement. Add classes back into the widgets that
	 * are constructed from the child elements.
	 * @param parentElement
	 */
	private void pagesFromDomElement(Element parentElement) {

		DomParser domParser = new DomParser();
		final ArrayList<Element> doomedDomElements = new ArrayList<Element>();
	    domParser.addHandler(new DomElementByClassNameFinder(CAROUSEL_PAGE_CLASS) {
	        @Override
	        public void onDomElementFound(Element e, String id) {
	        	HTML page = new HTML(e.getInnerHTML());
	        	page.setStyleName(CAROUSEL_PAGE_CLASS);
	        	doomedDomElements.add(e);
		    	addWidget(page);
	        }
	    });
	    domParser.addHandler(new DomElementByClassNameFinder(CAROUSEL_HEADER_CLASS) {
	        @Override
	        public void onDomElementFound(Element e, String id) {
	        	
	        	fixedHeader = new HTML(e.getInnerHTML());
	        	fixedHeader.setStyleName(CAROUSEL_HEADER_CLASS);
	        	doomedDomElements.add(e);
	        	viewport.add(fixedHeader, 0, 0);
	        }
	    });
	    domParser.parseDom(parentElement);

	    for(Element e : doomedDomElements) {
	    	e.removeFromParent();
	    }
	}

	/**
	 * redraw widget and pages within the carousel
	 */
	private void onResize() {
	    width = holdingPanel.getOffsetWidth();
	    height = holdingPanel.getOffsetHeight();
	    viewport.setPixelSize(width, height);
	    
	    logger.finer("Resize with "+width+"x"+height);

	    if(fixedHeader!=null) headerOffset = fixedHeader.getOffsetHeight();
	    else 				  headerOffset = 0;

	    int contentsHeight = height-headerOffset;
	    for(int i=0; i<widgets.size(); i++) {
	    	Widget w = widgets.get(i);
	    	w.setHeight(""+contentsHeight+"px");
			if( i == currentWidget ) {
				// visible
				viewport.setWidgetPosition(w, 0, headerOffset);
			} else {
				// ensure it's hidden
				viewport.setWidgetPosition(w, 0, height);
			}
	    }
	}

	private void setupControls() {

	    Button previous = new Button("Previous");
	    previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(-1);
			}
	    });
	    Button next = new Button("Next");
	    next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(1);
			}
	    });
	    holdingPanel.add(previous);
	    holdingPanel.add(next);

	}

	public void moveTo(int direction) {
		
		int widgetToShowIndex = currentWidget-direction;
		if( widgetToShowIndex < 0 ) widgetToShowIndex = widgetCount-1;
		if( widgetToShowIndex > widgetCount-1 ) widgetToShowIndex = 0;
		
		// position widgetToShow to one side of viewpoint
		Widget widgetToShow = widgets.get(widgetToShowIndex);
		viewport.setWidgetPosition(widgetToShow, width*direction, headerOffset);
		
		Widget current = widgets.get(currentWidget);
		AnimateViewpoint av = new AnimateViewpoint( direction*-1, widgetToShow, current);
		av.run(animationDuration);
		currentWidget = widgetToShowIndex;
	}

	public void addWidget(Widget w) {

		widgets.add(w);
		widgetCount = widgets.size();

		// put it somewhere out of sight
		viewport.add(w, 0, height+10);
	}


}
