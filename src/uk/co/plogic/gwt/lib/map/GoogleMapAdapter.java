package uk.co.plogic.gwt.lib.map;

import uk.co.plogic.gwt.lib.events.MapReadyEvent;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.ArrayHelper;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
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

	public GoogleMapAdapter(HandlerManager eventBus, String mapDiv) {
		this.eventBus = eventBus;
		this.mapDiv = mapDiv;
		setup();
	}

	public GoogleMapAdapter(HandlerManager eventBus, Element mapContainerElement) {
		this.eventBus = eventBus;
		this.mapElement = mapContainerElement;
		setup();

	}

	private void setup() {
		myOptions = MapOptions.create();
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    myOptions.setPanControl(false);
	    
	    ZoomControlOptions zco = ZoomControlOptions.create();
	    zco.setPosition(ControlPosition.RIGHT_CENTER);
	    myOptions.setZoomControlOptions(zco);
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
