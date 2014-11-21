package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.ui.layout.CarouselBasedInfoPanel;

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
	int superCurrentWidget = 0;

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

	@Override
	public void moveTo(int direction, int widgetToShowIndex, boolean animate) {

		Carousel currentCarousel = (Carousel) widgets.get(currentWidget);
		int nextChildPage = currentCarousel.nextWidgetIndex(direction);
		int currentChildPage = currentCarousel.currentWidget;

		logger.info("current:"+currentWidget);
		logger.info("ccur:"+currentCarousel.currentWidget+" next:"+nextChildPage);
		
		if( direction > 0 ) {
			if( nextChildPage <= currentChildPage ) {
				// child has looped around, move to next carousel
				int nextCarouselIndex = nextWidgetIndex(direction);
				Carousel nextCarousel = (Carousel) widgets.get(nextCarouselIndex);
				int currentChildIndex = nextCarousel.currentWidget;
				nextCarousel.currentWidget = -1;
				int nextChildIndex = nextCarousel.nextWidgetIndex(direction);
				nextCarousel.currentWidget = currentChildIndex;
				//logger.info("setting carousel "+nextCarouselIndex+" to position "+nextChildIndex);
				nextCarousel.moveTo(direction, nextChildIndex, false);
				super.moveTo(direction, nextCarouselIndex, true);
			}
			else currentCarousel.moveTo(direction, nextChildPage, true);
		} else {
			if( nextChildPage >= currentCarousel.currentWidget ) {
				// child has looped around, move to next carousel
				int nextCarouselIndex = nextWidgetIndex(direction);
				Carousel nextCarousel = (Carousel) widgets.get(nextCarouselIndex);
				int currentChildIndex = nextCarousel.currentWidget;
				nextCarousel.currentWidget = nextCarousel.widgets.size();;
				int nextChildIndex = nextCarousel.nextWidgetIndex(direction);
				nextCarousel.currentWidget = currentChildIndex;
				//logger.info("setting carousel "+nextCarouselIndex+" to position "+nextChildIndex);
				nextCarousel.moveTo(direction, nextChildIndex, false);
				super.moveTo(direction, nextCarouselIndex, true);
			}
			else currentCarousel.moveTo(direction, nextChildPage, true);
		}

		
//		if( direction > 0 ) {
//			if( nextChildPage <= currentCarousel.currentWidget ) {
//				// child has looped around, move to next child
//				currentCarousel.currentWidget = nextChildPage;
//				super.moveTo(direction, nextWidgetIndex(direction));
//			}
//			else currentCarousel.moveTo(direction, nextChildPage);
//		} else {
//			if( nextChildPage >= currentCarousel.currentWidget ) {
//				// child has looped around, move to next child
//				currentCarousel.currentWidget = nextChildPage;
//				super.moveTo(direction, nextWidgetIndex(direction));
//			}
//			else currentCarousel.moveTo(direction, nextChildPage);
//		}
		superCurrentWidget += direction;
		if( superCurrentWidget < 0 ) superCurrentWidget = visibleWidgetsCount-1;
		if( superCurrentWidget > visibleWidgetsCount-1 ) superCurrentWidget = 0;

		updateControls(superCurrentWidget);
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
		updateControls(superCurrentWidget);
	}

}
