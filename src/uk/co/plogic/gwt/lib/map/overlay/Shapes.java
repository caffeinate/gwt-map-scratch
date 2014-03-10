package uk.co.plogic.gwt.lib.map.overlay;

import java.util.HashMap;

import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;

public class Shapes extends AbstractOverlay {
	
	private AbstractShapeMarker lockedFocusMarker = null;
	private AbstractShapeMarker currentFocusMarker = null;
	protected HashMap<String, AbstractShapeMarker> markers = new HashMap<String, AbstractShapeMarker>();

	public Shapes(HandlerManager eventBus) {
		super(eventBus);
	}

	public void setMap(GoogleMap googleMap) {
		super.setMap(googleMap);
		
		gMap.addClickListener(new ClickHandler() {
			@Override
			public void handle(MouseEvent event) {
				focusOnMarker((AbstractShapeMarker) null);
			}
		});
	}

	public void addPolygon(PolygonMarker p) {
		p.setOpacity(getOpacity());
		p.setMap(gMap);
		p.setOverlay(this);
		markers.put(p.getId(), p);
	}

	public void userInteractionWithMarker(UserInteraction interactionType, String markerId) {

		if( ! markers.containsKey(markerId) )
			return;

		AbstractShapeMarker targetMarker = markers.get(markerId);
		
		// lock marker as selected
		if( interactionType == UserInteraction.CLICK ) {

			if( lockedFocusMarker != null && lockedFocusMarker.getId() == markerId) {
				// 2nd click unselects
				focusOnMarker((AbstractShapeMarker) null);
				return;
			} else {
				lockedFocusMarker = targetMarker;
				focusOnMarker(targetMarker);
				return;
			}
		}

		// don't clear on mouseout if marker has been set to selected
		if( lockedFocusMarker != null )
			return;
		
		if( interactionType == UserInteraction.MOUSEOVER ) {
			focusOnMarker(targetMarker);
		}
		if( interactionType == UserInteraction.MOUSEOUT ) {
			focusOnMarker((AbstractShapeMarker) null);
		}


	}
	
	protected void focusOnMarker(AbstractShapeMarker targetMarker) {
		
		if( targetMarker == null && currentFocusMarker != null ) {
			// reset opacity back to that of overlay
			currentFocusMarker.setOpacity(getOpacity());
			lockedFocusMarker = null;
		} else if ( targetMarker != null ) {
			targetMarker.setOpacity(1.0);
		}
		
		currentFocusMarker = targetMarker;

	}
	

}
