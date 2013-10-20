package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.dom.AttachClickFireEvent;
import uk.co.plogic.gwt.lib.dom.FindMicroFormat_Geo;
import uk.co.plogic.gwt.lib.dom.FormFiddle;
import uk.co.plogic.gwt.lib.events.ClickFireEvent;
import uk.co.plogic.gwt.lib.events.ClickFireEventHandler;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEventHandler;
import uk.co.plogic.gwt.lib.map.BasicPoint;
import uk.co.plogic.gwt.lib.map.MapPointMarker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MouseEvent;

public class BasicMap implements EntryPoint {

	
	// TODO - un-hardwire these into config in the html page
	final static String DOM_ELEMENT_ADD_BLOG_POST = "add_blog_post";
	
	protected HandlerManager eventBus;
	protected GoogleMap gMap;
    private InfoWindow infowindow;
    private InfoWindowOptions infowindowOpts;

	
	@Override
	public void onModuleLoad() {
		
		// There can be only one Highlander/HandlerManager per map
		eventBus = new HandlerManager(null);
		
		
	    LatLng myLatLng = LatLng.create(51.4, -0.73);
	    MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(8.0);
	    myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    gMap = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);
	    
	    
        // Google maps managed info window - only one open at a time
	    infowindowOpts = InfoWindowOptions.create();
	    infowindowOpts.setMaxWidth(200);
	    infowindow = InfoWindow.create(infowindowOpts);

	    
	    
	    
	    
	    
	    FindMicroFormat_Geo coordsFromHtml = new FindMicroFormat_Geo("info_panel");
        if( coordsFromHtml.has_content() ){
        	for( BasicPoint aPoint: coordsFromHtml.getGeoPoints() ) {
        		new MapPointMarker(eventBus, aPoint, gMap);
        	}
        }
        


        
        // prepare a DOM element with the give id to fire a ClickFireEvent when it's clicked
        new AttachClickFireEvent(eventBus, DOM_ELEMENT_ADD_BLOG_POST);
	    
        // do something with ClickFireEvents
        eventBus.addHandler(ClickFireEvent.TYPE, new ClickFireEventHandler() {

			@Override
			public void onClick(ClickFireEvent e) {
				
				if( e.getElement_id().equals(DOM_ELEMENT_ADD_BLOG_POST)) {
					// click map to do something
					gMap.addClickListenerOnce(new GoogleMap.ClickHandler() {

						@Override
						public void handle(MouseEvent event) {

							LatLng mapClickCoords = event.getLatLng();
							
							// Feedback to user - show it on the map
							BasicPoint newPoint = new BasicPoint(mapClickCoords.lat(),
																 mapClickCoords.lng());
							new MapPointMarker(eventBus, newPoint, gMap);

							// Add coords to new blog post form and make form visible
					        new FormFiddle(mapClickCoords.lat(), mapClickCoords.lng());

						}

			    	});
				}

			}
        });
        
        eventBus.addHandler(MapMarkerClickEvent.TYPE, new MapMarkerClickEventHandler() {

			@Override
			public void onClick(MapMarkerClickEvent e) {
				FlowPanel info_panel = new FlowPanel();
		    	info_panel.setStyleName("info_window");
		    	String text = "";
		    	MapPointMarker mpm = e.getMapPointMarker();
		    	BasicPoint bp = mpm.getBasicPoint();
		    	if (bp.getTitle() != null) {
		            text += "<h1>" + bp.getTitle() + "</h1>";
		        }
		        if (bp.getDescription() != null) {
		            text += "<p>" + bp.getDescription() + "</p>";
		        }

		        if (!text.equals("")) {
		        	HTML info_msg = new HTML(text);
		        	info_msg.setStyleName("info_window");
		        	info_panel.add(info_msg);
		        	
		            infowindow.setContent(info_panel.getElement());
		    		infowindow.setPosition(mpm.getMapMarker().getPosition());
		    		infowindow.open(gMap);
		        }

				
			}
        	
        });
        
	}

}
