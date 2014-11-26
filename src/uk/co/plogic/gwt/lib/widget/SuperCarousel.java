package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import uk.co.plogic.gwt.lib.ui.layout.CarouselBasedInfoPanel;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;

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
	}

//	public void display(ArrayList<Carousel> carousel_list) {
//
//		carousels = carousel_list;
//		
////		Image previous = new Image(images.leftArrow());
////		previous.addClickHandler(new ClickHandler() {
////
////			@Override
////			public void onClick(ClickEvent event) {
////				logger.info("click from super constructor");
////			}
////		});
////		addWidget(previous, null, null);
//
//		// copy all pages into this
//		for(Carousel c : carousels) {
//			
//			if(parent == null )
//				parent = (CarouselBasedInfoPanel) c.getParent();
//			c.removeFromParent();
//			//c.setVisible(true);
//			c.setFooterVisibility(false);
//			ResponsiveSizing originalSizing = c.getSizing();
//			c.setSizing(responsiveSizing);
//			//c.setPixelAdjustments(footerOffset*-1, 0);
//			//c.heightAbsolute = -1;
//			//c.widthAbsolute = -1;
//			addWidget(c, c.holdingPanel.getElement(), originalSizing);
//		}
//	}

//	public void undisplay() {
//		
//		// return carousels back to original parent
//		for(WidgetElement we : originalElements) {
//			Carousel c = (Carousel) we.w;
//			c.removeFromParent();
//			c.setFooterVisibility(true);
//			c.setSizing(we.r);
//		}
//		carousels = null;
//		originalElements.clear();
//
//		return;
//	}

	@Override
	public void moveTo(int direction, int widgetToShowIndex, boolean animate) {


		Carousel currentCarousel = (Carousel) widgets.get(currentWidget);
		int nextChildPage = currentCarousel.nextWidgetIndex(direction);
		int currentChildPage = currentCarousel.currentWidget;

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

		superCurrentWidget += direction;
		if( superCurrentWidget < 0 ) superCurrentWidget = visibleWidgetsCount-1;
		if( superCurrentWidget > visibleWidgetsCount-1 ) superCurrentWidget = 0;

		updateControls(superCurrentWidget);
	}

	@Override
	public void onResize() {
		
		//if(carousels==null)
			// not yet loaded
			//return;
		
		super.onResize();

	    visibleWidgetsCount = 0;
		for(Widget w : widgets) {
			if( w instanceof Carousel) {
				Carousel c = (Carousel) w;
				c.onResize();
				visibleWidgetsCount += c.visibleWidgetsCount;
			}
		}
		updateControls(superCurrentWidget);
	}

}
