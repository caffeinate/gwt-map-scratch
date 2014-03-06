package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.MapPointMarker;

import com.google.gwt.event.shared.GwtEvent;

public class MouseOutMapMarkerEvent  extends GwtEvent<MouseOutMapMarkerEventHandler> {

    public static Type<MouseOutMapMarkerEventHandler> TYPE =
            new Type<MouseOutMapMarkerEventHandler>();

    private MapPointMarker mapMarker;

    public MouseOutMapMarkerEvent(MapPointMarker mapMarker) {
    	this.mapMarker = mapMarker;
    }

	@Override
	public Type<MouseOutMapMarkerEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseOutMapMarkerEventHandler h) { h.onMouseOutMapMarker(this); }

	public MapPointMarker getMapMarker() {
		return mapMarker;
	}

}
