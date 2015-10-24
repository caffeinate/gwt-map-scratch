package uk.co.plogic.gwt.lib.map.markers;

import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Rectangle;
import com.google.maps.gwt.client.Rectangle.ClickHandler;
import com.google.maps.gwt.client.Rectangle.MouseMoveHandler;
import com.google.maps.gwt.client.Rectangle.MouseOverHandler;
import com.google.maps.gwt.client.Rectangle.MouseOutHandler;
import com.google.maps.gwt.client.RectangleOptions;

/**
 * Rectangle/BoundingBox, not yet MultiRectangles
 * @author si
 *
 */
public class BoundingBoxMarker extends AbstractShapeMarker implements ShapeMarker {

	Rectangle boundingBox;
	RectangleOptions boundingBoxOpts;
	double opacity = 0.8;
	double strokeWeight = 2.0;
	Logger logger = Logger.getLogger("BoundingBoxMarker");

	/**
	 *
	 * @param gmap
	 * @param path simple path - no holes
	 */
	public BoundingBoxMarker(final HandlerManager eventBus, String uniqueIdentifier) {
		super(eventBus, uniqueIdentifier);

		boundingBoxOpts = RectangleOptions.create();
		boundingBoxOpts.setStrokeColor("000000");
		boundingBoxOpts.setStrokeOpacity(opacity);
		boundingBoxOpts.setStrokeWeight(strokeWeight);
		fillColour = "ff0000";
		boundingBoxOpts.setFillColor(fillColour);
		boundingBoxOpts.setFillOpacity(opacity);

	}

	public BoundingBoxMarker(final HandlerManager eventBus, String uniqueIdentifier,
						 String strokeColour, double strokeWeight, String fillColour ) {
		super(eventBus, uniqueIdentifier);

		boundingBoxOpts = RectangleOptions.create();
		boundingBoxOpts.setStrokeColor(strokeColour);
		boundingBoxOpts.setStrokeOpacity(opacity);
		this.strokeWeight = strokeWeight;
		boundingBoxOpts.setStrokeWeight(strokeWeight);
		this.fillColour = fillColour.toLowerCase();
		boundingBoxOpts.setFillColor(this.fillColour);
		boundingBoxOpts.setFillOpacity(opacity);

	}

	public void setCorners(LatLng south_west, LatLng north_east) {

		LatLngBounds bounds = LatLngBounds.create(south_west, north_east);
		boundingBoxOpts.setBounds(bounds);
		boundingBox = Rectangle.create(boundingBoxOpts);
		attachEventHandling();

	}

	@Override
	public void setMap(GoogleMap gMap) {
		super.setMap(gMap);
		boundingBox.setMap(gmap);
	}

	@Override
	public void show() { boundingBox.setMap(gmap); }

	@Override
	public void hide() { boundingBox.setMap((GoogleMap) null); }

	@Override
	public double getOpacity() {
		return opacity;
	}

	@Override
	public void setOpacity(double opacity) {
		this.opacity = opacity;
		boundingBoxOpts.setFillOpacity(opacity);
		boundingBoxOpts.setStrokeOpacity(opacity);
		boundingBox.setOptions(boundingBoxOpts);
	}

	private void attachEventHandling() {

		boundingBox.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				//System.out.println("click:"+event.getLatLng());
				relayUserAction(UserInteraction.CLICK, event.getLatLng());
			}
		});

		boundingBox.addMouseMoveListener(new MouseMoveHandler() {
			@Override
			public void handle(MouseEvent event) {
				relayUserAction(UserInteraction.MOUSEMOVE, event.getLatLng());
				logger.finer("Mouse move");
			}
		});

		boundingBox.addMouseOverListener(new MouseOverHandler() {
			@Override
			public void handle(MouseEvent event) {
				relayUserAction(UserInteraction.MOUSEOVER, event.getLatLng());
				logger.finer("Mouse over");
			}
		});

		boundingBox.addMouseOutListener(new MouseOutHandler() {
			@Override
			public void handle(MouseEvent event) {
				relayUserAction(UserInteraction.MOUSEOUT, event.getLatLng());
				logger.finer("Mouse out");
			}
		});
	}

	@Override
	public void highlight() {
		boundingBoxOpts.setStrokeOpacity(1.0);
		boundingBoxOpts.setStrokeWeight(strokeWeight*3);
		boundingBox.setOptions(boundingBoxOpts);
	}

	@Override
	public void unhighlight() {
		boundingBoxOpts.setStrokeOpacity(opacity);
		boundingBoxOpts.setStrokeWeight(strokeWeight);
		boundingBox.setOptions(boundingBoxOpts);
	}

	@Override
	public void setZindex(double zIndex) {
		boundingBoxOpts.setZindex(zIndex);
		if( boundingBox != null )
			boundingBox.setOptions(boundingBoxOpts);
	}

}
