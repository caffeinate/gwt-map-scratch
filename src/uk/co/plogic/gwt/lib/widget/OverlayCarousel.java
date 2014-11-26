package uk.co.plogic.gwt.lib.widget;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;

public class OverlayCarousel extends Carousel {

	HandlerManager eventBus;
	String overlayID;

	public static String CAROUSEL_CLASS = "carousel_overlay";

	public OverlayCarousel(HandlerManager eventBus, Element e) {
		this(eventBus);
		overlayID = e.getAttribute("data-overlay-id");
		pagesFromDomElement(e);
		showHidePages(overlayID, false);
	}

	public OverlayCarousel(HandlerManager eventBus) {
		super();
		this.eventBus = eventBus;

        eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				showHidePages(e.getOverlayId(), e.isVisible());
			}
		});

	}
	
	private void showHidePages(String overlay, boolean visibility) {
		if( overlayID != null && overlayID.equals(overlay)) {
			// check all elements' original attributes
			boolean changeMade = false;
			for(WidgetElement o : originalElements) {
				Element ee = o.e;
				if( ee.hasAttribute("data-show-if-overlay-visible") &&
					visibility != o.w.isVisible() ) {
					o.w.setVisible(visibility);
					changeMade = true;
				}
			}
			if( changeMade ) onResize();
			
		}
	}
	
	protected void pagesFromDomElement(Element parentElement) {
		super.pagesFromDomElement(parentElement);

		OverlayOnOffSwitch layerSwitch = new OverlayOnOffSwitch(eventBus, overlayID);
		layerSwitch.addStyleName("dataset_switch");
		fixedHeader.add(layerSwitch);
	}
}
