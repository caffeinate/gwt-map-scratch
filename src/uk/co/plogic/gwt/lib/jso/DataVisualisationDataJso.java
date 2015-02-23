package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class DataVisualisationDataJso extends JavaScriptObject {

	protected DataVisualisationDataJso() {}

	public native KeyValueJso getValues() /*-{
		return this.hasOwnProperty("values") ? this.values : null;
	}-*/;

	public native String getValueLabel() /*-{
        return this.hasOwnProperty("value_label") ? this.value_label : null;
    }-*/;
}
