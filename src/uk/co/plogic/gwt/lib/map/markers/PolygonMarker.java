package uk.co.plogic.gwt.lib.map.markers;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.Polygon;
import com.google.maps.gwt.client.PolygonOptions;

public class PolygonMarker extends AbstractShapeMarker implements ShapeMarker {

	Polygon polygon;
	PolygonOptions polyOpts;
	double opacity = 0.8;

	/**
	 * 
	 * @param gmap
	 * @param path simple path - no holes
	 */
	public PolygonMarker(final HandlerManager eventBus, String uniqueIdentifier,
					     ArrayList<LatLng> path) {
		super(eventBus, uniqueIdentifier);
		
		polyOpts = PolygonOptions.create();
		polyOpts.setStrokeColor("000000");
		polyOpts.setStrokeOpacity(opacity);
		polyOpts.setStrokeWeight(2.0);
		polyOpts.setFillColor("FF0000");
		polyOpts.setFillOpacity(opacity);
		createPolygon(path, polyOpts);
	}

	public PolygonMarker(final HandlerManager eventBus, String uniqueIdentifier,
						 ArrayList<LatLng> path, String strokeColour, double strokeWeight,
						 String fillColour, double opacity ) {
		super(eventBus, uniqueIdentifier);

		this.opacity = opacity;
		polyOpts = PolygonOptions.create();
		polyOpts.setStrokeColor(strokeColour);
		polyOpts.setStrokeOpacity(opacity);
		polyOpts.setStrokeWeight(strokeWeight);
		polyOpts.setFillColor(fillColour);
		polyOpts.setFillOpacity(opacity);
		createPolygon(path, polyOpts);
	}
	
	private void createPolygon(ArrayList<LatLng> pathArray, PolygonOptions polyOpts) {
		
		MVCArray<MVCArray<LatLng>> multiPath = MVCArray.create();
		MVCArray<LatLng> path = MVCArray.create();
		
		for(LatLng ll : pathArray ) {
			path.push(ll);
		}
		multiPath.push(path);
		polyOpts.setPaths(multiPath);
		polygon = Polygon.create(polyOpts);
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

}
