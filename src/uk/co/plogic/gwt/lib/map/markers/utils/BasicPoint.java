package uk.co.plogic.gwt.lib.map.markers.utils;

import com.google.maps.gwt.client.LatLng;


/**
 * Co-ord on a map ; usually represented with a marker
 * @author si
 *
 */
public class BasicPoint {
	
	// TODO - make the attributes of BasicPoint a dictionary so arbitrary
	// like this 	private HashMap<String, String> namedAttrib = new HashMap<String, String>();
	
	// TODO - id is a String and is abused by map markers who use spaces to separate multiple
	//        ids to demonstrate marker groupings. This all needs clarifying by way of more
	//		  obvious coding and data structures.

	private String id;
	private String title;
	private String description;
	private double lat;
	private double lng;
	private LatLng coord;
	private int weight;

	public BasicPoint() {
		super();
	}

	public BasicPoint(double lat, double lng) {
		super();
		this.setLat(lat);
		this.setLng(lng);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
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
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
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
