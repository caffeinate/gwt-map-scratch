package uk.co.plogic.gwt.lib.widget;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;

public class OverlayCarousel extends Carousel {

	HandlerManager eventBus;
	String overlayID;
	OverlayOnOffSwitch layerSwitch;

	public static String CAROUSEL_CLASS = "carousel_overlay";

	public OverlayCarousel(HandlerManager eventBus, Element e) {
		this(eventBus);
		overlayID = e.getAttribute("data-overlay-id");
		pagesFromDomElement(e);
		showHidePages(overlayID, false);
		setupControls();
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
	protected void setup() {
		// overlayID is needed by Controls so unlike Carousel (super) don't call it now.
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

	@Override
	protected void setupControls() {
		super.setupControls();

		layerSwitch = new OverlayOnOffSwitch(eventBus, overlayID);
		layerSwitch.addStyleName("dataset_switch");
		fixedHeader.add(layerSwitch);
	}
	
	private void showControls() {
		layerSwitch.setVisible(! responsiveMode.startsWith("mobile"));
	}
}
