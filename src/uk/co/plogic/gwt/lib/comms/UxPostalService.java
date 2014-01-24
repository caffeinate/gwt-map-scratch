package uk.co.plogic.gwt.lib.comms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashMap;


public class UxPostalService {
	
	DropBox<LetterBox<?>> replyDropBoxes = new DropBox<LetterBox<?>>();
	DropBox<List<KeyValuePair>> outgoingBuild = new DropBox<List<KeyValuePair>>();
	DropBox<List<KeyValuePair>> outgoingInFlight;

//	class DropBoxX {
//		public HashMap<String, ArrayList<LetterBox<?>>> sectionLetterbox = new HashMap<String, ArrayList<LetterBox<?>>>();
//	}
	
	/**
	 * url + section => something (Generics Object)
	 * @author si
	 *
	 */
	class DropBox<E extends Object> {
		
		public HashMap<String, HashMap<String, ArrayList<E>>> boxes = new HashMap<String, HashMap<String, ArrayList<E>>>();
		
		public DropBox() {}
		
		public void add(String url, String section, E something) {
			
			if( ! boxes.containsKey(url) ) {
				boxes.put(url, new HashMap<String, ArrayList<E>>());
			}

			HashMap<String, ArrayList<E>> s = boxes.get(url);
			if( ! s.containsKey(section) ) {
				s.put(section, new ArrayList<E>());
			}
			ArrayList<E> letter_boxes = s.get(section);
			letter_boxes.add(something);
		}
		
		/**
		 * return all section names
		 * @param url
		 * @return 
		 */
		public Set<String> get(String url) {
			return boxes.get(url).keySet();
		}

	}
	
	public class RegisteredLetterBox {

		private String envelopeSection;
		private String url;
		
		public RegisteredLetterBox(String url, String envelopeSection) {
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
			UxPostalService.this.firstClassSend(url, envelopeSection, params);
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
	public RegisteredLetterBox addRecipient(String url, String envelopeSection, LetterBox<?> deliveryPoint) {
		// TODO add http request method		
		replyDropBoxes.add(url, envelopeSection, deliveryPoint);
		return new RegisteredLetterBox(envelopeSection, url);
	}

	/** send as soon as possible
	 * 
	 * @param url
	 * @param envelopeSection
	 * @param params
	 */
	public void firstClassSend(String url, String envelopeSection, List<KeyValuePair> params) {
		outgoingBuild.add(url, envelopeSection, params);
		// set timer to send
	}
	
	private void actualSend() {
		prepareOutgoing();
	}
	
	String buildJson(String url, DropBox<List<KeyValuePair>> dropBox) {
		
		String ret = "";
		for( String section : dropBox.get(url) ) {
			ret += section + " ";
		}
		
		return ret;
	}
	
	/**
	 * move messages from outgoingBuild to outgoingInFlight
	 */
	synchronized public void prepareOutgoing() {

		outgoingInFlight = outgoingBuild;
		outgoingBuild = new DropBox<List<KeyValuePair>>();

	}

}
