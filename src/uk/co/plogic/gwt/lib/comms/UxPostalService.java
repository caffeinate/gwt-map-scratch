package uk.co.plogic.gwt.lib.comms;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.comms.envelope.Envelope;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import java.util.HashMap;


public class UxPostalService {
	
	// TODO add http request method somewhere!
	
	String url; // one url per UPS
	
	HashMap<String, ArrayList<DropBox>> replyDropBoxes = new HashMap<String, ArrayList<DropBox>>();
	HashMap<String, ArrayList<Envelope>> outgoingBuild = new HashMap<String, ArrayList<Envelope>>();
	HashMap<String, ArrayList<Envelope>> outgoingInFlight;

	public UxPostalService(String url) {
		this.url = url;
	}
	
	public LetterBox createLetterBox(String letterBoxName) {
		// TODO - maybe add better typing, i.e. argument like <Type> envelopeType
		if( ! replyDropBoxes.containsKey(letterBoxName) ) {
			replyDropBoxes.put(letterBoxName, new ArrayList<DropBox>() );
		}
		return new LetterBox(letterBoxName);
	}

	/**
	 * Connection class between the UxPostalService and producers and consumers of messages.
	 * @author si
	 *
	 */
	public class LetterBox {

		private String letterBoxName;
		
		public LetterBox(String letterBoxName) {
			this.letterBoxName = letterBoxName;
		}

		public String getLetterBoxName() { return letterBoxName; }
		
		
		/**
		 * 
		 * @param deliveryPoint	  : when replys with a 'letterBoxName' section arrive,
		 * 							onDelivery() on this object will be called
		 */
		public void addRecipient(DropBox deliveryPoint) {		
			ArrayList<DropBox> dropBox = replyDropBoxes.get(letterBoxName);
			dropBox.add(deliveryPoint);
		}

		public void send(Envelope envelope) {
			UxPostalService.this.firstClassSend(letterBoxName, envelope);
		}

	}


	/** send as soon as possible
	 * 
	 * @param letterBoxName
	 * @param params
	 */
	public void firstClassSend(String letterBoxName, Envelope envelope) {
		
		ArrayList<Envelope> mailQueue;
		if( outgoingBuild.containsKey(letterBoxName) ) {
			mailQueue = outgoingBuild.get(letterBoxName);
		} else {
			mailQueue = new ArrayList<Envelope>();
			outgoingBuild.put(letterBoxName, mailQueue);
		}
		mailQueue.add(envelope);
		// TODO set timer to send
		actualSend();
	}
	
	private void actualSend() {
		prepareOutgoing();
		// TODO request object
		// ?? Response.setHeader("Access-Control-Allow-Origin","http://myhttpserver");
		
		String json = buildJson(outgoingInFlight);
		//System.out.println(url);
		//System.out.println(json);


		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);

		try {
			// POST to the request body. i.e. not via a form
			builder.sendRequest(URL.encodeQueryString(json), new RequestCallback() {
			    public void onError(Request request, Throwable exception) {
			       // Couldn't connect to server (could be timeout, SOP violation, etc.)
			    }
	
			    public void onResponseReceived(Request request, Response response) {
			      if (200 == response.getStatusCode()) {
			          // Process the response in response.getText()
			    	  //System.out.println("Got reply...");
			    	  //System.out.println(getStatus(response.getText()));

			    	  //String jsonstr = "{\"value\" : [[12,34],[56,78]]}";
			    	  //JSONValue jsv = JSONParser.parseStrict(jsonstr);

			    	  JSONValue j = JSONParser.parseLenient(response.getText());
			    	  JSONObject d = j.isObject();
			    	  if( d != null ) {

			    		  for( String letterBoxName : d.keySet() ) {
			    			  if( replyDropBoxes.containsKey(letterBoxName) ) {
			    				  ArrayList<DropBox> delivery_points = 
			    						  					replyDropBoxes.get(letterBoxName);
			    				  for( DropBox deliveryPoint : delivery_points ) {
			    					  JSONString payload = d.get(letterBoxName).isString();
			    					  if( payload == null ) {
			    						  System.out.println("Empty payload for " + letterBoxName);
			    					  }
			    					  deliveryPoint.onDelivery(payload.stringValue());
			    				  }
			    			  }
			    		  }
			    		  
			    	  }

			    	  
			      } else {
			        // Handle the error.  Can get the status text from response.getStatusText()
			      }
			    }

		  });
		} catch (RequestException e) {
		  // Couldn't connect to server
		}
		
		
	}
	
	String buildJson(HashMap<String, ArrayList<Envelope>> envelopes) {

		String json = "{";
		for( String letterBoxName : envelopes.keySet() ) {
			// TODO - are string appends OK or should something faster be used

			if( json.length() > 1 )
				json += ", ";

			json += JsonUtils.escapeValue(letterBoxName)+" : [";

			String sectionJson = "";
			for( Envelope envelope : envelopes.get(letterBoxName) ) {

				if( sectionJson.length() > 0 )
					sectionJson += ", ";

				sectionJson += envelope.asJson();
			}
			json += sectionJson + "]";
		}

		return json + "}";
	}
	
	/**
	 * move messages from outgoingBuild to outgoingInFlight
	 */
	synchronized public void prepareOutgoing() {

		outgoingInFlight = outgoingBuild;
		outgoingBuild = new HashMap<String, ArrayList<Envelope>>();

	}
	
	private final native String getStatus(String json) /*-{
		eval('var r = ' + json);
		return r['status'];
	}-*/;

}
