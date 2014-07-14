package uk.co.plogic.gwt.lib.map;

import java.util.logging.Logger;

import com.google.maps.gwt.client.LatLng;

public class Viewpoint {
	
	protected LatLng centre;
	protected int zoom;
	protected Logger logger = Logger.getLogger("Viewpoint");
	
	public Viewpoint() {}
	
	public Viewpoint(double centre_lat, double centre_lng, int zoom) {
		centre = LatLng.create(centre_lat, centre_lng);
		this.zoom = zoom;
	}

	public Viewpoint(String colonDelimitedViewpoint) {

		String[] parts = colonDelimitedViewpoint.split(":");
		double lat = Double.parseDouble(parts[1]);
		double lng = Double.parseDouble(parts[2]);
		int zoom = Integer.parseInt(parts[3]);

		centre = LatLng.create(lat, lng);
		this.zoom = zoom;
	}

	public LatLng getCentre() {
		return centre;
	}

	public void setCentre(LatLng centre) {
		this.centre = centre;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

}
