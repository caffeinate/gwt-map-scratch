package uk.co.plogic.gwt.lib.map.overlay;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;

public abstract class AbstractOverlay {

	protected HandlerManager eventBus;
	protected GoogleMap gMap;

	public AbstractOverlay(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	public void setMap(GoogleMap googleMap) {
		gMap = googleMap;
	}
}
