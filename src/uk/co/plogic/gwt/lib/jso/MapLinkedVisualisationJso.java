package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
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

    public native String getKeyLabel() /*-{
        return this.hasOwnProperty("key_label") ? this.key_label : null;
    }-*/;

    public native String getKeyField() /*-{
        return this.hasOwnProperty("key_field") ? this.key_field : null;
    }-*/;

    public native String getValueLabel() /*-{
        return this.hasOwnProperty("value_label") ? this.value_label : null;
    }-*/;

    public native String getValueField() /*-{
        return this.hasOwnProperty("value_field") ? this.value_field : null;
    }-*/;

    public native JsArrayString getValueFields() /*-{
        return this.hasOwnProperty("value_fields") ? this.value_fields : null;
    }-*/;

    /**
     * The map's shape/feature's ID. Used for mouse overs etc.
     * @return
     */
    public native String getFeatureIdField() /*-{
        return this.hasOwnProperty("feature_id_field") ? this.feature_id_field : null;
    }-*/;

    public native String getSortField() /*-{
        return this.hasOwnProperty("sort_field") ? this.sort_field : null;
    }-*/;

    public native String getVAxisLabel() /*-{
        return this.hasOwnProperty("vaxis_label") ? this.vaxis_label : null;
    }-*/;

    /**
     * Before any user interaction, load these into the chart.
     * At somepoint this will be a URL instead.
     * @return
     */
    public native JsArray<DataVisualisationDataJso> getSetValues() /*-{
        return this.hasOwnProperty("set_values") ? this.set_values : null;
    }-*/;


}
