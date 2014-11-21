package uk.co.plogic.gwt.lib.ui.layout;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.widget.Carousel;
import uk.co.plogic.gwt.lib.widget.SuperCarousel;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class CarouselBasedInfoPanel extends HTMLPanel implements RequiresResize, ProvidesResize {

	Logger logger = Logger.getLogger("CarouselBasedInfoPanel");
	String responsiveMode = "unknown";
	ArrayList<Carousel> carousels;
	int currentCarousel = 0;
	SuperCarousel superCarousel = new SuperCarousel();
	boolean isFresh = false; // only call loadCarousels when needed

	public CarouselBasedInfoPanel(SafeHtml safeHtml) {
		super(safeHtml);
		
	}

	public void setResponsiveMode(String mode) {
		responsiveMode = mode;
		
		if(!isFresh) {
			loadCarousels();
			superCarousel.load(carousels);
			isFresh = true;
		}
		logger.fine("found "+carousels.size()+" carousels");
		
		if( carousels.size() > 0 && responsiveMode.startsWith("mobile") ) {
			if( ! superCarousel.isAttached() ) {
				// only visible in mobile responsive mode
				ResponsiveSizing rs = new ResponsiveSizing(getParent());
				rs.setPixelAdjustments(-19, -19);
				superCarousel.setSizing(rs);
				add(superCarousel);
			}

			setCarouselsVisibility(false);
			superCarousel.display();
			superCarousel.setVisible(true);

		} else {
			superCarousel.setVisible(false);

			if( superCarousel.isAttached() )
				superCarousel.undisplay();
			
			setCarouselsVisibility(true);
		}
		
	}

	private void setCarouselsVisibility(boolean visibility) {
		for(Carousel c : carousels)
			c.setVisible(visibility);
	}

	private void loadCarousels() {
		carousels = new ArrayList<Carousel>();
		logger.fine("Info panel has "+getWidgetCount()+" widgets");
		for(int i=0; i<getWidgetCount(); i++) {
			Widget w = getWidget(i);
			if( w instanceof Carousel ) {
				carousels.add((Carousel) w);
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

		isFresh = false;
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
