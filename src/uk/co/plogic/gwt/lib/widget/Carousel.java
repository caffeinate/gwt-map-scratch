package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveLayoutImageResource;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
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
	ResponsiveLayoutImageResource images;

	HorizontalPanel fixedHeader; // optional - when it exists, it is added to viewport
	int headerOffset = 0; // if there is a fixed header section

	// navigation- automatically visible on multi page
	boolean showFooter = true;
	FlowPanel fixedFooter;
	HorizontalPanel dotsPanel;
	int footerOffset = 24; // height of fixed footer section - TODO, possible with just CSS?

	int width = 1;
	int height = 1;

	Widget scale_widget;
	Element scale_element;
	double heightScale = 1; // percent of parent panel's height this
	double widthScale = 1;  // should be.
	int widthAdjust = 0;   // pixel adjustments, can't be CSS as is responsive
	int heightAdjust = 0;

	int currentWidget = 0;
	int visibleWidgetsCount = 0;
	// order of pages matters so use ArrayList
	ArrayList<Widget> widgets = new ArrayList<Widget>();
	// id -> {element, Widget}
	HashMap<String, WidgetElement> originalElements = new HashMap<String, WidgetElement>();
	static int animationDuration = 350;

	public static String CAROUSEL_PAGE_CLASS = "carousel_page";
	public static String CAROUSEL_HEADER_CLASS = "carousel_header";
	public static String CAROUSEL_FOOTER_CLASS = "carousel_footer";
	public static String CAROUSEL_CLASS = "carousel";

	class WidgetElement {
		Widget w;
		Element e;
		public WidgetElement(Widget w, Element e) {
			this.w = w;
			this.e = e;
		}
	}
	
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
		images = GWT.create(ResponsiveLayoutImageResource.class);
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
	    if( showFooter )
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
	 * Used when carousels are being responsively scaled based on the size
	 * of another widget @see setSizing(Widget).
	 * These values are also taken from the dom when using the Element
	 * constructor, @see pagesFromDomElement
	 * 
	 * @param widthScale
	 * @param heightScale
	 */
	public void setScaleFactor(double widthScale, double heightScale) {
		this.heightScale = heightScale;
		this.widthScale = widthScale;
	}
	
	/**
	 * positive or negative to responsively adjust the size of carousels
	 * which are based on the size of another widget. @see setScaleFactor()
	 * @param heightAdjust
	 * @param widthAdjust
	 */
	public void setPixelAdjustments(int heightAdjust, int widthAdjust) {
		this.heightAdjust = heightAdjust;
		this.widthAdjust = widthAdjust;
	}

	/**
	 * Remove header (CAROUSEL_HEADER_CLASS) and page (CAROUSEL_PAGE_CLASS)
	 * elements from parentElement. Add classes back into the widgets that
	 * are constructed from the child elements.
	 * @param parentElement
	 */
	protected void pagesFromDomElement(Element parentElement) {

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
		    	addWidget(id, page, e);

				// maybe all carousel_page items should have these in their CSS?
				String eStyle = e.getAttribute("style");
				page.getElement().setAttribute("style", eStyle+"overflow:auto;");
				page.setWidth("100%");
	        }
	    });
	    domParser.addHandler(new DomElementByClassNameFinder(CAROUSEL_HEADER_CLASS) {
	        @Override
	        public void onDomElementFound(Element e, String id) {
	        	
	        	fixedHeader = new HorizontalPanel(); 
	        	HTML h = new HTML(e.getInnerHTML());
	        	fixedHeader.add(h);
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

		if( width < 0 || height < 0 ) {
			// save some CPU time when the browser hasn't quite finished firing up
			logger.finer("ignoring carousel resize");
			return;
		}

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
	    if( widgets.size()>0 && ! widgets.get(currentWidget).isVisible())
    		moveTo(1, nextWidgetIndex(1), true); // choose next one that is visible
	    
	    if( showFooter && visibleWidgetsCount > 1) {
	    	//viewport.add(fixedFooter, 0, height-footerOffset);
	    	viewport.setWidgetPosition(fixedFooter, 0, height-footerOffset);
	    	fixedFooter.setVisible(true);
	    	contentsHeight -= footerOffset;
	    } else {
	    	fixedFooter.setVisible(false);
	    }
	    updateControls(currentWidget);

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

		fixedFooter = new FlowPanel();
		FlowPanel footerContainer = new FlowPanel();
		footerContainer.setStyleName("carousel_footer_container");
		fixedFooter.add(footerContainer);
		HorizontalPanel navPanel = new HorizontalPanel();
		navPanel.setStyleName("carousel_footer_centre");
		footerContainer.add(navPanel);
		dotsPanel = new HorizontalPanel();
		
		Image previous = new Image(images.leftArrow());
		previous.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				moveTo(-1, nextWidgetIndex(-1), true);
			}
		});

		Image next = new Image(images.rightArrow());
		next.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				moveTo(1, nextWidgetIndex(1), true);
			}
		});

		fixedFooter.setStyleName(CAROUSEL_FOOTER_CLASS);
		fixedFooter.setHeight(footerOffset+"px");
		navPanel.add(previous);
		navPanel.add(dotsPanel);
		navPanel.add(next);

	}
	
	protected void updateControls(int selectedWidget) {
		dotsPanel.clear();
		Image im;
		for(int i=0; i<visibleWidgetsCount; i++) {
			if( selectedWidget == i )
				im = new Image(images.dot_selected());
			else
				im = new Image(images.dot());
			
			im.setStyleName("carousel_footer_dot");
			dotsPanel.add(im);
		}
	}

	/**
	 * return position in widget array of next visible widget
	 * given the direction of travel through the carousel
	 * @param direction
	 * @return
	 */
	protected int nextWidgetIndex(int direction) {

		if( direction < -1 || direction > 1)
			return -1;

		if( visibleWidgetsCount < 1 )
			// safety to avoid infinite loop below
			return -1;

		int widgetsCount = widgets.size();
		int widgetToShowIndex = currentWidget;
		do {
			widgetToShowIndex += direction;
			if( widgetToShowIndex < 0 ) widgetToShowIndex = widgetsCount-1;
			if( widgetToShowIndex > widgetsCount-1 ) widgetToShowIndex = 0;
		} while(! widgets.get(widgetToShowIndex).isVisible());
		return widgetToShowIndex;
	}
	
	/**
	 * Move one place. Plan is to make this capable of moving to arbitrary
	 * position. For now, just + or - 1 place. The hide on invisible feature
	 * for pages means a little more thought is needed.
	 * 
	 * @param direction 1 or -1
	 */
	public void moveTo(int direction, int widgetToShowIndex, boolean animate) {

		if(widgetToShowIndex < 0 || widgetToShowIndex>widgets.size()-1)
			return;

		Widget widgetToShow = widgets.get(widgetToShowIndex);
		Widget current = widgets.get(currentWidget);
		if( animate ) {
			// position widgetToShow to one side of viewpoint
			viewport.setWidgetPosition(widgetToShow, width*direction, headerOffset);
			AnimateViewpoint av = new AnimateViewpoint( direction*-1, widgetToShow, current);
			av.run(animationDuration);
		} else {
			viewport.setWidgetPosition(current, 0, height+10);
			viewport.setWidgetPosition(widgetToShow, 0, headerOffset);
		}
		currentWidget = widgetToShowIndex;
		updateControls(currentWidget);
	}

	public void addWidget(String elementId, Widget w, Element originalElement) {

		widgets.add(w);
		originalElements.put(elementId, new WidgetElement(w, originalElement));

		if( w.isVisible() )
    		visibleWidgetsCount++;

		// put it somewhere out of sight
		viewport.add(w, 0, height+10);
	}
	
	public HashMap<String, WidgetElement> getElementWidgets() {
		return originalElements;
	}
	
	public void setFooterVisibility(boolean visible) {
		showFooter = visible;
	}
}
