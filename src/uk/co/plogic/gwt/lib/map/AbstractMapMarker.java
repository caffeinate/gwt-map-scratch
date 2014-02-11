package uk.co.plogic.gwt.lib.map;

import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;

abstract public class AbstractMapMarker {
	
	protected GoogleMap gmap;
	protected Marker mapMarker;

	public AbstractMapMarker(GoogleMap gmapx) {
		gmap = gmapx;
	}

	public double getLat() {
		return mapMarker.getPosition().lat();
	}
	public double getLng() {
		return mapMarker.getPosition().lng();
	}
	
	/**
	 * clear marker from map
	 */
	public void hideMarker() {
		// Not totally sure if this is enough but JS API seems to look
		// like it is
		mapMarker.setMap((GoogleMap) null);
	}
	
	public void showMarker() {
		mapMarker.setMap(gmap);
	}

	public LatLng getPosition() {
		return mapMarker.getPosition();
	}

	public void setPosition(LatLng position) {
		mapMarker.setPosition(position);
	}
	
}
