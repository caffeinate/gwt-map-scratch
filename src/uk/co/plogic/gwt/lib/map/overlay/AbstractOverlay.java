package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;

public abstract class AbstractOverlay {

	protected HandlerManager eventBus;
	protected GoogleMap gMap;
	protected String overlayId;
	protected double opacity = 0.4;
	protected boolean visible = false;

	public AbstractOverlay(HandlerManager eventBus) {
		this.eventBus = eventBus;
		
		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				
				if(overlayId != null && overlayId.equals(e.getOverlayId()) ) {
					if( e.isVisible() )
						show();
					else
						hide();
				}
			}
		});
	}

	public void setMap(GoogleMap googleMap) {
		gMap = googleMap;
	}
	
	public void show() {
		visible = true;
	}
	
	public void hide() {
		visible = false;
	}

	public boolean isVisible() { return visible; }

	/**
	 * non-eventbus way for a marker to tell it's overlay (if it has one) that the user
	 * has done something to it
	 * @param interactionType
	 * @param markerId
	 */
	public void userInteractionWithMarker(AbstractBaseMarker.UserInteraction interactionType,
										  String markerId) {}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public String getOverlayId() {
		return overlayId;
	}

	public void setOverlayId(String overlayId) {
		this.overlayId = overlayId;
	}
}
