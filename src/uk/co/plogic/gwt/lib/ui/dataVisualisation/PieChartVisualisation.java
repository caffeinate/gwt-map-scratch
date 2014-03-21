package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;
import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.ui.ElementScrapper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;


public class PieChartVisualisation {
	
	String overlayId;
	String panelId; // element in DOM
	Panel panel;
	boolean apiLoaded = false;
	PieChart pie;

	public PieChartVisualisation(HandlerManager eventBus, final Element e) {

		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		    	  apiLoaded = true;
		      }
		};
	    // Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
	    VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
	    
		ElementScrapper es = new ElementScrapper();
		overlayId = es.findOverlayId(e, "span", "overlay_id");
		panelId = e.getId();
		e.removeClassName("hidden");
		panel = RootPanel.get(panelId);
		panel.setVisible(false);

	    eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {
				String visualisationFor = e.getOverlay().getOverlayId(); 
				if(overlayId != null && overlayId.equals(visualisationFor)
				   && e.hasMarker() ) {

					OverlayHasMarkers overlay = (OverlayHasMarkers) e.getOverlay();
					AttributeDictionary d = overlay.getMarkerAttributes(e.getMarkerId());
					if( d != null )
						setData(d);
				}
			}
			
		});
	    
	    eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {
				
				if(overlayId != null && overlayId.equals(e.getOverlayId()) ) {
					panel.setVisible(e.isVisible());
				}
			}
		});
	    
	    
	    
	}
	
	public void setData(AttributeDictionary d) {
        
		//piePanel.clear();
        
		if( ! panel.isVisible() )
			panel.setVisible(true);

		DataTable data;
		data = DataTable.create();
		data.addColumn(ColumnType.STRING, "");
		data.addColumn(ColumnType.NUMBER, "Percent");


        for( String attribKey : d.keySet() ) {
        	//System.out.println(attribKey);
        	if( d.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
        		// it's a pie segment
        		data.addRow();
        		int rowPos = data.getNumberOfRows()-1;
        		data.setValue(rowPos, 0, attribKey);
        		data.setValue(rowPos, 1, d.getDouble(attribKey));
        	}
        }

        PieOptions options = PieOptions.create();
	    
	    int pWidth = (int) (panel.getOffsetWidth()*0.9);
	    options.setWidth(pWidth);
	    options.setHeight((int) (pWidth*1.1));
	    options.set3D(true);
	    options.set("legend", pieChartSpecialOptions());
	    //options.setLegend(LegendPosition.BOTTOM);
	    //ChartArea chartArea = ChartArea.create();
	    //chartArea.setHeight("100%");
	    //chartArea.setWidth("100%");
	    //options.setChartArea(chartArea);
	    
	    //options.setLegend("labeled");
	    //options.set("legend.maxLines", 4.0);


	    if( pie == null && apiLoaded ) {
  	  		pie = new PieChart(data, options);
  	  		panel.add(pie);
	    }
	    
	    if( pie != null )
	    	pie.draw(data, options);

	    //pie.addSelectHandler(createSelectHandler(pie));

	}
    public static native JavaScriptObject pieChartSpecialOptions() /*-{

    	return {position: 'top', maxLines: 4};
	}-*/;
//return {position: 'top', textStyle: {color: 'blue', fontSize: 16}};
    
}
