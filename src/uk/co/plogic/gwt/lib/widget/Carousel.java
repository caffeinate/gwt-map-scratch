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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Widget which holds other widgets which can be rotated through showing
 * one at a time.
 * 
 * The width and height need to be set in pixels. This can be done with
 * setSize() or setSizingWidget().
 * 
 * 
 * @author si
 *
 */
public class Carousel extends Composite implements RequiresResize, ProvidesResize {

	final Logger logger = Logger.getLogger("Carousel");
	FocusPanel holdingPanel = new FocusPanel();
	AbsolutePanel viewport = new AbsolutePanel();

	HTML fixedHeader; // optional - when it exists, it is added to viewport
	int headerOffset = 0; // if there is a fixed header section

	// navigation- automatically visible on multi page
	HorizontalPanel fixedFooter;
	HorizontalPanel dotsPanel;
	int footerOffset = 20; // height of fixed footer section - TODO, possible with just CSS?

	private int width = 1;
	private int height = 1;

	private Widget scale_widget;
	private Element scale_element;
	private double heightScale = 1; // percent of parent panel's height this
	private double widthScale = 1;  // should be.
	private int widthAdjust = 0;   // pixel adjustments, can't be CSS as is responsive
	private int heightAdjust = 0;

	int currentWidget = 0;
	int visibleWidgetsCount = 0;
	ArrayList<Widget> widgets = new ArrayList<Widget>();
	static int animationDuration = 350;
	
	final String CAROUSEL_PAGE_CLASS = "carousel_page";
	final String CAROUSEL_HEADER_CLASS = "carousel_header";
	final String CAROUSEL_FOOTER_CLASS = "carousel_footer";
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
	    holdingPanel.addAttachHandler(new Handler(){
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				
				if( event.isAttached() ) {
					logger.finer("just got attached "+viewport.getOffsetHeight()+" "+holdingPanel.getOffsetHeight());
					onResize();
				} else {
					logger.finer("just got detached");
				}
			}
	    });
		initWidget(holdingPanel);

	    setupControls();
	    viewport.add(fixedFooter, 0, height-footerOffset);
	}

	/**
	 * Set fixed size
	 * use setSize() or setSizingWidget(), not both
	 * @param width
	 * @param height
	 */
	public void setSizing(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * set a widget to stay in proportion to.
   	 * use setSize() or setSizingWidget(), not both
   	 * 
   	 * The HTML attributes 'data-height' and 'data-width' set the proportions.
   	 * see pagesFromDomElement
   	 * 
	 * @param w
	 * @param scale_height
	 * @param scale_width
	 */
	public void setSizing(Widget w) {
		scale_widget = w;
	}
	
	public void setSizing(Element e) {
		scale_element = e;
	}

	/**
	 * Remove header (CAROUSEL_HEADER_CLASS) and page (CAROUSEL_PAGE_CLASS)
	 * elements from parentElement. Add classes back into the widgets that
	 * are constructed from the child elements.
	 * @param parentElement
	 */
	private void pagesFromDomElement(Element parentElement) {

		for( String att : new String [] {"data-height", "data-width"} ) {
			String domAttribute = parentElement.getAttribute(att);
			if(domAttribute != null && domAttribute.length() > 0) {
				if( domAttribute.endsWith("%")) {
					int clipTo = domAttribute.length()-1;
					String numb = domAttribute.substring(0, clipTo);
					Double pc = Double.parseDouble(numb) / 100;
					
					if(att.equals("data-height")) heightScale = pc;
					else						  widthScale = pc;

				} else if(domAttribute.endsWith("px")) {
					int clipTo = domAttribute.length()-2;
					String numb = domAttribute.substring(0, clipTo);
					int px = Integer.parseInt(numb);

					if(att.equals("data-height")) heightAdjust = px;
					else						  widthAdjust = px;

				} else {
					logger.warning("carousel attribute ["+att+"] must end with '%' or 'px'");				
				}
			}

		}

		DomParser domParser = new DomParser();
		final ArrayList<Element> doomedDomElements = new ArrayList<Element>();
	    domParser.addHandler(new DomElementByClassNameFinder(CAROUSEL_PAGE_CLASS) {
	        @Override
	        public void onDomElementFound(Element e, String id) {
	        	HTML page = new HTML(e.getInnerHTML());
	        	page.setStyleName(CAROUSEL_PAGE_CLASS);
	        	doomedDomElements.add(e);
		    	addWidget(page);

				// maybe all carousel_page items should have these in their CSS?
				String eStyle = e.getAttribute("style");
				page.getElement().setAttribute("style", eStyle+"overflow:auto;");
				page.setWidth("100%");
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
	@Override
	public void onResize() {

		if( scale_widget != null ) {
			width = (int) (((double) scale_widget.getOffsetWidth() ) * widthScale);
			height = (int) (((double) scale_widget.getOffsetHeight() ) * heightScale);
		} else if( scale_element != null ) {
			width = (int) (((double) scale_element.getOffsetWidth() ) * widthScale);
			height = (int) (((double) scale_element.getOffsetHeight() ) * heightScale);
		}

		width += widthAdjust;
		height += heightAdjust;

		viewport.setPixelSize(width, height);
	    logger.finer("Resize with "+width+"x"+height);

	    if(fixedHeader!=null) headerOffset = fixedHeader.getOffsetHeight();
	    else 				  headerOffset = 0;


	    int contentsHeight = height-headerOffset;

	    visibleWidgetsCount = 0;
	    for(int i=0; i<widgets.size(); i++) {
	    	Widget w = widgets.get(i);
	    	if( w.isVisible() )
	    		visibleWidgetsCount++;
	    }

	    // current widget has just gone invisible
	    if( ! widgets.get(currentWidget).isVisible())
    		moveTo(1); // choose next one that is visible
	    
	    if( visibleWidgetsCount > 1 ) {
	    	//viewport.add(fixedFooter, 0, height-footerOffset);
	    	viewport.setWidgetPosition(fixedFooter, 0, height-footerOffset);
	    	fixedFooter.setVisible(true);
	    	contentsHeight -= footerOffset;
	    } else {
	    	fixedFooter.setVisible(false);
	    }
	    updateControls();

	    if( contentsHeight<1 ) contentsHeight = 1;

	    for(int i=0; i<widgets.size(); i++) {
	    	Widget w = widgets.get(i);
	    	w.setHeight(""+contentsHeight+"px");

	    	if (w instanceof RequiresResize) {
	            ((RequiresResize) w).onResize();
	        }

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

		fixedFooter = new HorizontalPanel();
		dotsPanel = new HorizontalPanel();
		
	    Button previous = new Button("<");
	    previous.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(-1);
			}
	    });
	    Button next = new Button(">");
	    next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				moveTo(1);
			}
	    });

		fixedFooter.setStyleName(CAROUSEL_FOOTER_CLASS);
		fixedFooter.setHeight(footerOffset+"px");
		fixedFooter.add(previous);
		fixedFooter.add(dotsPanel);
		fixedFooter.add(next);

	}
	
	private void updateControls() {
		dotsPanel.clear();
		for(int i=0; i<visibleWidgetsCount; i++) {
			dotsPanel.add(new HTML(" o "));
		}
	}

	/**
	 * Move one place. Plan is to make this capable of moving to arbitrary
	 * position. For now, just + or - 1 place. The hide on invisible feature
	 * for pages means a little more thought is needed.
	 * 
	 * @param direction 1 or -1
	 */
	public void moveTo(int direction) {

		if( visibleWidgetsCount < 2 || direction < -1 || direction > 1)
			return;

		logger.info("current:"+currentWidget+" direction:"+direction);
		
		int widgetsCount = widgets.size();
		int widgetToShowIndex = currentWidget;
		do {
			widgetToShowIndex -= direction;
			if( widgetToShowIndex < 0 ) widgetToShowIndex = widgetsCount-1;
			if( widgetToShowIndex > widgetsCount-1 ) widgetToShowIndex = 0;
		} while(! widgets.get(widgetToShowIndex).isVisible());

		// position widgetToShow to one side of viewpoint
		Widget widgetToShow = widgets.get(widgetToShowIndex);
		viewport.setWidgetPosition(widgetToShow, width*direction, headerOffset);
		
		Widget current = widgets.get(currentWidget);
		AnimateViewpoint av = new AnimateViewpoint( direction*-1, widgetToShow, current);
		av.run(animationDuration);
		currentWidget = widgetToShowIndex;
		logger.info("new:"+currentWidget);
	}

	public void addWidget(Widget w) {

		widgets.add(w);

		if( w.isVisible() )
    		visibleWidgetsCount++;

		// put it somewhere out of sight
		viewport.add(w, 0, height+10);
	}


}
