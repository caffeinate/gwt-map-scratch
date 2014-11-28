package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizingAccepted;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

public abstract class ChartVisualisation extends Composite implements
													ResponsiveSizingAccepted {

	final Logger logger = Logger.getLogger("ChartVisualisation");
	protected FlowPanel panel;
	boolean apiLoaded = false;
	String overlayId;
	protected HandlerManager eventBus;
	protected NumberFormat numberFormat = NumberFormat.getFormat("#.0");
	protected NumberFormat numberFormat1Dp = NumberFormat.getFormat("#");
	protected String title; // title on graph - displayed to user
	protected ResponsiveSizing responsiveSizing;

	public ChartVisualisation(HandlerManager eventBus, final Element e, String chartPackage) {

		this.eventBus = eventBus;
		
		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		    	  apiLoaded = true;
		      }
		};
	    // Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
	    VisualizationUtils.loadVisualizationApi(onLoadCallback, chartPackage);
		
		if( e.hasAttribute("data-overlay-id") ) {
			overlayId = e.getAttribute("data-overlay-id");
		} else {
			logger.warning("data-overlay-id attribute is missing");
		}
		
		panel = new FlowPanel();
		initWidget(panel);
	}

	public void handleMarkerAttributeData() {
	    eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {
				String visualisationFor = e.getOverlay().getOverlayId(); 
				if(overlayId != null && overlayId.equals(visualisationFor)
				   && e.hasMarker() ) {

					OverlayHasMarkers overlay = (OverlayHasMarkers) e.getOverlay();
					AttributeDictionary d = overlay.getMarkerAttributes(e.getMarkerId());
					if( d != null ) {
						DataTable dt = buildChartData(d);
						drawChart(dt);
						//System.out.println(d.toString());
					}
				}
			}
		});
	}

	/**
	 * create chart instance if not already in place or update existing chart
	 * with new data
	 * @param d
	 */
	abstract public void drawChart(DataTable dt);

	/**
	 * @param d
	 * @return data suitable for the given chart type
	 */
	abstract public DataTable buildChartData(AttributeDictionary d);

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ResponsiveSizing getSizing() {
		return responsiveSizing;
	}

	public void setSizing(ResponsiveSizing r) {
		responsiveSizing = r;
	}
}
