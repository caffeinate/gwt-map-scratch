package uk.co.plogic.gwt.lib.map;

import uk.co.plogic.gwt.lib.events.MapPanToEvent;
import uk.co.plogic.gwt.lib.events.MapPanToEventHandler;
import uk.co.plogic.gwt.lib.events.MapReadyEvent;
import uk.co.plogic.gwt.lib.events.MapResizeEvent;
import uk.co.plogic.gwt.lib.events.MapViewChangedEvent;
import uk.co.plogic.gwt.lib.events.MapZoomToEvent;
import uk.co.plogic.gwt.lib.events.MapZoomToEventHandler;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.maps.gwt.client.ArrayHelper;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.BoundsChangedHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeControlOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MapTypeStyle;
import com.google.maps.gwt.client.MapTypeStyler;
import com.google.maps.gwt.client.StyledMapType;
import com.google.maps.gwt.client.StyledMapTypeOptions;
import com.google.maps.gwt.client.ZoomControlOptions;
import com.google.maps.gwt.client.GoogleMap.IdleHandler;
import com.google.maps.gwt.client.GoogleMap.ResizeHandler;

public class GoogleMapAdapter {

	private MapOptions myOptions;
	private String mapDiv;
	private StyledMapType greyMapType;
	private LatLngBounds bounds;
	private double zoom = -1;
	private LatLng centreCoord;
	private boolean mapLoaded = false;
	private HandlerManager eventBus;
	GoogleMap gMap;
	private Element mapElement;
	private Panel mapContainerPanel;

	public GoogleMapAdapter(HandlerManager eventBus, String mapDiv) {
		this.eventBus = eventBus;
		this.mapDiv = mapDiv;
		setup();
	}

	public GoogleMapAdapter(HandlerManager eventBus, Panel mapContainerPanel) {
		this.eventBus = eventBus;
		this.mapElement = mapContainerPanel.getElement();
		this.mapContainerPanel = mapContainerPanel;
		setup();
	}

	private void setup() {
		myOptions = MapOptions.create();
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    myOptions.setPanControl(false);

	    ZoomControlOptions zco = ZoomControlOptions.create();
	    zco.setPosition(ControlPosition.RIGHT_CENTER);
	    myOptions.setZoomControlOptions(zco);

	    eventBus.addHandler(MapPanToEvent.TYPE, new MapPanToEventHandler() {
			@Override
			public void onMapPanToEvent(MapPanToEvent event) {
				if( gMap != null )
					gMap.panTo(LatLng.create(event.getLat(), event.getLng()));
			}
	    });

	    eventBus.addHandler(MapZoomToEvent.TYPE, new MapZoomToEventHandler() {
			@Override
			public void onMapZoomEvent(MapZoomToEvent event) {
				if( gMap != null )
					gMap.setZoom(event.getZoom());
			}
	    });
	}

	/**
	 * create a simple div (FlowPanel) within the map's panel.
	 * It could be used to overlay annotations etc. on the map.
	 *
	 * @param panelId  don't set if null
	 * @param panelStyleClass don't set if null
	 * @return a panel already attached or null if not possible
	 */
	public FlowPanel createMapOverlayPanel(String panelId, String panelStyleClass) {

		if( mapContainerPanel == null )
			return null;

		FlowPanel marker_info = new FlowPanel();
		if( panelId != null )
			marker_info.getElement().setId(panelId);
		if( panelStyleClass != null )
			marker_info.setStyleName(panelStyleClass);
		mapContainerPanel.add(marker_info);
		return marker_info;
	}

	public void fitBounds(double lat_a, double lng_a, double lat_b, double lng_b) {
		//check for Double.isNaN ?
		LatLng pointA = LatLng.create(lat_a, lng_a);
		LatLng pointB = LatLng.create(lat_b, lng_b);
		bounds = LatLngBounds.create(pointA, pointB);
	}

	public void fitBounds(LatLngBounds bounds) {
		this.bounds = bounds;
	}

	public GoogleMap create() {

		if( mapElement != null )
			gMap = GoogleMap.create(mapElement, myOptions);
		else
			gMap = GoogleMap.create(Document.get().getElementById(mapDiv), myOptions);

		if( greyMapType != null ) {
			gMap.getMapTypes().set("grey_scale", greyMapType);
			gMap.setMapTypeId("grey_scale");
		}
		if( bounds != null )
			gMap.fitBounds(bounds);
		else{
			if( centreCoord != null )
				gMap.setCenter(centreCoord);
			if( zoom != -1 )
				gMap.setZoom(zoom);
		}

		gMap.addIdleListenerOnce(new IdleHandler() {

			@Override
			public void handle() {
				// the map is ready so prompt all the overlays
				mapLoaded = true;
				eventBus.fireEvent(new MapReadyEvent());
			}

		});

	    /*
	     * re-broadcast map's own resize. Doing this de-couples google maps and we
	     * just pass the event bus to anything that needs to know this.
	     */
	    gMap.addResizeListener(new ResizeHandler() {

			@Override
			public void handle() {
				eventBus.fireEvent(new MapResizeEvent());
			}

		});

	    /* decoupling google maps as above
	     * the event bus event is more generalised (as it covers any change to
	     * the map's view) but google maps' BoundsChanged event seems to cover
	     * all.
	     */
	    gMap.addBoundsChangedListener(new BoundsChangedHandler() {

            @Override
            public void handle() {
                eventBus.fireEvent(new MapViewChangedEvent());
            }

	    });

		return gMap;
	}

	public void setGoogleMap(GoogleMap map) {
		gMap = map;
	}

	public GoogleMap getGoogleMap() {
		return gMap;
	}

	public void setGreyscale() {

		MapTypeStyle greyscaleStyle = MapTypeStyle.create();
		greyscaleStyle.setStylers(ArrayHelper.toJsArray(MapTypeStyler.saturation(-99)));

		StyledMapTypeOptions styledMapTypeOptions = StyledMapTypeOptions.create();
		styledMapTypeOptions.setName("Greyscale");

		greyMapType = StyledMapType.create(ArrayHelper.toJsArray(greyscaleStyle), styledMapTypeOptions);

		MapTypeControlOptions myMapTypeControlOpts = MapTypeControlOptions.create();
		myMapTypeControlOpts.setMapTypeIds(
				ArrayHelper.toJsArrayString(MapTypeId.ROADMAP.getValue(),
											MapTypeId.SATELLITE.getValue(),
											"grey_scale"
											));
		myOptions.setMapTypeControlOptions(myMapTypeControlOpts);

	}

	public boolean isMapLoaded() { return mapLoaded; }

	public void setViewpoint(Viewpoint vp) {
		centreCoord = vp.getCentre();
		zoom = vp.getZoom();

		if( gMap != null ) {
			if( centreCoord != null )
				gMap.setCenter(centreCoord);
			if( zoom != -1 )
				gMap.setZoom(zoom);
		}
	}
}
