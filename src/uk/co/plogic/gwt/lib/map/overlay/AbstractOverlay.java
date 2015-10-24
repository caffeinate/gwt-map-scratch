package uk.co.plogic.gwt.lib.map.overlay;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.MapReadyEvent;
import uk.co.plogic.gwt.lib.events.MapReadyEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayInRangeEvent;
import uk.co.plogic.gwt.lib.events.OverlayLoadingEvent;
import uk.co.plogic.gwt.lib.events.OverlayOpacityEvent;
import uk.co.plogic.gwt.lib.events.OverlayOpacityEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;

public abstract class AbstractOverlay {

	protected HandlerManager eventBus;
	protected GoogleMap gMap;
	protected GoogleMapAdapter mapAdapter;
	protected String overlayId;
	protected double opacity = 0.8;
	protected boolean visible = false;
	protected boolean inRange = false; // @see updateInRange(..)
	protected double zIndex = 1.0;
	private boolean isLoading = true; // i.e. data not ready
    protected ArrayList<Integer> zoomLevels;
    protected Logger logger = Logger.getLogger("AbstractOverlay");

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

		eventBus.addHandler(MapReadyEvent.TYPE, new MapReadyEventHandler() {

            @Override
            public void onMapReadyEvent(MapReadyEvent event) {
                logger.fine("map ready for AbstractOverlay");
                updateInRange();
            }
		});
	}

	public void setMap(GoogleMapAdapter mapAdapter) {
		gMap = mapAdapter.getGoogleMap();
		this.mapAdapter = mapAdapter;

		gMap.addZoomChangedListener(new ZoomChangedHandler() {
            @Override
            public void handle() {
                updateInRange();
            }
        });
	}

	/**
	 * when the overlay should be visible but can't be shown because zoom
	 * level isn't available for current zoom or (not yet implemented) the
	 * layer isn't available in current map bounding - fire the
	 * OverlayInRangeEvent when state changes.
	 */
	protected void updateInRange() {

	    if( !mapAdapter.isMapLoaded() ) {
	        logger.fine("updateInRange couldn't get map");
	        return;
	    }

        int zoomLevel = (int) gMap.getZoom();
        if( zoomLevels == null ) {
            // not set means available at all levels
            inRange = true;
        } else if( zoomLevels.contains(zoomLevel) ) {
             inRange = true;
        } else {
            inRange = false;
        }

        eventBus.fireEvent(new OverlayInRangeEvent(inRange, overlayId));

	}

	/**
	 *
	 * @return boolean indicating if visibility changed state
	 */
	public boolean show() {
		boolean wasHidden = (visible == false);
		visible = true;
		updateInRange();
		return wasHidden;
	}

	/**
	 *
	 * @return boolean indicating if visibility changed state
	 */
	public boolean hide() {
		boolean wasVisible = (visible == true);
		visible = false;
		updateInRange();
		return wasVisible;
	}

	public boolean isVisible() { return visible; }

	/**
	 * remove all markers from map and delete them all if stored in
	 * other structures.
	 *
	 * this doesn't need to be implemented in all overlays
	 */
	public void clearAllMarkers() {}

	/**
	 * non-eventbus way for a marker to tell it's overlay (if it has one) that the user
	 * has done something to it
	 * @param interactionType
	 * @param markerId
	 * @param latLng
	 */
	public void userInteractionWithMarker(AbstractBaseMarker.UserInteraction interactionType,
										  String markerId, LatLng latLng) {
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public String getOverlayId() { return overlayId; }
	public void setOverlayId(String overlayId) {

		if( ! StringUtils.legalIdString(overlayId))
			throw new IllegalArgumentException("Alphanumeric, _ and - only for all IDs");

		this.overlayId = overlayId;
	}

	/**
	 * layer ordering amongst shapes - not sure about tiles yet
	 * @param zIndex
	 */
	public void setZindex(double zIndex) {
		this.zIndex = zIndex;
	}

	public double getZindex() {
		return zIndex;
	}

	/**
	 * child overlays should signal when loading starts and ends.
	 *
	 * @param isLoading
	 */
	protected void setIsLoading(boolean isLoading) {
	    this.isLoading = isLoading;
	    eventBus.fireEvent(new OverlayLoadingEvent(isLoading, overlayId));
    }

	protected boolean isLoading() { return isLoading; }


    public void setZoomLevels(ArrayList<Integer> zoomLevels) {
        this.zoomLevels = zoomLevels;
    }
}
