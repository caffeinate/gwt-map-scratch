package uk.co.plogic.gwt.lib.ui;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.comms.envelope.ListOfObjectsEnvelope;
import uk.co.plogic.gwt.lib.events.ActiveUpdateElementEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEventHandler;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Take results from GazetteerResultsEvent and do a further JSON lookup. Send the results
 * of this JSON lookup to an ActiveUpdateElementEvent.
 * 
 * @author si
 *
 */

public class GazetteerLookupRelay {


	Logger logger = Logger.getLogger("GazetteerLookupRelay");
	private String jsonRequestUrlTemplate;

	/**
	 * 
	 * @param eventBus
	 * @param jsonRequestUrl	- URL template for JSON request @see buildUrl(..)
	 * @param jsonField			- assumes JSON response is an object, take this field, assumed to be string...
	 * @param targetActiveElementId - ... and pass it to this ActiveElement
	 */
	public GazetteerLookupRelay(final HandlerManager eventBus,
								final String jsonRequestUrlTemplate,
								final String jsonField,
								final String targetActiveElementId) {

		this.jsonRequestUrlTemplate = jsonRequestUrlTemplate;
	    final ListOfObjectsEnvelope envelope = new ListOfObjectsEnvelope();	    
	    final DropBox postGazetteerLookup = new DropBox() {

			@Override
			public void onDelivery(String letterBoxName, String jsonEncodedPayload) {
				
				logger.fine("general json got: "+jsonEncodedPayload);
				envelope.loadJson(jsonEncodedPayload);
				AttributeDictionary payload = envelope.get(0);

				String fieldValue = payload.get(jsonField);
				eventBus.fireEvent(new ActiveUpdateElementEvent(targetActiveElementId, fieldValue));
			}
	    	
	    };

		eventBus.addHandler(GazetteerResultsEvent.TYPE, new GazetteerResultsEventHandler() {

			@Override
			public void onResults(GazetteerResultsEvent e) {

				String url = buildUrl(e.getLat(), e.getLng());
				logger.fine("Relay lookup for: "+url);
				GeneralJsonService generalJson = new GeneralJsonService(url);
			    generalJson.setDeliveryPoint(postGazetteerLookup);
			    generalJson.setHttpMethodToGET();
			    generalJson.doRequest(envelope);
			}

	    });
	}

	private String buildUrl(double lat, double lng) {
		String url = jsonRequestUrlTemplate.replace("LAT", ""+lat);
		url = url.replace("LNG", ""+lng);
		return url;
	}

}
