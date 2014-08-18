package uk.co.plogic.gwt.lib.widget;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService.LetterBox;
import uk.co.plogic.gwt.lib.comms.envelope.GazetteerEnvelope;
import uk.co.plogic.gwt.lib.dom.DomElementByAttributeFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEvent;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

public class GazetteerSearch extends Composite implements DropBox {
	
	private GoogleMap gMap;
	private FlowPanel targetPanel = new FlowPanel();
	private GeneralJsonService gjson;
	private LetterBox letterBox;
	private HandlerManager eventBus;
	private String searchTerm;
	final static int delayDuration = 500;
	private Timer requestTimer;
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private final SuggestBox suggestbox = new SuggestBox(oracle);
	final Logger logger = Logger.getLogger("GazetteerSearch");
	private HTML locationNotFound;

	
	public GazetteerSearch(HandlerManager eventBus, DomParser domParser,
							String url, String inputId) {

		/*
		<div class="input-group">
			<span class="input-group-addon">Postcode</span>
			<input id="search_text" name="search" type="text" class="form-control" placeholder="EC1A">
			<span class="input-group-btn">
				<button id="search_button" class="btn btn-default" type="button">Go!</button>
			</span>
		</div>
		*/
		this.eventBus = eventBus;
		
		// setup comms
		gjson = new GeneralJsonService(url);
		gjson.setDeliveryPoint(this);
		letterBox = gjson.createLetterBox();
		
		domParser.addHandler(new DomElementByAttributeFinder("id", inputId) {
	        @Override
	        public void onDomElementFound(final Element element, String id) {

	        	Event.setEventListener(element, new EventListener() {
	                @Override
	                public void onBrowserEvent(Event event) {
	                    if( DOM.eventGetType(event) == Event.ONKEYUP
	                    	&& event.getKeyCode()  == KeyCodes.KEY_ENTER )
	                    	runQuery(suggestbox.getValue());
	                }
	            });
	            Event.sinkEvents(element, Event.ONKEYUP);
	        }
	    });
		
	    FlowPanel searchBoxPanel = new FlowPanel();
	    
	    searchBoxPanel.setStyleName("input-group");
	    
	    HTML title = new HTML("Postcode");
	    title.setStyleName("input-group-addon");
	    searchBoxPanel.add(title);
	    
	    suggestbox.setStyleName("form-control");
	    suggestbox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				requestTimer.cancel();
				requestTimer.schedule(delayDuration);
			}
		});

	    suggestbox.addSelectionHandler(new SelectionHandler<Suggestion>(){

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedLocationString = event.getSelectedItem().getReplacementString();
				logger.fine("selection for "+selectedLocationString);
				runQuery(selectedLocationString);
			}
	    	
	    });

	    
	    searchBoxPanel.add(suggestbox);
	    // TODO, maybe id="search_text" class="form-control" type="text" placeholder="EC1A" name="search"
	    		
	    FlowPanel buttonPanel = new FlowPanel();
	    buttonPanel.setStyleName("input-group-btn");
	    Button go = new Button("Go!");
	    go.setStyleName("btn");
	    go.addStyleName("btn-default");
	    go.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				runQuery(suggestbox.getValue());
			}
		});
	    buttonPanel.add(go);
	    searchBoxPanel.add(buttonPanel);
		
		requestTimer = new Timer() {  
		    @Override
		    public void run() {
		    	//logger.info("making request now");
		    	runQuery(suggestbox.getValue());
		    }
		};
		
		FlowPanel searchExtraPanel = new FlowPanel();
		locationNotFound = new HTML("Location not found!");
		locationNotFound.setVisible(false);
		locationNotFound.setStyleName("searchExtraHtml");
		searchExtraPanel.add(locationNotFound);
		
		targetPanel.add(searchBoxPanel);
		targetPanel.add(searchExtraPanel);

		initWidget(targetPanel);
	}

	public void setMap(GoogleMap gMap) {
		this.gMap = gMap;
	}

	public void runQuery(String searchTerm) {
		
		this.searchTerm = searchTerm;
		
		GazetteerEnvelope envelope = new GazetteerEnvelope();
    	envelope.searchTerm(searchTerm);
		
		letterBox.send(envelope);
	}

	@Override
	public void onDelivery(String letterBoxName, String jsonEncodedPayload) {
		//System.out.println("ondelivery got:"+jsonEncodedPayload);
		locationNotFound.setVisible(false);
		
		JSONObject fullDoc = JSONParser.parseLenient(jsonEncodedPayload).isObject();
		JSONArray locations = fullDoc.get("locations").isArray();
  	  
		if( locations.size() == 0 ) {
			locationNotFound.setVisible(true);
			//eventBus.fireEvent(new GazetteerResultsEvent(null, Double.NaN, Double.NaN));
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
			eventBus.fireEvent(new GazetteerResultsEvent(searchTerm, lat, lng));
		} else {
			// many results
			oracle.clear();
			for(int i=0; i<locations.size(); i++) {
				JSONObject l = locations.get(i).isObject();
				String suggestedItem = l.get("name").isString().stringValue();
				//logger.info(suggestedItem);
				oracle.add(suggestedItem);
			}
			suggestbox.refreshSuggestionList();
			suggestbox.showSuggestionList();

		}
		
		
	}
}
