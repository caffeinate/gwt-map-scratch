package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.BaseMarker;

import com.google.gwt.event.shared.GwtEvent;

public class MouseOutMapMarkerEvent  extends GwtEvent<MouseOutMapMarkerEventHandler> {

    public static Type<MouseOutMapMarkerEventHandler> TYPE =
            new Type<MouseOutMapMarkerEventHandler>();

    private BaseMarker mapMarker;

    public MouseOutMapMarkerEvent(BaseMarker mapMarker) {
    	this.mapMarker = mapMarker;
    }

	@Override
	public Type<MouseOutMapMarkerEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseOutMapMarkerEventHandler h) { h.onMouseOutMapMarker(this); }

	public BaseMarker getMapMarker() {
		return mapMarker;
	}

}
