package uk.co.plogic.gwt.lib.comms;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashMap;

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