package uk.co.plogic.gwt.lib.comms.envelope;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

@Deprecated
public class NodeInfoEnvelope implements Envelope {

	String datasetName;
	String id;

	String htmlReply;

	public NodeInfoEnvelope() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String asJson() {
		String json = "{ \"dataset_name\" : \"" + datasetName + "\", ";
		json += "\"id\" : \"" + id + "\"}";
		return json;
	}

	@Override
	public void loadJson(String json) {

		JSONValue j = JSONParser.parseLenient(json);
		JSONString html = j.isString();
		htmlReply = html.stringValue();
	}

	public void request(String datasetName, String id) {
		this.datasetName = datasetName;
		this.id = id;
	}

	public String getHtmlReply() { return htmlReply; }

	@Override
	public String asUrlEncoded() {
		// TODO Auto-generated method stub
		return null;
	}

}
