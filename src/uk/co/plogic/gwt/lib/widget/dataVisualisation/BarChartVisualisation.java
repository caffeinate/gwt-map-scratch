package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.HorizontalAxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.maps.gwt.client.ArrayHelper;


public class BarChartVisualisation extends ChartVisualisation {
	
	//ColumnChart chart;
	BarChart chart;

	public BarChartVisualisation(HandlerManager eventBus, final Element e) {

		//super(eventBus, e, ColumnChart.PACKAGE);
		super(eventBus, e, BarChart.PACKAGE);
		handleMarkerAttributeData();
	}
	
	public void drawChart(DataTable dataTable) {

		if( ! panel.isVisible() )
			panel.setVisible(true);

        Options options = Options.create();

	    int pWidth = responsiveSizing.getWidth();
	    options.setWidth(pWidth);
	    options.setHeight(responsiveSizing.getHeight());
	    //options.set("hAxis.viewWindow.max", 100.0);
	    options.set("vAxis", barChartSpecialOptions());
	    options.setLegend("top");
	    ChartArea chartArea = ChartArea.create();
	    chartArea.setLeft((int) (pWidth*0.33));
	    //chartArea.setHeight("85%");
	    //chartArea.setWidth("65%");
	    //chartArea.setWidth(100);
	    options.setChartArea(chartArea);
	    
	    HorizontalAxisOptions hOptions = HorizontalAxisOptions.create();
	    hOptions.setMinValue(0.0);
	    hOptions.setMaxValue(20.0);
	    hOptions.setShowTextEvery(1);
	    hOptions.set("format", "#'%'");
	    options.setHAxisOptions(hOptions);
	    
	    //options.setColors("blue");
	    //options.setColors("green");
	    
	    String [] c = {"red", "gray"};
	    JsArrayString x = ArrayHelper.toJsArrayString(c);
	    options.setColors(x);
	    
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
		return { textStyle : {fontSize: 10} };
	}-*/;

}
