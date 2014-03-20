package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

public interface OverlayHasMarkers {

	public AbstractShapeMarker getMarker(String markerId);
	public String getOverlayId();
	public AttributeDictionary getMarkerAttributes(String markerId); 

}
