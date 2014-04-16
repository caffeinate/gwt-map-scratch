package uk.co.plogic.gwt.lib.comms.envelope;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

/**
 * A common JSON structure is a list of objects (aka dictionaries). The loadJson(..) method
 * makes these easily accessable.
 * @author si
 *
 */
public class ListOfObjectsEnvelope  implements Envelope {

	ArrayList<AttributeDictionary> allContent = new ArrayList<AttributeDictionary>();
	
	@Override
	public String asJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String asUrlEncoded() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadJson(String json) {

		JSONValue j = JSONParser.parseLenient(json);
		JSONArray records = j.isArray();
		if( records == null ) return;
		
		for(int i=0; i < records.size(); i++ ) {
			JSONObject obj = records.get(i).isObject();
			if( obj == null ) {
				System.out.println("Non object found by ListOfObjectsEnvelope");
				continue;
			}
			AttributeDictionary ad = new AttributeDictionary();
			for( String k : obj.keySet()) {
				
				// at time of writing AttributeDictionary supports just String and Double
				if( obj.get(k).isNumber() != null )
					ad.set(k, obj.get(k).isNumber().doubleValue());
				else if( obj.get(k).isString() != null )
					ad.set(k, obj.get(k).isString().stringValue().toString());
			}
			allContent.add(ad);

		}
	}
	
	/**
	 * 
	 * @return count of objects that were found in the loaded JSON doc.
	 */
	public int size() {
		return allContent.size();
	}
	
	public AttributeDictionary get(int i) {
		return allContent.get(i);
	}

}
