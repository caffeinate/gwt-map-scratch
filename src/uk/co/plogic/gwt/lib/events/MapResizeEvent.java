package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MapResizeEvent extends GwtEvent<MapResizeEventHandler> {

    public static Type<MapResizeEventHandler> TYPE = new Type<MapResizeEventHandler>();
    
    public MapResizeEvent() {}

    @Override
    protected void dispatch(MapResizeEventHandler handler) {
        handler.onResizeEvent(this);
    }

    @Override
    public Type<MapResizeEventHandler> getAssociatedType() {
        return TYPE;
    }

}
