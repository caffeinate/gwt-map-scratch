package uk.co.plogic.gwt.lib.map.overlay;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.BoundsChangedHandler;
import com.google.maps.gwt.client.GoogleMap.CenterChangedHandler;
import com.google.maps.gwt.client.GoogleMap.DragEndHandler;
import com.google.maps.gwt.client.GoogleMap.ResizeHandler;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.comms.envelope.ClusterPointsEnvelope;
import uk.co.plogic.gwt.lib.map.BasicPoint;
import uk.co.plogic.gwt.lib.map.MapPointMarker;

public class ClusterPoints implements DropBox {

	private LetterBox letterBox;
	private GoogleMap gMap;
	private ArrayList<BasicPoint> points;
	private ArrayList<MapPointMarker> mapMarkers;
	private HandlerManager eventBus;
	private int requestedNoPoints = 35;


	public ClusterPoints(HandlerManager eventBus) {
		this.eventBus = eventBus;
		mapMarkers = new ArrayList<MapPointMarker>();
	}

	@Override
	public void onDelivery(String jsonEncodedPayload) {

		ClusterPointsEnvelope envelope = new ClusterPointsEnvelope();
		envelope.loadJson(jsonEncodedPayload);
		points = envelope.getPoints();
		System.out.println("Got ["+points.size()+"] new points");
		refreshMapMarkers();

	}

	/**
	 * get mapMarkers into sync with points (which have probably just been
	 * delivered).
	 * 
	 * for now: clear old markers and add new ones
	 */
	public void refreshMapMarkers() {

		for( MapPointMarker m : mapMarkers ) {
			m.removeMarker();
		}
		mapMarkers.clear();

		for( BasicPoint aPoint: points ) {
    		MapPointMarker m = new MapPointMarker(	eventBus,
    												"static/icons/marker.png",
    												"static/icons/marker_active.png",
    												aPoint, gMap);
    		System.out.println("Adding: "+aPoint.getId()+" "+aPoint.getLat()+","+aPoint.getLng());
    		mapMarkers.add(m);
    	}
		
	}
	
	public void setLetterBox(LetterBox registeredLetterBox) {
		this.letterBox = registeredLetterBox;
	}

	public void setMap(GoogleMap googleMap) {
		
		this.gMap = googleMap;
		
		
		final Timer requestTimer = new Timer() {  
		    @Override
		    public void run() {
		    	System.out.println("requesting now");
		    	
		    	LatLngBounds mapBounds = gMap.getBounds();
		    	LatLng ll0 = mapBounds.getSouthWest();
		    	LatLng ll1 = mapBounds.getNorthEast();
		    	
		    	ClusterPointsEnvelope envelope = new ClusterPointsEnvelope();
		    	envelope.requestBounding(ll0.lng(), ll0.lat(), ll1.lng(), ll1.lat());
		    	envelope.requestNoPoints(requestedNoPoints);
		    	letterBox.send(envelope);
		    }
		};

		gMap.addZoomChangedListener(new ZoomChangedHandler() {

			@Override
			public void handle() {
				System.out.println("zoom changed");
			}
			
		});
		gMap.addBoundsChangedListener(new BoundsChangedHandler() {

			@Override
			public void handle() {
				System.out.println("bounds changed");
				requestTimer.cancel();
				requestTimer.schedule(250);
			}
			
		});
		
		gMap.addCenterChangedListener(new CenterChangedHandler() {

			@Override
			public void handle() {
				System.out.println("center changed");
				requestTimer.cancel();
				requestTimer.schedule(250);
			}
			
		});
		
		gMap.addDragEndListener(new DragEndHandler() {

			@Override
			public void handle() {
				System.out.println("end drag");
			}
			
		});
		
		gMap.addResizeListener(new ResizeHandler() {

			@Override
			public void handle() {
				System.out.println("resized");
			}
			
		});
		
	}

	
}
