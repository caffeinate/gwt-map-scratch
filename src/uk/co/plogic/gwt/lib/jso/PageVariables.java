package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

public final class PageVariables extends JavaScriptObject {

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
}
