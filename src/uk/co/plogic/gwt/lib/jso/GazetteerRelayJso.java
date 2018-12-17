package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class GazetteerRelayJso extends JavaScriptObject {

	protected GazetteerRelayJso() {}

	public native String getUrlTemplate()/*-{
    	return this.hasOwnProperty("url_template") ? this.url_template : null;
	}-*/;

	public native String getHtmlTemplate() /*-{
		return this.hasOwnProperty("html_template") ? this.html_template : null;
	}-*/;

	public native String getTargetElementId() /*-{
		return this.hasOwnProperty("target_element_id") ? this.target_element_id : null;
	}-*/;

	public native String getOverlaysToMakeVisible() /*-{
		return this.hasOwnProperty("overlays_visible") ? this.overlays_visible : null;
	}-*/;

	public native boolean centre_map() /*-{
		return this.hasOwnProperty("centre_map") ? this.centre_map : false;
	}-*/;

	public native int getZoomTo() /*-{
		return this.hasOwnProperty("zoom_to") ? this.zoom_to : -1;
	}-*/;

	public native boolean getFocusOnMarker() /*-{
        return this.hasOwnProperty("focus_on_marker") ? this.focus_on_marker : false;
    }-*/;

	public native boolean showPointMarker() /*-{
        return this.hasOwnProperty("show_point_marker") ? this.show_point_marker : false;
    }-*/;
}
