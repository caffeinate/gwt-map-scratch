package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;


public class PieChartVisualisation extends ChartVisualisation {

	PieChart pie;

	public PieChartVisualisation(HandlerManager eventBus, final Element e) {
		super(eventBus, e, PieChart.PACKAGE);
		setupEventHandling();
	}

    @Override
    protected Widget redraw() {
        if( pie == null ) {
            //chart = new ColumnChart(dataTable, options);
            pie = new PieChart(chartDataTable, createOptions());
            return (Widget) pie;
        } else {
            pie.draw(chartDataTable, createOptions());
        }
        return null;
    }

    public Options createOptions() {

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

	    return options;
	}

    public static native JavaScriptObject pieChartSpecialOptions() /*-{
		return {position: 'top', maxLines: 4};
	}-*/;
    //return {position: 'top', textStyle: {color: 'blue', fontSize: 16}};

}