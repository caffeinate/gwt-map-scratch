package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MapMarkerHighlightByIdEvent extends GwtEvent<MapMarkerHighlightByIdEventHandler> {

    public static Type<MapMarkerHighlightByIdEventHandler> TYPE =
            new Type<MapMarkerHighlightByIdEventHandler>();

    private String Id;
    private String overlayId;
    private boolean show;

    public MapMarkerHighlightByIdEvent(boolean show, String overlayId, String Id) {
    	this.Id = Id;
    	this.overlayId = overlayId;
    	this.show = show;
    }

	@Override
	public Type<MapMarkerHighlightByIdEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MapMarkerHighlightByIdEventHandler h) { h.onHighlight(this); }

	public boolean getShow() { return show; }
	public String getOverlayId() { return overlayId; }
	public String getId() { return Id; }

}
