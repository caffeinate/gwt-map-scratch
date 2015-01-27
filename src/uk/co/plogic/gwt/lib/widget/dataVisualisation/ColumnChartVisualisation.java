package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.HorizontalAxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.maps.gwt.client.ArrayHelper;


public class ColumnChartVisualisation extends ChartVisualisation {

	ColumnChart chart;

	public ColumnChartVisualisation(HandlerManager eventBus, final Element e) {

		super(eventBus, e, ColumnChart.PACKAGE);
		setupEventHandling();
	}

    @Override
    public Options createOptions() {
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

        return options;
    }

    @Override
    protected Widget redraw() {
        if( chart == null ) {
            //chart = new ColumnChart(dataTable, options);
            chart = new ColumnChart(chartDataTable, createOptions());
            return (Widget) chart;
        } else {
            chart.draw(chartDataTable, createOptions());
        }
        return null;
    }

    public static native JavaScriptObject barChartSpecialOptions() /*-{
        return { textStyle : {fontSize: 10} };
    }-*/;

}
