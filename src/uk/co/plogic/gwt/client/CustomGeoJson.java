package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.overlay.ShapesCustomJson;

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
public class CustomGeoJson implements EntryPoint {
	
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
	    
	    

	    
		String geoJsonUrl = pv.getStringVariable("GEO_JSON_URL");
		if( geoJsonUrl != null ) {
			ShapesCustomJson shapeLayer = new ShapesCustomJson(eventBus);
			shapeLayer.setMap(gMap);
			
			GeneralJsonService gjson = new GeneralJsonService(geoJsonUrl);
			gjson.setDeliveryPoint(shapeLayer);
			gjson.doRequest();
			
//			ArrayList<LatLng> path = new ArrayList<LatLng>();
//			// London
//			path.add(LatLng.create(51.298981,-0.494310));
//			path.add(LatLng.create(51.697121,-0.494310));
//			path.add(LatLng.create(51.697121,0.182250));
//			path.add(LatLng.create(51.298981,0.182250));
//
//			PolygonMarker london = new PolygonMarker(eventBus, "london", path);
//			shapeLayer.addPolygon(london);
//
//			path = new ArrayList<LatLng>();
//			// next to London
//			path.add(LatLng.create(51.298981,-0.594310));
//			path.add(LatLng.create(51.697121,-0.594310));
//			path.add(LatLng.create(51.697121,-0.482250));
//			path.add(LatLng.create(51.298981,-0.482250));
//			
//			PolygonMarker n2london = new PolygonMarker(eventBus, "next to london", path,
//													   "00FFFF", 1.0, "00DD00", 0.5);
//			shapeLayer.addPolygon(n2london);

		}

	}

    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
