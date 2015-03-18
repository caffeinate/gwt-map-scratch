package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class OverlayFocusOnMarkerEvent extends GwtEvent<OverlayFocusOnMarkerEventHandler> {

    String overlayId;
    String markerId;

    public OverlayFocusOnMarkerEvent(String overlayId, String markerId) {
    	this.markerId = markerId;
    	this.overlayId = overlayId;
    }

    public static Type<OverlayFocusOnMarkerEventHandler> TYPE = new Type<OverlayFocusOnMarkerEventHandler>();

    @Override
    public Type<OverlayFocusOnMarkerEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(OverlayFocusOnMarkerEventHandler handler) {
		handler.onFocusOnMarker(this);
	}

	public String getMarkerId() {
		return markerId;
	}

	public String getOverlayId() {
		return overlayId;
	}

}
