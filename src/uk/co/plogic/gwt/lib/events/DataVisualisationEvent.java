package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

import com.google.gwt.event.shared.GwtEvent;

public class DataVisualisationEvent extends GwtEvent<DataVisualisationEventHandler> {

	String overlayId;
    AttributeDictionary visualisationData;
    
    public DataVisualisationEvent(AttributeDictionary ad, String overlayId) {
    	visualisationData = ad;
    	this.overlayId = overlayId;
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
	
	public AttributeDictionary getVisualisationData() {
		return visualisationData;
	}

	public String getOverlayId() {
		return overlayId;
	}
}
