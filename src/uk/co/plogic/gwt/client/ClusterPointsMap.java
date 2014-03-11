package uk.co.plogic.gwt.client;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.events.ClusterChangePointCountEvent;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.overlay.ClusterPoints;
import uk.co.plogic.gwt.lib.ui.HideReveal;
import uk.co.plogic.gwt.lib.widget.Slider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.GoogleMap;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;

public class ClusterPointsMap implements EntryPoint {
	
	protected GoogleMap gMap;
	private HandlerManager eventBus;

	@Override
	public void onModuleLoad() {

		eventBus = new HandlerManager(null);
		PageVariables pv = getPageVariables();
		
		DomParser domParser = new DomParser();
		String hideReveal = pv.getStringVariable("HIDE_REVEAL");
		if( hideReveal != null ) {
			new HideReveal(domParser, eventBus, hideReveal);
		}
		domParser.parseDom();

		GoogleMapAdapter gma = new GoogleMapAdapter(eventBus, pv.getStringVariable("DOM_MAP_DIV"));
	    gma.fitBounds(	pv.getDoubleVariable("LAT_A"), pv.getDoubleVariable("LNG_A"),
	    				pv.getDoubleVariable("LAT_B"), pv.getDoubleVariable("LNG_B"));

	    gMap = gma.create();


		String upsUrl = pv.getStringVariable("UPS_SERVICE");
		String mapMarkersUrl = pv.getStringVariable("MAP_MARKER_DYNAMIC_ICONS_URL");
		String clusterDataset = pv.getStringVariable("CLUSTER_DATASET");
		if( upsUrl != null && mapMarkersUrl != null && clusterDataset != null ) {
			ClusterPoints clusterPoints = new ClusterPoints(eventBus, mapMarkersUrl);
			clusterPoints.setMap(gMap);

			int clusterPointCount = pv.getIntegerVariable("CLUSTER_POINT_COUNT", -1);
			if( clusterPointCount > 0 ) {
				clusterPoints.setRequestedNoPoints(clusterPointCount);
			}

		    // comms
		    UxPostalService uxPostalService = new UxPostalService(upsUrl); 

			
			// TODO envelopeSection could/should be in pv
			LetterBox letterBox = uxPostalService.createLetterBox(clusterDataset);
			// clusterPoints should recieve content sent from the server to this named
			// letter box....
			letterBox.addRecipient(clusterPoints);
			// ... and it can send via this letter box
			clusterPoints.setLetterBoxClusterPoints(clusterDataset, letterBox);
			
			// for NodeInfo queries
			LetterBox letterBoxNodeInfo = uxPostalService.createLetterBox("node_info");
			letterBoxNodeInfo.addRecipient(clusterPoints);
			clusterPoints.setLetterBoxNodeInfo(letterBoxNodeInfo);
			
			String pointSliderDiv = pv.getStringVariable("CLUSTER_POINT_COUNT_SLIDER_DIV");
			if( pointSliderDiv != null ) {
				// add a slider widget
				RootPanel pDiv = RootPanel.get(pointSliderDiv);
				
				FlowPanel panel = new FlowPanel();
				
				Slider s = new Slider(9, "90%");
				panel.add(s);
				final HTML label = new HTML("");
				panel.add(label);
				
				pDiv.add(panel);

				s.addBarValueChangedHandler(new BarValueChangedHandler() {

					@Override
					public void onBarValueChanged(BarValueChangedEvent event) {
						int scale = event.getValue()+1;
						int requestedPoints = scale*scale*5;
						label.setHTML(""+requestedPoints);
						eventBus.fireEvent(new ClusterChangePointCountEvent(requestedPoints));
					}
				});

				int sliderPosition = (int) Math.sqrt(clusterPoints.getRequestedNoPoints()/5)-1;
				s.setValue(sliderPosition);

			}
		}


	}

	
    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
