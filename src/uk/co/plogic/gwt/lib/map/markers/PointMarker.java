package uk.co.plogic.gwt.lib.map.markers;

import com.google.maps.gwt.client.LatLng;

public interface PointMarker extends BaseMarker {
	public LatLng getPosition();
	public void setPosition(LatLng position);
}
