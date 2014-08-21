package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MapZoomToEvent extends GwtEvent<MapZoomToEventHandler> {

    public static Type<MapZoomToEventHandler> TYPE =
        new Type<MapZoomToEventHandler>();
	
    private int zoom;
    
    public MapZoomToEvent(int zoom) {
    	this.zoom = zoom;
    }
    
    @Override
    protected void dispatch(MapZoomToEventHandler handler) {
        handler.onMapZoomEvent(this);
    }

    @Override
    public Type<MapZoomToEventHandler> getAssociatedType() {
        return TYPE;
    }

	public int getZoom() {
		return zoom;
	}
    
}
