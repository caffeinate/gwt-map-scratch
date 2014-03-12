package uk.co.plogic.gwt.lib.map.overlay;

import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.plogic.gwt.lib.map.markers.AbstractShapeMarker;
import uk.co.plogic.gwt.lib.map.markers.PolygonMarker;
import uk.co.plogic.gwt.lib.map.markers.AbstractBaseMarker.UserInteraction;

import com.google.gwt.event.shared.HandlerManager;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;

public class Shapes extends AbstractOverlay {
	
	private AbstractShapeMarker currentFocusMarker = null;
	private boolean lockedFocusMarker = false;
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
				lockedFocusMarker = false;
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

			if( lockedFocusMarker ) {
				// 2nd click unselects

				if( currentFocusMarker != null && currentFocusMarker.getId().equals(markerId)) {
					focusOnMarker((AbstractShapeMarker) null);
					lockedFocusMarker = false;
					return; // don't refocus same shape
				} else {
					focusOnMarker((AbstractShapeMarker) null);
				}
			}

			lockedFocusMarker = true;
			focusOnMarker(targetMarker);

		}

		// don't clear on mouseout if marker has been set to selected
		if( lockedFocusMarker )
			return;
		
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
				currentFocusMarker.setOpacity(getOpacity());
				currentFocusMarker = targetMarker;
			}
			return;

		} else if( currentFocusMarker != null ) {
			// target and currentFocusMarker are both set
			if( currentFocusMarker.getId() == targetMarker.getId() ) {
				// nothing todo
				return;
			} else {
				// return to original opacity
				currentFocusMarker.setOpacity(getOpacity());
			}
		}
		targetMarker.setOpacity(1.0);
		currentFocusMarker = targetMarker;

	}

	@Override
	public void show() {
		super.show();
		for( Entry<String, AbstractShapeMarker> e : markers.entrySet() ) {
			e.getValue().setMap(gMap);
		}
	}

	@Override
	public void hide() {
		super.hide();
		for( Entry<String, AbstractShapeMarker> e : markers.entrySet() ) {
			e.getValue().setMap((GoogleMap) null);
		}
	}
	
	public AbstractShapeMarker getMarker(String markerId) {
		if( ! markers.containsKey(markerId) )
			return null;

		return markers.get(markerId);
	}
	

}
