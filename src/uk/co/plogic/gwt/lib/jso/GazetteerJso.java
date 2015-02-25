package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class GazetteerJso extends JavaScriptObject {

	protected GazetteerJso() {}

	public native String getUrl()/*-{
    	return this.url;
	}-*/;

	/**
	 * where in the DOM to place the drop down style input. This is optional. If not
	 * provided then just the map-control type gazetteer-control will be used.
	 * @return
	 */
	public native String getTargetElement() /*-{
		return this.hasOwnProperty("target_element") ? this.target_element : null;
	}-*/;
}
