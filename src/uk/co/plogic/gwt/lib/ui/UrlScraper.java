package uk.co.plogic.gwt.lib.ui;

/**
 * provider of info about the URL being served from. Not as literal as Window.Location.getHref();
 * as map might be in an iframe.
 * @author si
 *
 */
public interface UrlScraper {

	public String getUrlforShares();

}
