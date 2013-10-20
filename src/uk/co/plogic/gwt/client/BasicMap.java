package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.dom.FindMicroFormat_Geo;
import uk.co.plogic.gwt.lib.map.BasicPoint;
import uk.co.plogic.gwt.lib.map.MapPointMarker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

public class BasicMap implements EntryPoint {

	@Override
	public void onModuleLoad() {
	    LatLng myLatLng = LatLng.create(51.4, -0.73);
	    MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(8.0);
	    myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    GoogleMap gMap = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
	    
	    FindMicroFormat_Geo coordsFromHtml = new FindMicroFormat_Geo("info_panel");
        if( coordsFromHtml.has_content() ){
        	for( BasicPoint aPoint: coordsFromHtml.getGeoPoints() ) {
        		//Window.alert("hello " + aPoint.getTitle());
        		new MapPointMarker(aPoint, gMap);
        	}
        }
        


	    
	}

}
