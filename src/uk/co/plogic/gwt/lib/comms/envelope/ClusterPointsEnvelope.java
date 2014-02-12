package uk.co.plogic.gwt.lib.comms.envelope;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.map.BasicPoint;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


public class ClusterPointsEnvelope implements Envelope {

	Double x0,x1,y0,y1;
	ArrayList<BasicPoint> points;
	int requestedNoPoints = 20;

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
		
		String json = "{ \"points_required\" : " + requestedNoPoints + ", ";
		json += "\"x0\" : " + Double.toString(x0) + ", ";
		json += "\"y0\" : " + Double.toString(y0) + ", ";
		json += "\"x1\" : " + Double.toString(x1) + ", ";
		json += "\"y1\" : " + Double.toString(y1) + "}";

		return json;
	}

	@Override
	public void loadJson(String json) {
	
		JSONValue j = JSONParser.parseLenient(json);
		JSONArray clustered_points = j.isArray();
		if( clustered_points == null ) return;
		
		points = new ArrayList<BasicPoint>();
		for(int i=0; i < clustered_points.size(); i++ ) {
			JSONObject point = clustered_points.get(i).isObject();
			BasicPoint bp = new BasicPoint(	point.get("y").isNumber().doubleValue(),
											point.get("x").isNumber().doubleValue());
			bp.setId(point.get("id").isString().stringValue());
			Double weight = point.get("weight").isNumber().doubleValue();
			bp.setWeight(weight.intValue());
			points.add(bp);
		}
		
	}
	
	public ArrayList<BasicPoint> getPoints() {
		return points;
	}
	
}
