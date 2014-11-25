package uk.co.plogic.gwt.lib.ui.layout;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.widget.Carousel;
import uk.co.plogic.gwt.lib.widget.SuperCarousel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class CarouselBasedInfoPanel extends HTMLPanel implements RequiresResize, ProvidesResize {

	Logger logger = Logger.getLogger("CarouselBasedInfoPanel");
	String responsiveMode = "unknown";
	int currentCarousel = 0;
	SuperCarousel superCarousel;
	ArrayList<CarouselElement> carousels = new ArrayList<CarouselElement>();

	class CarouselElement {
		// for recording position within the DOM for elements that are
		// moved about
		Carousel c;
		Element e;
		public CarouselElement(Carousel c, Element e) {
			this.c = c;
			this.e = e;
		}
	}
	
	public CarouselBasedInfoPanel(SafeHtml safeHtml) {
		super(safeHtml);
		
	}

	public void setResponsiveMode(String mode) {

		String lastResponsiveMode = responsiveMode;
		responsiveMode = mode;
		
		if( lastResponsiveMode.equals(responsiveMode) )
			// no change
			return;
		
		if( carousels.size() < 1 )
			// info panel currently only cares about carousel based layouts
			return;

		logger.fine("setResponsiveMode found "+carousels.size()+" carousels");

		if( responsiveMode.startsWith("mobile") ) {
			if( superCarousel == null ) {
				// only visible in mobile responsive mode
				superCarousel = new SuperCarousel();
				ResponsiveSizing rs = new ResponsiveSizing(getParent());
				rs.setPixelAdjustments(-19, -19);
				superCarousel.setSizing(rs);
				add(superCarousel);
			}

			ArrayList<Carousel> c = new ArrayList<Carousel>();
			for(CarouselElement cc : carousels)
				c.add(cc.c);

			superCarousel.display(c);
			superCarousel.setVisible(true);
			superCarousel.onResize();

		} else {
			if( superCarousel != null ) {
				superCarousel.setVisible(false);
				if( superCarousel.isAttached() ) {
					superCarousel.undisplay();
					
					// claim them back to belonging to this info panel
					for(CarouselElement cc : carousels) {
						add(cc.c, cc.e.getId());
						// some sizing info is being left behind
						cc.c.getElement().removeAttribute("style");
						cc.c.onResize();
					}
				}
				superCarousel.removeFromParent();
				superCarousel = null;
			}
		}
		
	}

//	private void setCarouselsVisibility(boolean visibility) {
//		for(Carousel c : carousels)
//			c.setVisible(visibility);
//	}

	public void loadCarousels() {

		carousels.clear();
		logger.fine("Info panel has "+getWidgetCount()+" widgets");
		for(int i=0; i<getWidgetCount(); i++) {
			Widget w = getWidget(i);
			if( w instanceof Carousel ) {
				CarouselElement cc = new CarouselElement((Carousel) w, w.getElement());
				String q = w.getElement().getId();
				carousels.add(cc);
				//Element ee = w.getElement();
				//logger.info("id::::"+w.getElement().getId());
			}
		}
	}

	/**
	 * wrap (replace) of an element which is within the info panel's
	 * HTML with the given widget.
	 * 
	 * @param elementId
	 * @param w
	 * @return if successful
	 */
	public boolean updateElement(String elementId, Widget w, Boolean replace) {

		if( w instanceof Carousel ) {
			
			Element cElement = Document.get().getElementById(elementId);
		    if (cElement == null)
		      throw new Error("No such element Id");

			CarouselElement cc = new CarouselElement((Carousel) w, cElement);
			carousels.add(cc);
		}

		if( replace ) addAndReplaceElement(w, elementId);
		else		  add(w, elementId);

		return true;
	}

	@Override
	public void onResize() {
		
		logger.finer("Info panel has "+getWidgetCount()+" widgets");
		for(int i=0; i<getWidgetCount(); i++) {
			Widget w = getWidget(i);
			if (w instanceof RequiresResize) {
		            ((RequiresResize) w).onResize();
		    }
		}
	}

}
