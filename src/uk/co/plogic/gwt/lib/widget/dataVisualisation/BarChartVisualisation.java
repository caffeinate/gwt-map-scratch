package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.corechart.BarChart;
import com.googlecode.gwt.charts.client.corechart.BarChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.TextPosition;


public class BarChartVisualisation extends ChartVisualisation {

	//ColumnChart chart;
	BarChart chart;

	public BarChartVisualisation(HandlerManager eventBus, final Element e) {

		//super(eventBus, e, ColumnChart.PACKAGE);
	    super(eventBus, e, ChartPackage.CORECHART);
		setupEventHandling();
	}

    @Override
    public BarChartOptions createOptions() {
        BarChartOptions options = BarChartOptions.create();

        int pWidth = responsiveSizing.getWidth();
        options.setWidth(pWidth);
        options.setHeight(responsiveSizing.getHeight());
        //options.set("hAxis.viewWindow.max", 100.0);
        //options.set("vAxis", barChartSpecialOptions());
        options.setLegend(Legend.create(LegendPosition.NONE));
        //ChartArea chartArea = ChartArea.create();
        //chartArea.setLeft((int) (pWidth*0.33));
        //chartArea.setHeight("85%");
        //chartArea.setWidth("65%");
        //chartArea.setWidth(100);
//        options.setChartArea(chartArea);

//        HorizontalAxisOptions hOptions = HorizontalAxisOptions.create();
//        hOptions.setMinValue(0.0);
//        hOptions.setMaxValue(20.0);
//        hOptions.setShowTextEvery(1);
//        hOptions.set("format", "#'%'");
//        options.setHAxisOptions(hOptions);

        HAxis hAxis = HAxis.create();
        hAxis.setTextPosition(TextPosition.NONE);
        options.setHAxis(hAxis);

        //options.setColors("blue");
        //options.setColors("green");

        options.setColors("red");

//        String [] c = {"red", "gray"};
//        JsArrayString x = ArrayHelper.toJsArrayString(c);
//        options.setColors(x);

        return options;
    }

    @Override
    protected Widget redraw() {

        if( chartDataTable == null)
            return (Widget) null;

        if( chart == null ) {
            //chart = new ColumnChart(dataTable, options);
            chart = new BarChart();

            chart.draw(chartDataTable, createOptions());
            return (Widget) chart;
        } else {
            chart.redraw();
        }
        return (Widget) null;
    }

//    public static native JavaScriptObject barChartSpecialOptions() /*-{
//        return { textStyle : {fontSize: 10} };
//    }-*/;

}
