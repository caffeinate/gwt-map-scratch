package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.RegisteredLetterBox;
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
	    
	    
	    // comms
	    UxPostalService uxPostalService = new UxPostalService(); 
	    
	    
		String onDemandUrl = pv.getStringVariable("MAP_MARKER_CLUSTER_URL");
		if( onDemandUrl != null ) {
			ClusterPoints clusterPoints = new ClusterPoints();
			clusterPoints.setMap(gMap);
			// TODO envelopeSection could/should be in pv
			RegisteredLetterBox letterBox = uxPostalService.addRecipient(clusterPoints,
																		"cluster_points_0",
																		onDemandUrl);
			clusterPoints.setLetterBox(letterBox);
		}

	}

	
    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
