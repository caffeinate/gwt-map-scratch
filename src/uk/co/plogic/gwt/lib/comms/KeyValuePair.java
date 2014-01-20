package uk.co.plogic.gwt.lib.comms;

public class KeyValuePair {
	private String key;
	private String value;
	public KeyValuePair(String k, String v) {
		key = k;
		value = v;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
