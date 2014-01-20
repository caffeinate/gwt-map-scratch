package uk.co.plogic.gwt.lib.comms;

import com.google.gwt.core.client.JavaScriptObject;

public interface LetterBox<E extends JavaScriptObject> {
	
	/**
	 * called by UxPostalService on receipt of an envelope which contains a section
	 * which has been ear marked for processing by the user of this interface.
	 * 
	 * @param letter a section of a larger envelope
	 */
	public abstract void onDelivery(E letter);
	
}
