package uk.co.plogic.gwt.lib.dom;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.map.BasicPoint;

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;

/**
 * 
 * Problem with GIS data in JS environments is that they aren't indexed by search
 * engines as all the co-ords are transported via JSON. Instead, and as a little
 * demo for Potato they are picked out of the HTML where they are stored in a
 * microformat. This isn't a generic way of doing thing.
 * 
 * @author si
 *
 */
public class FindMicroFormat_Geo {

	private ArrayList<BasicPoint> geoPoints = new ArrayList<BasicPoint>();	
	/**
	 * 
	 * @param className 
	 */
	public FindMicroFormat_Geo(String idName) {
		

		
		DomParser domParser = new DomParser();
	    domParser.addHandler(new DomElementByAttributeFinder("id", idName) {
	        @Override
	        public void onDomElementFound(Element element, String id) {

	        	// http://microformats.org/wiki/geo
	        	NodeList<com.google.gwt.dom.client.Element> possibleElements = element.getElementsByTagName("div");
	        	for( int i=0; i<possibleElements.getLength(); i++ ) {
	                Element item = (Element) possibleElements.getItem(i);
	                if( "geo".equals(item.getAttribute("class")) ) {
	                	
	                	BasicPoint basicPoint = new BasicPoint();
	                	NodeList<com.google.gwt.dom.client.Element> geo = item.getElementsByTagName("span");
	                	for( int ii=0; ii<geo.getLength(); ii++ ) {
	                		Element geo_e = (Element) geo.getItem(ii);
	                		String className = geo_e.getAttribute("class");
	                		if( "latitude".equals(className) ) {
	                			basicPoint.setLat(Double.parseDouble(geo_e.getInnerText()));
	                		}
	                		if( "longitude".equals(className) ) {
	                			basicPoint.setLng(Double.parseDouble(geo_e.getInnerText()));
	                		}
	                		if( "title".equals(className) ) {
	                			basicPoint.setTitle(geo_e.getInnerText());
	                		}
	                		if( "description".equals(className) ) {
	                			basicPoint.setDescription(geo_e.getInnerText());
	                		}
	                	}
	                	geoPoints.add(basicPoint);
	                }
	        	}

	        }
	    });
	    domParser.parseDom();
	}

	public boolean has_content() {
		return ! geoPoints.isEmpty();
	}

	public ArrayList<BasicPoint> getGeoPoints() {
		return geoPoints;
	}

}
