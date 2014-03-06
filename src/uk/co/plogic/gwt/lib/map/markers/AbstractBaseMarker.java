package uk.co.plogic.gwt.lib.map.markers;

import com.google.maps.gwt.client.GoogleMap;

import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;

public abstract class AbstractBaseMarker implements BaseMarker {
	
	protected GoogleMap gmap;
	protected String uniqueIdentifier;
	protected AbstractOverlay overlay; // a marker belongs to 0 or 1 overlays

	public enum UserInteraction { CLICK, MOUSEOVER, MOUSEOUT }
	
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

	public void setOverlay(AbstractOverlay overlay) { this.overlay = overlay; }
	public AbstractOverlay getOverlay() { return overlay; }
	
	/**
	 * Marker tells parent of user interaction. The marker could also use the
	 * eventHandler to tell everyone.
	 */
	protected void relayUserAction(UserInteraction ui) {

		// markers don't need to belong to an overlay 
		if(overlay == null)
			return;

		overlay.userInteractionWithMarker(ui, getId());
	}

}