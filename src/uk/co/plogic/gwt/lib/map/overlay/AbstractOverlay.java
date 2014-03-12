package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.events.OverlayOpacityEvent;
import uk.co.plogic.gwt.lib.events.OverlayOpacityEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;

public abstract class AbstractOverlay {

	protected HandlerManager eventBus;
	protected GoogleMap gMap;
	protected String overlayId;
	protected double opacity = 0.8;
	protected boolean visible = false;
	protected LegendAttributes legendAttributes;

	public AbstractOverlay(HandlerManager eventBus) {
		this.eventBus = eventBus;
		
		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				
				if(overlayId != null && overlayId.equals(e.getOverlayId()) ) {
					if( e.isVisible() )
						show();
					else
						hide();
				}
			}
		});

		eventBus.addHandler(OverlayOpacityEvent.TYPE, new OverlayOpacityEventHandler() {

			@Override
			public void onOverlayOpacityChange(OverlayOpacityEvent e) {
				if(overlayId != null && overlayId.equals(e.getOverlayId()) ) {
					setOpacity(e.getOpacity());
				}
			}
		});
	}

	public void setMap(GoogleMap googleMap) {
		gMap = googleMap;
	}
	
	public void show() {
		visible = true;
	}
	
	public void hide() {
		visible = false;
	}

	public boolean isVisible() { return visible; }

	/**
	 * non-eventbus way for a marker to tell it's overlay (if it has one) that the user
	 * has done something to it
	 * @param interactionType
	 * @param markerId
	 */
	public void userInteractionWithMarker(AbstractBaseMarker.UserInteraction interactionType,
										  String markerId) {}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public String getOverlayId() { return overlayId; }
	public void setOverlayId(String overlayId) { this.overlayId = overlayId; }
	public AttributeDictionary getAttributes(String markerId) { return null; }

	public boolean hasLegendAttributes() { return legendAttributes != null; }
	public LegendAttributes getLegendAttributes() {	return legendAttributes; }
	public void setLegendAttributes(LegendAttributes legendAttributes) {
		this.legendAttributes = legendAttributes;
	}
}
