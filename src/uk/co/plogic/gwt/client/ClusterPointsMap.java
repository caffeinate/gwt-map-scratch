package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.overlay.ClusterPoints;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

public class ClusterPointsMap implements EntryPoint {
	
	protected GoogleMap gMap;
	
	@Override
	public void onModuleLoad() {
		
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
	    
	    
		String onDemandUrl = pv.getStringVariable("MAP_MARKER_CLUSTER_URL");
		if( onDemandUrl != null ) {
			ClusterPoints clusterPoints = new ClusterPoints();
			clusterPoints.setMap(gMap);
			
		    // comms
		    UxPostalService uxPostalService = new UxPostalService(onDemandUrl); 

			
			// TODO envelopeSection could/should be in pv
			LetterBox letterBox = uxPostalService.createLetterBox("cluster_points_0");
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
