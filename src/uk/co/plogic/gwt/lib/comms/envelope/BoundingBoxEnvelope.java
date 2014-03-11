package uk.co.plogic.gwt.lib.comms.envelope;

public class BoundingBoxEnvelope implements Envelope {

	Double x0,x1,y0,y1;
	String cached;

	public void requestBounding(Double x0, Double y0, Double x1, Double y1, String cached) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.cached = cached;
	}

	public String asJson() {
		
		String json = "{";
		json += "\"x0\" : " + Double.toString(x0) + ", ";
		json += "\"y0\" : " + Double.toString(y0) + ", ";
		json += "\"x1\" : " + Double.toString(x1) + ", ";
		json += "\"y1\" : " + Double.toString(y1) + ", ";
		json += "\"cached\" : " + cached + "}";

		return json;
	}

	@Override
	public void loadJson(String json) {
		// This envelope is only used to send bounding boxes; it doesn't decode the reply
	}

	@Override
	public String asUrlEncoded() {
		return "x0="+Double.toString(x0)+"&y0="+Double.toString(y0)+"&x1="+Double.toString(x1)+"&y1="+Double.toString(y1)+"&cached="+cached;
	}

}
