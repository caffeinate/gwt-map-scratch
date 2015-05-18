package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

public interface OverlayHasMarkers {

	public AbstractBaseMarker getMarker(String markerId);
	public String getOverlayId();
	public AttributeDictionary getMarkerAttributes(String markerId);

}
