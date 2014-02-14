package uk.co.plogic.gwt.lib.map;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MarkerImage;


public class IconMarker extends AbstractMapMarker {

	public IconMarker(	final HandlerManager eventBus,
						final MarkerImage markerIcon,
						LatLng coord,
						GoogleMap gmapx) {
		super(gmapx);
		this.gmap = gmapx;

		MarkerOptions options = MarkerOptions.create();
		//options.setTitle(bp.getTitle());
		//options.setTitle(bp.getId());
		if( markerIcon != null )
			options.setIcon(markerIcon);

		options.setPosition(coord);
		options.setMap(gmap);
	
		mapMarker = Marker.create(options);

	}
	
	public Marker getMapMarker() {
		return mapMarker;
	}
	
	public void setIcon(MarkerImage icon) {
		mapMarker.setIcon(icon);
	}
	
	public void setShadow(MarkerImage icon) {
		mapMarker.setShadow(icon);
	}

//	public BasicPoint getBasicPoint() {
//		return bp;
//	}

}
