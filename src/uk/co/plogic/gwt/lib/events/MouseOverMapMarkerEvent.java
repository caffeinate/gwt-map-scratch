package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.AbstractMarker;

import com.google.gwt.event.shared.GwtEvent;

public class MouseOverMapMarkerEvent  extends GwtEvent<MouseOverMapMarkerEventHandler> {

    public static Type<MouseOverMapMarkerEventHandler> TYPE =
            new Type<MouseOverMapMarkerEventHandler>();

    private AbstractMarker mapMarker;

    public MouseOverMapMarkerEvent(AbstractMarker mapMarker) {
    	this.mapMarker = mapMarker;
    }

	@Override
	public Type<MouseOverMapMarkerEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MouseOverMapMarkerEventHandler h) { h.onMouseOverMapMarker(this); }

	public AbstractMarker getMapMarker() {
		return mapMarker;
	}

}
