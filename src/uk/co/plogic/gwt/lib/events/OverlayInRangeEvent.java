package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An overlay might not be available at given zoom and extent.
 * @author si
 *
 */
public class OverlayInRangeEvent extends GwtEvent<OverlayInRangeEventHandler> {

    String overlayId;
    boolean inRange;

    public OverlayInRangeEvent(boolean inRange, String overlayId) {
    	this.inRange = inRange;
    	this.overlayId = overlayId;
    }

    public static Type<OverlayInRangeEventHandler> TYPE = new Type<OverlayInRangeEventHandler>();

    @Override
    public Type<OverlayInRangeEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(OverlayInRangeEventHandler handler) {
		handler.onOverlayInRangeChange(this);
	}

	public boolean isInRange() {
		return inRange;
	}

	public String getOverlayId() {
		return overlayId;
	}

}
