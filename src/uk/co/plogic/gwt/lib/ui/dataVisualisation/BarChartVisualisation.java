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
	
	public void drawChart(DataTable dataTable) {
        
		if( ! panel.isVisible() )
			panel.setVisible(true);

        Options options = Options.create();
	    
	    int pWidth = (int) (panel.getOffsetWidth()*0.9);
	    options.setWidth(pWidth);
	    options.setHeight((int) (pWidth*1.1));
	    //options.set("hAxis.viewWindow.max", 100.0);
	    options.set("hAxis", barChartSpecialOptions());

	    if( chart == null && apiLoaded ) {
	    	chart = new BarChart(dataTable, options);
  	  		panel.add(chart);
	    }
	    
	    if( chart != null )
	    	chart.draw(dataTable, options);


	}
    public static native JavaScriptObject barChartSpecialOptions() /*-{
		return {maxValue: 100, minValue: 0};
	}-*/;

	@Override
	public DataTable buildChartData(AttributeDictionary d) {
		DataTable dt;
		dt = DataTable.create();
		dt.addColumn(ColumnType.STRING, "");
		dt.addColumn(ColumnType.NUMBER, "Percent");


        for( String attribKey : d.keySet() ) {
        	//System.out.println(attribKey);
        	if( d.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
        		// it's a pie segment
        		dt.addRow();
        		int rowPos = dt.getNumberOfRows()-1;
        		dt.setValue(rowPos, 0, attribKey);
        		dt.setValue(rowPos, 1, d.getDouble(attribKey));
        	}
        }
        return dt;
	}
}
