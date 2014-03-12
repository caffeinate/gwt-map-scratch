package uk.co.plogic.gwt.lib.map.markers.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class LegendAttributes {

	private ArrayList<LegendKey> keys = new ArrayList<LegendKey>();
	private HashMap<String, ArrayList<String>> colours = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> labels = new HashMap<String, ArrayList<String>>();
	
	public class LegendKey {
		public String colour;
		public String label;
		LegendKey(String colour, String label) {
			this.colour = colour;
			this.label = label;
		}
	}

	public void addKey(String colour, String label) {
		keys.add(new LegendKey(colour,label));

		if( ! colours.containsKey(colour) )
			colours.put(colour, new ArrayList<String>());
		colours.get(colour).add(label);

		if( ! labels.containsKey(label) )
			labels.put(label, new ArrayList<String>());
		labels.get(label).add(colour);

	}

	public int size() {
		return keys.size();
	}
	
	public ArrayList<LegendKey> getKeys() {
		return keys;
	}

}
