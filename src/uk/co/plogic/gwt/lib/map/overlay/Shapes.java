package uk.co.plogic.gwt.lib.map.overlay;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.Polygon;
import com.google.maps.gwt.client.PolygonOptions;

public class Shapes extends AbstractOverlay {
	
	PolygonOptions defaultPolyOpts;
	
	public Shapes(HandlerManager eventBus) {
		
		super(eventBus);

		defaultPolyOpts = PolygonOptions.create();
		defaultPolyOpts.setStrokeColor("000000");
		defaultPolyOpts.setStrokeOpacity(0.8);
		defaultPolyOpts.setStrokeWeight(2.0);
		defaultPolyOpts.setFillColor("FF0000");
		defaultPolyOpts.setFillOpacity(0.8);

	}

	public void setMap(GoogleMap googleMap) {
		super.setMap(googleMap);
	}
	
	/**
	 * 
	 * @param path simple path - no holes
	 */
	public void addPolygon(ArrayList<LatLng> path) {
		addPolygon(path, defaultPolyOpts);
	}

	public void addPolygon(	ArrayList<LatLng> path, String strokeColour, double strokeOpacity,
							double strokeWeight, String fillColour, double fillOpacity ) {

		PolygonOptions polyOpts = PolygonOptions.create();
		polyOpts.setStrokeColor(strokeColour);
		polyOpts.setStrokeOpacity(strokeOpacity);
		polyOpts.setStrokeWeight(strokeWeight);
		polyOpts.setFillColor(fillColour);
		polyOpts.setFillOpacity(fillOpacity);
		addPolygon(path, polyOpts);

	}

	private void addPolygon(ArrayList<LatLng> pathArray, PolygonOptions polyOpts) {
		
		MVCArray<MVCArray<LatLng>> multiPath = MVCArray.create();
		MVCArray<LatLng> path = MVCArray.create();
		
		for(LatLng ll : pathArray ) {
			path.push(ll);
		}
		multiPath.push(path);
		polyOpts.setPaths(multiPath);
		Polygon polygon = Polygon.create(polyOpts);
		polygon.setMap(gMap);
	}

}
