package uk.co.plogic.gwt.lib.comms;

import uk.co.plogic.gwt.lib.comms.envelope.GenericEnvelope;

public interface DropBox<E extends GenericEnvelope> {
	
	/**
	 * called by UxPostalService on receipt of an envelope which contains a 
	 * LetterBox (section) which has been earmarked for processing by the class
	 * which implements this interface.
	 * 
	 * @param letterBox: a section of a UxPostalService delivery
	 */
	public abstract void onDelivery(E envelope);
	
}
