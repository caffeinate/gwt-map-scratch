package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.overlay.Tiles;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

public class TileMap implements EntryPoint {
	
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

		String tilesUrl = pv.getStringVariable("TILE_URL");
		if( tilesUrl != null ) {
			Tiles tileLayer = new Tiles(eventBus, tilesUrl);
			tileLayer.setMap(gMap);
		}

	}

	
    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
