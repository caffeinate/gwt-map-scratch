package uk.co.plogic.gwt.lib.comms.envelope;

public class DatasetPointEnvelope implements Envelope {

	Double lat,lng;
	String dataset_name;
	String id;

	public void setPoint(String id, Double lat, Double lng, String dataset_name) {
		this.lng = lng;
		this.lat = lat;
		this.dataset_name = dataset_name;
		this.id = id;
	}

	public String asJson() {
		
		String json = "{";
		json += "\"lat\" : " + Double.toString(lat) + ", ";
		json += "\"lng\" : " + Double.toString(lng) + ", ";
		json += "\"id\" : " + id + ", ";
		json += "\"dataset_name\" : " + dataset_name + "}";

		return json;
	}

	@Override
	public void loadJson(String json) {
		// This envelope is only used to send bounding boxes; it doesn't decode the reply
	}

	@Override
	public String asUrlEncoded() {
		return "lat="+Double.toString(lat)+"&lng="+Double.toString(lng)+"&id="+id+"&dataset_name="+dataset_name;
	}

}
