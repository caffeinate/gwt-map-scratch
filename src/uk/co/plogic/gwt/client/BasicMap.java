package uk.co.plogic.gwt.client;

import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.plogic.gwt.lib.dom.AttachClickFireEvent;
import uk.co.plogic.gwt.lib.dom.AttachActiveElementsEvent;
import uk.co.plogic.gwt.lib.dom.FindMicroFormat_Geo;
import uk.co.plogic.gwt.lib.dom.FormFiddle;
import uk.co.plogic.gwt.lib.dom.ShowHide;
import uk.co.plogic.gwt.lib.events.ClickFireEvent;
import uk.co.plogic.gwt.lib.events.ClickFireEventHandler;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerClickEventHandler;
import uk.co.plogic.gwt.lib.events.MouseClickEvent;
import uk.co.plogic.gwt.lib.events.MouseClickEventHandler;
import uk.co.plogic.gwt.lib.events.MouseOutEvent;
import uk.co.plogic.gwt.lib.events.MouseOutEventHandler;
import uk.co.plogic.gwt.lib.events.MouseOutMapMarkerEvent;
import uk.co.plogic.gwt.lib.events.MouseOutMapMarkerEventHandler;
import uk.co.plogic.gwt.lib.events.MouseOverEvent;
import uk.co.plogic.gwt.lib.events.MouseOverEventHandler;
import uk.co.plogic.gwt.lib.events.MouseOverMapMarkerEvent;
import uk.co.plogic.gwt.lib.events.MouseOverMapMarkerEventHandler;
import uk.co.plogic.gwt.lib.jso.PageVariables;
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
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MouseEvent;

public class BasicMap implements EntryPoint {

	private String DOM_ELEMENT_ADD_BLOG_POST;
	private String DOM_MOUSEOVER_CLASS;
	private String DOM_MOUSEOVER_ACTIVE_CLASS;
	private String DOM_ADD_POST_INSTRUCTIONS;
	private String DOM_MAP_DIV;
	private String MAP_MARKER_ICON_PATH;
	private String MAP_MARKER_ACTIVE_ICON_PATH;
	private String DOM_INFO_PANEL_DIV;

	protected GoogleMap gMap;
    private InfoWindow infowindow;
    private InfoWindowOptions infowindowOpts;
    protected HashMap<String, MapPointMarker> mapMarkers = new HashMap<String, MapPointMarker>();
	
	@Override
	public void onModuleLoad() {
		
		// There can be only one Highlander/HandlerManager per map
		HandlerManager eventBus = new HandlerManager(null);
		
		PageVariables pv = getPageVariables();
		DOM_ELEMENT_ADD_BLOG_POST = pv.getStringVariable("DOM_ELEMENT_ADD_BLOG_POST");
		DOM_MOUSEOVER_CLASS = pv.getStringVariable("DOM_MOUSEOVER_CLASS");
		DOM_MOUSEOVER_ACTIVE_CLASS = pv.getStringVariable("DOM_MOUSEOVER_ACTIVE_CLASS");
		DOM_ADD_POST_INSTRUCTIONS = pv.getStringVariable("DOM_ADD_POST_INSTRUCTIONS");
		DOM_MAP_DIV = pv.getStringVariable("DOM_MAP_DIV");
		DOM_INFO_PANEL_DIV = pv.getStringVariable("DOM_INFO_PANEL_DIV");
		MAP_MARKER_ICON_PATH = pv.getStringVariable("MAP_MARKER_ICON_PATH");
		MAP_MARKER_ACTIVE_ICON_PATH = pv.getStringVariable("MAP_MARKER_ACTIVE_ICON_PATH");

		// Go to bounding box
		String latA = pv.getStringVariable("LAT_A");
		String lngA = pv.getStringVariable("LNG_A");
		String latB = pv.getStringVariable("LAT_B");
		String lngB = pv.getStringVariable("LNG_B");
		LatLng pointA = LatLng.create(Double.parseDouble(latA), Double.parseDouble(lngA));
		LatLng pointB = LatLng.create(Double.parseDouble(latB), Double.parseDouble(lngB));
		LatLngBounds bounds = LatLngBounds.create(pointA, pointB);


	    MapOptions myOptions = MapOptions.create();
	    //myOptions.setZoom(8.0);
	    //LatLng myLatLng = LatLng.create(51.4, -0.73);
	    //myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    gMap = GoogleMap.create(Document.get().getElementById(DOM_MAP_DIV), myOptions);
	    gMap.fitBounds(bounds);
	    
        // Google maps managed info window - only one open at a time
	    infowindowOpts = InfoWindowOptions.create();
	    infowindowOpts.setMaxWidth(200);
	    infowindow = InfoWindow.create(infowindowOpts);


	    FindMicroFormat_Geo coordsFromHtml = new FindMicroFormat_Geo(DOM_INFO_PANEL_DIV);
        if( coordsFromHtml.has_content() ){
        	for( BasicPoint aPoint: coordsFromHtml.getGeoPoints() ) {
        		MapPointMarker m = new MapPointMarker(	eventBus,
        												MAP_MARKER_ICON_PATH,
        												MAP_MARKER_ACTIVE_ICON_PATH,
        												aPoint, gMap);
        		// used with mouse over events to show relationship between marker and blog entry
        		mapMarkers.put(aPoint.getId(), m);
        	}
        }
        


        
        // prepare a DOM element with the give id to fire a ClickFireEvent when it's clicked
        new AttachClickFireEvent(eventBus, DOM_ELEMENT_ADD_BLOG_POST);

        // elements marked with class="mouse_over mouse_over_1 ...." will have the "active"
        // class added on mouse over
        // TODO consider tablet users too
        new AttachActiveElementsEvent(eventBus, DOM_MOUSEOVER_CLASS, DOM_MOUSEOVER_ACTIVE_CLASS);


        // General, messey event handling setup
        AttachGeneralEvents(eventBus);
        
	}
	
	protected void AttachGeneralEvents(final HandlerManager eventBus) {
        
        // listen for these events so that markers can create mouse over events and change
        // colour when there is a mouseover event.
        // Note that Markers are decoupled from MouseOverEvents as the connection should be
        // at a high level - i.e. not all mouseovers will be about markers

        eventBus.addHandler(MouseOverEvent.TYPE, new MouseOverEventHandler() {
			@Override
			public void onMouseOver(MouseOverEvent e) {
				if( mapMarkers.containsKey(e.getMouseOver_id())) {
					mapMarkers.get(e.getMouseOver_id()).showActiveIcon(true);
				}
			}
		});
		eventBus.addHandler(MouseOutEvent.TYPE, new MouseOutEventHandler() {
			@Override
			public void onMouseOut(MouseOutEvent e) {
				if( mapMarkers.containsKey(e.getMouseOut_id())) {
					mapMarkers.get(e.getMouseOut_id()).showActiveIcon(false);
				}
			}
		});
		eventBus.addHandler(MouseClickEvent.TYPE, new MouseClickEventHandler() {
			@Override
			public void onMouseClick(MouseClickEvent e) {
				if( mapMarkers.containsKey(e.getMouseClick_id())) {
					MapPointMarker m = mapMarkers.get(e.getMouseClick_id());
					LatLng mLatLng = LatLng.create(m.getLat(), m.getLng());
					gMap.setCenter(mLatLng);
				}
			}
		});
		eventBus.addHandler(MouseOverMapMarkerEvent.TYPE, new MouseOverMapMarkerEventHandler() {

			@Override
			public void onMouseOverMapMarker(MouseOverMapMarkerEvent e) {
				MapPointMarker aMarker = e.getMapMarker();
				if( mapMarkers.containsValue(aMarker) ) {
					for( Entry<String, MapPointMarker> entry : mapMarkers.entrySet()) {
				        if( aMarker == entry.getValue() ) {
				            eventBus.fireEvent(new MouseOverEvent(entry.getKey()));
				        }
				    }					
				}
			}
		});
		eventBus.addHandler(MouseOutMapMarkerEvent.TYPE, new MouseOutMapMarkerEventHandler() {

			@Override
			public void onMouseOutMapMarker(MouseOutMapMarkerEvent e) {
				MapPointMarker aMarker = e.getMapMarker();
				if( mapMarkers.containsValue(aMarker) ) {
					for( Entry<String, MapPointMarker> entry : mapMarkers.entrySet()) {
				        if( aMarker == entry.getValue() ) {
				            eventBus.fireEvent(new MouseOutEvent(entry.getKey()));
				        }
				    }					
				}
			}
		});



        // do something with ClickFireEvents
        eventBus.addHandler(ClickFireEvent.TYPE, new ClickFireEventHandler() {

			@Override
			public void onClick(ClickFireEvent e) {
				
				if( e.getElement_id().equals(DOM_ELEMENT_ADD_BLOG_POST)) {
					
					
					final ShowHide instruction = new ShowHide(DOM_ADD_POST_INSTRUCTIONS);
					instruction.show();
					ShowHide addBlogButton = new ShowHide(e.getElement_id());
					addBlogButton.hide();

					// indicate to the user that they can click the map
					// TODO: better cursor
					MapOptions options = MapOptions.create();
					options.setDraggableCursor("crosshair");
					gMap.setOptions(options);

					// click map to do something
					gMap.addClickListenerOnce(new GoogleMap.ClickHandler() {

						@Override
						public void handle(MouseEvent event) {

							LatLng mapClickCoords = event.getLatLng();
							
							// Feedback to user - show it on the map
							BasicPoint newPoint = new BasicPoint(mapClickCoords.lat(),
																 mapClickCoords.lng());
							new MapPointMarker(	eventBus, MAP_MARKER_ICON_PATH,
												MAP_MARKER_ACTIVE_ICON_PATH,
												newPoint, gMap);

							// Add coords to new blog post form and make form visible
					        new FormFiddle(mapClickCoords.lat(), mapClickCoords.lng());
					        instruction.hide();
					        
					        // reset cursor
							MapOptions options = MapOptions.create();
							options.setDraggableCursor("");
							gMap.setOptions(options);

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

    private native PageVariables getPageVariables() /*-{
    	return $wnd["config"];
	}-*/;

}
