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
import com.google.gwt.user.client.ui.FlowPanel;
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
	int width = 0;
	int height = 0;
	int headerOffset = 0; // if there is a fixed header section
	int currentWidget = -1;
	int widgetCount = 0;
	ArrayList<Widget> widgets = new ArrayList<Widget>();
	static int animationDuration = 350;
	Element parentElement;
	ArrayList<Element> pages = new ArrayList<Element>();
	
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

	public Carousel() {

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
				logger.info("just got attached "+viewport.getOffsetHeight());
				
			}
	    	
	    });
		initWidget(holdingPanel);
	    //setupControls();

	}

	public void pagesFromDomElement(Element e) {


		parentElement = e;

		DomParser domParser = new DomParser();
	    domParser.addHandler(new DomElementByClassNameFinder("carousel_page") {
	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	pages.add(element);
	        }
	    });
	    domParser.parseDom(parentElement);

	    width = parentElement.getOffsetWidth();
	    height = parentElement.getOffsetHeight();
	    viewport.setPixelSize(width, height);

    	// remove pages from parent and set them up
    	ArrayList<FlowPanel> pagePanels = new ArrayList<FlowPanel>();
	    for(Element p: pages) {
	    	FlowPanel fp = new FlowPanel();
	    	Element fpe = fp.getElement();
	    	// TODO - copy any other attributes?
	    	fpe.setClassName(p.getClassName());
	    	fpe.setId(p.getId());
	    	fpe.setInnerHTML(p.getInnerHTML());
	    	p.removeFromParent();
	    	pagePanels.add(fp);
	    }

	    Element vpe = viewport.getElement();
    	// TODO - copy any other attributes?
    	vpe.setClassName(parentElement.getClassName());
    	vpe.setId(parentElement.getId());

    	String headerCopy = parentElement.getInnerHTML().trim();
    	parentElement.setInnerHTML("");
    	if( headerCopy.length() > 0 ) {
    		HTML fixedHeader = new HTML(headerCopy);
    		viewport.add(fixedHeader, 0, 0);
    		// TODO set header's height on page ready or page resize
    		headerOffset = fixedHeader.getOffsetHeight();
    	}
    	//parentElement.removeFromParent();	    

    	int contentsHeight = height-headerOffset;
	    for(FlowPanel fp : pagePanels) {
	    	fp.setHeight(""+contentsHeight+"px");
	    	addWidget(fp);
	    }
		logger.info("running pagesFromDomElement "+viewport.getOffsetHeight());
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
		if( currentWidget == -1 ) {
			currentWidget = 0;
			viewport.add(w, 0, headerOffset);
		} else {
			// put it somewhere out of sight
			viewport.add(w, 0, height+10);
		}
		widgetCount = widgets.size();
	}


}
