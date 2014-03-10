package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

import com.google.gwt.event.shared.GwtEvent;

public class DataVisualisationEvent extends GwtEvent<DataVisualisationEventHandler> {

    
    AttributeDictionary visualisationData;
    
    public DataVisualisationEvent(AttributeDictionary event) {
    	visualisationData = event;
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

}
