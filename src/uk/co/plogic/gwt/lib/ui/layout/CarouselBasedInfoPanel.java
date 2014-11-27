package uk.co.plogic.gwt.lib.ui.layout;

import java.util.ArrayList;
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
		ResponsiveSizing r;
		public CarouselElement(Carousel c, Element e, ResponsiveSizing r) {
			this.c = c;
			this.e = e;
			this.r = r;
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
			// the superCarousel is only visible in mobile responsive mode
			if( superCarousel == null ) {
				// TODO - take sizing from DOM HTML5 attributes, same as normal
				//        carousels.
				//		  also see Which? pie and bar charts at domManipulationSiteSpecific
				superCarousel = new SuperCarousel();
				ResponsiveSizing rs = new ResponsiveSizing(this.getParent());
				rs.setPixelAdjustments(-25, -15);
				superCarousel.setSizing(rs);
				superCarousel.setFooterVisibility(true);
				add(superCarousel);
			}

			// TODO - take this sizing from the DOM as well
			ResponsiveSizing superSized = new ResponsiveSizing(superCarousel);
			superSized.setPixelAdjustments(-30, 0);
			for(CarouselElement cc : carousels) {
				cc.e.addClassName("hidden");
				cc.c.setFooterVisibility(false);
				cc.c.setSizing(superSized);
				cc.c.setResponsiveMode(responsiveMode);
				superCarousel.addWidget(cc.c, null, null);
			}
			superCarousel.setVisible(true);
			superCarousel.setResponsiveMode(responsiveMode);
			superCarousel.onResize();

		} else {
			if( superCarousel != null ) {
				superCarousel.setVisible(false);
				if( superCarousel.isAttached() ) {
					// return them to this info panel
					for(CarouselElement cc : carousels) {
						Carousel c = cc.c;
						add(c, cc.e.getId());
						cc.e.removeClassName("hidden");
						// some sizing info is being left behind, not sure why
						c.getElement().removeAttribute("style");
						c.setFooterVisibility(true);
						c.setSizing(cc.r);
						c.setResponsiveMode(responsiveMode);
						c.onResize();
					}
				}
				superCarousel.removeFromParent();
				superCarousel = null;
			}
		}
	}

	public void loadCarousels() {

		// TODO - I think this should only be called once and must be
		//        before updateElement(..) is used. Enforce this.

		carousels.clear();
		logger.fine("Info panel has "+getWidgetCount()+" widgets");
		for(int i=0; i<getWidgetCount(); i++) {
			Widget w = getWidget(i);
			if( w instanceof Carousel ) {
				Carousel c = (Carousel) w;
				CarouselElement cc = new CarouselElement(c, w.getElement(),
														 c.getSizing());
				carousels.add(cc);
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

		    Carousel c = (Carousel) w;
			CarouselElement cc = new CarouselElement(c, cElement, c.getSizing());
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
