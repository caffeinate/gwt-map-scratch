package uk.co.plogic.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.dom.DomParser;
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
import uk.co.plogic.gwt.lib.map.Viewpoint;
import uk.co.plogic.gwt.lib.map.markers.IconMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.BasicPoint;
import uk.co.plogic.gwt.lib.ui.FindMicroFormat_Geo;
import uk.co.plogic.gwt.lib.ui.FormFiddle;
import uk.co.plogic.gwt.lib.ui.ShowHide;
import uk.co.plogic.gwt.lib.ui.activatedElements.MouseInteractions;
import uk.co.plogic.gwt.lib.ui.activatedElements.ClickFireInteraction;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.ArrayHelper;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeControlOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MapTypeStyle;
import com.google.maps.gwt.client.MapTypeStyler;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;
import com.google.maps.gwt.client.StyledMapType;
import com.google.maps.gwt.client.StyledMapTypeOptions;

public class BlogMap implements EntryPoint {

	private String DOM_ADD_BLOG_POST;
	private String DOM_ADD_SURFACE;
	private String DOM_MOUSEOVER_CLASS;
	private String DOM_MOUSEOVER_ACTIVE_CLASS;
	private String DOM_ADD_POST_INSTRUCTIONS;
	private String DOM_MAP_DIV;
	private String MAP_MARKER_ICON_PATH;
	private String MAP_MARKER_ACTIVE_ICON_PATH;
	private String DOM_INFO_PANEL_DIV;
	private String DOM_ADD_POST_HIDE_ITEM;
	private String DOM_ADD_FORM;
	protected ShowHide showHideAddSurface;
	protected ShowHide showHideAddPostHideItem;
	protected ShowHide showHideAddPostInstructions;
	protected ShowHide showHideAddForm;
	protected ShowHide showHideAddBlogButton;

	protected GoogleMap gMap;
    private InfoWindow infowindow;
    private InfoWindowOptions infowindowOpts;
    protected HashMap<String, ArrayList<IconMarker>> mapMarkers =
									new HashMap<String, ArrayList<IconMarker>>(); // id -> markers
    protected HashMap<String, BasicPoint> markerAttributes = new HashMap<String, BasicPoint>();
    private MarkerImage activeIcon;
    private MarkerImage normalIcon;
    protected Logger logger = Logger.getLogger("BasicMap");

	@Override
	public void onModuleLoad() {

		// There can be only one Highlander/HandlerManager per map
		HandlerManager eventBus = new HandlerManager(null);

		PageVariables pv = getPageVariables();
		DOM_ADD_BLOG_POST = pv.getStringVariable("DOM_ADD_BLOG_POST");
		DOM_ADD_SURFACE = pv.getStringVariable("DOM_ADD_SURFACE");
		DOM_ADD_FORM = pv.getStringVariable("DOM_ADD_FORM");
		DOM_MOUSEOVER_CLASS = pv.getStringVariable("DOM_MOUSEOVER_CLASS");
		DOM_MOUSEOVER_ACTIVE_CLASS = pv.getStringVariable("DOM_MOUSEOVER_ACTIVE_CLASS");
		DOM_ADD_POST_INSTRUCTIONS = pv.getStringVariable("DOM_ADD_POST_INSTRUCTIONS");
		DOM_MAP_DIV = pv.getStringVariable("DOM_MAP_DIV");
		DOM_INFO_PANEL_DIV = pv.getStringVariable("DOM_INFO_PANEL_DIV");
		MAP_MARKER_ICON_PATH = pv.getStringVariable("MAP_MARKER_ICON_PATH");
		MAP_MARKER_ACTIVE_ICON_PATH = pv.getStringVariable("MAP_MARKER_ACTIVE_ICON_PATH");
		DOM_ADD_POST_HIDE_ITEM = pv.getStringVariable("DOM_ADD_POST_HIDE_ITEM");

	    MapTypeStyle greyscaleStyle = MapTypeStyle.create();
	    greyscaleStyle.setStylers(ArrayHelper.toJsArray(MapTypeStyler.saturation(-80)));

	    StyledMapTypeOptions myStyledMapTypeOpts = StyledMapTypeOptions.create();
	    myStyledMapTypeOpts.setName("Blighted");

	    StyledMapType greyMapType = StyledMapType.create(
	        ArrayHelper.toJsArray(greyscaleStyle),
	        myStyledMapTypeOpts);

	    MapTypeControlOptions myMapTypeControlOpts = MapTypeControlOptions.create();
	    myMapTypeControlOpts.setMapTypeIds(ArrayHelper.toJsArrayString(
	        MapTypeId.ROADMAP.getValue(), MapTypeId.SATELLITE.getValue(),
	        "grey_scale"));

	    MapOptions myOptions = MapOptions.create();
	    //myOptions.setZoom(8.0);
	    //LatLng myLatLng = LatLng.create(51.4, -0.73);
	    //myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    myOptions.setMapTypeControlOptions(myMapTypeControlOpts);

	    gMap = GoogleMap.create(Document.get().getElementById(DOM_MAP_DIV), myOptions);

	    // Viewpoint could be in hash
	    String rawHash = Location.getHash();
		if( rawHash != null && !rawHash.isEmpty() && rawHash.startsWith("va:")) {
			// e.g. http://127.0.0.1:8888/BasicMap.html?gwt.codesvr=127.0.0.1:9997#va:54.0314:-8.0469:15
			rawHash = rawHash.substring(1); // remove hash character
			logger.fine("URL has hash:"+rawHash);
			Viewpoint vp = new Viewpoint(rawHash);
			gMap.setZoom(vp.getZoom());
			gMap.setCenter(vp.getCentre());
		}
	    if ( pv.getStringVariable("VIEWPOINT") != null ) {
			// from JS Config variable
			logger.fine("Taking viewpoint from JS Config");
			Viewpoint vp = new Viewpoint(pv.getStringVariable("VIEWPOINT"));
			gMap.setZoom(vp.getZoom());
			gMap.setCenter(vp.getCentre());

	    } else {

			// Go to bounding box
			String latA = pv.getStringVariable("LAT_A");
			String lngA = pv.getStringVariable("LNG_A");
			String latB = pv.getStringVariable("LAT_B");
			String lngB = pv.getStringVariable("LNG_B");
			LatLng pointA = LatLng.create(Double.parseDouble(latA), Double.parseDouble(lngA));
			LatLng pointB = LatLng.create(Double.parseDouble(latB), Double.parseDouble(lngB));
			LatLngBounds bounds = LatLngBounds.create(pointA, pointB);

			gMap.fitBounds(bounds);
	    }
	    gMap.getMapTypes().set("grey_scale", greyMapType);
	    gMap.setMapTypeId("grey_scale");

        // Google maps managed info window - only one open at a time
	    infowindowOpts = InfoWindowOptions.create();
	    infowindowOpts.setMaxWidth(200);
	    infowindow = InfoWindow.create(infowindowOpts);

 		int width = 32;
		int height = 37;
		int anchor_x = 16;
		int anchor_y = 35;

		normalIcon = MarkerImage.create(MAP_MARKER_ICON_PATH, Size.create(width, height),
										Point.create(0, 0), Point.create(anchor_x, anchor_y));

		activeIcon = MarkerImage.create(MAP_MARKER_ACTIVE_ICON_PATH, Size.create(width, height),
										Point.create(0, 0), Point.create(anchor_x, anchor_y));







        domManipulators(eventBus);

        // General, messy event handling setup
        AttachGeneralEvents(eventBus);

	}

	/**
	 * Read and fiddle with the DOM
	 */
	private void domManipulators(HandlerManager eventBus) {

		DomParser domParser = new DomParser();

	    FindMicroFormat_Geo coordsFromHtml = new FindMicroFormat_Geo(domParser, DOM_INFO_PANEL_DIV);

        // prepare a DOM element with the give id to fire a ClickFireEvent when it's clicked
        new ClickFireInteraction(domParser, eventBus, DOM_ADD_BLOG_POST);
        new ClickFireInteraction(domParser, eventBus, DOM_ADD_SURFACE);

        // elements marked with class="mouse_over mouse_over_1 ...." will have the "active"
        // class added on mouse over
        // TODO consider tablet users too
        new MouseInteractions(eventBus, DOM_MOUSEOVER_CLASS, DOM_MOUSEOVER_ACTIVE_CLASS);

		showHideAddSurface = new ShowHide(domParser, DOM_ADD_SURFACE);
		showHideAddPostHideItem = new ShowHide(domParser, DOM_ADD_POST_HIDE_ITEM);
		showHideAddPostInstructions = new ShowHide(domParser, DOM_ADD_POST_INSTRUCTIONS);
		showHideAddForm = new ShowHide(domParser, DOM_ADD_FORM);
		showHideAddBlogButton = new ShowHide(domParser, DOM_ADD_BLOG_POST);

        domParser.parseDom();

        if( coordsFromHtml.has_content() ){
        	for( BasicPoint bp: coordsFromHtml.getGeoPoints() ) {

        		markerAttributes.put(bp.getId(), bp);
        		IconMarker m = new IconMarker(eventBus, bp.getId(), normalIcon,
        		                              bp.getCoord(), bp.getTitle());
        		m.setMap(gMap);

        		// used with mouse over events to show relationship between marker and blog entry.
        		// important - the BasicPoint's ID is a String. The space separated parts of this
        		// string are treated as group IDs.
        		// @see: note in BasicPoint.java about abuse of id field. here is that abuse-
        		for( String anID : bp.getId().split("_") ) {
        			if( ! mapMarkers.containsKey(anID) ) {
        				mapMarkers.put(anID, new ArrayList<IconMarker>());
        			}
        			mapMarkers.get(anID).add(m);
        		}

        	}
        }

	}

	protected void AttachGeneralEvents(final HandlerManager eventBus) {

        // listen for these events so that markers can create mouse over events and change
        // colour when there is a mouseover event.
        // Note that Markers are decoupled from MouseOverEvents as the connection should be
        // at a high level - i.e. not all mouseovers will be about markers

        eventBus.addHandler(MouseOverEvent.TYPE, new MouseOverEventHandler() {
			@Override
			public void onMouseOver(MouseOverEvent e) {
				String mid = e.getMouseOver_id();
				if( mapMarkers.containsKey(mid)) {
					for( IconMarker mm : mapMarkers.get(mid) ) {
						mm.setIcon(activeIcon);
					}
				}
			}
		});
		eventBus.addHandler(MouseOutEvent.TYPE, new MouseOutEventHandler() {
			@Override
			public void onMouseOut(MouseOutEvent e) {
				String mid = e.getMouseOut_id();
				if( mapMarkers.containsKey(mid)) {
					for( IconMarker mm : mapMarkers.get(mid) ) {
						mm.setIcon(normalIcon);
					}
				}
			}
		});
		eventBus.addHandler(MouseClickEvent.TYPE, new MouseClickEventHandler() {
			@Override
			public void onMouseClick(MouseClickEvent e) {
				if( mapMarkers.containsKey(e.getMouseClick_id())) {
					// just go to the first item - the web designer should have realised that
					// multi IDs are only a good technique sometimes
					// TODO - idea for next time - iterate through the set of markers
					ArrayList<IconMarker> many_markers = mapMarkers.get(e.getMouseClick_id());
					IconMarker m = many_markers.get(0);
					LatLng ll = m.getPosition();
					LatLng mLatLng = LatLng.create(ll.lat(), ll.lng());
					gMap.panTo(mLatLng);
				}
			}
		});
		eventBus.addHandler(MouseOverMapMarkerEvent.TYPE, new MouseOverMapMarkerEventHandler() {

			@Override
			public void onMouseOverMapMarker(MouseOverMapMarkerEvent e) {
				IconMarker aMarker = (IconMarker) e.getMapMarker();
				String markerID = aMarker.getId();
				eventBus.fireEvent(new MouseOverEvent(markerID));

				for( String anID : markerID.split("_") ) {
					if( mapMarkers.containsKey(anID) ) {
			            eventBus.fireEvent(new MouseOverEvent(anID));
					}
				}

//					for( Entry<String, ArrayList<IconMarker>> entry : mapMarkers.entrySet()) {
//						for( IconMarker bMarker : entry.getValue() ) {
//					        if( aMarker ==  bMarker) {
//					            eventBus.fireEvent(new MouseOverEvent(entry.getKey()));
//					        }
//						}
//				    }

			}
		});
		eventBus.addHandler(MouseOutMapMarkerEvent.TYPE, new MouseOutMapMarkerEventHandler() {
			// TODO - check this
			@Override
			public void onMouseOutMapMarker(MouseOutMapMarkerEvent e) {
				IconMarker aMarker = (IconMarker) e.getMapMarker();

					for( Entry<String, ArrayList<IconMarker>> entry : mapMarkers.entrySet()) {
						for( IconMarker bMarker : entry.getValue() ) {
					        if( aMarker ==  bMarker) {
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


				if( e.getElement_id().equals(DOM_ADD_SURFACE)) {



					showHideAddSurface.hide();
					showHideAddPostHideItem.hide();
					showHideAddPostInstructions.show();
					showHideAddForm.show();
				}
				else if( e.getElement_id().equals(DOM_ADD_BLOG_POST)) {

				showHideAddPostInstructions.show();
				showHideAddBlogButton.hide();
				showHideAddPostHideItem.hide();

				// indicate to the user that they can click the map
				// TODO: better cursor
				MapOptions options = MapOptions.create();
				options.setDraggableCursor("crosshair");
				gMap.setOptions(options);
				final ShowHide instruction = showHideAddPostInstructions;
				// click map to do something
				gMap.addClickListenerOnce(new GoogleMap.ClickHandler() {

					@Override
					public void handle(MouseEvent event) {

						LatLng mapClickCoords = event.getLatLng();

//						// Feedback to user - show it on the map
//						BasicPoint newPoint = new BasicPoint(mapClickCoords.lat(),
//															 mapClickCoords.lng());

						IconMarker m = new IconMarker(eventBus, "", normalIcon,
						                              mapClickCoords, null);
						m.setMap(gMap);

						// Add coords to new blog post form and make form visible
						DomParser domParser = new DomParser();
				        new FormFiddle(domParser, DOM_ADD_FORM, mapClickCoords.lat(), mapClickCoords.lng());
					    domParser.parseDom();

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
		    	IconMarker mpm = (IconMarker) e.getMapPointMarker();

		    	if( ! markerAttributes.containsKey(mpm.getId()) )
		    		return;

		    	BasicPoint bp = markerAttributes.get(mpm.getId());

		    	// TODO - maybe use more intelligence with escaping HTML
		    	// For now, it's secure enough - unicode safe?
		    	// can't use Normalizer in GWT
		    	// http://stackoverflow.com/questions/1265282/recommended-method-for-escaping-html-in-java

		    	if (bp.getTitle() != null) {
		            text += "<h1>" + bp.getTitle().replace("<", "&lt;").replace(">", "&gt;") + "</h1>";
		        }
		        if (bp.getDescription() != null) {
		            text += "<p>" + bp.getDescription().replace("<", "&lt;").replace(">", "&gt;") + "</p>";
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
