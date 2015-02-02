package uk.co.plogic.gwt.lib.widget.dataVisualisation;

import java.util.ArrayList;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.events.DataVisualisationEventHandler;
import uk.co.plogic.gwt.lib.map.overlay.OverlayHasMarkers;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizing;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveSizingAccepted;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.options.Options;

public abstract class ChartVisualisation extends Composite implements
                                        RequiresResize, ResponsiveSizingAccepted {

	final Logger logger = Logger.getLogger("ChartVisualisation");
	protected FlowPanel panel;
	boolean apiLoaded = false;
	String overlayId;
	protected HandlerManager eventBus;
	protected NumberFormat numberFormat = NumberFormat.getFormat("#.0");
	protected NumberFormat numberFormat1Dp = NumberFormat.getFormat("#");
	protected String title; // title on graph - displayed to user
	protected ResponsiveSizing responsiveSizing;
	protected DataTable chartDataTable;

	// Two modes to set data
	// 1.
	protected AttributeDictionary rawData;
	// 2.
	protected String keyFieldName;
	protected String valueFieldName;
	protected ArrayList<MapLinkedData> mapLinkedData;


	public ChartVisualisation(HandlerManager eventBus, final Element e, ChartPackage corechart) {

		this.eventBus = eventBus;

	    ChartLoader chartLoader = new ChartLoader(corechart);
	    // Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
	    chartLoader.loadApi(new Runnable() {
		      public void run() {
		    	  apiLoaded = true;

		    	  if( chartDataTable == null ) {
		    	      if( rawData != null )
		    	          setChartData(rawData);
		    	      else if( mapLinkedData != null )
		    	          setChartData(keyFieldName, valueFieldName, mapLinkedData);
		    	  }

		    	  drawChart();
		      }
		});

		if( e.hasAttribute("data-overlay-id") ) {
			overlayId = e.getAttribute("data-overlay-id");
			logger.fine("chart using data-overlay-id="+overlayId);
		} else {
			logger.warning("data-overlay-id attribute is missing");
		}

		panel = new FlowPanel();
		initWidget(panel);
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



	    // default behaviour is to use all marker attributes
        if( markerAttributes != null ) {
            setChartData(markerAttributes);
            logger.info("marker viz for:"+markerId);
        }

	}

	/**
	 * create chart instance if not already in place or update existing chart
	 * with new data
	 */
	public void drawChart() {

        if( ! apiLoaded || chartDataTable == null)
            return;

        if( ! panel.isVisible() )
            panel.setVisible(true);

        Widget w = redraw();
        if( w != null )
            panel.add(w);
    }

	/**
	 * Only return a chart if the holding panel needs to be updated.
	 * @return
	 */
	abstract protected Widget redraw();

    public void onResize() {
        if( apiLoaded )
            redraw();
    }

	/**
	 * provide data for the graph. Graph will automatically re-draw
	 * if already visible.
	 *
	 * @param d
	 */
	public void setChartData(AttributeDictionary d) {

	    rawData = d;
	    if( ! apiLoaded )
	        // it will be loaded into chartDataTable later
	        return;

        chartDataTable = DataTable.create();
        chartDataTable.addColumn(ColumnType.STRING, "");
        chartDataTable.addColumn(ColumnType.NUMBER, "Percent");

        for( String attribKey : d.keySet() ) {
            if( d.isType(AttributeDictionary.DataType.dtDouble, attribKey) ) {
                chartDataTable.addRow();
                int rowPos = chartDataTable.getNumberOfRows()-1;
                chartDataTable.setValue(rowPos, 0, attribKey);
                chartDataTable.setValue(rowPos, 1, d.getDouble(attribKey));
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

        for( MapLinkedData ld : lmd ) {
            chartDataTable.addRow();
            int rowPos = chartDataTable.getNumberOfRows()-1;
            ld.rowId = rowPos;
            chartDataTable.setValue(rowPos, 0, ld.key);
            chartDataTable.setValue(rowPos, 1, ld.value);
        }
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
}
