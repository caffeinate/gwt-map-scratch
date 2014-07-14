package uk.co.plogic.gwt.lib.ui;

import com.google.gwt.user.client.Window;


/**
 * provider of info about the URL being served from. Not as literal as Window.Location.getHref();
 * as map might be in an iframe.
 * @author si
 *
 */
public class UrlScraper {

	// This could be an interface

	public String getUrlforShares() {
		
		return Window.Location.getHref();
		
//		return "http://www.xxx.com/abc/#444xxxxxxxxxxxxxxxxxxx";
	}

}
