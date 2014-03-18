package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;

/**
 * For layers with markers and legend. But returning Null from getLegendAttributes() is fine.
 * @author si
 *
 */
public interface OverlayDatavisualisationsBasic {
	
	public AbstractShapeMarker getMarker(String markerId);

	public String getOverlayId();

	public LegendAttributes getLegendAttributes();

	public AttributeDictionary getMarkerAttributes(String markerId); 

}
