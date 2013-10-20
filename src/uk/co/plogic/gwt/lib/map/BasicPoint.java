package uk.co.plogic.gwt.lib.map;

import com.google.maps.gwt.client.LatLng;


/**
 * Co-ord on a map ; usually represented with a marker
 * @author si
 *
 */
public class BasicPoint {
	
	// TODO - make the attributes of BasicPoint a dictionary so arbitrary
	// like this 	private HashMap<String, String> namedAttrib = new HashMap<String, String>();

	private String title;
	private String description;
	private double lat;
	private double lng;
	private LatLng coord;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public LatLng getCoord() {
		if( coord != null ) {
			return coord;
		} else {
			coord = LatLng.create(lat, lng);
			return coord;
		}
	}


}
