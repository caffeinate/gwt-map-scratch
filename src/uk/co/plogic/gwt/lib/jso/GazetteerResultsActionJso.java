package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class GazetteerResultsActionJso extends JavaScriptObject {
	
	protected GazetteerResultsActionJso() {}

	/**
	 * maybe implement a non-conditional version
	 * @return
	 */
	public native String getCondition() /*-{
		return this.hasOwnProperty("condition") ? this.condition : null;
	}-*/;

	public native String getTargetElementId() /*-{
		return this.hasOwnProperty("target_element_id") ? this.target_element_id : null;
	}-*/;

	/**
	 * 
	 * @return String template
	 */
	public native String getOnTrue() /*-{
		return this.hasOwnProperty("on_true") ? this.on_true : null;
	}-*/;

	public native String getOnFalse() /*-{
		return this.hasOwnProperty("on_false") ? this.on_false : null;
	}-*/;

}
