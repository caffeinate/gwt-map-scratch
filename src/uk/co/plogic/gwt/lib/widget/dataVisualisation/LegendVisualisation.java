package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEvent;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes.LegendKey;
import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasLegend;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;


public class LegendVisualisation extends Composite {

	final Logger logger = Logger.getLogger("LegendVisualisation");
	protected HandlerManager eventBus;
	protected HashSet<String> overlayId = new HashSet<String>();
	protected FlowPanel legendPanel;
	protected LegendAttributes legendAttributes;
	protected String legendTitle;
	protected Grid grid;
	// at present, colours need to be unique
	protected HashMap<String, Integer> indicatorLookup = new HashMap<String, Integer>();
	final String CSS_ACTIVE = "legend_active";

	public LegendVisualisation(HandlerManager eventBus, final Element e) {

		this.eventBus = eventBus;

		if( e.hasAttribute("data-overlay-ids") ) {
			String overlayIds = e.getAttribute("data-overlay-ids");
			logger.fine("have legend dom element for overlays ["+overlayIds+"]");
			if(overlayIds.contains(",")) {
				// comma separated
				for(String oId : overlayIds.split(","))
					overlayId.add(oId);
			}
		} else if( e.hasAttribute("data-overlay-id") ) {
		    String ovId = e.getAttribute("data-overlay-id");
			overlayId.add(ovId);
			logger.fine("have legend dom element for overlay ["+ovId+"]");
		} else {
			logger.warning("data-overlay-id attribute is missing");
		}

		legendPanel = new FlowPanel();
		legendPanel.setVisible(false);
		initWidget(legendPanel);

	    eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {

				AbstractOverlay overlay = e.getOverlay();
				String visualisationFor = overlay.getOverlayId();
				AbstractBaseMarker targetMarker;
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
						&& (targetMarker = ((OverlayHasMarkers) overlay).getMarker(markerId)) != null
						&& (targetMarker instanceof AbstractShapeMarker) ) {
						//System.out.println("legend is:"+targetMarker.getId());
						String targetColour = ((AbstractShapeMarker) targetMarker).getFillColour();
						if( targetColour != null ) {
							String colourHex = targetColour.replace("#", "").toLowerCase();
							indicateColour(colourHex);
						}
					}
				}
			}
		});

	}

	private void indicateColour(String colour) {

		// clear existing indicator
		for( int i : indicatorLookup.values())
			grid.getCellFormatter().removeStyleName(i+1, 1, CSS_ACTIVE);

		if(indicatorLookup.containsKey(colour))
			grid.getCellFormatter().addStyleName(indicatorLookup.get(colour)+1, 1, CSS_ACTIVE);
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
		grid.addStyleName("table-condensed");

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
