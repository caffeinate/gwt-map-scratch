package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MapInfoWindowUpdateEvent extends GwtEvent<MapInfoWindowUpdateEventHandler> {

    String url;

    // big assumption (which is true for google maps) is that there can only be one
    // info window. i.e. not tied to a feature or layer

    public MapInfoWindowUpdateEvent(String url) {
    	this.url = url;
    }

    public static Type<MapInfoWindowUpdateEventHandler> TYPE = new Type<MapInfoWindowUpdateEventHandler>();

    @Override
    public Type<MapInfoWindowUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

	@Override
	protected void dispatch(MapInfoWindowUpdateEventHandler handler) {
		handler.onMapInfoWindowUpdate(this);
	}

	public String getUrl() {
		return url;
	}

}
