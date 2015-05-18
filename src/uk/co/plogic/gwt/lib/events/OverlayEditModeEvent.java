package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class OverlayEditModeEvent extends GwtEvent<OverlayEditModeEventHandler> {

    String overlayId;
    boolean editMode;

    public OverlayEditModeEvent(boolean editMode, String overlayId) {
    	this.editMode = editMode;
    	this.overlayId = overlayId;
    }

    public static Type<OverlayEditModeEventHandler> TYPE = new Type<OverlayEditModeEventHandler>();

    @Override
    public Type<OverlayEditModeEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(OverlayEditModeEventHandler handler) {
		handler.onOverlayEditModeChange(this);
	}

	public boolean editMode() {
		return editMode;
	}

	public String getOverlayId() {
		return overlayId;
	}

}
