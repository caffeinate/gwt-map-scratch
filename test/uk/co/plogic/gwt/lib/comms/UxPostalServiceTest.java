package uk.co.plogic.gwt.lib.comms;

//import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import uk.co.plogic.gwt.lib.comms.envelope.GenericEnvelope;


public class UxPostalServiceTest {

	@Test
	public void test() {
		
		UxPostalService ups = new UxPostalService("http://api.example.com");
		

//		List<KeyValuePair> params = new ArrayList<KeyValuePair>();
//		params.add(new KeyValuePair("apple", "cheese"));
//
//		ups.firstClassSend(testServer, "general", params);
		
		HashMap<String, ArrayList<GenericEnvelope>> envelopes = new HashMap<String, ArrayList<GenericEnvelope>>();
		
		ArrayList<GenericEnvelope> applesList = new ArrayList<GenericEnvelope>();
		GenericEnvelope applesEnvelope = new GenericEnvelope();
		applesList.add(applesEnvelope);
		envelopes.put("Apples", applesList);
		
//		String json = ups.buildJson(envelopes);
		
//		System.out.println(json);
		
		
//		fail("Not yet implemented XXXX");
	}

}
