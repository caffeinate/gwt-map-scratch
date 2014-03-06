package uk.co.plogic.gwt.lib.map.markers.utils;

import java.util.HashMap;

/**
 * Key value pairs. Just a HashMap for now, refine when data types are needed.
 * 
 * @author si
 *
 */

public class AttributeDictionary {

	private HashMap<String, String> dictionary = new HashMap<String, String>();
	
	public void set(String key, String value) {
		dictionary.put(key, value);
	}
	
	public String get(String key) {
		if( dictionary.containsKey(key) )
			return dictionary.get(key);
		else
			return null;
	}

}
