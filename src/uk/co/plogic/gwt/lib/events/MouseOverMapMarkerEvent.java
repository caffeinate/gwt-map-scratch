package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.BaseMarker;

import com.google.gwt.event.shared.GwtEvent;

public class MouseOverMapMarkerEvent  extends GwtEvent<MouseOverMapMarkerEventHandler> {

    public static Type<MouseOverMapMarkerEventHandler> TYPE =
            new Type<MouseOverMapMarkerEventHandler>();

    private BaseMarker mapMarker;

    public MouseOverMapMarkerEvent(BaseMarker mapMarker) {
    	this.mapMarker = mapMarker;
    }

	@Override
	public Type<MouseOverMapMarkerEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseOverMapMarkerEventHandler h) { h.onMouseOverMapMarker(this); }

	public BaseMarker getMapMarker() {
		return mapMarker;
	}

}
