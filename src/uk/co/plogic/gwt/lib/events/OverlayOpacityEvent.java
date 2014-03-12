package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class OverlayOpacityEvent extends GwtEvent<OverlayOpacityEventHandler> {

    String overlayId;
    double opacity;
    
    public OverlayOpacityEvent(double opacity, String overlayId) {
    	this.opacity = opacity;
    	this.overlayId = overlayId;
    }
    
    public static Type<OverlayOpacityEventHandler> TYPE = new Type<OverlayOpacityEventHandler>();
    
    @Override
    public Type<OverlayOpacityEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(OverlayOpacityEventHandler handler) {
		handler.onOverlayOpacityChange(this);
	}
	
	public double getOpacity() {
		return opacity;
	}

	public String getOverlayId() {
		return overlayId;
	}

}
