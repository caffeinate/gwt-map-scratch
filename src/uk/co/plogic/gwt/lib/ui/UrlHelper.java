package uk.co.plogic.gwt.lib.ui;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.map.overlay.OverlayScorecard;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

/**
 * provider of info about the URL being served from. Not as literal as Window.Location.getHref();
 * as map might be in an iframe.
 * @author si
 *
 */
public class UrlHelper implements ShareUrl {

	protected GoogleMap gMap;
	protected NumberFormat numberFormat = NumberFormat.getFormat("#.00000");
	protected OverlayScorecard overlayScorecard;
	protected Logger logger = Logger.getLogger("ViewpointUrlScraper");
	
	public UrlHelper(GoogleMap gMap, OverlayScorecard overlayScorecard) {
		this.gMap = gMap;
		this.overlayScorecard = overlayScorecard;
	}
	
	@Override
	public String getUrlforShares() {


		String url;
		String baseUrl ="";
		if( inIframe() ) {
			// make Url from parent window's Url
			int beforeHash = baseUrl.lastIndexOf("#");
			if( beforeHash == -1 ) {
				url = baseUrl;
				logger.info("baseurl being used is:"+url);
			}
			else {
				url = baseUrl.substring(0, beforeHash);
				logger.info("baseurl (sub) being used is:"+url);
			}
		} else
			url = Window.Location.getProtocol()+"//"+Window.Location.getHost()+Window.Location.getPath();
		
		// Viewpoint type A - if possible
		if( gMap != null ) {
			LatLng centre = gMap.getCenter();

			String lat = numberFormat.format(centre.lat());
			String lng = numberFormat.format(centre.lng());
			int zoom = (int) gMap.getZoom();

			url += "#va:" + lat + ":" + lng + ":" + zoom;
			
			String [] layers = overlayScorecard.getVisibleOverlays();
			if( layers.length > 0 ) {
				url += ":";
				for( String layerId : layers ) {
					url += layerId; // no separator
				}
			}

		}

		return url;
	}
	
	/**
	 * 
	 * @param hash must start with this string
	 * @return String or null
	 */
	public String getHash(String prefix) {
		
		String rawHash = Location.getHash();
		if( rawHash != null && !rawHash.isEmpty() ) {
			rawHash = rawHash.substring(1);
			logger.fine("URL has hash:"+rawHash);
		} else
			return null;

		// mode 'a' - centre_lat:centre_lng:zoom:XXXYYYZZZ - first two args are mandatory
		if( ! rawHash.startsWith(prefix) )
			return null;

		return rawHash;

	}

	/** 
	 * 
	 * @return true if currently in an iframe
	 */
	public boolean inIframe() {
		String baseUrl = getParentUrl();
	    String ourUrl = Window.Location.getHref();
	    return ! baseUrl.equals(ourUrl);
	}

	/**
	 * 
	 * @return url of parent frame which the href when site fully occupies the browser
	 * 		   or href of parent if same domain as iframe or empty string if cross
	 *         domain. i.e. parent.location isn't available.
	 */
    private static final native String getParentUrl() /*-{
		try {
			return $wnd.parent.location.href;
		} catch(e) {
			return "";
		}
	}-*/;


}
