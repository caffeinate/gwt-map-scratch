package uk.co.plogic.gwt.lib.ui;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.comms.envelope.ListOfObjectsEnvelope;
import uk.co.plogic.gwt.lib.events.ActiveUpdateElementEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEvent;
import uk.co.plogic.gwt.lib.events.GazetteerResultsEventHandler;
import uk.co.plogic.gwt.lib.events.MapPanToEvent;
import uk.co.plogic.gwt.lib.events.MapZoomToEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

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
	private String overlaysToMakeVisible; // optional

	/**
	 *
	 * @param eventBus
	 * @param jsonRequestUrl	- URL template for JSON request @see buildUrl(..)
	 * @param jsonField			- assumes JSON response is an object, take this field, assumed to be string...
	 * @param targetActiveElementId - ... and pass it to this ActiveElement
	 */
	public GazetteerLookupRelay(final HandlerManager eventBus,
								final String jsonRequestUrlTemplate,
								final String htmlTemplate,
								final String targetActiveElementId,
								final boolean centre_map,
								final int zoomTo) {

		this.jsonRequestUrlTemplate = jsonRequestUrlTemplate;
	    final ListOfObjectsEnvelope envelope = new ListOfObjectsEnvelope();
	    final DropBox postGazetteerLookup = new DropBox() {

			@Override
			public void onDelivery(String letterBoxName, String jsonEncodedPayload) {

			    if( htmlTemplate == null ) {
			        logger.warning("html template missing for gazetteer relay");
			        return;
			    }

				logger.fine("general json got: "+jsonEncodedPayload);
				envelope.loadJson(jsonEncodedPayload);
				AttributeDictionary payload = envelope.get(0);

				String fieldValue = StringUtils.renderHtml(htmlTemplate, payload);
				eventBus.fireEvent(new ActiveUpdateElementEvent(targetActiveElementId, fieldValue));
			}

	    };

		eventBus.addHandler(GazetteerResultsEvent.TYPE, new GazetteerResultsEventHandler() {

			@Override
			public void onResults(GazetteerResultsEvent e) {

			    if( Double.isNaN(e.getLat()) )
			        // this is a clear event
			        return;

				if( jsonRequestUrlTemplate != null ) {
					String url = buildUrl(e.getLat(), e.getLng());
					logger.fine("Relay lookup for: "+url);
					GeneralJsonService generalJson = new GeneralJsonService(url);
				    generalJson.setDeliveryPoint(postGazetteerLookup);
				    generalJson.setHttpMethodToGET();
				    generalJson.doRequest("", envelope);
				}

			    if( overlaysToMakeVisible != null )
			    	eventBus.fireEvent(new OverlayVisibilityEvent(true, overlaysToMakeVisible));

			    if( zoomTo != -1 )
			    	eventBus.fireEvent(new MapZoomToEvent(zoomTo));

			    if( centre_map )
			    	eventBus.fireEvent(new MapPanToEvent(e.getLat(), e.getLng()));
			}

	    });
	}

	private String buildUrl(double lat, double lng) {
		String url = jsonRequestUrlTemplate.replace("LAT", ""+lat);
		url = url.replace("LNG", ""+lng);
		return url;
	}

	public void setVisibleOverlays(String overlaysToMakeVisible) {
		// TODO separate if multiple overlayIds given
		this.overlaysToMakeVisible = overlaysToMakeVisible;
	}

}
