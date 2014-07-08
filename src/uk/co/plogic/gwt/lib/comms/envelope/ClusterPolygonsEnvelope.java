package uk.co.plogic.gwt.lib.comms.envelope;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.maps.gwt.client.LatLng;

public class ClusterPolygonsEnvelope implements Envelope {

	Double x0,x1,y0,y1;
	ArrayList<ArrayList<LatLng>> paths = new ArrayList<ArrayList<LatLng>>();
	int requestedNoPoints = 20;
	protected Logger logger = Logger.getLogger("ClusterPolygonsEnvelope");


	public void requestBounding(Double x0, Double y0, Double x1, Double y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public void requestNoPoints(int requestedNoPoints) {
		this.requestedNoPoints = requestedNoPoints;
	}

	public String asJson() {
		
		String json = "{ \"polygons_required\" : " + requestedNoPoints + ", ";
		json += "\"x0\" : " + Double.toString(x0) + ", ";
		json += "\"y0\" : " + Double.toString(y0) + ", ";
		json += "\"x1\" : " + Double.toString(x1) + ", ";
		json += "\"y1\" : " + Double.toString(y1) + "}";

		return json;
	}

	@Override
	public void loadJson(String json) {
	
		// TODO - fix this nasty code which only handles normal polygons without holes
		
		JSONObject fullDoc = JSONParser.parseLenient(json).isObject();
		
		// TODO - check "type" == "FeatureCollection"

		JSONArray allFeatures = fullDoc.get("features").isArray();
		for(int ix=0; ix < allFeatures.size(); ix++ ) {

			JSONObject feature = allFeatures.get(ix).isObject();
			JSONObject geometry = feature.get("geometry").isObject();
			//coordinates = geometry.get("coordinates").isObject();
			String id = feature.get("id").isString().toString();

			JSONArray pointsArray = geometry.get("coordinates").isArray();

			ArrayList<LatLng> path = new ArrayList<LatLng>(); // not multipolygons
			JSONArray outerRing = pointsArray.get(0).isArray(); // 0 means we are ignoring holes
			for(int ii=0; ii < outerRing.size(); ii++ ) {
				
				JSONArray coord = outerRing.get(ii).isArray();;
				
				if( coord == null || coord.size() != 2 ) {
					throw new UnsupportedOperationException("Coord expected but not found");
				}
				
				double lng = coord.get(0).isNumber().doubleValue();
				double lat = coord.get(1).isNumber().doubleValue();
				//logger.info("got lat lng :"+lat);
				path.add(LatLng.create(lat, lng));

			}

			logger.fine("Polygon "+id+" with "+path.size()+" points");
			paths.add(path);
		
		}
	}

	public ArrayList<ArrayList<LatLng>> getPaths() {
		return paths;
	}

	@Override
	public String asUrlEncoded() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
