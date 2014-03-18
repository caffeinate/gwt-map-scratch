package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.overlay.OverlayDatavisualisationsBasic;

import com.google.gwt.event.shared.GwtEvent;

public class DataVisualisationEvent extends GwtEvent<DataVisualisationEventHandler> {

	OverlayDatavisualisationsBasic overlay;
    String markerId;
    
    public DataVisualisationEvent(String markerId, OverlayDatavisualisationsBasic overlay) {
    	this.markerId = markerId;
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
	
	public OverlayDatavisualisationsBasic getOverlay() {
		return overlay;
	}

	public String getMarkerId() {
		return markerId;
	}
}
