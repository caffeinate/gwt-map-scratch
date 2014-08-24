package uk.co.plogic.gwt.lib.jso;

import com.google.gwt.core.client.JavaScriptObject;

final public class ResponsiveJso extends JavaScriptObject {

	protected ResponsiveJso() {}

	public native String getResponsiveMode() /*-{
		return this.responsive_mode;
	}-*/;

	public native String getTargetElementId() /*-{
		return this.target_element_id;
	}-*/;

	/**
	 * optional
	 * @return
	 */
	public native String getAddClass() /*-{
		return this.hasOwnProperty("add_class") ? this.add_class : null;
	}-*/;

	/**
	 * optional
	 * @return
	 */
	public native String getRemoveClass() /*-{
		return this.hasOwnProperty("remove_class") ? this.remove_class : null;
	}-*/;

}
