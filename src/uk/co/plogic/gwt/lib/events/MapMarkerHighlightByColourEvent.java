package uk.co.plogic.gwt.lib.events;

import com.google.gwt.event.shared.GwtEvent;

public class MapMarkerHighlightByColourEvent extends GwtEvent<MapMarkerHighlightByColourEventHandler> {

    public static Type<MapMarkerHighlightByColourEventHandler> TYPE =
            new Type<MapMarkerHighlightByColourEventHandler>();

    private String colour;
    private String overlayId;
    private boolean show;

    public MapMarkerHighlightByColourEvent(boolean show, String colour, String overlayId) {
    	this.colour = colour;
    	this.overlayId = overlayId;
    	this.show = show;
    }

	@Override
	public Type<MapMarkerHighlightByColourEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(MapMarkerHighlightByColourEventHandler h) { h.onHighlight(this); }

	public boolean getShow() { return show; }
	public String getOverlayId() { return overlayId; }
	public String getColour() { return colour; }

}
