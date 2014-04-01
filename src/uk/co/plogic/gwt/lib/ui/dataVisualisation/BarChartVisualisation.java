package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;


public class BarChartVisualisation extends ChartVisualisation {
	
	//ColumnChart chart;
	BarChart chart;

	public BarChartVisualisation(HandlerManager eventBus, final Element e) {

		//super(eventBus, e, ColumnChart.PACKAGE);
		super(eventBus, e, BarChart.PACKAGE);

		handleMarkerAttributeData();
		handleOverlayVisibilityChanges();

	}
	
	public void drawChart(DataTable dataTable) {
        
		if( ! panel.isVisible() )
			panel.setVisible(true);

        Options options = Options.create();
	    
	    //int pWidth = (int) (panel.getOffsetWidth()*0.95);
	    options.setWidth((int) (panel.getOffsetWidth()*0.95));
	    options.setHeight((int) (panel.getOffsetWidth()*1.2));
	    //options.set("hAxis.viewWindow.max", 100.0);
	    //options.set("hAxis", barChartSpecialOptions());
	    options.setLegend("top");
	    ChartArea chartArea = ChartArea.create();
	    chartArea.setHeight("85%");
	    chartArea.setWidth("65%");
	    //chartArea.setWidth(100);
	    options.setChartArea(chartArea);
	    
//	    AxisOptions axOpt = AxisOptions.create();
//	    axOpt.setTextPosition("out");
//	    options.setVAxisOptions(axOpt);

	    if( chart == null && apiLoaded ) {
	    	//chart = new ColumnChart(dataTable, options);
	    	chart = new BarChart(dataTable, options);
  	  		panel.add(chart);
	    }

	    if( chart != null )
	    	chart.draw(dataTable, options);


	}

	@Override
	public DataTable buildChartData(AttributeDictionary d) {
		DataTable dt;
		dt = DataTable.create();
		dt.addColumn(ColumnType.STRING, "");
		dt.addColumn(ColumnType.NUMBER, "Percent");


        for( String attribKey : d.keySet() ) {
        	//System.out.println(attribKey);
        	if( d.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
        		dt.addRow();
        		int rowPos = dt.getNumberOfRows()-1;
        		dt.setValue(rowPos, 0, attribKey);
        		dt.setValue(rowPos, 1, d.getDouble(attribKey));
        	}
        }
        return dt;
	}

    public static native JavaScriptObject barChartSpecialOptions() /*-{
		return {maxValue: 100, minValue: 0};
	}-*/;
}
