package uk.co.plogic.gwt.lib.comms;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UxPostalServiceTest {

	@Test
	public void test() {
		
		UxPostalService ups = new UxPostalService();
		
		String testServer = "http://api.example.com";
		List<KeyValuePair> params = new ArrayList<KeyValuePair>();
		params.add(new KeyValuePair("apple", "cheese"));

		ups.firstClassSend(testServer, "general", params);
		
		String json = ups.buildJson(testServer, ups.outgoingBuild);
		
		System.out.println(json);
		
		
//		fail("Not yet implemented XXXX");
	}

}
