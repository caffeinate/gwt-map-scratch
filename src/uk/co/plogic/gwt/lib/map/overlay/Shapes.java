package uk.co.plogic.gwt.lib.map.overlay;

import java.util.HashMap;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEventHandler;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEventHandler;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.MapUtils;
import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;
import uk.co.plogic.gwt.lib.map.overlay.resources.OverlayImageResource;
import uk.co.plogic.gwt.lib.ui.layout.ResponsiveLayoutImageResource;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;

public class Shapes extends AbstractOverlay implements OverlayHasMarkers {

	Logger logger = Logger.getLogger("overlay.Shapes");
	private AbstractShapeMarker currentFocusMarker = null;
	private boolean lockedFocusMarker = false;
	protected HashMap<String, AbstractShapeMarker> markers = new HashMap<String, AbstractShapeMarker>();
	protected FlowPanel info_marker;
	protected String markerTemplate;
	protected boolean showInfoMarkerOnMouseover = true;
	protected boolean focusOnAnyMarker = false; // ensure a marker is in focus when the dataset loads.
											    // Some datasets look better with this.
	OverlayImageResource images;

	public Shapes(HandlerManager eventBus) {
		super(eventBus);
		images = GWT.create(OverlayImageResource.class);

		eventBus.addHandler(MapMarkerHighlightByColourEvent.TYPE, new MapMarkerHighlightByColourEventHandler() {

			@Override
			public void onHighlight(MapMarkerHighlightByColourEvent e) {


				if( ! e.getOverlayId().equals(getOverlayId()) || ! isVisible() )
					return;

				boolean showHide = e.getShow();

				for( AbstractShapeMarker targetMarker : markers.values() ) {

					if( targetMarker.getFillColour().equals("#"+e.getColour())) {
						// marker with matching colour
						if( showHide )
							targetMarker.highlight();
						else
							targetMarker.unhighlight();
					}
				}
			}
		});

		eventBus.addHandler(MapMarkerHighlightByIdEvent.TYPE, new MapMarkerHighlightByIdEventHandler() {

            @Override
            public void onHighlight(MapMarkerHighlightByIdEvent e) {


                if( ! e.getOverlayId().equals(getOverlayId()) || ! isVisible() )
                    return;

                boolean showHide = e.getShow();
                if( markers.containsKey(e.getId()) ) {
                    AbstractShapeMarker targetMarker = markers.get(e.getId());
                    if( showHide )
                        targetMarker.highlight();
                    else
                        targetMarker.unhighlight();
                }
            }
        });
	}

	@Override
	public void setMap(GoogleMapAdapter mapAdapter) {
		super.setMap(mapAdapter);

		gMap.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				focusOnMarker((AbstractShapeMarker) null);
				lockedFocusMarker = false;

				if( info_marker != null )
				    info_marker.setVisible(false);
			}
		});
	}

	public void setFocusOnAnyMarker(boolean focus) {
		focusOnAnyMarker = focus;
	}

	public void setInfoMarkerTemplate(String template) {
		markerTemplate = template;
	}

	/**
	 * should the info marker follow the mouse around the map?
	 * @param b
	 */
	public void setShowInfoMarkerOnMouseover(boolean b) {
	    showInfoMarkerOnMouseover = b;
	}

	public void addPolygon(PolygonMarker p) {
		p.setOpacity(getOpacity());
		p.setMap(gMap);
		p.setOverlay(this);
		p.setZindex(getZindex());
		markers.put(p.getId(), p);
		logger.finer("Added polygon with z-index:"+getZindex()+" to overlayId:"+overlayId);

		if( focusOnAnyMarker && currentFocusMarker == null )
			focusOnMarker(p);

	}

	@Override
	public void userInteractionWithMarker(UserInteraction interactionType, String markerId, LatLng latLng) {

		logger.finer("userInteraction for markerId:"+markerId+" in layer:"+getOverlayId());

		if( ! markers.containsKey(markerId) )
			return;

		AbstractShapeMarker targetMarker = markers.get(markerId);

		// lock marker as selected
		if( interactionType == UserInteraction.CLICK ) {

			if( lockedFocusMarker ) {
				// 2nd click unselects

				if( currentFocusMarker != null && currentFocusMarker.getId().equals(markerId)) {
					focusOnMarker((AbstractShapeMarker) null);
					lockedFocusMarker = false;
					annotateMarker((AbstractShapeMarker) null, (LatLng) null);
					return; // don't refocus same shape
				} else {
					focusOnMarker((AbstractShapeMarker) null);
					annotateMarker((AbstractShapeMarker) null, (LatLng) null);
				}
			}

			lockedFocusMarker = true;
			focusOnMarker(targetMarker);
			annotateMarker(targetMarker, latLng);
		}

		// don't clear on mouseout if marker has been set to selected
		if( lockedFocusMarker )
			return;

		if( showInfoMarkerOnMouseover && interactionType == UserInteraction.MOUSEMOVE ) {
			annotateMarker(targetMarker, latLng);
		}
		if( interactionType == UserInteraction.MOUSEOVER ) {
			focusOnMarker(targetMarker);
		}
		if( interactionType == UserInteraction.MOUSEOUT ) {
			focusOnMarker((AbstractShapeMarker) null);
			if( info_marker != null ) {
			    info_marker.setVisible(false);
			}
		}

	}

	protected void focusOnMarker(AbstractShapeMarker targetMarker) {

		if( targetMarker == null )	{
			// focus on nothing
			if( currentFocusMarker != null ) {
				currentFocusMarker.unhighlight();
				currentFocusMarker = targetMarker;
			}
			return;

		} else if( currentFocusMarker != null ) {
			// target and currentFocusMarker are both set
			if( currentFocusMarker.getId() == targetMarker.getId() ) {
				// nothing todo
				return;
			} else {
				// return to original
				currentFocusMarker.unhighlight();
			}
		}
		targetMarker.highlight();
		currentFocusMarker = targetMarker;
	}

	protected void annotateMarker(AbstractShapeMarker targetMarker, LatLng latLng) {

		if( info_marker == null ) {
			final String mname = "marker_info_box";
			info_marker = mapAdapter.createMapOverlayPanel(mname, mname);
		}

		if( targetMarker == null ||  latLng == null ) {
			info_marker.setVisible(false);
			return;
		}

		AttributeDictionary markerData = getMarkerAttributes(targetMarker.getId());
		if( markerTemplate == null || markerData == null )
			return;

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
                focusOnMarker((AbstractShapeMarker) null);
                lockedFocusMarker = false;
            }
        });
		info_marker.add(closeButton);

		info_marker.add(h);
		double offsetX = p.getX()+10;
		double offsetY = p.getY();

		// not sure of best strategy to keep info window visible as it changes
		// size and overflows when placed on edge of map.
		// for now, give it at least `allowedSpace` from edge.
		final int allowedSpace = 250;
		LatLngBounds mapBounds = gMap.getBounds();
		int maxX = (int) MapUtils.LatLngToPixel(gMap, mapBounds.getNorthEast()).getX();
		int maxY = (int) MapUtils.LatLngToPixel(gMap, mapBounds.getSouthWest()).getY();

		if( offsetX+allowedSpace > maxX )
		    offsetX = maxX-allowedSpace;

		if( offsetY+allowedSpace > maxY )
            offsetY = maxY-allowedSpace;

		info_marker.getElement().setAttribute(
		                                "style",
										"left: "+offsetX+"px;top: "+offsetY+"px;"
											 );
		info_marker.setVisible(true);

		String msg = "Shapes info window is:"+info_marker.getOffsetWidth()+"x";
		msg += info_marker.getOffsetHeight()+" map max: "+maxX+"x"+maxY;
		msg += " using offsets:"+offsetX+"x"+offsetY;
		logger.finer(msg);

	}

	@Override
	public boolean show() {
		boolean wasHidden = super.show();
		for( AbstractShapeMarker marker : markers.values() ) {
			marker.setMap(gMap);
		}
		return wasHidden;
	}

	@Override
	public boolean hide() {
		boolean wasVisible = super.hide();
		for( AbstractShapeMarker marker : markers.values() ) {
			marker.setMap((GoogleMap) null);
		}

		if( info_marker != null )
			info_marker.setVisible(false);

		return wasVisible;
	}

	@Override
    public AbstractShapeMarker getMarker(String markerId) {
		if( ! markers.containsKey(markerId) )
			return null;

		return markers.get(markerId);
	}

	@Override
    public void setOpacity(double opacity) {
		super.setOpacity(opacity);
		for( AbstractShapeMarker marker : markers.values() ) {
			marker.setOpacity(opacity);
		}
	}

	@Override
	public AttributeDictionary getMarkerAttributes(String markerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
