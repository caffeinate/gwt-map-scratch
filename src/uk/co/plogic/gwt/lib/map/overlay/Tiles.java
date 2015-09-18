package uk.co.plogic.gwt.lib.map.overlay;

import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.comms.DropBox;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService;
import uk.co.plogic.gwt.lib.comms.GeneralJsonService.LetterBox;
import uk.co.plogic.gwt.lib.comms.envelope.PointValueEnvelope;
import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.MapUtils;
import uk.co.plogic.gwt.lib.map.markers.utils.LegendAttributes;
import uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayer;
import uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayerOptions;
import uk.co.plogic.gwt.lib.map.overlay.resources.OverlayImageResource;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;

public class Tiles extends AbstractOverlay implements GoogleTileLayerOptions.Callback,
                                                        OverlayHasLegend, DropBox {

	private String tilesUrl;
	private GoogleTileLayer layer;
	private GoogleTileLayerOptions options;
	private LegendAttributes legendAttributes;
	private String legendTitle;
	protected Logger logger = Logger.getLogger("Tiles");
	protected FlowPanel info_marker;
    protected String markerTemplate;

    // comms for 'value at co-ord' queries
    protected GeneralJsonService postalService;
    protected String queryBasePath; // will be appended with /lat/lng/
    protected LetterBox letterBox;
    protected OverlayImageResource images;

	public Tiles(HandlerManager eventBus, final String tilesUrl) {
		super(eventBus);
		this.tilesUrl = tilesUrl;
		images = GWT.create(OverlayImageResource.class);

		options = GoogleTileLayerOptions.create();
		// zooms don't seem to work
		options.setMinZoom(1);
		options.setMaxZoom(21);
		Size tileSize = Size.create(256, 256);
		options.setTileSize(tileSize);
		options.setOpacity(opacity);
		options.setGetTileUrl(this);

		layer = GoogleTileLayer.create(options);

	}

	@Override
	public void setMap(GoogleMapAdapter mapAdapter) {

		super.setMap(mapAdapter);
		if( isVisible() )
			show();

		gMap.addClickListener(new ClickHandler() {

            @Override
            public void handle(MouseEvent event) {
                showInfoMarker(event.getLatLng());
            }
        });
	}

	protected void showInfoMarker(LatLng latLng) {

        logger.fine("tiles click at ("+latLng.lat()+","+latLng.lng()+")");

        if( ! isVisible() || markerTemplate == null || queryBasePath == null) {
            if( info_marker != null )
                info_marker.setVisible(false);
            return;
        }

        postalService.setUrl(queryBasePath+latLng.lat()+'/'+latLng.lng()+'/');
        letterBox.send();

	}

	@Override
	public String handle(Point point, double zoomLevel) {
		return new StringBuilder(tilesUrl)
		        .append("/Z")
		        .append((int) zoomLevel)
		        .append("/")
		        .append((int) point.getY())
		        .append("/")
				.append((int) point.getX())
				.append(".png")
				.toString();
	}

	@Override
	public boolean show() {
		boolean wasHidden = super.show();

		// layer.setMap isn't safe to be called twice. It overlays
		// the same tileset again. So if layer is currently visible
		// and don't call it.
		if( wasHidden )
			layer.setMap(layer, gMap);

		eventBus.fireEvent(new DataVisualisationEvent(this));
		return wasHidden;
	}

	@Override
	public boolean hide() {
		boolean wasVisible = super.hide();
		layer.unsetMap(layer, gMap);
		return wasVisible;
	}

	@Override
	public void setOpacity(double opacity) {
		super.setOpacity(opacity);
		layer.setOpacity(opacity);
	}

	@Override
	public String getLegendTitle() { return legendTitle; }

	@Override
	public void setLegendTitle(String legendTitle) {
		this.legendTitle = legendTitle;
	}

	@Override
	public LegendAttributes getLegendAttributes() {	return legendAttributes; }

	@Override
	public void setLegendAttributes(LegendAttributes legendAttributes) {
		this.legendAttributes = legendAttributes;
	}

    public void setInfoMarkerTemplate(  String mapInfoMarkerTemplate,
                                        String queryBasePath) {

        markerTemplate = mapInfoMarkerTemplate;
        this.queryBasePath = queryBasePath;

        postalService = new GeneralJsonService(queryBasePath);
        postalService.setHttpMethodToGET();
        postalService.setDeliveryPoint(this);
        letterBox = postalService.createLetterBox("tiles_"+overlayId);

    }

    @Override
    public void onDelivery(String letterBoxName, String jsonEncodedPayload) {
        PointValueEnvelope env = new PointValueEnvelope();
        env.loadJson(jsonEncodedPayload);

        AttributeDictionary markerData = new AttributeDictionary();
        markerData.set("value", env.value);

        LatLng ll = LatLng.create(env.lat, env.lng);
        showInfoMarker(markerData, ll);
    }

    @Override
    public void onDeliveryProblem(String letterBoxName, int statusCode) {
        // do nothing
        logger.severe("got problem with letterBox="+letterBoxName+" code:" + statusCode);
    }

    public void showInfoMarker(AttributeDictionary markerData, LatLng latLng) {
        // see shapes.annotateMarker - much more complete version of this method
        // TODO both methods should be merged
        // there is just one tiny method call in shapes (focusOnMarker) that can't be
        // called here

        if( info_marker == null ) {
            final String mname = "marker_info_box";
            info_marker = mapAdapter.createMapOverlayPanel(mname, mname);
        }

        if( markerData == null ) {
            info_marker.setVisible(false);
            return;
        }

        String builtHtml = StringUtils.renderHtml(markerTemplate, markerData);
        Point p = MapUtils.LatLngToPixel(gMap, latLng);
        HTML h = new HTML(builtHtml);

        info_marker.clear();

        Image closeButton = new Image(images.close());
        closeButton.setStyleName("marker_info_box_close");
        closeButton.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                info_marker.setVisible(false);
            }
        });
        info_marker.add(closeButton);

        info_marker.add(h);
        double offsetX = p.getX()+10;
        double offsetY = p.getY();


        LatLngBounds mapBounds = gMap.getBounds();
        int maxX = (int) MapUtils.LatLngToPixel(gMap, mapBounds.getNorthEast()).getX();
        int maxY = (int) MapUtils.LatLngToPixel(gMap, mapBounds.getSouthWest()).getY();
        //final int margin = 200;
        //maxX -= margin;
        //maxY -= margin;

        info_marker.getElement().setAttribute(
                                        "style",
                                        "left: "+offsetX+"px;top: "+offsetY+"px;"
                                             );
        info_marker.setVisible(true);

        String msg = "Shapes info window is:"+info_marker.getOffsetWidth()+"x";
        msg += info_marker.getOffsetHeight()+" map max: "+maxX+"x"+maxY;
        msg += " using offsets:"+offsetX+"x"+offsetY;
        logger.fine(msg);


        // not sure of best strategy to keep info window visible as it changes
        // size and overflows when placed on edge of map.
        // for now, give it at least `allowedSpace` from edge.
        int info_width = info_marker.getOffsetWidth();
        int info_height = info_marker.getOffsetHeight();

        if( info_width+offsetX > maxX || info_height+offsetY > maxY ) {
            // some of info window isn't visible

            // start by positioning the info window away from the edge
            // take the worse case (biggest dimension) for the distance
            // away from the edge
            int allowedSpace = Math.max(info_width, info_height);
            offsetX = maxX-allowedSpace;
            offsetY = maxY-allowedSpace;

            msg = "Shapes info overflow #1 offsets now:"+offsetX+"x";
            msg += offsetY+" allowedSpace:"+allowedSpace;
            logger.fine(msg);

            info_marker.getElement().setAttribute(
                    "style",
                    "left: "+offsetX+"px;top: "+offsetY+"px;"
                         );

            // the info window will have redrawn so might have different
            // dimensions.
            info_width = info_marker.getOffsetWidth();
            info_height = info_marker.getOffsetHeight();

            // position it only as far as needed from edge
            offsetX = p.getX();
            if( offsetX+info_width > maxX)
                 offsetX -= info_width;

            offsetY = p.getY();
            if( offsetY+info_height > maxY)
                offsetY -= info_height;

            if( offsetX < 0 ) offsetX = 0;
            if( offsetY < 0 ) offsetY = 0;

            msg = "Shapes info overflow #2 offsets now:"+offsetX+"x"+offsetY;
            logger.fine(msg);

            info_marker.getElement().setAttribute(
                    "style",
                    "left: "+offsetX+"px;top: "+offsetY+"px;"
                         );
        }
    }

}
