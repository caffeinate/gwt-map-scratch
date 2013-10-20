package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.dom.ClickAndFire;
import uk.co.plogic.gwt.lib.dom.FindMicroFormat_Geo;
import uk.co.plogic.gwt.lib.dom.FormFiddle;
import uk.co.plogic.gwt.lib.events.ClickFireEvent;
import uk.co.plogic.gwt.lib.events.ClickFireEventHandler;
import uk.co.plogic.gwt.lib.map.BasicPoint;
import uk.co.plogic.gwt.lib.map.MapPointMarker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

public class BasicMap implements EntryPoint {

	
	protected HandlerManager eventBus;
	
	
	@Override
	public void onModuleLoad() {
		
		// There can be only one Highlander/HandlerManager per map
		eventBus = new HandlerManager(null);
		
		
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
        

        new FormFiddle(123.4, 567.8);
        new ClickAndFire(eventBus, "add_blog_post");
	    
        eventBus.addHandler(ClickFireEvent.TYPE, new ClickFireEventHandler() {

			@Override
			public void onClick(ClickFireEvent e) {
				Window.alert("I fired an event");
			}


        });
        
        
	}

}
