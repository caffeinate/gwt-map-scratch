package uk.co.plogic.gwt.lib.comms;

import java.util.ArrayList;

import com.google.gwt.dev.util.collect.HashMap;


public class UxPostalService {
	
	private HashMap<String, DropBox> urlDropBoxes = new HashMap<String, DropBox>();
	
	class DropBox {
		public HashMap<String, ArrayList<LetterBox<?>>> sectionLetterbox = new HashMap<String, ArrayList<LetterBox<?>>>();
	}

	public void addRecipient(LetterBox<?> deliveryPoint, String envelopeSection, String url) {
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
	}

}
