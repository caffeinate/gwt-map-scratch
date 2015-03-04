package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.comms.envelope.Envelope;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEventHandler;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.markers.IconMarker;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.GoogleMap.BoundsChangedHandler;
import com.google.maps.gwt.client.GoogleMap.CenterChangedHandler;

public abstract class AbstractClusteredOverlay extends AbstractOverlay implements DropBox {

	protected LetterBox letterBoxClusterFeatures;
	protected uk.co.plogic.gwt.lib.comms.GeneralJsonService.LetterBox letterBoxNodeInfo;
	protected String nodeInfoPathTemplate;

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

		    	if( gMap != null && isVisible() ) {
			    	letterBoxClusterFeatures.send(factoryRequestEnvelope());
		    	}
		    }
		};
	}

	/**
	 * create a new envelope containing bounding box etc. ready for new server request
	 * @return
	 */
	abstract Envelope factoryRequestEnvelope();

	@Override
	public void setMap(GoogleMapAdapter mapAdapter) {

		super.setMap(mapAdapter);
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

	public void setLetterBoxNodeInfo(String nodeInfoPathTemplate,
    uk.co.plogic.gwt.lib.comms.GeneralJsonService.LetterBox registeredLetterBox) {
	    this.nodeInfoPathTemplate = nodeInfoPathTemplate;
		letterBoxNodeInfo = registeredLetterBox;
	}

	protected void setupInfoWindow() {

	    infowindowOpts = InfoWindowOptions.create();
	    infowindowOpts.setMaxWidth(500);
	    infowindow = InfoWindow.create(infowindowOpts);

        eventBus.addHandler(MapMarkerClickEvent.TYPE, new MapMarkerClickEventHandler() {

			@Override
			public void onClick(MapMarkerClickEvent e) {

			    if( nodeInfoPathTemplate == null )
			        return;

		    	IconMarker m = (IconMarker) e.getMapPointMarker();
		    	// namespace:id
		    	String[] idParts = m.getId().split(":");
		    	if( ! idParts[0].equals(namespace) )
		    		// doesn't belong to this ClusterPoints
		    		return;

		    	AttributeDictionary nodeData = new AttributeDictionary();
		    	nodeData.set("node_id", idParts[1]);
		    	String nodeInfoPath = StringUtils.renderHtml(nodeInfoPathTemplate, nodeData);
		    	letterBoxNodeInfo.setUrl(nodeInfoPath);
		    	letterBoxNodeInfo.send();

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

	    JSONValue j = JSONParser.parseLenient(jsonEncodedPayload);
	    String htmlContent = j.isString().stringValue();

		HTML infoWindowBody = new HTML(htmlContent);
	    infoWindowBody.setStyleName("info_window");
		FlowPanel info_panel = new FlowPanel();
    	info_panel.setStyleName("info_window");
    	info_panel.add(infoWindowBody);
        infowindow.setContent(info_panel.getElement());

	}

	abstract public void refreshMapMarkers();

}
