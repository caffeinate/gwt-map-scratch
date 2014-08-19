package uk.co.plogic.gwt.lib.comms.envelope;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

/**
 * A common JSON structure is dictionary/object. The loadJson(..) method
 * makes these easily accessable.
 * @author si
 *
 */
public class ObjectEnvelope implements Envelope {

	AttributeDictionary allContent;
	
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

		allContent = new AttributeDictionary();
		JSONValue j = JSONParser.parseLenient(json);
		JSONObject obj = j.isObject();

		if( obj == null ) {
			System.out.println("Non object found");
			return;
		}

		for( String k : obj.keySet()) {

			// AttributeDictionary currently just supports just String and Double
			if( obj.get(k).isNumber() != null )
				allContent.set(k, obj.get(k).isNumber().doubleValue());
			else if( obj.get(k).isString() != null )
				allContent.set(k, obj.get(k).isString().stringValue().toString());
		}
	}

	public AttributeDictionary getAttributeDictionary() {
		return allContent;
	}

}
