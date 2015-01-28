package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

final public class MapLinkedVisualisationJso extends JavaScriptObject {

	protected MapLinkedVisualisationJso() {}

	/**
	 * target url - the source of the data supplied to the visualisation
	 * @return
	 */
	public native String getUrl() /*-{
		return this.hasOwnProperty("url") ? this.url : null;
	}-*/;

	public native String getTargetElementId() /*-{
		return this.hasOwnProperty("target_element_id") ? this.target_element_id : null;
	}-*/;

	/**
	 * Field within dataset that should be given to the visualisation
	 * @return
	 */
    public native JsArrayString getTargetFields() /*-{
        return this.hasOwnProperty("fields") ? this.fields : null;
    }-*/;

}
