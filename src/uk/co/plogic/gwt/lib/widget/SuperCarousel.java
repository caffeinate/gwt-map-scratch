package uk.co.plogic.gwt.lib.widget;

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
	int superCurrentWidget = 0;

	public SuperCarousel() {
		super();
	}

	@Override
	public void moveTo(int direction, int widgetToShowIndex, boolean animate) {

		Carousel currentCarousel = (Carousel) pages.get(currentPage);
		int nextChildPage = currentCarousel.nextWidgetIndex(direction);
		int currentChildPage = currentCarousel.currentPage;

		if( direction > 0 ) {
			if( nextChildPage <= currentChildPage ) {
				// child has looped around, move to next carousel
				int nextCarouselIndex = nextWidgetIndex(direction);
				Carousel nextCarousel = (Carousel) pages.get(nextCarouselIndex);
				int currentChildIndex = nextCarousel.currentPage;
				nextCarousel.currentPage = -1;
				int nextChildIndex = nextCarousel.nextWidgetIndex(direction);
				nextCarousel.currentPage = currentChildIndex;
				//logger.info("setting carousel "+nextCarouselIndex+" to position "+nextChildIndex);
				nextCarousel.moveTo(direction, nextChildIndex, false);

				// indicate they are changing visibility
				currentCarousel.show(false);
				nextCarousel.show(true);

				super.moveTo(direction, nextCarouselIndex, true);
			}
			else currentCarousel.moveTo(direction, nextChildPage, true);
		} else {
			if( nextChildPage >= currentCarousel.currentPage ) {
				// child has looped around, move to next carousel
				int nextCarouselIndex = nextWidgetIndex(direction);
				Carousel nextCarousel = (Carousel) pages.get(nextCarouselIndex);
				int currentChildIndex = nextCarousel.currentPage;
				nextCarousel.currentPage = nextCarousel.pages.size();;
				int nextChildIndex = nextCarousel.nextWidgetIndex(direction);
				nextCarousel.currentPage = currentChildIndex;
				//logger.info("setting carousel "+nextCarouselIndex+" to position "+nextChildIndex);
				nextCarousel.moveTo(direction, nextChildIndex, false);

				// indicate they are changing visibility
				currentCarousel.show(false);
				nextCarousel.show(true);

				super.moveTo(direction, nextCarouselIndex, true);
			}
			else currentCarousel.moveTo(direction, nextChildPage, true);
		}

		superCurrentWidget += direction;
		if( superCurrentWidget < 0 ) superCurrentWidget = visiblePagesCount-1;
		if( superCurrentWidget > visiblePagesCount-1 ) superCurrentWidget = 0;

		updateControls(superCurrentWidget);
	}

	@Override
	public void onResize() {
		super.onResize();

	    visiblePagesCount = 0;
		for(Widget w : pages) {
			if( w instanceof Carousel) {
				Carousel c = (Carousel) w;
				c.setResponsiveMode(responsiveMode);
				c.onResize();
				// for now, assume that when a carousel isShowing it
				// makes all it's pages visible.
				visiblePagesCount += c.pages.size();
			}
		}
		updateControls(superCurrentWidget);
	}

}
