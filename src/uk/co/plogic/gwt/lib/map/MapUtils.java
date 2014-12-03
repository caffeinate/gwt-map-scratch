package uk.co.plogic.gwt.lib.map;

import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Point;

public class MapUtils {
	
	public static Point LatLngToPixel(GoogleMap gMap, LatLng latLng) {
		// TODO, maybe this should belong to a map class, GoogleMapAdapter or GoogleMap?

		double scale = Math.pow(2, gMap.getZoom());
		LatLng latlngOrigin = LatLng.create(gMap.getBounds().getNorthEast().lat(),
								  gMap.getBounds().getSouthWest().lng());
		Point pointOrigin = gMap.getProjection().fromLatLngToPoint(latlngOrigin);
		Point asPoint = gMap.getProjection().fromLatLngToPoint(latLng);
		Point pixelOffset = Point.create(
		    Math.floor((asPoint.getX()-pointOrigin.getX()) * scale),
		    Math.floor((asPoint.getY()-pointOrigin.getY()) * scale)
		);
		return pixelOffset;
	}

}
