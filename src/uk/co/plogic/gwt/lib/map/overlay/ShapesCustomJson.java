package uk.co.plogic.gwt.lib.map.overlay;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MouseEvent;

public class ShapesCustomJson extends Shapes implements DropBox {
	
	private HashMap<String, PolygonMarker> polygonAttributes = new HashMap<String, PolygonMarker>();
	private String selectedMarkerId = null;

	public ShapesCustomJson(HandlerManager eventBus) {
		super(eventBus);
	}

	@Override
	public void onDelivery(String letterBoxName, String jsonEncodedPayload) {

		JSONObject fullDoc = JSONParser.parseLenient(jsonEncodedPayload).isObject();
		JSONObject d = fullDoc.get("payload").isObject();
  	  
		// TODO more error checking of payload
		// this is painful nasty parsing
		//overlayId = d.get("serial_number").toString().trim();

		JSONArray features = d.get("features").isObject().get("features").isArray();
		for(int i=0; i < features.size(); i++ ) {

			JSONObject allFeatures = features.get(i).isObject();
			JSONObject feature = allFeatures.get("geometry").isObject();
			
			String id = allFeatures.get("id").isNumber().toString();
			
			JSONObject properties = allFeatures.get("properties").isObject();
			double line_width = properties.get("line_width").isNumber().doubleValue();
			String fill_colour = properties.get("fill_colour").isString().stringValue();
			String line_colour = properties.get("line_colour").isString().stringValue();
			
			AttributeDictionary markerAttributes = new AttributeDictionary();
			JSONArray attributes = properties.get("attributes").isArray();
			for(int a=0; a < attributes.size(); a++ ) {
				JSONObject attribs = attributes.get(a).isObject();
				// type info is in the JSON doc. Am loosing it here for expediencies sake
				String key = attribs.get("display_name").toString();
				JSONValue value = attribs.get("value");
				String type = attribs.get("type").toString();
				if( type.equals("double") )
					markerAttributes.set(key, value.isNumber().doubleValue() );
				else
					markerAttributes.set(key, value.toString());
			}

			if( ! feature.get("type").isString().stringValue().equals("MultiPolygon") ) {
				System.out.println("No multi-polygon found");
				continue;
			}

			// coordinate is a list of multi-polygons
			// a multi-polygon is an array of polygons
			// a polygon is an array of outline : holes
		
			JSONArray multiPolygons = feature.get("coordinates").isArray();
			ArrayList<ArrayList<LatLng>> multiPath = new ArrayList<ArrayList<LatLng>>();
			for(int ii=0; ii < multiPolygons.size(); ii++ ) {
				JSONArray polygonHole = multiPolygons.get(ii).isArray();
				JSONArray polygon = polygonHole.get(0).isArray();
				// TODO - am ignoring holes here.
				
				ArrayList<LatLng> path = new ArrayList<LatLng>();
				for(int iii=0; iii < polygon.size(); iii++ ) {
					double lng = polygon.get(iii).isArray().get(0).isNumber().doubleValue();
					double lat = polygon.get(iii).isArray().get(1).isNumber().doubleValue();
					path.add(LatLng.create(lat, lng));
				}
				multiPath.add(path);
			}
			PolygonMarker p = new PolygonMarker(eventBus, id, line_colour,
												line_width, fill_colour);
			p.setMultiPolygonPath(multiPath);
			polygonAttributes.put(id, p);
			addPolygon(p);


		}

	}
	public void userInteractionWithMarker(UserInteraction interactionType, String markerId) {

		if( ! polygonAttributes.containsKey(markerId) )
			return;

		PolygonMarker targetMarker = polygonAttributes.get(markerId);
		
		// lock marker as selected
		if( interactionType == UserInteraction.CLICK ) {

			// 2nd click unselects
			if( selectedMarkerId == markerId) {
				clearSelectedMarker();
				return;
			}
			clearSelectedMarker();

			selectedMarkerId = markerId;
			targetMarker.setOpacity(1.0);
		}

		// don't clear on mouseout if marker has been set to selected
		if( selectedMarkerId != null )
			return;
		
		if( interactionType == UserInteraction.MOUSEOVER ) {
			targetMarker.setOpacity(1.0);
		}
		if( interactionType == UserInteraction.MOUSEOUT ) {
			targetMarker.setOpacity(getOpacity());
		}


	}
	
	private void clearSelectedMarker() {
		if(selectedMarkerId == null)
			return;
		polygonAttributes.get(selectedMarkerId).setOpacity(getOpacity());
		selectedMarkerId = null;
	}
	
	public void setMap(GoogleMap googleMap) {
		super.setMap(googleMap);

		googleMap.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				clearSelectedMarker();
			}
		});
	}
	
}