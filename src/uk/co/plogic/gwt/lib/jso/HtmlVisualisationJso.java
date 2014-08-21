package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class HtmlVisualisationJso extends JavaScriptObject {
	
	protected HtmlVisualisationJso() {}

	public native String getHtmlTemplate() /*-{
		return this.hasOwnProperty("html_template") ? this.html_template : null;
	}-*/;

	public native String getTargetElementId() /*-{
		return this.hasOwnProperty("target_element_id") ? this.target_element_id : null;
	}-*/;

	public native String getOverlayIds() /*-{
		return this.hasOwnProperty("overlay_ids") ? this.overlay_ids : null;
	}-*/;
}
