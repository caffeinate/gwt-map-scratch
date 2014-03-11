package uk.co.plogic.gwt.lib.comms;

import uk.co.plogic.gwt.lib.comms.envelope.Envelope;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class GeneralJsonService {

	String url;
	DropBox deliveryPoint;

	public GeneralJsonService(String url) {
		this.url = url;
	}

	/**
	 * Connection class between the UxPostalService and producers and consumers of messages.
	 * @author si
	 *
	 */
	public class LetterBox {
		
		public LetterBox() {}

		public void send(Envelope envelope) {
			GeneralJsonService.this.doRequest(envelope);
		}

	}
	public LetterBox createLetterBox() {
		return new LetterBox();
	}

	public void doRequest(Envelope envelope) {
		
		
		//String requestData = "x0=-2.241211000000021&y0=54.43251194074159&x1=-1.6740418105468962&y1=54.848152999999996&cached=%5B%5D"; 
		String requestData = envelope.asUrlEncoded();
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			// POST to the request body. i.e. not via a form
			builder.sendRequest(requestData, new RequestCallback() {
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
