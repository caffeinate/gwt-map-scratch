package uk.co.plogic.gwt.lib.comms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dev.util.collect.HashMap;


public class UxPostalService {
	
	private HashMap<String, DropBox> urlDropBoxes = new HashMap<String, DropBox>();
	
	class DropBox {
		public HashMap<String, ArrayList<LetterBox<?>>> sectionLetterbox = new HashMap<String, ArrayList<LetterBox<?>>>();
	}
	
	public class RegisteredLetterBox {

		private String envelopeSection;
		private String url;
		
		public RegisteredLetterBox(String envelopeSection, String url) {
			// TODO - this needs more thought.
			// could this just be a pointer to the delivery point? delivery point could be
			// registered (addRecipient) multiple times and any RegisteredLetterBox be used?
			// or because the RegisteredLetterBox has the envelopeSection associated with it
			// the envelopeSection is set in the server request.
			this.url = url;
			this.envelopeSection = envelopeSection;
		}

		public String getEnvelopeSection() { return envelopeSection; }
		public String getUrl() { return url; }

		public void send(List<KeyValuePair> params) {
			UxPostalService.this.send(url, envelopeSection, params);
		}

	}

	/**
	 * 
	 * @param url			  : server
	 * @param envelopeSection : the server returns GenericEnvelope (or descendent of) which is a
	 * 							dictionary with subsections as key values
	 * @param deliveryPoint	  : onDelivery() on this object will be called
	 * @return RegisteredLetterBox : this is used to send messages. It keeps a track of the params
	 */
	public RegisteredLetterBox addRecipient(LetterBox<?> deliveryPoint, String envelopeSection, String url) {
		// TODO add http request method
		if( ! urlDropBoxes.containsKey(url) ) {
			urlDropBoxes.put(url, new DropBox());
		}
		HashMap<String, ArrayList<LetterBox<?>>> sections = urlDropBoxes.get(url).sectionLetterbox;
		if( ! sections.containsKey(envelopeSection) ) {
			sections.put(envelopeSection, new ArrayList<LetterBox<?>>());
		}
		ArrayList<LetterBox<?>> letter_boxes = sections.get(envelopeSection);
		letter_boxes.add(deliveryPoint);
		return new RegisteredLetterBox(envelopeSection, url);
	}

	public void send(String url, String envelopeSection, List<KeyValuePair> params) {
		// TODO Auto-generated method stub
		
	}

}
