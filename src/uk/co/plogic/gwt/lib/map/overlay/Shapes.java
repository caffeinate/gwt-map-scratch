package uk.co.plogic.gwt.lib.map.overlay;

import java.util.HashMap;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEventHandler;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByIdEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayEditModeEvent;
import uk.co.plogic.gwt.lib.events.OverlayEditModeEventHandler;
import uk.co.plogic.gwt.lib.events.OverlayFocusOnMarkerEvent;
import uk.co.plogic.gwt.lib.events.OverlayFocusOnMarkerEventHandler;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.MapUtils;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;
import uk.co.plogic.gwt.lib.map.markers.EdittableMarker;
import uk.co.plogic.gwt.lib.map.overlay.resources.OverlayImageResource;
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

public class Shapes extends AbstractOverlay implements OverlayHasMarkers, EdittableOverlay {

	Logger logger = Logger.getLogger("overlay.Shapes");
	private AbstractBaseMarker currentFocusMarker = null;
	private boolean lockedFocusMarker = false;
	protected HashMap<String, AbstractBaseMarker> markers =
	                                new HashMap<String, AbstractBaseMarker>();
	protected FlowPanel info_marker;
	protected String markerTemplate;
	protected boolean showInfoMarkerOnMouseover = true;
	protected boolean focusOnAnyMarker = false; // ensure a marker is in focus when the dataset loads.
											    // Some datasets look better with this as focusing on
	                                            // a marker activates an info panel with details of the
	                                            // selected marker.
	protected boolean editMode = false;
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

				for( AbstractBaseMarker marker : markers.values() ) {

		            if( marker instanceof AbstractShapeMarker ) {
		                AbstractShapeMarker targetMarker = (AbstractShapeMarker) marker;

    					if( targetMarker.getFillColour().equals("#"+e.getColour())) {
    						// marker with matching colour
    						if( showHide )
    							targetMarker.highlight();
    						else
    							targetMarker.unhighlight();
    					}
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
                if( markers.containsKey(e.getId())
                 && markers.get(e.getId()) instanceof AbstractShapeMarker ) {
                    AbstractShapeMarker targetMarker =
                                    (AbstractShapeMarker) markers.get(e.getId());
                    if( showHide )
                        targetMarker.highlight();
                    else
                        targetMarker.unhighlight();
                }
            }
        });

		eventBus.addHandler(OverlayFocusOnMarkerEvent.TYPE, new OverlayFocusOnMarkerEventHandler() {

            @Override
            public void onFocusOnMarker(OverlayFocusOnMarkerEvent e) {

                if( ! e.getOverlayId().equals(getOverlayId()) || ! isVisible() )
                    return;

                focusOnMarker(getMarker(e.getMarkerId()));
            }

		});

		eventBus.addHandler(OverlayEditModeEvent.TYPE, new OverlayEditModeEventHandler() {
		    @Override
		    public void onOverlayEditModeChange(OverlayEditModeEvent e) {
                if( overlayId != null
                    && overlayId.equals(e.getOverlayId()) ) {
                    setEditMode(e.editMode());
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

	public void setEditMode(boolean editMode) {
	    this.editMode = editMode;
	    logger.fine("going into edit mode with:"+markers.size());
	    for( AbstractBaseMarker marker : markers.values() ) {
	        logger.fine("edit mode for "+marker.getId());
	        // propagate edit mode
            if( marker instanceof EdittableMarker ) {
                ((EdittableMarker) marker).setEditMode(editMode);
            }
        }
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

	public void addMarker(AbstractShapeMarker m) {
		m.setOpacity(getOpacity());
		m.setMap(gMap);
		m.setOverlay(this);
		m.setZindex(getZindex());
		markers.put(m.getId(), m);
		logger.finer("Added shape with z-index:"+getZindex()+" to overlayId:"+overlayId);

		if( focusOnAnyMarker && currentFocusMarker == null )
			focusOnMarker(m);

		if( m instanceof EdittableMarker ) {
            ((EdittableMarker) m).setEditMode(editMode);
        }

	}

	public void addMarker(AbstractBaseMarker m) {
	    m.setMap(gMap);
        m.setOverlay(this);
        markers.put(m.getId(), m);
        logger.finer("Added marker id:"+m.getId()+" to overlayId:"+overlayId);

        if( focusOnAnyMarker && currentFocusMarker == null )
            focusOnMarker(m);

        if( m instanceof EdittableMarker ) {
            ((EdittableMarker) m).setEditMode(editMode);
        }
	}

	@Override
	public void userInteractionWithMarker(UserInteraction interactionType, String markerId, LatLng latLng) {

		logger.finer("userInteraction for markerId:"+markerId+" in layer:"+getOverlayId());

		if( ! markers.containsKey(markerId) )
			return;

		AbstractBaseMarker targetMarker = markers.get(markerId);

		// lock marker as selected
		if( interactionType == UserInteraction.CLICK ) {

			if( lockedFocusMarker ) {

				// 2nd click un-selects
				if( currentFocusMarker != null && currentFocusMarker.getId().equals(markerId)) {
					focusOnMarker((AbstractShapeMarker) null);
					lockedFocusMarker = false;
					annotateMarker((AbstractShapeMarker) null, (LatLng) null);
					return; // don't refocus same shape
				} else {
				    // clear which ever marker is selected
					focusOnMarker((AbstractShapeMarker) null);
					annotateMarker((AbstractShapeMarker) null, (LatLng) null);
				}
			}

			lockedFocusMarker = true;
			focusOnMarker(targetMarker);
			if( ! editMode ) {
			    annotateMarker(targetMarker, latLng);
			}
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

	protected void focusOnMarker(AbstractBaseMarker targetMarker) {

		if( targetMarker == null )	{
			// focus on nothing
			if( currentFocusMarker != null ) {
				if( currentFocusMarker instanceof AbstractShapeMarker ) {
				    ((AbstractShapeMarker) currentFocusMarker).unhighlight();
				}
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
			    if( currentFocusMarker instanceof AbstractShapeMarker ) {
                    ((AbstractShapeMarker) currentFocusMarker).unhighlight();
                }
			}
		}
		if( targetMarker instanceof AbstractShapeMarker ) {
            ((AbstractShapeMarker) targetMarker).highlight();
        }
		currentFocusMarker = targetMarker;
	}

	protected void annotateMarker(AbstractBaseMarker targetMarker, LatLng latLng) {

		if( info_marker == null ) {
			final String mname = "marker_info_box";
			info_marker = mapAdapter.createMapOverlayPanel(mname, mname);
		}

		if( targetMarker == null ||  latLng == null || markerTemplate == null ) {
			info_marker.setVisible(false);
			return;
		}

		AttributeDictionary markerData = getMarkerAttributes(targetMarker.getId());
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
                focusOnMarker((AbstractShapeMarker) null);
                lockedFocusMarker = false;
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

	@Override
	public boolean show() {
		boolean wasHidden = super.show();
		for( AbstractBaseMarker marker : markers.values() ) {
			marker.setMap(gMap);
		}
		return wasHidden;
	}

	@Override
	public boolean hide() {
		boolean wasVisible = super.hide();
		for( AbstractBaseMarker marker : markers.values() ) {
			marker.setMap((GoogleMap) null);
		}

		if( info_marker != null )
			info_marker.setVisible(false);

		return wasVisible;
	}

	@Override
    public AbstractBaseMarker getMarker(String markerId) {
		if( ! markers.containsKey(markerId) )
			return null;

		return markers.get(markerId);
	}

	@Override
    public void setOpacity(double opacity) {
		super.setOpacity(opacity);
		for( AbstractBaseMarker marker : markers.values() ) {
		    if( marker instanceof AbstractShapeMarker ) {
		        ((AbstractShapeMarker) marker).setOpacity(opacity);
		    }
		}
	}

	@Override
	public AttributeDictionary getMarkerAttributes(String markerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
