package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.overlay.ClusterPoints;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

public class ClusterPointsMap implements EntryPoint {
	
	protected GoogleMap gMap;
	private HandlerManager eventBus;
	
	@Override
	public void onModuleLoad() {

		eventBus = new HandlerManager(null);

		PageVariables pv = getPageVariables();
		

		
		MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(Double.parseDouble(pv.getStringVariable("ZOOM")));
	    LatLng myLatLng = LatLng.create(Double.parseDouble(pv.getStringVariable("LAT")),
	    								Double.parseDouble(pv.getStringVariable("LNG"))
	    								);
	    myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);

	    gMap = GoogleMap.create(Document.get().getElementById(pv.getStringVariable("DOM_MAP_DIV")),
	    													  myOptions);
	    
	    
		String upsUrl = pv.getStringVariable("UPS_SERVICE");
		String mapMarkersUrl = pv.getStringVariable("MAP_MARKER_DYNAMIC_ICONS_URL");
		String clusterDataset = pv.getStringVariable("CLUSTER_DATASET");
		if( upsUrl != null && mapMarkersUrl != null && clusterDataset != null ) {
			ClusterPoints clusterPoints = new ClusterPoints(eventBus, mapMarkersUrl);
			clusterPoints.setMap(gMap);
			
		    // comms
		    UxPostalService uxPostalService = new UxPostalService(upsUrl); 

			
			// TODO envelopeSection could/should be in pv
			LetterBox letterBox = uxPostalService.createLetterBox(clusterDataset);
			// clusterPoints should recieve content sent from the server to this named
			// letter box....
			letterBox.addRecipient(clusterPoints);
			// ... and it can send via this letter box
			clusterPoints.setLetterBox(letterBox);
		}

	}

	
    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
