package uk.co.plogic.gwt.client;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;
import uk.co.plogic.gwt.lib.map.overlay.Shapes;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

/**
 * A shape is a vector feature that can have transparency and has more dimensions then a point.
 * @author si
 *
 */
public class ShapeMap implements EntryPoint {
	
	protected GoogleMapAdapter mapAdpater;
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
	    GoogleMapAdapter mapAdpater = new GoogleMapAdapter(eventBus, map_div);
	    mapAdpater.setGoogleMap(gMap);
	    

//		String tilesUrl = pv.getStringVariable("TILE_URL");
//		if( tilesUrl != null ) {
			Shapes shapeLayer = new Shapes(eventBus);
			shapeLayer.setMap(mapAdpater);
			
			ArrayList<LatLng> path = new ArrayList<LatLng>();
			// London
			path.add(LatLng.create(51.298981,-0.494310));
			path.add(LatLng.create(51.697121,-0.494310));
			path.add(LatLng.create(51.697121,0.182250));
			path.add(LatLng.create(51.298981,0.182250));

			PolygonMarker london = new PolygonMarker(eventBus, "london");
			london.setPolygonPath(path);
			shapeLayer.addPolygon(london);

			path = new ArrayList<LatLng>();
			// next to London
			path.add(LatLng.create(51.298981,-0.594310));
			path.add(LatLng.create(51.697121,-0.594310));
			path.add(LatLng.create(51.697121,-0.482250));
			path.add(LatLng.create(51.298981,-0.482250));
			
			PolygonMarker n2london = new PolygonMarker(eventBus, "nexttolondon",
													   "00FFFF", 1.0, "00DD00");
			n2london.setPolygonPath(path);
			shapeLayer.addPolygon(n2london);

//		}

	}

    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
