package uk.co.plogic.gwt.lib.map.markers;

import com.google.maps.gwt.client.GoogleMap;

import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;

public abstract class AbstractBaseMarker implements BaseMarker {
	
	protected GoogleMap gmap;
	protected String uniqueIdentifier;
	
	public AbstractBaseMarker(String Id) {
		setId(Id);
	}

	public String getId() { return uniqueIdentifier; }
	public void setId(String id) { uniqueIdentifier = id; }

	public void setMap(GoogleMap gMap) { gmap = gMap; }
	public void remove() {
		hide();
		gmap = null;
	}

	public void setOverlay(AbstractOverlay overlay) {
		// TODO Auto-generated method stub
	}

	public AbstractOverlay getOverlay() {
		// TODO Auto-generated method stub
		return null;
	}

}