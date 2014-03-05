package uk.co.plogic.gwt.lib.map.overlay;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.Polygon;
import com.google.maps.gwt.client.PolygonOptions;

public class Shapes extends AbstractOverlay {
	
	public Shapes(HandlerManager eventBus) {
		super(eventBus);
	}

	public void setMap(GoogleMap googleMap) {
		super.setMap(googleMap);
		
		MVCArray<MVCArray<LatLng>> multiPath = MVCArray.create();
		MVCArray<LatLng> path = MVCArray.create();

		// London
		path.push(LatLng.create(51.298981,-0.494310));
		path.push(LatLng.create(51.697121,-0.494310));
		path.push(LatLng.create(51.697121,0.182250));
		path.push(LatLng.create(51.298981,0.182250));
		multiPath.push(path);

		PolygonOptions polyOpts = PolygonOptions.create();
		polyOpts.setPaths(multiPath);
		polyOpts.setStrokeColor("000000");
		polyOpts.setStrokeOpacity(0.8);
		polyOpts.setStrokeWeight(2.0);
		polyOpts.setFillColor("FF0000");
		polyOpts.setFillOpacity(0.8);
		
		Polygon polygon = Polygon.create(polyOpts);
		polygon.setMap(googleMap);

	}

}
