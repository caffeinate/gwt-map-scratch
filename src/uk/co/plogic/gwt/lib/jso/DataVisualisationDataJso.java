package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class DataVisualisationDataJso extends JavaScriptObject {

	protected DataVisualisationDataJso() {}

	public native int getSeriesIndex() /*-{
		return this.series_index;
	}-*/;

	public native KeyValueJso getValues() /*-{
		return this.hasOwnProperty("values") ? this.values : null;
	}-*/;

}
