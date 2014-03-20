package uk.co.plogic.gwt.lib.ui;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService.LetterBox;
import uk.co.plogic.gwt.lib.comms.envelope.GazetteerEnvelope;
import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

public class GazetteerConnector implements DropBox {

	private GoogleMap gMap;
	private Element inputElement;
	private FlowPanel targetPanel;
	private GeneralJsonService gjson;
	private LetterBox letterBox;

	public GazetteerConnector(DomParser domParser, String url, String clickId,
							  String inputId, String targetId) {
		
		// setup comms
		gjson = new GeneralJsonService(url);
		gjson.setDeliveryPoint(this);
		letterBox = gjson.createLetterBox();
		
		domParser.addHandler(new DomElementByAttributeFinder("id", inputId) {
	        @Override
	        public void onDomElementFound(final Element element, String id) {
	        	inputElement = element;
	        	
	        	Event.setEventListener(element, new EventListener() {
	                @Override
	                public void onBrowserEvent(Event event) {
	                    if( DOM.eventGetType(event) == Event.ONKEYUP
	                    	&& event.getKeyCode()  == KeyCodes.KEY_ENTER )
	                    	runQuery();
	                }
	            });
	            Event.sinkEvents(element, Event.ONKEYUP);
	        }
	    });
		
	    domParser.addHandler(new DomElementByAttributeFinder("id", clickId) {
	        @Override
	        public void onDomElementFound(Element element, String id) {
	        	Event.setEventListener(element, new EventListener() {
	                @Override
	                public void onBrowserEvent(Event event) {
	                    if( DOM.eventGetType(event) == Event.ONCLICK )
	                    	runQuery();
	                }
	            });
	            Event.sinkEvents(element, Event.ONCLICK);
	        }
	    });
		
		// results etc. to show user. e.g. "location not found"
		RootPanel panel = RootPanel.get(targetId);
		targetPanel = new FlowPanel();
		panel.add(targetPanel);

	}

	public void setMap(GoogleMap gMap) {
		this.gMap = gMap;
	}

	public void runQuery() {
		
		targetPanel.clear();
		
		InputElement input = InputElement.as(inputElement);
		String searchTerm = input.getValue();
		
		GazetteerEnvelope envelope = new GazetteerEnvelope();
    	envelope.searchTerm(searchTerm);
		
		letterBox.send(envelope);
	}

	@Override
	public void onDelivery(String letterBoxName, String jsonEncodedPayload) {
		//System.out.println("ondelivery got:"+jsonEncodedPayload);
		JSONArray locations = JSONParser.parseLenient(jsonEncodedPayload).isArray();
		
		if( locations.size() == 0 ) {
			HTML msg = new HTML("Location not found!");
			targetPanel.add(msg);
		} else if( locations.size() == 1 ) {
			// move map to it
			JSONObject l = locations.get(0).isObject();
			//System.out.println("one result: "+l.get("name").isString().stringValue() );
			Double lat = l.get("lat").isNumber().doubleValue();
			Double lng = l.get("lng").isNumber().doubleValue();
			if( gMap != null ) {
				gMap.setZoom(14);
				gMap.panTo(LatLng.create(lat, lng));
			}
		} else {
			// many results
			// TODO display list
		}
		
		
	}

}
