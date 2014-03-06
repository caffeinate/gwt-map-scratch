package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;

public abstract class AbstractOverlay {

	protected HandlerManager eventBus;
	protected GoogleMap gMap;
	protected String overlayId;
	protected double opacity = 0.4;

	public AbstractOverlay(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	public void setMap(GoogleMap googleMap) {
		gMap = googleMap;
	}
	
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
}
