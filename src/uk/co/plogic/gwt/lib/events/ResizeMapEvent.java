package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class ResizeMapEvent extends GwtEvent<ResizeMapEventHandler> {

    public static Type<ResizeMapEventHandler> TYPE = new Type<ResizeMapEventHandler>();
    
    public ResizeMapEvent() {}

    @Override
    protected void dispatch(ResizeMapEventHandler handler) {
        handler.onResizeMapEvent(this);
    }

    @Override
    public Type<ResizeMapEventHandler> getAssociatedType() {
        return TYPE;
    }

}
