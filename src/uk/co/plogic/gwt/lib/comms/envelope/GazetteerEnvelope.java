package uk.co.plogic.gwt.lib.comms.envelope;

import com.google.gwt.http.client.URL;

public class GazetteerEnvelope implements Envelope {

	String searchTerm;
	boolean autoSuggest = false;

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
		String url = "search="+URL.encodeQueryString(searchTerm);
		if( autoSuggest )
			url += "&auto_suggest=1";
		return url;
	}

	public void autoSuggest(boolean autoSuggest) {
		this.autoSuggest = autoSuggest;
	}

}
