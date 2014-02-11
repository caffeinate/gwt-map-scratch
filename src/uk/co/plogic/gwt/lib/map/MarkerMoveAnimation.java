package uk.co.plogic.gwt.lib.map;

import com.google.gwt.animation.client.Animation;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;


/**
 * Linear move animation moves a marker from one position to another.
 * @author si
 *
 */
public class MarkerMoveAnimation  extends Animation {

	Marker mapMarker;
	LatLng startPosition;
	double lat_diff, lng_diff;

	public MarkerMoveAnimation(Marker mapMarker, LatLng startPosition, LatLng endPosition) {
		this.mapMarker = mapMarker;
		this.startPosition = startPosition;
		
		lat_diff = endPosition.lat() - startPosition.lat();
		lng_diff = endPosition.lng() - startPosition.lng();
	}

	@Override
	protected void onUpdate(double progress) {
		double lat = startPosition.lat() + (lat_diff*progress);
		double lng = startPosition.lng() + (lng_diff*progress);
		LatLng position = LatLng.create(lat, lng);
		mapMarker.setPosition(position);
	}
	
}
