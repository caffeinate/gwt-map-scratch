package uk.co.plogic.gwt.lib.map.overlay;

import java.util.HashMap;
import java.util.logging.Logger;

import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEvent;
import uk.co.plogic.gwt.lib.events.MapMarkerHighlightByColourEventHandler;
import uk.co.plogic.gwt.lib.map.GoogleMapAdapter;
import uk.co.plogic.gwt.lib.map.MapUtils;
import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;
import uk.co.plogic.gwt.lib.utils.AttributeDictionary;
import uk.co.plogic.gwt.lib.utils.StringUtils;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
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

	public Shapes(HandlerManager eventBus) {
		super(eventBus);

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
	}

	@Override
	public void setMap(GoogleMapAdapter mapAdapter) {
		super.setMap(mapAdapter);

		gMap.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				focusOnMarker((AbstractShapeMarker) null);
				lockedFocusMarker = false;
			}
		});
	}
	
	public void setInfoMarkerTemplate(String template) {
		markerTemplate = template;
	}

	public void addPolygon(PolygonMarker p) {
		p.setOpacity(getOpacity());
		p.setMap(gMap);
		p.setOverlay(this);
		p.setZindex(getZindex());
		markers.put(p.getId(), p);
		logger.finer("Added polygon with z-index:"+getZindex()+" to overlayId:"+overlayId);
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

		if( interactionType == UserInteraction.MOUSEMOVE ) {
			annotateMarker(targetMarker, latLng);
		}
		if( interactionType == UserInteraction.MOUSEOVER ) {
			focusOnMarker(targetMarker);
		}
		if( interactionType == UserInteraction.MOUSEOUT ) {
			focusOnMarker((AbstractShapeMarker) null);
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
		info_marker.add(h);
		double offsetX = p.getX()+25;
		info_marker.getElement().setAttribute("style",
										"left: "+offsetX+"px;top: "+p.getY()+"px;"
											 );
		info_marker.setVisible(true);

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
		return wasVisible;
	}
	
	public AbstractShapeMarker getMarker(String markerId) {
		if( ! markers.containsKey(markerId) )
			return null;

		return markers.get(markerId);
	}
	
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
