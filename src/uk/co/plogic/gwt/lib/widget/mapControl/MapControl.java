package uk.co.plogic.gwt.lib.widget.mapControl;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;

public interface MapControl {

	/**
	 * 
	 * @return an 18x18 icon which will be wrapped in a click handler and placed
	 * 		   in the control panel. The icon can be changed by the control to
	 *         reflect the open/close state for example.
	 *         return null if there isn't a user clickable icon for this control.
	 */
	public Image getIcon();
	
	/**
	 * Signal to the control that it's being opened by the user.
	 * 
	 * @return 	a panel suitable for placing in a FlowPanel or return null if
	 * 			there isn't one
	 */
	public Panel openControl();
	
	/**
	 * called when the control should leave it's open panel mode. The control
	 * could have closed itself without this call.
	 */
	// don't need this yet
	//public void closeControl();
}
