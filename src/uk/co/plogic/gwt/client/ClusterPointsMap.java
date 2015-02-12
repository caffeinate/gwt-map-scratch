package uk.co.plogic.gwt.client;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.comms.UxPostalService;
import uk.co.plogic.gwt.lib.comms.UxPostalService.LetterBox;
import uk.co.plogic.gwt.lib.dom.DomElementByClassNameFinder;
import uk.co.plogic.gwt.lib.dom.DomParser;
import uk.co.plogic.gwt.lib.jso.PageVariables;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.overlay.ClusterPoints;
import uk.co.plogic.gwt.lib.ui.HideReveal;
import uk.co.plogic.gwt.lib.widget.ClusterPointsSlider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.maps.gwt.client.GoogleMap;


public class ClusterPointsMap implements EntryPoint {

    final Logger logger = Logger.getLogger("ClusterPointsMap");
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

		domParser.addHandler(new DomElementByClassNameFinder("cluster_points_slider") {
            @Override
            public void onDomElementFound(final Element element, String id) {
                final ClusterPointsSlider cps = new ClusterPointsSlider(eventBus, element);
                cps.setVisible(true);
                HTMLPanel h = HTMLPanel.wrap(element);
                h.add(cps);
            }
        });
		domParser.parseDom();


		GoogleMapAdapter gma = new GoogleMapAdapter(eventBus, pv.getStringVariable("DOM_MAP_DIV"));
	    gma.fitBounds(	pv.getDoubleVariable("LAT_A"), pv.getDoubleVariable("LNG_A"),
	    				pv.getDoubleVariable("LAT_B"), pv.getDoubleVariable("LNG_B"));

	    gMap = gma.create();


		String upsUrl = pv.getStringVariable("UPS_SERVICE");
		String mapMarkersUrl = pv.getStringVariable("MAP_MARKER_DYNAMIC_ICONS_URL");
		String holdingMarkerUrl = pv.getStringVariable("MAP_MARKER_ICON_PATH");
		String clusterDataset = pv.getStringVariable("CLUSTER_DATASET");
		if( upsUrl != null && mapMarkersUrl != null && clusterDataset != null ) {
		    logger.info("Creating cluster points connection for:"+clusterDataset);
			ClusterPoints clusterPoints = new ClusterPoints(eventBus, mapMarkersUrl, holdingMarkerUrl);
			clusterPoints.setMap(gma);
			clusterPoints.setOverlayId(clusterDataset);

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
			clusterPoints.show();
		}

	}


    private native PageVariables getPageVariables() /*-{
		return $wnd["config"];
	}-*/;
}
