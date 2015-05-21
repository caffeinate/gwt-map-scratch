package uk.co.plogic.gwt.lib.comms.envelope;

public class DatasetPointEnvelope implements Envelope {

	Double lat,lng;
	String datasetName;
	String id;
	String csrfToken;

	public void setPoint(String csrfToken, String id, Double lat, Double lng,
	                     String datasetName) {
		this.lng = lng;
		this.lat = lat;
		this.datasetName = datasetName;
		this.id = id;
		this.csrfToken = csrfToken;
	}

	public String asJson() {

		String json = "{";
		json += "\"lat\" : " + Double.toString(lat) + ", ";
		json += "\"lng\" : " + Double.toString(lng) + ", ";
		json += "\"id\" : " + id + ", ";
		json += "\"dataset_name\" : \"" + datasetName + "\", ";
		json += "\"csrf_token\" : \"" + csrfToken + "\"}";

		return json;
	}

	@Override
	public void loadJson(String json) {
		// This envelope is only used to send bounding boxes; it doesn't decode the reply
	}

	@Override
	public String asUrlEncoded() {
	    String encoded = "csrf_token="+csrfToken+"&lat="+Double.toString(lat);
	    encoded += "&lng="+Double.toString(lng)+"&id="+id+"&dataset_name=";
	    encoded += datasetName;
		return encoded;
	}

}
