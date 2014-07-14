package uk.co.plogic.gwt.lib.ui;

import uk.co.plogic.gwt.lib.map.overlay.OverlayScorecard;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

public class ViewpointUrlScraper implements UrlScraper {

	protected GoogleMap gMap;
	protected NumberFormat numberFormat = NumberFormat.getFormat("#.00000");
	protected OverlayScorecard overlayScorecard;
	
	public ViewpointUrlScraper(GoogleMap gMap, OverlayScorecard overlayScorecard) {
		this.gMap = gMap;
		this.overlayScorecard = overlayScorecard;
	}
	
	@Override
	public String getUrlforShares() {


		String url = Window.Location.getProtocol()+"//"+Window.Location.getHost()+Window.Location.getPath();
		
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
		System.out.println(url);
		
		return url;
	}

	

}
