package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.ui.ElementScrapper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

public abstract class ChartVisualisation {

	boolean apiLoaded = false;
	String overlayId;
	String panelId; // ID of element in DOM
	protected Panel panel;
	HandlerManager eventBus;
	protected NumberFormat numberFormat = NumberFormat.getFormat("#.00");
	
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
		
		ElementScrapper es = new ElementScrapper();
		overlayId = es.findOverlayId(e, "span", "overlay_id");
		panelId = e.getId();
		e.removeClassName("hidden");
		panel = RootPanel.get(panelId);
		panel.setVisible(false);

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
	
	public void handleOverlayVisibilityChanges() {
	    eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				
				if(overlayId != null && overlayId.equals(e.getOverlayId()) ) {
					panel.setVisible(e.isVisible());
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
	
}
