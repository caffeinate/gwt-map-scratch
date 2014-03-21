package uk.co.plogic.gwt.lib.map.markers.utils;

import java.util.HashMap;
import java.util.Set;

/**
 * Key value pairs. Just a HashMap for now, refine when data types are needed.
 * 
 * @author si
 *
 */

public class AttributeDictionary {

	public enum DataType { dtString, dtDouble }
	
	private HashMap<String, AttributeProperties> dictionary = new HashMap<String, AttributeProperties>();
	
	class AttributeProperties {
		// hmmmmmmm - not great
		DataType dt;
		Double d;
		String s;
		
		public String toString() {
			if(dt == DataType.dtString)
				return s;
			if(dt == DataType.dtDouble)
				return Double.toString(d);
			return null;
		}
	}

	public void set(String key, String value) {
		AttributeProperties ap = new AttributeProperties();
		ap.dt = DataType.dtString;
		ap.s = value;
		dictionary.put(key, ap);
	}

	public void set(String key, Double value) {
		AttributeProperties ap = new AttributeProperties();
		ap.dt = DataType.dtDouble;
		ap.d = value;
		dictionary.put(key, ap);
	}

	public String get(String key) {
		if( dictionary.containsKey(key) )
			return dictionary.get(key).toString();
		else
			return null;
	}
	
	public double getDouble(String key) {
		if( dictionary.containsKey(key) )
			return dictionary.get(key).d;
		return Double.NaN;
	}

	public boolean isType(DataType dt, String key) {
		if( dictionary.containsKey(key) ) {
			if( dictionary.get(key).dt == dt )
				return true;
		}

		return false;
	}
	
	public Set<String> keySet() {
		return dictionary.keySet();
	}
	
	public String toString() {
		String asString = "";
		for( String key : keySet() ) {
			asString += "\""+key+"\":\""+get(key)+"\"; ";
		}
		return asString;
	}
	
}
