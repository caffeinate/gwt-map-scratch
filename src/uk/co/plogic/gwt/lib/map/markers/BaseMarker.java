package uk.co.plogic.gwt.lib.map.markers;

import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;

import com.google.maps.gwt.client.GoogleMap;

public interface BaseMarker {

	public void setMap(GoogleMap gMap);

	/**
	 * clear up any datastructures used by this marker and clear it from the map.
	 */
	public void remove();
	
	public void show();
	public void hide();
	
	public String getId();
	public void setId(String id);
	
	public void setOverlay(AbstractOverlay overlay);
	public AbstractOverlay getOverlay();
}
