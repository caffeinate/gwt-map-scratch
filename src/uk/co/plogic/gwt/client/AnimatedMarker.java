package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.markers.PointMarker;
import uk.co.plogic.gwt.lib.map.markers.IconMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.MarkerMoveAnimation;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MouseEvent;


public class AnimatedMarker implements EntryPoint {

	protected GoogleMap gMap;


	@Override
	public void onModuleLoad() {

		PageVariables pv = getPageVariables();

		MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(Double.parseDouble(pv.getStringVariable("ZOOM")));
	    LatLng myLatLng = LatLng.create(Double.parseDouble(pv.getStringVariable("LAT")),
	    								Double.parseDouble(pv.getStringVariable("LNG"))
	    								);
	    myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);

	    gMap = GoogleMap.create(Document.get().getElementById(pv.getStringVariable("DOM_MAP_DIV")),
	    													  myOptions);
	    
	    final LatLng cheltenham = LatLng.create(51.91716758909015, -2.0775318145751953);
	    final LatLng enfield = LatLng.create(51.66233415804707, -0.07802009582519531);
	    
	    //MarkerOptions options = MarkerOptions.create();
	    //options.setPosition(cheltenham);
		//options.setMap(gMap);
	    final IconMarker iconMarker = new IconMarker(null,"only_marker",null,cheltenham);
	    
		final Marker mapMarker = iconMarker.getMapMarker();
		mapMarker.addClickListener(new ClickHandler() {

			@Override
			public void handle(MouseEvent event) {
				//mapMarker.setPosition(enfield);
				
				MarkerMoveAnimation ma = new MarkerMoveAnimation((PointMarker) iconMarker,
																 cheltenham, enfield);
				ma.run(500);
			}
			
		});
//	    gMap.addClickListener(new ClickHandler() {
//			@Override
//			public void handle(MouseEvent event) {
//				System.out.println("click:"+event.getLatLng());
//			}
//		});

	}

    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
