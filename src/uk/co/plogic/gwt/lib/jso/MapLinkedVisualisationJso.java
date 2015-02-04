package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

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

    public native String getKeyField() /*-{
        return this.hasOwnProperty("key_field") ? this.key_field : null;
    }-*/;

    public native String getValueField() /*-{
        return this.hasOwnProperty("value_field") ? this.value_field : null;
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
}
