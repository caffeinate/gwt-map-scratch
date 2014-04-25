package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes.LegendKey;
import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasLegend;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.ui.ElementScrapper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class LegendVisualisation {
	
	protected HandlerManager eventBus;
	protected HashSet<String> overlayId = new HashSet<String>();
	protected String panelId; // element in DOM
	protected Panel panel;
	protected FlowPanel legendPanel;
	protected LegendAttributes legendAttributes;
	protected String legendTitle;
	protected Grid grid;
	// at present, colours need to be unique
	protected HashMap<String, Integer> indicatorLookup = new HashMap<String, Integer>();
	
	public LegendVisualisation(HandlerManager eventBus, final Element e) {

		this.eventBus = eventBus;

		ElementScrapper es = new ElementScrapper();
		String overlayIds = es.findOverlayId(e, "span", "overlay_id");
		
		if(overlayIds.contains(",")) {
			// comma separated
			for(String oId : overlayIds.split(","))
				overlayId.add(oId);
		} else
			overlayId.add(overlayIds);
		
		
		panelId = e.getId();
		e.removeClassName("hidden");
		panel = RootPanel.get(panelId);
		panel.setVisible(false);

		legendPanel = new FlowPanel();
		legendPanel.setVisible(false);

		panel.add(legendPanel);		


	    eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {

				AbstractOverlay overlay = e.getOverlay();
				String visualisationFor = overlay.getOverlayId();
				AbstractShapeMarker targetMarker;
				String markerId;
				if(overlayId.contains(visualisationFor) && overlay instanceof OverlayHasLegend ) {

					//System.out.println("using "+visualisationFor);
					
					OverlayHasLegend overlayLegend = (OverlayHasLegend) overlay;
					LegendAttributes la = overlayLegend.getLegendAttributes();
					if( la != legendAttributes ) {
						legendAttributes = la;
						legendTitle = overlayLegend.getLegendTitle();
						buildTable();
					}
					markerId = e.getMarkerId();
					if( la != null && markerId != null
						&& overlay instanceof OverlayHasMarkers
						&& (targetMarker = ((OverlayHasMarkers) overlay).getMarker(markerId)) != null ) {
						//System.out.println("legend is:"+targetMarker.getId());
						String targetColour = targetMarker.getFillColour();
						if( targetColour != null ) {
							String colourHex = targetColour.replace("#", "").toLowerCase();
							indicateColour(colourHex);
						}
					}
				}
			}
		});
	    
	    eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

	    	@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				String visualisationFor = e.getOverlayId();
				if(overlayId.contains(visualisationFor) )
					panel.setVisible(e.isVisible());
			}
	    });
	    
	}

	private void indicateColour(String colour) {

		// clear existing indicator
		for( int i : indicatorLookup.values())
			grid.getCellFormatter().removeStyleName(i+1, 1, "active");

		if(indicatorLookup.containsKey(colour))
			grid.getCellFormatter().addStyleName(indicatorLookup.get(colour)+1, 1, "active");
	}
	
	
	private void buildTable() {

		if(legendAttributes == null)
			return;

		legendPanel.clear();
		legendPanel.setVisible(true);
		
		final HashSet<String> overlaysFinal = new HashSet<String>();
		overlaysFinal.addAll(overlayId);

		int keyCount = legendAttributes.size();
		grid = new Grid(keyCount+1, 2);
		grid.setStyleName("table");
//		grid.addStyleName("table-bordered");
		
		
		HTML legendTitleHtml = new HTML(legendTitle);
		legendTitleHtml.setStyleName("legendTitle");
		grid.setWidget(0, 1, legendTitleHtml);
		
		ArrayList<LegendKey> keys = legendAttributes.getKeys();
		for(int i=0; i<keyCount; i++) {

			LegendKey key = keys.get(i);


			final String keyColour = key.colour.toLowerCase();
			MouseOverHandler legendEntryInteractionOver = new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {

					indicateColour(keyColour);
					
					for( String overlayX : overlaysFinal ) {
						//System.out.println("mouse over " + overlayX + " "+keyColour);
						eventBus.fireEvent(new MapMarkerHighlightByColourEvent(true, keyColour, overlayX));
					}					
				}
			};
			
			MouseOutHandler legendEntryInteractionOut = new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					for( String overlayX : overlaysFinal ) {
						//System.out.println("mouse out " + overlayX + " "+keyColour);
						eventBus.fireEvent(new MapMarkerHighlightByColourEvent(false, keyColour, overlayX));
					}
				}
			};
			
			
			HTML label = new HTML(key.label);
			label.setStyleName("legend_label");
			label.addMouseOverHandler(legendEntryInteractionOver);
			label.addMouseOutHandler(legendEntryInteractionOut);
			
			HTML colour = new HTML("&nbsp;");
			colour.setStyleName("legend_colour");
			colour.getElement() .setAttribute("style", "background-color:#"+key.colour);
			colour.addMouseOverHandler(legendEntryInteractionOver);
			colour.addMouseOutHandler(legendEntryInteractionOut);

			indicatorLookup.put(key.colour.toLowerCase(), i);

			grid.setWidget(i+1, 0, colour);
			grid.setWidget(i+1, 1, label);
			
		}


		legendPanel.add(grid);
		
	}
	
}
