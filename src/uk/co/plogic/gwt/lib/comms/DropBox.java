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
	public abstract void onDelivery(String letterBoxName, String jsonEncodedPayload);

	/**
	 * called when an HTTP request has a status code of anything other then 200 or 204
	 *
	 * if there is a TCP error or other problem outside the HTTP wrapper, statusCode
	 * will be 0.
	 */
	public abstract void onDeliveryProblem(String letterBoxName, int statusCode);

}
