package uk.co.plogic.gwt.lib.comms.envelope;


public class ClusterPointsEnvelope extends GenericEnvelope {

	Double x0,x1,y0,y1;
	
	public void requestBounding(Double x0, Double y0, Double x1, Double y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public String asJson() {
		
		String json = "{\"x0\" : " + Double.toString(x0) + ", ";
		json += "\"y0\" : " + Double.toString(y0) + ", ";
		json += "\"x1\" : " + Double.toString(x1) + ", ";
		json += "\"y1\" : " + Double.toString(y1) + "}";

		return json;
	}
	
}
