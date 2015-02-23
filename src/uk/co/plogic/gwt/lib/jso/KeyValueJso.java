package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

final public class KeyValueJso extends JavaScriptObject {
    protected KeyValueJso() {}

    public native double getDoubleVariable(String key) /*-{
        return this.hasOwnProperty(key) ? this[key] : Number.NaN;
    }-*/;

    public native JsArrayString getKeys() /*-{
        return Object.keys(this);
    }-*/;

}
