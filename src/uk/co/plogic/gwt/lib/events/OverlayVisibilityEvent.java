package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class OverlayVisibilityEvent extends GwtEvent<OverlayVisibilityEventHandler> {

    String overlayId;
    boolean visability;
    
    public OverlayVisibilityEvent(boolean visability, String overlayId) {
    	this.visability = visability;
    	this.overlayId = overlayId;
    }
    
    public static Type<OverlayVisibilityEventHandler> TYPE = new Type<OverlayVisibilityEventHandler>();
    
    @Override
    public Type<OverlayVisibilityEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(OverlayVisibilityEventHandler handler) {
		handler.onOverlayVisibilityChange(this);
	}
	
	public boolean isVisible() {
		return visability;
	}

	public String getOverlayId() {
		return overlayId;
	}

}
