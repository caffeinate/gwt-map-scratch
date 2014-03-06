package uk.co.plogic.gwt.lib.map.markers;

import uk.co.plogic.gwt.lib.events.MapMarkerClickEvent;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MouseEvent;


public class IconMarker extends AbstractMarker {
	
	private String uniqueIdentifier;
	
	public IconMarker(	final HandlerManager eventBus,
						final MarkerImage markerIcon,
						LatLng coord,
						GoogleMap gmapx, String uniqueIdentifier) {
		super(gmapx);
		this.gmap = gmapx;
		this.uniqueIdentifier = uniqueIdentifier;
		
		MarkerOptions options = MarkerOptions.create();
		//options.setTitle(bp.getTitle());
		//options.setTitle(bp.getId());
		if( markerIcon != null )
			options.setIcon(markerIcon);

		options.setPosition(coord);
		options.setMap(gmap);
	
		mapMarker = Marker.create(options);
		final IconMarker thisMapPointMarker = this;
		mapMarker.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				//System.out.println("click:"+event.getLatLng());
				eventBus.fireEvent(new MapMarkerClickEvent(thisMapPointMarker));
			}
		});

	}
	
	public Marker getMapMarker() {
		return mapMarker;
	}
	
	public void setIcon(MarkerImage icon) {
		mapMarker.setIcon(icon);
	}

	public String getunique_identifier() { return uniqueIdentifier; }
}
