package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.ui.dataVisualisation.MapLinkVizControlPoint;
import uk.co.plogic.gwt.lib.ui.dataVisualisation.MapLinkVizControlPoint.VizDataSeries;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizingAccepted;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataColumn;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.RoleType;
import com.googlecode.gwt.charts.client.options.Options;

public abstract class ChartVisualisation extends Composite implements
                                        RequiresResize, ResponsiveSizingAccepted {

	final Logger logger = Logger.getLogger("ChartVisualisation");
	protected FlowPanel panel;
	boolean apiLoaded = false;
	String overlayId;
	protected HandlerManager eventBus;
	protected NumberFormat numberFormat = NumberFormat.getFormat("#.#");
	protected NumberFormat numberFormat1Dp = NumberFormat.getFormat("#");
	protected String title; // title on graph - displayed to user
	protected String vAxisLabel; // on graph - displayed to user
	protected ResponsiveSizing responsiveSizing;
	protected DataTable chartDataTable;
	protected ArrayList<String> fieldOrder;
	protected Double averageValue = Double.NaN;
	protected Double minValue = Double.NaN;
	protected Double maxValue = Double.NaN;


	protected MapLinkVizControlPoint mapLinkVizControlPoint;
	protected String keyFieldName;
	protected String valueFieldName;
    // Two modes to set data
	// 1.
	protected HashMap<Integer, SeriesData> rawData =
	                                new HashMap<Integer, SeriesData>();
	// 2.
	protected ArrayList<MapLinkedData> mapLinkedData;


	public static final String HOLDING_PANEL_CLASS = "chart_panel";

	class SeriesData {
	    String seriesName;
	    AttributeDictionary dict;
	}

	public ChartVisualisation(HandlerManager eventBus, final Element e, ChartPackage corechart) {

		this.eventBus = eventBus;

	    //ChartLoader chartLoader = new ChartLoader(corechart);
	    // Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
	    //chartLoader.loadApi(new Runnable() {

		//});

		if( e.hasAttribute("data-overlay-id") ) {
			overlayId = e.getAttribute("data-overlay-id");
			logger.fine("chart using data-overlay-id="+overlayId);
		} else {
			logger.warning("data-overlay-id attribute is missing");
		}

		panel = new FlowPanel();
		panel.setStyleName(HOLDING_PANEL_CLASS);
		initWidget(panel);
	}

    public void onApiLoaded() {
        apiLoaded = true;
        logger.info("finished loading API for "+overlayId);
        if( chartDataTable == null ) {
            if( rawData.size() > 0 ) {
                for(int seriesIndex : rawData.keySet()) {
                    setChartData(seriesIndex, rawData.get(seriesIndex));
                }
            }
            else if( mapLinkedData != null )
                setChartData(keyFieldName, valueFieldName, mapLinkedData);
        }

        drawChart();
    }

    public void setMapLinkVizControlPoint(MapLinkVizControlPoint m) {
        mapLinkVizControlPoint = m;

        if( mapLinkVizControlPoint.valueFields != null && fieldOrder == null ) {
        	// if not already set, set the field order in the dataTable
        	// should this be a copy?
        	fieldOrder = mapLinkVizControlPoint.valueFields;
        }

        int seriesIndex=0;
        for( VizDataSeries vds : mapLinkVizControlPoint.seriesData ) {
            SeriesData sd = new SeriesData();
            sd.seriesName = vds.valueLabel;
            sd.dict = vds.dataDict;
            setChartData(seriesIndex, sd);
            seriesIndex++;
        }
    }

	protected void setupEventHandling() {
	    eventBus.addHandler(DataVisualisationEvent.TYPE, new DataVisualisationEventHandler() {

			@Override
			public void onDataAvailableEvent(DataVisualisationEvent e) {
				String visualisationFor = e.getOverlay().getOverlayId();
				if(overlayId != null && overlayId.equals(visualisationFor)
				   && e.hasMarker() ) {

					OverlayHasMarkers overlay = (OverlayHasMarkers) e.getOverlay();
					AttributeDictionary d = overlay.getMarkerAttributes(e.getMarkerId());
					onMarkerDataVisualisation(e.getMarkerId(), d);

				}
			}
		});
	}

	/**
	 * for example - fired with mouse over on map or other events that would like
	 * a data visualisation.
	 */
	protected void onMarkerDataVisualisation(String markerId,
	                                         AttributeDictionary markerAttributes) {

	    // marker attributes could be used to create a visualisation
        if( markerAttributes != null && mapLinkVizControlPoint != null) {
            final int targetChartSeries = 0;
            String valueLabel = "";
            if(mapLinkVizControlPoint.seriesData != null ) {
                valueLabel = mapLinkVizControlPoint.seriesData.get(targetChartSeries).valueLabel;
                if(valueLabel != null)
                    valueLabel = StringUtils.renderHtml(valueLabel, markerAttributes);
            }

            //rawData.put(targetChartSeries, markerAttributes);
            SeriesData sd = rawData.get(targetChartSeries);
            sd.dict = markerAttributes;
            sd.seriesName = valueLabel;
            setChartData(targetChartSeries, sd);
            logger.info("marker viz for:"+markerId+" "+valueLabel);
        }

	}

	/**
	 * create chart instance if not already in place or update existing chart
	 * with new data
	 */
	public void drawChart() {

	    logger.info("starting drawchart for "+overlayId);

        if( ! apiLoaded || chartDataTable == null) {
            logger.info("drawchart: returning, no data or api for "+overlayId);
            return;
        }

        if( ! panel.isVisible() ) {
            panel.setVisible(true);
            logger.info("drawchart: setting visible for "+overlayId);
        }

        Widget w = redraw();
        if( w != null ) {
            panel.add(w);
            logger.info("drawchart: adding to panel for "+overlayId);
        }
    }

	/**
	 * Only return a chart if the holding panel needs to be updated.
	 * @return
	 */
	abstract protected Widget redraw();

    public void onResize() {
        if( apiLoaded ) {
            logger.finer("Chart has recieved an onReSize()");
            redraw();
        }
    }

    /**
     * provide data for the graph. Graph will automatically re-draw
     * if already visible.
     *
     * @param keyFieldName
     * @param valueFieldName
     * @param d                 - a single dictionary
     */
	public void setChartData(int seriesIndex, SeriesData d) {

	    String keyLabel = "";

        rawData.put(seriesIndex, d);
        if( ! apiLoaded )
            // it will be loaded into chartDataTable later
            return;


        chartDataTable = DataTable.create();
        chartDataTable.addColumn(ColumnType.STRING, keyLabel);

        // TODO - don't assume all series data has been set. So do a sort
        for(int seriesIdx=0; seriesIdx<rawData.size(); seriesIdx++) {
            chartDataTable.addColumn(ColumnType.NUMBER,
                                     rawData.get(seriesIdx).seriesName);
            logger.fine("adding column:"+rawData.get(seriesIdx).seriesName);
        }


        // TODO - multi-series data is too subtle. At present you just give
        // series data with series_index > 0 to instantiate multi series data
//        if( mapLinkVizControlPoint.seriesData == null ) {
//            chartDataTable.addColumn(ColumnType.NUMBER, valueLabel);
//        } else {
//            for( VizDataSeries dataSeries : mapLinkVizControlPoint.seriesData ) {
//                chartDataTable.addColumn(ColumnType.NUMBER, dataSeries.valueLabel);
//            }
//        }

        DataColumn style = DataColumn.create(ColumnType.STRING, RoleType.STYLE);
        chartDataTable.addColumn(style);


        for(int seriesIdx=0; seriesIdx<rawData.size(); seriesIdx++) {

            int columnIndex = seriesIdx+1;

            AttributeDictionary seriesData = rawData.get(seriesIdx).dict;
            if( seriesData == null ) {
                logger.info("data not found for series:"+seriesIdx);
                continue;
            }

            // clear any existing values just in case a row is missing from
            // new data
            for(int r=0; r<chartDataTable.getNumberOfRows(); r++)
                chartDataTable.setValueNull(r, columnIndex);

            for( String attribKey : seriesData.keySet() ) {

                // filter fields to just those listed in JSO?
                if( fieldOrder != null && ! fieldOrder.contains(attribKey))
                    continue;

                // maintain order of rows
                int rowPos = -1;
                if( fieldOrder != null ) {
                    rowPos = fieldOrder.indexOf(attribKey);
                }

                if( rowPos == -1 ) {
                    rowPos = chartDataTable.getNumberOfRows();
                }

                // auto-enlarge data table
                while(chartDataTable.getNumberOfRows() < rowPos+1)
                    chartDataTable.addRow();

                // only add if it's a number - I don't think AttributeDictionary
                // handles number types other then double
                if( seriesData.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
                    chartDataTable.setValue(rowPos, 0, attribKey);
                    chartDataTable.setValue(rowPos, columnIndex, seriesData.getDouble(attribKey));
                    String formattedValue = numberFormat.format(seriesData.getDouble(attribKey))+"%";
                    chartDataTable.setFormattedValue(rowPos, columnIndex, formattedValue);
                }
            }
        }
        drawChart();
    }

	/**
	 *
	 *
	 *
	 * @param keyFieldName
	 * @param valueFieldName
	 * @param lmd              - like a list of tuples
	 */
	public void setChartData(String keyFieldName, String valueFieldName,
	                         ArrayList<MapLinkedData> lmd) {


	    // TODO this MapLinkedData version of setChartData can't handle
	    // multiple series data. Solution is to create an internal
	    // structure to use in the absence of DataTable (i.e. during API load)
	    // which supports all the features on MapLinkedData and
	    // AttributeDictionary combined.

	    this.keyFieldName = keyFieldName;
	    this.valueFieldName = valueFieldName;
	    this.mapLinkedData = lmd;
	    if( ! apiLoaded )
            // it will be loaded into chartDataTable later
            return;

	    chartDataTable = DataTable.create();
        chartDataTable.addColumn(ColumnType.STRING, keyFieldName);
        chartDataTable.addColumn(ColumnType.NUMBER, valueFieldName);
        DataColumn style = DataColumn.create(ColumnType.STRING, RoleType.STYLE);
        chartDataTable.addColumn(style);

        double totalValue = 0;
        for( MapLinkedData ld : lmd ) {
            chartDataTable.addRow();
            int rowPos = chartDataTable.getNumberOfRows()-1;
            ld.rowId = rowPos;
            chartDataTable.setValue(rowPos, 0, ld.key);
            chartDataTable.setValue(rowPos, 1, ld.value);
            String formattedValue = numberFormat.format(ld.value)+"%";
            chartDataTable.setFormattedValue(rowPos, 1, formattedValue);

            totalValue += ld.value;
            if(minValue.isNaN() || ld.value < minValue)
                minValue = ld.value;
            if(maxValue.isNaN() || ld.value > maxValue)
                maxValue = ld.value;
        }
        averageValue = totalValue / lmd.size();
        if( maxValue > 98 && maxValue < 101 )
            maxValue = 100.0;

        drawChart();
	}

	abstract public Options createOptions();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ResponsiveSizing getSizing() {
		return responsiveSizing;
	}

	public void setSizing(ResponsiveSizing r) {
		responsiveSizing = r;
	}

    public void setVAxisLabel(String vAxisLabel) {
        this.vAxisLabel = vAxisLabel;
    }

}
