package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import uk.co.plogic.gwt.lib.events.ClusterChangePointCountEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes.LegendKey;
import uk.co.plogic.gwt.lib.map.overlay.AbstractOverlay;
import uk.co.plogic.gwt.lib.map.overlay.Shapes;
import uk.co.plogic.gwt.lib.ui.ElementScrapper;
import uk.co.plogic.gwt.lib.widget.Slider;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;


public class LegendTransparencyCombinedVisualisation {
	
	protected HashSet<String> overlayId = new HashSet<String>();
	protected String panelId; // element in DOM
	protected Panel panel;
	protected FlowPanel legendPanel;
	protected FlowPanel sliderPanel;
	protected LegendAttributes legendAttributes;
	// at present, colours need to be unique
	protected HashMap<String, HTML> indicatorLookup = new HashMap<String, HTML>();
	
	public LegendTransparencyCombinedVisualisation(HandlerManager eventBus, final Element e) {

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
		
		sliderPanel = new FlowPanel();
		
		Slider slider = new Slider(20, "90%");
		
		slider.addBarValueChangedHandler(new BarValueChangedHandler() {

			@Override
			public void onBarValueChanged(BarValueChangedEvent event) {
				System.out.println(""+event.getValue());
//				int scale = event.getValue()+1;
//				int requestedPoints = scale*scale*5;
//				label.setHTML(""+requestedPoints);
//				eventBus.fireEvent(new ClusterChangePointCountEvent(requestedPoints));
			}
		});

		//int sliderPosition = (int) Math.sqrt(clusterPoints.getRequestedNoPoints()/5)-1;
		//s.setValue(sliderPosition);
		
		sliderPanel.add(slider);
		panel.add(legendPanel);		
		panel.add(sliderPanel);

	    eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {

				String visualisationFor = e.getOverlay().getOverlayId();
				if(overlayId.contains(visualisationFor) ) {

					AbstractOverlay overlay = e.getOverlay();
					LegendAttributes la = overlay.getLegendAttributes();
					if( la != legendAttributes ) {
						legendAttributes = la;
						buildTable();
					}
					
					if( overlay instanceof Shapes && la != null ) {
						Shapes shapeOverlay = (Shapes) overlay;
						AbstractShapeMarker targetMarker = shapeOverlay.getMarker(e.getMarkerId());
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
				if(overlayId.contains(visualisationFor) ) {
					panel.setVisible(e.isVisible());
					sliderPanel.setVisible(e.isVisible());
				}
			}
	    });
	    
	}

	private void indicateColour(String colour) {

		// clear existing indicator
		for( HTML i : indicatorLookup.values())
			i.getElement().setAttribute("style", "background-color:#ffffff");

		if(indicatorLookup.containsKey(colour))
			indicatorLookup.get(colour).getElement().setAttribute("style", "background-color:#000000");
	}
	
	
	private void buildTable() {

		if(legendAttributes == null)
			return;

		legendPanel.clear();
		legendPanel.setVisible(true);

		int keyCount = legendAttributes.size();
		Grid grid = new Grid(keyCount, 3);
		grid.setStyleName("legend");

		ArrayList<LegendKey> keys = legendAttributes.getKeys();
		for(int i=0; i<keyCount; i++) {
			LegendKey key = keys.get(i);
			HTML label = new HTML(key.label);
			label.setStyleName("legend_label");
			HTML colour = new HTML("&nbsp;");
			colour.setStyleName("legend_colour");
			colour.getElement() .setAttribute("style", "background-color:#"+key.colour);

			HTML indicator = new HTML("&nbsp;");
			indicator.setStyleName("legend_colour");
			indicator.getElement().setAttribute("style", "background-color:#000000");
			
			indicatorLookup.put(key.colour.toLowerCase(), indicator);
			
			grid.setWidget(i, 0, indicator);
			grid.setWidget(i, 1, colour);
			grid.setWidget(i, 2, label);
		}

		legendPanel.add(grid);
		
	}
	
	
//	public void setData(AttributeDictionary d) {
//        
//        panel.clear();
//        panel.setVisible(true);
//
//	    DataTable data = DataTable.create();
//	    data.addColumn(ColumnType.STRING, "");
//	    data.addColumn(ColumnType.NUMBER, "Percent");
//
//        for( String attribKey : d.keySet() ) {
//        	//System.out.println(attribKey);
//        	if( d.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
//        		// it's a pie segment
//        		data.addRow();
//        		int rowPos = data.getNumberOfRows()-1;
//        		data.setValue(rowPos, 0, attribKey);
//        		data.setValue(rowPos, 1, d.getDouble(attribKey));
//        	}
//        }
//        
//	    Options options = Options.create();
//	    options.setWidth(250);
//	    options.setHeight(250);
//	    options.set3D(true);
//	    //options.setTitle("My Daily Activities");
//	    options.setLegend(LegendPosition.NONE);
//	    
//        // Create a pie chart visualization.
//        pie = new PieChart(data, options);
//
//        //pie.addSelectHandler(createSelectHandler(pie));
//        panel.add(pie);
//	}

}
