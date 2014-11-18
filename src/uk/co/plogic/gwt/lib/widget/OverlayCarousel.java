package uk.co.plogic.gwt.lib.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;

public class OverlayCarousel extends Carousel {

	HandlerManager eventBus;

	public static String CAROUSEL_CLASS = "carousel_overlay";

	public OverlayCarousel(HandlerManager eventBus, Element e) {
		super();
		this.eventBus = eventBus;
		pagesFromDomElement(e);
	}

	public OverlayCarousel(HandlerManager eventBus) {
		super();
		this.eventBus = eventBus;
	}
	
	protected void pagesFromDomElement(Element parentElement) {
		super.pagesFromDomElement(parentElement);
		
		String overlayID = parentElement.getAttribute("data-overlay-id");
		OverlayOnOffSwitch layerSwitch = new OverlayOnOffSwitch(eventBus, overlayID);
		fixedHeader.add(layerSwitch);
	}
}
