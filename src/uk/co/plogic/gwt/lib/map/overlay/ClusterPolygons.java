package uk.co.plogic.gwt.lib.map.overlay;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;

import uk.co.plogic.gwt.lib.comms.envelope.ClusterPolygonsEnvelope;
import uk.co.plogic.gwt.lib.comms.envelope.Envelope;
import uk.co.plogic.gwt.lib.events.ClusterChangePointCountEvent;
import uk.co.plogic.gwt.lib.events.ClusterChangePointCountEventHandler;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;

public class ClusterPolygons extends AbstractClusteredOverlay {
	
	private KeyFrame oldKeyFrame;
	private KeyFrame newKeyFrame;
	protected Logger logger = Logger.getLogger("ClusterPolygons");

	class KeyFrame {
		ArrayList<PolygonMarker> markers = new ArrayList<PolygonMarker>();
	}

	
	public ClusterPolygons(HandlerManager eventBus) {
		super(eventBus);

		eventBus.addHandler(ClusterChangePointCountEvent.TYPE, new ClusterChangePointCountEventHandler() {

			@Override
			public void onPointCountChanged(ClusterChangePointCountEvent e) {
				
				if( requestedNoPoints != e.getPointCount() ) {
					requestedNoPoints = e.getPointCount();
					requestTimer.cancel();
					requestTimer.schedule(delayDuration);
				}
			}
			
		});
	}

	
	protected void processClusterFeaturesEnvelope(String jsonEncodedPayload) {
		ClusterPolygonsEnvelope envelope = new ClusterPolygonsEnvelope();
		envelope.loadJson(jsonEncodedPayload);

		ArrayList<ArrayList<LatLng>> paths = envelope.getPaths();
		
		
		
		
		logger.fine("Got ["+paths.size()+"] new PolygonMarkers");
		oldKeyFrame = newKeyFrame;
		newKeyFrame = new KeyFrame();

		for( ArrayList<LatLng> path : paths ) {
			// TODO pass Id
			PolygonMarker p = new PolygonMarker(eventBus, "");
			p.setPolygonPath(path);
			newKeyFrame.markers.add(p);			
		}

		refreshMapMarkers();

	}
	
	/**
	 * get mapMarkers into sync with points (which have probably just been
	 * delivered).
	 * 
	 * for now: clear old markers and add new ones
	 */
	public void refreshMapMarkers() {

		if( oldKeyFrame != null ) {
			for( PolygonMarker poly : oldKeyFrame.markers ) {
				poly.remove();
			}
		}
		
		for( PolygonMarker poly : newKeyFrame.markers ) {
			poly.setMap(gMap);
		}

	}

	@Override
	Envelope factoryRequestEnvelope() {
		
		if( gMap == null ) return null;

		LatLngBounds mapBounds = gMap.getBounds();
    	LatLng ll0 = mapBounds.getSouthWest();
    	LatLng ll1 = mapBounds.getNorthEast();

    	ClusterPolygonsEnvelope envelope = new ClusterPolygonsEnvelope();
    	envelope.requestBounding(ll0.lng(), ll0.lat(), ll1.lng(), ll1.lat());
    	envelope.requestNoPoints(requestedNoPoints);
    	return envelope;

	}

}
