package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Any change to the view of the map (zoom, bounding, centre point etc.) should
 * fire this event.
 *
 * This could be extended to hold details of what changed.
 */
public class MapViewChangedEvent extends GwtEvent<MapViewChangedEventHandler> {

    public static Type<MapViewChangedEventHandler> TYPE =
        new Type<MapViewChangedEventHandler>();

    public MapViewChangedEvent() {}

    @Override
    protected void dispatch(MapViewChangedEventHandler handler) {
        handler.onMapViewChangedEvent(this);
    }

    @Override
    public Type<MapViewChangedEventHandler> getAssociatedType() {
        return TYPE;
    }


}
