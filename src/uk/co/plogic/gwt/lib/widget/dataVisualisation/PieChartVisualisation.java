package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;


public class PieChartVisualisation extends ChartVisualisation {

	PieChart pie;

	public PieChartVisualisation(HandlerManager eventBus, final Element e) {
		super(eventBus, e, PieChart.PACKAGE);
		handleMarkerAttributeData();
	}
	
	public void drawChart(DataTable dataTable) {
        
		//piePanel.clear();
        
		if( ! panel.isVisible() )
			panel.setVisible(true);


        PieOptions options = PieOptions.create();
	    
	    options.setWidth(responsiveSizing.getWidth());
	    options.setHeight(responsiveSizing.getHeight());
	    options.set3D(true);
	    options.set("legend", pieChartSpecialOptions());
	    if( getTitle() != null )
	    	options.setTitle(getTitle());
	    //options.setLegend(LegendPosition.BOTTOM);
	    //ChartArea chartArea = ChartArea.create();
	    //chartArea.setHeight("100%");
	    //chartArea.setWidth("100%");
	    //options.setChartArea(chartArea);
	    
	    //options.setLegend("labeled");
	    //options.set("legend.maxLines", 4.0);


	    if( pie == null && apiLoaded ) {
  	  		pie = new PieChart(dataTable, options);
  	  		panel.add(pie);
	    }
	    
	    if( pie != null )
	    	pie.draw(dataTable, options);

	    //pie.addSelectHandler(createSelectHandler(pie));

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
        		// it's a pie segment
        		dt.addRow();
        		int rowPos = dt.getNumberOfRows()-1;
        		double columnValue = d.getDouble(attribKey);
        		dt.setValue(rowPos, 0, attribKey);
        		dt.setValue(rowPos, 1, columnValue);
        		//dt.setFormattedValue(rowPos, 1, numberFormat.format(columnValue)+"%");
        	}
        }
        return dt;
	}

    public static native JavaScriptObject pieChartSpecialOptions() /*-{
		return {position: 'top', maxLines: 4};
	}-*/;
    //return {position: 'top', textStyle: {color: 'blue', fontSize: 16}};

}