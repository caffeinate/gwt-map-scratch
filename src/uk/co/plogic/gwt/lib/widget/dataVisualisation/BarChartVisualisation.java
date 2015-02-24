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
import com.googlecode.gwt.charts.client.options.VAxis;
import com.googlecode.gwt.charts.client.options.ViewWindow;


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

        int width = responsiveSizing.getWidth();
        int height = (int) (width*0.33);
        options.setWidth(width);
        options.setHeight(height);

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

        options.setLegend(Legend.create(LegendPosition.TOP));

        HAxis hAxis = HAxis.create();
        hAxis.setTextPosition(TextPosition.OUT);

        // TODO - push this up to the JSON config
        hAxis.setFormat("#'%'");
        hAxis.setMinValue(0.0);
        hAxis.setMaxValue(15.0);

//        if( ! averageValue.isNaN() ) {
//
//
//            hAxis.setMinValue(minValue);
//            hAxis.setMaxValue(maxValue*1.5);
//
//            logger.info("setting view window for "+overlayId+" to "+minValue+":"+maxValue);
////            ViewWindow viewWindow = ViewWindow.create(minValue, maxValue);
////            vAxis.setViewWindow(viewWindow);
////
////            if( vAxisLabel != null ) {
////                // TODO move this to be independent of averageValue
////                vAxis.setTitle(vAxisLabel);
////            }
////            options.setVAxis(vAxis);
//        }
        options.setHAxis(hAxis);

        //options.setColors("blue");
        //options.setColors("green");

        options.setColors("red", "gray");

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
            chart.draw(chartDataTable, createOptions());
        }
        return (Widget) null;
    }

//    public static native JavaScriptObject barChartSpecialOptions() /*-{
//        return { textStyle : {fontSize: 10} };
//    }-*/;

}
