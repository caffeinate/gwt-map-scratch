package uk.co.plogic.gwt.lib.widget;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;

public class OverlayCarousel extends Carousel {

	HandlerManager eventBus;
	String overlayID;
	OverlayOnOffSwitch layerSwitch;
	ArrayList<Widget> onLayerVisible = new ArrayList<Widget>();

	public static String CAROUSEL_CLASS = "carousel_overlay";

	public OverlayCarousel(HandlerManager eventBus, Element e) {
		this(eventBus);
		overlayID = e.getAttribute("data-overlay-id");
		pagesFromDomElement(e);
		setupControls();
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

	@Override
	public void show(boolean visible) {
		super.show(visible);
		showControls();

		if( responsiveMode.startsWith("mobile") ) {
			// make overlay visible
			logger.finer("Changing overlay "+overlayID+" by carousel selection");
			eventBus.fireEvent(new OverlayVisibilityEvent(isShowing, overlayID));
		}
	}

	@Override
	public void setResponsiveMode(String mode) {
		super.setResponsiveMode(mode);
		showControls();

		// the overlay is made visible when this carousel appears so
		// make all pages appear
		if( responsiveMode.startsWith("mobile") )
			updateOverlayLinkedPageVisibility(true);
	}

	private void showHidePages(String overlay, boolean visibility) {
		if(  overlayID != null && overlayID.equals(overlay)
		&& ! responsiveMode.startsWith("mobile"))
			updateOverlayLinkedPageVisibility(visibility);
	}

	/**
	 * pages linked to the overlay layer's visibility
	 * @param visibility
	 */
	private void updateOverlayLinkedPageVisibility(boolean visibility) {
		for(Widget w : onLayerVisible) {
			w.setVisible(visibility);
		}
		if( onLayerVisible.size() > 0 ) onResize();
	}

	@Override
	public void setupControls() {
		super.setupControls();

		if( layerSwitch == null ) {
			layerSwitch = new OverlayOnOffSwitch(eventBus, overlayID);
			layerSwitch.addStyleName("dataset_switch");
			fixedHeader.add(layerSwitch);
		}

		onLayerVisible.clear();
		for(WidgetElement o : originalElements) {
			Element ee = o.e;
			if( ee.hasAttribute("data-show-if-overlay-visible") ) {
				onLayerVisible.add(o.w);
			}
		}
	}

	private void showControls() {
		layerSwitch.setVisible(! responsiveMode.startsWith("mobile"));
	}
}
