package uk.co.plogic.gwt.lib.comms;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.comms.envelope.Envelope;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class GeneralJsonService {

    final Logger logger = Logger.getLogger("GeneralJsonService");
	String url;
	DropBox deliveryPoint;
	Method httpMethod = RequestBuilder.POST;

	public GeneralJsonService(){}
	public GeneralJsonService(String url) {
		this.url = url;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

	/**
	 * LetterBox is a connection class between the GeneralJsonService and producers and consumers of messages.
	 * @author si
	 *
	 */
	public class LetterBox {

	    final String letterBoxName;
		public LetterBox(final String letterBoxName) {
		    this.letterBoxName = letterBoxName;
		}

		public void setUrl(String u) {
		    GeneralJsonService.this.setUrl(u);
		}

		public void send() {
			GeneralJsonService.this.doRequest(letterBoxName);
		}

		public void send(Envelope envelope) {
            GeneralJsonService.this.doRequest(letterBoxName, envelope);
        }

	}
	public LetterBox createLetterBox(final String letterBoxName) {
		return new LetterBox(letterBoxName);
	}
	public LetterBox createLetterBox() {
        return new LetterBox("");
    }

	public void setHttpMethodToGET() {
		httpMethod = RequestBuilder.GET;
	}

	public void doRequest(final String letterBox) {
        setHttpMethodToGET();
        doRequest(letterBox, (Envelope) null);
	}

	public void doRequest(final String letterBox, Envelope envelope) {

	    String requestData = "";
	    if( envelope != null)
	        requestData = envelope.asUrlEncoded();

		RequestBuilder builder = new RequestBuilder(httpMethod, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			// POST to the request body. i.e. not via a form
			builder.sendRequest(requestData, new RequestCallback() {
			    public void onError(Request request, Throwable exception) {
			    	// Couldn't connect to server (could be timeout, SOP violation, etc.)
			    	System.out.println("HTTP error occurred");
			    }

			    public void onResponseReceived(Request request, Response response) {
			      if( response.getStatusCode() == 200
			       || response.getStatusCode() == 204 ) {
			          // Process the response in response.getText()
			    	  //System.out.println("Got reply...");
			    	  //System.out.println(response.getText());
			    	  deliveryPoint.onDelivery(letterBox, response.getText());
			      } else {
			    	  // Handle the error.  Can get the status text from response.getStatusText()
			    	  String msg = "Received non-200 http response status:";
			    	  msg += response.getStatusCode();
			    	  logger.fine(msg);
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
