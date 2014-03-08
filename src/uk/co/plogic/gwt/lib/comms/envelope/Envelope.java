package uk.co.plogic.gwt.lib.comms.envelope;

public interface Envelope {

	public String asJson();
	public String asUrlEncoded(); // @see URL.encodeQueryString(
	public void loadJson(String json);

}
