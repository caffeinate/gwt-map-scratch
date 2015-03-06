package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.event.shared.GwtEvent;

/**
 * When results arrive back from a user query they become this event.
 *
 * The dictionary contains all of the response from the gazetteer server
 * so might have some of these fields-
 * gazette_title, lat, lng, name, qualifier
 *
 * "Location not found" becomes, searchTerm=null, lat=Double.NaN, lng=Double.NaN
 * clearing the search box also fires this empty result
 * @author si
 *
 */
public class GazetteerResultsEvent  extends GwtEvent<GazetteerResultsEventHandler> {

    public static Type<GazetteerResultsEventHandler> TYPE =
            new Type<GazetteerResultsEventHandler>();

    private String searchTerm;
	private double lat;
    private double lng;
    private AttributeDictionary dictionary;

    public GazetteerResultsEvent(String searchTerm, double lat, double lng,
                                 AttributeDictionary d) {
    	this.searchTerm = searchTerm;
    	this.lat = lat;
    	this.lng = lng;
    	this.dictionary = d;
    }

	@Override
	public Type<GazetteerResultsEventHandler> getAssociatedType() { return TYPE; }

	@Override
	protected void dispatch(GazetteerResultsEventHandler h) { h.onResults(this); }

	public String getSearchTerm() {
		return searchTerm;
	}

    public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public AttributeDictionary getAllGazetteerFields() {
		return dictionary;
	}

}
