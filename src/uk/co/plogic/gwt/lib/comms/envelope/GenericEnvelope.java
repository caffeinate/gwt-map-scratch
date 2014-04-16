package uk.co.plogic.gwt.lib.comms.envelope;

import java.util.ArrayList;

import com.google.gwt.core.client.JsonUtils;
import uk.co.plogic.gwt.lib.comms.KeyValuePair;


public class GenericEnvelope implements Envelope {

	ArrayList<KeyValuePair> simpleTuples = new ArrayList<KeyValuePair>();
	
	public void addKeyPair(KeyValuePair kvp) {
		simpleTuples.add(kvp);
	}
	
	
	/**
	 * 
	 * @return object encoded as JSON which is legal as the value
	 * 			part of a JSON dictionary.
	 */
	public String asJson() {
		
		String json = "";
		
		for( KeyValuePair kvp : simpleTuples ) {
			
			if( json.length() > 0 )
				json += ", ";

			json += "[" + JsonUtils.escapeValue(kvp.getKey()) + " : ";
			json += JsonUtils.escapeValue(kvp.getValue()) + "]";
		}
		
		// list of lists/tuples
		return "[ " + json + "]";
	}


	@Override
	public void loadJson(String json) {
		// Not implemented
	}


	@Override
	public String asUrlEncoded() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
