package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.overlay.ClusterPolygons;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * Show polygons from the cluster server.
 * @author si
 *
 */
public class ClusterPolygonsMap implements EntryPoint {
	
	protected GoogleMap gMap;
	private HandlerManager eventBus;

	@Override
	public void onModuleLoad() {

		eventBus = new HandlerManager(null);
		PageVariables pv = getPageVariables();
		
		if(pv.getStringVariable("LAT_A") == null )
			// no map
			return;

		LatLng pointA = LatLng.create(Double.parseDouble(pv.getStringVariable("LAT_A")),
									  Double.parseDouble(pv.getStringVariable("LNG_A")));
		LatLng pointB = LatLng.create(Double.parseDouble(pv.getStringVariable("LAT_B")),
									  Double.parseDouble(pv.getStringVariable("LNG_B")));
		LatLngBounds bounds = LatLngBounds.create(pointA, pointB);
		
		MapOptions myOptions = MapOptions.create();
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);

	    String map_div = pv.getStringVariable("DOM_MAP_DIV");
	    gMap = GoogleMap.create(Document.get().getElementById(map_div), myOptions);
	    gMap.fitBounds(bounds);
	    
		String upsUrl = pv.getStringVariable("UPS_SERVICE");
		String clusterDataset = pv.getStringVariable("CLUSTER_DATASET");
		if( upsUrl != null && clusterDataset != null ) {
			ClusterPolygons clusterPolygons = new ClusterPolygons(eventBus);
			clusterPolygons.setMap(gMap);

			int clusterPointCount = pv.getIntegerVariable("CLUSTER_POINT_COUNT", -1);
			if( clusterPointCount > 0 ) {
				clusterPolygons.setRequestedNoPoints(clusterPointCount);
			}

		    // comms
		    UxPostalService uxPostalService = new UxPostalService(upsUrl); 

			// TODO envelopeSection could/should be in pv
			LetterBox letterBox = uxPostalService.createLetterBox(clusterDataset);
			// clusterPoints should recieve content sent from the server to this named
			// letter box....
			letterBox.addRecipient(clusterPolygons);
			// ... and it can send via this letter box
			clusterPolygons.setLetterBoxClusterPoints(clusterDataset, letterBox);
			
			// for NodeInfo queries
			LetterBox letterBoxNodeInfo = uxPostalService.createLetterBox("node_info");
			letterBoxNodeInfo.addRecipient(clusterPolygons);
			clusterPolygons.setLetterBoxNodeInfo(letterBoxNodeInfo);

		}

	}

    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
