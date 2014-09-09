package uk.co.plogic.gwt.lib.events;

import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.event.shared.GwtEvent;

/**
 * When results arrive back from a user query they become this event.
 * 
 * "Location not found" becomes, searchTerm=null, lat=Double.NaN, lng=Double.NaN
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
    
    public GazetteerResultsEvent(String element_id, double lat, double lng) {
    	this.searchTerm = element_id;
    	this.lat = lat;
    	this.lng = lng;
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

	public void setFieldDictionary(AttributeDictionary ad) {
		dictionary = ad;
	}
	
	public AttributeDictionary getFieldDictionary() {
		return dictionary;
	}

}
