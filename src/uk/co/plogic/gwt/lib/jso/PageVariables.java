package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

public final class PageVariables extends JavaScriptObject {

	// Empty constructor as required by JavaScriptObject
    protected PageVariables() {}
    
    public native String getStringVariable(String variableName) /*-{
		return $wnd["config"].hasOwnProperty(variableName) ? $wnd["config"][variableName] : null;
	}-*/;

}
