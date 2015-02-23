package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;


public class PieChartVisualisation extends ChartVisualisation {

	PieChart pie;

	public PieChartVisualisation(HandlerManager eventBus, final Element e) {
	    super(eventBus, e, ChartPackage.CORECHART);
		setupEventHandling();
	}

    @Override
    protected Widget redraw() {
        if( chartDataTable == null)
            return (Widget) null;

        if( pie == null ) {
            //chart = new ColumnChart(dataTable, options);
            pie = new PieChart();
            pie.draw(chartDataTable, createOptions());
            return (Widget) pie;
        } else {
            pie.draw(chartDataTable, createOptions());
        }
        return null;
    }

    public PieChartOptions createOptions() {

        PieChartOptions options = PieChartOptions.create();

        int width = responsiveSizing.getWidth();
        int height = (int) (width*0.33);
        options.setWidth(width);
        options.setHeight(height);
        options.setLegend(Legend.create(LegendPosition.NONE));


	    //options.setWidth(responsiveSizing.getWidth());
	    //options.setHeight(responsiveSizing.getHeight());
	    //options.set3D(true);
	    //options.set("legend", pieChartSpecialOptions());
	    if( getTitle() != null )
	    	options.setTitle(getTitle());


	    options.setLegend(Legend.create(LegendPosition.TOP));

	    //ChartArea chartArea = ChartArea.create();
	    //chartArea.setHeight("100%");
	    //chartArea.setWidth("100%");
	    //options.setChartArea(chartArea);

	    //options.setLegend("labeled");
	    //options.set("legend.maxLines", 4.0);

	    return options;
	}

//    public static native JavaScriptObject pieChartSpecialOptions() /*-{
//		return {position: 'top', maxLines: 4};
//	}-*/;
    //return {position: 'top', textStyle: {color: 'blue', fontSize: 16}};

}