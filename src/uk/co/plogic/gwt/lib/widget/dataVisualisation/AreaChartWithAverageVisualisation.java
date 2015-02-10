package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import java.util.ArrayList;

import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEvent;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataColumn;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.RoleType;
import com.googlecode.gwt.charts.client.corechart.ComboChart;
import com.googlecode.gwt.charts.client.corechart.ComboChartOptions;
import com.googlecode.gwt.charts.client.corechart.ComboChartSeries;
import com.googlecode.gwt.charts.client.event.OnMouseOutEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOutHandler;
import com.googlecode.gwt.charts.client.event.OnMouseOverEvent;
import com.googlecode.gwt.charts.client.event.OnMouseOverHandler;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.SeriesType;
import com.googlecode.gwt.charts.client.options.TextPosition;
import com.googlecode.gwt.charts.client.options.VAxis;
import com.googlecode.gwt.charts.client.options.ViewWindow;


public class AreaChartWithAverageVisualisation extends ChartVisualisation {

    ComboChart chart;
	int currentlySelectedRow = -1;
	Double averageValue = Double.NaN;
	Double minValue = Double.NaN;
    Double maxValue = Double.NaN;

	public AreaChartWithAverageVisualisation(HandlerManager eventBus, final Element e) {

		super(eventBus, e, ChartPackage.CORECHART);
		setupEventHandling();
	}

    @Override
    public ComboChartOptions createOptions() {
        ComboChartOptions options = ComboChartOptions.create();

        options.setWidth(responsiveSizing.getWidth());
        options.setHeight(responsiveSizing.getHeight());
        //options.setWidth(400);
        //options.setHeight(400);
        options.setLegend(Legend.create(LegendPosition.NONE));
        options.setColors("FF0000", "568EBE", "00FF00");

        //Bar barOptions = Bar.create();
        //barOptions.setGroupWidth("100%");
        //options.setBar(barOptions);

        HAxis hAxis = HAxis.create();
        hAxis.setTextPosition(TextPosition.NONE);
        options.setHAxis(hAxis);

        if( ! averageValue.isNaN() ) {
            VAxis vAxis = VAxis.create();
            //vAxis.setBaseline(averageValue);
            //vAxis.setBaselineColor("red");
            //vAxis.setMinValue(minValue);
            //vAxis.setMaxValue(maxValue);

            logger.info("setting view window for "+overlayId+" to "+minValue+":"+maxValue);
            ViewWindow viewWindow = ViewWindow.create(minValue, maxValue);
            vAxis.setViewWindow(viewWindow);

            if( vAxisLabel != null ) {
                // TODO move this to be independent of averageValue
                vAxis.setTitle(vAxisLabel);
            }
            options.setVAxis(vAxis);
        }

        //options.setSeriesType(SeriesType.AREA);
        ComboChartSeries areaSeries = ComboChartSeries.create();
        areaSeries.setType(SeriesType.AREA);
        options.setSeries(1, areaSeries);

        ComboChartSeries lineSeries = ComboChartSeries.create();
        lineSeries.setType(SeriesType.LINE);
        options.setSeries(0, lineSeries);


        return options;
    }

    @Override
    protected Widget redraw() {

        if( chartDataTable == null)
            return (Widget) null;

        if( chart == null ) {

            chart = new ComboChart();
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
                    chartDataTable.setCell(currentlySelectedRow, 3, "");

                currentlySelectedRow = ld.rowId;
                String style = "color: #ff0000; stroke-width: 10; stroke-color: #ff0000; fill-color: #ff0000; fill-opacity: 1;";
                chartDataTable.setCell(ld.rowId, 3, style);
                redraw();
            }
        }


    }

    public void setChartData(String keyFieldName, String valueFieldName,
            ArrayList<MapLinkedData> lmd) {

        this.keyFieldName = keyFieldName;
        this.valueFieldName = valueFieldName;
        this.mapLinkedData = lmd;

        if( ! apiLoaded )
            // it will be loaded into chartDataTable later
            return;

        chartDataTable = DataTable.create();
        chartDataTable.addColumn(ColumnType.STRING, keyFieldName);
        chartDataTable.addColumn(ColumnType.NUMBER, valueFieldName);
        chartDataTable.addColumn(ColumnType.NUMBER, "Average");
        DataColumn style = DataColumn.create(ColumnType.STRING, RoleType.STYLE);
        chartDataTable.addColumn(style);

        double totalValue = 0;
        for( MapLinkedData ld : lmd ) {
            totalValue += ld.value;
            if(minValue.isNaN() || ld.value < minValue)
                minValue = ld.value;
            if(maxValue.isNaN() || ld.value > maxValue)
                maxValue = ld.value;
        }
        averageValue = totalValue / lmd.size();

        for( MapLinkedData ld : lmd ) {
            chartDataTable.addRow();
            int rowPos = chartDataTable.getNumberOfRows()-1;
            ld.rowId = rowPos;
            chartDataTable.setValue(rowPos, 0, ld.key);
            chartDataTable.setValue(rowPos, 2, ld.value);
            chartDataTable.setValue(rowPos, 1, averageValue);
            String formattedValue = numberFormat.format(ld.value)+"%";
            chartDataTable.setFormattedValue(rowPos, 2, formattedValue);
            String formattedValueAv = numberFormat.format(averageValue)+"%";
            chartDataTable.setFormattedValue(rowPos, 1, formattedValueAv);

        }
        drawChart();
    }

}
