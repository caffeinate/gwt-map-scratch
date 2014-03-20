package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;

import com.google.gwt.event.shared.GwtEvent;

public class DataVisualisationEvent extends GwtEvent<DataVisualisationEventHandler> {

	AbstractOverlay overlay;
    String markerId;
    
    public DataVisualisationEvent(String markerId, AbstractOverlay overlay) {
    	this.markerId = markerId;
    	this.overlay = overlay;
    }

    public DataVisualisationEvent(AbstractOverlay overlay) {
    	this.overlay = overlay;
    }
    
    public static Type<DataVisualisationEventHandler> TYPE = new Type<DataVisualisationEventHandler>();
    
    @Override
    public Type<DataVisualisationEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(DataVisualisationEventHandler handler) {
		handler.onDataAvailableEvent(this);
	}
	
	public AbstractOverlay getOverlay() {
		return overlay;
	}

	public boolean hasMarker() {
		return markerId != null && overlay instanceof OverlayHasMarkers;
	}
	
	public String getMarkerId() {
		return markerId;
	}
}
