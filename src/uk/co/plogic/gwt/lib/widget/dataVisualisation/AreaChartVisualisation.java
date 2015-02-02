package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEvent;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.googlecode.gwt.charts.client.corechart.AreaChartOptions;
import com.googlecode.gwt.charts.client.event.OnMouseOutEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOutHandler;
import com.googlecode.gwt.charts.client.event.OnMouseOverEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOverHandler;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.TextPosition;


public class AreaChartVisualisation extends ChartVisualisation {

	AreaChart chart;
	int currentlySelectedRow = -1;

	public AreaChartVisualisation(HandlerManager eventBus, final Element e) {

		super(eventBus, e, ChartPackage.CORECHART);
		setupEventHandling();
	}

    @Override
    public AreaChartOptions createOptions() {
    	AreaChartOptions options = AreaChartOptions.create();

        options.setWidth(responsiveSizing.getWidth());
        options.setHeight(responsiveSizing.getHeight());
        //options.setWidth(400);
        //options.setHeight(400);
        options.setLegend(Legend.create(LegendPosition.NONE));
        options.setColors("568EBE");

        //Bar barOptions = Bar.create();
        //barOptions.setGroupWidth("100%");
        //options.setBar(barOptions);

        HAxis hAxis = HAxis.create();
        hAxis.setTextPosition(TextPosition.NONE);
        options.setHAxis(hAxis);

        return options;
    }

    @Override
    protected Widget redraw() {

        if( chartDataTable == null)
            return (Widget) null;

        if( chart == null ) {
            chart = new AreaChart();
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
        return (Widget) null;
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

                if( currentlySelectedRow > -1 )
                    chartDataTable.setCell(currentlySelectedRow, 2, "");

                currentlySelectedRow = ld.rowId;
                String style = "color: #ff0000; stroke-width: 10; stroke-color: #ff0000; fill-color: #ff0000; fill-opacity: 1;";
                chartDataTable.setCell(ld.rowId, 2, style);
                redraw();
            }
        }


    }

}
