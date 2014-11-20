package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.plogic.gwt.lib.ui.layout.CarouselBasedInfoPanel;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * SuperCarousel is a collection of Carousels.
 * 
 * It is used to simplify the UI for mobile devices by combining all the pages
 * in a collection of carousels into one.
 * 
 * @author si
 *
 */
public class SuperCarousel extends Carousel {
	ArrayList<Carousel> carousels;
	CarouselBasedInfoPanel parent;

	public SuperCarousel() {
		super();
		holdingPanel.setSize("100%", "100%");
	}
	
	/**
	 * initiate with data from all the carousels that this
	 * will take over from.
	 * @param carousel_list
	 */
	public void load(ArrayList<Carousel> carousel_list) {

		carousels = carousel_list;
	}
	
	public void display() {

		// copy all pages into this
		for(Carousel c : carousels) {
			
			if(parent == null )
				parent = (CarouselBasedInfoPanel) c.getParent();
			c.removeFromParent();
			c.setVisible(true);
			c.setFooterVisibility(false);
			c.setSizing(this);
			c.setPixelAdjustments(footerOffset*-1, 0);
			addWidget(c.holdingPanel.getElement().getId(), c, c.holdingPanel.getElement());
		}
	}
	
	public void undisplay() {
		
		// TODO
		return;
		
//		for(Carousel c : carousels)
//			parent.add(c);
//		carousels = null;
	}

	public void moveTo(int direction, int widgetToShowIndex) {

		Carousel currentCarousel = (Carousel) widgets.get(currentWidget);
		int nextChildPage = currentCarousel.nextWidgetIndex(direction);

		if( direction > 0 ) {
			if( nextChildPage > currentCarousel.currentWidget ) {
				// child has looped around, move to next child
				currentCarousel.moveTo(direction, nextChildPage);
				super.moveTo(direction, nextWidgetIndex(direction));
			}
			else currentCarousel.moveTo(direction, nextChildPage);
		} else {
			if( nextChildPage < currentCarousel.currentWidget ) {
				// child has looped around, move to next child
				currentCarousel.moveTo(direction, nextChildPage);
				super.moveTo(direction, nextWidgetIndex(direction));
			}
			else currentCarousel.moveTo(direction, nextChildPage);
		}

	}

	@Override
	public void onResize() {
		
		if(carousels==null)
			// not yet loaded
			return;
		
		super.onResize();

	    visibleWidgetsCount = 0;
		for(Carousel c : carousels) {
			c.onResize();
			visibleWidgetsCount += c.visibleWidgetsCount;
		}
		updateControls();
	}

}
