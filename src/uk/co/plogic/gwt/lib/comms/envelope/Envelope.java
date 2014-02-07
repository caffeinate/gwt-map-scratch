package uk.co.plogic.gwt.lib.comms.envelope;

public interface Envelope {

	public String asJson();
	public void loadJson(String json);

}
