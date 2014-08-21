package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MapPanToEvent extends GwtEvent<MapPanToEventHandler> {

    public static Type<MapPanToEventHandler> TYPE =
        new Type<MapPanToEventHandler>();
	
    private double lat;
    private double lng;
    
    public MapPanToEvent(double lat, double lng) {
    	this.lat = lat;
    	this.lng = lng;
    }
    
    @Override
    protected void dispatch(MapPanToEventHandler handler) {
        handler.onMapPanToEvent(this);
    }

    @Override
    public Type<MapPanToEventHandler> getAssociatedType() {
        return TYPE;
    }

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

}
