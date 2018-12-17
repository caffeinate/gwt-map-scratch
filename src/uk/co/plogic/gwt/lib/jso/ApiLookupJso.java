package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class ApiLookupJso extends JavaScriptObject {

	protected ApiLookupJso() {}

	public native String getUrl()/*-{
    	return this.url;
	}-*/;

	/**
	 * where in the DOM to place the stuff built by ApiLookupControl
	 * @return
	 */
	public native String getTargetElement() /*-{
		return this.hasOwnProperty("target_element") ? this.target_element : null;
	}-*/;

	/**
	 * String label to choose behaviour of GWT code. i.e. what should be done with
	 * this control.
	 * @return
	 */
	public native String getActionLabel() /*-{
        return this.hasOwnProperty("action") ? this.action : null;
    }-*/;
}
