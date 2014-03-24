package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;


public class BarChartVisualisation extends ChartVisualisation {
	
	BarChart chart;

	public BarChartVisualisation(HandlerManager eventBus, final Element e) {

		super(eventBus, e, BarChart.PACKAGE);

		handleMarkerAttributeData();
		handleOverlayVisibilityChanges();

	}
	
	public void setData(AttributeDictionary d) {
        
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

        Options options = Options.create();
	    
	    int pWidth = (int) (panel.getOffsetWidth()*0.9);
	    options.setWidth(pWidth);
	    options.setHeight((int) (pWidth*1.1));
	    //options.set("hAxis.viewWindow.max", 100.0);
	    options.set("hAxis", barChartSpecialOptions());

	    if( chart == null && apiLoaded ) {
	    	chart = new BarChart(data, options);
  	  		panel.add(chart);
	    }
	    
	    if( chart != null )
	    	chart.draw(data, options);


	}
    public static native JavaScriptObject barChartSpecialOptions() /*-{
		return {maxValue: 100, minValue: 0};
	}-*/;
}
