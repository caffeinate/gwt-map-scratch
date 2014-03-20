package uk.co.plogic.gwt.lib.map.overlay;

import uk.co.plogic.gwt.lib.events.DataVisualisationEvent;
import uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayer;
import uk.co.plogic.gwt.lib.map.overlay.jsni.GoogleTileLayerOptions;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Size;

public class Tiles extends AbstractOverlay implements GoogleTileLayerOptions.Callback, OverlayHasLegend {
	
	private String tilesUrl;
	private GoogleTileLayer layer;
	private GoogleTileLayerOptions options;

	public Tiles(HandlerManager eventBus, final String tilesUrl) {
		super(eventBus);
		this.tilesUrl = tilesUrl;

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

	public void setMap(GoogleMap googleMap) {

		super.setMap(googleMap);
		if( isVisible() )
			show();
	}

	@Override
	public String handle(Point point, double zoomLevel) {
		return new StringBuilder(tilesUrl).append("/Z").append((int) zoomLevel).append("/").append((int) point.getY()).append("/")
				.append((int) point.getX()).append(".png").toString();
	}

	@Override
	public void show() {
		super.show();
		layer.setMap(layer, gMap);
		eventBus.fireEvent(new DataVisualisationEvent(this));
	}

	@Override
	public void hide() {
		super.hide();
		layer.unsetMap(layer, gMap);
	}

	@Override
	public void setOpacity(double opacity) {
		super.setOpacity(opacity);
		layer.setOpacity(opacity);
	}

}
