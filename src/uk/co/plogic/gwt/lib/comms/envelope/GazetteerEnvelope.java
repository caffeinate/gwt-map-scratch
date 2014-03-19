package uk.co.plogic.gwt.lib.comms.envelope;

import com.google.gwt.http.client.URL;

public class GazetteerEnvelope implements Envelope {

	String searchTerm;

	public void searchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
		
	}


	public String asJson() {
		String json = "{\"search\" : " + searchTerm + "}";
		return json;
	}

	@Override
	public void loadJson(String json) {
		System.out.println("got: "+json);
	}

	@Override
	public String asUrlEncoded() {
		return "search="+URL.encodeQueryString(searchTerm);
	}

}
