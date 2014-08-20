package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class GazetteerRelayJso extends JavaScriptObject {
	
	protected GazetteerRelayJso() {}

	public native String getUrlTemplate()/*-{
    	return this.url_template;
	}-*/;

	public native String getJsonField() /*-{
		return this.hasOwnProperty("field") ? this.field : null;
	}-*/;

	public native String getTargetElement() /*-{
		return this.hasOwnProperty("target_element") ? this.target_element : null;
	}-*/;

	public native String getOverlaysToMakeVisible() /*-{
		return this.hasOwnProperty("overlays_visible") ? this.overlays_visible : null;
	}-*/;

}
