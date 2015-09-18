package uk.co.plogic.gwt.lib.comms.envelope;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PointValueEnvelope implements Envelope {

    public Double lat,lng;
	public Double value;
	String datasetName;
	String projectName;
	String id;
	String csrfToken;


	public String asJson() {

		return "";
	}

	@Override
    public void loadJson(String json) {

        JSONValue j = JSONParser.parseLenient(json);
        JSONObject reply = j.isObject();

        //"project"
        //"serial"
        //"success"
        JSONNumber v = reply.get("value").isNumber();
        if( v == null ) value = Double.NaN;
        else            value = v.doubleValue();

        lat = reply.get("lat").isNumber().doubleValue();
        lng = reply.get("lng").isNumber().doubleValue();

    }

	@Override
	public String asUrlEncoded() {
		return "";
	}

}
