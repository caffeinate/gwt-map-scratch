package uk.co.plogic.gwt.lib.comms;

public interface DropBox {
	
	/**
	 * called by UxPostalService on receipt of an envelope which contains a 
	 * LetterBox (section) which has been earmarked for processing by the class
	 * which implements this interface.
	 * 
	 * @param jsonEncodedPayload: UxPostalService doesn't care what an envelope
	 * 							  contains, it just passes the payload as a String. 
	 */
	public abstract void onDelivery(String jsonEncodedPayload);
	
}
