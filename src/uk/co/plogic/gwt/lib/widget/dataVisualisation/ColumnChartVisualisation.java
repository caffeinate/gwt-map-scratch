package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEvent;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.ArrayHelper;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.corechart.ColumnChart;
import com.googlecode.gwt.charts.client.corechart.ColumnChartOptions;
import com.googlecode.gwt.charts.client.event.OnMouseOutEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOutHandler;
import com.googlecode.gwt.charts.client.event.OnMouseOverEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOverHandler;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;


public class ColumnChartVisualisation extends ChartVisualisation {

	ColumnChart chart;

	public ColumnChartVisualisation(HandlerManager eventBus, final Element e) {

		super(eventBus, e, ChartPackage.CORECHART);
		setupEventHandling();
	}

    @Override
    public ColumnChartOptions createOptions() {
    	ColumnChartOptions options = ColumnChartOptions.create();

        //options.setWidth(responsiveSizing.getWidth());
        //options.setHeight(responsiveSizing.getHeight());
        //options.set("hAxis.viewWindow.max", 100.0);
        //options.set("vAxis", barChartVAxisOptions());
        options.setLegend(Legend.create(LegendPosition.NONE));

        //options.set("series", barChartSeriesOptions());
        //options.set("bar", barChartBarOptions());

        //ChartArea chartArea = ChartArea.create();
        //chartArea.setLeft((int) (pWidth*0.33));
        //chartArea.setHeight("85%");
        //chartArea.setWidth("65%");
        options.setWidth(400);
        options.setHeight(400);
        //options.setChartArea(chartArea);

        //HorizontalAxisOptions hOptions = HorizontalAxisOptions.create();
//        hOptions.setMinValue(0.0);
//        hOptions.setMaxValue(20.0);
//        hOptions.setShowTextEvery(0);
        //hOptions.setTextPosition("none");
//        hOptions.set("format", "#'%'");
        //options.setHAxisOptions(hOptions);

        //options.setColors("blue");
        //options.setColors("green");

//        String [] c = {"568EBE"};
//        JsArrayString x = ArrayHelper.toJsArrayString(c);
//        options.setColors(x);
        

        return options;
    }

    @Override
    protected Widget redraw() {
        if( chart == null ) {
            chart = new ColumnChart();
            chart.addOnMouseOverHandler(new OnMouseOverHandler() {
                @Override
                public void onMouseOver(OnMouseOverEvent event) {
                    markerHightlight(event.getRow(), true);
                }
            });
            chart.addOnMouseOutHandler(new OnMouseOutHandler() {
                @Override
                public void onMouseOutEvent(OnMouseOutEvent event) {
                    markerHightlight(event.getRow(), false);
                }
            });
            chart.draw(chartDataTable, createOptions());
            return (Widget) chart;
        } else {
            chart.redraw();
        }
        return null;
    }

    private void markerHightlight(int selectedRow, boolean highlight) {
        if( mapLinkedData == null )
            return;

        for( MapLinkedData ld : mapLinkedData ) {
            if( ld.rowId == selectedRow ) {
                eventBus.fireEvent(new MapMarkerHighlightByIdEvent(highlight, overlayId, ld.featureId));
            }
        }
    }

    protected void onMarkerDataVisualisation(String markerId, AttributeDictionary ma) {


        for( MapLinkedData ld : mapLinkedData ) {
            if( ld.featureId.equals(markerId) ) {

                logger.fine("marker viz for:"+markerId+" found row:"+ld.rowId);

                //Selection [] s = {Selection.createCellSelection(ld.rowId, 1)};
                //Selection [] s = {Selection.createCellSelection(76, 1)};
                //JsArray<Selection> selection = ArrayHelper.toJsArray(s);
                //logger.fine("row sel:"+selection.get(0).getRow());


                //chart.setSelections(selection);
            }
        }


    }

    public static native JavaScriptObject barChartVAxisOptions() /*-{
        return { textStyle : {fontSize: 10} };
    }-*/;
    public static native JavaScriptObject barChartBarOptions() /*-{
        return { groupWidth : "100%" };
    }-*/;
    public static native JavaScriptObject barChartSeriesOptions() /*-{
    return {
            0: {
                // set the color to change to
                color: 'FF0000',
                // don't show this in the legend
                visibleInLegend: true
            }
         };
    }-*/;
}
