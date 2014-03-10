package uk.co.plogic.gwt.lib.ui.dataVisualisation;

import uk.co.plogic.gwt.lib.map.markers.utils.AttributeDictionary;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;

public class PieChartVisualisation {
	
	boolean apiLoaded = false;

	public PieChartVisualisation() {

		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		    	  apiLoaded = true;
		      }
		};
	    // Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
	    VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
	}
	
	public void setData(AttributeDictionary d) {
        
		Panel panel = RootPanel.get("graph");
        panel.clear();
        
	    DataTable data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "");
	    data.addColumn(ColumnType.NUMBER, "Percent");

        for( String attribKey : d.keySet() ) {
        	System.out.println(attribKey);
        	if( d.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
        		// it's a pie segment
        		data.addRow();
        		int rowPos = data.getNumberOfRows()-1;
        		data.setValue(rowPos, 0, attribKey);
        		data.setValue(rowPos, 1, d.getDouble(attribKey));
        	}
        }
        
//	    DataTable data = DataTable.create();
//	    data.addColumn(ColumnType.STRING, "Task");
//	    data.addColumn(ColumnType.NUMBER, "Hours per Day");
//	    data.addRows(2);
//	    data.setValue(0, 0, "Work");
//	    data.setValue(0, 1, 14);
//	    data.setValue(1, 0, "Sleep");
//	    data.setValue(1, 1, 10);
        
	    Options options = Options.create();
	    options.setWidth(400);
	    options.setHeight(240);
	    options.set3D(true);
	    options.setTitle("My Daily Activities");
	    
        // Create a pie chart visualization.
        PieChart pie = new PieChart(data, options);

        //pie.addSelectHandler(createSelectHandler(pie));
        panel.add(pie);
	}

}
