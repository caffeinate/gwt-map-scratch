package uk.co.plogic.gwt.lib.map.markers;

import com.google.maps.gwt.client.GoogleMap;

abstract public class AbstractShapeMarker implements ShapeMarker {
	
	protected GoogleMap gmap;
	protected String uniqueIdentifier;
	
	public AbstractShapeMarker(GoogleMap gmap) {
		this.gmap = gmap;
	}

}
