package uk.co.plogic.gwt.lib.map.overlay;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import uk.co.plogic.gwt.lib.events.OverlayVisibilityEvent;
import uk.co.plogic.gwt.lib.events.OverlayVisibilityEventHandler;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Instead of a manager to control which overlays are visible, this class
 * watches for OverlayVisibilityEvent-s and keeps track of orverlay Id
 * ready for a call to getVisibleOverlays()
 * @author si
 *
 */
public class OverlayScorecard {
	
	private Set<String> visibleOverlays = new HashSet<String>();
	private Set<String> limitToOverlays = new HashSet<String>(); // only record on scorecard these overlayIds

	public OverlayScorecard(HandlerManager eventBus) {
		
		eventBus.addHandler(OverlayVisibilityEvent.TYPE, new OverlayVisibilityEventHandler() {

			@Override
			public void onOverlayVisibilityChange(OverlayVisibilityEvent e) {

				String overlayId = e.getOverlayId();
				if( limitToOverlays.size() > 0 && ! limitToOverlays.contains(overlayId))
					return;
				
				if( e.isVisible() )
					visibleOverlays.add(overlayId);
				else if( visibleOverlays.contains(overlayId))
					visibleOverlays.remove(overlayId);
			}
		});
	}

	public String[] getVisibleOverlays() {
		
		String[] layers = visibleOverlays.toArray(new String[visibleOverlays.size()]);
		return layers;
	}

	/**
	 * If any overlays are set the scoreboard is restricted to just tracking these. If nothing is
	 * set the scoreboard tracks all overlay visibility events. 
	 * @return
	 */
	public void restrictToOverlays(Collection<? extends String> overlayIds) {
		limitToOverlays.addAll(overlayIds);
	}

	public String[] getRestrictedOverlays() {

		String[] layers = limitToOverlays.toArray(new String[limitToOverlays.size()]);
		return layers;
	}

}
