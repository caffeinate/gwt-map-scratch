package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.events.ClusterSetPointCountEvent;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.overlay.ClusterPoints;
import uk.co.plogic.gwt.lib.widget.Slider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;

public class ClusterPointsMap implements EntryPoint {
	
	protected GoogleMap gMap;
	private HandlerManager eventBus;
	
	@Override
	public void onModuleLoad() {

		eventBus = new HandlerManager(null);

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
	    
	    
		String upsUrl = pv.getStringVariable("UPS_SERVICE");
		String mapMarkersUrl = pv.getStringVariable("MAP_MARKER_DYNAMIC_ICONS_URL");
		String clusterDataset = pv.getStringVariable("CLUSTER_DATASET");
		if( upsUrl != null && mapMarkersUrl != null && clusterDataset != null ) {
			ClusterPoints clusterPoints = new ClusterPoints(eventBus, mapMarkersUrl);
			clusterPoints.setMap(gMap);
			
		    // comms
		    UxPostalService uxPostalService = new UxPostalService(upsUrl); 

			
			// TODO envelopeSection could/should be in pv
			LetterBox letterBox = uxPostalService.createLetterBox(clusterDataset);
			// clusterPoints should recieve content sent from the server to this named
			// letter box....
			letterBox.addRecipient(clusterPoints);
			// ... and it can send via this letter box
			clusterPoints.setLetterBox(letterBox);
			
			
			String pointSliderDiv = pv.getStringVariable("CLUSTER_POINT_COUNT_SLIDER_DIV");
			if( pointSliderDiv != null ) {
				// add a slider widget
				RootPanel pDiv = RootPanel.get(pointSliderDiv);
				
				FlowPanel panel = new FlowPanel();
				
				Slider s = new Slider(9, "80%");
				panel.add(s);
				final HTML label = new HTML("45");
				panel.add(label);
				
				pDiv.add(panel);

				s.addBarValueChangedHandler(new BarValueChangedHandler() {

					@Override
					public void onBarValueChanged(BarValueChangedEvent event) {
						int scale = event.getValue()+1;
						int requestedPoints = scale*scale*5;
						label.setHTML(""+requestedPoints);
						eventBus.fireEvent(new ClusterSetPointCountEvent(requestedPoints));
					}
				});
				s.setValue(2); // == 45 points
				
			}
			
			
		}

	}

	
    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
