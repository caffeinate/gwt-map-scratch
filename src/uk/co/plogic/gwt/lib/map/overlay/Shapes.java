package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;

public class Shapes extends AbstractOverlay {
	
	public Shapes(HandlerManager eventBus) {
		super(eventBus);
	}

	public void setMap(GoogleMap googleMap) {
		super.setMap(googleMap);
	}

	public void addPolygon(PolygonMarker p) {
		p.setMap(gMap);
		p.setOverlay(this);
	}

}
