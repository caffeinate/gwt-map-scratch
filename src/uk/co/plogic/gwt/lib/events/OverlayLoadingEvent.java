package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class OverlayLoadingEvent extends GwtEvent<OverlayLoadingEventHandler> {

    String overlayId;
    boolean isLoading;

    public OverlayLoadingEvent(boolean isLoading, String overlayId) {
    	this.isLoading = isLoading;
    	this.overlayId = overlayId;
    }

    public static Type<OverlayLoadingEventHandler> TYPE = new Type<OverlayLoadingEventHandler>();

    @Override
    public Type<OverlayLoadingEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(OverlayLoadingEventHandler handler) {
		handler.onOverlayLoading(this);
	}

	public boolean isLoading() {
		return isLoading;
	}

	public String getOverlayId() {
		return overlayId;
	}

}
