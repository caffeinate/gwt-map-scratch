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
        options.set("vAxis", barChartSpecialOptions());
        options.setLegend("top");
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

        String [] c = {"blue", "gray"};
        JsArrayString x = ArrayHelper.toJsArrayString(c);
        options.setColors(x);

        return options;
    }

    @Override
    protected Widget redraw() {
        if( chart == null ) {
            //chart = new ColumnChart(dataTable, options);
            chart = new ColumnChart(chartDataTable, createOptions());
            //chart.addSelectHandler(createSelectHandler(chart));
            chart.addOnMouseOverHandler(new OnMouseOverHandler() {
                @Override
                public void onMouseOverEvent(OnMouseOverEvent event) {
//                    logger.info("col:"+event.getColumn());
                    markerHightlight(event.getRow(), true);
                }
            });
            chart.addOnMouseOutHandler(new OnMouseOutHandler() {
                @Override
                public void onMouseOutEvent(OnMouseOutEvent event) {
                    markerHightlight(event.getRow(), false);
                }
            });
            //chart.addSelectHandler(createSelectHandler(chart));
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

                //Selection [] s = {Selection.createCellSelection(ld.rowId, 1)};
                Selection [] s = {Selection.createCellSelection(76, 1)};
                JsArray<Selection> selection = ArrayHelper.toJsArray(s);
                //logger.fine("row sel:"+selection.get(0).getRow());


                chart.setSelections(selection);
            }
        }


    }

//    private SelectHandler createSelectHandler(final ColumnChart chart) {
//        return new SelectHandler() {
//          @Override
//          public void onSelect(SelectEvent event) {
//            String message = "";
//
//            // May be multiple selections.
//            JsArray<Selection> selections = chart.getSelections();
//
//            for (int i = 0; i < selections.length(); i++) {
//              // add a new line for each selection
//              message += i == 0 ? "" : "\n";
//
//              Selection selection = selections.get(i);
//
//              if (selection.isCell()) {
//                // isCell() returns true if a cell has been selected.
//
//                // getRow() returns the row number of the selected cell.
//                int row = selection.getRow();
//                // getColumn() returns the column number of the selected cell.
//                int column = selection.getColumn();
//                message += "cell " + row + ":" + column + " selected";
//              } else if (selection.isRow()) {
//                // isRow() returns true if an entire row has been selected.
//
//                // getRow() returns the row number of the selected row.
//                int row = selection.getRow();
//                message += "row " + row + " selected";
//              } else {
//                // unreachable
//                message += "Pie chart selections should be either row selections or cell selections.";
//                message += "  Other visualizations support column selections as well.";
//              }
//            }
//
//            Window.alert(message);
//          }
//        };
//      }
    public static native JavaScriptObject barChartSpecialOptions() /*-{
        return { textStyle : {fontSize: 10} };
    }-*/;

}
