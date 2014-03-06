package uk.co.plogic.gwt.lib.comms;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class GeneralJsonService {

	String url;
	DropBox deliveryPoint;

	public GeneralJsonService(String url) {
		this.url = url;
	}

	public void doRequest() {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	
		try {
			// POST to the request body. i.e. not via a form
			builder.sendRequest(URL.encodeQueryString(""), new RequestCallback() {
			    public void onError(Request request, Throwable exception) {
			       // Couldn't connect to server (could be timeout, SOP violation, etc.)
			    }
	
			    public void onResponseReceived(Request request, Response response) {
			      if (200 == response.getStatusCode()) {
			          // Process the response in response.getText()
			    	  //System.out.println("Got reply...");
			    	  //System.out.println(response.getText());
			    	  deliveryPoint.onDelivery("", response.getText());
			      } else {
				    // Handle the error.  Can get the status text from response.getStatusText()
				  }
				}
				
				});
		} catch (RequestException e) {
			// Couldn't connect to server
		}
	}

	public DropBox getDeliveryPoint() {
		return deliveryPoint;
	}

	public void setDeliveryPoint(DropBox deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}
}
