package uk.co.plogic.gwt.lib.map.markers;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polygon;
import com.google.maps.gwt.client.Polygon.ClickHandler;
import com.google.maps.gwt.client.Polygon.MouseOverHandler;
import com.google.maps.gwt.client.Polygon.MouseOutHandler;
import com.google.maps.gwt.client.PolygonOptions;

/**
 * Polygons and MultiPolygons
 * @author si
 *
 */
public class PolygonMarker extends AbstractShapeMarker implements ShapeMarker {

	Polygon polygon;
	PolygonOptions polyOpts;
	double opacity = 0.8;
	double strokeWeight = 2.0;
	Logger logger = Logger.getLogger("PolygonMarker");

	/**
	 * 
	 * @param gmap
	 * @param path simple path - no holes
	 */
	public PolygonMarker(final HandlerManager eventBus, String uniqueIdentifier) {
		super(eventBus, uniqueIdentifier);
		
		polyOpts = PolygonOptions.create();
		polyOpts.setStrokeColor("000000");
		polyOpts.setStrokeOpacity(opacity);
		polyOpts.setStrokeWeight(strokeWeight);
		fillColour = "ff0000";
		polyOpts.setFillColor(fillColour);
		polyOpts.setFillOpacity(opacity);

	}

	public PolygonMarker(final HandlerManager eventBus, String uniqueIdentifier,
						 String strokeColour, double strokeWeight, String fillColour ) {
		super(eventBus, uniqueIdentifier);

		polyOpts = PolygonOptions.create();
		polyOpts.setStrokeColor(strokeColour);
		polyOpts.setStrokeOpacity(opacity);
		this.strokeWeight = strokeWeight;
		polyOpts.setStrokeWeight(strokeWeight);
		this.fillColour = fillColour.toLowerCase();
		polyOpts.setFillColor(this.fillColour);
		polyOpts.setFillOpacity(opacity);

	}
	
	public void setPolygonPath(ArrayList<LatLng> pathArray) {
		
		MVCArray<MVCArray<LatLng>> multiPath = MVCArray.create();
		MVCArray<LatLng> path = MVCArray.create();
		
		for(LatLng ll : pathArray ) {
			path.push(ll);
		}
		multiPath.push(path);
		polyOpts.setPaths(multiPath);
		polygon = Polygon.create(polyOpts);
		attachEventHandling();
		
	}

	/**
	 * 
	 * @param paths - first array of points is the outline; subsequent arrays are the holes
	 */
	public void setPolygonPath(MVCArray<MVCArray<LatLng>> paths) {

		polyOpts.setPaths(paths);
		polygon = Polygon.create(polyOpts);
		attachEventHandling();
	}

	
	@Override
	public void setMap(GoogleMap gMap) {
		super.setMap(gMap);
		polygon.setMap(gmap);
	}
	
	@Override
	public void show() { polygon.setMap(gmap); }

	@Override
	public void hide() { polygon.setMap((GoogleMap) null); }

	@Override
	public double getOpacity() {
		return opacity;
	}

	@Override
	public void setOpacity(double opacity) {
		this.opacity = opacity;
		polyOpts.setFillOpacity(opacity);
		polyOpts.setStrokeOpacity(opacity);
		polygon.setOptions(polyOpts);
	}
	
	private void attachEventHandling() {

		polygon.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				//System.out.println("click:"+event.getLatLng());
				
				relayUserAction(UserInteraction.CLICK);
				
				//eventBus.fireEvent(new MapMarkerClickEvent(thisMapPointMarker));
				//Window.alert("hi");
			}
		});
		
		polygon.addMouseOverListener(new MouseOverHandler() {
			@Override
			public void handle(MouseEvent event) {
				relayUserAction(UserInteraction.MOUSEOVER);
				//eventBus.fireEvent(new MouseOverMapMarkerEvent(thisMapPointMarker));
				logger.fine("Mouse over polygon "+getFillColour());
			}
		});
		polygon.addMouseOutListener(new MouseOutHandler() {
			@Override
			public void handle(MouseEvent event) {
				relayUserAction(UserInteraction.MOUSEOUT);
				//eventBus.fireEvent(new MouseOutMapMarkerEvent(thisMapPointMarker));
			}
		});
	}

	@Override
	public void highlight() {
		polyOpts.setStrokeOpacity(1.0);
		polyOpts.setStrokeWeight(strokeWeight*3);
		polygon.setOptions(polyOpts);
	}

	@Override
	public void unhighlight() {
		polyOpts.setStrokeOpacity(opacity);
		polyOpts.setStrokeWeight(strokeWeight);
		polygon.setOptions(polyOpts);
	}

	@Override
	public void setZindex(double zIndex) {
		polyOpts.setZindex(zIndex);
		if( polygon != null )
			polygon.setOptions(polyOpts);
	}

}
