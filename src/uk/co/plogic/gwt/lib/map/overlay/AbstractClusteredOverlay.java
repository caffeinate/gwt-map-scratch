package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.comms.envelope.ClusterPointsEnvelope;
import uk.co.plogic.gwt.lib.comms.envelope.NodeInfoEnvelope;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEventHandler;
import uk.co.plogic.gwt.lib.map.markers.IconMarker;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.GoogleMap.BoundsChangedHandler;
import com.google.maps.gwt.client.GoogleMap.CenterChangedHandler;

public abstract class AbstractClusteredOverlay extends AbstractOverlay implements DropBox {

	protected LetterBox letterBoxClusterFeatures;
	protected LetterBox letterBoxNodeInfo;
	
	protected InfoWindow infowindow;
    protected InfoWindowOptions infowindowOpts;

    protected int requestedNoPoints = 45;
    protected final static int markerAnimationDuration = 750;
	protected String namespace = "";
	
	protected final static int delayDuration = 200; // wait a bit after map moves and eventBus requests
	protected Timer requestTimer;  		  			// before making a request to the cluster server

	public AbstractClusteredOverlay(HandlerManager eventBus) {
		super(eventBus);
		requestTimer = new Timer() {  
		    @Override
		    public void run() {
		    	//System.out.println("requesting now");
		    	
		    	if( gMap != null ) {
			    	LatLngBounds mapBounds = gMap.getBounds();
			    	LatLng ll0 = mapBounds.getSouthWest();
			    	LatLng ll1 = mapBounds.getNorthEast();

			    	ClusterPointsEnvelope envelope = new ClusterPointsEnvelope();
			    	envelope.requestBounding(ll0.lng(), ll0.lat(), ll1.lng(), ll1.lat());
			    	envelope.requestNoPoints(requestedNoPoints);
			    	letterBoxClusterFeatures.send(envelope);
		    	}
		    }
		};
		

	}
	public void setMap(GoogleMap googleMap) {

		super.setMap(googleMap);
		setupInfoWindow();

//		gMap.addIdleListener(new IdleHandler(){
//
//			@Override
//			public void handle() {
//				System.out.println("Idle now");
//				
//			}
//			
//		}); 
//		gMap.addZoomChangedListener(new ZoomChangedHandler() {
//
//			@Override
//			public void handle() {
//				System.out.println("zoom changed");
//			}
//			
//		});
		gMap.addBoundsChangedListener(new BoundsChangedHandler() {

			@Override
			public void handle() {
				//System.out.println("bounds changed");
				requestTimer.cancel();
				requestTimer.schedule(delayDuration);
			}
			
		});
		
		gMap.addCenterChangedListener(new CenterChangedHandler() {

			@Override
			public void handle() {
				//System.out.println("center changed");
				requestTimer.cancel();
				requestTimer.schedule(delayDuration);
			}
			
		});
		
//		gMap.addDragEndListener(new DragEndHandler() {
//
//			@Override
//			public void handle() {
//				System.out.println("end drag");
//			}
//			
//		});
//		
//		gMap.addResizeListener(new ResizeHandler() {
//
//			@Override
//			public void handle() {
//				System.out.println("resized");
//			}
//			
//		});
	}

	public int getRequestedNoPoints() {
		return requestedNoPoints;
	}

	public void setRequestedNoPoints(int requestedNoPoints) {
		this.requestedNoPoints = requestedNoPoints;
	}

	public void setLetterBoxClusterPoints(String datasetName, LetterBox registeredLetterBox) {
		letterBoxClusterFeatures = registeredLetterBox;
		namespace = datasetName;
	}

	public void setLetterBoxNodeInfo(LetterBox registeredLetterBox) {
		letterBoxNodeInfo = registeredLetterBox;
	}

	protected void setupInfoWindow() {

	    infowindowOpts = InfoWindowOptions.create();
	    infowindowOpts.setMaxWidth(200);
	    infowindow = InfoWindow.create(infowindowOpts);

        eventBus.addHandler(MapMarkerClickEvent.TYPE, new MapMarkerClickEventHandler() {

			@Override
			public void onClick(MapMarkerClickEvent e) {
				

		    	IconMarker m = (IconMarker) e.getMapPointMarker();
		    	// namespace:id
		    	String[] idParts = m.getId().split(":");
		    	if( ! idParts[0].equals(namespace) )
		    		// doesn't belong to this ClusterPoints
		    		return;

				NodeInfoEnvelope envelope = new NodeInfoEnvelope();
		    	envelope.request(idParts[0], idParts[1]);
		    	letterBoxNodeInfo.send(envelope);
		    	
		    	 // clear existing. New copy set in onDelivery()
		    	infowindow.setContent("");
	    		infowindow.setPosition(m.getMapMarker().getPosition());
	    		infowindow.open(gMap);
			}
        });
	}

	@Override
	public void onDelivery(String letterBoxName, String jsonEncodedPayload) {

		if( letterBoxName.equals(namespace) )
			processClusterFeaturesEnvelope(jsonEncodedPayload);
		else
			processNodeInfo(jsonEncodedPayload);
	}
	
	abstract void processClusterFeaturesEnvelope(String jsonEncodedPayload);

	protected void processNodeInfo(String jsonEncodedPayload) {
		
		HTML infoWindowBody = new HTML(jsonEncodedPayload);
	    infoWindowBody.setStyleName("info_window");
		FlowPanel info_panel = new FlowPanel();
    	info_panel.setStyleName("info_window");
    	info_panel.add(infoWindowBody);
        infowindow.setContent(info_panel.getElement());

	}
	
	abstract public void refreshMapMarkers();

}
