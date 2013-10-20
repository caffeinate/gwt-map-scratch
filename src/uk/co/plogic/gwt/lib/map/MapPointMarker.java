package uk.co.plogic.gwt.lib.map;

import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;


public class MapPointMarker {

	private BasicPoint bp;
	private GoogleMap gmap;
	private Marker mapMarker;

	
	public MapPointMarker(BasicPoint bpx, GoogleMap gmapx) {
		super();
		bp = bpx;
		gmap = gmapx;
	
		final MarkerOptions options = MarkerOptions.create();
		options.setTitle(bp.getTitle());
		
		/*
		//import com.google.maps.gwt.client.MarkerImage;
		//import com.google.maps.gwt.client.Size;
 		int width = 17;
		int height = 17;
		int anchor_x = 8;
		int anchor_y = 7;
		MarkerImage icon = MarkerImage.create(gwtIconUrl+map_marker,
										  Size.create(width, height),
										  Point.create(0, 0),
										  Point.create(anchor_x, anchor_y));
		options.setIcon(icon);*/

		options.setPosition(bp.getCoord());
		options.setMap(gmap);
	
		mapMarker = Marker.create(options);
	}


	public Marker getMapMarker() {
		return mapMarker;
	}
	
}
