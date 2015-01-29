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
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.events.OnMouseOutHandler;
import com.google.gwt.visualization.client.events.OnMouseOverHandler;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.Selection;
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
        options.set("vAxis", barChartVAxisOptions());
        options.setLegend("top");

        //options.set("series", barChartSeriesOptions());
        options.set("bar", barChartBarOptions());

        ChartArea chartArea = ChartArea.create();
        //chartArea.setLeft((int) (pWidth*0.33));
        //chartArea.setHeight("85%");
        //chartArea.setWidth("65%");
        //chartArea.setWidth(100);
        options.setChartArea(chartArea);

        HorizontalAxisOptions hOptions = HorizontalAxisOptions.create();
//        hOptions.setMinValue(0.0);
//        hOptions.setMaxValue(20.0);
//        hOptions.setShowTextEvery(0);
        hOptions.setTextPosition("none");
//        hOptions.set("format", "#'%'");
        options.setHAxisOptions(hOptions);

        //options.setColors("blue");
        //options.setColors("green");

        String [] c = {"568EBE"};
        JsArrayString x = ArrayHelper.toJsArrayString(c);
        options.setColors(x);

        return options;
    }

    @Override
    protected Widget redraw() {
        if( chart == null ) {
            chart = new ColumnChart(chartDataTable, createOptions());
            chart.addOnMouseOverHandler(new OnMouseOverHandler() {
                @Override
                public void onMouseOverEvent(OnMouseOverEvent event) {
                    markerHightlight(event.getRow(), true);
                }
            });
            chart.addOnMouseOutHandler(new OnMouseOutHandler() {
                @Override
                public void onMouseOutEvent(OnMouseOutEvent event) {
                    markerHightlight(event.getRow(), false);
                }
            });
            return (Widget) chart;
        } else {
            chart.draw(chartDataTable, createOptions());
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

                Selection [] s = {Selection.createCellSelection(ld.rowId, 1)};
                //Selection [] s = {Selection.createCellSelection(76, 1)};
                JsArray<Selection> selection = ArrayHelper.toJsArray(s);
                //logger.fine("row sel:"+selection.get(0).getRow());


                chart.setSelections(selection);
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
