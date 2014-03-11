package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired by a map when it has finished loading
 *
 */
public class MapReadyEvent extends GwtEvent<MapReadyEventHandler> {

    public static Type<MapReadyEventHandler> TYPE =
        new Type<MapReadyEventHandler>();
    
    public MapReadyEvent() {}
    
    @Override
    protected void dispatch(MapReadyEventHandler handler) {
        handler.onMapReadyEvent(this);
    }

    @Override
    public Type<MapReadyEventHandler> getAssociatedType() {
        return TYPE;
    }

    
}
