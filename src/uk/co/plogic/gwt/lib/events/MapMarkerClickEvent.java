package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.map.markers.BaseMarker;

import com.google.gwt.event.shared.GwtEvent;

public class MapMarkerClickEvent  extends GwtEvent<MapMarkerClickEventHandler> {

    public static Type<MapMarkerClickEventHandler> TYPE =
            new Type<MapMarkerClickEventHandler>();

    private BaseMarker mm;

    public MapMarkerClickEvent(BaseMarker m) {
    	this.mm = m;
    }

	@Override
	public Type<MapMarkerClickEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MapMarkerClickEventHandler h) { h.onClick(this); }

	public BaseMarker getMapPointMarker() {
		return mm;
	}

}
