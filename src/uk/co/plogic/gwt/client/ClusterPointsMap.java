package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.jso.PageVariables;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;

public class ClusterPointsMap implements EntryPoint {
	
	protected GoogleMap gMap;
	
	@Override
	public void onModuleLoad() {
		
		PageVariables pv = getPageVariables();
		
		MapOptions myOptions = MapOptions.create();
	    myOptions.setZoom(Double.parseDouble(pv.getStringVariable("ZOOM")));
	    LatLng myLatLng = LatLng.create(Double.parseDouble(pv.getStringVariable("LAT")),
	    								Double.parseDouble(pv.getStringVariable("LNG"))
	    								);
	    myOptions.setCenter(myLatLng);
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);

	    gMap = GoogleMap.create(Document.get().getElementById(pv.getStringVariable("DOM_MAP_DIV")),
	    													  myOptions);
	    


	}

    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
