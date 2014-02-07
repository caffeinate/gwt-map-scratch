package uk.co.plogic.gwt.lib.map;

import uk.co.plogic.gwt.lib.events.MapMarkerClickEvent;
import uk.co.plogic.gwt.lib.events.MouseOutMapMarkerEvent;
import uk.co.plogic.gwt.lib.events.MouseOverMapMarkerEvent;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;


public class MapPointMarker {

	private BasicPoint bp;
	private GoogleMap gmap;
	private Marker mapMarker;
	private String map_marker_path;
	private String map_marker_active_path;
	private final MarkerOptions options;
	private MarkerImage normalIcon;
	private MarkerImage activeIcon;

	
	public MapPointMarker(final HandlerManager eventBus, final String map_marker_path,
						  final String map_marker_active_path, BasicPoint bp, GoogleMap gmapx) {
		super();
		this.bp = bp;
		this.gmap = gmapx;
		this.map_marker_path = map_marker_path;
		this.map_marker_active_path = map_marker_active_path;

		options = MarkerOptions.create();
		options.setTitle(bp.getTitle());
		//options.setTitle(bp.getId());
		

		// TODO: store marker meta data outside of the java; somewhere with the png would be good
 		int width = 32;
		int height = 37;
		int anchor_x = 16;
		int anchor_y = 35;
		normalIcon = MarkerImage.create(this.map_marker_path,
										  Size.create(width, height),
										  Point.create(0, 0),
										  Point.create(anchor_x, anchor_y));
		
		// set active icon up ready
		activeIcon = MarkerImage.create(this.map_marker_active_path,
				  Size.create(width, height),
				  Point.create(0, 0),
				  Point.create(anchor_x, anchor_y));

		options.setIcon(normalIcon);
		options.setPosition(bp.getCoord());
		options.setMap(gmap);
	
		mapMarker = Marker.create(options);
		//mapMarker.setDraggable(true);
		//mapMarker.setVisible(true);

		final MapPointMarker thisMapPointMarker = this;
		mapMarker.addClickListener(new ClickHandler() {

			@Override
			public void handle(MouseEvent event) {
				eventBus.fireEvent(new MapMarkerClickEvent(thisMapPointMarker));
			}
			
		});
		
		mapMarker.addMouseOverListener(new Marker.MouseOverHandler() {
			@Override
			public void handle(MouseEvent event) {
				eventBus.fireEvent(new MouseOverMapMarkerEvent(thisMapPointMarker));
			}
		});
		mapMarker.addMouseOutListener(new Marker.MouseOutHandler() {
			@Override
			public void handle(MouseEvent event) {
				eventBus.fireEvent(new MouseOutMapMarkerEvent(thisMapPointMarker));
			}
		});

		
	}
	
	/**
	 * toggle map icon
	 * @param show
	 */
	public void showActiveIcon(boolean show) {
		
		if( show ) mapMarker.setIcon(activeIcon);
		else mapMarker.setIcon(normalIcon);

	}

	public Marker getMapMarker() {
		return mapMarker;
	}

	public BasicPoint getBasicPoint() {
		return bp;
	}

	public double getLat() {
		return mapMarker.getPosition().lat();
	}
	public double getLng() {
		return mapMarker.getPosition().lng();
	}
	
	/**
	 * clear marker from map
	 */
	public void removeMarker() {
		// Not totally sure if this is enough but JS API seems to look
		// like it is
		mapMarker.setMap((GoogleMap) null);
	}

}
