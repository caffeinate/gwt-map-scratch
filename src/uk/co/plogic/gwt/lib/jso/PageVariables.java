package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

final public class PageVariables extends JavaScriptObject {

	// Empty constructor as required by JavaScriptObject
    protected PageVariables() {}
    
    public native String getStringVariable(String variableName) /*-{
		return $wnd["config"].hasOwnProperty(variableName) ? $wnd["config"][variableName] : null;
	}-*/;


    /**
     * 
     * @param variableName
     * @param defaultValue  to avoid complexity of Integer == null rather then int ?= null, set this
     * 						to a value that is out of band for the given variable
     * @return
     */
    public native int getIntegerVariable(String variableName, int defaultValue) /*-{
		return $wnd["config"].hasOwnProperty(variableName) ? $wnd["config"][variableName] : defaultValue;
	}-*/;
    
    public native JsArrayString getStringArrayVariable(String variableName) /*-{
		return $wnd["config"].hasOwnProperty(variableName) ? $wnd["config"][variableName] :null;
	}-*/;

    public native double getDoubleVariable(String variableName) /*-{
		return $wnd["config"].hasOwnProperty(variableName) ? $wnd["config"][variableName] : Number.NaN;
	}-*/;

    /**
     * responsive page layout
     * @return
     */
    public native JsArray<ResponsiveJso> getResponsiveElements() /*-{
		return $wnd["config"].hasOwnProperty("responsive") ? $wnd["config"]["responsive"] :[];
	}-*/;
}